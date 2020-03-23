package Similiar;

public class TextSimiliar {
	private String s1,s2;
	public TextSimiliar(String text1,String text2){
		s1 = text1;
		s2 = text2;
	}
	public float levenshtein()
    {
       
        //���������ַ����ĳ��ȡ�  
        int len1 = s1.length();
        int len2 = s2.length();
        //�������飬���ַ����ȴ�һ���ռ�  
        int[][] dif = new int[len1 + 1][];
        //��ʼ����ά����
        for (int i = 0; i < dif.length; i++)
        {
            dif[i] = new int[len2 + 1];
        }

        //����ֵ������B��  
        for (int a = 0; a <= len1; a++)
        {
            dif[a][0] = a;
        }
        for (int a = 0; a <= len2; a++)
        {
            dif[0][a] = a;
        }
        //���������ַ��Ƿ�һ�����������ϵ�ֵ  
        int temp;
        for (int i = 1; i <= len1; i++)
        {
            for (int j = 1; j <= len2; j++)
            {
                if (s1.charAt(i-1)==s2.charAt(j-1))
                {
                    temp = 0;
                }
                else
                {
                    temp = 1;
                }
                //ȡ����ֵ����С��  
                int num1 = dif[i - 1][j - 1] + temp;
                int num2 = dif[i][j - 1] + 1;
                int num3 = dif[i - 1][j] + 1;
                int min = num1 < num2 ? num1 : num2;
                dif[i][j] = min < num3 ? min : num3;
            }
        }
        //System.out.println("�ַ���\""+str1+"\"��\""+str2+"\"�ıȽ�");  
        //ȡ�������½ǵ�ֵ��ͬ����ͬλ�ô���ͬ�ַ����ıȽ�  
        //System.out.println("���첽�裺"+dif[len1][len2]);  
        //�������ƶ�  
        float similarity = 1 - (float)dif[len1][len2] / Math.max(s1.length(), s2.length());
        //System.out.println("���ƶȣ�"+similarity);
        return similarity;

    }
}
