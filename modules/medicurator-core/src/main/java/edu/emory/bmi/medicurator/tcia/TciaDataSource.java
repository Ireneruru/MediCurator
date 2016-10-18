/*
 * Title:        Medicurator
 * Description:  Near duplicate detection framework for heterogeneous medical data sources
 * Licence:      Apache License Version 2.0 - http://www.apache.org/licenses/
 *
 * Copyright (c) 2016, Yiru Chen <chen1ru@pku.edu.cn>
 */
package edu.emory.bmi.medicurator.tcia;

import edu.emory.bmi.medicurator.infinispan.ID;
import edu.emory.bmi.medicurator.general.*;
import java.util.UUID;

/*
 * Implementation of TCIA DataSource, return a ROOT level TciaDataSet
 * Also support retrieve a TciaDataSet with Metadata
 */
public class TciaDataSource extends DataSource
{
    private  UUID rootDataSet = null;

    //DataSource type is 'tcia'
    public TciaDataSource()
    {
	super("tcia");
	rootDataSet = null;
	store();
    }

    //get a ROOT TciaDataSet
    public UUID getRootDataSet() 
    {
	if (rootDataSet == null)
	{
	    rootDataSet = (new TciaDataSet(TciaHierarchy.ROOT, null, new Metadata())).getID();
	    store();
	}
	return rootDataSet;
    }


    //retrieve a DataSet of specified Metadata
    //if the Metadata contains key 'SeriesInstanceUID' then return a SERIES DataSet
    //else if the Metadata contains key 'StudyInstanceUID' then return a STUDY DataSet
    //  ...
    //  ...
    public UUID retrieveDataSet(Metadata meta)
    {
	TciaHierarchy hier = TciaHierarchy.ROOT;
	Metadata[] metas = null;
	if (meta.contains("SeriesInstanceUID"))
	{
	    hier = TciaHierarchy.SERIES;
	    metas = TciaAPI.getSeries(null, null, null, meta.get("SeriesInstanceUID"), null, null, null, null);
	}
	else if (meta.contains("StudyInstanceUID"))
	{
	    hier = TciaHierarchy.STUDY;
	    metas = TciaAPI.getPatientStudy(null, null, meta.get("StudyInstanceUID"));
	}
	else if (meta.contains("PatnentID"))
	{
	    hier = TciaHierarchy.PATIENT;
	    metas = TciaAPI.getPatientStudy(null, null, meta.get("PatientID"));
	}
	else if (meta.contains("Collection"))
	{
	    hier = TciaHierarchy.COLLECTION;
	    metas = TciaAPI.getPatient(meta.get("Collection"));
	}
	if (metas == null) return null;

	UUID dataset = getRootDataSet();
	meta = metas[0];
	if (hier == TciaHierarchy.ROOT) return dataset;
	dataset = ((TciaDataSet)ID.getDataSet(dataset)).getSubset(meta.get("Collection")); 
	if (hier == TciaHierarchy.COLLECTION) return dataset;
	dataset = ((TciaDataSet)ID.getDataSet(dataset)).getSubset(meta.get("PatientID")); 
	if (hier == TciaHierarchy.PATIENT) return dataset;
	dataset = ((TciaDataSet)ID.getDataSet(dataset)).getSubset(meta.get("StudyInstanceUID")); 
	if (hier == TciaHierarchy.STUDY) return dataset;
	dataset = ((TciaDataSet)ID.getDataSet(dataset)).getSubset(meta.get("SeriesInstanceUID")); 
	if (hier == TciaHierarchy.SERIES) return dataset;
	return null;
    }
}

