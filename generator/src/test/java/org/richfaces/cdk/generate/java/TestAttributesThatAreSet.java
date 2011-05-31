package org.richfaces.cdk.generate.java;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIInput;
import javax.faces.component.html.HtmlCommandLink;

import org.junit.Test;

/**
 * <p class="changed_added_4_0">
 * Test functionality of 'attributesThatAreSet' collection
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public class TestAttributesThatAreSet {
    private static final String RENDERED = "rendered";
    private static final String ID = "id";
    private static final String ATTRIBUTES_THAT_ARE_SET_KEY = UIComponentBase.class.getName() + ".attributesThatAreSet";
    private static final @SuppressWarnings("serial")
    ValueExpression DUMMY_EXPRESSION = new ValueExpression() {
        @Override
        public boolean isLiteralText() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public int hashCode() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public String getExpressionString() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public boolean equals(Object obj) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void setValue(ELContext context, Object value) {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean isReadOnly(ELContext context) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public Object getValue(ELContext context) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Class<?> getType(ELContext context) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Class<?> getExpectedType() {
            // TODO Auto-generated method stub
            return null;
        }
    };

    @Test
    public void testId() throws Exception {
        UIComponentBase component = createComponentBase();
        assertAttributeNotSet(component, ID);
        component.setId("foo");
        assertAttributeNotSet(component, ID);
    }

    @Test
    public void testIdByAttributes() throws Exception {
        UIComponentBase component = createComponentBase();
        assertAttributeNotSet(component, ID);
        component.getAttributes().put(ID, "foo");
        assertAttributeNotSet(component, ID);
    }

    @Test
    public void testRenderedSet() throws Exception {
        UIComponentBase component = createComponentBase();
        assertAttributeNotSet(component, RENDERED);
        component.setRendered(true);
        assertAttributeNotSet(component, RENDERED);
    }

    @Test
    public void testRenderedAttribute() throws Exception {
        UIComponentBase component = createComponentBase();
        assertAttributeNotSet(component, RENDERED);
        component.getAttributes().put(RENDERED, true);
        assertAttributeNotSet(component, RENDERED);
    }

    @Test
    public void testRenderedByEl() throws Exception {
        UIComponentBase component = createComponentBase();
        assertAttributeNotSet(component, RENDERED);
        component.setValueExpression(RENDERED, DUMMY_EXPRESSION);
        assertAttributeSet(component, RENDERED);
    }

    @Test
    public void testInputMessage() throws Exception {
        UIInput input = new UIInput();
        input.setConverterMessage("foo");
        assertAttributeNotSet(input, "converterMessage");
    }

    @Test
    public void testCustomProperty() throws Exception {
        UIInput input = new UIInput();
        input.getAttributes().put("foobar", RENDERED);
        assertAttributeSet(input, "foobar");
    }

    @Test
    public void testHtmlAttribute() throws Exception {
        HtmlCommandLink link = new HtmlCommandLink();
        link.setDir("lefttoright");
        assertAttributeSet(link, "dir");
    }

    private UIComponentBase createComponentBase() {
        UIComponentBase component = new UIComponentBase() {
            @Override
            public String getFamily() {
                // TODO Auto-generated method stub
                return null;
            }
        };
        return component;
    }

    private void assertAttributeSet(UIComponent component, String attribute) {
        List<String> list = getAttributesList(component);
        assertTrue(list.contains(attribute));
    }

    private void assertAttributeNotSet(UIComponent component, String attribute) {
        List<String> list = getAttributesList(component);
        assertFalse(list.contains(attribute));
    }

    private List<String> getAttributesList(UIComponent component) {
        Object attributesList = component.getAttributes().get(ATTRIBUTES_THAT_ARE_SET_KEY);
        if (null != attributesList) {
            assertTrue(attributesList instanceof List<?>);
            List<String> list = (List<String>) attributesList;
            return list;
        } else {
            return Collections.emptyList();
        }
    }
}
