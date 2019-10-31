package controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.dcm4che3.data.Attributes;
import org.postgresql.util.PSQLException;

import dao.ImageDAO;
import dao.PatientStudyDAO;
import dao.SeriesDAO;
import model.DCMParser;
import model.Image;
import model.PatientStudy;
import model.Series;

public class DicomViewerController {

	static DCMParser dcmParser;
	private PatientStudy pStudy;
	private Series series;
	private Image image;
	
	private static Logger logger = Logger.getLogger(DicomViewerController.class.getName());
	
	public static Logger getLogger() throws SecurityException, IOException
	{
		//logger.addHandler(LoggerFormat.getFileHandler());
		logger.setLevel(Level.FINE);
		return logger;
	}
	public DicomViewerController(PatientStudy pStudy, Series series, Image image) {
		this.pStudy = pStudy;
		this.series = series;
		this.image = image;
	}

	public DicomViewerController(DCMParser dcmParser) throws SecurityException, IOException  {		
		DicomViewerController.dcmParser = dcmParser;
		//logger.addHandler(LoggerFormat.getFileHandler());
		logger.setLevel(Level.FINE);
		logger.info("Controller initiated...!");		
	}

	public static void parseDCMFile(File f) throws Exception {
		dcmParser.dcmFileParser(f);		
	}

	public static ArrayList<String> getPatientStudyTags()  {
		ArrayList<String> patientStudy = dcmParser.getPatientStudyList();
		return patientStudy;		
	}

	public static ArrayList<String> getSeriesTags()  {
		ArrayList<String> series = dcmParser.getSeriesList();

		return series;
	}

	public static ArrayList<String> getImageTags() {
		ArrayList<String> image = dcmParser.getImageList();

		return image;
	}

	public void insertPatientStudy() throws PSQLException, Exception {
		try {
			PatientStudyDAO.insert(pStudy);
			logger.info("Patient study : inserted successfully");
		} catch (PSQLException e) {
			throw e;
		}
	}

	public void insertSeries() throws PSQLException, Exception {
		try {

			SeriesDAO.insert(series);
			logger.info("Series : inserted successfully");
		} catch (PSQLException e) {
			throw e;
		} 

	}

	public void insertImage() throws PSQLException, Exception {
		try {
			ImageDAO.insert(image);
			logger.info("Image : inserted successfully");
		} catch (PSQLException e) {
			throw e;
		} 

	}	

	public static List<Series> viewSeries(String studyId) throws Exception {
		List<Series> list = SeriesDAO.viewSeriesByStudy(studyId);
		logger.info("Series : retrieval");
		return list;
	}
	public static List<PatientStudy> viewAllPatientStudy() throws Exception {
		List<PatientStudy> list=PatientStudyDAO.viewAllPatientStudy();
		logger.info("Patient study : retrieval");
		return list;
	}
	public static List<Image> viewImage(String studyId, String seriesId) throws Exception {
		List<Image> list = ImageDAO.viewImageBySeries(studyId, seriesId);
		logger.info("Image : retrieval");
		return list;
	}
	public static Attributes getTagSet() {
		Attributes tags = DCMParser.getTags();
		return tags;
	}
	
	

}
