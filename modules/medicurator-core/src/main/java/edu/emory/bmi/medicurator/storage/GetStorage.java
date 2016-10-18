/*
 * Title:        Medicurator
 * Description:  Near duplicate detection framework for heterogeneous medical data sources
 * Licence:      Apache License Version 2.0 - http://www.apache.org/licenses/
 *
 * Copyright (c) 2016, Yiru Chen <chen1ru@pku.edu.cn>
 */
package edu.emory.bmi.medicurator.storage;

import java.io.InputStream;
import edu.emory.bmi.medicurator.Constants;

/**
 * getStorage: local hdfs
 */
public class GetStorage
{
    public static Storage get()
    {
	if (Constants.STORAGE.equals("local"))
	    return new LocalStorage();
	if (Constants.STORAGE.equals("hdfs"))
	    return new HdfsStorage();
	return null;
    }
}
