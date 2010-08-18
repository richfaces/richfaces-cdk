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

/**
 * Interface for all wrappers of org.jboss.el.parser.Node class.
 *
 * @author amarkhel
 */
public interface ITreeNode {

    /**
     * Return node of current wrapper.
     *
     * @return instance of org.jboss.el.parser.Node
     */
    Node getNode();

    /**
     * Visit current node. Generate Java code, that represent current node.
     * @param visitor       - ELVisitor
     * @param stringBuilder instance to collect information.
     *
     * @return instance of org.jboss.el.parser.Node
     * @throws ParsingException - if error occurred during parsing process.
     */
    void visit(StringBuilder sb, ELVisitor visitor) throws ParsingException;

    /**
     * Return child of wrapped node by specified index
     *
     * @param index - index of child.
     * @param visitor TODO
     * @return wrapper for child
     * @throws ParsingException - if error occurred(child not found).
     */
    ITreeNode getChild(int index, ELVisitor visitor) throws ParsingException;

    /**
     * Returns count of children for this node
     * @return children count
     */
    int getChildrenCount();

}
