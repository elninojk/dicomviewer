package ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import dcmparser.DCMParser;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.LayoutStyle.ComponentPlacement;

public class FileImportUi extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FileImportUi frame = new FileImportUi();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FileImportUi() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 452, 150);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		JFileChooser  jf = new JFileChooser();
		
		JButton importFile = new JButton("Import File");
		importFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser  jf = new JFileChooser();
				FileNameExtensionFilter dcmfilter = new FileNameExtensionFilter("Dicom file(.dcm)","dcm");
				
				jf.setCurrentDirectory(new File("."));
				jf.setAcceptAllFileFilterUsed(false);
				jf.addChoosableFileFilter(dcmfilter);
				jf.setFileFilter(dcmfilter);
				jf.setFileSelectionMode(jf.FILES_ONLY);
				jf.showOpenDialog(null);
				File f = jf.getSelectedFile();
				try 
				{
					DCMParser.dcmFileParser(f);
				} 
				catch (Exception e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		JButton importFolder = new JButton("Import folder");
		importFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser  jf = new JFileChooser();
				FileNameExtensionFilter dcmfilter = new FileNameExtensionFilter("Dicom file(.dcm)","dcm");
				
				jf.setCurrentDirectory(new File("."));
				jf.setDialogTitle("choose File");
				jf.setFileSelectionMode(jf.DIRECTORIES_ONLY);
				jf.setAcceptAllFileFilterUsed(false);
				jf.showOpenDialog(null);
				
			
				String directoryPath = jf.getSelectedFile().getAbsolutePath();
				System.out.println(directoryPath);
				File dcmfolder =  new File(directoryPath);
				File[] dcmfiles = dcmfolder.listFiles();
				
				
				try 
				
				{
					DCMParser.dcmFolderParser(dcmfiles);
			} 
				catch (ClassNotFoundException | IOException | SQLException e) {
					
					e.printStackTrace();
				}
			}
		});
		
		JButton btnNewButton_1 = new JButton("Exit");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(39)
					.addComponent(importFile)
					.addGap(46)
					.addComponent(importFolder)
					.addPreferredGap(ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
					.addComponent(btnNewButton_1)
					.addGap(33))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(38)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(importFile)
						.addComponent(importFolder)
						.addComponent(btnNewButton_1))
					.addContainerGap(40, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
	}
}
