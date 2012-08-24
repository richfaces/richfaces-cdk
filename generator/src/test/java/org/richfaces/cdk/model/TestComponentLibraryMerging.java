package org.richfaces.cdk.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.Iterator;

import org.junit.Test;
import org.richfaces.cdk.util.SerializationUtils;

public class TestComponentLibraryMerging {
    
    @Test
    public void testComponentSerialization() {
        // having
        ComponentModel component = new ComponentModel();
        component.setId(new FacesId("faces-id"));
        component.setFamily(new FacesId("faces-family"));
        
        ComponentLibrary library = new ComponentLibrary();
        library.getComponents().add(component);
        
        // when
        library = serializeDeserialize(library);
        
        // then
        Iterator<ComponentModel> iterator = library.getComponents().iterator();
        assertTrue("deserialized library should contain at least one component", iterator.hasNext());
        
        component = iterator.next();
        assertNotNull(component);
        assertNotNull(component.getId());
        assertEquals(component.getId().getType(), "faces-id");

        assertNotNull(component);
        assertNotNull(component.getFamily());
        assertEquals(component.getFamily().getType(), "faces-family");
    }
    
    private <T extends Serializable> T serializeDeserialize(T object) {
        String base64 = SerializationUtils.serializeToBase64(object);
        T deserialized = SerializationUtils.deserializeFromBase64(base64);
        return deserialized;
    }
}
