/*
 * Title:        Medicurator
 * Description:  Near duplicate detection framework for heterogeneous medical data sources
 * Licence:      Apache License Version 2.0 - http://www.apache.org/licenses/
 *
 * Copyright (c) 2016, Yiru Chen <chen1ru@pku.edu.cn>
 */
package edu.emory.bmi.medicurator.dupdetect;

import edu.emory.bmi.medicurator.general.*;
import edu.emory.bmi.medicurator.image.Image;
import edu.emory.bmi.medicurator.infinispan.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.*;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.function.Predicate;
import java.util.function.Function;
import org.infinispan.Cache;
import org.infinispan.stream.CacheCollectors;
import java.io.Serializable;


/**
 * Detect near-duplicate pairs of given images
 * Input: an array of Image ID
 * Output: an array of near-duplicate pairs of image ID
 */
public class DupDetect
{
	/**
	 * DuplicatePair detect
	 * @param imgIDs UUID[]
	 * @return Duplicatepair
     */
    public static DuplicatePair[] detect(UUID[] imgIDs)
    {
	//create a Cache to store image IDs
	Cache<UUID, Image> idCache = Manager.get().getCache(UUID.randomUUID().toString());
	for (UUID id : imgIDs)
	{
	    idCache.put(id, ID.getImage(id));
	}

		/**
		 * find the most diverse --(have the most dofferent values) keys of images' Metadata
		 * count the number of different values of each key
 		 */
	Map<String, Integer> order = idCache.entrySet().parallelStream()
	    .map((Serializable & Function<Map.Entry<UUID, Image>, Map.Entry<String, String>[]>) e ->
		    {
			Metadata meta = e.getValue().getMetadata();
			String[] keys = meta.getKeys();
			SimpleEntry[] result = new SimpleEntry[keys.length];
			for (int i = 0; i < keys.length; ++i)
			{
			    result[i] = new SimpleEntry(keys[i], meta.get(keys[i]));
			}
			return result;
		    })
	    .flatMap((Serializable & Function<Map.Entry<String, String>[], Stream<Map.Entry<String, String>>>) Arrays::stream)
	    .collect(CacheCollectors.serializableCollector(() -> Collectors.groupingBy(e -> e.getKey())))
	    .entrySet().parallelStream()
	    .map((Serializable & Function<Map.Entry<String, List<Map.Entry<String, String>>>, Map.Entry<String, Integer>>) e ->
		    {
			HashSet<String> S = new HashSet<String>();
			for (Map.Entry<String, String> s : e.getValue()) S.add(s.getValue());
			return new SimpleEntry(e.getKey(), S.size());
		    })
	    .collect(Collectors.toMap(Map.Entry<String, Integer>::getKey, Map.Entry<String, Integer>::getValue));

	//store the number of different values as a key's order
	//the larger order means more diversity
	Cache<String, Integer> metaOrder = Manager.get().getCache("metaOrder");
	for (Map.Entry<String, Integer> e : order.entrySet())
	{
	    metaOrder.put(e.getKey(), e.getValue());
	}

	//get candidate pairs from both Metadata and image data
	DuplicatePair[] candidateMeta = DetectMetadata.detect(idCache);
	DuplicatePair[] candidateImg = DetectImage.detect(idCache);

	System.out.println("Metadata candidates :");
	for (DuplicatePair dp : candidateMeta)
	{
	    System.out.println(ID.getImage(dp.first).getPath() + " == " + ID.getImage(dp.second).getPath());
	}
	System.out.println("Image candidates :");
	for (DuplicatePair dp : candidateImg)
	{
	    System.out.println(ID.getImage(dp.first).getPath() + " == " + ID.getImage(dp.second).getPath());
	}

	Cache<DuplicatePair, Integer> candidate = Manager.get().getCache(UUID.randomUUID().toString());
	HashSet<DuplicatePair> dedup = new HashSet<DuplicatePair>();

	for (DuplicatePair dp : candidateMeta) candidate.put(dp, 0);
	for (DuplicatePair dp : candidateImg) candidate.put(dp, 0);


		/**
		 * verify if the candidate pair is a real near-duplicate pair
		 */
	List<DuplicatePair> result = candidate.keySet().parallelStream()
	    .filter(dp -> Verify.verify(ID.getImage(dp.first), ID.getImage(dp.second)))
	    .collect(CacheCollectors.serializableCollector(() -> Collectors.toList()));

	candidate.stop();
	idCache.stop();
	return (DuplicatePair[])result.toArray(new DuplicatePair[0]);
    }
}

