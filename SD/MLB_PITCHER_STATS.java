package 測試專用;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mysql.jdbc.Statement;

public class MLB_PITCH_BBB_ONETABLE {
	public static void main(String args[]) throws SQLException,
			ClassNotFoundException, IOException {
		Class.forName("com.mysql.jdbc.Driver");
		// String jdbcurl =
		// "jdbc:mysql://163.13.201.32/mlb_pitcher_stats?useunicode=true&characterencoding=utf 8";
		// 本機
		String jdbcurl = "jdbc:mysql://localhost/mlb_pitcher_2014?useunicode=true&characterencoding=utf 8";
		// 遠端
		String user = "root";
		String password = "1234";
		Connection conn = DriverManager.getConnection(jdbcurl, user, password);
		Statement stmt = (Statement) conn.createStatement();
		ResultSet rs = stmt.executeQuery("select * from mlb_player_link");
		String link[] = new String[30];
		String separate_link[] = {
				"?cut_type=0&stat_category=mlb.stat_category.2",
				"?cut_type=32&stat_category=mlb.stat_category.2",
				"?cut_type=31&stat_category=mlb.stat_category.2",
				"?cut_type=33&stat_category=mlb.stat_category.2",
				"?cut_type=34&stat_category=mlb.stat_category.2",
				"?cut_type=35&stat_category=mlb.stat_category.2",
				"?cut_type=36&stat_category=mlb.stat_category.2" };

		String seperate_table[] = { "OVERALL", "RH", "LH", "HOME", "AWAY",
				"DAY", "NIGHT" };// for creating table

		String link2[] = new String[210];
		int k = 0;
		while (rs.next()) {
			link[k] = rs.getString(1);
			k++;
		}

		int count = 0;
		int a = 0;
		int x = 0;
		
		CreateTable(seperate_table[0]);
		CreateTable(seperate_table[1]);
		CreateTable(seperate_table[2]);
		CreateTable(seperate_table[3]);
		CreateTable(seperate_table[4]);
		CreateTable(seperate_table[5]);
		CreateTable(seperate_table[6]);
			 
	
		 

		for (int i = 0; i < separate_link.length; i++) {
			for (int j = 0; j < link.length; j++) {
				System.out.println(link[j] + separate_link[i]);
				link2[a] = link[j] + separate_link[i];

				if (a % 30 == 0 && a != 0) {
					System.out
							.println("----------------------------------"
									+ "--------------------------------------------------------------------------------------");
					count++;
				}

				System.out.print(CrawlerName(link2[a]));
				System.out.print(CrawlerTeamName(link2[a]));

				PreparedStatement pstmt = null;

				store(seperate_table[count], CrawlerName(link2[a]).split("%"),
						CrawlerTeamName(link2[a]).split("%"),
						CrawlerStats(link2[a]).split("%"), conn, stmt, pstmt);

				System.out.println("---done---" + a);

				a++;
			}
		}



	}

	public static String CrawlerName(String target) throws IOException {
		URL url = new URL(target);
		Document doc = Jsoup.parse(url, 3000000);
		Elements player = doc.select("div.data-container th");
		StringBuilder sb = new StringBuilder();
		int read = 0;
		try {
			while (read != -1) {
				if (read < 19)
					read++;

				else {
					sb.append(player.get(read).text() + "%");
					read++;
				}
			}

		} catch (Exception e) {
			read = -1;
			System.out
					.println("------------------------------------------------------------------------");
		}
		return sb.toString();

	}

	public static String CrawlerTeamName(String link) throws IOException {
		URL url = new URL(link);
		Document doc = Jsoup.parse(url, 30000);
		Elements team = doc.select("div.data-container th");
		StringBuilder sb = new StringBuilder();

		int read = 0;
		try {
			String after = link.replace("/", "").replace(":", "")
					.replace(".", "").replace("httpsportsyahoocommlbteams", "")
					.replace("stats", "").replace("&", "").replace("=", "")
					.replace("_cuttype", "").replaceAll("[0-9]", "")
					.replace("cut_typestat_categorymlbstat_category", "")
					.replace("?", "").replace(" ","");
			;

			while (read != -1) {
				if (read < 17)
					read++;
				else {
					team.get(read);
					sb.append(after + "%");
					read++;
				}
			}
		} catch (Exception e) {
			read = -1;
			System.out
					.println("------------------------------------------------------------------------");
		}
		return sb.toString();
	}

