package Similiar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.cdt.core.dom.ast.IASTComment;
import org.eclipse.cdt.core.dom.ast.IASTPreprocessorStatement;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.gnu.cpp.GPPLanguage;
import org.eclipse.cdt.core.model.ILanguage;
import org.eclipse.cdt.core.parser.DefaultLogService;
import org.eclipse.cdt.core.parser.FileContent;
import org.eclipse.cdt.core.parser.IncludeFileContentProvider;
import org.eclipse.cdt.core.parser.ScannerInfo;

public class CFile implements FileType {
	private String text = "";
	private String[] textOrignal;//Դ�ļ�����
	private File file;
	public void file(File file) throws Exception {
		this.file = file;
		formatCode();
	}
	/**
     * ����������Ԫ
     * @param source
     * @return
     * @throws Exception
     */
    private IASTTranslationUnit getTranslationUnit() throws Exception{
        FileContent reader = FileContent.create(
                file.getAbsolutePath(), 
                getContentFile(file).toCharArray());

    	
        //C++��GPPLanguage������C��GCCLanguage����
        return GPPLanguage.getDefault().getASTTranslationUnit(
                reader, 
                new ScannerInfo(), 
                IncludeFileContentProvider.getSavedFilesProvider(), 
                null, 
                ILanguage.OPTION_IS_SOURCE_UNIT, 
                new DefaultLogService());
               
        
//        return GCCLanguage.getDefault().getASTTranslationUnit(
//                reader, 
//                new ScannerInfo(), 
//                IncludeFileContentProvider.getSavedFilesProvider(), 
//                null, 
//                ILanguage.OPTION_IS_SOURCE_UNIT, 
//                new DefaultLogService());
    }

    /**
     * ����ļ��е�����
     * @param file
     * @return
     * @throws IOException
     */
    private String getContentFile(File file) throws IOException {
    	//System.out.println("����ļ��е�����");
        StringBuilder content = new StringBuilder();
        String line;

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file)))) {
            while ((line = br.readLine()) != null) {
                content.append(line).append('\n');
                //���ļ���ÿһ����ӵ�text�洢����
                text += line+"\n";
                //��ΪԴ�ļ����浽textOrignal 
                textOrignal = text.split("\n");
            }
        }
        //System.out.println(getText());
        
        return content.toString();
    }
	//@Override
	public void formatCode() throws Exception {
		// TODO Auto-generated method stub
		/**
         * Ҫ��ȥ���ֶ��� "int","char","struct","float","return"
         * ���ţ�   { } �� ��
         * ���ַ�  '\0'  ���з�  '\n'   ˮƽ�Ʊ�� '\t'  �س��� '\r'
         * ע���磺    //��  /* * / ,�Լ����е�������
         */
		IASTTranslationUnit u = getTranslationUnit();
		
		//��ȡע��
        IASTComment[]  com = u.getComments();
        for (IASTComment iastComment : com) {
			//System.out.println("IASTComment:"+iastComment);
			//��ȥtext�е�ע��
			String s = iastComment.toString();
			//System.out.println(s);
			text = text.replace(s, "\n");
		}
        /**
         * �õ�Ԥ�������
         * #include <queue>
		 * #define maxnum 120   ��һ��
         */
        IASTPreprocessorStatement[] ps = u.getAllPreprocessorStatements();
        for (IASTPreprocessorStatement iastPreprocessorStatement : ps) {
			//System.out.println(iastPreprocessorStatement.getRawSignature());
			String s = iastPreprocessorStatement.toString();
			//ֻ��ȥͷ�ļ�
			if(s.contains("include"))
				text= text.replace(s, "\n");
		}
        
        //System.out.println(getText());
		String[] symbol = {"\0","\n","\r","\t","{","}","\r\n"};
		for(String s : symbol) {
			text = text.replace(s,"");
		}
		
	}
	@Override
	public String getText() {
		return text.toLowerCase();
	}
	public String[] getOrignalFile() {
		return textOrignal;
	}
}
