package file;

public class SimilarString {
	//用于显示文件相似之处
	private String  content;
	private boolean isFamiliar;
	public SimilarString(String content,boolean isFamiliar) {
		// TODO Auto-generated constructor stub
		this.content = content ;
		this.isFamiliar = isFamiliar;
	}
	public String getContent() {
		return content;
	}
	public boolean isFamiliar() {
		return isFamiliar;
	}
	
}
