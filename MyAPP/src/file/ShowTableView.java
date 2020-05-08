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
        //第一列
        TableColumn<ResultData, String> tableColumn = new TableColumn<>("Student");
        tableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ResultData,String>, ObservableValue<String>>() {
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<ResultData, String> param) {
				// TODO Auto-generated method stub
				SimpleStringProperty ssp = new SimpleStringProperty(param.getValue().getMyFileName());
				return ssp;
			}
		});
        //自定义tableColumn的单元工厂，用于布局显示
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
							//将label居中
							hBox.setAlignment(Pos.CENTER);
							Label label = new Label(item);
							
							hBox.setStyle("-fx-background-color:#DEDEDE");//阴影#DEDEDE								
							hBox.getChildren().add(label);
							this.setGraphic(hBox);
						}
					}

				};
				return cell;
			}
		});
        tableView.getColumns().add(tableColumn);
        //设置为可编辑状态 	
     	tableView.setEditable(true);
	        for (int i = 0; i < arrayList.size(); i++) {
	        	//第i列,用于展示结果
	        	String nameString = arrayList.get(i).getName();
	        	TableColumn<ResultData, Number> resultColumn;
	        	//第i列的列表名设置为第i个学生的文件名(前八个字符)
	        	if (nameString.length()>8) {
					resultColumn = new TableColumn<>(nameString.substring(0, 8));
				}
	        	else {
	        		resultColumn = new TableColumn<>(nameString);
				}
	        	
	        	//加载tableColumn的数据
		        resultColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ResultData,Number>, ObservableValue<Number>>() {
		        	//返回的float用SimpleFloatProperty，然而继承的是ObservableValue<Number>
					@Override
					public ObservableValue<Number> call(CellDataFeatures<ResultData, Number> param) {
						//该列的属性值是：当前用户类(ResultData)表示的文件与该列表示的文件(param.getTableColumn().getText())的计算结果							
						SimpleFloatProperty sfp = new SimpleFloatProperty(param.getValue().getResult(param.getTableColumn().getText()));
						return sfp;
					}
				});
		        //自定义tableColumn的单元工厂，用于布局显示
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
									//将label居中
									hBox.setAlignment(Pos.CENTER);
									Label label = new Label(item.toString());
									//根据相似度结果设置不同的颜色
									//如果该列与该行所代表的文件是同一个
									if (tableView.getItems().get(this.getIndex()).getMyFileName().contains(this.getTableColumn().getText())) {
										hBox.setStyle("-fx-background-color:#008000");//Green纯绿#008000
									}
									//完全抄袭，红色标注
									else if (item.floatValue() == 1) {
										hBox.setStyle("-fx-background-color:#FF0000");//Red纯红#FF0000
									}
									//涉嫌抄袭，黄色标注
									else if (item.floatValue() >= 0.8f) {
										hBox.setStyle("-fx-background-color:#FFFF00");//Yellow纯黄#FFFF00
									}
									
									hBox.getChildren().add(label);
									this.setGraphic(hBox);
									//当前用户类文件名前8位（学号）
									String tip = tableView.getItems().get(this.getIndex()).getMyFileName();
									if ( tip.length()>8) {
										tip = tip.substring(0,8);
									}
									//提示框
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
	        //空列、为了原最后一列的数据完整显示
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
