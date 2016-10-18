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
 * Index.
 * list all the Replicasets.
 */
public class Index extends Page
{
    protected String title = "Medicurator";

    protected  void printSection(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
    {
	new User("a", "a");
	if (request.getParameter("logout") != null || !cookie.containsKey("userid"))
	{
	    String username = request.getParameter("username");
	    String password = request.getParameter("password");

	    Cookie userid = new Cookie("userid", "");
	    userid.setMaxAge(0);

	    if (username == null || password == null)
	    {
		out.println("<form action=\"/Index\">");
		out.println("Username:<input type=\"text\" name=\"username\"><br/><br/>");
		out.println("Password:<input type=\"password\" name=\"password\"><br/><br/>");
		out.println("<input type=\"submit\" value=\"Login\"> ");
		out.println("<input type=\"button\" onclick=\"location='/Signup'\" value=\"Sign Up\">");
		out.println("</form>");
	    }
	    else
	    {
		User user = User.lookup(username);
		if (user == null)
		{
		    out.println("<h1>username does not exist</h1>");
		}
		else
		{
		    if (!user.checkPassword(password))
		    {
			out.println("<h1>password incorrect</h1>");
			out.println("<h1><a href=\"/Index\">return</a></h1>");
		    }
		    else
		    {
			userid.setValue(user.getID().toString());
			userid.setMaxAge(3600);
			out.println("<h1>log in successfully</h1>");
			out.println("<a href=\"/Index\">return</a>");
		    }
		}
	    }
	    response.addCookie(userid);
	}
	else
	{
	    User user = ID.getUser(UUID.fromString(cookie.get("userid")));
	    out.println("<h3>Here list all the Replicasets.</h3>");
	    
	    for (UUID setid : user.getReplicaSets())
	    {
	    	out.println("<a href=\"/ShowReplicaSet?replicasetID=" + setid + "\">" + ID.getReplicaSet(setid).getName() + "</a>");
		out.println("<br/>");

	    }
	    out.println("<input type=\"button\" onclick=\"location='/CreateReplicaSet'\"  value=\"Create a New Replicaset\">");
	out.println("<input type=\"button\" onclick=\"location='/DuplicateDetect'\" value=\"Duplicate Detect\" />");
	}
    }
}

