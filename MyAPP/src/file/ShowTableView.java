package file;

import java.util.ArrayList;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import similar.ShowSimilar;

public class ShowTableView {
	TableView<ResultData> tableView;
	ArrayList<ItemData> arrayList;
	Stage thirdStage;
	public ShowTableView(Stage thirdStage,ArrayList< ItemData> arrayList) {
		// TODO Auto-generated constructor stub
		tableView = new TableView<>();
		this.arrayList = arrayList; 
		this.thirdStage = thirdStage;
	}
	public TableView<ResultData> getTableView() {
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
			return tableView;
	}
}
