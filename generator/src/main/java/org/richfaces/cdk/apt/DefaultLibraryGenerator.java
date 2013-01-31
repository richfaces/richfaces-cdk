package org.richfaces.cdk.apt;

import java.util.Set;

import org.richfaces.cdk.CdkException;
import org.richfaces.cdk.CdkWriter;
import org.richfaces.cdk.Logger;
import org.richfaces.cdk.TimeMeasure;
import org.richfaces.cdk.model.ComponentLibrary;

import com.google.inject.Inject;

public class DefaultLibraryGenerator implements LibraryGenerator {

    @Inject
    private Set<CdkWriter> generators;

    @Inject
    private ComponentLibrary library;

    @Inject
    private Logger log;

    @Override
    public void generate() throws CdkException {
        for (CdkWriter generator : generators) {
            TimeMeasure time = new TimeMeasure("generator", log).info(true).start(generator.getClass().getSimpleName());
            generator.render(library);
            time.stop();
        }
    }
}
