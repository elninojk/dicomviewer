package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import view.Home.Actions;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.io.File;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.ActionListener;

public class DicomReader extends JFrame {
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DicomReader frame = new DicomReader();
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
	public DicomReader() {
		setTitle("DICOM READER");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmOpen = new JMenuItem("Open ");
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser  jf = new JFileChooser();
				FileNameExtensionFilter dcmfilter = new FileNameExtensionFilter("Dicom file(.dcm)","dcm");
				
				jf.setCurrentDirectory(new File("."));
				jf.setAcceptAllFileFilterUsed(false);
				jf.addChoosableFileFilter(dcmfilter);
				jf.setFileFilter(dcmfilter);
				jf.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jf.showOpenDialog(null);			
			}
		});
		mntmOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		
		mnFile.add(mntmOpen);
		
		JMenuItem mntmConvertToText = new JMenuItem("Convert to text file");
		mntmConvertToText.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_MASK));
		mnFile.add(mntmConvertToText);
		
		JMenuItem mntmDownloadPixelData = new JMenuItem("Download Pixel Data");
		mntmDownloadPixelData.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
		mnFile.add(mntmDownloadPixelData);
		
		JMenuItem mntmCompareFiles = new JMenuItem("Compare Files");
		mntmCompareFiles.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
		mnFile.add(mntmCompareFiles);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		mntmExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
		mnFile.add(mntmExit);
		
		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		JMenu mnNewMenu = new JMenu("Copy");
		mnEdit.add(mnNewMenu);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Find");
		mntmNewMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK));
		mnEdit.add(mntmNewMenuItem);
	}
	 
}
