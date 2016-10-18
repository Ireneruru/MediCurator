/*
 * Title:        Medicurator
 * Description:  Near duplicate detection framework for heterogeneous medical data sources
 * Licence:      Apache License Version 2.0 - http://www.apache.org/licenses/
 *
 * Copyright (c) 2016, Yiru Chen <chen1ru@pku.edu.cn>
 */
package edu.emory.bmi.medicurator.tcia;

import edu.emory.bmi.medicurator.infinispan.ID;
import edu.emory.bmi.medicurator.storage.*;
import edu.emory.bmi.medicurator.general.*;
import edu.emory.bmi.medicurator.image.*;
import java.util.*;
import java.util.zip.*;

/**
 * Implementation of TCIA DataSet
 * Each hierarchy has a keyword, such as PatientID of patient or StudyInstanceUID of study
 * Series contains Images, other hierarchies don't have Image
 */
public class TciaDataSet extends DataSet
{
    private Storage storage = GetStorage.get();
    private TciaHierarchy hierarchy;
    private String keyword;
    private UUID parent;
    private ArrayList<UUID> subsets;
    private UUID[] images;
    private String lastVisit;

    /* happy QiXi vacation */

    private void visit()
    {
	Date now = new Date();
	lastVisit = now.toString();
    }

    public boolean updated()
    {
	Metadata meta = getMetadata();
	switch (hierarchy)
	{
	    case COLLECTION:
		Metadata[] newPatients = TciaAPI.newPatientsInCollection(lastVisit, meta.get("Collection"));
		if (newPatients.length > 0) return false;
		break;
	    case PATIENT:
		Metadata[] newStudies = TciaAPI.newStudiesInPatientCollection(lastVisit, meta.get("Collection"), meta.get("PatientID"));
		if (newStudies.length > 0) return false;
		break;
	}
	return true;
    }

    /**
     * create a TciaDataSet with specified Metadata
     */
    public TciaDataSet(TciaHierarchy hierarchy, UUID parent, Metadata meta)
    {
	super("tcia");
	meta.store();
	setMetaID(meta.getID());
	this.hierarchy = hierarchy;
	this.parent = parent;
	subsets = new ArrayList<UUID>();
	lastVisit = "1970-01-01";
	images = null;
	switch (hierarchy)   // get keyword
	{
	    case ROOT:
		keyword = "TCIA";
		break;
	    case COLLECTION:
		keyword = "Collection: " + meta.get("Collection");
		break;
	    case PATIENT:
		keyword = "PatientID: " + meta.get("PatientID");
		break;
	    case STUDY:
		keyword = "StudyInstanceUID: " + meta.get("StudyInstanceUID");
		break;
	    case SERIES:
		keyword = "SeriesInstanceUID: " + meta.get("SeriesInstanceUID");
		break;
	}
	store();
    }

    public UUID getParent()
    {
	return parent;
    }

    /**
     * create this DataSet's subsets
     * @param metas metas
     * @param subHierarchy
     */
    private void makeSubsets(Metadata[] metas, TciaHierarchy subHierarchy)
    {
	if (metas.length == 0) return;
	for (int i = 0; i < metas.length; ++i)
	{
	    DataSet subset = new TciaDataSet(subHierarchy, getID(), metas[i]);
	    subsets.add(subset.getID());
	}
    }


