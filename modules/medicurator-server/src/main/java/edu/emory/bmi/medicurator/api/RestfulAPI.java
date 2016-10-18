/*
 * Title:        Medicurator
 * Description:  Near duplicate detection framework for heterogeneous medical data sources
 * Licence:      Apache License Version 2.0 - http://www.apache.org/licenses/
 *
 * Copyright (c) 2016, Yiru Chen <chen1ru@pku.edu.cn>
 */
package edu.emory.bmi.medicurator.api;

import edu.emory.bmi.medicurator.general.*;
import edu.emory.bmi.medicurator.tcia.*;
import edu.emory.bmi.medicurator.infinispan.*;
import edu.emory.bmi.medicurator.image.*;
import edu.emory.bmi.medicurator.storage.*;
import edu.emory.bmi.medicurator.Constants;
import edu.emory.bmi.medicurator.dupdetect.*;
import java.lang.StringBuilder;
import java.io.*;
import java.util.*;

/**
 * excute the function of every API
 */
public class RestfulAPI
{
    //about User

    private static class JSON
    {
	StringBuilder json;
	boolean exist = false;

	public JSON()
	{
	    json = new StringBuilder(); 
	    json.append("{");
	    exist = false;
	}

	public JSON(String name, String value)
	{
	    this();
	    json.append("\"" + name + "\":\"" + value + "\"");
	    exist = true;
	}

	public void put(String name, String value)
	{
	    if (exist) json.append(",");
	    exist = true;
	    json.append("\"" + name + "\":\"" + value + "\"");
	}

	public String get()
	{
	    return json.toString() + "}";
	}
    }

    public static String signup(String username, String password) // return userid or failed
    {
	if(username == null || password == null)
	{
	    return  (new JSON("Status","Failed")).get();
	}
	else
	{
	    if(User.lookup(username) != null)
	    {
		return (new JSON("Status","Failed")).get();
	    }
	    else
	    {
		User user = new User(username,password);
		return (new JSON("userid",user.getID().toString())).get();
	    }
	}	
    }

    public static String login(String username, String password) // return userid
    {
	if(username == null || password == null)
	{
	    return (new JSON("userid","null")).get();
	}
	else
	{
	    User user = User.lookup(username);
	    if(user == null)
		return (new JSON("userid","null")).get();
	    else
	    {
		if(!user.checkPassword(password))
		{
		    return (new JSON("userid","null")).get();
		}
		else
		{
		    return (new JSON("userid",user.getID().toString())).get();
		}
	    }
	}
    }

    public static String getReplicaSets(String userid)  // return array of replicaset id 
    {
	User user = ID.getUser(UUID.fromString(userid));
	String[] a = new String[user.getReplicaSets().length];
	int i = 0;
	for (UUID id : user.getReplicaSets())
	{
	    JSON json = new JSON();
	    json.put("replicasetName", ID.getReplicaSet(id).getName());
	    json.put("replicasetID", id.toString());
	    a[i++] = json.get();
	}
	return Arrays.toString(a);

    }

    public static String createReplicaSet(String userid, String replicaName)  // return new replicaset id
    {	
	User user = ID.getUser(UUID.fromString(userid));
	ReplicaSet newSet = new ReplicaSet(replicaName);
	user.addReplicaSet(newSet.getID());
	return (new JSON("replicasetID", newSet.getID().toString())).get();
    }

    //about ReplicaSet
    public static String  getDataSets(String replicasetID)  // return array of dataset id
    {
	ReplicaSet replicaset = ID.getReplicaSet(UUID.fromString(replicasetID));
	String[] a = new String[replicaset.getDataSets().length];
	int i = 0;
	for (UUID id : replicaset.getDataSets())
	{
	    JSON json = new JSON();
	    json.put("datasetName", ID.getDataSet(id).getKeyword());
	    json.put("datasetID", id.toString());
	    a[i++] = json.get();
	}
	return Arrays.toString(a);
    }

    public static String addDataSet(String replicasetID, String datasetID)
    {
	ReplicaSet replicaset = ID.getReplicaSet(UUID.fromString(replicasetID));
	replicaset.putDataSet(UUID.fromString(datasetID));
	return (new JSON("Status","succeed")).get();
    }

