/*
 * Title:        Medicurator
 * Description:  Near duplicate detection framework for heterogeneous medical data sources
 * Licence:      Apache License Version 2.0 - http://www.apache.org/licenses/
 *
 * Copyright (c) 2016, Yiru Chen <chen1ru@pku.edu.cn>
 */
package edu.emory.bmi.medicurator.servlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;

import edu.emory.bmi.medicurator.general.*;
import edu.emory.bmi.medicurator.tcia.*;
import edu.emory.bmi.medicurator.infinispan.*;
import edu.emory.bmi.medicurator.image.*;
import edu.emory.bmi.medicurator.storage.*;
import edu.emory.bmi.medicurator.dupdetect.*;
import edu.emory.bmi.medicurator.*;

/**
 * Duplicate Page
 */
public class DuplicateDetect extends Page 
{
    public DuplicateDetect(){
	title = "Duplicate Detection";
    }

    public void add(ArrayList<UUID> images ,DataSet dataset)
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

    protected  void printSection(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
    {
	User user = ID.getUser(UUID.fromString(cookie.get("userid")));
	Enumeration replicaIDs = request.getParameterNames();

	if (replicaIDs.hasMoreElements())
	{
	    ArrayList<UUID> images = new ArrayList<UUID>();
	    while (replicaIDs.hasMoreElements())
	    {
		String id = (String)replicaIDs.nextElement();
		UUID replicaID = UUID.fromString(id);
		ReplicaSet replicaset = ID.getReplicaSet(replicaID);
		for(UUID ids : replicaset.getDataSets())
		{
		    add(images, ID.getDataSet(ids));
		}
	    }
	    DuplicatePair[] dpairs = DupDetect.detect(images.toArray(new UUID[0]));
	    out.println("<h1>Near Duplicate pairs</h1>");
	    for (DuplicatePair pair : dpairs)
	    {
		out.println("<ul>");
		Image img1 = ID.getImage(pair.first);
		Image img2 = ID.getImage(pair.second);
		out.println("<li>" + img1.getPath() + "</li>");
		out.println("<li>" + img2.getPath() + "</li>");
		out.println("</ul>");
		out.println("<br />");
	    }
	}
	else
	{
	    out.println("<form action = \"/DuplicateDetect\" method = \"get\" />");
	    for(UUID id : user.getReplicaSets())
	    {
		out.println("<input type=\"checkbox\" name=\"" + id.toString() + "\" />");
		out.println(ID.getReplicaSet(id).getName());
		out.println("<br />");
	    }
	    out.println("<input type=\"submit\" value=\"Detect\" />");
	    out.println("</form>");
	    }
    }
}

