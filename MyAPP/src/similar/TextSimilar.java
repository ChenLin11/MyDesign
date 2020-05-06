package similar;

public class TextSimilar {

	public TextSimilar(){

	}
	public float levenshtein(String s1,String s2)
    {
       
        //计算两个字符串的长度。  
        int len1 = s1.length();
        int len2 = s2.length();
        //建立数组，比字符长度大一个空间  
        int[][] dif = new int[len1 + 1][];
        //初始化二维数组
        for (int i = 0; i < dif.length; i++)
        {
            dif[i] = new int[len2 + 1];
        }

        //赋初值，步骤B。  
        for (int a = 0; a <= len1; a++)
        {
            dif[a][0] = a;
        }
        for (int a = 0; a <= len2; a++)
        {
            dif[0][a] = a;
        }
        //计算两个字符是否一样，计算左上的值  
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
                //取三个值中最小的  
                int num1 = dif[i - 1][j - 1] + temp;
                int num2 = dif[i][j - 1] + 1;
                int num3 = dif[i - 1][j] + 1;
                int min = num1 < num2 ? num1 : num2;
                dif[i][j] = min < num3 ? min : num3;
            }
        }
       
        //取数组右下角的值，同样不同位置代表不同字符串的比较  
     
        //计算相似度  
        float similarity = 1 - (float)dif[len1][len2] / Math.max(len1, len2);
       
        return similarity;

    }
}
