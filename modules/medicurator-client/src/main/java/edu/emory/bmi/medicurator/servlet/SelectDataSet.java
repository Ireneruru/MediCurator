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
import edu.emory.bmi.medicurator.*;

/**
 * SelectDataSet Page
 */
public class SelectDataSet extends Page 
{
    public SelectDataSet()
    {
	title = "Select DataSet";
    }

    protected  void printSection(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
    {
	UUID replicaSetID = UUID.fromString(request.getParameter("replicasetID"));
	if (request.getParameter("datasetID") == null)
	{
	    for (DataSource source : Constants.DATA_SOURCES)
	    {
		DataSet dataset = ID.getDataSet(source.getRootDataSet());
		out.println("<p>");
		out.println("<a href=\"SelectDataSet?replicasetID=" + replicaSetID.toString() + 
			"&datasetID=" + dataset.getID().toString() + "\">" + dataset.getKeyword() + "</a>");
		out.println("</p>");
	    }
	    out.println("<h3><a href=\"ShowReplicaSet?replicasetID=" + replicaSetID.toString() + "\">return</a></h3>");
	}
	else
	{
	    UUID dataSetID = UUID.fromString(request.getParameter("datasetID"));

	    DataSet dataset = ID.getDataSet(dataSetID);
	    out.println("<h1>" + dataset.getKeyword() + "</h1>");

	    UUID[] subsets = dataset.getSubsets();
	    if (subsets == null)
	    {
		out.println("<h3>No subset</h3>");
	    }
	    else
	    {
		for (UUID id : subsets)
		{
		    out.println("<table border=\"0\">");
		    DataSet subset = ID.getDataSet(id);
		    out.println("<tr>");
		    out.println("<td width=\"10%\">"); 
		    out.println("<a href=\"ShowReplicaSet?replicasetID=" + replicaSetID.toString() + 
			    "&datasetID=" + id.toString() + "\">ADD</a>");
		    out.println("</td>"); 
		    out.println("<td>");
		    out.println("<a href=\"SelectDataSet?replicasetID=" + replicaSetID.toString() + 
			    "&datasetID=" + id.toString() + "\">" + subset.getKeyword() + "</a>");
		    out.println("</td>");
		    out.println("</tr>");
		}
		out.println("</table>");
	    }
	    out.print("<a href=\"SelectDataSet?replicasetID=" + replicaSetID);
	    if (dataset.getParent() != null) out.print("&datasetID=" + dataset.getParent()); 
	    out.println("\">return</a>");
	}
    }
}

