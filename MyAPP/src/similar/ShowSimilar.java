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
 * չʾ����Box��box����listViewչʾ���ƵĴ���
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
	//���������ҳ�
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
		int k = 0;//��ʼƥ���к�
		int file2StartLine = 0;//�ļ�2�п�ʼ��ӵ�codeList2���б�
		for (int i = 0; i < lineStrings1.length; i++) {
			
			float max = 0.0f;//�ļ�1��i�����ļ�2�������ƶ����Ľ��
			int k1 = -1;//k1Ϊ�����Ƶ��к�
			for (int j = k; j < lineStrings2.length; j++) {
				float similarity = textSimiliar.levenshtein(lineStrings1[i], lineStrings2[j]);
				if (similarity > max && similarity >= 0.8f) {
					k1 = j;
					max = similarity;
				}
			}
			//˵����û�к����Ƶĵط�,���Ϊ�����Ƽ��뵽codeList1��
			if (k1 < 0 ) {
				codeList1.add(new SimilarString(lineStrings1[i],false));
			}
			else {//�����ļ�1��i�и��ļ�2��k1������
				codeList1.add(new SimilarString(lineStrings1[i],true));
				//k1֮ǰ������
				for (int j = file2StartLine; j < k1; j++) { 
					codeList2.add(new SimilarString(lineStrings2[j],false));
				}
				//��k1������
				codeList2.add(new SimilarString(lineStrings2[k1],true));
				//�´��ļ�2��k1+1������,�Ҵ�k1+1����ӣ�����ظ����
				k = k1+1;
				file2StartLine = k;
				
			}
			//���i���ļ�1���һ�У��� k1 = k,˵���ļ�2֮��������
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
		label1.setStyle("-fx-background-color:#87CEFA");//LightSkyBlue����ɫ#87CEFA
		
		ListView<SimilarString> codeView1 = buildListView(codeList1);
		vBox1.getChildren().addAll(label1,codeView1);
		
		return vBox1;
	}
	public VBox getvBox2() {
		vBox2 = new VBox();
		Label label2 = new Label(fileName2);
		label2.setStyle("-fx-background-color:#87CEFA");//LightSkyBlue����ɫ#87CEFA
		
		ListView<SimilarString> codeView2 = buildListView(codeList2);
		vBox2.getChildren().addAll(label2,codeView2);
		
		return vBox2;
	}
	private ListView<SimilarString> buildListView(ArrayList<SimilarString> codeList) {
		ListView<SimilarString> codeView = new ListView<>();
		ObservableList<SimilarString> codeItemtemList = FXCollections.observableArrayList(codeList);
		// ��������Դ
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
							if (item.isFamiliar()) {//���ƴ����
								label.setStyle("-fx-background-color:#87CEFA");//LightSkyBlue����ɫ#87CEFA
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
