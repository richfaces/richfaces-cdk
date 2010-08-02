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

import java.util.ArrayList;
import java.util.List;

import org.jboss.el.parser.Node;
import org.richfaces.cdk.templatecompiler.el.ELNodeConstants;
import org.richfaces.cdk.templatecompiler.el.ELVisitor;
import org.richfaces.cdk.templatecompiler.el.ParsingException;
import org.richfaces.cdk.templatecompiler.el.types.ELType;
import org.richfaces.cdk.templatecompiler.el.types.TypesFactory;

/**
 * @author Nick Belaevski
 * 
 */
public abstract class AbstractMethodTreeNode extends AbstractTreeNode {

    private static final ELType[] EMPTY_TYPES_ARRAY = new ELType[0];

    /**
     * @param node
     */
    public AbstractMethodTreeNode(Node node) {
        super(node);
    }

    protected void visitMethod(StringBuilder sb, ELVisitor visitor, String methodName)
        throws ParsingException {
        ELType currentExpressionType = visitor.getType();
        List<ELType> argumentTypes = new ArrayList<ELType>();

        sb.append(ELNodeConstants.DOT);
        sb.append(methodName);
        sb.append(ELNodeConstants.LEFT_BRACKET);

        int childrenCount = getChildrenCount();

        for (int k = 0; k < childrenCount; k++) {
            if (k != 0) {
                sb.append(ELNodeConstants.COMMA);
            }
            String childOutput = getChildOutput(k, visitor);
            sb.append(childOutput);

            // TODO: handle generic matches -?
            argumentTypes.add(visitor.getType());
        }

        sb.append(ELNodeConstants.RIGHT_BRACKET);

        ELType returnType = null;
        visitor.setExpressionType(currentExpressionType);
        try {
            returnType = visitor.getMatchingVisibleMethodReturnType( methodName, argumentTypes
                .toArray(EMPTY_TYPES_ARRAY));
        } catch (ParsingException e) {
            // TODO: handle exception
            returnType = TypesFactory.OBJECT_TYPE;
        }

        visitor.setLiteral(false);
        visitor.setExpressionType(returnType);
    }
}
