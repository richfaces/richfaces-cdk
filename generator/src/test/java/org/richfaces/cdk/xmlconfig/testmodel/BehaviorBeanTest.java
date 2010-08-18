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

package org.richfaces.cdk.xmlconfig.testmodel;

import java.io.StringWriter;

import org.junit.Test;
import org.richfaces.cdk.model.AttributeModel;
import org.richfaces.cdk.model.BehaviorModel;
import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.model.FacesId;
import org.richfaces.cdk.model.PropertyBase;
import org.richfaces.cdk.xmlconfig.XmlTest;

/**
 * @author akolonitsky
 * @since Jan 21, 2010
 */
public class BehaviorBeanTest extends XmlTest {

    @Test
    public void testMarshal() throws Exception {
        ComponentLibrary library = new ComponentLibrary();

        BehaviorModel behaviorModel = new BehaviorModel(FacesId.parseId("my_behavior"));
        behaviorModel.setTargetClass(ClassName.parseName(Object.class.getName()));
        PropertyBase prop = behaviorModel.getOrCreateAttribute("attr1");
        prop.setType(new ClassName(String.class));

        AttributeModel attributeModel = new AttributeModel();
        attributeModel.setName("attr2");
        attributeModel.setType(new ClassName(String.class));
        behaviorModel.getAttributes().add(attributeModel);

        library.getBehaviors().add(behaviorModel);

        // Jaxb marshaling
        StringWriter facesConfig = generateFacesConfig(library);
        log.debug(facesConfig.toString());

        // Checks
        checkXmlStructure(facesConfig);
        validateXml(facesConfig);
    }
}
