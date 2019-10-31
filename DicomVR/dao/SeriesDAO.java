package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Series;

public class SeriesDAO {
	
	private static String INSERTSQL = "insert into Series values(?, ? , ?, ?, ?)";
	private static String READBYSTUDYSQL = "select * from Series where (studyId = ?)";
	
	static DbConnector connector = DbConnector.getInstance();

	

	public static void insert(Series series) throws Exception {

		Connection con = null;
		PreparedStatement pStmt = null;
		try {
			con = connector.getConnection();

			pStmt = con.prepareStatement(INSERTSQL);
			pStmt.setString(1, series.getSeriesId());
			pStmt.setString(2, series.getStudyId());
			pStmt.setString(3, series.getSeriesNumber()); 
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

	public static List<Series> viewSeriesByStudy(String studyId) throws Exception {

		Connection con = null;
		PreparedStatement pStmt = null;
		try {
			con = connector.getConnection();

			pStmt = con.prepareStatement(READBYSTUDYSQL);
			pStmt.setString(1, studyId);

			ResultSet rs = pStmt.executeQuery();
			ArrayList<Series> seriesList = new ArrayList<>();
			while (rs.next()) {
				Series series = new Series(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5));
				seriesList.add(series);
			}			
			return seriesList;
		} catch (Exception ex) {
			throw ex;

		} finally {
			pStmt.close();
			con.close();
		}
	}

	
	
}