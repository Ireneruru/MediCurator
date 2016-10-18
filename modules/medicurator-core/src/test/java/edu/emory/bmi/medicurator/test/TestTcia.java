/*
 * Title:        Medicurator
 * Description:  Near duplicate detection framework for heterogeneous medical data sources
 * Licence:      Apache License Version 2.0 - http://www.apache.org/licenses/
 *
 * Copyright (c) 2016, Yiru Chen <chen1ru@pku.edu.cn>
 */
package edu.emory.bmi.medicurator.test;

import edu.emory.bmi.medicurator.general.*;
import edu.emory.bmi.medicurator.tcia.*;
import edu.emory.bmi.medicurator.infinispan.*;
import edu.emory.bmi.medicurator.image.*;
import edu.emory.bmi.medicurator.storage.*;

import org.junit.Test;

public class TestTcia
{
    public void printMetadata(Metadata meta)
    {
	for (String key : meta.getKeys())
	{
	    System.out.println(key + " : " + meta.get(key));
	}
    }

    @Test
    public void testTcia()
    {
	TciaDataSource source = new TciaDataSource();
	TciaDataSet root = (TciaDataSet)ID.getDataSet(source.getRootDataSet());

	System.out.println(" ======= Create a new TCIA DataSource");

	TciaDataSet collection = (TciaDataSet)ID.getDataSet(root.getSubsets()[0]);

	System.out.println(" ======= Test collection " + collection.getKeyword());
	printMetadata(collection.getMetadata());


	TciaDataSet patient = (TciaDataSet)ID.getDataSet(collection.getSubsets()[0]);

	System.out.println(" ======= Test patient " + patient.getKeyword());
	printMetadata(patient.getMetadata());

	TciaDataSet study = (TciaDataSet)ID.getDataSet(patient.getSubsets()[0]);

	System.out.println(" ====== Test study " + study.getKeyword());
	printMetadata(study.getMetadata());

	TciaDataSet series = (TciaDataSet)ID.getDataSet(study.getSubsets()[0]);
	System.out.println(" ====== Test series " + study.getKeyword());
	printMetadata(study.getMetadata());

	System.out.println(" ====== Test download series");
	series.download();
	System.out.println(" ====== Test download tracking");
	series.download();

	System.out.println("Finish");
    }
}

