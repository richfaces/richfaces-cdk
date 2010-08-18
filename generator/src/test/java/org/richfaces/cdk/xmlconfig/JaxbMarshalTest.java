package org.richfaces.cdk.xmlconfig;

import java.io.StringWriter;

import javax.xml.transform.stream.StreamResult;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.cdk.CdkTestRunner;
import org.richfaces.cdk.Mock;
import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.model.ComponentModel;
import org.richfaces.cdk.model.FacesId;
import org.richfaces.cdk.model.RenderKitModel;
import org.richfaces.cdk.model.RendererModel;
import org.richfaces.cdk.xmlconfig.model.FacesConfigAdapter;

import com.google.inject.Inject;

@RunWith(CdkTestRunner.class)
public class JaxbMarshalTest extends JaxbTestBase {

    @Mock
    JAXB jaxbBinding;

    @Inject
    ComponentLibrary library;

    @Test
    public void testMarshalResultStringT() throws Exception {
        ComponentModel component = new ComponentModel(FacesId.parseId("foo.bar"));
        library.getComponents().add(component);
        RenderKitModel renderKit = library.addRenderKit("HTML");
        RendererModel renderer = new RendererModel(FacesId.parseId("foo.Renderer"));
        renderKit.getRenderers().add(renderer);

        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        FacesConfigAdapter adapter = new FacesConfigAdapter();

        jaxbBinding.marshal(result, FacesConfigGenerator.FACES_SCHEMA_LOCATION, adapter.marshal(library));
        System.out.println(writer.toString());
    }
}
