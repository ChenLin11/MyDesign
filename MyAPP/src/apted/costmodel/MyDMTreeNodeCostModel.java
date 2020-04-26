package apted.costmodel;

import Similiar.TextSimiliar;
import apted.node.AptedNode;
import apted.node.MyStringNodeData;

public class MyDMTreeNodeCostModel implements CostModel<MyStringNodeData> {

	@Override
	public float del(AptedNode<MyStringNodeData> n) {
		// TODO Auto-generated method stub
		return 1.0f;
	}

	@Override
	public float ins(AptedNode<MyStringNodeData> n) {
		// TODO Auto-generated method stub
		return 1.0f;
	}

	@Override
	public float ren(AptedNode<MyStringNodeData> n1, AptedNode<MyStringNodeData> n2) {
		// TODO Auto-generated method stub
		//get two node's node_type and then compare them
		String type1 = n1.getNodeData().getNodeType();
		String type2 = n2.getNodeData().getNodeType();
		if (type1.equals(type2)) {
			TextSimiliar textSimiliar = new TextSimiliar();			
			//当节点类型一样，还要参考节点本文
			//return 1-textSimiliar.levenshtein(n1.getNodeData().getNodeContent(), n2.getNodeData().getNodeContent());
			return 0.0f;
		}
		return 1.0f;
	}

	
}
