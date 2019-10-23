package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.datatransfer.StringSelection;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.JToolBar;
import javax.swing.JScrollBar;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import java.awt.Color;
import java.awt.Desktop;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.JTabbedPane;
import javax.swing.tree.DefaultTreeModel;

import com.sun.javafx.tk.Toolkit;

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
	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	protected JTabbedPane tabbedPane;
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
				} catch (NullPointerException e1) {

				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Error!!!", JOptionPane.INFORMATION_MESSAGE);
					e1.printStackTrace();
				}
			}
		});
		mnFile.add(mntmOpenFile);

		JMenuItem mntmConvertToTextFile = new JMenuItem("Convert To Text File");
		mntmConvertToTextFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				JFileChooser fileChooser = new JFileChooser();
				int returnValue = fileChooser.showOpenDialog(null);
				File dicomFile = fileChooser.getSelectedFile();
				try {
					File textFile = ConvertToText.convertToText(dicomFile);
					Desktop.getDesktop().open(textFile);
				} catch (NullPointerException e1) {

				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Error!!!", JOptionPane.INFORMATION_MESSAGE);
					e1.printStackTrace();
				}
			}
		});
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

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBorder(null);
		tabbedPane.setBounds(0, 0, 1042, 607);
		contentPane.add(tabbedPane);

	}

	public void display(File dicomFile) throws Exception {

		//////////////

		DicomViewerController controllerObj = new DicomViewerController(new DCMParser());
		controllerObj.parseDCMFile(dicomFile);
		ArrayList<String> patientStudyTags = controllerObj.getPatientStudyTags();
		ArrayList<String> seriesTags = controllerObj.getSeriesTags();
		ArrayList<String> imageTags = controllerObj.getImageTags();

		DefaultMutableTreeNode root = new DefaultMutableTreeNode(patientStudyTags.get(5), true);
		DefaultTreeModel studyTree = new DefaultTreeModel(root);

		String patientStudyColoumns[] = { "(0010,0020) [LO] <patientId> ", "(0010,0010) [PN] <patientName> ",
				"(0010,0030) [DA] <patientDOB> ", "(0008,0050) [SH] <accessionNumber> ", "(0020,0010) [SH] <studyId> ",
				"(0008,1030) [LO] <studyDescription> ", "(0032,0032) [DA] <studyDateTime> " };
		String seriesColumns[] = { "(0020,000E) [UI] <seriesId> ", "(0020,0010) [SH] <studyId> ",
				"(0020,0011) [IS] <seriesNumber> ", "(0008,0060) [CS] <modality> ",
				"(0008,103E) [LO] <seriesDescription> " };
		String imageColumns[] = { "(0008,0018) [UI] <SOP Instance UID> ", "(0020,0010) [SH] <studyId> ",
				"(0020,000E)[UI] <seriesId> ", "(0008,0008) [CS] <imageType> ", "(0028,0010)[US] <rows> ",
				"(0028,0011) [US] <columns> ", "(0028,0100)[US] <bitsAllocated> ", "(0028,0101) [US] <bitsStored> " };
		/////////////////////////////////
		JPanel tabPanel = new JPanel(new BorderLayout());
		tabPanel.setOpaque(false);
		JButton tabButton = new JButton("X");
		
        tabPanel.setOpaque(false);
        
        //make JLabel read titles from JTabbedPane
//        JLabel label = new JLabel() {
//            public String getText() {
//                int i = pane.indexOfTabComponent(ButtonTabComponent.this);
//                if (i != -1) {
//                    return pane.getTitleAt(i);
//                }
//                return null;
//            }

		int size = 17;
		tabButton.setPreferredSize(new Dimension(size, size));
		tabButton.setToolTipText("close this tab");
		// Make the button looks the same for all Laf's
		tabButton.setUI(new BasicButtonUI());
		// Make it transparent
		tabButton.setContentAreaFilled(false);
		// No need to be focusable
		tabButton.setFocusable(false);
		tabButton.setBorder(BorderFactory.createEtchedBorder());
		tabButton.setBorderPainted(false);
		// Making nice rollover effect
		// we use the same listener for all buttons
		////////// tabButton.addMouseListener(buttonMouseListener);
		tabButton.setRolloverEnabled(true);
		// Close the proper tab by clicking the button
		////////// tabButton.addActionListener(this);

		tabPanel.add(tabButton);
		tabbedPane.addTab(patientStudyTags.get(1) + "_" + patientStudyTags.get(5), null, tabPanel, null);
		tabPanel.setLayout(null);

		///////////////////////////////////
		for (int i = 0; i < patientStudyTags.size(); ++i) {
			studyTree.insertNodeInto(
					new DefaultMutableTreeNode(patientStudyColoumns[i] + "[" + patientStudyTags.get(i) + "]", false),
					root, i);

		}

		DefaultMutableTreeNode seriesParent = new DefaultMutableTreeNode(seriesTags.get(0), true);
		studyTree.insertNodeInto(seriesParent, root, patientStudyTags.size());
		for (int i = 0, j = 0; i < seriesTags.size(); ++i) {
			if (i != 1) {
				studyTree.insertNodeInto(
						new DefaultMutableTreeNode(seriesColumns[i] + "[" + seriesTags.get(i) + "]", false),
						seriesParent, j);
				j++;
			}
		}

		DefaultMutableTreeNode imageParent = new DefaultMutableTreeNode(imageTags.get(0), true);
		studyTree.insertNodeInto(imageParent, seriesParent, seriesTags.size() - 1);

		for (int i = 0, j = 0; i < imageTags.size(); ++i) {
			if (i != 1 && i != 2) {
				studyTree.insertNodeInto(
						new DefaultMutableTreeNode(imageColumns[i] + "[" + imageTags.get(i) + "]", false), imageParent,
						j);
				j++;
			}
		}
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 1037, 579);
		tabPanel.add(scrollPane);
		JTree tree = new JTree(studyTree);
		scrollPane.setViewportView(tree);
		tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);

		//////////////
		
