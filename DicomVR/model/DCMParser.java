package model;


import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.io.DicomInputStream;
import controller.DicomViewerController;

public class DCMParser {

	private static DicomInputStream din;
	private static Attributes tagDataSet;
	
	// dicom file parser
	public void dcmFileParser(File f) throws Exception {
		try {
			 Logger logger = DicomViewerController.getLogger();			
			 din = new DicomInputStream(f);
			 tagDataSet = din.readDataset(-1, -1);				
			 logger.info("Parsing dcm file");
			 //new Dcm2Jpg().convert(new File("D:\\Dicom Viewer-Project\\DicomViewer\\src\\dcmfiles\\dicom\\178537360.dcm"), new File("x.jpg"));
			 }		    		
		catch(Exception e)
		{
			throw e;			
		}
	}
	
	private static StringBuffer splitDate(String sd) {
		StringBuffer studyDate = new StringBuffer();
		for (int i = 0; i < sd.length(); i++) {

			if (i == 4 || i == 6 || i == 8) {
				studyDate.append("/");
			} else {
				studyDate.append(sd.charAt(i));
			}
		}
		return studyDate;

	}

	public static StringBuffer splitTime(String st) {
		
		StringBuffer studyDate = new StringBuffer();
		for (int i = 0; i < st.length(); i++) {

			if (i == 2 || i == 5) {
				studyDate.append(":");
			} else if (i == 8) {
				break;
			} else {
				studyDate.append(st.charAt(i));
			}

		}
		return studyDate;
	}

	public ArrayList<String> getPatientStudyList() {
		ArrayList<String> patientStudyList = new ArrayList<>();
		patientStudyList.add(tagDataSet.getString(Tag.PatientID));
		patientStudyList.add(tagDataSet.getString(Tag.PatientName));
		patientStudyList.add(tagDataSet.getString(Tag.PatientBirthDate));
		patientStudyList.add(tagDataSet.getString(Tag.AccessionNumber));
		patientStudyList.add(tagDataSet.getString(Tag.StudyInstanceUID));
		patientStudyList.add(tagDataSet.getString(Tag.StudyDescription));
		String sdate = tagDataSet.getString(Tag.StudyDate);
		String stime = tagDataSet.getString(Tag.StudyTime);
		if(sdate != null && stime != null)
		{
				StringBuffer studyDate = splitDate(sdate);
				StringBuffer studyTime = splitTime(stime);	
				patientStudyList.add(String.valueOf(studyDate.append(" " + studyTime)));
		}			
		return patientStudyList;
	}

	public ArrayList<String> getSeriesList()  {
		ArrayList<String> seriesList = new ArrayList<>();
		seriesList.add(tagDataSet.getString(Tag.SeriesInstanceUID));
		seriesList.add(tagDataSet.getString(Tag.StudyInstanceUID));
		seriesList.add(tagDataSet.getString(Tag.SeriesNumber));
		seriesList.add(tagDataSet.getString(Tag.Modality));
		seriesList.add(tagDataSet.getString(Tag.SeriesDescription));
		
		
		return seriesList;
	}

	public ArrayList<String> getImageList()  {
		ArrayList<String> imageList = new ArrayList<>();
		imageList.add(tagDataSet.getString(Tag.SOPInstanceUID));
		imageList.add(tagDataSet.getString(Tag.StudyInstanceUID));
		imageList.add(tagDataSet.getString(Tag.SeriesInstanceUID));
		imageList.add(tagDataSet.getString(Tag.ImageType));
		imageList.add(tagDataSet.getString(Tag.Rows));
		imageList.add(tagDataSet.getString(Tag.Columns));
		imageList.add(tagDataSet.getString(Tag.BitsAllocated));
		imageList.add(tagDataSet.getString(Tag.BitsStored));
		
		return imageList;
	}

	public static Attributes getTags() {		
		return tagDataSet;
	}	

}
