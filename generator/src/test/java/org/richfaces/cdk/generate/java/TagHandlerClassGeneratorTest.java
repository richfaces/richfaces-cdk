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
package org.richfaces.cdk.generate.java;

import static org.easymock.EasyMock.anyInt;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.StringWriter;

import javax.faces.view.facelets.ComponentHandler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.cdk.CdkTestRunner;
import org.richfaces.cdk.Logger;
import org.richfaces.cdk.generate.java.taghandler.TagHandlerClassGenerator;
import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.model.ComponentModel;
import org.richfaces.cdk.model.TagModel;

import com.google.inject.Inject;

/**
 * @author akolonitsky
 * @since Feb 22, 2010
 */
@RunWith(CdkTestRunner.class)
public class TagHandlerClassGeneratorTest extends AbstractClassGeneratorTest {
    @Inject
    private TagHandlerClassGenerator generator;
    @Inject
    private Logger log;

    @Test
    public void testGetOutputFileBehavior() throws Exception {
        final StringWriter outputWriter = new StringWriter();
        expect(output.createOutput((String) anyObject(), anyInt())).andReturn(outputWriter);
        replay(output);
        ComponentModel model = ComponentClassGeneratorTest.createComponent();
        library.getComponents().add(model);

        TagModel tagModel = new TagModel();
        tagModel.setName("mytag");
        tagModel.setTargetClass(ClassName.parseName("org.richfaces.cdk.generate.java.GeneratedTagHandler"));
        tagModel.setBaseClass(ClassName.parseName(ComponentHandler.class.getName()));

        generator.process(model, tagModel);

        log.debug(outputWriter.toString());
        verify(output);

        // TODO - check generated class.
    }
}
