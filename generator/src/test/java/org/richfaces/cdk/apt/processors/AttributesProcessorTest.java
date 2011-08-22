/*
 * $Id$
 *
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */
package org.richfaces.cdk.apt.processors;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.same;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.Collections;

import javax.lang.model.element.TypeElement;

import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.cdk.CdkTestRunner;
import org.richfaces.cdk.Mock;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.apt.AnnotationProcessorTestBase;
import org.richfaces.cdk.apt.SourceUtils;
import org.richfaces.cdk.apt.SourceUtils.BeanProperty;
import org.richfaces.cdk.apt.SourceUtils.SuperTypeVisitor;
import org.richfaces.cdk.model.BeanModelBase;
import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.model.PropertyModel;
import org.richfaces.cdk.xmlconfig.CdkEntityResolver;
import org.richfaces.cdk.xmlconfig.JAXB;
import org.richfaces.cdk.xmlconfig.model.Fragment;

import com.google.inject.Inject;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
@RunWith(CdkTestRunner.class)
public class AttributesProcessorTest extends AnnotationProcessorTestBase {
    private static final String FOO = "foo";
    private static final String FOO_XML = "foo.xml";
    @Mock
    private DescriptionProcessor descriptionProcessor;
    @Inject
    private AttributesProcessorImpl processor;
    @Mock
    private SourceUtils utils;
    @Mock
    private JAXB xmlProcessor;

    /**
     * Test method for
     * {@link org.richfaces.cdk.apt.processors.AttributesProcessorImpl#processType(org.richfaces.cdk.model.BeanModelBase, javax.lang.model.element.TypeElement)}
     * .
     */
    @Test
    public void testProcessType() {
        BeanModelBase bean = new BeanModelBase();
        TypeElement element = createMock(TypeElement.class);
        utils.visitSupertypes(same(element), EasyMock.isA(SuperTypeVisitor.class));
        expectLastCall();
        BeanProperty beanProperty = createNiceMock(BeanProperty.class);
        expect(utils.getBeanPropertiesAnnotatedWith(eq(Attribute.class), same(element))).andReturn(
                Collections.singleton(beanProperty));
        // expect(utils.getAbstractBeanProperties(same(element))).andReturn(new HashSet<BeanProperty>(0));
        expect(beanProperty.getName()).andReturn(FOO);
        expect(beanProperty.getType()).andReturn(ClassName.parseName(String.class.getName()));
        mockController.replay();
        replay(element, beanProperty);
        processor.processType(bean, element);
        mockController.verify();
        verify(element, beanProperty);
        assertEquals(1, bean.getAttributes().size());
    }

    @Test
    public void testProcessXmlFragment() throws Exception {
        BeanModelBase bean = new BeanModelBase();
        Fragment fragment = new Fragment();
        PropertyModel propertyModel = new PropertyModel();
        propertyModel.setName(FOO);
        fragment.getProperties().add(propertyModel);
        expect(xmlProcessor.unmarshal(eq(CdkEntityResolver.URN_ATTRIBUTES + FOO_XML), (String) anyObject(), eq(Fragment.class)))
                .andReturn(fragment);
        mockController.replay();
        processor.processXmlFragment(bean, FOO_XML);
        mockController.verify();
        assertEquals(1, bean.getAttributes().size());
    }

    @Override
    protected Iterable<String> sources() {
        return Collections.emptySet();
    }
}
