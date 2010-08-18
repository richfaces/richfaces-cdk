package org.richfaces.cdk.xmlconfig;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.cdk.CdkClassLoader;
import org.richfaces.cdk.CdkTestBase;
import org.richfaces.cdk.CdkTestRunner;
import org.richfaces.cdk.FileManager;
import org.richfaces.cdk.Mock;
import org.richfaces.cdk.Source;
import org.richfaces.cdk.Sources;
import org.richfaces.cdk.annotations.JsfComponent;
import org.xml.sax.InputSource;

import com.google.common.collect.ImmutableList;
import com.google.inject.Binder;
import com.google.inject.Inject;

@RunWith(CdkTestRunner.class)
public class CdkResolverTest extends CdkTestBase {

    private class MyURLConnection extends URLConnection {

        private InputStream inputStream;

        MyURLConnection(URL u, InputStream inputStream) {
            super(u);
            this.inputStream = inputStream;
        }

        @Override
        public void connect() throws IOException {
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return this.inputStream;
        }
    }

    private class MyURLStreamHandler extends URLStreamHandler {

        private InputStream inputStream;

        MyURLStreamHandler(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        protected URLConnection openConnection(URL u) throws IOException {
            return new MyURLConnection(u, inputStream);
        }
    }

    private static final String TEST_HTML = "org/richfaces/cdk/apt/test.html";

    @Inject
    private CdkEntityResolver entityResolver;

    @Mock
    @Source(Sources.FACES_CONFIGS)
    private FileManager facesConfigSource;

    @Mock
    @Source(Sources.RENDERER_TEMPLATES)
    private FileManager templatesSource;

    @Override
    public void configure(Binder binder) {
        super.configure(binder);
        try {
            CdkClassLoader classLoader =
                new CdkClassLoader(ImmutableList.of(getLibraryFile("test.source.properties"),
                    getLibraryFile(JsfComponent.class)), null) {
                    @Override
                    public URL getResource(String name) {
                        if ("foo/bar.xml".equals(name)) {
                            try {
                                MyURLStreamHandler myURLStreamHandler = new MyURLStreamHandler(getBarXmlStream());
                                return new URL("file", "localhost", 80, "/foo/bar.xml", myURLStreamHandler);
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                                return null;
                            }
                        } else {
                            return super.getResource(name);
                        }
                    }

                    @Override
                    public InputStream getResourceAsStream(String name) {
                        if ("foo/bar.xml".equals(name)) {
                            return getBarXmlStream();
                        } else {
                            return super.getResourceAsStream(name);
                        }
                    }

                };
            binder.bind(CdkClassLoader.class).toInstance(classLoader);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetProjectInputSource() throws Exception {
        expect(facesConfigSource.getFile(TEST_HTML)).andReturn(getJavaFile(TEST_HTML));
        replay(facesConfigSource, templatesSource);
        InputSource input = entityResolver.getProjectInputSource(facesConfigSource, TEST_HTML);
        verify(facesConfigSource, templatesSource);
        assertNotNull(input);
    }

    @Test
    public void testResolveSystemIdAttributes() throws Exception {

        replay(facesConfigSource, templatesSource);
        InputSource input = entityResolver.resolveSystemId("urn:attributes:test-props.xml");
        verify(facesConfigSource, templatesSource);
        assertNotNull(input);
    }

    @Test
    public void testResolveSystemIdResource() throws Exception {
        replay(facesConfigSource, templatesSource);
        InputSource input = entityResolver.resolveSystemId("urn:resource:foo/bar.xml");
        verify(facesConfigSource, templatesSource);
        assertNotNull(input);
    }

    @Test
    public void testResolveSystemIdSystem() throws Exception {

        replay(facesConfigSource, templatesSource);

        InputSource input = entityResolver.resolveSystemId("http://java.sun.com/xml/ns/javaee/web-facesconfig_2_0.xsd");
        verify(facesConfigSource, templatesSource);
        assertNotNull(input);
    }

    private ByteArrayInputStream getBarXmlStream() {
        return new ByteArrayInputStream("baz".getBytes());
    }
}
