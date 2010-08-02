/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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

package org.richfaces.cdk.model.validator;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.cdk.CdkTestBase;
import org.richfaces.cdk.CdkTestRunner;
import org.richfaces.cdk.Logger;
import org.richfaces.cdk.Mock;
import org.richfaces.cdk.NamingConventions;
import org.richfaces.cdk.apt.SourceUtils;
import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.model.ComponentModel;
import org.richfaces.cdk.model.FacesId;

import com.google.inject.Inject;

/**
 * @author asmirnov
 * @version $Id$
 * 
 */
@RunWith(CdkTestRunner.class)
public class ModelValidatorTest extends CdkTestBase {

    private static final FacesId FOO_BAZ = FacesId.parseId("foo.baz");

    @Mock
    protected Logger log;

    @Mock
    SourceUtils utils;

    @Inject
    ValidatorImpl validator;

    @Inject
    private ComponentLibrary library;

    @Mock
    private NamingConventions namiingConventions;

    /**
     * Test method for
     * {@link org.richfaces.cdk.model.validator.ValidatorImpl#verifyComponent(org.richfaces.cdk.model.ComponentModel)}.
     */
    @Test
    public void testVerifyEmptyComponent() {
        ComponentModel component = new ComponentModel();
        log.error((CharSequence) anyObject());
        expectLastCall();
        replay(log, utils, namiingConventions);
        validator.verifyComponentType(component);
        verify(log, utils, namiingConventions);
    }

    @Test
    public void testVerifyNoTypeComponent() {
        ComponentModel component = new ComponentModel();
        ClassName className = new ClassName("foo.component.UIBar");
        FacesId type = FacesId.parseId("foo.Bar");
        component.setTargetClass(className);
        expect(namiingConventions.inferComponentType(className)).andReturn(type);
        expect(namiingConventions.inferUIComponentFamily(type)).andReturn(FOO_BAZ);
        replay(log, utils, namiingConventions);
        // Validator should set component type from base class.
        validator.verifyComponentType(component);
        verify(log, utils, namiingConventions);
        assertEquals(type, component.getId());
        assertEquals(FOO_BAZ, component.getFamily());
    }

}
