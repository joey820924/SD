

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class NBAscore2013 {
	public static void main(String[] args) throws Exception {
				String a[]=null;
				String b[]=null;
				String year = "2013";
				String month = "09";
				String date = "00";
				String url = "";
				int k;
				//0 6
				
				for (int j = 10; j < 13; j++) {
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
				
				String month1 = "09";
				String date1 = "00";
				String url1 = "";
				int k1;

				for (int j = 10; j < 13; j++) {
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
						url1 += "`"+year + month1 + date1 + "`@";
					}
				}
				
				b=url1.toString().split("@");
				
				
						Crawler("20131007","`20131007");
			}

	public static void Crawler(String a, String n) throws SQLException,
			ClassNotFoundException, IOException {
		Class.forName("com.mysql.jdbc.Driver");
		String jdbcurl = "jdbc:mysql://localhost/score_2013?useunicode=true&characterencoding=utf 8";
		String user = "root";
		String password = "joey820924";
		StringBuilder sb = new StringBuilder();
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2=new StringBuilder();
		URL url = new URL("http://www.nba.com/gameline/"+a+"/");
		Document doc = Jsoup.parse(url, 30000);
		Elements stat = doc.select("div.nbaMnQuScores td");
		Elements name = doc.select("div.nbaModTopTeamName");
		Elements num=doc.select("td");
		int i1 = 0;
		int k=0;
		int o=44;
		int j = 0;
		int p=44;
		try {
			PreparedStatement pstmt = null;
			while (j != -1) {
				
					sb1.append(name.get(j).text() + "&");
				j++;
			}

		} catch (Exception e) {
			j = -1;
			
		}
		
		try {
			PreparedStatement pstmt = null;
			
			while (p != -1) {
				//System.out.println(num.get(p).text()+"("+p+")");
				if(o==45){
					o=62;
				}
				if(o==63){
					o=101;
				}
				if(o==102){
					o=119;
				}
				if(o==120){
					o=158;
				}
				if(o==159){
					o=176;
				}
				if(o==177){
					o=215;
				}
				if(o==216){
					o=233;
				}
				if(o==234){
					p=-2;
				}
				
				
				if(p%o==0){
					sb2.append(num.get(p).text()+"&");	
					o++;
				}
				p++;
				
				
			}

		} catch (Exception e) {
			p = -1;
		}
		try{
			while(i1!=-1){
				//System.out.println(stat.get(i1).text()+"("+i1+")");
				if(i1%1000==k){
					sb.append(stat.get(i1).text()+"&");
					k++;
				}
				i1++;
				
				if(k==8){
					k=18;
				}
				if(k==26){
					k=27;
				}
				if(k==35){
					k=45;
				}
				if(k==53)
					k=54;
				if(k==62){
					k=72;
				}
				if(k==80){
					k=81;
				}
				if(k==89){
					k=99;
				}
				if(k==107){
					k=108;
				}
				if(k==116){
					k=126;
				}
				if(k==134){
					k=135;
				}
				if(k==143){
					k=153;
				}
				if(k==161){
					k=162;
				}
				if(k==170){
					k=180;
				}
				if(k==188){
					k=189;
				}
				if(k==197){
					k=207;
				}
				
				
				
				
				
				
			}
		}catch(Exception ee){
			i1=-1;
		}
		
		while (sb1.length() != 0) {
			try (Connection conn = DriverManager.getConnection(jdbcurl, user,
					password)) {
				System.out.printf("資料庫已%s %n", conn.isClosed() ? "關閉" : "開-起");
				Statement stmt = conn.createStatement();
				
				try {
					 
					String NBApoint[] = sb.toString().split("&");
					String NBAname[] = sb1.toString().split("&");
					String NBAnum[]=sb2.toString().split("&");
					
					store(n, conn, stmt);
					int k1 = 0;
					int l = 0;
					int x =0;
					PreparedStatement pstmt = null;
					while (k1 < 22) {
						if (k1 % 2 == 0) {
							pstmt = conn.prepareStatement("INSERT INTO " + n
									+ " VALUES(?,?,?,?,?,?,?,?,?,?,?)");
							for (int i11 = 0; i11 < 11; i11++) {
								if (i11 == 0) {
									pstmt.setString(1, NBAname[k1]);
									k1++;
								} else if (i11 == 1) {
									pstmt.setString(2, null);
								}else if(i11==10){
									pstmt.setString(11, NBAnum[x]);
									x++;
								} else{
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
									pstmt.setString(2, NBAname[k1]);
									k1++;
								}else if(i11==10){
									pstmt.setString(11, NBAnum[x]);
										x++;
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
		stmt.executeUpdate(sql);
	

	}
}