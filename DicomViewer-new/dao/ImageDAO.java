package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import model.Image;

public class ImageDAO {
	private static String CREATESQL = "create table Image (imageNumber CHARACTER VARYING(150) ,seriesId CHARACTER VARYING(150) NOT NULL,"
			+ "studyId CHARACTER VARYING(150) NOT NULL, imageType CHARACTER VARYING(150), rows CHARACTER VARYING(150),"
			+ "columns CHARACTER VARYING(250)  ,bitsAllocated CHARACTER VARYING(250)  ,bitsStored CHARACTER VARYING(250) )";
	private static String INSERTSQL = "insert into Image values(?, ?, ?, ?, ?, ?, ?, ?)";
	private static String READBYSERIESSQL = "select * from Image where (seriesId = ? AND studyId = ?)";
	private static String READSTUDYSQL = "select * from PatientStudy where studyId = ?";
	private static String READSERIESSQL = "select * from Series where studyId = ? and seriesId = ?";
	private static String READROWSQL = "select * from Image where studyId= ? and seriesId = ? and imageNumber = ?";


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

	public static void insert(Image image) throws Exception {

		Connection con = null;
		PreparedStatement pStmt = null;
		try {
			con = DbConnector.getConnection();

			pStmt = con.prepareStatement(INSERTSQL);
			pStmt.setString(1, image.getImageNumber());
			pStmt.setString(2, image.getSeriesId());
			pStmt.setString(3, image.getStudyId());
			pStmt.setString(4, image.getImageType()); 
			pStmt.setString(5, image.getRows());
			pStmt.setString(6, image.getColumns());
			pStmt.setString(7, image.getBitsAllocated());
			pStmt.setString(8, image.getBitsStored());

			pStmt.executeUpdate();

		} catch (Exception ex) {
			throw ex;

		} finally {

			pStmt.close();
			con.close();

		}
	}