    public static String removeDataSet(String replicasetID, String datasetID)
    {
	ReplicaSet replicaset = ID.getReplicaSet(UUID.fromString(replicasetID));
	replicaset.removeDataSet(UUID.fromString(datasetID));
	return (new JSON("Status","succeed")).get();
    }

    //about DataSet

    public static String getRootDataSets()
    {
	ArrayList<String> roots = new ArrayList<String>();
	for (DataSource source : Constants.DATA_SOURCES)
	{
	    JSON json = new JSON();
	    json.put("datasetName", ID.getDataSet(source.getRootDataSet()).getKeyword());
	    json.put("datasetID", source.getRootDataSet().toString());
	    roots.add(json.get());
	}
	return Arrays.toString(roots.toArray(new String[0]));
    }

    public static String getSubsets(String datasetID)  // return array of subset id 
    {
	DataSet dataset = ID.getDataSet(UUID.fromString(datasetID));
	String[] a = new String[dataset.getSubsets().length];
	int i = 0;
	for (UUID id : dataset.getSubsets())
	{
	    JSON json = new JSON();
	    json.put("datasetName", ID.getDataSet(id).getKeyword());
	    json.put("datasetID", id.toString());
	    a[i++] = json.get();
	}
	return Arrays.toString(a);
    }

    public static String downloadDataSets(String datasetID)
    {
	DataSet dataset = ID.getDataSet(UUID.fromString(datasetID));
	if(dataset.download())
	{   
	    return (new JSON("Status","Succeed")).get();
	}
	else
	{
	    return (new JSON("Status","Failled")).get();
	}
    }

    public static String downloadOneDataSet(String datasetID)
    {
	DataSet dataset = ID.getDataSet(UUID.fromString(datasetID));
	if(dataset.self_download())
	{
	    return (new JSON("Status","Succeed")).get();
	}
	else
	{
	    return (new JSON("Status","Failed")).get();
	}
    }

    public static String deleteDataSets(String datasetID)
    {
	DataSet dataset = ID.getDataSet(UUID.fromString(datasetID));
	if(dataset.delete())
	{
	    return (new JSON("Status","Succeed")).get();
	}
	else
	{
	    return (new JSON("Status","Failed")).get();
	}
    }

    public static String deleteOneDataSet(String datasetID)
    {
	DataSet dataset = ID.getDataSet(UUID.fromString(datasetID));
	if(dataset.self_delete())
	{
	    return (new JSON("Status","Succeed")).get();
	}
	else
	{
	    return (new JSON("Status","Failed")).get();
	}
    }

    public static  void add(ArrayList<UUID> images ,DataSet dataset)
    {
	if (dataset.self_downloaded)
	{
	    for(UUID id : dataset.getImages())
	    {
		images.add(id);
	    }
	}
	if (dataset.getSubsets() != null)
       {
            for(UUID id: dataset.getSubsets())
             {
                 add(images, ID.getDataSet(id));
             }
         }
    }

    public static  String DuplicateDetect(String ReplicasetID1, String ReplicasetID2)
    {
	UUID replicasetID1 = UUID.fromString(ReplicasetID1);
	UUID replicasetID2 = UUID.fromString(ReplicasetID2);
	ArrayList<UUID> images = new ArrayList<UUID>();
	ReplicaSet replicaset1 = ID.getReplicaSet(replicasetID1);
	ReplicaSet replicaset2 = ID.getReplicaSet(replicasetID2);
	for(UUID ids: replicaset1.getDataSets())
	{
	    add(images, ID.getDataSet(ids));
	}
	for(UUID ids: replicaset2.getDataSets())
	{
	    add(images, ID.getDataSet(ids));
	}
	DuplicatePair[] dpairs = DupDetect.detect(images.toArray(new UUID[0]));
        
	String[] a = new String[dpairs.length];
        int i = 0;
        for (DuplicatePair pair : dpairs)
        {

            Image img1 = ID.getImage(pair.first);
            Image img2 = ID.getImage(pair.second);
            JSON json = new JSON();
            int num = i + 1;
	    json.put("duplicate"+ num , img1.getPath());
            json.put("duplicate"+ num , img2.getPath());
            a[i++] = json.get();
	}
        return Arrays.toString(a);
    }
}

