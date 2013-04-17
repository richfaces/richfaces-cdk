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
 * Implementation for easy on inheritance. By default, all calls delegated to the {@link #defaultAction(Visitable, Object)}
 * method.
 *
 * @author akolonitsky
 * @since Feb 22, 2010
 */
public abstract class SimpleVisitor<R, D> implements Visitor<R, D> {
    /**
     * <p class="changed_added_4_0">
     * Default action for all visitXXX method.
     * </p>
     *
     * @param model visited object.
     * @param param optional parameter for visit method.
     * @return null by default.
     */
    protected R defaultAction(Visitable model, D param) {
        return null;
    }

    public R visitComponentLibrary(ComponentLibrary model, D param) {
        return defaultAction(model, param);
    }

    public R visitComponent(ComponentModel model, D param) {
        return defaultAction(model, param);
    }

    public R visitConverter(ConverterModel model, D param) {
        return defaultAction(model, param);
    }

    public R visitValidator(ValidatorModel model, D param) {
        return defaultAction(model, param);
    }

    public R visitBehavior(BehaviorModel model, D param) {
        return defaultAction(model, param);
    }

    public R visitFacet(FacetModel model, D param) {
        return defaultAction(model, param);
    }

    public R visitEvent(EventModel model, D param) {
        return defaultAction(model, param);
    }

    public R visitBehaviorRenderer(BehaviorRendererModel model, D param) {
        return defaultAction(model, param);
    }

    public R visitProperty(PropertyBase model, D param) {
        return defaultAction(model, param);
    }

    public R visitRenderKit(RenderKitModel model, D param) {
        return defaultAction(model, param);
    }

    public R visitRender(RendererModel model, D param) {
        return defaultAction(model, param);
    }

    public R visitListener(ListenerModel model, D param) {
        return defaultAction(model, param);
    }

    @Override
    public R visitFunction(FunctionModel model, D param) {
        return defaultAction(model, param);
    }

    @SuppressWarnings("unchecked")
    public R visit(ModelElement model, D param) {
        return defaultAction(model, param);
    }
}
