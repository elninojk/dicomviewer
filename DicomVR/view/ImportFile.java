package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.dcm4che3.io.DicomStreamException;

import controller.DicomVRController;
import model.DCMObjFactory;
import model.DCMParser;

public class ImportFile extends Thread {
	
	private int patientImportStatus = 0 ,seriesImportStatus = 0 ,imageImportStatus = 0;
	static DicomVRController parserController;
	static JFileChooser jf;
	static File f;
	public ImportFile()	
	{
		
	
	jf = new JFileChooser();
	
	jf.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent ae) {
			
			
			 if (ae.getActionCommand().equals(JFileChooser.APPROVE_SELECTION))
			{
				patientImportStatus=1;
				seriesImportStatus=1;
				imageImportStatus=1;
				perform();
				System.out.println("Approve");
			}
			
		}
	});
			
	FileNameExtensionFilter dcmfilter = new FileNameExtensionFilter("Dicom file(.dcm)", "dcm","DCM");
	
	jf.setCurrentDirectory(new File("."));
	jf.setAcceptAllFileFilterUsed(false);
	jf.addChoosableFileFilter(dcmfilter);
	jf.setFileFilter(dcmfilter);
	jf.setFileSelectionMode(JFileChooser.FILES_ONLY);
	jf.showOpenDialog(null);
	
	
	
}
	public void run()
	{
		Viewer.dispPatient();
		Viewer.dispSeries();
		Viewer.dispImage();
	}
	public int getPatientImportStatus()
	{
		return patientImportStatus;
	}public int getSeriesImportStatus()
	{
		return seriesImportStatus;
	}
	public int getImageImportStatus()
	{
		return imageImportStatus;
	}
	public void perform()
	{
		try {
			parserController = new DicomVRController(new DCMParser());
			 f = jf.getSelectedFile();		
			
				parserController.parseDCMFile(f);
				DicomVRController dbController = DCMObjFactory.createDCMObj();
				try {
					dbController.insertPatientStudy();
					
				} catch (Exception e) {
				
					patientImportStatus = 0;

				}
				try {
					dbController.insertSeries();
				} catch (Exception e) {
					
					seriesImportStatus = 0;
				}
				try {
					dbController.insertImage();
				} catch (Exception e) {
					imageImportStatus = 0;

				}
				if(patientImportStatus ==0 && seriesImportStatus == 0 && imageImportStatus == 0)
				{
					javax.swing.JOptionPane.showMessageDialog(jf, f.getName() + " File Already Imported", "Dicom viewer", 2);
				}
			
		}
		catch(DicomStreamException e)
		{
			javax.swing.JOptionPane.showMessageDialog(jf, f.getName() + " corrupted dcm file", "Dicom viewer", 2);			
			patientImportStatus =0 ; 
			seriesImportStatus = 0 ;
			imageImportStatus = 0;
		}
		
		catch (Exception e) {
			e.printStackTrace();
			javax.swing.JOptionPane.showMessageDialog(jf, "File could not be read", "Dicom viewer", 2);	
			patientImportStatus =0 ; 
			seriesImportStatus = 0 ;
			imageImportStatus = 0;
		}
	}
	
}
