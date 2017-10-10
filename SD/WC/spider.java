package WC;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public class spider {
	private static void initCrawlerWithSeeds(String []seeds){
		for(int i=0;i<seeds.length;i++){
			 SQ.addUnvisitedUrl(seeds[i]);
		}
	}
	
	public static void crawling(String []seeds,String n) throws ClassNotFoundException, IOException, SQLException{
		StringBuilder sb = new StringBuilder();
		StringBuilder sb1 = new StringBuilder();
		Class.forName("com.mysql.jdbc.Driver");
		String jdbcurl = "jdbc:mysql://localhost/point?useunicode=true&characterencoding=utf 8";
		String user = "root";
		String password = "joey820924";
		LinkFilter filter=new LinkFilter(){
			public boolean accept(String url){
				if(url.startsWith(seeds[0])){
					return true;
				}
				else{
					return false;
				}
			}
		};
		
		initCrawlerWithSeeds(seeds);  
		String visitUrl = (String) SQ.unVisitedUrlDeQueue(); 
		Set<String> links = HtmlParser.extracLinks(visitUrl, filter); 
		Object[] aa=links.toArray();
		while (!SQ.unVisitedUrlsEmpty() && SQ.getVisitedUrlNum() <= 1000) {  
			
			if(visitUrl==null){
				continue;
			}
			 SQ.addVisitedUrl(visitUrl);  
			
			URL url=new URL(visitUrl);
			Document doc = Jsoup.parse(url, 30000);
			Elements stat = doc.select("div.nbaMnQuScores td");
			Elements name = doc.select("div.nbaMnQuScores th");
			int i1 = 0;

			String data;
			int j = 0;

			try {
				PreparedStatement pstmt = null;
				while (j != -1) {
					if (j % 12 == 10 || j % 12 == 11)
						sb1.append(name.get(j).text() + "&");
					j++;
				}

			} catch (Exception e) {
				j = -1;
			}
			try {
				int k = 9;
				PreparedStatement pstmt = null;
				while (i1 != -1) {
					if (i1 % 1000 != k) {
						sb.append(stat.get(i1).text() + "&");
					} else if (i1 % 1000 == k) {
						k++;
						if (k == 18) {
							k = 36;
						}
						if (k == 45) {
							k = 63;
						}
						if (k == 72) {
							k = 90;
						}
						if (k == 99) {
							k = 117;
						}
						if (k == 126) {
							k = 144;
						}
						if (k == 153) {
							k = 171;
						}
						if (k == 180) {
							k = 198;
						}
						if (k == 207) {
							k = 225;
						}
						if (k == 234) {
							k = 252;
						}
						if (k == 261) {
							k = 279;
						}
						if (k == 288) {
							k = 306;
						}
						if (k == 315) {
							k = 333;
						}
					}

					i1++;

				}

			} catch (Exception e) {
				i1 = -1;
			}
			while (sb.length() != 0) {
				try (Connection conn = DriverManager.getConnection(jdbcurl, user,
						password)) {
					System.out.printf("資料庫已%s %n", conn.isClosed() ? "關閉" : "開-起");
					Statement stmt = conn.createStatement();
					try {
						
						String NBApoint[] = sb.toString().split("&");
						String NBAname[] = sb1.toString().split("&");
						int jj = 0;
						for (int i11 = 0; i11 < NBAname.length; i11++) {
							if (i11 % 2 == 0) {
								System.out.print(NBAname[i11] + " ");
								System.out.print("NULL" + " ");
								for (int j1 = 0; j1 < 9; j1++) {
									System.out.print(NBApoint[jj++] + " ");
								}
								System.out.println();

							} else {
								System.out.print("NULL" + " ");
								System.out.print(NBAname[i11] + " ");

								for (int j1 = 0; j1 < 9; j1++)
									System.out.print(NBApoint[jj++] + " ");
								System.out.println();
							}

						}

						store(n, conn, stmt);
						int k = 0;
						int l = 0;
						int x = 0;
						PreparedStatement pstmt = null;
						while (k < 10) {
							if (k % 2 == 0) {
								pstmt = conn.prepareStatement("INSERT INTO " + n
										+ " VALUES(?,?,?,?,?,?,?,?,?,?,?)");
								for (int i11 = 0; i11 < 11; i11++) {

									if (i11 == 0) {
										pstmt.setString(1, NBAname[k]);
										k++;
									} else if (i11 == 1) {
										pstmt.setString(2, null);
									} else {
										pstmt.setString(i11 + 1, NBApoint[l]);
										l++;

									}

								}
							} else {
								pstmt = conn.prepareStatement("INSERT INTO " + n
										+ " VALUES(?,?,?,?,?,?,?,?,?,?,?)");
								for (int i11 = 0; i11 < 11; i11++) {
									if (i11 == 0) {
										pstmt.setString(1, null);

									} else if (i11 == 1) {
										pstmt.setString(2, NBAname[k]);
										k++;
									} else {

										pstmt.setString(i11 + 1, NBApoint[l]);
										l++;
									}

								}
							}

							pstmt.executeUpdate();

						}
					} catch (Exception ex) {

					}
					conn.close();
					break;
				}
			
		}
			for(String link:links){
				 SQ.addUnvisitedUrl(link); 
			}	
		}
		
	}
	public static void main(String[]args) throws ClassNotFoundException{
		
		
		spider crawler = new spider();
		
		
		ScheduledExecutorService service=Executors.newSingleThreadScheduledExecutor();
		service.scheduleWithFixedDelay(new Runnable(){
			
			public void run(){
				String a[]=null;
				String b[]=null;
				String year = "2014";
				String month = "00";
				String date = "00";
				String url = "";
				int k;
				//0 6
				
				for (int j = 0; j < 12; j++) {
					int m = Integer.parseInt(month) + 1;
					if (m < 10)
						month = "0" + m;
					else
						month = "" + m;
					date = "00";
					for (int i = 0; i < 31; i++) {

						k = Integer.parseInt(date) + 1;
						if (k < 10)
							date = "0" + k;
						else
							date = "" + k;
						url += year + month + date + "@";
					}

				}
				a = url.toString().split("@");
				
				String month1 = "00";
				String date1 = "00";
				String url1 = "";
				int k1;

				for (int j = 0; j < 12; j++) {
					int m = Integer.parseInt(month1) + 1;
					if (m < 10)
						month1 = "0" + m;
					else
						month1 = "" + m;
					date1 = "00";
					for (int i = 0; i < 31; i++) {

						k1 = Integer.parseInt(date1) + 1;
						if (k1 < 10)
							date1 = "0" + k1;
						else
							date1 = "" + k1;
						url1 += year + month1 + date1 + "比數@";
					}
				}
//				for(int i = 0 ; i < a.length ; i++)
//					System.out.println(a[i]);
				b=url1.toString().split("@");
				for(int i=0;i<a.length;i++){
					try {
						crawler.crawling(new String[]{"http://www.nba.com/gameline/"+a[i]+"/"}, b[i]);
					} catch (ClassNotFoundException | IOException
							| SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		},2000 , 1000, TimeUnit.MILLISECONDS);
		
			
		
	}
	public static void store(String name, Connection conn, Statement stmt)
			throws SQLException {
		String sql = "CREATE TABLE " + name + " (`Away` VARCHAR(255), "
				+ "`Home` VARCHAR(255), " + "`Q1` VARCHAR(255) , "
				+ "`Q2` VARCHAR(255) , " + "`Q3` VARCHAR(255) , "
				+ "`Q4` VARCHAR(255) , " + "`OT1` VARCHAR(255) , "
				+ "`OT2` VARCHAR(255) , " + "`OT3` VARCHAR(255) , "
				+ "`OT4` VARCHAR(255) , " + "`F` VARCHAR(255)) "
				+ "ENGINE = MyISAM DEFAULT CHARSET = utf8 COLLATE = utf8_bin";

		// ///////建立UTF8編碼資料表

		stmt.executeUpdate(sql);
	}
	
}

