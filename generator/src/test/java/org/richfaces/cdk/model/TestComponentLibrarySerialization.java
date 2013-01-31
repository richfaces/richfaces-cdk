package org.richfaces.cdk.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.Iterator;

import org.junit.Test;
import org.richfaces.cdk.util.SerializationUtils;

public class TestComponentLibrarySerialization {

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
        assertEquals("faces-id", component.getId().getType());

        assertNotNull(component);
        assertEquals("faces-family", component.getFamily().getType());
    }

    @Test
    public void testComponentTagSerialization() {
        // given
        ComponentModel component = new ComponentModel();

        TagModel tag = new TagModel();
        tag.setName("tag-name");
        component.getTags().add(tag);

        ComponentLibrary library = new ComponentLibrary();
        library.getComponents().add(component);

        // when
        library = serializeDeserialize(library);

        // then
        component = library.getComponents().iterator().next();
        tag = component.getTags().iterator().next();

        assertNotNull(tag);
        assertEquals("tag-name", tag.getName());
    }

    private <T extends Serializable> T serializeDeserialize(T object) {
        byte[] serialized = SerializationUtils.serializeToBytes(object);
        T deserialized = SerializationUtils.deserializeFromBytes(serialized);
        return deserialized;
    }
}
