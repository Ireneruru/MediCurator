# Project Overview

MediCurator is a medical image data manager. It allows user to fetch image data from heterogeneous data sources, access them in a uniform way, and build a local image database without near duplicate images.

You can reach the document at http://medicurator.readthedocs.io/en/latest/ .

## Setting the Constants
Set the API_KEY in ./config.sh

DATA_BASE_DIR in ./src/main/java/edu/emory/bmi/medicurator/Constants.java

Also set PROXY_HOST, PROXY_PORT if necessary.

The user can choose use HdfsStorage or LocalStorage by changing STORAGE (hdfs/local) in Constants.java.
To use HDFS, you should config HDFS_URI and HDFS_BASEDIR in Constants.java.
For example, HDFS_URI = "hdfs://localhost:9000/"  and  HDFS_BASEDIR = "/user/xxx/medicurator/"


## Building and Executing Using Apache Maven 3.3.x
Building
--------
    ./compile.sh

Testing
--------
    mvn test

Run webapp
--------
    ./run_servlet.sh

It is expected to have the TCIA_API_KEY set in the Constants.java to build with tests.

The webapp runs at http://localhost:2222/Index

You may want to run

    mvn exec:java -Dexec.mainClass="edu.emory.bmi.medicurator.infinispan.StartInfinispan" 

first so that data you hava stored beforehand won't be lost although the web server has been closed.

Run Restful API
--------
    ./run_api.sh
    
    API:
    http://localhost:4567/signup?username=***&password=*** 
    http://localhost:4567/login?username=***&password=***
    http://localhost:4567/getReplicaSets?userid=***
    http://localhost:4567/createReplicaSets?userid=***&replicaName=***
    http://localhost:4567/getDataSets?replicasetID=***
    http://localhost:4567/addDataSet?replicasetID=***&datasetID=***
    http://localhost:4567/removeDataSet?replicasetID=***&datasetID=***
    http://localhost:4567/getRootDataSets
    http://localhost:4567/getSubsets?datasetID=***
    http://localhost:4567/downloadDataSets?datasetID=***
    http://localhost:4567/downloadOneDataSets?datasetID=***
    http://localhost:4567/deleteDataSets?datasetID=***
    http://localhost:4567/deleteOneDataSet?datasetID=***
    http://localhost:4567/duplicateSets?replicasetID1=***&replicasetID2=***
    
    Extend or leverage the exposed APIs, or simply test using a REST client such as the Postman Chrome application.


## Dependencies
This project depends on the below major projects.

* Infinispan 8.2 stable
* dcm4che 3
* Apache HTTP Client
* Tomcat 7
* Hadoop 2.7.2
* Spark Java 2.5

