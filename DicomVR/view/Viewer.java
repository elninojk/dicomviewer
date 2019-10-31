package view;

import java.awt.EventQueue;
import controller.DicomVRController;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.awt.event.ActionEvent;

import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.dcm4che3.io.DicomStreamException;
import model.DCMObjFactory;
import model.DCMParser;
import model.Image;
import model.PatientStudy;
import model.Series;

public class Viewer extends JFrame {	
	private static final long serialVersionUID = 7810259421035585537L;
	private static JPanel contentPane;
	private static JTable tSeries;
	private static JTable tImage;
	private static JTable tPatient;
	private JTextField tPatientId;
	private JTextField tPatientName;
	private JTextField tDOB;
	private JTextField tAccNumber;
	private JTextField tStudyId;
	private JTextField tStudyDecs;
	private JTextField tStudyDateTime;
	private static JFileChooser jf;
	private TableRowSorter<TableModel> sorter;
	static DefaultTableModel dtmPatient;
	static DicomVRController parserController;
	static JProgressBar progressBar = new JProgressBar();
	static int importStatus;
	static boolean dcmFileFound;
	static boolean fileFound;
	static String directoryPath;
	static int w;
	static int heightPatient;
	static boolean seriesFound, imageFound, PatientStudyFound = false;
	static JFrame frameViewer;
	/**
	 * Launch the application.
	 */
	public static void ViewerUI() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Viewer frame = new Viewer();
				frameViewer = frame;
				frame.setVisible(true);
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Viewer() {
		setTitle("Dicom Viewer");
	
		Toolkit t = Toolkit.getDefaultToolkit();
		Dimension d = t.getScreenSize();
		int h = d.height;
		w = d.width;
		setBounds(0, 0, w, h);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(0, 0, w, h));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		progressBar.setForeground(Color.BLACK);
		progressBar.setStringPainted(true);
	
		// IMPORT FILE

		JButton btnImportFile = new JButton("Import File");
		btnImportFile.setFont(new Font("Microsoft Tai Le", Font.PLAIN, 14));
		btnImportFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				progressBar.setBounds(40, 135 + heightPatient, w - 80, 25);
				contentPane.add(progressBar);
				progressBar.setStringPainted(true);
				progressBar.setValue(0);
				progressBar.setString("");
				progressBar.setVisible(true);

				ImportFile imp = new ImportFile();
				imp.start();
				int i = 0;
				while ((imp.getPatientImportStatus() == 1 || imp.getSeriesImportStatus() == 1
						|| imp.getImageImportStatus() == 1) && i < 101) {
					for (i = 0; i < 90; i = i + 2) {
						progressBar.setValue(i);
						progressBar.setString("Importing...................");
						System.out.println(progressBar.getString());
						progressBar.update(progressBar.getGraphics());
						System.out.println(
								Thread.currentThread().getName() + "    " + i + "    " + progressBar.getValue());
						// Thread.sleep(10);
					}
					try {
						imp.join();
					} catch (Exception e) {
						javax.swing.JOptionPane.showMessageDialog(jf, "Import error", "Dicom Viewer", 1);
					}

					progressBar.setValue(95);
					progressBar.update(progressBar.getGraphics());
					System.out.println(Thread.currentThread().getName());

					i = 100;
					progressBar.setValue(i);
					progressBar.setString("Importing Completed");
					progressBar.update(progressBar.getGraphics());
					System.out.println(i + "Importing Completed");
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
						e.getMessage();
					}

