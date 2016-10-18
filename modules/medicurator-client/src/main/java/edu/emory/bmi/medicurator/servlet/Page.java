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
 * The CSS style
 */
public abstract class Page extends HttpServlet
{
    protected String title = "Medicurator";
    protected String info = "Contact: chen1ru@pku.edu.cn";

    protected String header()
    {
	return "<!DOCTYPE html>" +
	    "<html lang=\"en-us\">" +
	    "<head>" +
	    "<style>" +
	    "#header {" +
	    "	background-color:black;" +
	    "	height:30%;" +
	    "	color:white;" +
	    "	text-align:center; padding:15px;" +
	    "	font-family:verdana }" +
	    "#nav {" +
	    "	line-height:30px;" +
	    "	background-color:#eeeeee;" +
	    "	text-align:center;"+
	    "	padding:15px; }" +
	    "#section {" +
	    "	padding:15px; }" +
	    "#footer {" +
	    "	background-color:black;" +
	    "	color:white; clear:both;" +
	    "	text-align:center;" +
	    "	padding:15px; }" +
	    "</style>" +
	    "</head>" +
	    " <body> " +
	    "<div id=\"header\"> <h1>" + title + "</h1> </div>";
    }

    protected String footer()
    {
	return "<div id=\"footer\">" + info + "</div> </body> </html>";
    }

    protected HashMap<String, String> cookie;

    protected abstract void printSection(HttpServletRequest request, HttpServletResponse response, PrintWriter out);

    public void doGet( HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
	response.setContentType("text/html;charset=UTF-8");
	PrintWriter out = response.getWriter();
	Cookie[] cookies = request.getCookies();
	cookie  = new HashMap<String, String>();
	if (cookies != null)
	{
	    for (int i = 0; i < cookies.length; i++)
	    {
		cookie.put(cookies[i].getName(), cookies[i].getValue());
	    }
	}

//	try 
//	{
	    out.println(header());
	    out.println("<table border=\"0\" id=\"content\">");
	    out.println("<tr>");
	    out.println("<td id=\"nav\" width=\"20%\">");
	    if (request.getParameter("logout") == null && cookie.containsKey("userid"))
	    {
		User user = ID.getUser(UUID.fromString(cookie.get("userid")));
		if (user != null)
		{
		    out.println("<p style=\"font-size:120%;\">Dear <a href=\"/Index\"><i>" + user.getUsername() + "</i></a>");
		    out.println("<br/>Welcome to Medicurator!</p>");
		    out.println("<br/><br/><br/><br/><br/>");
		    out.println("<a href=\"/Index?logout\">");
		    out.println("<img src=\"Logout.jpg\" alt=\"Logout\" style=\"width:64px;height:64px;border:0;\">");
		    out.println("</a>");
		}
		else
		{
		    cookie.remove("userid");
		}
	    }
	    out.println("</td>");
	    out.println("<td id=\"section\" width=\"80%\">");
	    printSection(request, response, out);
	    out.println("<td>");
	    out.println("<tr>");
	    out.println("</table>");
	    out.println(footer());
//	}
//	catch (Exception e)
//	{
//	    out.println(e);
//	}
    }
}