	public static List<Image> viewImageBySeries(String studyId, String seriesId) throws Exception {

		Connection con = null;
		PreparedStatement pStmt = null;
		try {
			con = DbConnector.getConnection();

			pStmt = con.prepareStatement(READBYSERIESSQL);
			pStmt.setString(1, seriesId);
			pStmt.setString(2, studyId);

			ResultSet rs = pStmt.executeQuery();
			ArrayList<Image> imageList = new ArrayList<>();
			boolean flag = false;
			while (rs.next()) {
				flag = true;
				Image image = new Image(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8));
				imageList.add(image);
			}
			if (!flag) {
				throw new Exception("Image Not Found !!!");
			}
			return imageList;
		} catch (Exception ex) {
			throw ex;

		} finally {
			pStmt.close();
			con.close();
		}
	}

	public static List<Image> filter(String colName, String lowValue, String highValue) throws Exception {

		PreparedStatement pStmt = null;
		Connection con = null;
		try {
			con = DbConnector.getConnection();
			pStmt = con.prepareStatement("select * from Image WHERE " + colName + "::int BETWEEN "
					+ Integer.parseInt(lowValue) + " AND " + Integer.parseInt(highValue));
			ResultSet rs = pStmt.executeQuery();

			ArrayList<Image> imageList = new ArrayList<>();
			boolean flag = false;
			while (rs.next()) {
				flag = true;
				Image image = new Image(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(7));
				imageList.add(image);
			}
			if (!flag) {
				throw new Exception("Image Not Found");
			}
			return imageList;
		} catch (Exception ex) {
			throw ex;
		} finally {
			pStmt.close();
			con.close();
		}

	}

	public static LinkedHashMap<String, String> details(String stdyId, String srsId, String imgNumber)
			throws Exception {
		Connection con = null;
		PreparedStatement pStmt1 = null;
		PreparedStatement pStmt2 = null;
		PreparedStatement pStmt3 = null;
		try {
			con = DbConnector.getConnection();

			pStmt1 = con.prepareStatement(READSTUDYSQL);
			pStmt1.setString(1, stdyId);
			pStmt2 = con.prepareStatement(READSERIESSQL);
			pStmt2.setString(1, stdyId);
			pStmt2.setString(2, srsId);
			pStmt3 = con.prepareStatement(READROWSQL);
			pStmt3.setString(1, stdyId);
			pStmt3.setString(2, srsId);
			pStmt3.setString(3, imgNumber);

			LinkedHashMap<String, String> detailsTable = new LinkedHashMap<>();
			String patientStudyColumns[] = { "patientId", "patientName", "patientDOB", "accessionNumber", "studyId",
					"studyDescription", "studyDateTime" };
			ResultSet rs1 = pStmt1.executeQuery();
			while (rs1.next()) {
				detailsTable.put(patientStudyColumns[0], rs1.getString(1));
				detailsTable.put(patientStudyColumns[1], rs1.getString(2));
				detailsTable.put(patientStudyColumns[2], rs1.getString(3));
				detailsTable.put(patientStudyColumns[3], rs1.getString(4));
				detailsTable.put(patientStudyColumns[4], rs1.getString(5));
				detailsTable.put(patientStudyColumns[5], rs1.getString(6));
				detailsTable.put(patientStudyColumns[6], rs1.getString(7));
			}

			String seriesColumns[] = { "seriesId", "studyId", "seriesNumber", "modality", "seriesDescription" };
			ResultSet rs2 = pStmt2.executeQuery();
			while (rs2.next()) {
				detailsTable.put(seriesColumns[0], rs2.getString(1));
				detailsTable.put(seriesColumns[1], rs2.getString(2));
				detailsTable.put(seriesColumns[2], rs2.getString(3));
				detailsTable.put(seriesColumns[3], rs2.getString(4));
				detailsTable.put(seriesColumns[4], rs2.getString(5));

			}

			String imageColumns[] = { "imageNumber", "studyId", "seriesId", "imageType", "rows", "columns",
					"bitsAllocated", "bitsStored" };
			ResultSet rs3 = pStmt3.executeQuery();
			while (rs3.next()) {
				detailsTable.put(imageColumns[0], rs3.getString(1));
				detailsTable.put(imageColumns[1], rs3.getString(2));
				detailsTable.put(imageColumns[2], rs3.getString(3));
				detailsTable.put(imageColumns[3], rs3.getString(4));
				detailsTable.put(imageColumns[4], rs3.getString(5));
				detailsTable.put(imageColumns[5], rs3.getString(6));
				detailsTable.put(imageColumns[6], rs3.getString(7));
				detailsTable.put(imageColumns[7], rs3.getString(8));

			}
			return detailsTable;
		} catch (Exception ex) {
			throw ex;

		} finally {
			pStmt1.close();
			pStmt2.close();
			pStmt3.close();
			con.close();
		}

	}

	public static DefaultTreeModel getImageTree(DefaultTreeModel studyTree, DefaultMutableTreeNode grandParent, String studyId, String seriesId, String imgNumber) throws Exception {

		PreparedStatement ps1 = null;
		int index = studyTree.getChildCount(grandParent);
		DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(imgNumber, true);
		studyTree.insertNodeInto(parentNode, grandParent, index);
		try {
			Connection con = DbConnector.getConnection();

			ps1 = con.prepareStatement(READROWSQL);
			ps1.setString(1, studyId);
			ps1.setString(2, seriesId);
			ps1.setString(3, imgNumber);
			ResultSet rs1 = ps1.executeQuery();
			String imageColumns[] = { "(0008,0018) [UI] <SOP Instance UID>", "(0020,0010) [SH] <studyId>", "(0020,000E)[UI] <seriesId>", "(0008,0008) [CS] <imageType>",
					"(0028,0010)[US] <rows>", "(0028,0011) [US] <columns>",	"(0028,0100)[US] <bitsAllocated>", "(0028,0101) [US] <bitsStored>" };
			while (rs1.next()) {

				studyTree.insertNodeInto(new DefaultMutableTreeNode(imageColumns[0] + "[" + rs1.getString(1) + "]", false), parentNode, 0);
//				studyTree.insertNodeInto(new DefaultMutableTreeNode(imageColumns[1] + "[" + rs1.getString(2) + "]", false), parentNode, 1);
//				studyTree.insertNodeInto(new DefaultMutableTreeNode(imageColumns[2] + "[" + rs1.getString(3) + "]", false), parentNode, 2);
				studyTree.insertNodeInto(new DefaultMutableTreeNode(imageColumns[3] + "[" + rs1.getString(4) + "]", false), parentNode, 1);
				studyTree.insertNodeInto(new DefaultMutableTreeNode(imageColumns[4] + "[" + rs1.getString(5) + "]", false), parentNode, 2);
				studyTree.insertNodeInto(new DefaultMutableTreeNode(imageColumns[5] + "[" + rs1.getString(6) + "]", false), parentNode, 3);
				studyTree.insertNodeInto(new DefaultMutableTreeNode(imageColumns[6] + "[" + rs1.getString(7) + "]", false), parentNode, 4);
				studyTree.insertNodeInto(new DefaultMutableTreeNode(imageColumns[7] + "[" + rs1.getString(8) + "]", false), parentNode, 5);
			}
			return studyTree;
		} catch (ClassNotFoundException e) {
			throw e;
		} catch (SQLException e) {
			throw e;
		}
	}
}