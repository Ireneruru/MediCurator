/*
 * Title:        Medicurator
 * Description:  Near duplicate detection framework for heterogeneous medical data sources
 * Licence:      Apache License Version 2.0 - http://www.apache.org/licenses/
 *
 * Copyright (c) 2016, Yiru Chen <chen1ru@pku.edu.cn>
 */
package edu.emory.bmi.medicurator.general;

import edu.emory.bmi.medicurator.infinispan.ID;
import java.util.UUID;
import java.util.HashSet;
import java.io.Serializable;

/**
 * A ReplicaSet is a set of DataSets.
 * One can add or remove DataSet in the ReplicaSet, and download the ReplicaSet.
 * When downloads a ReplicaSet, MediCurator will download all DataSets in it.
 */
public class ReplicaSet implements Serializable
{
    /**
     * the unique ID used to retrieve a ReplicaSet inside MediCurator
     */
    private UUID replicaSetID = UUID.randomUUID();

    /**
     * get ID
     * @return UUID
     */
    public UUID getID() { return replicaSetID; }

    /**
     * store the IDs of DataSets in this ReplicaSet
     */
    private HashSet<UUID> dataSets;

    /**
     * the name of this RelicaSet
     */
    private String name;

    /**
     * create a new empty ReplicaSet
     * @param name String name
     */
    public ReplicaSet(String name)
    {
	this.name = name;
	dataSets = new HashSet<UUID>();
	store();
    }

    /**
     * make a copy of another ReplicaSet
     * @param name String
     * @param another ReplicaSet
     */
    public ReplicaSet(String name, ReplicaSet another)
    {
	this.name = name;
	dataSets = new HashSet<UUID>(another.dataSets);
	store();
    }

    /**
     * get ReplicaSet's name
     * @return name String
     */
    public String getName()
    {
	return name;
    }

    /**
     * add a DataSet
     * @param datasetID UUID of the dataset
     */
    public void putDataSet(UUID datasetID)
    {
	dataSets.add(datasetID);
	store();
    }

    /**
     * get the array of IDs of DataSets in the ReplicaSet
     * @return UUID[] the UUID of the DataSets
     */
    public UUID[] getDataSets()
    {
	return (UUID[])dataSets.toArray(new UUID[0]);
    }

    /**
     * get the array of IDs of DataSets in the ReplicaSet
     * @param datasetID UUID
     */
    public void removeDataSet(UUID datasetID)
    {
	dataSets.remove(datasetID);
	store();
    }

    /**
     * download this ReplicaSet to local
     */
    public void download()
    {
	for (UUID id : dataSets)
	{
	    ID.getDataSet(id).download();
	}
    }

    /**
     *  store the information of this ReplicaSet to Infinispan
     */
    public void store()
    {
	ID.setReplicaSet(replicaSetID, this);
    }
}

