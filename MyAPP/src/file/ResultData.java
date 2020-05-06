package file;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

import apted.costmodel.MyDMTreeNodeCostModel;
import apted.distance.APTED;
import apted.node.AptedNode;
import apted.node.MyStringNodeData;
import apted.parser.DMTreeNodeParser;
import myTree.CreateMyJTree;
import similar.CFile;
import similar.TextSimilar;

/*
 * ��������tableViewչʾmyFileName��arrayList�����ļ������ƶȽ��
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
	//��i��IteData���ļ��������ļ�����APTED���������������
	private void calculateAPTED(int i) throws Exception {
		//��i��IteData���ļ����﷨���ڵ�
		AptedNode<MyStringNodeData> myAptedNode = parser.fromNode(new CreateMyJTree(new File(myFilePath)).getRootDMTreeNode());
		//��i��IteData���ļ����ı��ڵ�
		CFile myCFile = new CFile(new File(myFilePath));
		
		//������������AptedNode���ƶȵĹ�����
		APTED<MyDMTreeNodeCostModel, MyStringNodeData> apted = new APTED<>(new MyDMTreeNodeCostModel());
		//������������CFile���ƶȵĹ�����
		TextSimilar textSimilar = new TextSimilar();
		
		results = new float[arrayList.size()];
		for (int j = 0; j < arrayList.size(); j++) {
			//����ļ�һ��
			if (myFileName.equals(arrayList.get(j).getName())) {
				results[j] = 1.0f;
			}
			else {
				CFile cFile = new CFile(new File(arrayList.get(j).getPath()));
				AptedNode<MyStringNodeData> aptedNode = parser.fromNode(new CreateMyJTree(new File(arrayList.get(j).getPath())).getRootDMTreeNode());					
				int maxNodes = myAptedNode.getNodeCount()>aptedNode.getNodeCount()?myAptedNode.getNodeCount():aptedNode.getNodeCount();
				//�﷨���ƶ�
				float result1 = 1 - apted.computeEditDistance(myAptedNode, aptedNode)/maxNodes;
				//�ı����ƶ�
				float result2 = textSimilar.levenshtein(myCFile.getText(), cFile.getText());
				//�﷨��:�ı�  ���ƶ� ��Ȩ��Ϊ 7:3
				results[j] = Float.valueOf(new DecimalFormat("0.00").format(result1 * 0.7 + result2 * 0.3));
			
			}

		}
	}
	//�õ���ǰ�û�����ļ���ǰ8λ��ѧ�ţ�
	public String getMyFileName() {
		if (myFileName.length()>8) {
			return myFileName.substring(0, 8);
		}
		return myFileName;
	}
	//�õ���ǰ�û�����ļ�ǰ8λ��ѧ�ţ�
	public String getMyFileAllName() {
		return myFileName;
	}	
	public String getMyFilePath() {
		return myFilePath;
	}
	public String getIFileName(String string) {
		int i = location(string);
		if (arrayList.get(i).getName().length()>8) {
			return arrayList.get(i).getName().substring(0, 8);
		}
		return arrayList.get(i).getName();
	}
	//����ѧ���ҵ��ļ�ȫ��
	public String getIFileAllName(String string) {
		int i = location(string);
		return arrayList.get(i).getName();
	}
	//����ѧ���ҵ��ļ�·��
	public String getIFilePath(String string) {
		int i = location(string);
		return arrayList.get(i).getPath();
	}	
	/*
	 * string ��tableView����������ʾһ���ļ���
	 * ���ļ������Ҹ��ļ����û����ļ�����Ľ��
	 */
	public float getResult(String string) {
		int i = location(string);
		return results[i];
	}
	//�����ļ����Ʋ�����arrayList�е�λ��
	private int location(String string) {
		int i=0;
		
		for (int j = 0; j < arrayList.size(); j++) {
			if (arrayList.get(j).getName().contains(string)) {
				i = j;
			}
		}
		return i;
	}
	
	public String toString() {
		return myFileName;
	}
}