	public static String CrawlerStats(String target) throws IOException {
		URL url = new URL(target);
		Document doc = Jsoup.parse(url, 30000);
		Elements player = doc.select("div.data-container td");
		StringBuilder sb = new StringBuilder();
		int read = 0;
		String tmp;
		try {
			while (read != -1) {
				tmp = player.get(read).text().replace(" ","");
				sb.append(tmp+"%");
				read++;
			}

		} catch (Exception e) {
			read = -1;
			System.out
					.println("------------------------------------------------------------------------");
		}
		return sb.toString();
	}

	public static void store(String name, String Pname[], String Teamname[],
			String Pstats[], Connection conn, Statement stmt,
			PreparedStatement pstmt) throws SQLException {
		int j = 0, k = 0, o = 0;

		String filter = "pitcher_"
				+ name.replace("/", "").replace(":", "").replace(".", "")
						.replace("httpsportsyahoocommlbteams", "")
						.replace("stats", "_").replace("?", "")
						.replace("&", "").replace("=", "")
						.replace("_cuttype", "").replaceAll("[0-9]", "")
						.replace("cut_typestat_categorymlbstat_category", "");

		for (int n = 0; n < Pname.length; n++) { // 因為目標爬的東西是NBA的各隊資料 NBA共30隊
													// 所以直接新增30筆資料
			pstmt = (PreparedStatement) conn.prepareStatement("insert into "
					+ filter
					+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		
			for (int i = 1; i < 21; i++) {
				if (i == 1) { // 第一筆資料跟第二筆資料存在name.txt所以用if , else if拉出
					pstmt.setString(1, Pname[j]);// pstmt.setString(資料位置,資料內容)
					j++;
				}
				else if (i == 2) {
					pstmt.setString(2, Teamname[o]);
					o++;
				} else {
					pstmt.setString(i, Pstats[k]);
					k++;
				}
			}
			pstmt.executeUpdate();// 更新資料到資料庫裡面
		}
	}

	public static void CreateTable(String name) throws ClassNotFoundException,
			SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		String jdbcurl = "jdbc:mysql://localhost/mlb_pitcher_new?useunicode=true&characterencoding=utf 8";
		String user = "root";
		String password = "1234";
		Connection conn = DriverManager.getConnection(jdbcurl, user, password);
		Statement stmt = (Statement) conn.createStatement();

		String filter = "pitcher_"
				+ name.replace("/", "").replace(":", "").replace(".", "")
						.replace("httpsportsyahoocommlbteams", "")
						.replace("stats", "_").replace("?", "")
						.replace("&", "").replace("=", "")
						.replace("_cuttype", "").replaceAll("[0-9]", "")
						.replace("cut_typestat_categorymlbstat_category", "");

		// cuttype31statcategorymlbstatcategory2
		// .replace("cuttype0statcategorymlbstatcategory2", "") 原本的

		// G GS W L SV BS HLD CG K IP H R ER HR BB ERA WHIP BAA
		String sql = "CREATE TABLE " + filter + " (NAME VARCHAR(255),"
				+ "`TEAM` VARCHAR(255)," + "`G` VARCHAR(255),"
				+ "`GS` VARCHAR(255)," + "`W` VARCHAR(255),"
				+ "`L` VARCHAR(255)," + "`SV` VARCHAR(255),"
				+ "`BS` VARCHAR(255)," + "`HLD` VARCHAR(255),"
				+ "`CG` VARCHAR(255)," + "`K` VARCHAR(255),"
				+ "`IP` VARCHAR(255)," + "`H` VARCHAR(255),"
				+ "`R` VARCHAR(255)," + "`ER` VARCHAR(255),"
				+ "`HR` VARCHAR(255)," + "`BB` VARCHAR(255),"
				+ "`ERA` VARCHAR(255)," + " `WHIP` VARCHAR(255),"
				+ " `BAA` VARCHAR(255))";
		stmt.executeUpdate(sql);
	}

}
