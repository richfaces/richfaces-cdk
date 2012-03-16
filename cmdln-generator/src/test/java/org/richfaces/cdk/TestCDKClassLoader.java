package org.richfaces.cdk;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.richfaces.cdk.Parameters;


public class TestCDKClassLoader {
    private static final String PROJECT_ROOT = "/home/lfryc/workspaces/richfaces/sandbox/hot-deployment/ui";
    
    Parameters parameters;
    
    @Before
    public void setup() {
        parameters = new Parameters();
        parameters.projectRoot = PROJECT_ROOT;
    }
    
    @Test
    public void test() {
        List<File> files = parameters.getClassPathElements();
        
        boolean foundFacesContext =  false;
        for (File file : files) {
            if (file.getName().contains("FacesContext.class")) {
                foundFacesContext = true;
            }
        }
        
        assertTrue(foundFacesContext);
    }
    
    @Test
    public void testClassLoader() {
        ClassLoader cdkClassLoader = parameters.createProjectClassLoader();
        
        try {
            cdkClassLoader.loadClass("javax.faces.context.FacesContext");
            cdkClassLoader.loadClass("org.richfaces.component.AbstractHotComponent");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        
    }
}
