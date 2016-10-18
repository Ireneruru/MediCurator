/*
 * Title:        Medicurator
 * Description:  Near duplicate detection framework for heterogeneous medical data sources
 * Licence:      Apache License Version 2.0 - http://www.apache.org/licenses/
 *
 * Copyright (c) 2016, Yiru Chen <chen1ru@pku.edu.cn>
 */
package edu.emory.bmi.medicurator.infinispan;

import org.infinispan.manager.DefaultCacheManager;
import java.io.IOException;

/**
 * the global shared DefaulCacheManager
 */
public class Manager
{
    private static DefaultCacheManager manager = null;

    public static DefaultCacheManager get()
    {   
	while (manager == null)
	{
	    try {
		manager = new DefaultCacheManager("infinispan.xml");
	    }
	    catch (IOException e) {
		System.out.println("[ERROR] get DefaultCacheManager error -- " + e);
	    }
	}
	return manager;
    }
}

