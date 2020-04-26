package file;

public class ResultData {

	private String myFileName = "";
	private String  otherFileName = "";
	private float result;
	
	public ResultData(String myFileName, String otherFileName, float result) {
		super();
		this.myFileName = myFileName;
		this.otherFileName = otherFileName;
		this.result = result;
	}
	public ResultData(String myFileName) {
		super();
		this.myFileName = myFileName;
	}
	public String getMyFileName() {
		return myFileName;
	}
	public void setMyFileName(String myFileName) {
		this.myFileName = myFileName;
	}
	public String getOtherFileName() {
		return otherFileName;
	}
	public void setOtherFileName(String otherFileName) {
		this.otherFileName = otherFileName;
	}
	public float getResult() {
		return result;
	}
	public void setResult(float result) {
		this.result = result;
	}
	
	
}
