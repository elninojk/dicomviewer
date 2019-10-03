package dcmparser;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.io.DicomInputStream;

import daoclasses.ImageDAO;
import daoclasses.PatientStudyDAO;
import daoclasses.SeriesDAO;
import ui.home;

public class DCMParser {
	
	static DicomInputStream din;
	// dicom file parser
	public static void dcmFileParser(File f)  throws Exception
	{		
		din = new DicomInputStream(f);				
		Attributes tagDataSet = din.readDataset(-1, -1);	
		String studyDateTime;
		studyDateTime = tagDataSet.getString((int) Tag.StudyDateAndTime);
		
		
		// creating new patient, study, image obj
		PatientStudy  ps = new PatientStudy(tagDataSet.getString(Tag.PatientID), tagDataSet.getString(Tag.PatientName), tagDataSet.getString(Tag.PatientBirthDate), tagDataSet.getString(Tag.AccessionNumber), tagDataSet.getString(Tag.StudyInstanceUID), tagDataSet.getString(Tag.StudyDescription), studyDateTime);
		Series s = new Series(tagDataSet.getString(Tag.SeriesInstanceUID), tagDataSet.getString(Tag.StudyInstanceUID), tagDataSet.getString(Tag.SeriesNumber), tagDataSet.getString(Tag.Modality), tagDataSet.getString(Tag.SeriesDescription));
		Image i = new Image(tagDataSet.getString(Tag.ReferenceImageNumber), tagDataSet.getString(Tag.StudyInstanceUID), tagDataSet.getString(Tag.SeriesInstanceUID), tagDataSet.getString(Tag.ImageType), tagDataSet.getString(Tag.Rows), tagDataSet.getString(Tag.Columns) , tagDataSet.getString(Tag.BitsAllocated), tagDataSet.getString(Tag.BitsStored));
		//	Adding to db
		//	DcmDAO.insertPatient(patient); 		
		
		PatientStudyDAO.insert(ps);
		SeriesDAO.insert(s);
		ImageDAO.insert(i);
		// 	Displaying info	
		System.out.println("patient \n " + ps);
		System.out.println("\nstudy \n " + s);
		System.out.println("\nimage \n " + i);

		
		
	}
	// dicom folder parser
	public static void dcmFolderParser(File[] f) throws Exception
	{
		for(File file : f)
		{
			din = new DicomInputStream(file);				
			Attributes tagDataSet = din.readDataset(-1, -1);	
			
			// adding patient, series, image info to the list
			PatientStudy  ps = new PatientStudy(tagDataSet.getString(Tag.PatientID), tagDataSet.getString(Tag.PatientName), tagDataSet.getString(Tag.PatientBirthDate), tagDataSet.getString(Tag.AccessionNumber), tagDataSet.getString(Tag.StudyInstanceUID), tagDataSet.getString(Tag.StudyDescription), tagDataSet.getString((int) Tag.StudyDateAndTime));
			Series s = new Series(tagDataSet.getString(Tag.SeriesInstanceUID), tagDataSet.getString(Tag.StudyInstanceUID), tagDataSet.getString(Tag.SeriesNumber), tagDataSet.getString(Tag.Modality), tagDataSet.getString(Tag.SeriesDescription));
			Image i = new Image(tagDataSet.getString(Tag.ReferenceImageNumber), tagDataSet.getString(Tag.StudyInstanceUID), tagDataSet.getString(Tag.SeriesInstanceUID), tagDataSet.getString(Tag.ImageType), tagDataSet.getString(Tag.Rows), tagDataSet.getString(Tag.Columns) , tagDataSet.getString(Tag.BitsAllocated), tagDataSet.getString(Tag.BitsStored));
			
			PatientStudyDAO.insert(ps);
			SeriesDAO.insert(s);
			ImageDAO.insert(i);
			
			// Displaying info
			System.out.println("patient \n " + ps);
			System.out.println("\nstudy \n " + s);		
			System.out.println("\nimage \n " + i);		
			
		}
	}	
	
	
	
}
