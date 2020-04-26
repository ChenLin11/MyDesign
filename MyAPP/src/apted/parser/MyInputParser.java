package apted.parser;

import javax.swing.tree.DefaultMutableTreeNode;

import apted.node.AptedNode;

public interface MyInputParser<D> {
	 public AptedNode<D> fromNode(DefaultMutableTreeNode dmTreeNode);
}
