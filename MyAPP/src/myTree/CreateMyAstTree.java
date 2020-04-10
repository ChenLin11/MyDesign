package myTree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.cdt.core.dom.ast.ExpansionOverlapsBoundaryException;
import org.eclipse.cdt.core.dom.ast.IASTDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTForStatement;
import org.eclipse.cdt.core.dom.ast.IASTFunctionDefinition;
import org.eclipse.cdt.core.dom.ast.IASTIfStatement;
import org.eclipse.cdt.core.dom.ast.IASTNode;
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
public class CreateMyAstTree {
	private MyAstNode rootAstNode;

	IASTTranslationUnit u ;
	//�����ļ��������ڵ�
	public CreateMyAstTree(File file) throws Exception {
		// TODO Auto-generated constructor stub
		//���뵥Ԫ
		u = getTranslationUnit(file);
		rootAstNode = new MyAstNode(u);
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
    	//System.out.println("����ļ��е�����");
        StringBuilder content = new StringBuilder();
        String line;

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file)))) {
            while ((line = br.readLine()) != null) {
                content.append(line).append('\n');
            }
        }
        //System.out.println(getText());
        
        return content.toString();
    }
    //�������ļ�������ASTTree
    public MyAstNode getMyAstTree() throws ExpansionOverlapsBoundaryException {
    	//�õ��ļ��ж��������
        IASTDeclaration[] decs = u.getDeclarations();
        //�õ���������
        for ( IASTDeclaration child : decs) {
        	if (child instanceof IASTFunctionDefinition){
        		//�������ڵ����Ϊ rootAstNode�ӽڵ�
        		MyAstNode node = new MyAstNode(child);
				rootAstNode.addMyChild(node);
				//�������ĺ����崫������
				visitScope(node,((IASTFunctionDefinition) child).getBody().getChildren());
			}
        }
		return rootAstNode;
	}
    
    //���뺯����/�����飬��һ������
  	private void visitScope(MyAstNode root,IASTNode[] iastNodes) throws ExpansionOverlapsBoundaryException {
  		
  		//���û���ӽڵ㣬���˳�
  		if(iastNodes.length ==0 ) {
  			return;
  		}
  		for(IASTNode iastNode : iastNodes) {
  			
  			//�ж����Ķ������
  			
  			//for ���
  			if (iastNode instanceof IASTForStatement) {
  				//������for�����뵽�ӽڵ�
  				MyAstNode forNode = new MyAstNode(iastNode);
  				root.addMyChild(forNode);
  				if (((IASTForStatement) iastNode).getInitializerStatement()!=null) {
  					//����г�ʼ��,���뵽for�ڵ���ӽڵ���
  					forNode.addMyChild(new MyAstNode(((IASTForStatement) iastNode).getInitializerStatement()));
				}
  				if (((IASTForStatement) iastNode).getConditionExpression()!=null) {
  					//�����ѭ������,���뵽for�ڵ���ӽڵ���	
  					forNode.addMyChild(new MyAstNode(((IASTForStatement) iastNode).getConditionExpression()));
				}
  				if (((IASTForStatement) iastNode).getIterationExpression()!=null) {
  					//����е�����ϵ,���뵽for�ڵ���ӽڵ���	
  	  				forNode.addMyChild(new MyAstNode(((IASTForStatement) iastNode).getIterationExpression()));
				}
  				MyAstNode node = new MyAstNode(((IASTForStatement) iastNode).getBody());
  				//��ѭ������뵽for�ڵ���ӽڵ���	
  				forNode.addMyChild(node);
  				visitScope(node,((IASTForStatement) iastNode).getBody().getChildren());
  			}
  			//if ���
  			else if(iastNode instanceof IASTIfStatement){
  				//������if �����뵽�ӽڵ�
  				MyAstNode ifNode = new MyAstNode(iastNode);
  				root.addMyChild(ifNode);
  				//��if�����еĿ���������if�ڵ���ӽڵ���
  				ifNode.addMyChild(new MyAstNode(((IASTIfStatement) iastNode).getConditionExpression()));
  				
  				if(((IASTIfStatement)iastNode).getThenClause()!=null) {//��else���ʱ
						MyAstNode node = new MyAstNode(((IASTIfStatement) iastNode).getThenClause());
						//��if(){}�����if�ڵ���ӽڵ���
						ifNode.addMyChild(node);
						visitScope(node,((IASTIfStatement)iastNode).getThenClause().getChildren());
				}
				if(((IASTIfStatement)iastNode).getElseClause()!=null) {//��else���ʱ
						MyAstNode node = new MyAstNode(((IASTIfStatement) iastNode).getElseClause());
						//��else if(){}������ӽڵ�
						ifNode.addMyChild(node);
						visitScope(node,((IASTIfStatement)iastNode).getElseClause().getChildren());
				}
  			}
  			//while ���
  			else if (iastNode instanceof IASTWhileStatement) {
				//������while�������ӽڵ�
  				MyAstNode whileNode = new MyAstNode(iastNode);
  				root.addMyChild(whileNode);
  				//�������������뵽while�ڵ���ӽڵ���
				whileNode.addMyChild(new MyAstNode(((IASTWhileStatement)iastNode).getCondition()));
				//��ѭ��������ӽڵ�
				MyAstNode whileScopeNode = new MyAstNode(((IASTWhileStatement)iastNode).getBody());
				whileNode.addMyChild(whileScopeNode);
				//������ѭ����
				visitScope(whileScopeNode,((IASTWhileStatement)iastNode).getBody().getChildren());
		
			}
  			//switch���
  			else if (iastNode instanceof IASTSwitchStatement) {
  				MyAstNode switchNode = new MyAstNode(iastNode);
  				root.addMyChild(switchNode);
  				switchNode.addMyChild(new MyAstNode(((IASTSwitchStatement)iastNode).getControllerExpression()));
  				MyAstNode caseNode = new MyAstNode(((IASTSwitchStatement)iastNode).getBody());
  				//��{}�����switch�ڵ�
  				switchNode.addMyChild(caseNode);
  				visitScope(caseNode,((IASTSwitchStatement)iastNode).getBody().getChildren());
			}
  			//����û�з�֧�����ֱ�����Ϊ�ӽڵ�
  			else {
				root.addMyChild(new MyAstNode(iastNode));
			}
  			
  		}
  	}
  	//��֤
  	public void getNodes() {
		for(MyAstNode node : rootAstNode.getChildrenList()) {
			if(node.getNode() instanceof IASTFunctionDefinition) {
				System.out.println(((IASTFunctionDefinition)node.getNode()).getDeclarator().getName());
				System.out.println(node.getNode().hashCode());
				next(node);
			}
		}
	}
  	private void next(MyAstNode node) {
  		for(MyAstNode node1 :node.getChildrenList()) {
			System.out.println(node1.getNode().getRawSignature());
			System.out.println(node1.getNode().hashCode());
			if (!node1.isLeaf()) {
				next(node1);
			}
		}
	}
}
