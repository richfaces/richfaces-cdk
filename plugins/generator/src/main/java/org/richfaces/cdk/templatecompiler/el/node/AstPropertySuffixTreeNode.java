/**
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

package org.richfaces.cdk.templatecompiler.el.node;

import org.jboss.el.parser.Node;
import org.richfaces.cdk.templatecompiler.el.ELNodeConstants;
import org.richfaces.cdk.templatecompiler.el.ELVisitor;
import org.richfaces.cdk.templatecompiler.el.ParsingException;
import org.richfaces.cdk.templatecompiler.el.types.ELPropertyDescriptor;
import org.richfaces.cdk.templatecompiler.el.types.ELType;

/**
 * This class extend AbstractTreeNode and wrap AstPropertySuffix node.
 * 
 * @author amarkhel
 */
public class AstPropertySuffixTreeNode extends AbstractTreeNode {

    public AstPropertySuffixTreeNode(Node node) {
        super(node);
    }

    @Override
    public void visit(StringBuilder sb, ELVisitor visitor) throws ParsingException {
        String propertyName = getNode().getImage();

        String readMethodName;
        ELType readMethodReturnType;
        ELPropertyDescriptor propertyDescriptor = visitor.getPropertyDescriptor(propertyName);
        readMethodName = propertyDescriptor.getReadMethodName();
        readMethodReturnType = propertyDescriptor.getType();

        visitor.setExpressionType(readMethodReturnType);

        sb.append(ELNodeConstants.DOT);
        sb.append(readMethodName);
        sb.append(ELNodeConstants.LEFT_BRACKET);
        sb.append(ELNodeConstants.RIGHT_BRACKET);
        visitor.setLiteral(false);
    }
}
