/*
 * Title:        Medicurator
 * Description:  Near duplicate detection framework for heterogeneous medical data sources
 * Licence:      Apache License Version 2.0 - http://www.apache.org/licenses/
 *
 * Copyright (c) 2016, Yiru Chen <chen1ru@pku.edu.cn>
 */
package edu.emory.bmi.medicurator.local;

import edu.emory.bmi.medicurator.general.*;
import java.util.*;

/**
 * LocalDataSource
 */
public class LocalDataSource extends DataSource
{
    private UUID rootDataSet = null;
    public String root;

    public LocalDataSource(String name, String root)
    {
	super("local." + name);
	this.root = root;
	store();
    }

    public UUID getRootDataSet()
    {
	if (rootDataSet == null)
	{
	    rootDataSet = (new LocalDataSet(dataSourceType, root, "/" + dataSourceType, null)).getID();
	    store();
	}
	return rootDataSet;
    }
}