					i = 101;
				}
				try {
					imp.join();
				} catch (Exception e) {
					javax.swing.JOptionPane.showMessageDialog(jf, "progressbar error", "Dicom Viewer", 1);
				}
				progressBar.setVisible(false);

			}
		});
		btnImportFile.setBounds(20, 11, 108, 23);
		contentPane.add(btnImportFile);

		// IMPORT FOLDER

		JButton btnImportFolder = new JButton("Import Folder");
		btnImportFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				importFolder();

			}
		});
		btnImportFolder.setFont(new Font("Microsoft Tai Le", Font.PLAIN, 14));
		btnImportFolder.setBounds(149, 11, 125, 23);
		contentPane.add(btnImportFolder);

		// COMPARE

		JButton btnCompareFiles = new JButton("Compare Files");
		btnCompareFiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new CompareFiles(frameViewer).setVisible(true);
				setEnabled(false);
			}
		});
		btnCompareFiles.setFont(new Font("Microsoft Tai Le", Font.PLAIN, 14));
		btnCompareFiles.setBounds(284, 11, 126, 23);
		contentPane.add(btnCompareFiles);

		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnExit.setFont(new Font("Microsoft Tai Le", Font.PLAIN, 14));
		btnExit.setBounds(w - 109, 11, 89, 23);
		contentPane.add(btnExit);

		// TABLE PATIENT STUDY DETAILS

		tPatient = new JTable() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 3305957411993496745L;

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return false;
			}
		};

		tPatient.setAutoCreateRowSorter(true);
		tPatient.setUpdateSelectionOnSort(true);
		int widthPatient = (w - 60) * 7 / 12;
		heightPatient = h - 275;

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(20, 105, widthPatient, heightPatient);
		contentPane.add(scrollPane);
		dtmPatient = new DefaultTableModel(new String[] { "Patient Id", "Patient Name", "DOB", "Accession Number",
				"Study Id", "Study Description", "Study Date" }, 0);
		dtmPatient.setRowCount(0);

		tPatient.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tPatient.setModel(dtmPatient);
		try {
			List<PatientStudy> list = DicomVRController.viewAllPatientStudy();
			for (int i = 0; i < list.size(); i++) {
				Object row[] = { list.get(i).getPatientId(), list.get(i).getPatientName(), list.get(i).getPatientDOB(),
						list.get(i).getAccessionNumber(), list.get(i).getStudyId(), list.get(i).getStudyDescription(),
						list.get(i).getStudyDateTime() };
				dtmPatient.addRow(row);
				tPatient.setModel(dtmPatient);

			}
			tPatient.setRowSelectionInterval(0, 0);

		} catch (Exception ex) {
			javax.swing.JOptionPane.showMessageDialog(jf, "Study not found", "Dicom Viewer", 1);
		}

		scrollPane.setViewportView(tPatient);
		tPatient.setBorder(new LineBorder(new Color(0, 0, 0)));

		// TABLE SERIES
		int widthSeries = (w - 60) * 5 / 12;
		int heightSeries = (heightPatient - 70) / 2;

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(widthPatient + 40, 105, widthSeries, heightSeries);
		contentPane.add(scrollPane_1);
		DefaultTableModel dtmSeries = new DefaultTableModel(
				new String[] { "Series Id", "Series Number", "Modality", "Series Description" }, 0);
		dtmSeries.setRowCount(0);
		tSeries = new JTable() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 8114004201727745258L;

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return false;
			}
		};

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
		scrollPane_2.setBounds(widthPatient + 40, heightSeries + 175, widthSeries, heightSeries);
		contentPane.add(scrollPane_2);
		DefaultTableModel dtmImage = new DefaultTableModel(
				new String[] { "Image Number", "Image Type", "Rows", "Columns", "Bits Allocated", "Bits Stored" }, 0);
		dtmImage.setRowCount(0);
		tImage = new JTable() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -5764907445545304635L;

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return false;
			}
		};
		tImage.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

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

		// VIEW DETAILS

		JButton btnDetails = new JButton("Details");
		btnDetails.setFont(new Font("Microsoft Tai Le", Font.PLAIN, 14));
		btnDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String studyId = tPatient.getModel()
						.getValueAt(tPatient.getRowSorter().convertRowIndexToModel(tPatient.getSelectedRow()), 4)
						.toString();
				String seriesId = tSeries.getModel()
						.getValueAt(tSeries.getRowSorter().convertRowIndexToModel(tSeries.getSelectedRow()), 0)
						.toString();
				String imageNumber = tImage.getModel()
						.getValueAt(tImage.getRowSorter().convertRowIndexToModel(tImage.getSelectedRow()), 0)
						.toString();
				new ImageDetails(studyId, seriesId, imageNumber).setVisible(true);
				System.out.println(studyId + " IMAGE " + seriesId + "  SERIES " + imageNumber);

			}
		});
		btnDetails.setBounds(1256, 328, 90, 23);
		contentPane.add(btnDetails);

		JLabel lblNewLabel = new JLabel("Patient - Study Details");
		lblNewLabel.setFont(new Font("Microsoft Tai Le", Font.BOLD, 14));
		lblNewLabel.setOpaque(true);
		lblNewLabel.setBackground(Color.LIGHT_GRAY);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(20, 59, widthPatient, 20);
		contentPane.add(lblNewLabel);

		JLabel lblImageDetails = new JLabel("Image  Details");
		lblImageDetails.setFont(new Font("Microsoft Tai Le", Font.BOLD, 14));
		lblImageDetails.setOpaque(true);
		lblImageDetails.setHorizontalAlignment(SwingConstants.CENTER);
		lblImageDetails.setBackground(Color.LIGHT_GRAY);
		lblImageDetails.setBounds(widthPatient + 40, heightSeries + 150, widthSeries, 20);
		contentPane.add(lblImageDetails);

		JLabel lblSeriesDetails = new JLabel("Series Details");
		lblSeriesDetails.setFont(new Font("Microsoft Tai Le", Font.BOLD, 14));
		lblSeriesDetails.setOpaque(true);
		lblSeriesDetails.setHorizontalAlignment(SwingConstants.CENTER);
		lblSeriesDetails.setBackground(Color.LIGHT_GRAY);
		lblSeriesDetails.setBounds(widthPatient + 40, 59, widthSeries, 20);
		contentPane.add(lblSeriesDetails);
		int widthText = widthPatient / 7;

		tPatientId = new JTextField();
		tPatientId.setBounds(20, 80, widthText, 23);
		contentPane.add(tPatientId);
		tPatientId.setColumns(10);

		tPatientName = new JTextField();
		tPatientName.setBounds(widthText + 20, 80, widthText, 23);
		contentPane.add(tPatientName);
		tPatientName.setColumns(10);

		tDOB = new JTextField();
		tDOB.setBounds(widthText * 2 + 20, 80, widthText, 23);
		contentPane.add(tDOB);
		tDOB.setColumns(10);

		tAccNumber = new JTextField();
		tAccNumber.setBounds(widthText * 3 + 20, 80, widthText, 23);
		contentPane.add(tAccNumber);
		tAccNumber.setColumns(10);

		tStudyId = new JTextField();
		tStudyId.setBounds(widthText * 4 + 20, 81, widthText, 22);
		contentPane.add(tStudyId);
		tStudyId.setColumns(10);

		tStudyDecs = new JTextField();
		tStudyDecs.setBounds(widthText * 5 + 21, 81, widthText, 22);
		contentPane.add(tStudyDecs);
		tStudyDecs.setColumns(10);

		tStudyDateTime = new JTextField();
		tStudyDateTime.setBounds(widthText * 6 + 23, 81, widthText, 22);
		contentPane.add(tStudyDateTime);
		tStudyDateTime.setColumns(10);

		tPatientId.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
					tPatientName.setText("");
					tDOB.setText("");
					tAccNumber.setText("");
					tStudyId.setText("");
					tStudyDecs.setText("");
					tStudyDateTime.setText("");
				sorter = new TableRowSorter<TableModel>(dtmPatient);
				sorter.setRowFilter(RowFilter.regexFilter(tPatientId.getText(), 0));
				tPatient.setRowSorter(sorter);
				try {
					tPatient.setRowSelectionInterval(0, 0);
					dispSeries();
					dispImage();
					
				} catch (Exception ex) {
					tSeries.setVisible(false);
					tImage.setVisible(false);
					javax.swing.JOptionPane.showMessageDialog(jf, "No matching found", "Dicom viewer", 2);
					tPatientId.setText("");
					dispPatient();
				}

			}

		});

		tPatientName.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
					tPatientId.setText("");
					tDOB.setText("");
					tAccNumber.setText("");
					tStudyId.setText("");
					tStudyDecs.setText("");
					tStudyDateTime.setText("");
				TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(dtmPatient);
				sorter.setRowFilter(RowFilter.regexFilter(tPatientName.getText(), 1));

				tPatient.setRowSorter(sorter);
				try {
					tPatient.setRowSelectionInterval(0, 0);
					dispSeries();
					dispImage();
					
				} catch (Exception ex) {
					tSeries.setVisible(false);
					tImage.setVisible(false);
					javax.swing.JOptionPane.showMessageDialog(jf, "No matching found", "Dicom viewer", 2);
					tPatientName.setText("");
					dispPatient();
				}

			}

		});

		tDOB.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
					tPatientName.setText("");
					tPatientId.setText("");
					tAccNumber.setText("");
					tStudyId.setText("");
					tStudyDecs.setText("");
					tStudyDateTime.setText("");
				sorter = new TableRowSorter<TableModel>(dtmPatient);
				sorter.setRowFilter(RowFilter.regexFilter(tDOB.getText(), 2));

				tPatient.setRowSorter(sorter);
				try {
					tPatient.setRowSelectionInterval(0, 0);
					dispSeries();
					dispImage();
					
				} catch (Exception ex) {
					tSeries.setVisible(false);
					tImage.setVisible(false);
					javax.swing.JOptionPane.showMessageDialog(jf, "No matching found", "Dicom viewer", 2);
					tDOB.setText("");
					dispPatient();
				}

			}

		});

		tAccNumber.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
					tPatientName.setText("");
					tPatientId.setText("");
					tDOB.setText("");
					tStudyId.setText("");
					tStudyDecs.setText("");
					tStudyDateTime.setText("");
				TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(dtmPatient);
				sorter.setRowFilter(RowFilter.regexFilter(tAccNumber.getText(), 3));

				tPatient.setRowSorter(sorter);
				try {
					tPatient.setRowSelectionInterval(0, 0);
					dispSeries();
					dispImage();
					
				} catch (Exception ex) {
					tSeries.setVisible(false);
					tImage.setVisible(false);
					javax.swing.JOptionPane.showMessageDialog(jf, "No matching found", "Dicom viewer", 2);
					tAccNumber.setText("");
					dispPatient();
				}

			}

		});

		tStudyId.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
					tPatientName.setText("");
					tPatientId.setText("");
					tAccNumber.setText("");
					tDOB.setText("");
					tStudyDecs.setText("");
					tStudyDateTime.setText("");
				TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(dtmPatient);
				sorter.setRowFilter(RowFilter.regexFilter(tStudyId.getText(), 4));

				tPatient.setRowSorter(sorter);
				try {
					tPatient.setRowSelectionInterval(0, 0);
					dispSeries();
					dispImage();
					
				} catch (Exception ex) {
					tSeries.setVisible(false);
					tImage.setVisible(false);
					javax.swing.JOptionPane.showMessageDialog(jf, "No matching found", "Dicom viewer", 2);
					tStudyId.setText("");
					dispPatient();
				}

			}

		});

		tStudyDateTime.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
					tPatientName.setText("");
					tPatientId.setText("");
					tAccNumber.setText("");
					tStudyId.setText("");
					tStudyDecs.setText("");
					tDOB.setText("");
				TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(dtmPatient);
				sorter.setRowFilter(RowFilter.regexFilter(tStudyDateTime.getText(), 6));

				tPatient.setRowSorter(sorter);
				try {
					tPatient.setRowSelectionInterval(0, 0);
					dispSeries();
					dispImage();
					
				} catch (Exception ex) {
					tSeries.setVisible(false);
					tImage.setVisible(false);
					javax.swing.JOptionPane.showMessageDialog(jf, "No matching found", "Dicom viewer", 2);
					tStudyDateTime.setText("");
					dispPatient();
				}

			}

		});

		tStudyDecs.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				
				tPatientName.setText("");
				tPatientId.setText("");
				tAccNumber.setText("");
				tStudyId.setText("");
				tDOB.setText("");
				tStudyDateTime.setText("");
				TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(dtmPatient);
				sorter.setRowFilter(RowFilter.regexFilter(tStudyDecs.getText(), 5));
				tPatient.setRowSorter(sorter);			
				try {
					tPatient.setRowSelectionInterval(0, 0);
					dispSeries();
					dispImage();
					tStudyDecs.setText("");
				} catch (Exception ex) {
					tSeries.setVisible(false);
					tImage.setVisible(false);
					javax.swing.JOptionPane.showMessageDialog(jf, "No matching found", "Dicom viewer", 2);
					tStudyDecs.setText("");
					dispPatient();
				}
			}

		});

		tPatient.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
				// String studyId = tPatient.getModel().getValueAt(tPatient.getSelectedRow(),
				// 4).toString();
				dispSeries();
			}

		});
		tSeries.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {

				dispImage();
			}

		});
		dispSeries();
		dispImage();

	}


	

	public static void dispSeries() {
		try {
			tImage.setVisible(false);
			DefaultTableModel dtmSeries = new DefaultTableModel(
					new String[] { "Series Id", "Series Number", "Modality", "Series Description" }, 0);
			String studyId = null;
			dtmSeries.setRowCount(0);
			studyId = String.valueOf(tPatient.getModel()
					.getValueAt(tPatient.getRowSorter().convertRowIndexToModel(tPatient.getSelectedRow()), 4));

			List<Series> list;

			list = DicomVRController.viewSeries(studyId);
			for (int i = 0; i < list.size(); i++) {
				Object rowSeries[] = { list.get(i).getSeriesId(), list.get(i).getSeriesNumber(),
						list.get(i).getModality(), list.get(i).getSeriesDescription() };
				dtmSeries.addRow(rowSeries);
				tSeries.setModel(dtmSeries);
				seriesFound = true;
			}
			tSeries.setRowSelectionInterval(0, 0);
			tSeries.setVisible(true);
		} catch (Exception e) {
			seriesFound = false;
		}

	}

	public static void dispImage() {
		try {
			String studyId = tPatient.getModel()
					.getValueAt(tPatient.getRowSorter().convertRowIndexToModel(tPatient.getSelectedRow()), 4)
					.toString();
			DefaultTableModel dtmImage = new DefaultTableModel(
					new String[] { "Image Number", "Image Type", "Rows", "Columns", "Bits Allocated", "Bits Stored" },
					0);
			dtmImage.setRowCount(0);
			String seriesId = null;

			seriesId = tSeries.getModel()
					.getValueAt(tSeries.getRowSorter().convertRowIndexToModel(tSeries.getSelectedRow()), 0).toString();

			List<Image> list = DicomVRController.viewImage(studyId, seriesId);
			for (int i = 0; i < list.size(); i++) {
				Object rowImage[] = { list.get(i).getImageNumber(), list.get(i).getImageType(), list.get(i).getRows(),
						list.get(i).getColumns(), list.get(i).getBitsAllocated(), list.get(i).getBitsStored() };
				dtmImage.addRow(rowImage);
				tImage.setModel(dtmImage);
				imageFound = true;
			}
			tImage.setVisible(true);
			tImage.setRowSelectionInterval(0, 0);

		} catch (Exception e) {
			imageFound = false;
		}

	}

	public static void dispPatient() {
		List<PatientStudy> list = null;

		dtmPatient = new DefaultTableModel(new String[] { "Patient Id", "Patient Name", "DOB", "Accession Number",
				"Study Id", "Study Description", "Study Date" }, 0);
		dtmPatient.setRowCount(0);

		tPatient.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tPatient.setModel(dtmPatient);
		try {
			list = DicomVRController.viewAllPatientStudy();
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					Object row[] = { list.get(i).getPatientId(), list.get(i).getPatientName(),
							list.get(i).getPatientDOB(), list.get(i).getAccessionNumber(), list.get(i).getStudyId(),
							list.get(i).getStudyDescription(), list.get(i).getStudyDateTime() };
					dtmPatient.addRow(row);
					tPatient.setModel(dtmPatient);
					tPatient.setRowSelectionInterval(0, 0);
					PatientStudyFound = true;
				}
			}

		} catch (Exception e) {
			if(PatientStudyFound == imageFound == seriesFound)
			javax.swing.JOptionPane.showMessageDialog(jf, "Study not found", "Dicom viewer", JOptionPane.INFORMATION_MESSAGE);
		}

	}

	public static void importFolder() {

		JFileChooser jf = new JFileChooser();

		jf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {

				if (ae.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {
					try {
						System.out.println("approve");
						importStatus = 0;
						dcmFileFound = false;
						directoryPath = null;

						parserController = new DicomVRController(new DCMParser());

						directoryPath = jf.getSelectedFile().getAbsolutePath();
						File dcmfolder = new File(directoryPath);

						File[] dcmfiles = dcmfolder.listFiles();
						int fileSize = dcmfiles.length;

						progressBar = new JProgressBar();
						progressBar.setBounds(40, 135 + heightPatient, w - 80, 25);
						contentPane.add(progressBar);
						progressBar.setStringPainted(true);
						progressBar.setVisible(true);
						fileFound = false;
						for (File f : dcmfiles) {
							int commonFileStatus = -1;
							fileFound = true;
							try {
								parserController.parseDCMFile(f);
								dcmFileFound = true;

								DicomVRController dbController = DCMObjFactory.createDCMObj();
								try {
									dbController.insertPatientStudy();
									importStatus = importStatus + 1;
									commonFileStatus = commonFileStatus + 1;
								} catch (Exception e) {

								}
								try {
									dbController.insertSeries();
									importStatus = importStatus + 1;
									commonFileStatus = commonFileStatus + 1;
								} catch (Exception e) {

								}
								try {
									dbController.insertImage();
									importStatus = importStatus + 1;
									commonFileStatus = commonFileStatus + 1;
								} catch (Exception e) {

								}
								if (commonFileStatus >= 0)
									fileSize = fileSize + commonFileStatus;
								progressBar.setValue((importStatus * 98) / (fileSize));
								progressBar.setString(
										"Importing..................." + (importStatus * 98) / (fileSize) + "%");
								progressBar.update(progressBar.getGraphics());
								System.out.println(
										(importStatus * 98) / (fileSize) + "   " + importStatus + "   " + fileSize);
							} catch (Exception e) {

							}
						}

					} catch (DicomStreamException e) {
						javax.swing.JOptionPane.showMessageDialog(jf, e.getMessage(), "Dicom viewer", 2);
					} catch (Exception e) {
						javax.swing.JOptionPane.showMessageDialog(jf, e.getMessage(), "Dicom viewer", 2);
						e.printStackTrace();
					}

					dispPatient();
					dispSeries();
					dispImage();
					if (importStatus >= 1) {
						progressBar.setValue(100);
						progressBar.setString("Importing  Completed.....100%");
						System.out.println(progressBar.getString());
						progressBar.update(progressBar.getGraphics());

					}
					if (importStatus == 0 && directoryPath != null && dcmFileFound) {
						progressBar.setVisible(false);
						javax.swing.JOptionPane.showMessageDialog(jf, "File already imported\nPlease select new folder",
								"Dicom viewer", 2);

					}
					if (fileFound && !dcmFileFound) {
						progressBar.setVisible(false);
						javax.swing.JOptionPane.showMessageDialog(jf, "No dcm file found in selected folder",
								"Dicom viewer", 2);
					}
					progressBar.setVisible(false);

				}

			}
		});

		jf.setCurrentDirectory(new File("."));
		jf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		jf.setAcceptAllFileFilterUsed(false);
		jf.showOpenDialog(null);

	}

}
