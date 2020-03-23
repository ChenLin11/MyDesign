package file;

import javafx.scene.control.TreeCell;

//TreeCell ����ÿ�����ڵ����ʾ
public class MyTreeCell extends TreeCell<ItemData>
{
	@Override
	protected void updateItem(ItemData item, boolean empty)
	{
		super.updateItem(item, empty);
		if(item == null)
		{
			this.setText("");
		}
		else
		{
			this.setGraphic(getTreeItem().getGraphic());
			this.setText(item.getName());
		}
	}		
}