//		tree.setShowsRootHandles(true);
//		tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
//
//			public void valueChanged(TreeSelectionEvent e) {
//				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
//				StringSelection stringSelection = new StringSelection("");
//				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
//				 
//				mntmText.addActionListener(new ActionListener() { // TEXT
//					public void actionPerformed(ActionEvent ev) {
//						
//						StringSelection stringSelection = new StringSelection(selectedNode.getUserObject().toString());
//						Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
//						clipboard.setContents(stringSelection, null);
//				
//					}
//				});
//				mntmTagName.addActionListener(new ActionListener() { // TAG NAME
//					public void actionPerformed(ActionEvent ev) {
//
//						String str = selectedNode.toString();
//						String tagName = str.substring(str.indexOf("<") + 1, str.indexOf(">"));
//						StringSelection stringSelection = new StringSelection(tagName);
//						Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
//						clipboard.setContents(stringSelection, null);
//
//					}
//				});
//				mntmTagValue.addActionListener(new ActionListener() { // TAG VALUE
//					public void actionPerformed(ActionEvent ev) {
//
//						String str = selectedNode.toString();
//						String[] tagName = str.split(">");
//						for (int i = 0; i < tagName.length; i++) {
//
//							StringSelection stringSelection = new StringSelection(tagName[i]);
//							Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
//							clipboard.setContents(stringSelection, null);
//						}
//
//					}
//				});
//				mntmTag.addActionListener(new ActionListener() { // TAG
//					public void actionPerformed(ActionEvent ev) {
//
//						String str = selectedNode.toString();
//						String tag = str.substring(str.indexOf("(") + 1, str.indexOf(")"));
//						StringSelection stringSelection = new StringSelection(tag);
//						Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
//						clipboard.setContents(stringSelection, null);
//
//					}
//				});
//				mntmTagVr.addActionListener(new ActionListener() { // TAG VR
//					public void actionPerformed(ActionEvent ev) {
//
//						String str = selectedNode.toString();
//						String tagVR = str.substring(str.indexOf("[") + 1, str.indexOf("]"));
//						StringSelection stringSelection = new StringSelection(tagVR);
//						Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
//						clipboard.setContents(stringSelection, null);
//
//					}
//				});
//
//			}
//		});

	}
}
