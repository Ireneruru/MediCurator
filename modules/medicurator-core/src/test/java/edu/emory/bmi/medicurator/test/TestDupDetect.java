/*
 * Title:        Medicurator
 * Description:  Near duplicate detection framework for heterogeneous medical data sources
 * Licence:      Apache License Version 2.0 - http://www.apache.org/licenses/
 *
 * Copyright (c) 2016, Yiru Chen <chen1ru@pku.edu.cn>
 */
package edu.emory.bmi.medicurator.test;

import org.junit.Test;

import edu.emory.bmi.medicurator.general.*;
import edu.emory.bmi.medicurator.infinispan.*;
import edu.emory.bmi.medicurator.dupdetect.*;
import edu.emory.bmi.medicurator.image.*;
import java.util.ArrayList;
import java.util.UUID;
import java.io.*;

public class TestDupDetect
{
    @Test
    public void testDuplicateDetect()
    {
	UUID[] imgIDs = new UUID[14];
	for (int i = 0; i < 10; ++i)
	{
	    imgIDs[i] = (new DicomImage("/dicoms/" + i + ".dcm")).getID();
	}
	imgIDs[10] = (new DicomImage("/dicoms/1_dup.dcm")).getID();
	imgIDs[11] = (new DicomImage("/dicoms/3_dup.dcm")).getID();
	imgIDs[12] = (new DicomImage("/dicoms/7_dup.dcm")).getID();
	imgIDs[13] = (new DicomImage("/dicoms/9_dup.dcm")).getID();

	DuplicatePair[] dps = DupDetect.detect(imgIDs);

	System.out.println("Result :");
	for (int i = 0; i < dps.length; ++i)
	{
	    System.out.println(ID.getImage(dps[i].first).getPath() + "   ===   " + ID.getImage(dps[i].second).getPath());
	}
    }
}

