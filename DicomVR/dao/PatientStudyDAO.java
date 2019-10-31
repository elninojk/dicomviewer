package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;

import org.postgresql.util.PSQLException;

import model.PatientStudy;


public class PatientStudyDAO {
	
	private static String INSERTSQL = "insert into PatientStudy values(?, ? , ?, ?, ?, ?, ?)";
	private static String READALLSQL = "select * from PatientStudy";
		static DbConnector connector = DbConnector.getInstance();


	public static void insert(PatientStudy pStudy) throws Exception {

		Connection con = null;
		PreparedStatement pStmt = null;
		try {
			con = connector.getConnection();

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
			con = connector.getConnection();

			pStmt = con.prepareStatement(READALLSQL);

			ResultSet rs = pStmt.executeQuery();
			ArrayList<PatientStudy> pStudyList = new ArrayList<>();
			while (rs.next()) {
				PatientStudy pStudy = new PatientStudy(rs.getString(1), rs.getString(2), rs.getString(3),
						rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));
				pStudyList.add(pStudy);
			}			
			return pStudyList;
		} catch (PSQLException ex) {
			throw ex;

		} finally {
			pStmt.close();
			con.close();
		}
	}
	
	
}