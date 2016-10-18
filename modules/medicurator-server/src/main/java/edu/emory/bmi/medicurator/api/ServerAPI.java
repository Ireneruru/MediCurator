/*
 * Title:        Medicurator
 * Description:  Near duplicate detection framework for heterogeneous medical data sources
 * Licence:      Apache License Version 2.0 - http://www.apache.org/licenses/
 *
 * Copyright (c) 2016, Yiru Chen <chen1ru@pku.edu.cn>
 */
package edu.emory.bmi.medicurator.api;

import edu.emory.bmi.medicurator.api.RestfulAPI;

import static spark.Spark.get;
import static spark.Spark.post;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * List Restful API format
 */
public class ServerAPI
{
    public static void main( String[] args) {
	get("/signup", (req, res) -> {
		String username = req.queryParams("username");
		String password = req.queryParams("password");
		return RestfulAPI.signup(username, password);
		});
	get("/login", (req, res) -> {
		String username = req.queryParams("username");
		String password = req.queryParams("password");
		return RestfulAPI.login(username, password);
		});
	get("/getReplicaSets", (req, res) -> {
		String userid = req.queryParams("userid");
		return RestfulAPI.getReplicaSets(userid);
		});
	get("/createReplicaSet", (req, res) -> {
		String userid = req.queryParams("userid");
		String replicaName = req.queryParams("replicaName");
		return RestfulAPI.createReplicaSet(userid, replicaName);
		});
	get("/getDataSets", (req, res) -> {
		String replicasetID = req.queryParams("replicasetID");
		return RestfulAPI.getDataSets(replicasetID);
		});
	get("/addDataSet", (req, res) -> {
		String replicasetID = req.queryParams("replicasetID");
		String datasetID = req.queryParams("datasetID");
		return RestfulAPI.addDataSet(replicasetID, datasetID);
		});
	get("/removeDataSet", (req, res) -> {
		String replicasetID = req.queryParams("replicasetID");
		String datasetID = req.queryParams("datasetID");
		return RestfulAPI.removeDataSet(replicasetID, datasetID);
		});
	get("/getRootDataSets", (req, res) -> {
		return RestfulAPI.getRootDataSets();
		});
	get("/getSubsets", (req, res) -> {
		String datasetID = req.queryParams("datasetID");
		return RestfulAPI.getSubsets(datasetID);
		});
	get("/downloadDataSets", (req, res) -> {
		String datasetID = req.queryParams("datasetID");
		return RestfulAPI.downloadDataSets(datasetID);
		});
	get("/downloadOneDataSet", (req, res) -> {
		String datasetID = req.queryParams("datasetID");
		return RestfulAPI.downloadOneDataSet(datasetID);
		});
	get("/deleteDataSets", (req, res) -> {
		String datasetID = req.queryParams("datasetID");
		return RestfulAPI.deleteDataSets(datasetID);
		});
	get("/deleteOneDataSet", (req, res) -> {
		String datasetID = req.queryParams("datasetID");
		return RestfulAPI.deleteOneDataSet(datasetID);
		});
	get("/duplicateSets", ( req, res) -> {
		String replicasetID1 = req.queryParams("replicasetID1");
		String replicasetID2 = req.queryParams("replicasetID2");
		return RestfulAPI.DuplicateDetect(replicasetID1,replicasetID2);
	});
    }
}

