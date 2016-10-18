/*
 * Title:        Medicurator
 * Description:  Near duplicate detection framework for heterogeneous medical data sources
 * Licence:      Apache License Version 2.0 - http://www.apache.org/licenses/
 *
 * Copyright (c) 2016, Yiru Chen <chen1ru@pku.edu.cn>
 */

package edu.emory.bmi.medicurator.servlet;

import edu.emory.bmi.medicurator.general.*;
import edu.emory.bmi.medicurator.tcia.*;
import edu.emory.bmi.medicurator.infinispan.*;
import edu.emory.bmi.medicurator.image.*;
import edu.emory.bmi.medicurator.storage.*;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;

/**
 * ShowReplicateSet
 */
public class ShowReplicaSet extends Page
{
    public ShowReplicaSet()
    {
	title = "ReplicaSet";
    }

    protected void printSection(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
    {
	UUID replicaSetID = UUID.fromString(request.getParameter("replicasetID"));
	UUID dataSetID = null;
	if (request.getParameter("datasetID") != null)
	{
	    dataSetID = UUID.fromString(request.getParameter("datasetID"));
	}
	UUID deleteID = null;
	if (request.getParameter("deleteID") != null)
	{
	    deleteID = UUID.fromString(request.getParameter("deleteID"));
	}
	
	ReplicaSet replicaSet = ID.getReplicaSet(replicaSetID);
	if (dataSetID != null)
	{
	    replicaSet.putDataSet(dataSetID);
	}
	if (deleteID != null)
	{
	    replicaSet.removeDataSet(deleteID);
	}
	out.println("<h1>" + replicaSet.getName() + "</h1>");
	for (UUID datasetID : replicaSet.getDataSets())
	{
	    DataSet dataSet = ID.getDataSet(datasetID);
	    out.println("<p>" + dataSet.getKeyword());
	    out.println("<input type=\"button\" onclick=\"location='/ShowReplicaSet?replicasetID=" + 
			replicaSetID + "&deleteID=" + datasetID + "'\" value=\"Remove\" />");

	    if(!dataSet.downloaded)
		out.println("<input type=\"button\" onclick=\"location='/Download?datasetID=" + dataSet.getID() + "'\" value=\"Download All\">");
	    else
		out.println("<input type=\"button\" onclick=\"location='/Download?deleteID=" + dataSet.getID() + "'\" value=\"Delete All\">");
	    if(!dataSet.self_downloaded)
		out.println("<input type=\"button\" onclick=\"location='/Download?one_datasetID=" + dataSet.getID() + "'\" value=\"Download It\">");
	    else
		out.println("<input type=\"button\" onclick=\"location='/Download?one_deleteID=" + dataSet.getID() + "'\" value=\"Delete It\">");
	    out.println("</p>");
	}
	out.println("<br />");
	out.println("<input type=\"button\" onclick=\"location='/SelectDataSet?replicasetID=" + 
			replicaSetID.toString() + "'\" value=\"Add DataSet\" />");
    }
}

