package myTree;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class MyJtreeCell extends DefaultTreeCellRenderer {

	public MyJtreeCell() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,  
            boolean sel, boolean expanded, boolean leaf, int row,  
            boolean hasFocus) {  
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;  
        // ���ڵ��0��ʼ����������  
        
        this.setText(value.toString());  
        return this;  
    }  
      
}
