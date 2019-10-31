package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import controller.DicomVRController;
import model.DCMParser;

import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JScrollPane;

import javax.swing.JComboBox;
import javax.swing.JLabel;

public class CompareFiles extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private static JTable table;
	static DicomVRController controller;
	static DefaultTableModel dtmCompare;
	static int tagDifference = 0;
	static JLabel lblNewLabel;
	

	/**
	 * Create the dialog.
	 */
	public CompareFiles(JFrame frame) {
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent e) {
		    	try {
		    		frame.setEnabled(true);
		    		dispose();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
		    }
		});
		setTitle("ComapreFiles");
		setResizable(false);
		Toolkit t = Toolkit.getDefaultToolkit();
		Dimension d = t.getScreenSize();
		int h = d.height;
		int w = d.width;
		setBounds(w / 2 - 1100 / 2, h / 2 - 497 / 2, 1100, 497);

		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, w, h));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
//		this.setAlwaysOnTop(true);
		JComboBox<File> comboBox = new JComboBox<File>();
		comboBox.addActionListener(new ActionListener() {

			// START FROM HERE

			public void actionPerformed(ActionEvent arg0) {
				comboBox.getSelectedItem();
				
				
				File path = (File) comboBox.getSelectedItem();
				tableDisplay(path, table, 1);

			}
		});
		comboBox.setBounds(27, 15, 368, 20);
		contentPanel.add(comboBox);

		JComboBox<File> comboBox_1 = new JComboBox<File>();
		comboBox_1.addActionListener(new ActionListener() {

			// START FROM HERE

			public void actionPerformed(ActionEvent arg0) {
				
				
				File path = (File) comboBox_1.getSelectedItem();
				tableDisplay(path, table, 2);

			}
		});
		comboBox_1.setBounds(669, 15, 368, 20);
		contentPanel.add(comboBox_1);
		JButton btnNewButton = new JButton("New button");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser jfile1 = new JFileChooser();

				jfile1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {

						if (ae.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {

							table.setBackground(Color.white);
						
							File path = jfile1.getSelectedFile();
//							tableDisplay(path, table, 1);

							// String location = path.getAbsolutePath();
							comboBox.insertItemAt(path, 0);
							comboBox.setSelectedIndex(0);
							
						}

					}
				});
				FileNameExtensionFilter fileFilter = new FileNameExtensionFilter(".dcm", "dcm");
				jfile1.setFileFilter(fileFilter);
				jfile1.showOpenDialog(null);

			}
		});
		btnNewButton.setBounds(405, 14, 23, 23);
		contentPanel.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("New button");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser jfile2 = new JFileChooser();

				jfile2.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						if (ae.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {

							
							
							File path = jfile2.getSelectedFile();
//							tableDisplay(path, table, 2);

							// String location = path.getAbsolutePath();
							comboBox_1.insertItemAt(path, 0);
							comboBox_1.setSelectedIndex(0);

						}

					}
				});
				FileNameExtensionFilter fileFilter = new FileNameExtensionFilter(".dcm", "dcm");
				jfile2.setFileFilter(fileFilter);
				jfile2.showOpenDialog(null);

			}
		});
		btnNewButton_1.setBounds(1047, 14, 23, 23);
		contentPanel.add(btnNewButton_1);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(27, 46, 1043, 342);
		contentPanel.add(scrollPane);

		table = new JTable() {		

			/**
			 * 
			 */
			private static final long serialVersionUID = 2849222572137862698L;

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return false;
			}
		};
		table.setCellSelectionEnabled(false);
		scrollPane.setViewportView(table);
		dtmCompare = new DefaultTableModel(new String[] { "Tag", "Value", " Value" }, 0);
		table.setModel(dtmCompare);
		
		lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(500, 417, 284, 20);
		contentPanel.add(lblNewLabel);

	}

	public static void tableDisplay(File path, JTable table, int coloumn) {
		try {
			int j = 0;
			controller = new DicomVRController(new DCMParser());
			// DefaultTableModel dtmCompare = new DefaultTableModel(new String[] { "Tag",
			// "Size", "Value", "Size"," Value" }, 0);
			controller.parseDCMFile(path);
			ArrayList<String> patient = new ArrayList<String>();
			ArrayList<String> series = new ArrayList<String>();
			ArrayList<String> image = new ArrayList<String>();
			patient = DicomVRController.getPatientStudyTags();
			series = DicomVRController.getSeriesTags();
			image = DicomVRController.getImageTags();
			String[] str = { "PatientID", "PatientName", "PatientBirthDate", "AccessionNumber", "StudyInstanceUID",
					"StudyDescription", "StudyDate and StudyTime", "SeriesInstanceUID", "StudyInstanceUID",
					"SeriesNumber", "Modality", "SeriesDescription", "SOPInstanceUID", "StudyInstanceUID",
					"SeriesInstanceUID", "ImageType", "Rows", "Columns", "BitsAllocated", "BitsStored" };
			for (int i = 0; i < patient.size(); i++) {
				Object rowSeries[] = { str[j] };
				if (dtmCompare.getRowCount() < (patient.size() + series.size() + image.size()))
					dtmCompare.addRow(rowSeries);
				dtmCompare.setValueAt(patient.get(i), j, coloumn);			
				table.setModel(dtmCompare);
				j++;

			}
			for (int i = 0; i < series.size(); i++) {
				Object rowSeries[] = { str[j] };
				if (dtmCompare.getRowCount() < (patient.size() + series.size() + image.size()))
					dtmCompare.addRow(rowSeries);
				dtmCompare.setValueAt(series.get(i), j, coloumn);
				// dtmCompare.setValueAt(series.get(i).length(), j, coloumn-1);
				table.setModel(dtmCompare);
				j++;

			}
			for (int i = 0; i < image.size(); i++) {
				Object rowSeries[] = { str[j] };
				if (dtmCompare.getRowCount() < (patient.size() + series.size() + image.size()))
					dtmCompare.addRow(rowSeries);
				dtmCompare.setValueAt(image.get(i), j, coloumn);
				// dtmCompare.setValueAt(image.get(i).length(), j, coloumn-1);
				table.setModel(dtmCompare);
				j++;

			}
			
			 
				table.getColumnModel().getColumn(0).setCellRenderer(new ColoredTableCellRenderer());
				
				table.getColumnModel().getColumn(1).setCellRenderer(new ColoredTableCellRenderer());
//				tagDifference=0;
				table.getColumnModel().getColumn(2).setCellRenderer(new ColoredTableCellRenderer());
				
				
//				table.setForeground(Color.BLUE);
				table.update(table.getGraphics());
				
				System.out.println(findTagDifference());
				
				lblNewLabel.setText("Dissimilar Fields : "+ tagDifference);
				
//				
//				if (table.getModel().getValueAt(row, 1) != null && table.getModel().getValueAt(row, 2) != null) {
//					if (!(table.getModel().getValueAt(row, 1).equals(table.getModel().getValueAt(row, 2)))) {
//
//						setForeground(Color.RED);
//						CompareFiles.tagDifference = CompareFiles.tagDifference +1;
//						System.out.println(CompareFiles.tagDifference + "  CompareFiles.tagDifference" +table.getModel().getValueAt(row, 1));
//
//					} else
//						setForeground(Color.BLUE);
//
//				}

			

		} catch (Exception e) {	
			e.printStackTrace();
			javax.swing.JOptionPane.showMessageDialog(null, "File could not be read", "Dicom viewer", 2);	
			
		}

	}
	
	public static int findTagDifference()
	{
		tagDifference  = 0;
		
		for(int row =0 ; row < 20 ;row++)
		{
		if (table.getModel().getValueAt(row, 1) != null && table.getModel().getValueAt(row, 2) != null) {
			if (!(table.getModel().getValueAt(row, 1).equals(table.getModel().getValueAt(row, 2)))) {
	
				tagDifference = tagDifference + 1;

		}
		}
		
	}
		return tagDifference;
    }
}

class ColoredTableCellRenderer extends DefaultTableCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4625111482625389608L;
	

	public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused,
			int row, int column) {
//		CompareFiles.tagDifference  = 0;
		setEnabled(table == null || table.isEnabled()); // see question above
		
		if (table.getModel().getValueAt(row, 1) != null && table.getModel().getValueAt(row, 2) != null) {
			if (!(table.getModel().getValueAt(row, 1).equals(table.getModel().getValueAt(row, 2)))) {

				setForeground(Color.RED);
//				CompareFiles.tagDifference = CompareFiles.tagDifference + 1;
//				System.out.println(CompareFiles.tagDifference + "  CompareFiles.tagDifference" +table.getModel().getValueAt(row, 1));

			} else
				setForeground(Color.BLUE);

		}

		else
			setForeground(Color.BLACK);

		super.getTableCellRendererComponent(table, value, selected, focused, row, column);

		return this;
	}
}
