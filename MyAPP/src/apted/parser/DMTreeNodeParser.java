package apted.parser;

import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;

import apted.node.AptedNode;
import apted.node.MyStringNodeData;
import myTree.MyJtreeNode;

public class DMTreeNodeParser implements MyInputParser<MyStringNodeData> {

	@SuppressWarnings("unchecked")
	public AptedNode<MyStringNodeData> fromNode(DefaultMutableTreeNode dmTreeNode){
		//get node's type as aptedNode
		String nodeType = ((MyJtreeNode)dmTreeNode.getUserObject()).getNodeType();
		String nodeContent =  ((MyJtreeNode)dmTreeNode.getUserObject()).getNodeContent();
		AptedNode<MyStringNodeData> node = new AptedNode<MyStringNodeData>(new MyStringNodeData(nodeType,nodeContent));
		//node add children which come from dmTreeNode's children
		formatUtilities(node,dmTreeNode.children());
	   
	    return node; 
	}
	@SuppressWarnings("unchecked")
	private void formatUtilities(AptedNode<MyStringNodeData> rootAptedNode,Enumeration<DefaultMutableTreeNode> node) {

    	while (node.hasMoreElements()) {

    		DefaultMutableTreeNode currentNode = node.nextElement();
    	
    		String nodeType = ((MyJtreeNode)currentNode.getUserObject()).getNodeType();
    		String nodeContent =  ((MyJtreeNode)currentNode.getUserObject()).getNodeContent();
    		AptedNode<MyStringNodeData> aptedNode = new AptedNode<MyStringNodeData>(new MyStringNodeData(nodeType,nodeContent));
    		rootAptedNode.addChild(aptedNode);
    	
    		if (currentNode.children().hasMoreElements()) {
    			formatUtilities(aptedNode,currentNode.children());
			}
    	
		}
	}
	
}
