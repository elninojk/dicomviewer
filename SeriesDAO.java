package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import model.Series;
import model.Image;

public class SeriesDAO {
	private static String CREATESQL = "create table Series (seriesId CHARACTER VARYING(150) PRIMARY KEY NOT NULL,studyId CHARACTER VARYING(150) NOT NULL ,"
			+ "  seriesNumber CHARACTER VARYING(150), modality CHARACTER VARYING(150), seriesDescription CHARACTER VARYING(250))";
	private static String INSERTSQL = "insert into Series values(?, ? , ?, ?, ?)";
	private static String READBYSTUDYSQL = "select * from Series where (studyId = ?)";
	private static String READBYSERIESSQL = "select * from Series where (seriesId = ?)";
	
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

	public static DefaultTreeModel getSeriesTree(String studyId, String seriesId) throws Exception {             //////////ADDTOTREE

		PreparedStatement ps1 = null;
		DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(seriesId, true);
		DefaultTreeModel seriesTree = new DefaultTreeModel(parentNode);
		try {
			Connection con = DbConnector.getConnection();

			ps1 = con.prepareStatement(READBYSERIESSQL);
			ps1.setString(1, seriesId);
			ResultSet rs1 = ps1.executeQuery();
			String seriesColumns[] = { "<seriesId>", "<studyId>", "<seriesNumber>", "<modality>", "<seriesDescription>"};
			
			while (rs1.next()) {
				seriesTree.insertNodeInto(new DefaultMutableTreeNode(seriesColumns[0] + rs1.getString(1), false), parentNode, 0);
				seriesTree.insertNodeInto(new DefaultMutableTreeNode(seriesColumns[1] + rs1.getString(2), false), parentNode, 1);
				seriesTree.insertNodeInto(new DefaultMutableTreeNode(seriesColumns[2] + rs1.getString(3), false), parentNode, 2);
				seriesTree.insertNodeInto(new DefaultMutableTreeNode(seriesColumns[3] + rs1.getString(4), false), parentNode, 3);
				seriesTree.insertNodeInto(new DefaultMutableTreeNode(seriesColumns[4] + rs1.getString(5), false), parentNode, 4);
			}
			List imageList = null;
			try {
				imageList = ImageDAO.viewImageBySeries(studyId, seriesId);
			} catch (Exception e) {
				throw e;
			}
			ArrayList<String> imgNumberList = null;
			for(int i=0; i<imageList.size(); ++i) {
				Image img = (Image) imageList.get(i);
				DefaultTreeModel imgTree = ImageDAO.getImageTree(img.getImageNumber());
				seriesTree.insertNodeInto(imgTree.root, parentNode, 5+i);                            //////////////ADD TO TREEEE
			}
			
			return seriesTree;
		} catch (ClassNotFoundException e) {
			throw e;
		} catch (SQLException e) {
			throw e;
		}
	}
	
}
