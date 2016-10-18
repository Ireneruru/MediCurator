/*
 * Title:        Medicurator
 * Description:  Near duplicate detection framework for heterogeneous medical data sources
 * Licence:      Apache License Version 2.0 - http://www.apache.org/licenses/
 *
 * Copyright (c) 2016, Yiru Chen <chen1ru@pku.edu.cn>
 */
package edu.emory.bmi.medicurator.storage;

import java.io.InputStream;

/**
 * local storage driver interface
 * includs save and load methods
 */
public interface Storage
{
    boolean saveToPath(String path, InputStream in);
    InputStream loadFromPath(String path);
}
