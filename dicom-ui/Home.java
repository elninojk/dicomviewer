package ui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.postgresql.util.PSQLException;

import daoclasses.ImageDAO;
import daoclasses.PatientStudyDAO;
import daoclasses.SeriesDAO;
import dcmparser.DCMFileNotFoundExcepiton;
import dcmparser.DCMParser;
import dcmparser.Image;
import dcmparser.PatientStudy;
import dcmparser.Series;


 
public class Home implements ActionListener, Runnable {    
	private static JPanel contentPane;
	private static JTable tSeries;
	private static JTable tImage;
	private static JTable tPatient;
    static JProgressBar jpb;
    static Home _this;
    private static JTextField tPatientId;
    static DefaultTableModel dtmPatient;
    private static JTextField tPatientName;
    private static JTextField tDOB;
    private static JTextField tAccNumber;
    private static JTextField tStudyId;
    private static JTextField tStudyDecs;
    private static JTextField tStudyDateTime;
    private enum Actions {
        IMPORTFILE,
        IMPORTFOLDER
      }
     
    public Home (){
       _this = this;
    }
     
    private static void createAndShowGUI(){
    	JFrame frame = new JFrame ();
		frame.setVisible(true);
    	frame.setTitle("Dicom Viewer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	//	frame.setBounds(100, 100, 1058, 667);
		Toolkit t = Toolkit.getDefaultToolkit();
		Dimension d = t.getScreenSize();
		int h = d.height ;
		int w = d.width;
		frame.setBounds(0, 0, w, h);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
	//	contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBorder(new EmptyBorder(5, 5, w, h));
		frame.setContentPane(contentPane);
		contentPane.setLayout(null);

		// IMPORT FILE

		JButton btnImportFile = new JButton("Import File");
		btnImportFile.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		btnImportFile.setActionCommand(Actions.IMPORTFILE.name());
		btnImportFile.addActionListener(_this);	
		btnImportFile.setBounds(0, 11, 108, 23);
		contentPane.add(btnImportFile);

		// IMPORT FOLDER

		JButton btnImportFolder = new JButton("Import Folder");
		btnImportFolder.setActionCommand(Actions.IMPORTFOLDER.name());
		btnImportFolder.addActionListener(_this);
		btnImportFolder.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		btnImportFolder.setBounds(118, 11, 115, 23);
		contentPane.add(btnImportFolder);

		// COMPARE

		JButton btnCompareFiles = new JButton("Compare Files");
		btnCompareFiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			new CompareFiles().setVisible(true);
			}
		});
		btnCompareFiles.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		btnCompareFiles.setBounds(254, 11, 126, 23);
		contentPane.add(btnCompareFiles);

		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
			}
		});
		btnExit.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		btnExit.setBounds(1251, 11, 89, 23);
		contentPane.add(btnExit);

		// TABLE PATIENT STUDY DETAILS

		tPatient = new JTable();
		tPatient.setAutoCreateRowSorter(true);
		tPatient.setUpdateSelectionOnSort(true);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 101, 697, 526);
		contentPane.add(scrollPane);
		dispPatient();
		
		scrollPane.setViewportView(tPatient);
		tPatient.setBorder(new LineBorder(new Color(0, 0, 0)));

		// TABLE SERIES

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(717, 101, 623, 204);
		contentPane.add(scrollPane_1);
		DefaultTableModel dtmSeries = new DefaultTableModel(new String[] { "Series Id", "Series Number", "Modality", "Series Description" }, 0);
		dtmSeries.setRowCount(0);
		tSeries = new JTable();
		tSeries.setRowSelectionAllowed(true);
		


		tSeries.setAutoCreateRowSorter(true);
		tSeries.setUpdateSelectionOnSort(true);
		scrollPane_1.setViewportView(tSeries);
		tSeries.setBorder(new LineBorder(new Color(0, 0, 0)));

		// TABLE IMAGE

		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(717, 406, 623, 221);
		contentPane.add(scrollPane_2);
		DefaultTableModel dtmImage = new DefaultTableModel(new String[] { "Image Number", "Image Type", "Rows", "Columns", "Bits Allocated", "Bits Stored" }, 0);
		dtmImage.setRowCount(0);
		tImage = new JTable();

		tImage.setRowSelectionAllowed(true);
		
		tImage.setAutoCreateRowSorter(true);
		tImage.setUpdateSelectionOnSort(true);
		scrollPane_2.setViewportView(tImage);
		tImage.setBorder(new LineBorder(new Color(0, 0, 0)));
		tImage.setVisible(true);
		tImage.setToolTipText("Image-details");
		
  
		
		//VIEW DETAILS
		
		
		JButton btnDetails = new JButton("Details");
		btnDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					String studyId = tPatient.getModel().getValueAt(tPatient.getRowSorter().convertRowIndexToModel(tPatient.getSelectedRow()), 4).toString();
					String seriesId = tSeries.getModel().getValueAt(tSeries.getRowSorter().convertRowIndexToModel(tSeries.getSelectedRow()), 0).toString();
					String imageNumber = tImage.getModel().getValueAt(tImage.getRowSorter().convertRowIndexToModel(tImage.getSelectedRow()), 0).toString();
					new ImageDetails(studyId,seriesId,imageNumber).setVisible(true);
				}
				catch(Exception ex)
				{
					javax.swing.JOptionPane.showMessageDialog(frame, "Please select the image details" , "Dicom viewer", 2);
				}
			}
		});
		btnDetails.setBounds(1251, 348, 89, 23);
		contentPane.add(btnDetails);

		JLabel lblNewLabel = new JLabel("Patient - Study Details");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel.setOpaque(true);
		lblNewLabel.setBackground(Color.LIGHT_GRAY);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 55, 697, 20);
		contentPane.add(lblNewLabel);

		JLabel lblImageDetails = new JLabel("Image  Details");
		lblImageDetails.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblImageDetails.setOpaque(true);
		lblImageDetails.setHorizontalAlignment(SwingConstants.CENTER);
		lblImageDetails.setBackground(Color.LIGHT_GRAY);
		lblImageDetails.setBounds(717, 382, 623, 20);
		contentPane.add(lblImageDetails);

		JLabel lblSeriesDetails = new JLabel("Series Details");
		lblSeriesDetails.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSeriesDetails.setOpaque(true);
		lblSeriesDetails.setHorizontalAlignment(SwingConstants.CENTER);
		lblSeriesDetails.setBackground(Color.LIGHT_GRAY);
		lblSeriesDetails.setBounds(717, 55, 623, 20);
		contentPane.add(lblSeriesDetails);
		
	
		jpb=new JProgressBar();
		jpb.setBounds(58, 656, 1282, 14);
		jpb.setMinimum(0);
		jpb.setMaximum(100);
		jpb.setStringPainted(true);
		contentPane.add(jpb);
		
		tPatientId = new JTextField();
		tPatientId.setBounds(10, 80, 98, 20);
		contentPane.add(tPatientId);
		tPatientId.setColumns(10);
		
		tPatientName = new JTextField();
		tPatientName.setBounds(112, 80, 89, 20);
		contentPane.add(tPatientName);
		tPatientName.setColumns(10);
		
		tDOB = new JTextField();
		tDOB.setBounds(207, 80, 98, 20);
		contentPane.add(tDOB);
		tDOB.setColumns(10);
		
		tAccNumber = new JTextField();
		tAccNumber.setBounds(308, 80, 98, 20);
		contentPane.add(tAccNumber);
		tAccNumber.setColumns(10);
		
		tStudyId = new JTextField();
		tStudyId.setBounds(408, 80, 98, 20);
		contentPane.add(tStudyId);
		tStudyId.setColumns(10);
		
		tStudyDecs = new JTextField();
		tStudyDecs.setBounds(509, 80, 98, 20);
		contentPane.add(tStudyDecs);
		tStudyDecs.setColumns(10);
		
		tStudyDateTime = new JTextField();
		tStudyDateTime.setBounds(609, 80, 98, 20);
		contentPane.add(tStudyDateTime);
		tStudyDateTime.setColumns(10);
		jpb.setVisible(true);
		
		tPatientId.addActionListener(new java.awt.event.ActionListener() {
		    public void actionPerformed(java.awt.event.ActionEvent e) {		 
		    TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(dtmPatient); 
		    sorter.setRowFilter(RowFilter.regexFilter(tPatientId.getText(),0));
		    
		    
		    tPatientName.setText("");
		    tDOB.setText("");
		    tAccNumber.setText("");
		    tStudyId.setText("");
		    tStudyDecs.setText("");
		    tStudyDateTime.setText("");
		   
		    try
		    {
		    	
		    	tPatient.setRowSorter(sorter);
		    	tPatient.setRowSelectionInterval(0, 0);
		    	dispSeries();
		    	dispImage();
		    	
		    }
		    catch(Exception ex)
		    {
		    	javax.swing.JOptionPane.showMessageDialog(frame, "' " +  tPatientId.getText() +  "'  Id not found "  , "Dicom Viewer", 0);
		    }
		    	
			}
			
			
		});
		
		tPatientName.addActionListener(new java.awt.event.ActionListener() {
		    public void actionPerformed(java.awt.event.ActionEvent e) {
		   
		    TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(dtmPatient); 
		    sorter.setRowFilter(RowFilter.regexFilter(tPatientName.getText(),1));

		    		    
		    tPatientId.setText("");
		    tDOB.setText("");
		    tAccNumber.setText("");
		    tStudyId.setText("");
		    tStudyDecs.setText("");
		    tStudyDateTime.setText("");
		    try
		    {
		    	tPatient.setRowSorter(sorter);
		    	tPatient.setRowSelectionInterval(0, 0);
		    	dispSeries();
		    	dispImage();
		    }
		    catch(Exception ex)
		    {
		    	javax.swing.JOptionPane.showMessageDialog(frame, "' " +  tPatientName.getText() +  "' : name not found "  , "Dicom Viewer", 0);
		    }
		    	
			}
			
			
		});
		
		tDOB.addActionListener(new java.awt.event.ActionListener() {
		    public void actionPerformed(java.awt.event.ActionEvent e) {
		    	
		    TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(dtmPatient); 
		    sorter.setRowFilter(RowFilter.regexFilter(tDOB.getText(),2));

		   	  
		    tPatientName.setText("");
		    tPatientId.setText("");
		    tAccNumber.setText("");
		    tStudyId.setText("");
		    tStudyDecs.setText("");
		    tStudyDateTime.setText("");
		    try
		    {
		    	tPatient.setRowSorter(sorter);	
		    	tPatient.setRowSelectionInterval(0, 0);
		    	dispSeries();
		    	dispImage();
		    }
		    catch(Exception ex)
		    {
		    	javax.swing.JOptionPane.showMessageDialog(frame, "' " +  tDOB.getText() +  "' : DOB not found "  , "Dicom Viewer", 0);
		    }
		    	
			}
			
			
		});
		
		tAccNumber.addActionListener(new java.awt.event.ActionListener() {
		    public void actionPerformed(java.awt.event.ActionEvent e) {
		    	
		    TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(dtmPatient); 
		    sorter.setRowFilter(RowFilter.regexFilter(tAccNumber.getText(),3));

		  
		    tPatientName.setText("");
		    tPatientId.setText("");
		    tDOB.setText("");
		    tStudyId.setText("");
		    tStudyDecs.setText("");
		    tStudyDateTime.setText("");
		    try
		    {
		    	tPatient.setRowSorter(sorter);
		    	tPatient.setRowSelectionInterval(0, 0);
		    	dispSeries();
		    	dispImage();
		    }
		    catch(Exception ex)
		    {
		    	javax.swing.JOptionPane.showMessageDialog(frame, "' " +  tAccNumber.getText() +  "' : Accession number not found "  , "Dicom Viewer", 0);
		    }
		    	
			}
			
			
		});
		
		tStudyId.addActionListener(new java.awt.event.ActionListener() {
		    public void actionPerformed(java.awt.event.ActionEvent e) {
		    	
		    TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(dtmPatient); 
		    sorter.setRowFilter(RowFilter.regexFilter(tStudyId.getText(),4));

		
		    tPatientName.setText("");
		    tPatientId.setText("");
		    tAccNumber.setText("");
		    tDOB.setText("");
		    tStudyDecs.setText("");
		    tStudyDateTime.setText("");
		    try
		    {
		        tPatient.setRowSorter(sorter);
		        tPatient.setRowSelectionInterval(0, 0);
		    	dispSeries();
		    	dispImage();
		    }
		    catch(Exception ex)
		    {
		    	javax.swing.JOptionPane.showMessageDialog(frame, "' " +  tStudyId.getText() +  "' : Study id not found "  , "Dicom Viewer", 0);
		    }
		    	
			}
			
			
		});
		
		tStudyDateTime.addActionListener(new java.awt.event.ActionListener() {
		    public void actionPerformed(java.awt.event.ActionEvent e) {
		    	
		    TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(dtmPatient); 
		    sorter.setRowFilter(RowFilter.regexFilter(tStudyDateTime.getText(),6));

		  
		    tPatientName.setText("");
		    tPatientId.setText("");
		    tAccNumber.setText("");
		    tStudyId.setText("");
		    tStudyDecs.setText("");
		    tDOB.setText("");
		    try
		    {
		    	tPatient.setRowSorter(sorter);
		    	tPatient.setRowSelectionInterval(0, 0);
		    	dispSeries();
		    	dispImage();
		    }
		    catch(Exception ex)
		    {
		    	javax.swing.JOptionPane.showMessageDialog(frame, "' " +  tStudyDateTime.getText() +  "'  : StudyDate and time not found "  , "Dicom Viewer", 0);
		    }
		    	
			}
			
			
		});
		
		tStudyDecs.addActionListener(new java.awt.event.ActionListener() {
		    public void actionPerformed(java.awt.event.ActionEvent e) {
		    	
		    TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(dtmPatient); 
		    sorter.setRowFilter(RowFilter.regexFilter(tStudyDecs.getText(),5));

		  
		    tPatientName.setText("");
		    tPatientId.setText("");
		    tAccNumber.setText("");
		    tStudyId.setText("");
		    tDOB.setText("");
		    tStudyDateTime.setText("");
		    	System.out.print(tPatientId.getText());
		    	try
			    {
		    		  tPatient.setRowSorter(sorter);
		    		  tPatient.setRowSelectionInterval(0, 0);
		    		  dispSeries();
		    		  dispImage();
			    }
			    catch(Exception ex)
			    {
			    	javax.swing.JOptionPane.showMessageDialog(frame, "' " +  tStudyDecs.getText() +  "' : Study description not found "  , "Dicom Viewer", 0);
			    }
			}
			
			
		});
		tPatient.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) { 
				
				dispSeries();}

		});
		tSeries.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
				
			dispImage();
			}
			
		});
			
	}
    public static void dispSeries() {
		try {

			
					tImage.setVisible(false);
					DefaultTableModel dtmSeries = new DefaultTableModel(new String[] { "Series Id", "Series Number", "Modality", "Series Description" }, 0);
					String studyId = null;
					dtmSeries.setRowCount(0);
					studyId =String.valueOf( tPatient.getModel().getValueAt(tPatient.getRowSorter().convertRowIndexToModel(tPatient.getSelectedRow()), 4));
					
					List<Series> list;

					try {

						list = SeriesDAO.viewSeriesByStudy(studyId);
						for (int i = 0; i < list.size(); i++) {
							Object rowSeries[] = { list.get(i).getSeriesId(), list.get(i).getSeriesNumber(),
									list.get(i).getModality(), list.get(i).getSeriesDescription() };
							dtmSeries.addRow(rowSeries);
							tSeries.setModel(dtmSeries);
							dispImage();
							

						}
						tSeries.setVisible(true);
					} catch (Exception e) {
											}
					
				

		} catch (Exception ex) {
			
		}
	}
    
   

	public static void dispImage() {
		try {
			
					String studyId = tPatient.getModel().getValueAt(tPatient.getRowSorter().convertRowIndexToModel(tPatient.getSelectedRow()), 4).toString();
					DefaultTableModel dtmImage = new DefaultTableModel(
							new String[] { "Image Number", "Image Type", "Rows", "Columns", "Bits Allocated", "Bits Stored" },
							0);
					dtmImage.setRowCount(0);
					String seriesId = null;
					
					seriesId = tSeries.getModel().getValueAt(tSeries.getRowSorter().convertRowIndexToModel(tSeries.getSelectedRow()), 0).toString();

					try {
						
						List<Image> list = ImageDAO.viewImageBySeries(studyId, seriesId);
						for (int i = 0; i < list.size(); i++) {
							Object rowImage[] = { list.get(i).getImageNumber(), list.get(i).getImageType(),
									list.get(i).getRows(), list.get(i).getColumns(), list.get(i).getBitsAllocated(),
									list.get(i).getBitsStored() };
							dtmImage.addRow(rowImage);
							tImage.setModel(dtmImage);

						}
						tImage.setVisible(true);
						
					} catch (Exception e) {
						
					}
					
				} catch (Exception e) {
			
		}
	 
    }
	
	 public static void dispPatient()
	    {
	    	dtmPatient = new DefaultTableModel(new String[] { "Patient Id", "Patient Name", "DOB","Accession Number", "Study Id", "Study Description", "Study Date" }, 0);
			dtmPatient.setRowCount(0);

			tPatient.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tPatient.setModel(dtmPatient);
			try {
				List<PatientStudy> list = PatientStudyDAO.viewAllPatientStudy();
				for (int i = 0; i < list.size(); i++) {
					Object row[] = { list.get(i).getPatientId(), list.get(i).getPatientName(), list.get(i).getPatientDOB(),
							list.get(i).getAccessionNumber(), list.get(i).getStudyId(), list.get(i).getStudyDescription(),
							list.get(i).getStudyDateTime() };
					dtmPatient.addRow(row);
					tPatient.setModel(dtmPatient);
//					tPatient.setRowSelectionInterval(0, 0);
//					dispSeries();
//					dispImage();

				}
				tPatient.setRowSelectionInterval(0, 0);
				dispSeries();
				dispImage();
			} catch (Exception ex) {
				javax.swing.JOptionPane.showMessageDialog(new JFrame(), "dicom files not found" , "Dicom viewer", 2);
			}
	    }
     
    public static void main(String args[]) throws InterruptedException{
     
    	Home t = new Home();  
   	   new Thread(t).start();
   	  
   	javax.swing.SwingUtilities.invokeLater(new Runnable() {
        public void run() {
            createAndShowGUI();
        }
    });
       
    }
     
    public void actionPerformed(ActionEvent ae)
    {            
   
        if(ae.getActionCommand()==Actions.IMPORTFILE.name())
        {
        	JFileChooser  jf = new JFileChooser();
			 FileNameExtensionFilter dcmfilter = new FileNameExtensionFilter("Dicom file(.dcm)","dcm");
			
			jf.setCurrentDirectory(new File("."));
			jf.setAcceptAllFileFilterUsed(false);
			jf.addChoosableFileFilter(dcmfilter);
			jf.setFileFilter(dcmfilter);
			jf.setFileSelectionMode(JFileChooser.FILES_ONLY);
			jf.showOpenDialog(null);
		
			
			File f = jf.getSelectedFile();
			try 
			{
				
				Long l=new Long(f.length());
				jpb.setMaximum(l.intValue());
			    approveSelection(jf);
			    DCMParser.dcmFileParser(f);
			} 
			catch (PSQLException e) 
			{
				javax.swing.JOptionPane.showMessageDialog(jf, "File already imported\nPlease select new file" , "Dicom viewer", 2);
			}	  
			catch (NullPointerException e) {
				
			}
			catch (Exception e) {
				e.printStackTrace();
			}
        }
        else if(ae.getActionCommand()==Actions.IMPORTFOLDER.name())
        {   					
			JFileChooser  jf = new JFileChooser();
        	try 				
			{
	        	
				jf.setCurrentDirectory(new File("."));
				jf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				jf.setAcceptAllFileFilterUsed(false);
				jf.showOpenDialog(null);
			
			
				String directoryPath = jf.getSelectedFile().getAbsolutePath();
				File dcmfolder =  new File(directoryPath);
			
				File[] dcmfiles = dcmfolder.listFiles();
				Long l=new Long(dcmfolder.length());			   
				
				jpb.setMaximum(l.intValue());
				approveSelection(jf);
				DCMParser.dcmFolderParser(dcmfiles);
			} 
			catch (PSQLException e) 
			{
				javax.swing.JOptionPane.showMessageDialog(jf, "File already imported\nPlease select new file" , "Dicom viewer", 2);
			}	  
			catch (NullPointerException e) {
				
			}
        	
        	catch (DCMFileNotFoundExcepiton e) {
        		javax.swing.JOptionPane.showMessageDialog(jf, e.getMessage() , "Dicom viewer", 2);
			}
        	
			catch (Exception e) {
				e.printStackTrace();
			}
        	
        	
		
			
		    
        }
    }
    public  void approveSelection(JFileChooser j) throws InterruptedException
    {
    	synchronized(this)
    	{
    		notifyAll();
    	}    	
    	//run();
    }
// worker thread 
    public void run(){
        while(true){
         // wait for the signal from the GUI
            try{synchronized(this){wait();}}
            catch (InterruptedException e){
            	e.printStackTrace();
            }
            int limit=jpb.getMaximum();
          
         // simulate some long-running process like parsing a large file
            for (int i = 1; i <= limit; i=i*5){
            	
               jpb.setValue(i);
            //   System.out.println("actionPerformed sets jpb value to: "+i);
               try{Thread.sleep(150);
               if (i >= 0 && i < 10) 
                   jpb.setString("wait for sometime"); 
               else if(i>10 && i<200)
            	   jpb.setString("Importing started");
               		
               else
                   jpb.setString("Importing ....."); 
               }
              
            
               // make the process last a while
               catch (InterruptedException e){
            	   e.printStackTrace();
               }
           }
            jpb.setString("Importing completed");
            jpb.setValue(limit);
            try {
       		if(jpb.getValue()==limit)
       			{	Thread.sleep(10000);
       				jpb.setVisible(false); 
       			}
       		}
       	   catch (InterruptedException e){
      	   e.printStackTrace();
           }
       		 
        }
    }
}	