import java.io.BufferedWriter;
import java.io.FileWriter;
//import java IO �إ�TXT�ɩһ�
import java.net.URL;
//import URL �s�������һ�
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
//import JSoup �q�����W����Ʈɩһ�

 public class SD_401630727 {
	public static void main(String[] args) throws Exception {
			Crawler();
	}
	public static void Crawler() throws Exception {
		URL url = new URL("http://www.nba.com/gameline/20130101/"); 
		//�إ�URL �s����ڭ̷Q�n����ƪ�����
		Document xmlDoc =  Jsoup.parse(url, 3000);
		//�ϥ�JSoup�ѪR����  (�n�ѪR�����,timeout)
		FileWriter fw = new FileWriter("401631808.txt");
		//�ϥ�FileWriter�إ�TXT��
		BufferedWriter bw=new BufferedWriter(fw);
		//�إ�BufferedWriter �����|�N��ƥ��s�Jbw �A��iTXT
		//Elements date = xmlDoc.select("h2.nbaLgDate");
		//�ѪR�ڭ̩һݭn���Ϭq tag��h2 class�W��nbaLgDate ����ڭ̪����
		Elements scores = xmlDoc.select("td");
		//tag��div class�W��nbaMnQuScores �Htr���Ϭq����ڭ̭n�����
		//print�X���
		
		//��sbw
		int i=0; 
		//�]�@���ܼ�i �����Ω�j�����
		try{
			while(i!=-1){
				//�@�}�li=0 �ҥH�ڭ̷|���������W��0�Ӫ����    �]�N�O�ڭ̩ҭn�쪺��1�����
				System.out.println(scores.get(i).text());
				//print�X���
				bw.write(scores.get(i).text()+"\n");
				//�N����g�Jbw
				bw.newLine();
				//���� �@�˱ƪ���
				bw.flush();
				// ��sbw
				
				i=i+1;
				//�b�짹�@����ƫ�   �Ni=i+1 �A�~�����U�������
			}
		}catch(Exception e){
			i=-1;
		//��try catch���ҥ~�B�z    �p�G������F��F  �h�Ni=-1�����j��
		}
		bw.close();
		fw.close();
		//����������close
	}

}
