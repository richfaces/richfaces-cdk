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
package org.richfaces.cdk.templatecompiler;

import java.util.Collection;

import org.richfaces.cdk.CdkClassLoader;
import org.richfaces.cdk.Logger;
import org.richfaces.cdk.model.PropertyBase;
import org.richfaces.cdk.templatecompiler.el.types.TypesFactory;
import org.richfaces.cdk.templatecompiler.model.CompositeInterface;
import org.richfaces.cdk.templatecompiler.statements.HelperMethodFactory;

import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public class VisitorFactoryImpl implements TemplateVisitorFactory<RendererClassVisitor> {
    final TypesFactory typesFactory;
    final Logger log;
    final Injector injector;
    final CdkClassLoader loader;
    private final HelperMethodFactory helpersFactory;

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param classLoader
     * @param jaxbBinding
     * @param log
     */
    @Inject
    public VisitorFactoryImpl(TypesFactory typesFactory, Logger log, Injector injector, HelperMethodFactory helpersFactory,
            CdkClassLoader loader) {
        this.typesFactory = typesFactory;
        this.log = log;
        this.injector = injector;
        this.helpersFactory = helpersFactory;
        this.loader = loader;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.templatecompiler.TemplateVisitorFactory#createVisitor(org.richfaces.cdk.templatecompiler.model.
     * CompositeInterface)
     */
    @Override
    public RendererClassVisitor createVisitor(CompositeInterface composite, Collection<PropertyBase> attributes) {
        return new RendererClassVisitor(composite, attributes, log, injector, typesFactory, helpersFactory, loader);
    }
}
