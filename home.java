package dicomviewerui;

import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.ActionEvent;

import java.awt.Font;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JProgressBar;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import dicomviewer.PatientStudy;
import dicomviewer.PatientStudyDAO;
import dicomviewer.Series;
import dicomviewer.SeriesDAO;

import javax.swing.JScrollPane;
import javax.swing.JFileChooser;

public class home extends JFrame {

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
					home frame = new home();
					frame.setVisible(true);
					dispSeries();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public home() {
		setTitle("Dicom Viewer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 953, 636);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

//		IMPORT FILE 
		
		JButton btnImportFile = new JButton("Import File");
		btnImportFile.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		btnImportFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser jFile = new JFileChooser();
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
		
		
		
		//IMPORT FOLDER

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

		
		
		
		//COMPARE 
		
		JButton btnCompareFiles = new JButton("Compare Files");
		btnCompareFiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
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
		btnExit.setBounds(734, 11, 89, 23);
		contentPane.add(btnExit);
		
		
		//TABLE PATIENT STUDY DETAILS
		
		
		 tPatient = new JTable();
		 tPatient.setAutoCreateRowSorter(true);
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

		
		
		//TABLE SERIES
		
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(568, 111, 369, 194);
		contentPane.add(scrollPane_1);
		DefaultTableModel dtmSeries = new DefaultTableModel(
				new String[] { "Series Id", "Series Number", "Modality", "Series Description" }, 0);
		dtmSeries.setRowCount(0);
		tSeries = new JTable();
		tSeries.setRowSelectionAllowed(true);
		// tSeries.setCellSelectionEnabled(true);

		tSeries.setModel(dtmSeries);
		//GET SELECTED ROE I  PATIENT TABLE AND PRINT IN SERIES  
		
		
		
		

		scrollPane_1.setViewportView(tSeries);
		tSeries.setBorder(new LineBorder(new Color(0, 0, 0)));

		
		// TABLE    IMAGE
		
		
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(568, 402, 369, 150);
		contentPane.add(scrollPane_2);
		DefaultTableModel dtmImage = new DefaultTableModel(
				new String[] { "Image Number", "Image Type", "Rows", "Columns", "Bits Allocated", "Bits Stored" }, 0);
		dtmImage.setRowCount(0);
		tImage = new JTable();
		tImage.setModel(dtmImage);
		// TableColumn column = null;
		// for (int i = 0; i < 6; i++) {
		// column = tImage.getColumnModel().getColumn(i);
		//
		// column.setPreferredWidth(100);
		//
		// }
		scrollPane_2.setViewportView(tImage);
		tImage.setBorder(new LineBorder(new Color(0, 0, 0)));
		tImage.setVisible(true);
		tImage.setToolTipText("Image-details");

		JButton btnDetails = new JButton("Details");
		btnDetails.setBounds(822, 563, 89, 23);
		contentPane.add(btnDetails);

		JLabel lblNewLabel = new JLabel("Patient - Study Details");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel.setOpaque(true);
		lblNewLabel.setBackground(Color.LIGHT_GRAY);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 70, 397, 20);
		contentPane.add(lblNewLabel);

		JLabel lblImageDetails = new JLabel("Image  Details");
		lblImageDetails.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblImageDetails.setOpaque(true);
		lblImageDetails.setHorizontalAlignment(SwingConstants.CENTER);
		lblImageDetails.setBackground(Color.LIGHT_GRAY);
		lblImageDetails.setBounds(568, 382, 369, 20);
		contentPane.add(lblImageDetails);

		JLabel lblSeriesDetails = new JLabel("Series Details");
		lblSeriesDetails.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSeriesDetails.setOpaque(true);
		lblSeriesDetails.setHorizontalAlignment(SwingConstants.CENTER);
		lblSeriesDetails.setBackground(Color.LIGHT_GRAY);
		lblSeriesDetails.setBounds(578, 70, 369, 20);
		contentPane.add(lblSeriesDetails);
	}
	public static void dispSeries()
	{
		try {
			
			 tPatient.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent event) {

					// do some actions here, for example
					// print first column value from selected row
					// System.out.println(tPatient.getValueAt(tPatient.getSelectedRow(),
					// 4).toString());
					DefaultTableModel dtmSeries = new DefaultTableModel(
							new String[] { "Series Id", "Series Number", "Modality", "Series Description" }, 0);
					String value = null;
					value = tPatient.getModel().getValueAt(tPatient.getSelectedRow(), 4).toString();
					System.out.println(value);
					List<Series> list;
					System.out.println("ggghhh");
					try {
						System.out.println("u");
						list = SeriesDAO.viewSeriesByStudy(value);
						for (int i = 0; i < list.size(); i++) {
							Object rowSeries[] = { list.get(i).getSeriesId(), list.get(i).getSeriesNumber(),
									list.get(i).getModality(), list.get(i).getSeriesDescription() };
							dtmSeries.addRow(rowSeries);
							tSeries.setModel(dtmSeries);
							System.out.println("HIII");

						}
						tSeries.setVisible(true);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			});

			
	} catch (

	Exception ex) {
		ex.printStackTrace();
	}
	}
//	public static void dispImage()
//	{
//		tPatient.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
//			public void valueChanged(ListSelectionEvent event) {
//				DefaultTableModel dtmImage = new DefaultTableModel(
//						new String[] { "Image Number", "Image Type", "Rows", "Columns", "Bits Allocated", "Bits Stored" }, 0);
//				String value = null;
//				value = tSeries.getModel().getValueAt(tPatient.getSelectedRow(), 0).toString();
//				List<Series> list;
//				System.out.println("ggghhh");
//				try {
//					System.out.println("u");
//					list = SeriesDAO.viewImageBySeries(String studyId, String seriesId);
//					for (int i = 0; i < list.size(); i++) {
//						Object rowSeries[] = { list.get(i).getSeriesId(), list.get(i).getSeriesNumber(),
//								list.get(i).getModality(), list.get(i).getSeriesDescription() };
//						dtmSeries.addRow(rowSeries);
//						tSeries.setModel(dtmSeries);
//						System.out.println("HIII");
//
//					}
//					tSeries.setVisible(true);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//
//			}
//
//		});
//
//		
//} catch (
//
//Exception ex) {
//	ex.printStackTrace();
//}
//}
//	
			
}
	
