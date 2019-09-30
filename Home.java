package dicomviewerui;

import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.FileFilter;
import java.util.List;

import java.awt.event.ActionEvent;

import java.awt.Font;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.Color;


import javax.swing.JProgressBar;
import javax.swing.table.DefaultTableModel;



import dicomviewer.Image;
import dicomviewer.ImageDAO;
import dicomviewer.PatientStudy;
import dicomviewer.PatientStudyDAO;
import dicomviewer.Series;
import dicomviewer.SeriesDAO;

import javax.swing.JScrollPane;
import javax.swing.JFileChooser;

public class Home extends JFrame {

	private JPanel contentPane;
	private static JTable tSeries;
	private static JTable tImage;
	private static JTable tPatient;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Home frame = new Home();
					frame.setVisible(true);

					//dispSeries();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Home() {
		setTitle("Dicom Viewer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1058, 667);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// IMPORT FILE

		JButton btnImportFile = new JButton("Import File");
		btnImportFile.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		btnImportFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser jFile = new JFileChooser();
				FileNameExtensionFilter fileFilter=new FileNameExtensionFilter(".dcm", "dcm");
				jFile.setFileFilter(fileFilter);
				jFile.showDialog(null, "Import");

				JProgressBar progressBar = new JProgressBar();
				progressBar.setBounds(10, 572, 813, 25);
				contentPane.add(progressBar);
				progressBar.setVisible(true);

				// File path = jfile.getSelectedFile();
				// String trackPath = path.getAbsolutePath();
				// tLocation.setText(trackPath);

			}
		});
		btnImportFile.setBounds(0, 11, 108, 23);
		contentPane.add(btnImportFile);

		// IMPORT FOLDER

		JButton btnImportFolder = new JButton("Import Folder");
		btnImportFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser jFile = new JFileChooser();
				jFile.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				jFile.showDialog(null, "Import");
				
				// jfile.getCurrentDirectory());
			}
		});
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
				dispose();
			}
		});
		btnExit.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		btnExit.setBounds(943, 11, 89, 23);
		contentPane.add(btnExit);

		// TABLE PATIENT STUDY DETAILS

		tPatient = new JTable();
		tPatient.setAutoCreateRowSorter(true);
		tPatient.setUpdateSelectionOnSort(true);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 101, 548, 455);
		contentPane.add(scrollPane);
		DefaultTableModel dtmPatient = new DefaultTableModel(new String[] { "Patient Id", "Patient Name", "DOB",
				"Accession Number", "Study Id", "Study Description", "Study Date" }, 0);
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

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// final TableColumnModel columnModel = tPatient.getColumnModel();
		// for (int column = 0; column < tPatient.getColumnCount(); column++) {
		// int width = 15; // Min width
		// for (int row = 0; row < tPatient.getRowCount(); row++) {
		// TableCellRenderer renderer = tPatient.getCellRenderer(row, column);
		// Component comp = tPatient.prepareRenderer(renderer, row, column);
		// width = Math.max(comp.getPreferredSize().width +1 , width);
		// }
		// if(width > 300)
		// width=300;
		// columnModel.getColumn(column).setPreferredWidth(width);
		// }
		// tPatient.setBounds(10, 1000, 10000, 800);
		scrollPane.setViewportView(tPatient);
		tPatient.setBorder(new LineBorder(new Color(0, 0, 0)));

		// TABLE SERIES

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(568, 111, 464, 194);
		contentPane.add(scrollPane_1);
		DefaultTableModel dtmSeries = new DefaultTableModel(
				new String[] { "Series Id", "Series Number", "Modality", "Series Description" }, 0);
		dtmSeries.setRowCount(0);
		tSeries = new JTable();
		tSeries.setRowSelectionAllowed(true);
		// tSeries.setCellSelectionEnabled(true);

		tSeries.setModel(dtmSeries);
		// GET SELECTED ROE I PATIENT TABLE AND PRINT IN SERIES
		tSeries.setAutoCreateRowSorter(true);
		tSeries.setUpdateSelectionOnSort(true);
		scrollPane_1.setViewportView(tSeries);
		tSeries.setBorder(new LineBorder(new Color(0, 0, 0)));

		// TABLE IMAGE

		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(568, 406, 464, 150);
		contentPane.add(scrollPane_2);
		DefaultTableModel dtmImage = new DefaultTableModel(
				new String[] { "Image Number", "Image Type", "Rows", "Columns", "Bits Allocated", "Bits Stored" }, 0);
		dtmImage.setRowCount(0);
		tImage = new JTable();
		tImage.setModel(dtmImage);
		tImage.setRowSelectionAllowed(true);
		// TableColumn column = null;
		// for (int i = 0; i < 6; i++) {
		// column = tImage.getColumnModel().getColumn(i);
		//
		// column.setPreferredWidth(100);
		//
		// }
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
					
				}
			}
		});
		btnDetails.setBounds(822, 563, 89, 23);
		contentPane.add(btnDetails);

		JLabel lblNewLabel = new JLabel("Patient - Study Details");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel.setOpaque(true);
		lblNewLabel.setBackground(Color.LIGHT_GRAY);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 70, 548, 20);
		contentPane.add(lblNewLabel);

		JLabel lblImageDetails = new JLabel("Image  Details");
		lblImageDetails.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblImageDetails.setOpaque(true);
		lblImageDetails.setHorizontalAlignment(SwingConstants.CENTER);
		lblImageDetails.setBackground(Color.LIGHT_GRAY);
		lblImageDetails.setBounds(568, 382, 464, 20);
		contentPane.add(lblImageDetails);

		JLabel lblSeriesDetails = new JLabel("Series Details");
		lblSeriesDetails.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSeriesDetails.setOpaque(true);
		lblSeriesDetails.setHorizontalAlignment(SwingConstants.CENTER);
		lblSeriesDetails.setBackground(Color.LIGHT_GRAY);
		lblSeriesDetails.setBounds(578, 70, 454, 20);
		contentPane.add(lblSeriesDetails);
		
		tPatient.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) { 
				// String studyId = tPatient.getModel().getValueAt(tPatient.getSelectedRow(), 4).toString();
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
					DefaultTableModel dtmSeries = new DefaultTableModel(
							new String[] { "Series Id", "Series Number", "Modality", "Series Description" }, 0);
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
							//dispImage(studyId);

						}
						tSeries.setVisible(true);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
					}
					
				

		} catch (

		Exception ex) {
			//ex.printStackTrace();
		}
	}

	public static void dispImage() {
		try {
			
					//String studyId = tPatient.getModel().getValueAt(tPatient.getSelectedRow(), 4).toString();
			
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
						// TODO Auto-generated catch block
						//e.printStackTrace();
					}
					//System.out.println(String.valueOf(tableProducts.getModel()
//					  .getValueAt(tableProducts.getRowSorter().convertRowIndexToModel(
//							  tableProducts.getSelectedRow()), 1)));

				

		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}
}
