package myTree;

import java.util.ArrayList;

import org.eclipse.cdt.core.dom.ast.IASTNode;

public class MyAstNode {
	private IASTNode node;
	private IASTNode parentNode;//���ڵ�
	private ArrayList<MyAstNode> myChildreNode;//����ӽڵ�
	private String nodeTypeString="";//�ڵ������
	
	
	public MyAstNode(IASTNode node) {
		// TODO Auto-generated constructor stub
		this.node = node;
		myChildreNode = new ArrayList<>();
		
	}
	//�Ƿ���Ҷ�ӽڵ�,�������ӽڵ����
	public boolean isLeaf() {
		return myChildreNode.isEmpty();
	}
	
	//�����ӽڵ�
	public void addMyChild(MyAstNode node) {
		myChildreNode.add(node);
	}
	//δ��ƺ�
	public void setNodeTypeString(String typeString) {
		nodeTypeString = typeString;
	}
	
	public String getNodeTypeString() {
		return nodeTypeString;
	}
	
	//���ظýڵ�
	public IASTNode getNode() {
		return node;
	}
	//�Ƴ�ĳ�ӽڵ�
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
