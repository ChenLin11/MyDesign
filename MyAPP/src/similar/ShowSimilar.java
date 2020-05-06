package similar;

import java.io.File;
import java.util.ArrayList;

import file.SimilarString;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/*
 * 展示两个Box，box含有listView展示相似的代码
 */
public class ShowSimilar {
	private String fileName1 = "";
	private String fileName2 = "";
	private String filePath1 = "";
	private String filePath2 = "";
	private VBox vBox1;
	private VBox vBox2;
	private String[] lineStrings1;
	private String[] lineStrings2;
	private ArrayList<SimilarString> codeList1;
	private ArrayList<SimilarString> codeList2;

	public ShowSimilar(String fileName1, String fileName2, String filePath1, String filePath2) {
		super();
		this.fileName1 = fileName1;
		this.fileName2 = fileName2;
		this.filePath1 = filePath1;
		this.filePath2 = filePath2;
		caculate();
	}
	//将相似行找出
	private void caculate() {
		codeList1 = new ArrayList<>();
		codeList2 = new ArrayList<>();
		CFile file1,file2;
		try {
			file1 = new CFile(new File(filePath1));
			lineStrings1 = file1.getOrignalFile();
			file2 = new CFile(new File(filePath2));
			lineStrings2 = file2.getOrignalFile();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		TextSimilar textSimiliar = new TextSimilar();
		int k = 0;//开始匹配行号
		int file2StartLine = 0;//文件2中开始添加到codeList2的行标
		for (int i = 0; i < lineStrings1.length; i++) {
			
			float max = 0.0f;//文件1第i行与文件2各行相似度最大的结果
			int k1 = -1;//k1为最相似的行号
			for (int j = k; j < lineStrings2.length; j++) {
				float similarity = textSimiliar.levenshtein(lineStrings1[i], lineStrings2[j]);
				if (similarity > max && similarity >= 0.8f) {
					k1 = j;
					max = similarity;
				}
			}
			//说明并没有很相似的地方,标记为不相似加入到codeList1中
			if (k1 < 0 ) {
				codeList1.add(new SimilarString(lineStrings1[i],false));
			}
			else {//否则，文件1第i行跟文件2第k1行相似
				codeList1.add(new SimilarString(lineStrings1[i],true));
				//k1之前不相似
				for (int j = file2StartLine; j < k1; j++) { 
					codeList2.add(new SimilarString(lineStrings2[j],false));
				}
				//第k1行相似
				codeList2.add(new SimilarString(lineStrings2[k1],true));
				//下次文件2从k1+1行搜索,且从k1+1行添加，免得重复添加
				k = k1+1;
				file2StartLine = k;
				
			}
			//如果i是文件1最后一行，且 k1 = k,说明文件2之后不再相似
			if (i == lineStrings1.length - 1) {
				for (int j = file2StartLine; j < lineStrings2.length; j++) {
					codeList2.add(new SimilarString(lineStrings2[j],false));
				}
			}
		}
		
	}
	public VBox getvBox1() {
		vBox1 = new VBox();
		Label label1 = new Label(fileName1);
		label1.setStyle("-fx-background-color:#87CEFA");//LightSkyBlue淡蓝色#87CEFA
		
		ListView<SimilarString> codeView1 = buildListView(codeList1);
		vBox1.getChildren().addAll(label1,codeView1);
		
		return vBox1;
	}
	public VBox getvBox2() {
		vBox2 = new VBox();
		Label label2 = new Label(fileName2);
		label2.setStyle("-fx-background-color:#87CEFA");//LightSkyBlue淡蓝色#87CEFA
		
		ListView<SimilarString> codeView2 = buildListView(codeList2);
		vBox2.getChildren().addAll(label2,codeView2);
		
		return vBox2;
	}
	private ListView<SimilarString> buildListView(ArrayList<SimilarString> codeList) {
		ListView<SimilarString> codeView = new ListView<>();
		ObservableList<SimilarString> codeItemtemList = FXCollections.observableArrayList(codeList);
		// 设置数据源
		codeView.setItems(codeItemtemList);
		codeView.setCellFactory(new Callback<ListView<SimilarString>, ListCell<SimilarString>>() {
				
			@Override
			public ListCell<SimilarString> call(ListView<SimilarString> param) {
				// TODO Auto-generated method stub
				ListCell<SimilarString> cell = new ListCell<SimilarString>() {

					@Override
					protected void updateItem(SimilarString item, boolean empty) {
						// TODO Auto-generated method stub
						super.updateItem(item, empty);
						if (!empty && item != null) {
							
							Label label = new Label(item.getContent());
							if (item.isFamiliar()) {//相似处标记
								label.setStyle("-fx-background-color:#87CEFA");//LightSkyBlue淡蓝色#87CEFA
							}
							this.setGraphic(label);
						}
					}
					
				};
				return cell;
			}
		});
		return codeView;
	}
	
}
