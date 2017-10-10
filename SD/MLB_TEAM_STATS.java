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
import org.jsoup.select.Elements;

import com.mysql.jdbc.Statement;

public class MLB_TEAM_STATS {
	public static void main(String args[]) throws IOException,
			ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		String jdbcurl = "jdbc:mysql://localhost/mlb_team_stats?useunicode=true&characterencoding=utf 8";
		String user = "root";
		String password = "1234";
		Connection conn = DriverManager.getConnection(jdbcurl, user, password);
		Statement stmt = (Statement) conn.createStatement();
		PreparedStatement pstmt = null;
		System.out
				.println(CrawlerTS("http://sports.yahoo.com/mlb/stats/byteam")
						.replace("?", ""));
		String table[] = {"OVERALL" ,"HOME", "ROAD", "DAY", "NIGHT", "INN16", "INN7",
				"NONEONBASE", "SCORINGPOSTITION", "SCORINGPOSTION2OUT",
				"BASEDLOAD" };
		
		String link[] =
			{
				"http://sports.yahoo.com/mlb/stats/byteam?cat=Overall&conference=MLB&year=season_2015",
				"http://sports.yahoo.com/mlb/stats/byteam?cat=Splits&conference=MLB&year=season_2015",
				"http://sports.yahoo.com/mlb/stats/byteam?cat=Splits&cut_type=34&conference=MLB&year=season_2015&sort=722",
				"http://sports.yahoo.com/mlb/stats/byteam?cat=Splits&cut_type=35&conference=MLB&year=season_2015&sort=722",
				"http://sports.yahoo.com/mlb/stats/byteam?cat=Splits&cut_type=36&conference=MLB&year=season_2015&sort=722",
				"http://sports.yahoo.com/mlb/stats/byteam?cat=Situational&conference=MLB&year=season_2015",
				"http://sports.yahoo.com/mlb/stats/byteam?cat=Situational&cut_type=48&conference=MLB&year=season_2015&sort=722",
				"http://sports.yahoo.com/mlb/stats/byteam?cat=Situational&cut_type=58&conference=MLB&year=season_2015&sort=722",
				"http://sports.yahoo.com/mlb/stats/byteam?cat=Situational&cut_type=39&conference=MLB&year=season_2015&sort=722",
				"http://sports.yahoo.com/mlb/stats/byteam?cat=Situational&cut_type=185&conference=MLB&year=season_2015&sort=722",
				"http://sports.yahoo.com/mlb/stats/byteam?cat=Situational&cut_type=94&conference=MLB&year=season_2015&sort=722"
			};
		
		
		for (int i=0;i<link.length;i++) {
		CreateTable("TEAM_"+table[i]);
		store("TEAM_"+table[i],
				CrawlerTS(link[i])
						.split("&"),
				CrawlerAVG(link[i]).split(
						"&"), conn, stmt, pstmt);
		}
	}

	public static String CrawlerTS(String link) throws IOException {
		URL url = new URL(link);
		Document doc = Jsoup.parse(url, 30000);
		Elements avg = doc.select("td[class~=ysptblclbg6]");
		Elements team = doc.select("td[class~=yspscores]");
		StringBuilder sb = new StringBuilder();
		int i = 0;
		try {
			while (i != -1) {
				sb.append(team.get(i).text() + "&");
				i++;
			}
		} catch (Exception e) {
			i = -1;
		}
		return sb.toString();
	}

	public static String CrawlerAVG(String link) throws IOException {
		URL url = new URL(link);
		Document doc = Jsoup.parse(url, 30000);
		Elements avg = doc.select("td[class~=ysptblclbg6]");
		StringBuilder sb = new StringBuilder();
		int i = 0;
		try {
			while (i != -1) {
				sb.append(avg.get(i).text() + "&");
				i++;
			}
		} catch (Exception e) {
			i = -1;
		}
		return sb.toString();
	}

	public static void CreateTable(String table) throws ClassNotFoundException,
			SQLException {

		Class.forName("com.mysql.jdbc.Driver");
		String jdbcurl = "jdbc:mysql://localhost/mlb_team_stats?useunicode=true&characterencoding=utf 8";
		String user = "root";
		String password = "1234";
		Connection conn = DriverManager.getConnection(jdbcurl, user, password);
		Statement stmt = (Statement) conn.createStatement();

		String sql = "CREATE TABLE " + table + " (`TEAM` VARCHAR(255), "
				+ "`AVG` VARCHAR(255)," + "`R` VARCHAR(255), "
				+ "`BATTINGH` VARCHAR(255) , " + "`HR` VARCHAR(255) , "
				+ "`2B` VARCHAR(255) , " + "`3B` VARCHAR(255) , "
				+ "`RBI` VARCHAR(255) , " + "`SB` VARCHAR(255), "
				+ "`OBP` VARCHAR(255), " + "`SLG` VARCHAR(255), "
				+ "`SPACE1` VARCHAR(255)," + "`ERA` VARCHAR(255), "
				+ "`PITCHINGH` VARCHAR(255), " + "`BB` VARCHAR(255), "
				+ "`K` VARCHAR(255), " + "`SV` VARCHAR(255), "
				+ "`WHIP` VARCHAR(255), " + "`SPACE2` VARCHAR(255))";
		stmt.execute(sql);
	}

	public static void store(String name, String teamstat[], String avg[],
			Connection conn, Statement stmt, PreparedStatement pstmt)
			throws SQLException {
		int j = 0;
		int o = 0;
		while (j < teamstat.length) {
			pstmt = (PreparedStatement) conn.prepareStatement("insert into "
					+ name + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			for (int i = 1; i < 20; i++) {
				if (i == 2) {
					pstmt.setString(2, avg[o]);
					o++;
				} else {
					pstmt.setString(i, teamstat[j]);
					j++;
				}
			}
			pstmt.executeUpdate();// 更新資料到資料庫裡面
		}
	}

}
