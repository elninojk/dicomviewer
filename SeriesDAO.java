package dicomviewer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SeriesDAO {

	private static String CREATESQL = "create table Series (seriesId CHARACTER VARYING(150) PRIMARY KEY NOT NULL,studyId CHARACTER VARYING(150) NOT NULL ,"
			+ "  seriesNumber CHARACTER VARYING(150), modality CHARACTER VARYING(150), seriesDescription CHARACTER VARYING(250))";
	private static String INSERTSQL = "insert into Series values(?, ? , ?, ?, ?)";
	private static String READBYSTUDYSQL = "select * from Series where (studyId = ?)";
	private static String FILTERSQL = "select * from series where ? BETWEEN ? AND ?";

//	OK
	public static void create() throws Exception {
		Connection con = null;
		PreparedStatement pStmt = null;
		try {
			con = DbConnector.getConnection();

			pStmt = con.prepareStatement(CREATESQL);
			pStmt.executeUpdate();
		} catch (Exception ex) {
			throw ex;
		} finally {
			pStmt.close();
			con.close();
		}

	}
//  OK
	public static void insert(Series series) throws Exception {

		Connection con = null;
		PreparedStatement pStmt = null;
		try {
			con = DbConnector.getConnection();

			pStmt = con.prepareStatement(INSERTSQL);
			pStmt.setString(1, series.getSeriesId());
			pStmt.setString(2, series.getStudyId());
			pStmt.setString(3, series.getSeriesNumber()); /// POSSIBLE EXCEPTION//UTIL DATE
			pStmt.setString(4, series.getModality());
			pStmt.setString(5, series.getSeriesDescription());

			pStmt.executeUpdate();

		} catch (Exception ex) {
			throw ex;

		} finally {

			pStmt.close();
			con.close();

		}
	}
//  OK
	public static List<Series> viewSeriesByStudy(String studyId) throws Exception {

		Connection con = null;
		PreparedStatement pStmt = null;
		try {
			con = DbConnector.getConnection();

			pStmt = con.prepareStatement(READBYSTUDYSQL);
			pStmt.setString(1, studyId);

			ResultSet rs = pStmt.executeQuery();
			ArrayList<Series> seriesList = new ArrayList<>();
			boolean flag = false;
			while (rs.next()) {
				flag = true;
				Series series = new Series(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5));
				seriesList.add(series);
			}
			if (!flag) {
				throw new Exception("Series Not Found !!!");
			}
			return seriesList;
		} catch (Exception ex) {
			throw ex;

		} finally {
			pStmt.close();
			con.close();
		}
	}
//  OK
	public static List<Series> filter(String colName, String lowValue, String highValue) throws Exception {

		PreparedStatement pStmt = null;
		Connection con = null;
		try {
			con = DbConnector.getConnection();
			pStmt = con.prepareStatement( "select * from Series WHERE " + colName +  "::int BETWEEN " + Integer.parseInt(lowValue) + " AND " + Integer.parseInt(highValue));
//			pStmt.setString(1, colName);
//			pStmt.setString(2, lowValue);
//			pStmt.setString(3, highValue);
			ResultSet rs = pStmt.executeQuery();

			ArrayList<Series> seriesList = new ArrayList<>();
			boolean flag = false;
			while (rs.next()) {
				flag = true;
				Series seies = new Series(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5));
				seriesList.add(seies);
			}
			if (!flag) {
				throw new Exception("Study Not Found");
			}
			return seriesList;
		} catch (Exception ex) {
			throw ex;
		} finally {
			pStmt.close();
			con.close();
		}

	}

	public static void main(String args[]) {
		try {
//			create();
//			insert(new Series("s2", "p2", "454ygs555", "5575", "hgh ghjghvhg h"));
			
//			ArrayList<Series> seriesList = (ArrayList<Series>)viewSeriesByStudy("p1");
//			int size = seriesList.size();
//			while(size>=0) {
//				Series srs = seriesList.get(--size);
//				System.out.println(srs.getModality());
//			}
			
//			ArrayList<Series> series = (ArrayList<Series>)filter("modality", "23","5585");
//			int size = series.size();
//			while(size>=0) {
//				Series srs = series.get(--size);
//				System.out.println(srs.getModality());
//			}


		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
