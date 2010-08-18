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

import org.jboss.el.parser.Node;
import org.richfaces.cdk.templatecompiler.el.ELVisitor;
import org.richfaces.cdk.templatecompiler.el.ParsingException;
import org.richfaces.cdk.templatecompiler.el.types.ELType;
import org.richfaces.cdk.templatecompiler.el.types.TypesFactory;

/**
 * @author Nick Belaevski
 * @since 4.0
 */
public class BinaryBooleanResultOperationTreeNode extends AbstractBinaryOperationTreeNode {

    /**
     * @param node
     * @param operatorString
     */
    public BinaryBooleanResultOperationTreeNode(Node node, String operatorString) {
        super(node, operatorString);
    }

    /* (non-Javadoc)
      * @see org.richfaces.cdk.templatecompiler.el.node.AbstractBinaryOperationTreeNode#getOperationType(java.lang.reflect.Type, java.lang.reflect.Type)
      */
    @Override
    protected ELType getOperationType(ELType firstArgumentType,
                                    ELType secondArgumentType) {
        return TypesFactory.BOOLEAN_TYPE;
    }

    @Override
    protected String getCoercedChildOutput(int childIndex, ELVisitor visitor)
        throws ParsingException {

        return getChildOutput(childIndex, visitor);
    }
}
