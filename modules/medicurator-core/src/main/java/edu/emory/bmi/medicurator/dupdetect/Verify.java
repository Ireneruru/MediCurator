/*
 * Title:        Medicurator
 * Description:  Near duplicate detection framework for heterogeneous medical data sources
 * Licence:      Apache License Version 2.0 - http://www.apache.org/licenses/
 *
 * Copyright (c) 2016, Yiru Chen <chen1ru@pku.edu.cn>
 */
package edu.emory.bmi.medicurator.dupdetect;

import edu.emory.bmi.medicurator.general.Metadata;
import edu.emory.bmi.medicurator.image.Image;
import java.util.AbstractMap.SimpleEntry;
import java.util.function.Function;
import java.util.*;

/**
 * Verify if a candidate pair is real near-duplicate pair
 * (1) if the raw image data's hash code equals, it is true
 * (2) compare all the metadata if there are less than five different metadata items, it is true
 */
public class Verify
{
    public static boolean verify(Image a, Image b)
    {
	try {
	    if (a.getID().equals(b.getID())) return false;
	    if (a.getHashCode().equals(b.getHashCode()))
	    {
		return true;
	    }
	    Metadata metaA = a.getMetadata();
	    Metadata metaB = b.getMetadata();
	    int count = 0;
	    for (String key : metaA.getKeys())
	    {
		if (!metaB.contains(key) || !metaB.get(key).equals(metaA.get(key)))
		    count++;
	    }
	    for (String key : metaB.getKeys())
	    {
		if (!metaA.contains(key))
		    count++;
	    }
	    if (count < 5) 
	    {
		return true;
	    }
	}
	catch (Exception e) {
	    System.out.println("[ERROR] when Verify image " + a.getPath() + " and image " + b.getPath() + " -- " + e);
	}
	return false;
    }
}

