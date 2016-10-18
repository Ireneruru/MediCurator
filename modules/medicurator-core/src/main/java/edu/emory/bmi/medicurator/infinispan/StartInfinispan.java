/*
 * Title:        Medicurator
 * Description:  Near duplicate detection framework for heterogeneous medical data sources
 * Licence:      Apache License Version 2.0 - http://www.apache.org/licenses/
 *
 * Copyright (c) 2016, Yiru Chen <chen1ru@pku.edu.cn>
 */
package edu.emory.bmi.medicurator.infinispan;

import org.infinispan.manager.DefaultCacheManager;

/**
 * Just start a Infinispan node
 */
public class StartInfinispan
{
    public static void main(String args[])
    {
	ID.start();
    }
}

