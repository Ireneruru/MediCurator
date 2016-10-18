/*
 * Title:        Medicurator
 * Description:  Near duplicate detection framework for heterogeneous medical data sources
 * Licence:      Apache License Version 2.0 - http://www.apache.org/licenses/
 *
 * Copyright (c) 2016, Yiru Chen <chen1ru@pku.edu.cn>
 */
package edu.emory.bmi.medicurator.local;

import edu.emory.bmi.medicurator.general.*;
import edu.emory.bmi.medicurator.infinispan.*;
import edu.emory.bmi.medicurator.image.*;
import edu.emory.bmi.medicurator.storage.*;
import edu.emory.bmi.medicurator.Constants;
import java.util.*;
import java.lang.*;
import java.io.*;

/**
 * LocalDataset similar with the Dataset
 */
public class LocalDataSet extends DataSet
{
    private String source;
    private String path;

    private UUID parent;
    private UUID[] subsets;
    private UUID[] images;

    public LocalDataSet(String name, String source, String path, UUID parent)
    {
	super(name);
	this.source = source;
	this.path = path;
	this.parent = parent;
	this.subsets = null;
	this.images = null;
	store();
    }

    public UUID getParent()
    {
	return parent;
    }

    public UUID[] getSubsets()
    {
	if (subsets == null)
	{
	    try
	    {
		File file = new File(source);
		ArrayList<UUID> sets = new ArrayList<UUID>();
		if(file.isDirectory())
		{
		    String[] filelist = file.list();
		    for(int i = 0; i < filelist.length;i++)
		    {
			File readfile = new File(source + "/" + filelist[i]);
			if(readfile.isDirectory())
			{
			    LocalDataSet ReadDataSet = new LocalDataSet(datasetType, source + "/" + filelist[i], path + "/" + filelist[i], this.getID());
			    sets.add(ReadDataSet.getID());
			}
		    }
		}
		subsets = (UUID[])sets.toArray(new UUID[0]);
	    }
	    catch (Exception e) {
		System.out.println("[ERROR] when Local dataset getImages " + getID() + " -- " + e);
		subsets = new UUID[0];
	    }
	    store();
	}
	return subsets;
    }

	/**
	 * if this is a Series DataSet, download the images of the Series
	 * @return
     */
    public UUID[] getImages()
    {
	if (images == null)
	{
	    try
	    {
		File file = new File(source);
		ArrayList<UUID> imgs = new ArrayList<UUID>();
		if(file.isDirectory())
		{
		    String[] filelist = file.list();
		    for(int i = 0; i < filelist.length; i++)
		    {
			File readfile = new File(source + "/" + filelist[i]);
			if(!readfile.isDirectory())
			{
			    Image ReadImage = new DicomImage(path + "/" + filelist[i]);
			    imgs.add(ReadImage.getID());
			    FileInputStream in = new FileInputStream(readfile);
			    Storage storage = new LocalStorage();
			    storage.saveToPath(path + "/" + filelist[i] ,in);
			}
		    }
		}
		images = (UUID[])imgs.toArray(new UUID[0]);
	    }
            catch (Exception e) {
		 System.out.println("[ERROR] when Local dataset getImages " + getID() + " -- " + e);
		 images = new UUID[0];
	    }	
	    store();
	}
	return images;
    }

    public boolean updated()
    {
	return true;
    }

    public String getKeyword()
    {
	return this.datasetType + "  " + this.path;
    }
}
