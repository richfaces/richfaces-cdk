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
import org.richfaces.cdk.templatecompiler.el.types.ELType;
import org.richfaces.cdk.templatecompiler.el.types.TypesFactory;

/**
 * This class extend AbstractTreeNode and wrap AstBracketSuffix node.
 *
 * @author amarkhel
 */
public class AstBracketSuffixTreeNode extends AbstractTreeNode {
    public AstBracketSuffixTreeNode(Node node) {
        super(node);
    }

    @Override
    public void visit(StringBuilder sb, ELVisitor visitor) throws ParsingException {
        ELType variableType = visitor.getType();
        String suffixValue = getChildOutput(0, visitor);

        if (variableType.isArray()) {
            sb.append(ELNodeConstants.LEFT_SQUARE_BRACKET);
            sb.append(suffixValue);
            sb.append(ELNodeConstants.RIGHT_SQUARE_BRACKET);
        } else {
            sb.append(ELNodeConstants.DOT);
            sb.append(ELNodeConstants.GET_FUNCTION);
            sb.append(ELNodeConstants.LEFT_BRACKET);
            sb.append(suffixValue);
            sb.append(ELNodeConstants.RIGHT_BRACKET);
        }

        ELType containerType = variableType.getContainerType();
        if (containerType == null) {
            containerType = TypesFactory.OBJECT_TYPE;
        }
        visitor.setLiteral(false);
        visitor.setExpressionType(containerType);
    }
}
