package dcmparser;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.io.DicomInputStream;

import com.sun.xml.bind.v2.schemagen.xmlschema.List;

public class DCMParser {
	 static ArrayList<String> patient;
	// dicom file parser
	public static void dcmFileParser(File f)  throws IOException, ClassNotFoundException, SQLException
	{
		
		DicomInputStream din = new DicomInputStream(f);				
		Attributes tagDataSet = din.readDataset(-1, -1);	
		
		// adding patient, series, image info to the list
		patient = patientStudyDetails(tagDataSet);
		patient = seriesDetails(tagDataSet);
		patient = imageDetails(tagDataSet);
		
		//Adding to db
		//DcmDAO.insertPatient(patient); 
		// Displaying info
		System.out.println("patient \n " + patientStudyDetails(tagDataSet));
		System.out.println("\nstudy \n " +seriesDetails(tagDataSet));
		System.out.println("\nimage \n " +imageDetails(tagDataSet));

		
		
	}
	// dicom folder parser
	public static void dcmFolderParser(File[] f) throws IOException, ClassNotFoundException, SQLException
	{
		for(File file : f)
		{
			DicomInputStream din = new DicomInputStream(file);				
			Attributes tagDataSet = din.readDataset(-1, -1);	
			
			// adding patient, series, image info to the list
			patient = patientStudyDetails(tagDataSet);
			patient = seriesDetails(tagDataSet);
			patient = imageDetails(tagDataSet);
			
			// Displaying info
			System.out.println("patient \n " + patientStudyDetails(tagDataSet));
			System.out.println("\nstudy \n " +seriesDetails(tagDataSet));		
			System.out.println("\nimage \n " +imageDetails(tagDataSet));		
			System.out.println(tagDataSet.getString(Tag.SeriesNumber));
		}
	}
	
	public static ArrayList<String> patientStudyDetails(Attributes patientAttrb)
	{
		ArrayList<String> patientDetailsList = new ArrayList<>();
		patientDetailsList.add(patientAttrb.getString(Tag.PatientName));
		patientDetailsList.add(patientAttrb.getString(Tag.PatientID));
		patientDetailsList.add(patientAttrb.getString(Tag.PatientBirthDate));
		patientDetailsList.add(patientAttrb.getString(Tag.PatientBirthDate));
		patientDetailsList.add(patientAttrb.getString(Tag.AccessionNumber));
		patientDetailsList.add(patientAttrb.getString(Tag.StudyInstanceUID));
		patientDetailsList.add(patientAttrb.getString(Tag.StudyDescription));
		patientDetailsList.add(patientAttrb.getString(Tag.StudyDate));
		patientDetailsList.add(patientAttrb.getString(Tag.StudyTime));
		
		return patientDetailsList;
	}
	
	public static ArrayList<String> seriesDetails(Attributes seriesAttrb)
	{
		ArrayList<String> seriesDetailsList = new ArrayList<>();
		seriesDetailsList.add(seriesAttrb.getString(Tag.ClinicalTrialSeriesID));
		seriesDetailsList.add(seriesAttrb.getString(Tag.SeriesNumber));
		seriesDetailsList.add(seriesAttrb.getString(Tag.Modality));
		seriesDetailsList.add(seriesAttrb.getString(Tag.SeriesDescription));
		
		return seriesDetailsList;
	}
	
	public static ArrayList<String> imageDetails(Attributes imageAttrb)
	{
		ArrayList<String> imageDetailsList = new ArrayList<>();
		
		imageDetailsList.add(imageAttrb.getString(Tag.ReferenceImageNumber));
		imageDetailsList.add(imageAttrb.getString(Tag.ImageType));
		imageDetailsList.add(imageAttrb.getString(Tag.Rows));
		imageDetailsList.add(imageAttrb.getString(Tag.Columns));
		imageDetailsList.add(imageAttrb.getString(Tag.BitsAllocated));
		imageDetailsList.add(imageAttrb.getString(Tag.BitsAllocated));
		
		
		return imageDetailsList;
	}

	
}