    /**
     * get ID array of subsets
     * @return UUID[]
     */
    public UUID[] getSubsets()
    {
	if (hierarchy == TciaHierarchy.SERIES)
	{
	    return null;
	}
	if (subsets.size() == 0)
	{
	    Metadata meta = getMetadata();
	    Metadata[] metas;
	    switch (hierarchy)
	    {
		case ROOT:
		    metas = TciaAPI.getCollectionValues();
		    makeSubsets(metas, TciaHierarchy.COLLECTION);
		    break;
		case COLLECTION:
		    metas = TciaAPI.getPatient(meta.get("Collection"));
		    makeSubsets(metas, TciaHierarchy.PATIENT);
		    break;
		case PATIENT:
		    metas = TciaAPI.getPatientStudy(meta.get("Collection"), meta.get("PatientID"), null);
		    makeSubsets(metas, TciaHierarchy.STUDY);
		    break;
		case STUDY:
		    metas = TciaAPI.getSeries(meta.get("Collection"), meta.get("PatientID"), meta.get("StudyInstanceUID"), null, null, null, null, null);
		    makeSubsets(metas, TciaHierarchy.SERIES);
		    break;
	    }
	    store();
	}
	else if (!updated())
	{
	    Metadata meta = getMetadata();
	    Metadata[] newcoming, metas;
	    if (hierarchy == TciaHierarchy.COLLECTION)
	    {
		newcoming = TciaAPI.newPatientsInCollection(lastVisit, meta.get("Collection"));
		metas = TciaAPI.getPatient(meta.get("Collection"));
		for (Metadata m : newcoming)
		{
		    for (Metadata m2 : metas)
		    {
			if (m.get("PatientID").equals(m2.get("PatientID")))
			{
			    DataSet subset = new TciaDataSet(TciaHierarchy.PATIENT, getID(), m2);
			    subsets.add(subset.getID());
			    break;
			}
		    }
		}
	    }
	    else if (hierarchy == TciaHierarchy.PATIENT)
	    {
		newcoming = TciaAPI.newStudiesInPatientCollection(lastVisit, meta.get("Collection"), meta.get("PatientID"));
		for (Metadata m : newcoming)
		{
		    metas = TciaAPI.getPatientStudy(meta.get("Collection"), meta.get("PatientID"), m.get("StudyInstanceUID"));
		    DataSet subset = new TciaDataSet(TciaHierarchy.STUDY, getID(), metas[0]);
		    subsets.add(subset.getID());
		}
	    }
	}
	visit();
	return subsets.toArray(new UUID[0]);
    }

    /**
     * if this is a Series DataSet, download the images of the Series
     * the downloaded images are compressed with zip, unzip them and store
     * @return UUID[]
     */
    public UUID[] getImages()
    {
	if (images == null)	
	{
	    if (hierarchy != TciaHierarchy.SERIES)
	    {
		images = new UUID[0];
	    }
	    else
	    {
		Metadata meta = getMetadata();
		ArrayList<UUID> imgs = new ArrayList<UUID>();
		try {
		    String root = "/tcia/" + meta.get("Collection") + "/" + ID.getDataSet(getParent()).getMetadata().get("PatientID")
			+ "/" + meta.get("StudyInstanceUID") + "/" + meta.get("SeriesInstanceUID") + "/";
		    ZipInputStream zip = new ZipInputStream(TciaAPI.getImage(meta.get("SeriesInstanceUID")));
		    ZipEntry ze;
		    while ((ze = zip.getNextEntry()) != null)
		    {
			if (ze.isDirectory()) continue;
			String path = root + ze.getName();
			storage.saveToPath(path, zip);
			System.out.println(ze.getName());
			Image img = new DicomImage(path);
			imgs.add(img.getID());
		    }
		    images = (UUID[])imgs.toArray(new UUID[0]);
		}
		catch (Exception e) {
		    System.out.println("[ERROR] when TCIA dataset getImages " + getID() + " -- " + e);
		    images = new UUID[0];
		}
	    }
	    store();
	}
	return images;
    }

    public String getKeyword()
    {
	return keyword;
    }

    /**
     * get a subset with specified keyword
     * @param keyword
     * @return
     */
    public UUID getSubset(String keyword)
    {
	for (UUID u : getSubsets())
	{
	    if (((TciaDataSet)ID.getDataSet(u)).getKeyword().equals(keyword))
	    {
		return u;
	    }
	}
	return null;
    }
}

