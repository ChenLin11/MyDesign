package file;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

import Similiar.CFile;
import Similiar.TextSimiliar;
import apted.costmodel.MyDMTreeNodeCostModel;
import apted.distance.APTED;
import apted.node.AptedNode;
import apted.node.MyStringNodeData;
import apted.parser.DMTreeNodeParser;
import myTree.CreateMyJTree;

/*
 * 该类用于tableView展示myFileName与arrayList所有文件的相似度结果
 */
public class ResultData {

	private String myFileName = "";
	private String myFilePath = "";
	private float[] results;
	private DMTreeNodeParser parser;
	private ArrayList< ItemData> arrayList;
	
	public ResultData(ArrayList< ItemData> arrayList,int i) throws Exception {
		super();
		myFileName = arrayList.get(i).getName();
		myFilePath = arrayList.get(i).getPath();
		this.arrayList = arrayList;
		parser = new DMTreeNodeParser();
		calculateAPTED(i);
	}
	//第i个IteData的文件与所有文件计算APTED，并存放在数组中
	private void calculateAPTED(int i) throws Exception {
		//第i个IteData的文件的语法树节点
		AptedNode<MyStringNodeData> myAptedNode = parser.fromNode(new CreateMyJTree(new File(myFilePath)).getRootDMTreeNode());
		//第i个IteData的文件的文本节点
		CFile myCFile = new CFile(new File(myFilePath));
		
		//用来计算两个AptedNode相似度的工具类
		APTED<MyDMTreeNodeCostModel, MyStringNodeData> apted = new APTED<>(new MyDMTreeNodeCostModel());
		//用来计算两个CFile相似度的工具类
		TextSimiliar textSimiliar = new TextSimiliar();
		
		results = new float[arrayList.size()];
		for (int j = 0; j < arrayList.size(); j++) {
			//如果文件一样
			if (myFileName.equals(arrayList.get(j).getName())) {
				results[j] = 1;
			}
			else {
				AptedNode<MyStringNodeData> aptedNode = parser.fromNode(new CreateMyJTree(new File(arrayList.get(j).getPath())).getRootDMTreeNode());
				CFile cFile = new CFile(new File(arrayList.get(j).getPath()));
				int maxNodes = myAptedNode.getNodeCount()>aptedNode.getNodeCount()?myAptedNode.getNodeCount():aptedNode.getNodeCount();
				//语法相似度
				float result1 = 1 - apted.computeEditDistance(myAptedNode, aptedNode)/maxNodes;
				//文本相似度
				float result2 = textSimiliar.levenshtein(myCFile.getText(), cFile.getText());
				//语法树:文本  相似度 的权重为 7:3
				results[j] = Float.valueOf(new DecimalFormat("0.00").format(result1 * 0.7 + result2 * 0.3));
		
			}

		}
	}
	public String getMyFileName() {
		if (myFileName.length()>8) {
			return myFileName.substring(0, 8);
		}
		return myFileName;
	}
	public String getMyfilePath() {
		return myFilePath;
	}
	public String getIFileName(String string) {
		int i = location(string);
		if (arrayList.get(i).getName().length()>8) {
			return arrayList.get(i).getName().substring(0, 8);
		}
		return arrayList.get(i).getName();
	}
	public String getIFilePath(String string) {
		int i = location(string);
		return arrayList.get(i).getPath();
	}
	/*
	 * string 是tableView的列名，表示一个文件名
	 * 由文件名查找该文件与用户类文件计算的结果
	 */
	public float getResult(String string) {
		int i = location(string);
		return results[i];
	}
	//根据文件名称查找在arrayList中的位置
	private int location(String string) {
		int i=0;
		
		for (int j = 0; j < arrayList.size(); j++) {
			if (arrayList.get(j).getName().contains(string)) {
				i = j;
			}
		}
		return i;
	}
	
}
