package dicomviewer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PatientStudyDAO {

	private static String CREATESQL = "create table PatientStudy (patientId CHARACTER VARYING(150) NOT NULL, patientName CHARACTER VARYING(150) , "
			+ "patientDOB CHARACTER VARYING(150) , accessionNumber CHARACTER VARYING(150) ,"
			+ "studyId CHARACTER VARYING(150) PRIMARY KEY NOT NULL, studyDescription CHARACTER VARYING(250) , studyDateTime CHARACTER VARYING(150) )";
	private static String INSERTSQL = "insert into PatientStudy values(?, ? , ?, ?, ?, ?, ?)";
	private static String SORTSQL = "select * from patientStudy ORDER BY ?";
	private static String FILTERSQL = "select * from patientStudy WHERE cast(patientdob as int) BETWEEN ? AND ?";
	private static String READALLSQL = "select * from PatientStudy";

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
	public static void insert(PatientStudy pStudy) throws Exception {

		Connection con = null;
		PreparedStatement pStmt = null;
		try {
			con = DbConnector.getConnection();

			pStmt = con.prepareStatement(INSERTSQL);
			pStmt.setString(1, pStudy.getPatientId());
			pStmt.setString(2, pStudy.getPatientName());
			pStmt.setString(3, pStudy.getPatientDOB()); /// POSSIBLE EXCEPTION//UTIL DATE
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
//	OK
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

			//pStmt.setString(1, colName);
//			pStmt.setInt(2, Integer.parseInt(lowValue));
//			pStmt.setInt(3, Integer.parseInt(highValue));
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

	public static void main(String args[]) {
		try {
//			create();
//			insert(new PatientStudy("p2", "wan", "23344555", "3333", "5575", "hgh ghg h", "4235456435" ));

			ArrayList<PatientStudy> stdList = (ArrayList<PatientStudy>)filter("accessionnumber", "23","5555");
			int size = stdList.size();
			while(size>=0) {
				PatientStudy pst = stdList.get(--size);
				System.out.println(pst.getPatientDOB());
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
