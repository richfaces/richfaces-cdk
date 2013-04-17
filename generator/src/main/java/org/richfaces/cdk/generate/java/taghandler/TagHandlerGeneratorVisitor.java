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
package org.richfaces.cdk.generate.java.taghandler;

import org.richfaces.cdk.annotations.TagType;
import org.richfaces.cdk.model.BehaviorModel;
import org.richfaces.cdk.model.ComponentModel;
import org.richfaces.cdk.model.ConverterModel;
import org.richfaces.cdk.model.ModelElementBase;
import org.richfaces.cdk.model.SimpleVisitor;
import org.richfaces.cdk.model.TagModel;
import org.richfaces.cdk.model.ValidatorModel;

import com.google.inject.Inject;

/**
 * @author akolonitsky
 * @since Feb 22, 2010
 */
public class TagHandlerGeneratorVisitor extends SimpleVisitor<Boolean, Boolean> {
    private final TagHandlerClassGenerator tagGenerator;

    @Inject
    public TagHandlerGeneratorVisitor(TagHandlerClassGenerator tagGenerator) {
        this.tagGenerator = tagGenerator;
    }

    @Override
    public Boolean visitComponent(ComponentModel model, Boolean data) {
        generateTagHandler(model);
        return null;
    }

    @Override
    public Boolean visitConverter(ConverterModel model, Boolean data) {
        generateTagHandler(model);
        return null;
    }

    @Override
    public Boolean visitValidator(ValidatorModel model, Boolean data) {
        generateTagHandler(model);
        return null;
    }

    @Override
    public Boolean visitBehavior(BehaviorModel model, Boolean data) {
        generateTagHandler(model);
        return null;
    }

    private void generateTagHandler(ModelElementBase model) {
        for (TagModel tag : model.getTags()) {
            if ((TagType.All.equals(tag.getType()) || TagType.Facelets.equals(tag.getType())) && tag.isGenerate()) {
                tagGenerator.process(model, tag);
            }
        }
    }
}
