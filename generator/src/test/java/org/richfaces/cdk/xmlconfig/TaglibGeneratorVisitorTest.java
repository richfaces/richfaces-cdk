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

package org.richfaces.cdk.xmlconfig;

import java.util.Set;

import org.junit.Test;
import org.richfaces.cdk.generate.taglib.TaglibGeneratorVisitor;
import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.model.EventName;
import org.richfaces.cdk.model.FacesId;
import org.richfaces.cdk.model.PropertyBase;
import org.richfaces.cdk.model.Taglib;
import org.richfaces.cdk.model.ValidatorModel;

/**
 * @author akolonitsky
 * @since Feb 3, 2010
 */
public class TaglibGeneratorVisitorTest extends XmlTest {

    @Test
    public void generationTest() throws Exception {
        ValidatorModel validator = new ValidatorModel(FacesId.parseId("foo.bar"));
        validator.setTargetClass(ClassName.parseName(Object.class.getName()));
        validator.setGenerate(true);

        addAttribute(validator, "testValue", Object.class, true);
        addAttribute(validator, "testFlag", boolean.class, true);
        PropertyBase attribute = addAttribute(validator, "id", String.class, false);

        Set<EventName> eventNames = attribute.getEventNames();
        eventNames.add(getEvent("id", false));
        eventNames.add(getEvent("action", true));

        ComponentLibrary library = new ComponentLibrary();
        library.getValidators().add(validator);
        Taglib taglib = new Taglib();
        taglib.setShortName("a4j");
        taglib.setUri("http://foo.bar/a4j");
        library.setTaglib(taglib);
        TaglibGeneratorVisitor visitor = new TaglibGeneratorVisitor();
        library.accept(visitor, library);
        // visitor.generate();

        // TODO validate result
    }

    private PropertyBase addAttribute(ValidatorModel validator, String attributeName, Class<?> type, boolean generate) {
        PropertyBase attribute = validator.getOrCreateAttribute(attributeName);
        attribute.setType(new ClassName(type));
        attribute.setGenerate(generate);
        return attribute;
    }

    private EventName getEvent(String name, boolean defaultEvent) {
        EventName event = new EventName();
        event.setName(name);
        event.setDefaultEvent(defaultEvent);
        return event;
    }
}
