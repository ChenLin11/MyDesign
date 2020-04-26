package file;

public class ItemData {
	private String name; // 文件名或目录名
	private boolean isDir = false; // 是否为目录
	private String path;//文件路径
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
