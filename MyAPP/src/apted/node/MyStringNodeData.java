package apted.node;

public class MyStringNodeData {
	private String nodeType = "";//节点类型
	private String nodeContent = "";//节点内容
	public MyStringNodeData(String type,String content) {
		// TODO Auto-generated constructor stub
		nodeType = type;
		nodeContent = content;
	}
	
	public String getNodeType() {
		return nodeType;
	}
	public String getNodeContent() {
		return nodeContent;
	}
	
}
