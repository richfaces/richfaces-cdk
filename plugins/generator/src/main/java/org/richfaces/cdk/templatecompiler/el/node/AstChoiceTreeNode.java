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

/**
 * This class extend AbstractTreeNode and wrap AstChoice node.
 *
 * @author amarkhel
 *
 */
public class AstChoiceTreeNode extends AbstractTreeNode {
    public AstChoiceTreeNode(Node node) {
        super(node);
    }

    @Override
    public void visit(StringBuilder sb, ELVisitor visitor) throws ParsingException {
        //condition ? correctConditionBranch : incorrectConditionBranch
        String condition = coerceToBoolean(getChildOutput(0, visitor), visitor);
        String correctConditionBranch = getChildOutput(1, visitor);
        ELType correctConditionBranchType = visitor.getType(); 
        String incorrectConditionBranch = getChildOutput(2, visitor);
        ELType incorrectConditionBranchType = visitor.getType(); 
        
        sb.append(ELNodeConstants.LEFT_BRACKET);

        sb.append(condition);
        sb.append(ELNodeConstants.QUESTION_SIGN);
        sb.append(correctConditionBranch);
        sb.append(ELNodeConstants.COLON);
        sb.append(incorrectConditionBranch);
        
        sb.append(ELNodeConstants.RIGHT_BRACKET);
        
        visitor.setLiteral(false);
        if (!correctConditionBranchType.isNullType()) {
            visitor.setExpressionType(correctConditionBranchType);
        } else {
            visitor.setExpressionType(incorrectConditionBranchType);
        }
    }
}
