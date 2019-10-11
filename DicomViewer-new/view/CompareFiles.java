package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import controller.DicomViewerController;
import model.DCMParser;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;

public class CompareFiles extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTable table;
	static DicomViewerController controller;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			CompareFiles dialog = new CompareFiles();
			 controller = new DicomViewerController(new DCMParser());
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public CompareFiles() {
		setTitle("ComapreFiles");
		setBounds(100, 100, 598, 439);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		@SuppressWarnings("rawtypes")
		JComboBox comboBox = new JComboBox();
		comboBox.addActionListener(new ActionListener() {
			
			
			
			// START FROM HERE
			
			public void actionPerformed(ActionEvent arg0) {
				comboBox.getSelectedItem();
			}
		});
		comboBox.setBounds(27, 15, 188, 20);
		contentPanel.add(comboBox);
		
		@SuppressWarnings("rawtypes")
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setBounds(322, 15, 173, 20);
		contentPanel.add(comboBox_1);
		JButton btnNewButton = new JButton("New button");
		btnNewButton.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser jfile1=new JFileChooser();
				
				FileNameExtensionFilter fileFilter=new FileNameExtensionFilter(".dcm", "dcm");
				jfile1.setFileFilter(fileFilter);
				jfile1.showOpenDialog(null);
				File path = jfile1.getSelectedFile();
				try {
						controller.parseDCMFile(path);
						
				} 
				catch (Exception e) {					
					e.printStackTrace();
				}
				
				String location = path.getAbsolutePath();
				comboBox.addItem(location);
			}
		});
		btnNewButton.setBounds(225, 14, 23, 23);
		contentPanel.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("New button");
		btnNewButton_1.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser jfile2=new JFileChooser();
				FileNameExtensionFilter fileFilter=new FileNameExtensionFilter(".dcm", "dcm");
				jfile2.setFileFilter(fileFilter);
				jfile2.showOpenDialog(null);
				File path = jfile2.getSelectedFile();
				String location = path.getAbsolutePath();
				comboBox.addItem(location);
				
			}
		});
		btnNewButton_1.setBounds(507, 14, 23, 23);
		contentPanel.add(btnNewButton_1);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 69, 541, 287);
		contentPanel.add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("CLOSE");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}
}