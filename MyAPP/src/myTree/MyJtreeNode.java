package myTree;

public class MyJtreeNode {
	private String nodeType = "";//�ڵ�����
	private String nodeContent = "";//�ڵ�����
	private int startLine;//�ڵ���ԭ�ĵ����к�
	private int endLine;//�ڵ���ԭ�ĵ����к�
	
	public void setNodeType(String type) {
		nodeType = type;
	}
	public String getNodeType() {
		return nodeType;
	}
	public void setStartLine(int start) {
		startLine = start;
	}
	public int getStartLine() {
		return startLine;
	}
	public void setEndLine(int end) {
		endLine = end;
	}
	public int getEndLine() {
		return endLine;
	}
	public void setNodeContent(String content) {
		nodeContent = content;
	}
	public String getNodeContent() {
		return nodeContent;
	}
	public String toString() {
		return nodeType +":"+ nodeContent;
	}
}
