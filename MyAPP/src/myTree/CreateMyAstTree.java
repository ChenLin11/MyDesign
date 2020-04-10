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
 * File file//源文件
 * return rootAstNode //根节点，且初始化其下所有子节点
 */
public class CreateMyAstTree {
	private MyAstNode rootAstNode;

	IASTTranslationUnit u ;
	//根据文件创建根节点
	public CreateMyAstTree(File file) throws Exception {
		// TODO Auto-generated constructor stub
		//编译单元
		u = getTranslationUnit(file);
		rootAstNode = new MyAstNode(u);
	}
	private static IASTTranslationUnit getTranslationUnit(File file) throws Exception{
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
	}
	 /**
     * 获得文件中的内容
     * @param file
     * @return
     * @throws IOException
     */
    private static String getContentFile(File file) throws IOException {
    	//System.out.println("获得文件中的内容");
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
    //返回由文件创建的ASTTree
    public MyAstNode getMyAstTree() throws ExpansionOverlapsBoundaryException {
    	//得到文件中定义的声明
        IASTDeclaration[] decs = u.getDeclarations();
        //得到方法声明
        for ( IASTDeclaration child : decs) {
        	if (child instanceof IASTFunctionDefinition){
        		//将方法节点加入为 rootAstNode子节点
        		MyAstNode node = new MyAstNode(child);
				rootAstNode.addMyChild(node);
				//将函数的函数体传入其中
				visitScope(node,((IASTFunctionDefinition) child).getBody().getChildren());
			}
        }
		return rootAstNode;
	}
    
    //传入函数体/方法块，进一步解析
  	private void visitScope(MyAstNode root,IASTNode[] iastNodes) throws ExpansionOverlapsBoundaryException {
  		
  		//如果没有子节点，则退出
  		if(iastNodes.length ==0 ) {
  			return;
  		}
  		for(IASTNode iastNode : iastNodes) {
  			
  			//判断语句的多种情况
  			
  			//for 语句
  			if (iastNode instanceof IASTForStatement) {
  				//将整个for语句加入到子节点
  				MyAstNode forNode = new MyAstNode(iastNode);
  				root.addMyChild(forNode);
  				if (((IASTForStatement) iastNode).getInitializerStatement()!=null) {
  					//如果有初始化,加入到for节点的子节点中
  					forNode.addMyChild(new MyAstNode(((IASTForStatement) iastNode).getInitializerStatement()));
				}
  				if (((IASTForStatement) iastNode).getConditionExpression()!=null) {
  					//如果有循环条件,加入到for节点的子节点中	
  					forNode.addMyChild(new MyAstNode(((IASTForStatement) iastNode).getConditionExpression()));
				}
  				if (((IASTForStatement) iastNode).getIterationExpression()!=null) {
  					//如果有迭代关系,加入到for节点的子节点中	
  	  				forNode.addMyChild(new MyAstNode(((IASTForStatement) iastNode).getIterationExpression()));
				}
  				MyAstNode node = new MyAstNode(((IASTForStatement) iastNode).getBody());
  				//将循环体加入到for节点的子节点中	
  				forNode.addMyChild(node);
  				visitScope(node,((IASTForStatement) iastNode).getBody().getChildren());
  			}
  			//if 语句
  			else if(iastNode instanceof IASTIfStatement){
  				//将整个if 语句加入到子节点
  				MyAstNode ifNode = new MyAstNode(iastNode);
  				root.addMyChild(ifNode);
  				//将if（）中的控制语句加入if节点的子节点中
  				ifNode.addMyChild(new MyAstNode(((IASTIfStatement) iastNode).getConditionExpression()));
  				
  				if(((IASTIfStatement)iastNode).getThenClause()!=null) {//有else语句时
						MyAstNode node = new MyAstNode(((IASTIfStatement) iastNode).getThenClause());
						//将if(){}块加入if节点的子节点中
						ifNode.addMyChild(node);
						visitScope(node,((IASTIfStatement)iastNode).getThenClause().getChildren());
				}
				if(((IASTIfStatement)iastNode).getElseClause()!=null) {//有else语句时
						MyAstNode node = new MyAstNode(((IASTIfStatement) iastNode).getElseClause());
						//将else if(){}块加入子节点
						ifNode.addMyChild(node);
						visitScope(node,((IASTIfStatement)iastNode).getElseClause().getChildren());
				}
  			}
  			//while 语句
  			else if (iastNode instanceof IASTWhileStatement) {
				//将整个while语句加入子节点
  				MyAstNode whileNode = new MyAstNode(iastNode);
  				root.addMyChild(whileNode);
  				//将控制条件加入到while节点的子节点中
				whileNode.addMyChild(new MyAstNode(((IASTWhileStatement)iastNode).getCondition()));
				//将循环体加入子节点
				MyAstNode whileScopeNode = new MyAstNode(((IASTWhileStatement)iastNode).getBody());
				whileNode.addMyChild(whileScopeNode);
				//并遍历循环体
				visitScope(whileScopeNode,((IASTWhileStatement)iastNode).getBody().getChildren());
		
			}
  			//switch语句
  			else if (iastNode instanceof IASTSwitchStatement) {
  				MyAstNode switchNode = new MyAstNode(iastNode);
  				root.addMyChild(switchNode);
  				switchNode.addMyChild(new MyAstNode(((IASTSwitchStatement)iastNode).getControllerExpression()));
  				MyAstNode caseNode = new MyAstNode(((IASTSwitchStatement)iastNode).getBody());
  				//将{}块加入switch节点
  				switchNode.addMyChild(caseNode);
  				visitScope(caseNode,((IASTSwitchStatement)iastNode).getBody().getChildren());
			}
  			//其他没有分支的情况直接添加为子节点
  			else {
				root.addMyChild(new MyAstNode(iastNode));
			}
  			
  		}
  	}
  	//验证
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
