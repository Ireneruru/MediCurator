/*
 * Title:        Medicurator
 * Description:  Near duplicate detection framework for heterogeneous medical data sources
 * Licence:      Apache License Version 2.0 - http://www.apache.org/licenses/
 *
 * Copyright (c) 2016, Yiru Chen <chen1ru@pku.edu.cn>
 */

package edu.emory.bmi.medicurator.dupdetect;

import org.infinispan.Cache;
import edu.emory.bmi.medicurator.general.Metadata;
import edu.emory.bmi.medicurator.image.Image;
import java.util.*;
import java.util.function.Function;
import java.util.AbstractMap.SimpleEntry;
import java.io.Serializable;
import org.infinispan.stream.CacheCollectors;
import java.util.stream.Collectors;



/**
 * Get the candidate duplicate pair from raw image data
 */
public class DetectImage
{
    public static DuplicatePair[] detect(Cache<UUID, Image> origin)
    {
	//calculate by comparing two images' hash code
	Map<String, List<Map.Entry<String, UUID>>> candidates = 
	    origin.entrySet().parallelStream()
	    .map((Serializable & Function<Map.Entry<UUID, Image>, Map.Entry<String, UUID>>) 
		    e -> new SimpleEntry<String, UUID>(e.getValue().getHashCode(), e.getKey()))
	    .collect(CacheCollectors.serializableCollector(() -> Collectors.groupingBy(e -> e.getKey())));


	//to find images who have the same hash code and make pair
	ArrayList<DuplicatePair> result = new ArrayList<DuplicatePair>();
	for (Map.Entry<String, List<Map.Entry<String, UUID>>> e : candidates.entrySet())
	{
	    Map.Entry<String, UUID>[] dup = (Map.Entry<String, UUID>[])e.getValue().toArray(new Map.Entry[0]);
	    for (int i = 1; i < dup.length; ++i)
	    {
		result.add(new DuplicatePair(dup[0].getValue(), dup[i].getValue()));
	    }
	}
	return (DuplicatePair[])result.toArray(new DuplicatePair[0]);
    }
}

