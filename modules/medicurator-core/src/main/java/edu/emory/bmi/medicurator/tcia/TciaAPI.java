/*
 * Title:        Medicurator
 * Description:  Near duplicate detection framework for heterogeneous medical data sources
 * Licence:      Apache License Version 2.0 - http://www.apache.org/licenses/
 *
 * Copyright (c) 2016, Yiru Chen <chen1ru@pku.edu.cn>
 */

package edu.emory.bmi.medicurator.tcia;

import edu.emory.bmi.medicurator.general.Metadata;

import org.json.JSONObject;
import org.json.JSONArray;
import java.lang.StringBuilder;
import java.util.Scanner;
import java.io.InputStream;
import java.util.Iterator;

/**
 * Implementation of TCIA RESTful API
 */
public class TciaAPI
{
	/**
	 * parseJSON
	 * @param json String
	 * @return Metadata[]
     */
    public static Metadata[] parseJSON(String json)
    {
	JSONArray arr = new JSONArray(json);
	if (arr == null || arr.length() == 0) return new Metadata[0];
	Metadata[] metas = new Metadata[arr.length()];
	for (int i = 0; i < arr.length(); ++i)
	{
	    JSONObject obj = arr.getJSONObject(i);
	    metas[i] = new Metadata();
	    for (Iterator iter = obj.keys(); iter.hasNext(); )
	    {
		String key = (String)iter.next();
		metas[i].put(key, obj.get(key).toString());
	    }
	}
	return metas;
    }

    public static Metadata[] getCollectionValues()
    {
	TciaQuery query = new TciaQuery("getCollectionValues");
	String result = query.getResult();
	return parseJSON(result);
    }
    public static Metadata[] getModalityValues(String collection, String bodyPartExamined)
    {
	TciaQuery query = new TciaQuery("getModalityValues");
	query.param("Collection", collection);
	query.param("BodyPartExamined", bodyPartExamined);
	String result = query.getResult();
	return parseJSON(result);
    }
    public static Metadata[] getBodyPartValues(String collection, String modality)
    {
	TciaQuery query = new TciaQuery("getBodyPartValues");
	query.param("Collection", collection);
	query.param("Modality", modality);
	String result = query.getResult();
	return parseJSON(result);
    }
    public static Metadata[] getManufacturerValues(String collection, String modality)
    {
	TciaQuery query = new TciaQuery("getManufacturerValues");
	query.param("Collection", collection);
	query.param("Modality", modality);
	String result = query.getResult();
	return parseJSON(result);
    }
    public static Metadata[] getPatient(String collection)
    {
	TciaQuery query = new TciaQuery("getPatient");
	query.param("Collection", collection);
	String result = query.getResult();
	return parseJSON(result);
    }
    public static Metadata[] getPatientsByModality(String collection, String modality)
    {
	TciaQuery query = new TciaQuery("getPatientByModality");
	query.param("Collection", collection);
	query.param("Modality", modality);
	String result = query.getResult();
	return parseJSON(result);
    }
    public static Metadata[] getPatientStudy(String collection, String patientID, String studyInstanceUID)
    {
	TciaQuery query = new TciaQuery("getPatientStudy");
	query.param("Collection", collection);
	query.param("PatientID", patientID);
	query.param("StudyInstanceUID", studyInstanceUID);
	String result = query.getResult();
	return parseJSON(result);
    }
    public static Metadata[] getSeries(String collection, String patientID, String studyInstanceUID, String seriesInstanceUID , String modality, String bodyPartExamined, String manufacturerModelName, String manufacturer)
    {
	TciaQuery query = new TciaQuery("getSeries");
	query.param("Collection", collection);
	query.param("PatientID", patientID);
	query.param("StudyInstanceUID", studyInstanceUID);
	query.param("SeriesInstanceUID", seriesInstanceUID);
	query.param("Modality", modality);
	query.param("BodyPartExamined", bodyPartExamined);
	query.param("ManufacturerModelName", manufacturerModelName);
	query.param("Manufacturer", manufacturer);
	String result = query.getResult();
	return parseJSON(result);
    }
    public static Metadata[] getSeriesSize(String seriesInstanceUID)
    {
	TciaQuery query = new TciaQuery("getSeriesSize");
	query.param("SeriesInstanceUID", seriesInstanceUID);
	String result = query.getResult();
	return parseJSON(result);
    }
    public static InputStream getImage(String seriesInstanceUID)
    {
	TciaQuery query = new TciaQuery("getImage");
	query.param("SeriesInstanceUID", seriesInstanceUID);
	return query.getRawResult();
    }
    public static Metadata[] newPatientsInCollection(String date, String collection)
    {
	TciaQuery query = new TciaQuery("newPatientsInCollection");
	query.param("Date", date);
	query.param("Collection", collection);
	String result = query.getResult();
	return parseJSON(result);
    }
    public static Metadata[] newStudiesInPatientCollection(String date, String collection, String patientID)
    {
	TciaQuery query = new TciaQuery("newStudiesInPatientCollection");
	query.param("Date", date);
	query.param("Collection", collection);
	query.param("PatientID", patientID);
	String result = query.getResult();
	return parseJSON(result);
    }
    public static Metadata[] getSOPInstanceUIDs(String seriesInstanceUID)
    {
	TciaQuery query = new TciaQuery("getSOPInstanceUIDs");
	query.param("SeriesInstanceUID", seriesInstanceUID);
	String result = query.getResult();
	return parseJSON(result);
    }
    public static InputStream getSingleImage(String seriesInstanceUID, String SOPInstanceUID)
    {
	TciaQuery query = new TciaQuery("getSingleImage");
	query.param("SeriesInstanceUID", seriesInstanceUID);
	query.param("SOPInstanceUID", SOPInstanceUID);
	return query.getRawResult();
    }
}

