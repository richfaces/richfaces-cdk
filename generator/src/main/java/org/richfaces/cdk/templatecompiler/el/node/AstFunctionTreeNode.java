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


import static org.richfaces.cdk.templatecompiler.el.ELNodeConstants.COMMA;
import static org.richfaces.cdk.templatecompiler.el.ELNodeConstants.LEFT_BRACKET;
import static org.richfaces.cdk.templatecompiler.el.ELNodeConstants.RIGHT_BRACKET;
import static org.richfaces.cdk.templatecompiler.el.ELNodeConstants.THIS_PREFIX;

import org.jboss.el.parser.AstFunction;
import org.jboss.el.parser.Node;
import org.richfaces.cdk.templatecompiler.el.ELVisitor;
import org.richfaces.cdk.templatecompiler.el.ParsingException;
import org.richfaces.cdk.templatecompiler.el.types.ELType;
import org.richfaces.cdk.templatecompiler.el.types.TypesFactory;
import org.richfaces.cdk.templatecompiler.statements.HelperMethod;
import org.richfaces.cdk.util.Strings;

/**
 * This class extend AbstractTreeNode and wrap AstFunction node.
 * 
 * @author amarkhel
 * 
 */
public class AstFunctionTreeNode extends AbstractMethodTreeNode {

    public AstFunctionTreeNode(Node node) {
        super(node);
    }

    @Override
    public void visit(StringBuilder sb, ELVisitor visitor) throws ParsingException {
        AstFunction functionNode = (AstFunction) getNode();
        
        HelperMethod helperMethod = findMatchingHelperMethod(functionNode);
        if (helperMethod != null) {
            visitHelperMethod(sb, visitor, helperMethod, functionNode);
        } else {
            visitObjectMethod(sb, visitor, functionNode);
        }
    }

    /**
     * @param sb
     * @param visitor
     * @param helperMethod
     * @param functionNode
     * @throws ParsingException 
     */
    private void visitHelperMethod(StringBuilder sb, ELVisitor visitor, HelperMethod helperMethod,
        AstFunction functionNode) throws ParsingException {
        visitor.addHelperMethods(helperMethod);
        
        visitor.setLiteral(false);
        
        //TODO - helper method doesn't provide this info
        visitor.setExpressionType(TypesFactory.OBJECT_TYPE);

        sb.append(helperMethod.getName());
        sb.append(LEFT_BRACKET);

        for (int i = 0; i < functionNode.jjtGetNumChildren(); i++) {
            if (i != 0) {
                sb.append(COMMA);
            }
            
            String childOutput = getChildOutput(i, visitor);
            sb.append(childOutput);
        }
        
        sb.append(RIGHT_BRACKET);
    }

    private HelperMethod findMatchingHelperMethod(AstFunction functionNode) {
        if (Strings.isEmpty(functionNode.getPrefix())) {
            for (HelperMethod helperMethod: HelperMethod.values()) {
                if (helperMethod.getName().equals(functionNode.getLocalName())) {
                    return helperMethod;
                }
            }
        }
         
        return null;
    }
    
    private void visitObjectMethod(StringBuilder sb, ELVisitor visitor, AstFunction functionNode)
        throws ParsingException {
        
        String identifierName = Strings.firstNonEmpty(functionNode.getPrefix(), THIS_PREFIX);
        sb.append(identifierName);

        ELType identifierType = visitor.getVariable(identifierName);
        visitor.setExpressionType(identifierType);

        visitMethod(sb, visitor, functionNode.getLocalName());
        visitor.setLiteral(false);
    }
}
