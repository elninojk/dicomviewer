package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import controller.DicomViewerController;

public class DCMObjFactory {
	static Logger logger ;
		public static DicomViewerController createDCMObj() throws SecurityException, IOException {
		logger = DicomViewerController.getLogger();
		ArrayList<String> patientlist = DicomViewerController.getPatientStudyTags();
		PatientStudy patientStudy = new PatientStudy(patientlist.get(0), patientlist.get(1), patientlist.get(2),
				patientlist.get(3), patientlist.get(4), patientlist.get(5), patientlist.get(6));
		logger.info("Patient : logged successfully");
		
		ArrayList<String> seriesList = DicomViewerController.getSeriesTags();
		Series series = new Series(seriesList.get(0), seriesList.get(1), seriesList.get(2), seriesList.get(3),
				seriesList.get(4));
		logger.info("Series : logged successfully");

		ArrayList<String> imageList = DicomViewerController.getImageTags();
		Image image = new Image(imageList.get(0), imageList.get(1), imageList.get(2), imageList.get(3),
				imageList.get(4), imageList.get(5), imageList.get(6), imageList.get(7));
		logger.info("Image : logged successfully");
		logger.info("DicomViewerController : logged successfully");
		return new DicomViewerController(patientStudy, series, image);
	}

}
