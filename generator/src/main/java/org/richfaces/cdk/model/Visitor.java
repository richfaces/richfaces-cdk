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
package org.richfaces.cdk.model;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author akolonitsky
 * @since Jan 23, 2010
 *
 * @param <R> type of result returned by visitXXX method.
 * @param <D> type of parameter for visit methods.
 */
public interface Visitor<R, D> {
    R visitComponentLibrary(ComponentLibrary model, D param);

    R visitComponent(ComponentModel model, D param);

    R visitConverter(ConverterModel model, D param);

    R visitValidator(ValidatorModel model, D param);

    R visitBehavior(BehaviorModel model, D param);

    R visitFacet(FacetModel model, D param);

    R visitEvent(EventModel model, D param);

    R visitBehaviorRenderer(BehaviorRendererModel model, D param);

    R visitProperty(PropertyBase model, D param);

    R visitRenderKit(RenderKitModel model, D param);

    R visitRender(RendererModel model, D param);

    R visitListener(ListenerModel model, D param);

    R visitFunction(FunctionModel model, D param);

    R visit(ModelElement model, D param);
}
