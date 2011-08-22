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

import java.lang.annotation.Annotation;

import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;

import org.richfaces.cdk.CdkProcessingException;
import org.richfaces.cdk.annotations.TagLibrary;
import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.model.Taglib;
import org.richfaces.cdk.util.Strings;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public class TagLibProcessor implements CdkAnnotationProcessor {
    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.apt.processors.CdkAnnotationProcessor#getProcessedAnnotation()
     */
    @Override
    public Class<? extends Annotation> getProcessedAnnotation() {
        return TagLibrary.class;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.apt.processors.CdkAnnotationProcessor#process(javax.lang.model.element.Element,
     * org.richfaces.cdk.model.ComponentLibrary)
     */
    @Override
    public void process(Element element, ComponentLibrary library) throws CdkProcessingException {
        TagLibrary tagLibrary = element.getAnnotation(TagLibrary.class);
        Taglib taglibModel = library.getTaglib();
        if (null == taglibModel) {
            taglibModel = new Taglib();
        }
        taglibModel.setUri(tagLibrary.uri());
        String shortName = tagLibrary.shortName();
        if (!Strings.isEmpty(shortName)) {
            taglibModel.setShortName(shortName);
        }
        String displayName = tagLibrary.displayName();
        if (!Strings.isEmpty(displayName)) {
            taglibModel.setDisplayName(displayName);
        }
        String tlibVersion = tagLibrary.tlibVersion();
        if (!Strings.isEmpty(tlibVersion)) {
            taglibModel.setTlibVersion(tlibVersion);
        }
        // Default prefix for library
        String preffix = tagLibrary.prefix();
        if (!Strings.isEmpty(preffix)) {
            library.setPrefix(preffix);
        } else if (Strings.isEmpty(library.getPrefix())) {
            // record package name to use in NamingConventions
            PackageElement packageElement = (PackageElement) element;
            preffix = packageElement.getQualifiedName().toString();
            if (!Strings.isEmpty(preffix)) {
                library.setPrefix(preffix);
            }
        }
        library.setTaglib(taglibModel);
    }
}
