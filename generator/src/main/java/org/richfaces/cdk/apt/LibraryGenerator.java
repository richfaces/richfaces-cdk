package org.richfaces.cdk.apt;

import org.richfaces.cdk.CdkException;

public interface LibraryGenerator {

    /**
     * Generate all types of files from library model.
     */
    void generate() throws CdkException;

}
