package myTree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import org.eclipse.cdt.core.dom.ast.ExpansionOverlapsBoundaryException;
import org.eclipse.cdt.core.dom.ast.IASTBreakStatement;
import org.eclipse.cdt.core.dom.ast.IASTCaseStatement;
import org.eclipse.cdt.core.dom.ast.IASTDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTDeclarationStatement;
import org.eclipse.cdt.core.dom.ast.IASTDefaultStatement;
import org.eclipse.cdt.core.dom.ast.IASTExpressionStatement;
import org.eclipse.cdt.core.dom.ast.IASTForStatement;
import org.eclipse.cdt.core.dom.ast.IASTFunctionCallExpression;
import org.eclipse.cdt.core.dom.ast.IASTFunctionDefinition;
import org.eclipse.cdt.core.dom.ast.IASTIfStatement;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.IASTReturnStatement;
import org.eclipse.cdt.core.dom.ast.IASTSwitchStatement;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.IASTWhileStatement;
import org.eclipse.cdt.core.dom.ast.gnu.cpp.GPPLanguage;
import org.eclipse.cdt.core.model.ILanguage;
import org.eclipse.cdt.core.parser.DefaultLogService;
import org.eclipse.cdt.core.parser.FileContent;
import org.eclipse.cdt.core.parser.IncludeFileContentProvider;
import org.eclipse.cdt.core.parser.ScannerInfo;

/**
 * 
 * @author hi
 * File file//Դ�ļ�
 * return rootAstNode //���ڵ㣬�ҳ�ʼ�����������ӽڵ�
 */
