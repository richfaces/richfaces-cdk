package org.richfaces.cdk.apt;

import java.util.Set;

import org.richfaces.cdk.CdkException;
import org.richfaces.cdk.CdkWriter;
import org.richfaces.cdk.model.ComponentLibrary;

import com.google.inject.Inject;

public class DefaultLibraryGenerator implements LibraryGenerator {

    @Inject
    private Set<CdkWriter> generators;
    
    @Inject
    private ComponentLibrary library;

    @Override
    public void generate() throws CdkException {
        for (CdkWriter generator : generators) {
            generator.render(library);
        }
    }
}
