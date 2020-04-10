package application;

import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.TreeNode;

import Similiar.CFile;
import Similiar.TextSimiliar;
import file.InitTreeView;
import file.ItemData;
import file.MyTreeCell;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.util.Callback;
import myTree.CreateMyJTree;
import myTree.MyJtreeNode;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

public class Main extends Application
{
	TreeView<ItemData> treeView = new TreeView<ItemData>();

	@Override
	public void start(Stage primaryStage)
	{
		try
		{
			
			BorderPane pane = new BorderPane();
			
			InitTreeView initTreeView = new InitTreeView(treeView);
			pane.setLeft(treeView);
			
			Scene scene = new Scene(pane, 1200, 600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			
			//CreateMyJTree myJTree1 = new CreateMyJTree(new File("D:\\20191230025851\\1208801141872599055\\17201125_1175583248878112768_1208801141872599055.c"));
			//JTree jTree = myJTree1.getJTree();
			CreateMyJTree myJTree3 = new CreateMyJTree(new File("D:\\20191230025851\\1208801141872599055\\19201503_1173833074035929088_1208801141872599055.c"));
			//CreateMyJTree myJTree2 = new CreateMyJTree(new File("D:\\C_code\\StringSort.c"));
			JFrame f = new JFrame("JTreeDemo");
	        //f.add(myJTree1.getJTree());
	        f.add(myJTree3.getJTree());
	        f.setSize(600, 1000);
	        f.setVisible(true);
	        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        
		
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) throws Exception
	{
		
        launch(args);
	}
}

