package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JToolBar;
import javax.swing.JScrollBar;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import java.awt.Color;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.JTabbedPane;
import javax.swing.tree.DefaultTreeModel;

import dao.PatientStudyDAO;
import model.PatientStudy;
import dao.ImageDAO;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JScrollPane;

public class ReaderHome extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ReaderHome frame = new ReaderHome();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws Exception 
	 */
	public ReaderHome() throws Exception {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1058, 667);
		this.setTitle("DICOM Reader");

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(UIManager.getColor("CheckBox.background"));
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		mnFile.setForeground(new Color(0, 0, 0));
		menuBar.add(mnFile);

		JMenuItem mntmOpenFile = new JMenuItem("Open File");
		mnFile.add(mntmOpenFile);

		JMenuItem mntmConvertToTextFile = new JMenuItem("Convert To Text File");
		mnFile.add(mntmConvertToTextFile);

		JMenuItem mntmDownloadPixelData = new JMenuItem("Download Pixel Data");
		mnFile.add(mntmDownloadPixelData);

		JMenuItem mntmCompareFiles = new JMenuItem("Compare Files");
		mnFile.add(mntmCompareFiles);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);

		JMenu mnEdit = new JMenu("Edit");
		mnEdit.setForeground(new Color(0, 0, 0));
		menuBar.add(mnEdit);

		JMenu mnCopy = new JMenu("Copy");
		mnEdit.add(mnCopy);

		JMenuItem mntmText = new JMenuItem("Text");
		mnCopy.add(mntmText);

		JMenuItem mntmTag = new JMenuItem("Tag");
		mnCopy.add(mntmTag);

		JMenuItem mntmTagVr = new JMenuItem("Tag VR");
		mnCopy.add(mntmTagVr);

		JMenuItem mntmTagName = new JMenuItem("Tag Name");
		mnCopy.add(mntmTagName);

		JMenuItem mntmTagValue = new JMenuItem("Tag Value");
		mnCopy.add(mntmTagValue);

		JMenuItem mntmFind = new JMenuItem("Find");
		mnEdit.add(mntmFind);
		contentPane = new JPanel();
		contentPane.setForeground(new Color(0, 0, 0));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBorder(null);
		tabbedPane.setBounds(0, 0, 1042, 607);
		contentPane.add(tabbedPane);

		// DEMO
//		JPanel panelDemo = new JPanel();
//		tabbedPane.addTab("Demo", null, panelDemo, null);
//		panelDemo.setLayout(null);
//		DefaultTreeModel studyTree = null;
//		try {
//			studyTree = PatientStudyDAO.getStudyTree("std1");
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		JScrollPane scrollPane = new JScrollPane();
//		scrollPane.setBounds(0, 0, 1037, 579);
//		panelDemo.add(scrollPane);
//		JTree tree = new JTree(studyTree);
//		scrollPane.setViewportView(tree);

		
		
		
		
		
		
		
		
		List<PatientStudy> studyList = null;
		try {
			studyList = PatientStudyDAO.viewAllPatientStudy();
		} catch (Exception ex) {
			javax.swing.JOptionPane.showMessageDialog(new JFrame(), "dicom files not found", "Dicom viewer", 2);
		}

		for (int i = 0; i < studyList.size(); i++) {

			String studyRow[] = { studyList.get(i).getPatientId(), studyList.get(i).getPatientName(),
					studyList.get(i).getPatientDOB(), studyList.get(i).getAccessionNumber(),
					studyList.get(i).getStudyId(), studyList.get(i).getStudyDescription(),
					studyList.get(i).getStudyDateTime() };

			JPanel panelDemo = new JPanel();
			tabbedPane.addTab(studyRow[1], null, panelDemo, null);
			panelDemo.setLayout(null);
			DefaultTreeModel studyTree = null;
			try {
				studyTree = PatientStudyDAO.getStudyTree(studyRow[0]);
			} catch (ClassNotFoundException e) {
				throw e;
			} catch (SQLException e) {
				throw e;
			} catch (Exception e) {
				throw e;
			}
			
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(0, 0, 1037, 579);
			panelDemo.add(scrollPane);
			JTree tree = new JTree(studyTree);
			scrollPane.setViewportView(tree);

		}

	}
}
