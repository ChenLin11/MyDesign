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
	private String[] textOrignal;//源文件内容
	private File file;
	public void file(File file) throws Exception {
		this.file = file;
		formatCode();
	}
	/**
     * 创建解析单元
     * @param source
     * @return
     * @throws Exception
     */
    private IASTTranslationUnit getTranslationUnit() throws Exception{
        FileContent reader = FileContent.create(
                file.getAbsolutePath(), 
                getContentFile(file).toCharArray());

    	
        //C++用GPPLanguage解析，C用GCCLanguage解析
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
     * 获得文件中的内容
     * @param file
     * @return
     * @throws IOException
     */
    private String getContentFile(File file) throws IOException {
    	//System.out.println("获得文件中的内容");
        StringBuilder content = new StringBuilder();
        String line;

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file)))) {
            while ((line = br.readLine()) != null) {
                content.append(line).append('\n');
                //将文件中每一行添加到text存储起来
                text += line+"\n";
                //作为源文件保存到textOrignal 
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
         * 要除去的字段有 "int","char","struct","float","return"
         * 符号：   { } ， ；
         * 空字符  '\0'  换行符  '\n'   水平制表符 '\t'  回车符 '\r'
         * 注释如：    //，  /* * / ,以及其中的中文字
         */
		IASTTranslationUnit u = getTranslationUnit();
		
		//获取注释
        IASTComment[]  com = u.getComments();
        for (IASTComment iastComment : com) {
			//System.out.println("IASTComment:"+iastComment);
			//除去text中的注释
			String s = iastComment.toString();
			//System.out.println(s);
			text = text.replace(s, "\n");
		}
        /**
         * 得到预处理语句
         * #include <queue>
		 * #define maxnum 120   这一类
         */
        IASTPreprocessorStatement[] ps = u.getAllPreprocessorStatements();
        for (IASTPreprocessorStatement iastPreprocessorStatement : ps) {
			//System.out.println(iastPreprocessorStatement.getRawSignature());
			String s = iastPreprocessorStatement.toString();
			//只除去头文件
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
