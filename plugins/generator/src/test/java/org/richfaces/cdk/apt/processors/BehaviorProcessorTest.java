/*
 * JBoss, Home of Professional Open Source
 * Copyright , Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.richfaces.cdk.apt.processors;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Collections;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.cdk.CdkTestRunner;
import org.richfaces.cdk.Mock;
import org.richfaces.cdk.NamingConventions;
import org.richfaces.cdk.Stub;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.Description;
import org.richfaces.cdk.annotations.JsfBehavior;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.apt.AnnotationProcessorTestBase;
import org.richfaces.cdk.apt.SourceUtils;
import org.richfaces.cdk.apt.SourceUtils.BeanProperty;
import org.richfaces.cdk.apt.SourceUtils.SuperTypeVisitor;
import org.richfaces.cdk.model.BehaviorModel;
import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.model.FacesId;
import org.richfaces.cdk.xmlconfig.JAXB;

import com.google.inject.Inject;

/**
 * @author akolonitsky
 * @since Jan 21, 2010
 */
@RunWith(CdkTestRunner.class)
public class BehaviorProcessorTest extends AnnotationProcessorTestBase {
    private static final String BEHAVIOR_CLASS_JAVA = "org/richfaces/cdk/test/component/MyBehavior.java";

    private static final String MY_BEHAVIOR = "my_behavior";

    @Mock
    private JsfBehavior behaviorAnnotation;

    @Mock
    private TypeElement componentElement;

    @Mock
    private NamingConventions conventions;

    @Mock
    private Description description;

    @Mock
    private JAXB jaxb;

    @Inject
    private ComponentLibrary library;

    @Stub
    private Name name;

    @Inject
    private BehaviorProcessor processor;

    @Stub
    private Tag tag;

    @Mock
    private SourceUtils utils;

    @Test
    @Ignore
    public void testProcess() throws Exception {

        expect(componentElement.getAnnotation(JsfBehavior.class)).andStubReturn(behaviorAnnotation);
        expect(behaviorAnnotation.id()).andReturn(MY_BEHAVIOR);
        expect(componentElement.getQualifiedName()).andReturn(name);
        expect(componentElement.getModifiers()).andReturn(Collections.singleton(Modifier.ABSTRACT));
        expect(componentElement.getQualifiedName()).andReturn(name).atLeastOnce();
        expect(behaviorAnnotation.generate()).andReturn("foo.Bar");
        expect(behaviorAnnotation.tag()).andReturn(tag);
        expect(behaviorAnnotation.attributes()).andReturn(new String[] {});
        expect(behaviorAnnotation.description()).andReturn(this.description);
        expect(tag.handler()).andStubReturn("");
        utils.visitSupertypes((TypeElement) anyObject(), (SuperTypeVisitor) anyObject());
        expectLastCall();
        expect(utils.getBeanPropertiesAnnotatedWith(eq(Attribute.class), (TypeElement) anyObject())).andReturn(
            Collections.<BeanProperty> emptySet());
        expect(utils.getDocComment((TypeElement) anyObject())).andStubReturn(null);
        replay(log, utils, componentElement, jaxb, conventions, behaviorAnnotation, tag, name);
        processor.process(componentElement, library);
        verify(log, utils, componentElement, jaxb, conventions, behaviorAnnotation, tag, name);
        Collection<BehaviorModel> behaviors = library.getBehaviors();

        assertEquals(1, behaviors.size());

        for (BehaviorModel model : behaviors) {
            FacesId id = model.getId();
            if (id != null) {
                assertEquals(MY_BEHAVIOR, id.toString());
            }
        }
    }

    @Override
    protected Iterable<String> sources() {
        return Collections.singleton(BEHAVIOR_CLASS_JAVA);
    }

}
