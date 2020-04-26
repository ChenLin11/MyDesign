package file;

import javafx.event.ActionEvent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

//TreeCell 负责每个树节点的显示
public class MyTreeCell extends TreeCell<ItemData>
{
	private ContextMenu addMenu = new ContextMenu();
	public MyTreeCell(){
		 
		 MenuItem addMenuItem = new MenuItem("Add Employee");
         addMenu.getItems().add(addMenuItem);
         addMenuItem.setOnAction((ActionEvent t) -> {
        	ItemData data = new ItemData(true," ","asdg");
     		TreeItem<ItemData> root = new TreeItem<ItemData>(data);
     		getTreeItem().getChildren().add(root);
         });
	}
	@Override
	protected void updateItem(ItemData item, boolean empty)
	{
		super.updateItem(item, empty);
		if(empty == true)
		{
			this.setGraphic(null);
			this.setText("");
		}
		else
		{
			this.setText(item.getName());
			HBox hBox = new HBox(10);
			ImageView iv = new ImageView();
			this.setGraphic(hBox);
			if (!item.getName().contains(".")) {
				iv.setImage(new Image("img/folder.png"));
			}
			else if (item.getName().endsWith(".c")) {
				iv.setImage(new Image("img/myfile.png"));
			}
			else if (item.getName().endsWith(".zip")) {
				iv.setImage(new Image("img/zip.png"));
			}

			hBox.getChildren().add(iv);
			this.setGraphic(hBox);
			
			
		}
	}		
}
