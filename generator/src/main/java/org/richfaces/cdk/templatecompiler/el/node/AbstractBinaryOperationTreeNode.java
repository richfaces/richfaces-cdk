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
import org.richfaces.cdk.templatecompiler.el.ELNodeConstants;
import org.richfaces.cdk.templatecompiler.el.ELVisitor;
import org.richfaces.cdk.templatecompiler.el.ParsingException;
import org.richfaces.cdk.templatecompiler.el.types.ELType;

/**
 * <p>
 * Abstract class for all binary (having two operands) operation expression nodes
 * </p>
 * <p>
 * For operands <code>A</code> and <code>B</code> and operator <code>x</code> output is <code>(A x B)</code>
 * </p>
 * 
 * @author Nick Belaevski
 */
public abstract class AbstractBinaryOperationTreeNode extends AbstractTreeNode {

    private final String operatorString;

    public AbstractBinaryOperationTreeNode(Node node, String operatorString) {
        super(node);

        this.operatorString = operatorString;
    }

    protected abstract ELType getOperationType(ELType firstArgumentType, ELType secondArgumentType);

    protected abstract String getCoercedChildOutput(int childIndex, ELVisitor visitor)
        throws ParsingException;

    /*
     * (non-Javadoc)
     * 
     * @see org.richfaces.cdk.templatecompiler.el.node.AbstractTreeNode#visit(java.lang.StringBuilder, java.util.Map,
     * org.richfaces.cdk.templatecompiler.el.ELVisitor)
     */
    @Override
    public void visit(StringBuilder sb, ELVisitor visitor) throws ParsingException {
        String firstChildOutput = getCoercedChildOutput(0, visitor);
        ELType firstChildType = visitor.getType();
        String secondChildOutput = getCoercedChildOutput(1, visitor);
        ELType secondChildType = visitor.getType();

        sb.append(ELNodeConstants.LEFT_BRACKET);

        sb.append(firstChildOutput);
        sb.append(operatorString);
        sb.append(secondChildOutput);

        sb.append(ELNodeConstants.RIGHT_BRACKET);

        visitor.setExpressionType(getOperationType(firstChildType, secondChildType));
    }
}
