package dcmparser;

import java.io.File;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.io.DicomInputStream;
import org.postgresql.util.PSQLException;

import daoclasses.ImageDAO;
import daoclasses.PatientStudyDAO;
import daoclasses.SeriesDAO;
import ui.Home;



public class DCMParser {
	
	static DicomInputStream din;
	// dicom file parser
	public static void dcmFileParser(File f) throws Exception  
	{		
		
		try
		{   
			din = new DicomInputStream(f);			
			Attributes tagDataSet = din.readDataset(-1, -1);	
			String sdate = tagDataSet.getString(Tag.StudyDate);
			String stime = tagDataSet.getString(Tag.StudyTime);
			
			StringBuffer studyDate = splitDate(sdate);			
			StringBuffer studyTime = splitTime(stime);
			//System.out.println(tagDataSet.getString());
			// creating new patient, study, image obj
			PatientStudy  ps = new PatientStudy(tagDataSet.getString(Tag.PatientID), tagDataSet.getString(Tag.PatientName), tagDataSet.getString(Tag.PatientBirthDate), tagDataSet.getString(Tag.AccessionNumber), tagDataSet.getString(Tag.StudyInstanceUID), tagDataSet.getString(Tag.StudyDescription), String.valueOf(studyDate.append("  " + studyTime)));
			Series s = 	new Series(tagDataSet.getString(Tag.SeriesInstanceUID), tagDataSet.getString(Tag.StudyInstanceUID), tagDataSet.getString(Tag.SeriesNumber), tagDataSet.getString(Tag.Modality), tagDataSet.getString(Tag.SeriesDescription));
			Image i = new Image(tagDataSet.getString(Tag.ReferenceImageNumber), tagDataSet.getString(Tag.StudyInstanceUID), tagDataSet.getString(Tag.SeriesInstanceUID), tagDataSet.getString(Tag.ImageType), tagDataSet.getString(Tag.Rows), tagDataSet.getString(Tag.Columns) , tagDataSet.getString(Tag.BitsAllocated), tagDataSet.getString(Tag.BitsStored));
			//	Adding to db				
			
			PatientStudyDAO.insert(ps);
			SeriesDAO.insert(s);
			ImageDAO.insert(i);
			// 	Displaying info	
			Home.dispPatient();
		}
		catch (PSQLException  e) {
			throw e;
		}
		catch(NullPointerException e)
		{
			throw e;
		}
		catch (Exception e) {
			throw e;
		}

	}
	
	// dicom folder parser
	public static void dcmFolderParser(File[] f) throws Exception
	{
		boolean flag = false;
		for(File file : f)
		{
			if(file.getName().endsWith(".dcm"))
			{				
				try			
				{				
					flag = true;				
					dcmFileParser(file);
				}
				catch (PSQLException  e) {
					throw e;
				}
				catch(NullPointerException e)
				{
					throw e;
				}
				catch (Exception e) {
					throw e;
				}
				
			}
			else if(flag == false)
			{
				throw new DCMFileNotFoundExcepiton("DCM file not found in the selected folder");
			}						
			
		}
	
	}	
	
	private static StringBuffer splitDate(String sd) {
		StringBuffer studyDate = new StringBuffer();
		for(int i=0; i < sd.length(); i++)
		{
			
			if(i == 4 || i == 6 || i == 8) 
			{
				studyDate.append("/");
			}
			else
			{
				studyDate.append(sd.charAt(i));
			}
		}
		return studyDate;
		
	}
	
	private static StringBuffer splitTime(String st) {
		StringBuffer studyDate = new StringBuffer();
		for(int i=0; i < st.length(); i++)
		{
			
			if(i == 2 || i == 5) 
			{
				studyDate.append(":");
			}
			else if ( i == 8)
			{
				break;
			}
			else
			{
				studyDate.append(st.charAt(i));
			}
			
		}	
		return studyDate;		
	}
	
	
	
}
