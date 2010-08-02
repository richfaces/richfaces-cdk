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
import org.richfaces.cdk.templatecompiler.el.ELVisitor;
import org.richfaces.cdk.templatecompiler.el.ParsingException;
import org.richfaces.cdk.templatecompiler.el.types.TypesFactory;

/**
 * This abstract class implement some methods of ITreeNode interface to using in subclasses.
 * 
 * @author amarkhel
 * 
 */
public abstract class AbstractTreeNode implements ITreeNode {

    private Node node;

    public AbstractTreeNode(Node node) {
        this.node = node;
    }

    /**
     * Collects output from visiting child of the current node with the specified index and returns collected string.
     * 
     * @param childIndex
     * @param context
     * @param visitor
     * @return
     * @throws ParsingException
     */
    protected String getChildOutput(int childIndex, ELVisitor visitor)
        throws ParsingException {

        StringBuilder sb = new StringBuilder();
        getChild(childIndex, visitor).visit(sb, visitor);

        return sb.toString();
    }

    protected String coerceToBoolean(String value, ELVisitor visitor) {
        return visitor.coerceToType(value,  TypesFactory.BOOLEAN_TYPE);
    }
    
    /**
     * Return node of current wrapper.
     * 
     * @return instance of org.jboss.el.parser.Node
     * 
     */
    public Node getNode() {
        return node;
    }

    /**
     * Visit current node. Generate Java code, that represent current node.
     * @param stringBuilder
     *            instance to collect information.
     * @param context
     *            - context to resolve beans
     * @param visitor
     *            - ELVisitor
     * 
     * @throws ParsingException
     *             - if error occurred during parsing process.
     * @return instance of org.jboss.el.parser.Node
     * 
     */
    public ITreeNode getChild(int index, ELVisitor visitor) throws ParsingException {
        Node childNode = getNode().jjtGetChild(index);

        if (null != childNode) {
            ITreeNode treeNode = visitor.determineNodeType(childNode);

            return treeNode;
        } else {
            throw new ParsingException("Child node not found of node " + node.getImage());
        }
    }

    /**
     * Returns count of children for this node
     * 
     * @return children count
     */
    public int getChildrenCount() {
        return getNode().jjtGetNumChildren();
    }

    /**
     * Return child of wrapped node by specified index. Abstract operation to override in subclasses.
     * @param index
     *            - index of child.
     * 
     * @throws ParsingException
     *             - if error occurred(child not found).
     * @return wrapper for child
     * 
     */
    public abstract void visit(StringBuilder sb, ELVisitor visitor) throws ParsingException;

}
