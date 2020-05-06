package application;

import java.awt.FlowLayout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.filechooser.FileSystemView;

import file.ItemData;
import file.MyTreeCell;
import file.ResultData;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import myTree.CreateMyJTree;

import similar.ShowSimilar;

public class Main extends Application
{
	TreeView<ItemData> treeView = new TreeView<ItemData>();
	ListView<ItemData> listView = new ListView<ItemData>();
	ArrayList< ItemData> arrayList = new ArrayList<ItemData>();
    SwingNode sNode = new SwingNode();
	BorderPane pane;
	Stage secondStage = new Stage();
	Stage thirdStage = new Stage();
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
				arrayList.removeAll(listView.getItems());
				arrayList.addAll(listView.getItems());
				
				listView.requestFocus();
			}
		});
		Button calculateAPTED = new Button("����APTED");
		calculateAPTED.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				//cannot calculate APTED when nodes's number less than two
				if (arrayList.size()<2) {
					return ;
				}
				
				StackPane secondPane = new StackPane();
		        Scene secondScene = new Scene(secondPane, 1000, 500);
				//tableView չʾn���ļ�֮������ƶȽ��
		        TableView<ResultData> tableView = new TableView<>();
		        secondPane.setAlignment(Pos.CENTER);
		        secondPane.setPadding(new Insets(5));
		        secondPane.getChildren().add(tableView);
		        try {
		        	ArrayList<ResultData> resultlList = new ArrayList<>();
					for (int i = 0; i < arrayList.size(); i++) {
						ResultData resultData = new ResultData(arrayList, i);
						resultlList.add(resultData);
					}
					tableView.getItems().addAll(resultlList);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        //��һ��
		        TableColumn<ResultData, String> tableColumn = new TableColumn<>("Student");
		        tableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ResultData,String>, ObservableValue<String>>() {
					
					@Override
					public ObservableValue<String> call(CellDataFeatures<ResultData, String> param) {
						// TODO Auto-generated method stub
						SimpleStringProperty ssp = new SimpleStringProperty(param.getValue().getMyFileName());
						return ssp;
					}
				});
		        //�Զ���tableColumn�ĵ�Ԫ���������ڲ�����ʾ
		        tableColumn.setCellFactory(new Callback<TableColumn<ResultData,String>, TableCell<ResultData,String>>() {

					@Override
					public TableCell<ResultData, String> call(TableColumn<ResultData, String> param) {
						// TODO Auto-generated method stub
						TableCell<ResultData, String> cell = new TableCell<ResultData, String>() {
							@Override
							protected void updateItem(String item,boolean empty) {
								super.updateItem(item, empty);
								if (!empty&&item != null) {
									HBox hBox = new HBox();
									//��label����
									hBox.setAlignment(Pos.CENTER);
									Label label = new Label(item);
									
									hBox.setStyle("-fx-background-color:#DEDEDE");//��Ӱ#DEDEDE								
									hBox.getChildren().add(label);
									this.setGraphic(hBox);
								}
							}

						};
						return cell;
					}
				});
		        tableView.getColumns().add(tableColumn);
		        //����Ϊ�ɱ༭״̬
	        	tableView.setEditable(true);
		        for (int i = 0; i < arrayList.size(); i++) {
		        	//��i��,����չʾ���
		        	String nameString = arrayList.get(i).getName();
		        	TableColumn<ResultData, Number> resultColumn;
		        	//��i�е��б�������Ϊ��i��ѧ�����ļ���(ǰ�˸��ַ�)
		        	if (nameString.length()>8) {
						resultColumn = new TableColumn<>(nameString.substring(0, 8));
					}
		        	else {
		        		resultColumn = new TableColumn<>(nameString);
					}
		        	
		        	//����tableColumn������
			        resultColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ResultData,Number>, ObservableValue<Number>>() {
			        	//���ص�float��SimpleFloatProperty��Ȼ���̳е���ObservableValue<Number>
						@Override
						public ObservableValue<Number> call(CellDataFeatures<ResultData, Number> param) {
							//���е�����ֵ�ǣ���ǰ�û���(ResultData)��ʾ���ļ�����б�ʾ���ļ�(param.getTableColumn().getText())�ļ�����							
							SimpleFloatProperty sfp = new SimpleFloatProperty(param.getValue().getResult(param.getTableColumn().getText()));
							return sfp;
						}
					});
			        //�Զ���tableColumn�ĵ�Ԫ���������ڲ�����ʾ
			        resultColumn.setCellFactory(new Callback<TableColumn<ResultData,Number>, TableCell<ResultData,Number>>() {

						@Override
						public TableCell<ResultData, Number> call(TableColumn<ResultData, Number> param) {
							// TODO Auto-generated method stub
							TableCell<ResultData, Number> cell = new TableCell<ResultData, Number>() {
								@Override
								protected void updateItem(Number item,boolean empty) {
									super.updateItem(item, empty);
									if (!empty&&item != null) {
										HBox hBox = new HBox();
										//��label����
										hBox.setAlignment(Pos.CENTER);
										Label label = new Label(item.toString());
										//�������ƶȽ�����ò�ͬ����ɫ
										//��������������������ļ���ͬһ��
										if (tableView.getItems().get(this.getIndex()).getMyFileName().contains(this.getTableColumn().getText())) {
											hBox.setStyle("-fx-background-color:#008000");//Green����#008000
										}
										//��ȫ��Ϯ����ɫ��ע
										else if (item.floatValue() == 1) {
											hBox.setStyle("-fx-background-color:#FF0000");//Red����#FF0000
										}
										//���ӳ�Ϯ����ɫ��ע
										else if (item.floatValue() >= 0.8f) {
											hBox.setStyle("-fx-background-color:#FFFF00");//Yellow����#FFFF00
										}
										
										hBox.getChildren().add(label);
										this.setGraphic(hBox);
										//��ǰ�û����ļ���ǰ8λ��ѧ�ţ�
										String tip = tableView.getItems().get(this.getIndex()).getMyFileName();
										if ( tip.length()>8) {
											tip = tip.substring(0,8);
										}
										//��ʾ��
										this.setTooltip(new Tooltip(tip+"--"+this.getTableColumn().getText()));
									}
									
								}

								@Override
								public void startEdit() {
									// TODO Auto-generated method stub
									super.startEdit();
									HBox hBox = new HBox();
									hBox.setPadding(new Insets(5));
									hBox.setSpacing(10);
									
									ShowSimilar showSimilar = new ShowSimilar(tableView.getItems().get(this.getIndex()).getMyFileAllName(),
											tableView.getItems().get(this.getIndex()).getIFileAllName(this.getTableColumn().getText()),
											tableView.getItems().get(this.getIndex()).getMyFilePath(),
											tableView.getItems().get(this.getIndex()).getIFilePath(this.getTableColumn().getText()));
	
									hBox.getChildren().addAll(showSimilar.getvBox1(),showSimilar.getvBox2());
									
							        Scene thirdScene = new Scene(hBox, 850, 400);
							        thirdStage.setScene(thirdScene);
							        thirdStage.show();
								}
								
							};
							return cell;
						}
					});
			        tableView.getColumns().add(resultColumn);
				}
		        //���С�Ϊ��ԭ���һ�е�����������ʾ
		        TableColumn<ResultData, String> nullColumn = new TableColumn<>("\t");
		        nullColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ResultData,String>, ObservableValue<String>>() {
					
					@Override
					public ObservableValue<String> call(CellDataFeatures<ResultData, String> param) {
						// TODO Auto-generated method stub
						SimpleStringProperty ssp = new SimpleStringProperty("\t");
						return ssp;
					}
				});
		        tableView.getColumns().add(nullColumn);
		        
		        secondStage.setScene(secondScene);
		        secondStage.show();
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
		
	
		Scene scene = new Scene(pane, 1200, 600);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
		//��ǰһ��stage���رգ������Ҳ��֮�ر�
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent event) {
				// TODO Auto-generated method stub
				secondStage.close();
				thirdStage.close();
			}
			
		});
		secondStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent event) {
				// TODO Auto-generated method stub
				thirdStage.close();
			}
			
		});
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
		
		//treeView �ĵ���¼�,��ʾ���µ��ļ��к��ļ�
		treeView.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
	    {
	        public void handle(MouseEvent event)
	        {
	        	
	        	 //��ȡ�����treeView�ڵ㣬����ʵ����Ϊһ��ItemData
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

