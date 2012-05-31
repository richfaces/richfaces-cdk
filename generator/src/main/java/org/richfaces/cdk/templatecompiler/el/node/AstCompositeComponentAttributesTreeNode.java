package org.richfaces.cdk.templatecompiler.el.node;

import org.jboss.el.parser.Node;
import org.richfaces.cdk.templatecompiler.el.ELVisitor;
import org.richfaces.cdk.templatecompiler.el.ParsingException;

/**
 * @author Lukas Fryc
 */
public class AstCompositeComponentAttributesTreeNode extends AbstractTreeNode {
    public AstCompositeComponentAttributesTreeNode(Node node) {
        super(node);
    }

    @Override
    public void visit(StringBuilder sb, ELVisitor visitor) throws ParsingException {
    }
}
