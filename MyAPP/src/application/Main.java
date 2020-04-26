package application;

import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.filechooser.FileSystemView;

import apted.node.StringNodeData;
import apted.costmodel.MyDMTreeNodeCostModel;
import apted.costmodel.StringUnitCostModel;
import apted.distance.APTED;
import apted.node.AptedNode;
import apted.node.MyStringNodeData;
import apted.parser.DMTreeNodeParser;
import file.ItemData;
import file.MyTreeCell;
import file.ResultData;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import myTree.CreateMyJTree;

public class Main extends Application
{
	TreeView<ItemData> treeView = new TreeView<ItemData>();
	ListView<ItemData> listView = new ListView<ItemData>();
	ArrayList< ItemData> arrayList = new ArrayList<ItemData>();
    SwingNode sNode = new SwingNode();
	BorderPane pane;
	Stage secondStage;
	@Override
	public void start(Stage primaryStage) throws Exception
	{
		pane = new BorderPane();
		
		
		initTreeView();
		pane.setLeft(treeView);
	
		//horizontal layout
		VBox vBox = new VBox();
		
		HBox hBox1 = new HBox();
		Button selectAllButton = new Button("ȫѡ");	
		selectAllButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				//ͨ����������ListView��ѡ�񵥸�������Ŀ��������û�ѡ������Щ��Ŀ
				//listView.getSelectionModel().selectAll();
				listView.getSelectionModel().selectAll();
				arrayList.addAll(listView.getItems());
				listView.requestFocus();
			}
		});
		Button calculateAPTED = new Button("����APTED");
		calculateAPTED.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				//cannot calculate APTED when nodes's numb less than two
				if (arrayList.size()<2) {
					return ;
				}
				ArrayList<AptedNode<MyStringNodeData>> aptedArrayList = new ArrayList<>();
				DMTreeNodeParser parser = new DMTreeNodeParser();
				
				for (int i = 0; i < arrayList.size(); i++) {
					try {
						File file = new File(arrayList.get(i).getPath());
						aptedArrayList.add(parser.fromNode(new CreateMyJTree(file).getRootDMTreeNode()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
				calculateAPTEDList(aptedArrayList);
				
			}
		});
		hBox1.setPadding(new Insets(10));
		hBox1.setSpacing(100);
		hBox1.getChildren().addAll(selectAllButton,calculateAPTED);
		
		HBox hBox2 = new HBox();
		hBox2.getChildren().add(listView);
		hBox2.setPrefHeight(550);
		
		vBox.getChildren().addAll(hBox1,hBox2);
		vBox.setVisible(false);
		vBox.setPrefWidth(400);
		vBox.setPrefHeight(600);
		pane.setCenter(vBox);
		
		// �����µ�stage
//        secondStage = new Stage();
//        StackPane secondPane = new StackPane();
//        Scene secondScene = new Scene(secondPane, 300, 200);
//        TreeView<String> tv = new TreeView<>();
//        TreeItem<String> item = new TreeItem<>("���ڵ�");
//        
//        tv.setRoot(item);
//        item.setExpanded(true);
//        TreeItem<String> i1 = new TreeItem<>("��Ӱ");
//        TreeItem<String> i2 = new TreeItem<>("����");
//        TreeItem<String> i3 = new TreeItem<>("��Ϸ");
//        item.getChildren().addAll(i1,i2,i3);
//        TreeItem<String> i4 = new TreeItem<>("���ܷ���");
//        TreeItem<String> i5 = new TreeItem<>("���ν��5");
//        i1.setExpanded(true);
//        i1.getChildren().addAll(i4,i5);
//	    tv.getStyleClass().add("resultTreeView");
//        tv.setStyle(".LeafNodesStyle {\r\n" + 
//	    		"    display: inline-block;\r\n" + 
//	    		"    padding: 5px 10px;\r\n" + 
//	    		"    float:left;\r\n" + 
//	    		"}");
//        secondPane.getChildren().add(tv);
//        secondStage.setScene(secondScene);
//        secondStage.show();
        
		Scene scene = new Scene(pane, 1200, 600);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();

		
	}
	private void calculateAPTEDList(ArrayList<AptedNode<MyStringNodeData>> aptedList) {
		//APTED<StringUnitCostModel, StringNodeData> apted = new APTED<>(new StringUnitCostModel());
		APTED<MyDMTreeNodeCostModel, MyStringNodeData> apted = new APTED<>(new MyDMTreeNodeCostModel());
		float[][] resultArray = new float[aptedList.size()][aptedList.size()];
		secondStage = new Stage();
        StackPane secondPane = new StackPane();
        Scene secondScene = new Scene(secondPane, 500, 500);
        //tableView չʾn���ļ�֮������ƶȽ��
        TableView<ResultData> tableView = new TableView<>();
        secondPane.getChildren().add(tableView);
        //��һ��
        TableColumn<ResultData, String> tableColumn = new TableColumn<>("Student");
        tableView.getColumns().add(tableColumn);
        //��ѡȡ���ļ��б����ļ�����ӵ���һ��
        for (ItemData data : arrayList) {
        	String nameString = data.getName();
			if (data.getName().length()>8) {
				nameString = data.getName().substring(0, 8);
			}
			//ֻ�洢�ļ�����������
			ResultData resultData = new ResultData(nameString);
			tableColumn.getColumns().add(new TableColumn<ResultData, String>("sdg"));
		}
        //tableColumn.getColumns()apted;
		for (int i = 0; i < aptedList.size(); i++) {
			
			
			for (int j = i+1; j < aptedList.size(); j++) {
				int maxNodes = aptedList.get(i).getNodeCount()>aptedList.get(j).getNodeCount()?aptedList.get(i).getNodeCount():aptedList.get(j).getNodeCount();
				float result = 1 - apted.computeEditDistance(aptedList.get(i), aptedList.get(j))/maxNodes;
				resultArray[i][j] = result;
				System.out.println(arrayList.get(i).getName().subSequence(0, 8)+"-"+arrayList.get(j).getName().subSequence(0, 8)+":"+result+"--"+apted.computeEditDistance(aptedList.get(i), aptedList.get(j)));
			}
		}
		
        secondStage.setScene(secondScene);
        secondStage.show();
	}
	
	public static void main(String[] args) throws Exception
	{
		
        launch(args);
	}
	
	//��ʼ��TreeView
	public void initTreeView() throws Exception 
	{
		
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
		treeView.setCellFactory((TreeView<ItemData> p) ->  new MyTreeCell());
		
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
	        	 if (item.getValue().getPath().endsWith(".c")) {
					
					initListView(item);
	        	 }
	        }
	    });
	}
	private void initListView(TreeItem<ItemData> selectTreeItem) {
		ArrayList<ItemData> list = new ArrayList<>();
		for (int i = 0; i < selectTreeItem.getParent().getChildren().size(); i++) {
			if (selectTreeItem.getParent().getChildren().get(i).getValue().getName().toString().endsWith(".c")) {
				ItemData data = selectTreeItem.getParent().getChildren().get(i).getValue();
				list.add(data);
			}
 		}
		ObservableList<ItemData> itemList = FXCollections.observableArrayList(list);
		// ��������Դ
		listView.setItems(itemList);
		listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		//���õ�Ԫ����ʾ
		listView.setCellFactory(new Callback<ListView<ItemData>, ListCell<ItemData>>() {

			public ListCell<ItemData> call(ListView<ItemData> param) {
				ListCell<ItemData> listCell = new ListCell<ItemData>() {
					protected void updateItem(ItemData item,boolean empty) {
						super.updateItem(item,empty);
						if(empty == true) {
							this.setText("");
						}
						else {
							HBox hBox = new HBox(10);
							//CheckBox checkBox = new CheckBox();
							Button getJTreeButton = new Button("JTree");
							Button getCodeButton = new Button("SourceCode");
							hBox.setAlignment(Pos.CENTER);
							hBox.getChildren().addAll(getJTreeButton,getCodeButton);
							
							if (item.getName().contains("_")) {
								this.setText(item.getName().substring(0, 8));
							}
							else{
								this.setText(item.getName());
							}
							
							this.setGraphic(hBox);
							pane.getChildren().get(1).setVisible(true);
							getJTreeButton.setOnAction(new EventHandler<ActionEvent>() {
								public void handle(ActionEvent event) {
									//javafx ʹ�� swing�������ֹ�߳�����
									Platform.runLater(new Runnable() {
										
										@Override
										public void run() {
											try {
									        	CreateMyJTree myJTree = new CreateMyJTree(new File(item.getPath()));
									        	JPanel jPanel = new JPanel();
									        
									        	jPanel.add(new JTree(myJTree.getRootDMTreeNode()));
									        	FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT,75,100);	
									        	jPanel.setLayout(flowLayout);
												sNode.setContent(jPanel);
												pane.setRight(sNode);
											} catch (Exception e) {
												e.printStackTrace();
											}
									        
										}
									});

								}
							});
							
							getCodeButton.setOnAction(new EventHandler<ActionEvent>() {
								
								@Override
								public void handle(ActionEvent event) {
									Text text = new Text("");
									File file = new File(item.getPath());
									StringBuilder content = new StringBuilder();
							        String line;

							        try (BufferedReader br = new BufferedReader(
							                new InputStreamReader(new FileInputStream(file)))) {
							            while ((line = br.readLine()) != null) {
							                content.append(line).append('\n');
							            }
							            text.setText(content.toString()); 
							      
							        } catch (IOException e) {
										e.printStackTrace();
									}     
							        pane.setRight(text);
								}
							});
							
							
						}
					}
				};
				return listCell;
			}
			
		});
		listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ItemData>() {	
			@Override
			public void changed(ObservableValue<? extends ItemData> observable, ItemData oldValue, ItemData newValue) {
				arrayList.clear();
				arrayList.addAll(listView.getSelectionModel().getSelectedItems());
				try {
					System.out.println(arrayList.size());
					for (int i = 0; i < arrayList.size(); i++) {
						System.out.println("select:"+arrayList.get(i).getName());
					}
					System.out.println("------------------------------------");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
		listView.setPrefSize(400, 500);
	}
	//����ĳһ�ļ����¼��ļ����ļ���
	private  void getAllFilePath(TreeItem<ItemData> treeItem,ItemData itemData){
		
		//��ȥ�����ӽڵ㣬���������
		treeItem.getChildren().clear();
		
		if(itemData.getIsDir()) {//������ļ���
			
			//��ȡ������Ŀ¼���ļ�
			File[] files = new File(itemData.getPath()).listFiles();
			//����Ŀ¼�ļ����Ϊ���ӽڵ�
			for (int i = 0; i < files.length; i++) {
				if (files[i].getName().contains(".BIN")||files[i].getName().contains(".sys")) {
					continue;
				}
				else {
					
					ItemData itemi =new ItemData(false,files[i].getPath(),files[i].getName());
					if(files[i].isDirectory()) {
						itemi.setIsDir(true);
						
					}
					
					TreeItem<ItemData> treeItemi =new TreeItem<ItemData>(itemi);
					treeItem.getChildren().add(treeItemi); 
				
				}
				
			}
			treeItem.setExpanded(true);
		}
		
	 }	
}

