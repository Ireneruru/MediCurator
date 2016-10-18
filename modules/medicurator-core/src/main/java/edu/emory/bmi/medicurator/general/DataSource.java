/*
 * Title:        Medicurator
 * Description:  Near duplicate detection framework for heterogeneous medical data sources
 * Licence:      Apache License Version 2.0 - http://www.apache.org/licenses/
 *
 * Copyright (c) 2016, Yiru Chen <chen1ru@pku.edu.cn>
 */
package edu.emory.bmi.medicurator.general;

import edu.emory.bmi.medicurator.infinispan.ID;
import java.util.*;
import java.io.*;

/**
 * MediCurator can manage data of heterogeneous data sources
 * The hypothesis is that the data in each data source is managed as a tree hierarchy
 * Each tree node is a DataSet and DataSource keeps the root DataSet of the tree.
 */
public abstract class DataSource implements Serializable
{
    /**
     * the unique ID used to retrieve the DataSource inside MediCurator
     */
    private UUID dataSourceID = UUID.randomUUID();

    /**
     * get UUID of the dataSourceID
     * @return UUID of the dataSource
     */
    public UUID getID() { return dataSourceID; }

    /**
     * each DataSource has a type, such as TCIA, local ...
     */
    protected final String dataSourceType;


    /**
     * get the root DataSet
     * @return UUID of the RootDataSet
     */
    public abstract UUID getRootDataSet();

    /**
     * create a new DataSource with its type
     * @param dataSourceType
     */
    public DataSource(String dataSourceType)
    {
	this.dataSourceType = dataSourceType;
    }

    /**
     * get the DataSource's type
     * @return dataSourceType string
     */
    public String getDataSourceType()
    {
	return dataSourceType;
    }

    /**
     * store the DataSource to Infinispan
     */
    public void store()
    {
	ID.setDataSource(dataSourceID, this);
    }
}

