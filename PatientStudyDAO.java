package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;


import model.Image;
import model.PatientStudy;
import model.Series;

public class PatientStudyDAO {
	private static String CREATESQL = "create table PatientStudy (patientId CHARACTER VARYING(150) NOT NULL, patientName CHARACTER VARYING(150) , "
			+ "patientDOB CHARACTER VARYING(150) , accessionNumber CHARACTER VARYING(150) ,"
			+ "studyId CHARACTER VARYING(150) PRIMARY KEY NOT NULL, studyDescription CHARACTER VARYING(250) , studyDateTime CHARACTER VARYING(150) )";
	private static String INSERTSQL = "insert into PatientStudy values(?, ? , ?, ?, ?, ?, ?)";
	private static String SORTSQL = "select * from patientStudy ORDER BY ?";
	private static String READALLSQL = "select * from PatientStudy";
	private static String READROWSQL = "select * from PatientStudy where studyId = ?";


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

	public static void insert(PatientStudy pStudy) throws Exception {

		Connection con = null;
		PreparedStatement pStmt = null;
		try {
			con = DbConnector.getConnection();
			
			pStmt = con.prepareStatement(INSERTSQL);
			pStmt.setString(1, pStudy.getPatientId());
			pStmt.setString(2, pStudy.getPatientName());
			pStmt.setString(3, pStudy.getPatientDOB());
			pStmt.setString(4, pStudy.getAccessionNumber());
			pStmt.setString(5, pStudy.getStudyId());
			pStmt.setString(6, pStudy.getStudyDescription());
			pStmt.setString(7, pStudy.getStudyDateTime());
			pStmt.executeUpdate();

		} catch (Exception ex) {
			throw ex;

		} finally {

			pStmt.close();
			con.close();

		}
	}

	public static List<PatientStudy> viewAllPatientStudy() throws Exception {

		Connection con = null;
		PreparedStatement pStmt = null;
		try {
			con = DbConnector.getConnection();

			pStmt = con.prepareStatement(READALLSQL);

			ResultSet rs = pStmt.executeQuery();
			ArrayList<PatientStudy> pStudyList = new ArrayList<>();
			boolean flag = false;
			while (rs.next()) {
				flag = true;
				PatientStudy pStudy = new PatientStudy(rs.getString(1), rs.getString(2), rs.getString(3),
						rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));
				pStudyList.add(pStudy);
			}
			if (!flag) {
				throw new Exception("Study Not Found !!!");
			}
			return pStudyList;
		} catch (Exception ex) {
			throw ex;

		} finally {
			pStmt.close();
			con.close();
		}
	}

	public static List<PatientStudy> sort(String colName) throws Exception {
		Connection con = null;
		PreparedStatement pStmt = null;
		try {
			con = DbConnector.getConnection();

			pStmt = con.prepareStatement(SORTSQL);
			pStmt.setString(1, colName);

			ResultSet rs = pStmt.executeQuery();
			ArrayList<PatientStudy> pStudyList = new ArrayList<>();
			boolean flag = false;
			while (rs.next()) {
				flag = true;
				PatientStudy pStudy = new PatientStudy(rs.getString(1), rs.getString(2), rs.getString(3),
						rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));
				pStudyList.add(pStudy);
			}
			if (!flag) {
				throw new Exception("Study Not Found !!!");
			}
			return pStudyList;
		} catch (Exception ex) {
			throw ex;

		} finally {
			pStmt.close();
			con.close();
		}
	}

	public static List<PatientStudy> filter(String colName, String lowValue, String highValue) throws Exception {

		PreparedStatement pStmt = null;
		Connection con = null;
		try {
			con = DbConnector.getConnection();
			pStmt = con.prepareStatement( "select * from patientStudy WHERE " + colName +  "::int BETWEEN " + Integer.parseInt(lowValue) + " AND " + Integer.parseInt(highValue));
			ResultSet rs = pStmt.executeQuery();

			ArrayList<PatientStudy> pStudyList = new ArrayList<>();
			boolean flag = false;
			while (rs.next()) {
				flag = true;
				PatientStudy pStudy = new PatientStudy(rs.getString(1), rs.getString(2), rs.getString(3),
						rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));
				pStudyList.add(pStudy);
			}
			if (!flag) {
				throw new Exception("Study Not Found");
			}
			return pStudyList;
		} catch (Exception ex) {
			throw ex;
		} finally {
			pStmt.close();
			con.close();
		}
		
	}

	public static DefaultTreeModel getStudyTree(String studyId) throws Exception {             

		PreparedStatement ps1 = null;
		DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(studyId, true);
		DefaultTreeModel studyTree = new DefaultTreeModel(parentNode);
		try {
			Connection con = DbConnector.getConnection();

			ps1 = con.prepareStatement(READROWSQL);
			ps1.setString(1, studyId);
			ResultSet rs1 = ps1.executeQuery();
			String patientStudyColoumns[] = { "(0010,0020) [LO] <patientId>", "(0010,0010) [PN] <patientName>", "(0010,0030) [DA] <patientDOB>", "(0008,0050) [SH] <accessionNumber>", "(0020,0010) [SH] <studyId>",
					"(0008,1030) [LO] <studyDescription>", "(0032,0032) [DA] <studyDateTime>" };
			
			while (rs1.next()) {
				studyTree.insertNodeInto(new DefaultMutableTreeNode(patientStudyColoumns[0] + "[" + rs1.getString(1) + "]", false), parentNode, 0);
				studyTree.insertNodeInto(new DefaultMutableTreeNode(patientStudyColoumns[1] + "[" + rs1.getString(2) + "]", false), parentNode, 1);
				studyTree.insertNodeInto(new DefaultMutableTreeNode(patientStudyColoumns[2] + "[" + rs1.getString(3) + "]", false), parentNode, 2);
				studyTree.insertNodeInto(new DefaultMutableTreeNode(patientStudyColoumns[3] + "[" + rs1.getString(4) + "]", false), parentNode, 3);
				studyTree.insertNodeInto(new DefaultMutableTreeNode(patientStudyColoumns[4] + "[" + rs1.getString(5) + "]", false), parentNode, 4);
				studyTree.insertNodeInto(new DefaultMutableTreeNode(patientStudyColoumns[5] + "[" + rs1.getString(6) + "]", false), parentNode, 5);
				studyTree.insertNodeInto(new DefaultMutableTreeNode(patientStudyColoumns[6] + "[" + rs1.getString(7) + "]", false), parentNode, 6);
			}
			List<Series> seriesList = null;
			try {
				seriesList = SeriesDAO.viewSeriesByStudy(studyId);             
			} catch (Exception e) {
				throw e;
			}
			for(int i=0; i<seriesList.size(); ++i) {
				Series srs = seriesList.get(i);
				studyTree = SeriesDAO.getSeriesTree(studyTree, parentNode, studyId, srs.getSeriesId());
			}
			
			return studyTree;
		} catch (ClassNotFoundException e) {
			throw e;
		} catch (SQLException e) {
			throw e;
		}
	}
}
