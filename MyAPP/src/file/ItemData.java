package file;

public class ItemData {
	private String name; // �ļ�����Ŀ¼��
	private boolean isDir = false; // �Ƿ�ΪĿ¼
	private String path;//�ļ�·��
	public ItemData() 
	{			
	}
	public ItemData(boolean isDir,String path, String name)
	{
		this.path = path;
		this.isDir = isDir;
		this.name = name;
	}
	public boolean getIsDir() {
		return isDir;
	}
	public String getName() {
		return name;
	}
	public String getPath() {
		return path;
	}
	public void setIsDir(boolean isDir) {
		this.isDir = isDir;
	}

}
