import java.io.BufferedWriter;
import java.io.FileWriter;
//import java IO 建立TXT檔所需
import java.net.URL;
//import URL 連接網站所需
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
//import JSoup 從網站上爬資料時所需

 public class SD_401630727 {
	public static void main(String[] args) throws Exception {
			Crawler();
	}
	public static void Crawler() throws Exception {
		URL url = new URL("http://www.nba.com/gameline/20130101/"); 
		//建立URL 連接到我們想要爬資料的網站
		Document xmlDoc =  Jsoup.parse(url, 3000);
		//使用JSoup解析網頁  (要解析的文件,timeout)
		FileWriter fw = new FileWriter("401631808.txt");
		//使用FileWriter建立TXT檔
		BufferedWriter bw=new BufferedWriter(fw);
		//建立BufferedWriter 等等會將資料先存入bw 再放進TXT
		//Elements date = xmlDoc.select("h2.nbaLgDate");
		//解析我們所需要的區段 tag為h2 class名為nbaLgDate 抓取我們的日期
		Elements scores = xmlDoc.select("td");
		//tag為div class名為nbaMnQuScores 以tr為區段抓取我們要的比分
		//print出日期
		
		//刷新bw
		int i=0; 
		//設一個變數i 等等用於迴圈抓資料
		try{
			while(i!=-1){
				//一開始i=0 所以我們會先抓到網站上第0個的資料    也就是我們所要抓的第1筆資料
				System.out.println(scores.get(i).text());
				//print出比分
				bw.write(scores.get(i).text()+"\n");
				//將比分寫入bw
				bw.newLine();
				//換行 一樣排版用
				bw.flush();
				// 刷新bw
				
				i=i+1;
				//在抓完一筆資料後   將i=i+1 再繼續抓取下ㄧ筆資料
			}
		}catch(Exception e){
			i=-1;
		//用try catch做例外處理    如果爬不到東西了  則將i=-1關閉迴圈
		}
		bw.close();
		fw.close();
		//全部完成後close
	}

}
