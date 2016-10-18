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
 * Download Page
 */
public class Download extends Page
{
    public Download()
    {
	title = "Download";
    }

    protected void printSection(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
    {
	if (request.getParameter("datasetID") != null)
	{
	    UUID datasetID = UUID.fromString(request.getParameter("datasetID"));
	    DataSet dataset = ID.getDataSet(datasetID);
	    dataset.download();
	    out.println("<h1>" + dataset.getKeyword() + " and its subsets downloaded</h1>");
	}
	else if (request.getParameter("deleteID") != null)
	{
	    UUID datasetID = UUID.fromString(request.getParameter("deleteID"));
	    DataSet dataset = ID.getDataSet(datasetID);
	    dataset.delete();
	    out.println("<h1>" + dataset.getKeyword() + " and its subsets deleted</h1>");
	}
	else if (request.getParameter("one_datasetID") != null)
	{
	    UUID datasetID = UUID.fromString(request.getParameter("one_datasetID"));
	    DataSet dataset = ID.getDataSet(datasetID);
	    dataset.self_download();
	    out.println("<h1>" + dataset.getKeyword() + " downloaded</h1>");
	}
	else if (request.getParameter("one_deleteID") != null)
	{
	    UUID datasetID = UUID.fromString(request.getParameter("one_deleteID"));
	    DataSet dataset = ID.getDataSet(datasetID);
	    dataset.self_delete();
	    out.println("<h1>" + dataset.getKeyword() + " deleted</h1>");
	}
    }
}