public class CreateMyJTree {
	private DefaultMutableTreeNode DMtreeNode;
	private JTree jTree;
	private static IASTTranslationUnit u ;
	//�����ļ��������ڵ� 
	public CreateMyJTree(File file) throws Exception {
		// TODO Auto-generated constructor stub
		//���뵥Ԫ
		u = getTranslationUnit(file);
		MyJtreeNode rootJtreeNode = new MyJtreeNode();
		rootJtreeNode.setNodeType("SourceCode");
		rootJtreeNode.setNodeContent(u.getFilePath().substring(u.getFilePath().lastIndexOf("\\")+1, u.getFilePath().length()));//�ļ�����
		rootJtreeNode.setStartLine(u.getFileLocation().getStartingLineNumber());
		rootJtreeNode.setEndLine(u.getFileLocation().getEndingLineNumber());
		DMtreeNode = new DefaultMutableTreeNode(rootJtreeNode);
		
		
	}
	private static IASTTranslationUnit getTranslationUnit(File file) throws Exception{
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
	}
	 /**
     * ����ļ��е�����
     * @param file
     * @return
     * @throws IOException
     */
    private static String getContentFile(File file) throws IOException {
    	
        StringBuilder content = new StringBuilder();
        String line;

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file)))) {
            while ((line = br.readLine()) != null) {
                content.append(line).append('\n');
            }
        }        
        return content.toString();
    }
    
    public JTree getJTree() throws Exception {
  		jTree = new JTree(DMtreeNode);
  		//�������ڵ��µ�����
  		//�õ��ļ��ж��������
        IASTDeclaration[] decs = u.getDeclarations();
        //�õ���������
        for ( IASTDeclaration child : decs) {
        	
        	//��������
    		if (child instanceof IASTFunctionDefinition) {
    			MyJtreeNode rootJtreeNode = new MyJtreeNode();
    			//���ú�������
    			rootJtreeNode.setNodeType("FunctionDefinition");
    			//���ú���������
    			rootJtreeNode.setNodeContent(((IASTFunctionDefinition)child).getDeclarator().getRawSignature());
    			rootJtreeNode.setStartLine(child.getFileLocation().getStartingLineNumber());
    			rootJtreeNode.setEndLine(child.getFileLocation().getEndingLineNumber());

    			DefaultMutableTreeNode node = new DefaultMutableTreeNode(rootJtreeNode);
    			DMtreeNode.add(node);
    			visitScope(node,((IASTFunctionDefinition) child).getBody().getChildren());
    		}
        }
        
		return jTree;
	}
    //���뺯����/�����飬��һ������
  	private void visitScope(DefaultMutableTreeNode root,IASTNode[] iastNodes) throws Exception {
  		
  		//���û���ӽڵ㣬���˳�
  		if(iastNodes.length ==0 ) {
  			return;
  		}
  		for(IASTNode iastNode : iastNodes) {
  			
  			//�ж����Ķ������
  			
  			//forStatement
  			if (iastNode instanceof IASTForStatement) {
  				visitForStatement(root, iastNode);
  			}
  			//ifStatement
  			else if(iastNode instanceof IASTIfStatement){
  				visitIfStatement(root, iastNode);

  			}
  			//whileStatement
  			else if (iastNode instanceof IASTWhileStatement) {
  				MyJtreeNode jtreeNode = new MyJtreeNode();
  				jtreeNode.setNodeType("WhileStatement");
  				jtreeNode.setNodeContent(((IASTWhileStatement)iastNode).getCondition().getRawSignature());//while�����еĿ������
  				jtreeNode.setStartLine(iastNode.getFileLocation().getStartingLineNumber());
  				jtreeNode.setEndLine(iastNode.getFileLocation().getEndingLineNumber());
  				DefaultMutableTreeNode whileNode = new DefaultMutableTreeNode(jtreeNode);
  				root.add(whileNode);
				
				//������ѭ����
				visitScope(whileNode,((IASTWhileStatement)iastNode).getBody().getChildren());
		
			}
  			//switchStatement
  			else if (iastNode instanceof IASTSwitchStatement) {
  				MyJtreeNode jtreeNode = new MyJtreeNode();
  				jtreeNode.setNodeType("SwitchStatement");
  				jtreeNode.setNodeContent(((IASTSwitchStatement)iastNode).getControllerExpression().getRawSignature());
  				jtreeNode.setStartLine(iastNode.getFileLocation().getStartingLineNumber());
  				jtreeNode.setEndLine(iastNode.getFileLocation().getEndingLineNumber());
  				DefaultMutableTreeNode switchNode = new DefaultMutableTreeNode(jtreeNode);
  				root.add(switchNode);
  				//��{}�����switch�ڵ�
  				visitScope(switchNode,((IASTSwitchStatement)iastNode).getBody().getChildren());
			}
  			else if(iastNode instanceof IASTCaseStatement) {
  				MyJtreeNode jtreeNode = new MyJtreeNode();
  				jtreeNode.setNodeType("CaseStatement");
  				jtreeNode.setNodeContent(((IASTCaseStatement)iastNode).getChildren()[0].getRawSignature());//case �����
  				jtreeNode.setStartLine(iastNode.getFileLocation().getStartingLineNumber());
  				jtreeNode.setEndLine(iastNode.getFileLocation().getEndingLineNumber());
  				DefaultMutableTreeNode caseNode = new DefaultMutableTreeNode(jtreeNode);
  				root.add(caseNode);
			}
  			else if (iastNode instanceof IASTBreakStatement) {
  				MyJtreeNode jtreeNode = new MyJtreeNode();
  				jtreeNode.setNodeType("BreakStatement");
  				jtreeNode.setNodeContent(((IASTBreakStatement)iastNode).getRawSignature());
  				jtreeNode.setStartLine(iastNode.getFileLocation().getStartingLineNumber());
  				jtreeNode.setEndLine(iastNode.getFileLocation().getEndingLineNumber());
  				DefaultMutableTreeNode breakNode = new DefaultMutableTreeNode(jtreeNode);
  				root.add(breakNode);
  			}
  			else if (iastNode instanceof IASTDefaultStatement) {
  				MyJtreeNode jtreeNode = new MyJtreeNode();
  				jtreeNode.setNodeType("DefaultStatement");
  				jtreeNode.setNodeContent(((IASTDefaultStatement)iastNode).getRawSignature());
  				jtreeNode.setStartLine(iastNode.getFileLocation().getStartingLineNumber());
  				jtreeNode.setEndLine(iastNode.getFileLocation().getEndingLineNumber());
  				DefaultMutableTreeNode defaultNode = new DefaultMutableTreeNode(jtreeNode);
  				root.add(defaultNode);
  			}
  			//����û�з�֧�����ֱ�����Ϊ�ӽڵ�
  			else if (iastNode instanceof IASTDeclarationStatement) {
  				MyJtreeNode jtreeNode = new MyJtreeNode();
  				jtreeNode.setNodeType("DeclarationStatement");
  				jtreeNode.setNodeContent(iastNode.getRawSignature());
  				jtreeNode.setStartLine(iastNode.getFileLocation().getStartingLineNumber());
  				jtreeNode.setEndLine(iastNode.getFileLocation().getEndingLineNumber());
  				DefaultMutableTreeNode node = new DefaultMutableTreeNode(jtreeNode);
  				root.add(node);
			}
  			else if (iastNode instanceof IASTExpressionStatement) {
  				MyJtreeNode jtreeNode = new MyJtreeNode();
  				jtreeNode.setNodeType("ExpressionStatement");
  				jtreeNode.setNodeContent(iastNode.getRawSignature());
  				jtreeNode.setStartLine(iastNode.getFileLocation().getStartingLineNumber());
  				jtreeNode.setEndLine(iastNode.getFileLocation().getEndingLineNumber());
  				DefaultMutableTreeNode node = new DefaultMutableTreeNode(jtreeNode);
  				root.add(node);
			}
  			else if (iastNode instanceof IASTReturnStatement) {
  				MyJtreeNode jtreeNode = new MyJtreeNode();
  				jtreeNode.setNodeType("ReturnStatement");
  				jtreeNode.setNodeContent(((IASTReturnStatement) iastNode).getReturnValue().getRawSignature());
  				jtreeNode.setStartLine(iastNode.getFileLocation().getStartingLineNumber());
  				jtreeNode.setEndLine(iastNode.getFileLocation().getEndingLineNumber());
  				DefaultMutableTreeNode node = new DefaultMutableTreeNode(jtreeNode);
  				root.add(node);
			}
  			else if (iastNode instanceof IASTFunctionCallExpression) {
  				MyJtreeNode jtreeNode = new MyJtreeNode();
  				jtreeNode.setNodeType("FunctionCall");
  				jtreeNode.setNodeContent(iastNode.getRawSignature());
  				jtreeNode.setStartLine(iastNode.getFileLocation().getStartingLineNumber());
  				jtreeNode.setEndLine(iastNode.getFileLocation().getEndingLineNumber());
  				DefaultMutableTreeNode node = new DefaultMutableTreeNode(jtreeNode);
  				root.add(node);
			}
//  			else {
//  				MyJtreeNode jtreeNode = new MyJtreeNode();
//  				jtreeNode.setNodeType("Expression");
//  				jtreeNode.setNodeContent(iastNode.getRawSignature());
//  				jtreeNode.setStartLine(iastNode.getFileLocation().getStartingLineNumber());
//  				jtreeNode.setEndLine(iastNode.getFileLocation().getEndingLineNumber());
//  				DefaultMutableTreeNode node = new DefaultMutableTreeNode(jtreeNode);
//  				root.add(node);		
//			}
  		}
  	}
  	//body in forStatement
  	private void visitForStatement(DefaultMutableTreeNode root,IASTNode forStatement) throws Exception {
  		//System.out.println("body0:"+forStatement.getRawSignature());
  		MyJtreeNode jtreeNode = new MyJtreeNode();
		jtreeNode.setNodeType("ForStatement");
		String string = "";
		if(((IASTForStatement) forStatement).getInitializerStatement()!=null) {
			string += ((IASTForStatement) forStatement).getInitializerStatement().getRawSignature();//i=0
		}
		if(((IASTForStatement) forStatement).getConditionExpression()!=null) {
			string += ((IASTForStatement) forStatement).getConditionExpression().getRawSignature();//i<n
		}
		if(((IASTForStatement) forStatement).getIterationExpression()!=null) {
			string += ((IASTForStatement) forStatement).getIterationExpression().getRawSignature();//i++
		}
		jtreeNode.setNodeContent(string);
		jtreeNode.setStartLine(forStatement.getFileLocation().getStartingLineNumber());
		jtreeNode.setEndLine(forStatement.getFileLocation().getEndingLineNumber());
		
		DefaultMutableTreeNode forNode =new DefaultMutableTreeNode(jtreeNode);
		root.add(forNode);
		//����forѭ���Ľṹ
		//to resolve the situation of "for{ for{}  }"
		if(((IASTForStatement) forStatement).getBody() instanceof IASTForStatement) {
			//System.out.println("body0:"+((IASTForStatement) forStatement).getBody().getRawSignature());
			visitForStatement(forNode, ((IASTForStatement) forStatement).getBody());
			
		}
		//to resolve the situation of "for{ if{}  }"
		else if(((IASTForStatement) forStatement).getBody() instanceof IASTIfStatement){
			//System.out.println("body1:"+((IASTForStatement) forStatement).getBody().getRawSignature());
			visitIfStatement(forNode, ((IASTForStatement) forStatement).getBody());	
		}
		else {
			//System.out.println("body2:"+((IASTForStatement) forStatement).getBody().getRawSignature());
			visitScope(forNode,((IASTForStatement) forStatement).getBody().getChildren());
		}
		
	}
  	//if����else����
  	private void visitIfStatement(DefaultMutableTreeNode root,IASTNode ifStatement) throws Exception {
  		MyJtreeNode jtreeNode = new MyJtreeNode();
  		jtreeNode.setNodeType("IfStatement");
		jtreeNode.setNodeContent(((IASTIfStatement) ifStatement).getConditionExpression().getRawSignature());//if�����еĿ������
		jtreeNode.setStartLine(ifStatement.getFileLocation().getStartingLineNumber());
		jtreeNode.setEndLine(ifStatement.getFileLocation().getEndingLineNumber());
		DefaultMutableTreeNode ifNode = new DefaultMutableTreeNode(jtreeNode);
		root.add(ifNode);
		
		if(((IASTIfStatement)ifStatement).getThenClause()!=null) {
//			for(IASTNode node:((IASTIfStatement)ifStatement).getThenClause().getChildren()) {
//				System.out.println("then:"+node.getRawSignature());
//			}
//		
			//if thenStatement is a ifStatement
			if(((IASTIfStatement)ifStatement).getThenClause() instanceof IASTIfStatement) {
				//System.out.println("then1:"+((IASTIfStatement)ifStatement).getThenClause().getRawSignature());
				visitIfStatement(ifNode, ((IASTIfStatement)ifStatement).getThenClause());
			}
			else {
				//System.out.println("then2:"+((IASTIfStatement)ifStatement).getThenClause().getRawSignature());
				visitScope(ifNode,((IASTIfStatement)ifStatement).getThenClause().getChildren());
			}
		}
		if(((IASTIfStatement)ifStatement).getElseClause()!=null) {
//			for(IASTNode node:((IASTIfStatement)ifStatement).getElseClause().getChildren()) {
//				System.out.println("else:"+node.getRawSignature());
//			}
			//System.out.println("else1:"+((IASTIfStatement)ifStatement).getElseClause().getRawSignature());
			//visitScope(ifNode,((IASTIfStatement)iastNode).getElseClause().getChildren());
			
			//if thenStatement is a ifStatement
			if(((IASTIfStatement)ifStatement).getElseClause() instanceof IASTIfStatement) {
				visitIfStatement(ifNode, ((IASTIfStatement)ifStatement).getElseClause());
			}
			//to resolve the situation of "if{ else{}  }"
			else {
				MyJtreeNode node = new MyJtreeNode();
		  		node.setNodeType("ElseStatement");
				node.setNodeContent("");
				node.setStartLine(((IASTIfStatement)ifStatement).getElseClause().getFileLocation().getStartingLineNumber());
				node.setEndLine(((IASTIfStatement)ifStatement).getElseClause().getFileLocation().getEndingLineNumber());
				DefaultMutableTreeNode elseNode = new DefaultMutableTreeNode(node);
				ifNode.add(elseNode);
				visitScope(elseNode,((IASTIfStatement)ifStatement).getElseClause().getChildren());
			}	
		}
  	}
  	
}
