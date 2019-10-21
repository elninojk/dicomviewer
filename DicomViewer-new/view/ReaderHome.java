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
import javax.swing.JFileChooser;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import java.awt.Color;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.JTabbedPane;
import javax.swing.tree.DefaultTreeModel;

import controller.DicomViewerController;
import dao.PatientStudyDAO;
import model.DCMParser;
import model.PatientStudy;
import dao.ImageDAO;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;

public class ReaderHome extends JFrame {

	private JPanel contentPane;
	static ReaderHome frame = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new ReaderHome();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * 
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
		mntmOpenFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mntmOpenFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int returnValue = fileChooser.showOpenDialog(null);
				File dicomFile = fileChooser.getSelectedFile();
				try {
					display(dicomFile);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Error!!!" , JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}) ;
		mnFile.add(mntmOpenFile);

		JMenuItem mntmConvertToTextFile = new JMenuItem("Convert To Text File");
		mntmConvertToTextFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_MASK));
		mnFile.add(mntmConvertToTextFile);

		JMenuItem mntmDownloadPixelData = new JMenuItem("Download Pixel Data");
		mntmDownloadPixelData.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
		mnFile.add(mntmDownloadPixelData);

		JMenuItem mntmCompareFiles = new JMenuItem("Compare Files");
		mntmCompareFiles.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
		mntmCompareFiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new CompareFiles().setVisible(true);
			}
		});
		mnFile.add(mntmCompareFiles);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					System.out.print("Exiting...");
					frame.dispose();
			}
		});
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
		mntmFind.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK));
		mnEdit.add(mntmFind);
		contentPane = new JPanel();
		contentPane.setForeground(new Color(0, 0, 0));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

	}

	public void display(File dicomFile) throws Exception {

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBorder(null);
		tabbedPane.setBounds(0, 0, 1042, 607);
		contentPane.add(tabbedPane);

		//////////////

		DicomViewerController controllerObj = new DicomViewerController(new DCMParser());
		controllerObj.parseDCMFile(dicomFile);
		ArrayList<String> patientStudyTags = controllerObj.getPatientStudyTags();
		ArrayList<String> seriesTags = controllerObj.getSeriesTags();
		ArrayList<String> imageTags = controllerObj.getImageTags();

		DefaultMutableTreeNode root = new DefaultMutableTreeNode(patientStudyTags.get(5), true);
		DefaultTreeModel studyTree = new DefaultTreeModel(root);

		String patientStudyColoumns[] = { "(0010,0020) [LO] <patientId>", "(0010,0010) [PN] <patientName>",
				"(0010,0030) [DA] <patientDOB>", "(0008,0050) [SH] <accessionNumber>", "(0020,0010) [SH] <studyId>",
				"(0008,1030) [LO] <studyDescription>", "(0032,0032) [DA] <studyDateTime>" };
		String seriesColumns[] = { "(0020,000E) [UI] <seriesId>", "(0020,0010) [SH] <studyId>",
				"(0020,0011) [IS] <seriesNumber>", "(0008,0060) [CS] <modality>",
				"(0008,103E) [LO] <seriesDescription>" };
		String imageColumns[] = { "(0008,0018) [UI] <SOP Instance UID>", "(0020,0010) [SH] <studyId>",
				"(0020,000E)[UI] <seriesId>", "(0008,0008) [CS] <imageType>", "(0028,0010)[US] <rows>",
				"(0028,0011) [US] <columns>", "(0028,0100)[US] <bitsAllocated>", "(0028,0101) [US] <bitsStored>" };

		JPanel panelDemo = new JPanel();
		tabbedPane.addTab(patientStudyTags.get(1) + "_" + patientStudyTags.get(5), null, panelDemo, null);
		panelDemo.setLayout(null);

		for (int i = 0; i < patientStudyTags.size(); ++i) {
			studyTree.insertNodeInto(
					new DefaultMutableTreeNode(patientStudyColoumns[i] + "[" + patientStudyTags.get(i) + "]", false),
					root, i);
		}
		DefaultMutableTreeNode seriesParent = new DefaultMutableTreeNode(seriesTags.get(0), true);
		studyTree.insertNodeInto(seriesParent, root, patientStudyTags.size());
		for (int i = 0, j = 0; i < seriesTags.size(); ++i) {
			while (i != 1) {
				studyTree.insertNodeInto(
						new DefaultMutableTreeNode(seriesColumns[i] + "[" + seriesTags.get(i) + "]", false),
						seriesParent, j);
				j++;
			}
		}

		DefaultMutableTreeNode imageParent = new DefaultMutableTreeNode(imageTags.get(0), true);
		studyTree.insertNodeInto(imageParent, seriesParent, seriesTags.size() - 1);
		for (int i = 0, j = 0; i < imageTags.size(); ++i) {
			while (i != 1 || i != 2) {
				studyTree.insertNodeInto(
						new DefaultMutableTreeNode(imageColumns[i] + "[" + seriesTags.get(i) + "]", false),
						seriesParent, j);
				j++;
			}
		}
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 1037, 579);
		panelDemo.add(scrollPane);
		JTree tree = new JTree(studyTree);
		scrollPane.setViewportView(tree);

		//////////////
		
	}
}
