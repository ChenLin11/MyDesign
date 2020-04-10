package myTree;

import java.util.ArrayList;

import org.eclipse.cdt.core.dom.ast.IASTNode;

public class MyAstNode {
	private IASTNode node;
	private IASTNode parentNode;//父节点
	private ArrayList<MyAstNode> myChildreNode;//存放子节点
	private String nodeTypeString="";//节点的类型
	
	
	public MyAstNode(IASTNode node) {
		// TODO Auto-generated constructor stub
		this.node = node;
		myChildreNode = new ArrayList<>();
		
	}
	//是否是叶子节点,视有无子节点而定
	public boolean isLeaf() {
		return myChildreNode.isEmpty();
	}
	
	//设置子节点
	public void addMyChild(MyAstNode node) {
		myChildreNode.add(node);
	}
	//未设计好
	public void setNodeTypeString(String typeString) {
		nodeTypeString = typeString;
	}
	
	public String getNodeTypeString() {
		return nodeTypeString;
	}
	
	//返回该节点
	public IASTNode getNode() {
		return node;
	}
	//移除某子节点
	public void removeMyChild(MyAstNode node) {
		if(myChildreNode.contains(node)) {
			myChildreNode.remove(node);
		}
	}
	public String getString() {
		return node.getRawSignature();
	}
	public ArrayList<MyAstNode> getChildrenList() {
		return myChildreNode;
	}
}
