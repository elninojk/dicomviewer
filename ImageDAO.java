package dicomviewer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import javax.swing.table.DefaultTableModel;

public class ImageDAO {

	private static String CREATESQL = "create table Image (imageNumber CHARACTER VARYING(150) PRIMARY KEY NOT NULL,seriesId CHARACTER VARYING(150) NOT NULL,"
			+ "studyId CHARACTER VARYING(150) NOT NULL, imageType CHARACTER VARYING(150), rows CHARACTER VARYING(150),"
			+ "columns CHARACTER VARYING(250)  ,bitsAllocated CHARACTER VARYING(250)  ,bitsStored CHARACTER VARYING(250) )";
	private static String INSERTSQL = "insert into Image values(?, ?, ?, ?, ?, ?, ?, ?)";
	private static String READBYSERIESSQL = "select * from Image where (seriesId = ? AND studyId = ?)";
	private static String READSTUDYSQL = "select * from PatientStudy where studyId = ?";
	private static String READSERIESSQL = "select * from Series where seriesId = ?";
	private static String READIMAGESQL = "select * from Image where imageNumber = ?";
	private static String FILTERSQL = "select * from Image where ? BETWEEN ? AND ?";

	// OK
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

	// OK
	public static void insert(Image image) throws Exception {

		Connection con = null;
		PreparedStatement pStmt = null;
		try {
			con = DbConnector.getConnection();

			pStmt = con.prepareStatement(INSERTSQL);
			pStmt.setString(1, image.getImageNumber());
			pStmt.setString(2, image.getSeriesId());
			pStmt.setString(3, image.getStudyId());
			pStmt.setString(4, image.getImageType()); /// POSSIBLE EXCEPTION//UTIL DATE
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

	// OK
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

	// OK
	public static List<Image> filter(String colName, String lowValue, String highValue) throws Exception {

		PreparedStatement pStmt = null;
		Connection con = null;
		try {
			con = DbConnector.getConnection();
			pStmt = con.prepareStatement("select * from Image WHERE " + colName + "::int BETWEEN "
					+ Integer.parseInt(lowValue) + " AND " + Integer.parseInt(highValue));
			// pStmt.setString(1, colName);
			// pStmt.setString(2, lowValue);
			// pStmt.setString(3, highValue);
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

	// OK
	public static LinkedHashMap<String, String> details(String stdyId, String srsId, String ImgNumber)
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
			pStmt2.setString(1, srsId);
			pStmt3 = con.prepareStatement(READIMAGESQL);
			pStmt3.setString(1, ImgNumber);

			LinkedHashMap<String, String> detailsTable = new LinkedHashMap<>();
			// boolean flag = false;

			String patientStudyColumns[] = { "patientId", "patientName", "patientDOB", "accessionNumber", "studyId",
					"studyDescription", "studyDateTime" };
			ResultSet rs1 = pStmt1.executeQuery();
			while (rs1.next()) {
				// flag = true;
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

			// if (!flag) {
			// throw new Exception("Image Not Found !!!");
			// }
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

	public static DefaultTableModel compareImage(String stdyId1, String srsId1, String imgNumber1, String stdyId2,
			String srsId2, String imgNumber2) {

		DefaultTableModel compTable = new DefaultTableModel();

		Connection con = null;
		PreparedStatement pStmt1 = null;
		PreparedStatement pStmt2 = null;
		PreparedStatement pStmt3 = null;
		PreparedStatement pStmt4 = null;
		PreparedStatement pStmt5 = null;
		PreparedStatement pStmt6 = null;
		try {
			con = DbConnector.getConnection();

			pStmt1 = con.prepareStatement(READSTUDYSQL);
			pStmt1.setString(1, stdyId1);
			pStmt2 = con.prepareStatement(READSERIESSQL);
			pStmt2.setString(1, srsId1);
			pStmt3 = con.prepareStatement(READIMAGESQL);
			pStmt3.setString(1, imgNumber1);
			pStmt4 = con.prepareStatement(READSTUDYSQL);
			pStmt4.setString(1, stdyId2);
			pStmt5 = con.prepareStatement(READSERIESSQL);
			pStmt5.setString(1, srsId2);
			pStmt6 = con.prepareStatement(READIMAGESQL);
			pStmt6.setString(1, imgNumber2);

			String patientStudyColumns[] = { "patientId", "patientName", "patientDOB", "accessionNumber", "studyId",
					"studyDescription", "studyDateTime" };
			String seriesColumns[] = { "seriesId", "studyId", "seriesNumber", "modality", "seriesDescription" };
			String imageColumns[] = { "imageNumber", "studyId", "seriesId", "imageType", "rows", "columns",
					"bitsAllocated", "bitsStored" };

			compTable.addColumn("Value");
			compTable.addColumn("Img1");
			compTable.addColumn("Img2");

			ResultSet rs1, rs2;
			rs1 = pStmt1.executeQuery();
			rs2 = pStmt4.executeQuery();

			while (rs1.next() && rs2.next()) {

				compTable.addRow(new Object[] { patientStudyColumns[0], rs1.getString(1), rs2.getString(1) });

				compTable.addRow(new Object[] { patientStudyColumns[1], rs1.getString(2), rs2.getString(2) });
				compTable.addRow(new Object[] { patientStudyColumns[2], rs1.getString(3), rs2.getString(3) });
				compTable.addRow(new Object[] { patientStudyColumns[3], rs1.getString(4), rs2.getString(4) });
				compTable.addRow(new Object[] { patientStudyColumns[4], rs1.getString(5), rs2.getString(5) });
				compTable.addRow(new Object[] { patientStudyColumns[5], rs1.getString(6), rs2.getString(6) });
				compTable.addRow(new Object[] { patientStudyColumns[6], rs1.getString(7), rs2.getString(7) });
			}

			rs1 = pStmt2.executeQuery();
			rs2 = pStmt5.executeQuery();

			while (rs1.next() && rs2.next()) {

				compTable.addRow(new String[] { seriesColumns[0], rs1.getString(1), rs2.getString(1) });
				// compTable.addRow(new String[] {patientStudyColumns[1], rs1.getString(2),
				// rs2.getString(2)});
				compTable.addRow(new String[] { seriesColumns[2], rs1.getString(3), rs2.getString(3) });
				compTable.addRow(new String[] { seriesColumns[3], rs1.getString(4), rs2.getString(4) });
				compTable.addRow(new String[] { seriesColumns[4], rs1.getString(5), rs2.getString(5) });

			}

			rs1 = pStmt3.executeQuery();
			rs2 = pStmt6.executeQuery();

			while (rs1.next() && rs2.next()) {

				compTable.addRow(new String[] { imageColumns[0], rs1.getString(1), rs2.getString(1) });

				compTable.addRow(new String[] { imageColumns[3], rs1.getString(4), rs2.getString(4) });
				compTable.addRow(new String[] { imageColumns[4], rs1.getString(5), rs2.getString(5) });
				compTable.addRow(new String[] { imageColumns[5], rs1.getString(6), rs2.getString(6) });
				compTable.addRow(new String[] { imageColumns[6], rs1.getString(7), rs2.getString(7) });
				compTable.addRow(new String[] { imageColumns[7], rs1.getString(8), rs2.getString(8) });
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return compTable;
	}

	public static void main(String args[]) {
		try {
			// create();
			// insert(new Image("i2", "s1", "p1", "3333", "5575", "hgh ghg h", "4235456435",
			// "yegf" ));
			//
			// ArrayList<Image> imageList = (ArrayList<Image>)viewImageBySeries("s1", "p1");
			// int size = imageList.size();
			// while(size>=0) {
			// Image img = imageList.get(--size);
			// System.out.println(img.getBitsStored());
			// }

			// ArrayList<Image> imgList = (ArrayList<Image>)filter("rows", "23","5585");
			// int size = imgList.size();
			// while(size>=0) {
			// Image img = imgList.get(--size);
			// System.out.println(img.getRows());
			// }

			// LinkedHashMap<String, String> imgDetails = details("std1", "srs1", "img1");
			// Set<String> keys = imgDetails.keySet();
			// Iterator<String> itr = keys.iterator();
			// while(itr.hasNext()) {
			// String key = (String)itr.next();
			//
			// System.out.println(key + " " + imgDetails.get(key) + "\n");
			// }

			DefaultTableModel compImg = compareImage("std1", "srs1", "img1", "std1", "srs1", "img2");
			System.out.println(compImg.getColumnCount());
			System.out.println(compImg.getRowCount());

			// compImg.getValueAt(1, 1);

			for (int i = 0; i < compImg.getRowCount(); ++i) {
				System.out.print(compImg.getValueAt(i, 0).toString());
				System.out.print("   " + compImg.getValueAt(i, 1).toString());
				System.out.print("   " + compImg.getValueAt(i, 2).toString() + "\n");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
