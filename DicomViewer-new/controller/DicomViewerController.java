package controller;


import java.io.File;
import java.util.ArrayList;

import org.postgresql.util.PSQLException;

import dao.ImageDAO;
import dao.PatientStudyDAO;
import dao.SeriesDAO;
import model.DCMObjFactory;
import model.DCMParser;
import model.Image;
import model.PatientStudy;
import model.Series;


public class DicomViewerController {
	
	static DCMParser dcmParser;
	private  PatientStudy pStudy;
	private  Series series;
	private  Image image;
	
	public DicomViewerController(PatientStudy pStudy, Series series, Image image) {		
		this.pStudy = pStudy;
		this.series = series;
		this.image = image;
	}

	public DicomViewerController(DCMParser dcmParser) {
		DicomViewerController.dcmParser = dcmParser;
	}	

	public void parseDCMFile(File f) throws Exception {
		dcmParser.dcmFileParser(f);		
	}

	public static  ArrayList<String> getPatientStudyTags() {
		ArrayList<String> patientStudy = dcmParser.getPatientStudyList();

		return patientStudy;
	}

	public static ArrayList<String> getSeriesTags() {
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
		} 
		catch (PSQLException e) 
		{
			throw e;
		}
		catch (Exception e) {
			throw  e;
		}
	}

	public  void insertSeries() throws PSQLException, Exception{
		try
		{
			
			SeriesDAO.insert(series);
		}
		catch (PSQLException e) 
		{
			throw e;
		}
		catch (Exception e) {
			throw  e;
		}
		
	}

	public void insertImage() throws PSQLException, Exception {
		try
		{			
			ImageDAO.insert(image);
		}
		catch (PSQLException e) 
		{
			throw e;
		}
		catch (Exception e) {
			throw  e;
		}
		
	}

	public DicomViewerController getDCMObj() {		
		return DCMObjFactory.createDCMObj();
	}



	
}
