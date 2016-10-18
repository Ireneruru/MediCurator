/*
 * Title:        Medicurator
 * Description:  Near duplicate detection framework for heterogeneous medical data sources
 * Licence:      Apache License Version 2.0 - http://www.apache.org/licenses/
 *
 * Copyright (c) 2016, Yiru Chen <chen1ru@pku.edu.cn>
 */
package edu.emory.bmi.medicurator.tcia;

/*
 * The hierarchy of TCIA dat
 * root -> collection -> patient -> study -> series -> image
 */
public enum TciaHierarchy
{
    ROOT,
    COLLECTION,
    PATIENT,
    STUDY,
    SERIES,
    IMAGE
}

