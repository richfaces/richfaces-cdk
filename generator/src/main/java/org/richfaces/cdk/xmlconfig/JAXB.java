package org.richfaces.cdk.xmlconfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Writer;

import javax.xml.transform.Result;

import org.richfaces.cdk.CdkException;

public interface JAXB {

    public abstract <T> T unmarshal(File file, String schemaLocation, Class<T> bindClass) throws CdkException,FileNotFoundException;

    public abstract <T> T unmarshal(String url, String schemaLocation, Class<T> bindClass) throws CdkException,FileNotFoundException;

    public abstract <T> void marshal(Writer output, String schemaLocation, T model) throws CdkException;

    public abstract <T> void marshal(Result output, String schemaLocation, T model) throws CdkException;

}