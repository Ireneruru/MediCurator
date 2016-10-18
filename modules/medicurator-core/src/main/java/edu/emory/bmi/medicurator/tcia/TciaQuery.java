/*
 * Title:        Medicurator
 * Description:  Near duplicate detection framework for heterogeneous medical data sources
 * Licence:      Apache License Version 2.0 - http://www.apache.org/licenses/
 *
 * Copyright (c) 2016, Yiru Chen <chen1ru@pku.edu.cn>
 */
package edu.emory.bmi.medicurator.tcia;

import java.lang.StringBuilder;
import java.util.Scanner;

import edu.emory.bmi.medicurator.Constants;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.*;
import org.apache.http.impl.client.*;
import org.apache.http.*;
import org.apache.http.conn.ssl.*;
import org.apache.http.auth.*;
import javax.net.ssl.*;
import java.security.cert.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.HttpEntity;
import java.io.InputStream;
import java.net.*;

/**
 * Build query of TCIA RESTful API, send query and get result
 * User needs to fill the TCIA_API_KEY in Constants before usage
 */
public class TciaQuery
{
    private static final String BASE_URL = "https://services.cancerimagingarchive.net/services/v3/TCIA/query/";
    private static final String API_KEY = Constants.TCIA_API_KEY;

    StringBuilder query;
    String method;

	/**
	 * create a new query with specified method(getPatient, getSeries, getImage...)
	 * @param method
     */
    public TciaQuery(String method)
    {
	query = new StringBuilder();
	this.method = method;
	String format = "format=json&";
	if (method.equals("getImage") || method.equals("getSingleImage")) 
	    format = "";
	query.append(BASE_URL + method + "?" + format + "api_key=" + API_KEY + "&");
    }

    //add a parameter
    public void param(String key, String value)
    {
	if (value == null) return;
	query.append(key + "=" + URLEncoder.encode(value) + "&");
    }

    //get the query URL
    public String getQuery()
    {
	System.out.println(query.toString());
	return query.toString();
    }

    //create a insecure ssl client
    private HttpClient getInsecureClient() throws Exception
    {
	SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
		public boolean isTrusted(X509Certificate[] chain,
		    String authType) throws CertificateException {
		return true;
		}
		}).build();
	SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
	HttpClientBuilder builder = HttpClientBuilder.create();
	builder.setSSLSocketFactory(sslsf);
	if (Constants.PROXY_HOST != null && Constants.PROXY_PORT != null)
	{
	    HttpHost proxy = new HttpHost(Constants.PROXY_HOST, Constants.PROXY_PORT, "http");
	    builder.setProxy(proxy);
	}
	if (Constants.PROXY_USERNAME != null && Constants.PROXY_PASSWORD != null)
	{
	    Credentials credentials = new UsernamePasswordCredentials(Constants.PROXY_USERNAME, Constants.PROXY_PASSWORD);
	    CredentialsProvider credsProvider = new BasicCredentialsProvider();
	    credsProvider.setCredentials(AuthScope.ANY, credentials);
	    builder.setDefaultCredentialsProvider(credsProvider);
	}
	return builder.build();
    }

    //send query and get text result
    public String getResult()
    {
	try{
	    HttpGet request = new HttpGet(getQuery());
	    HttpClient client = getInsecureClient();
	    HttpResponse response = client.execute(request);
	    HttpEntity entity = response.getEntity();
	    Scanner s = new Scanner(entity.getContent());
	    String str = s.nextLine();
	    System.out.println(str);
	    return str;
	}
	catch (Exception e) { 
	    System.out.println("[ERROR] when excute TCIA TESTful API " + getQuery() + " -- " + e);
	    return "";
	}
    }

    //send query and get raw data result
    public InputStream getRawResult()
    {
	try{
	    HttpGet request = new HttpGet(getQuery());
	    HttpClient client = getInsecureClient();
	    HttpResponse response = client.execute(request);
	    HttpEntity entity = response.getEntity();
	    return entity.getContent();
	}
	catch (Exception e) {
	    System.out.println("[ERROR] when excute TCIA TESTful API " + getQuery() + " -- " + e);
	    return null;
	}
    }
}

