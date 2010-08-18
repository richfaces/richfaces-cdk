package org.richfaces.cdk.xmlconfig;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Test;
import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.model.ComponentModel;
import org.richfaces.cdk.model.FacesId;
import org.richfaces.cdk.model.PropertyBase;
import org.richfaces.cdk.xmlconfig.model.ComponentAdapter;
import org.richfaces.cdk.xmlconfig.model.ComponentBean;
import org.richfaces.cdk.xmlconfig.model.ComponentBean.ComponentExtension;

import com.google.common.collect.Iterables;

public class ComponentAdapterTest {
    private static final String BAZ = "baz";
    private static final String FOO_BAR = "foo.Bar";
    private static final String FOO_DESCTIPTION = "foo.Desctiption";
    private static final FacesId FOO_FAMILY = FacesId.parseId("foo.Family");
    private static final String FOO_UI_BAR = "foo.UIBar";

    @Test
    public void testComponentAdapter() throws Exception {
        ComponentModel component = new ComponentModel(FacesId.parseId(FOO_BAR));

        component.setDescription(FOO_DESCTIPTION);
        component.setTargetClass(ClassName.parseName(FOO_UI_BAR));
        component.getOrCreateAttribute(BAZ);
        component.setFamily(FOO_FAMILY);
        ComponentAdapter componentAdapter = new ComponentAdapter();
        ComponentBean componentBean = componentAdapter.marshal(component);
        assertEquals(FOO_BAR, componentBean.getId().toString());

        Collection<PropertyBase> attributes = componentBean.getAttributes();

        assertEquals(1, attributes.size());
        assertEquals(BAZ, Iterables.getOnlyElement(attributes).getName());

        ComponentExtension extension = componentBean.getExtension();

        assertNotNull(extension);
        assertEquals(FOO_FAMILY, extension.getFamily());
    }

    @Test
    public void testCopyExtensions() {

        // fail("Not yet implemented");
    }

    @Test
    public void testCopyProperties() {

        // fail("Not yet implemented");
    }
}
