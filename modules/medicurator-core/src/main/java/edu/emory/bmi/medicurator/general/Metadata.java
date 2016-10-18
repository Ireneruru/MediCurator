/*
 * Title:        Medicurator
 * Description:  Near duplicate detection framework for heterogeneous medical data sources
 * Licence:      Apache License Version 2.0 - http://www.apache.org/licenses/
 *
 * Copyright (c) 2016, Yiru Chen <chen1ru@pku.edu.cn>
 */

package edu.emory.bmi.medicurator.general;

import edu.emory.bmi.medicurator.infinispan.ID;
import java.util.HashMap;
import java.io.Serializable;
import java.util.UUID;

/**
 * The uniform way to represent the meta information/attributes.<br>
 * A Metadata is a collection of String key-value pairs
 * Metadata is also able to store flags with key equals the flag and value equals ""
 */
public class Metadata implements Serializable
{
    /**
     * the unique ID used to retrieve the Metadata inside MediCurator
     */
    private UUID metaID = UUID.randomUUID();

    /**
     * get the MetaID
     * @return UUID
     */
    public UUID getID() { return metaID; }

    /**
     * store the String key-value pairs
     */
    public HashMap<String, String> keyValues;

    /**
     * create a new empty Metadata
     */
    public Metadata()
    {
	keyValues = new HashMap<String, String>();
    }


    /**
     * make a copy from another Metadata
     * @param another Metadata
     */
    public Metadata(Metadata another)
    {
	keyValues = new HashMap<String, String>(another.keyValues);
    }

    /**
     * store a flag
     * @param word key
     */
    public void put(String word)
    {
	keyValues.put(word, "");
    }

    /**
     * store a key-value pair
     * @param key the key string
     * @param value the value string
     */
    public void put(String key, String value)
    { 
	if (value == null) value = ""; 
	keyValues.put(key, value); 
    }

    /**
     * check if the key exists in the Metadata
     * @param key the key String
     * @return boolean
     */
    public boolean contains(String key) 
    {
	return keyValues.containsKey(key);
    }

    /**
     * get the corresponding value of a key
     * @param key string
     * @return the value string
     */
    public String get(String key)
    {
	return keyValues.get(key);
    }

    /**
     * get a String array consists of all the keys in the Metadata
     * @return String[]
     */
    public String[] getKeys()
    {
	return (String[])keyValues.keySet().toArray(new String[0]); 
    }

    /**
     * store the Metadata to Infinispan
     */
    public void store()
    {
	ID.setMetadata(metaID, this);
    }
}

