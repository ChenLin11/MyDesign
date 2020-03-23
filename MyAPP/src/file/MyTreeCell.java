package file;

import javafx.scene.control.TreeCell;

//TreeCell 负责每个树节点的显示
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
