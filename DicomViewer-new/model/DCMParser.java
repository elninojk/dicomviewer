package model;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.io.DicomInputStream;

import controller.DicomViewerController;
import exceptions.DCMFileNotFoundExcepiton;


public class DCMParser {

	private static DicomInputStream din;
	private static Attributes tagDataSet;
	
	// dicom file parser
	public void dcmFileParser(File f) throws Exception {

		try {
			 Logger logger = DicomViewerController.getLogger();			
			 din = new DicomInputStream(f);
			 tagDataSet = din.readDataset(-1, -1);
			 System.out.println(tagDataSet.getString(Tag.PixelData));
			 logger.info("Parsing dcm file");
		} catch (Exception e) {
			throw e;
		}
	}

	// dicom folder parser
	public static void dcmFolderParser(File[] f) throws Exception {
		boolean flag = false;
		for (File file : f) {
			if (file.getName().endsWith(".dcm")) {
				try {
					 
					flag = true;
					// //dcmFileParser(file);
					// } catch (PSQLException e) {
					// throw e;
				} catch (NullPointerException e) {
					throw e;
				} catch (Exception e) {
					throw e;
				}

			} else if (flag == false) {
				throw new DCMFileNotFoundExcepiton("DCM file not found in the selected folder");
			}

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
		StringBuffer studyDate = splitDate(sdate);
		StringBuffer studyTime = splitTime(stime);
		patientStudyList.add(String.valueOf(studyDate.append(" " + studyTime)));		
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

	// public static void main(String args[]) throws Exception
	// {
	// dcmFileParser(new
	// File("C:\\Users\\1026837\\Documents\\dicom\\dicom\\93677708.dcm"));
	// System.out.println(getPatientStudyTags());
	// }

}
