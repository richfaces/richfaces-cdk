/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat, Inc. and individual contributors
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
package org.richfaces.cdk.templatecompiler.el.node;

import org.richfaces.cdk.templatecompiler.el.ELNodeConstants;
import org.richfaces.cdk.templatecompiler.el.ELVisitor;
import org.richfaces.cdk.templatecompiler.el.ParsingException;
import org.richfaces.cdk.templatecompiler.el.types.ELType;
import org.richfaces.cdk.templatecompiler.el.types.NullType;
import org.richfaces.cdk.templatecompiler.el.types.TypesFactory;

/**
 * @author Nick Belaevski
 *
 */
public final class ConstantValueTreeNode extends AbstractTreeNode {
    // XXX what class to use for null object: Void.class or null - special NullType be used
    public static final ConstantValueTreeNode NULL_NODE = new ConstantValueTreeNode(ELNodeConstants.NULL_VALUE,
            NullType.INSTANCE);
    public static final ConstantValueTreeNode TRUE_NODE = new ConstantValueTreeNode(ELNodeConstants.TRUE_VALUE,
            TypesFactory.BOOLEAN_TYPE);
    public static final ConstantValueTreeNode FALSE_NODE = new ConstantValueTreeNode(ELNodeConstants.FALSE_VALUE,
            TypesFactory.BOOLEAN_TYPE);
    private final String value;
    private final ELType type;

    private ConstantValueTreeNode(String value, ELType type) {
        super(null);

        this.value = value;
        this.type = type;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.templatecompiler.el.node.AbstractTreeNode#visit(java.lang.StringBuilder, java.util.Map,
     * org.richfaces.cdk.templatecompiler.el.ELVisitor)
     */
    @Override
    public void visit(StringBuilder sb, ELVisitor visitor) throws ParsingException {
        sb.append(value);
        visitor.setExpressionType(type);
        if (type.isNullType()) {
            visitor.setLiteral(false);
        }
    }
}
