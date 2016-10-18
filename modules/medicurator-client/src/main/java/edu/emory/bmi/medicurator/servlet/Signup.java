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

import edu.emory.bmi.medicurator.general.*;
import edu.emory.bmi.medicurator.tcia.*;
import edu.emory.bmi.medicurator.infinispan.*;
import edu.emory.bmi.medicurator.image.*;
import edu.emory.bmi.medicurator.storage.*;

/**
 * Signup
 */
public class Signup extends Page {

    public Signup()
    {
	title = "Sign up";
    }

    protected  void printSection(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
    {
	if (request.getParameter("username") == null || request.getParameter("password") == null)
	{
	    out.println("<form action=\"/Signup\" method=\"get\">");
	    out.println("Username:<br/> <input type=\"text\" name=\"username\" /> <br/><br/>");
	    out.println("Password:<br/> <input type=\"password\" name=\"password\" /> <br/><br/>");
	    out.println("Password:<br/> <input type=\"password\" name=\"password\" /> <br/><br/>");
	    out.println("<input type=\"submit\" name=\"Submit\" value=\"Submit\" />");
	    out.println("</form>");
	}
	else
	{
	    String username = request.getParameter("username");
	    String password = request.getParameter("password");
	    if (User.lookup(username) != null)
	    {
		out.println("<h1>username has been existed</h1>");
	    }
	    else
	    {
		new User(username, password);
		out.println("<h1>Congratulations to be one of our menber!!!</h1>");
		out.println("<h1>Back to <a href=\"/Index\">Homepage</a></h1>");
	    }
	}
    }
}

