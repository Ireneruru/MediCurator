/*
 * Title:        Medicurator
 * Description:  Near duplicate detection framework for heterogeneous medical data sources
 * Licence:      Apache License Version 2.0 - http://www.apache.org/licenses/
 *
 * Copyright (c) 2016, Yiru Chen <chen1ru@pku.edu.cn>
 */
package edu.emory.bmi.medicurator.storage;

import edu.emory.bmi.medicurator.Constants;

import java.io.*;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.hdfs.*;  

/**
 * Implementation of HDFS storage
 */
public class HdfsStorage implements Storage, Serializable
{
    private String hdfsPath = Constants.HDFS_BASEDIR;

	/**
	 * save data from InputStream to a relative path
	 * required parent directories will be created if not exists
	 * @param path String path
	 * @param in InputStream
     * @return boolean
     */
    public boolean saveToPath(String path, InputStream in)
    {
	try {
	    Configuration conf = new Configuration();
	    FileSystem hdfs = FileSystem.get(new URI(Constants.HDFS_URI), conf);
	    Path dst = new Path(hdfsPath+path);

	    //Create a new file and write data to it.
	    hdfs.mkdirs(dst.getParent());
	    FSDataOutputStream out = hdfs.create(dst, true);
	    byte[] b = new byte[1024];
	    int numBytes = 0;
	    while ((numBytes = in.read(b)) > 0) {
		out.write(b, 0, numBytes);
	    }
	    //Close all the file descripters
	    out.close();
	    hdfs.close();
	    return true;
	}
	catch (Exception e) {
	    System.out.println("[ERROR]  Exception occurs when HdfsStorage saveToPath (" + path+ ")" + e);
	}
	return false;
    }

	/**
	 * get the InputStream of a file with specified relative path
	 * @param path
	 * @return InputStream
     */
    public InputStream loadFromPath(String path)
    {
	InputStream in = null;
	try{
	    Configuration conf = new Configuration(); 
	    FileSystem hdfs = FileSystem.get(new URI(Constants.HDFS_URI), conf);
	    Path hdfspath = new Path(hdfsPath+path);
	    if (!hdfs.exists(hdfspath)) {
		System.out.println("File does not exists");
		return in; 
	    }
	    in = hdfs.open(hdfspath);
	}
	catch (Exception e) {
	    System.out.println("[ERROR] Exception occurs when HdfsStorage loadFromPath (" + path+ ")" + e);
	}
	return in;
    }
}

