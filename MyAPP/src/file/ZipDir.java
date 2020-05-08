package file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipDir {

	public ZipDir() {
		// TODO Auto-generated constructor stub
	}
	/**
     * ��ѹzip
     *
     * @param zipFile
     * @param descDir
     * @throws Exception
     */
    public static void unZipFiles(File zipFile, String descDir) throws Exception {
        System.out.println("******************��ѹ��ʼ********************");
        File pathFile = new File(descDir);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        ZipFile zip = new ZipFile(zipFile);
        for (Enumeration entries = zip.entries(); entries.hasMoreElements();) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            String zipEntryName = entry.getName();
            InputStream in = zip.getInputStream(entry);
            String outPath = (descDir + "/" + zipEntryName).replaceAll("\\*", "/");
            // �ж�·���Ƿ����,�������򴴽��ļ�·��
            File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
            if (!file.exists()) {
                file.mkdirs();
            }
            // �ж��ļ�ȫ·���Ƿ�Ϊ�ļ���,����������Ѿ��ϴ�,����Ҫ��ѹ
            if (new File(outPath).isDirectory()) {
                continue;
            }
            OutputStream out = new FileOutputStream(outPath);
            byte[] buf1 = new byte[1024];
            int len;
            while ((len = in.read(buf1)) > 0) {
                out.write(buf1, 0, len);
            }
            if ((zipEntryName.trim().lastIndexOf("/")) == -1) {

            }
            in.close();
            out.close();
        }

        System.out.println("******************��ѹ���********************");
        System.out.println("******************�����ļ���********************");
        String parent_id = "";
       // traverse(descDir,parent_id);
    }
	
    
    /**
     * �ļ��б���
     * @param path
     * @throws Exception
     */
    public static void traverse(String path,String parent_id) throws Exception {
        System.out.println("path---->" + path);
        File file = new File(path);
        Map<String, Object> map = new HashMap<String, Object>();
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length == 0) {
                System.out.println("�ļ����ǿյ�!");
                return;
            } else {
                String k_id = UUID.randomUUID().toString();

                for (File file2 : files) {
                    if (file2.isDirectory()) {//�ļ���

                        traverse(file2.getAbsolutePath(),parent_id);
                        parent_id =  k_id;
                    } else if (file2.isFile()){//�ļ�
                    	
                    }
                }
            }
        } else {
            System.out.println("�ļ�������!");
        }
    }
}
