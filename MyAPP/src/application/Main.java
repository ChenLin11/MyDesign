package application;

import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.TreeNode;

import Similiar.CFile;
import Similiar.TextSimiliar;
import file.ItemData;
import file.MyTreeCell;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.util.Callback;
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
			
			initTreeView();
			pane.setLeft(treeView);
			
			Scene scene = new Scene(pane, 1200, 600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	//��ʼ��TreeView
	private void initTreeView() throws Exception 
	{
		/*
		 * CFile cFile = new CFile(); cFile.file(new File("D:\\C_code\\MYF2.c"));
		 * System.out.println(cFile.getText());
		 */
		CFile cfile1 = new CFile();
		cfile1.file(new File("D:\\20191230025851\\1208801141872599055\\17201125_1175583248878112768_1208801141872599055.c"));
		System.out.println(cfile1.getText());
		CFile cfile2 = new CFile();
		cfile2.file(new File("D:\\20191230025851\\1208801141872599055\\19201501_1173824973882798080_1208801141872599055.c"));
		System.out.println(cfile2.getText());
		TextSimiliar similiar = new TextSimiliar(cfile1.getText(),cfile2.getText());
		System.out.println(similiar.levenshtein());
		
		ItemData data_root = new ItemData(true," ","�ļ�����");
		TreeItem<ItemData> root = new TreeItem<ItemData>(data_root);
		
		root.setExpanded(true);
		//1. ��ȡ�����̷�
		File[] roots = File.listRoots();
		FileSystemView sys = FileSystemView.getFileSystemView();
		for (int i = 0; i < roots.length; i++) {
		    if(sys.getSystemTypeDescription(roots[i]).equals("���ش���")){
		    	ItemData itemi = new ItemData(true,roots[i].getPath(),roots[i].getPath());// ����·��
		    	TreeItem<ItemData> treeItemi = new TreeItem<ItemData>(itemi);
		    
		    	root.getChildren().add(treeItemi);
		        continue;
		    }
		    
		}
	
		
		// ���ø��ڵ�
		treeView.setRoot( root );
		treeView.setShowRoot(false);
		// ���� CellFactory
		treeView.setCellFactory(new Callback<TreeView<ItemData>,TreeCell<ItemData>>(){

			@Override
			public TreeCell<ItemData> call(TreeView<ItemData> param)
			{
				return new MyTreeCell();
			}
		
		});
		
		//treeview �ĵ���¼�,��ʾ���µ��ļ��к��ļ�
		treeView.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
	    {
	        public void handle(MouseEvent event)
	        {
	        	 //��ȡ�����treeview�ڵ㣬����ʵ����Ϊһ��ItemData
	        	 ItemData itemData= (ItemData) (treeView.getSelectionModel().getSelectedItem()).getValue();
	        
	        	 //��ȡ�����treeview�ڵ�
	        	 TreeItem<ItemData> item = ((TreeItem<ItemData>)treeView.getSelectionModel().getSelectedItem());
	        	 
	        	 if(item.isExpanded()) {//����ڵ��Ѿ�չ��
	        		 item.setExpanded(false);
	        	 }else {//��������ӽڵ���չ��
	        		 getAllFilePath(item ,itemData);
	        	 }
	        	 
	        }
	    });
		
	}
	//����ĳһ�ļ����¼��ļ����ļ���
	private void getAllFilePath(TreeItem<ItemData> treeItem,ItemData itemData){
		
		//��ȥ�����ӽڵ㣬���������
		treeItem.getChildren().clear();
		
		if(itemData.getIsDir()) {//������ļ���
			
			//��ȡ������Ŀ¼���ļ�
			File[] files = new File(itemData.getPath()).listFiles();
			//����Ŀ¼�ļ����Ϊ���ӽڵ�
			for (int i = 0; i < files.length; i++) {
				
				ItemData itemi =new ItemData(false,files[i].getPath(),files[i].getName());
				
				if(files[i].isDirectory()) {
					itemi.setIsDir(true);
					//ImageView iv = new ImageView(new Image(getClass().getResourceAsStream("/folder.png")));
					//itemi.setGraphic(iv);
				
				}
				TreeItem<ItemData> treeItemi =new TreeItem<ItemData>(itemi);
				treeItem.getChildren().add(treeItemi); 
			
				continue; 
			}
			treeItem.setExpanded(true);
		}
		
	 }
	
	
	
	public static void main(String[] args)
	{
		launch(args);
	}
}

