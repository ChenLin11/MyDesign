package myTree;

public class MyJtreeNode {
	private String nodeType = "";//节点类型
	private String nodeContent = "";//节点内容
	private int startLine;//节点在原文的首行号
	private int endLine;//节点在原文的首行号
	
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
