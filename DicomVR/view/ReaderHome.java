package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.JTabbedPane;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.io.DicomStreamException;
import controller.DicomVRController;
import model.DCMParser;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;

import java.awt.datatransfer.Clipboard;

public class ReaderHome extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2726540739177594064L;
	private static JPanel contentPane;
	private static JTabbedPane tabbedPane;
	private static ReaderHome frame = null;
	private static JMenuItem mntmDownloadPixelData;
	private static JMenu mnCopy;
	private static JMenuItem mntmFind;
	private static JMenuItem mntmConvertToTextFile;
	static boolean changing;
	static JTree tree;
	static File dicomFile;
	static boolean flag;
	private JMenuItem mntmText;
	private JMenuItem mntmTag;
	private JMenuItem mntmTagVr;
	private JMenuItem mntmTagName;
	private JMenuItem mntmTagValue;
	private static ArrayList<File> openedFile = new ArrayList<File>();
	static JFrame frameReader;

	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	/**
	 * Launch the application.
	 */
	public static void ReaderUI() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new ReaderHome();
					frameReader = frame;
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
		Toolkit t = Toolkit.getDefaultToolkit();
		Dimension d = t.getScreenSize();
		int h = d.height;
		int w = d.width;
		setBounds(0, 0, w, h);
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

				fileChooser.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						System.out.println(e.getActionCommand());
						if (e.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {

							try {
								dicomFile = fileChooser.getSelectedFile();
								if (!openedFile.contains(dicomFile.toString())) {
									display(dicomFile);
									openedFile.add(dicomFile);
									System.out.println(openedFile.size() + "file size");
								} else {
									JOptionPane.showMessageDialog(null, "File already imported");
								}
							} catch (Exception e1) {
								JOptionPane.showMessageDialog(null, e1.getMessage(), "Error!!!",
										JOptionPane.INFORMATION_MESSAGE);
							}
						}

					}
				});
				fileChooser.showOpenDialog(null);

			}
		});
		mnFile.add(mntmOpenFile);

		mntmConvertToTextFile = new JMenuItem("Convert To Text File");
		mntmConvertToTextFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {

				JFileChooser fileChooser = new JFileChooser();
				fileChooser.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						System.out.println(e.getActionCommand());
						if (e.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {
							JPanel panel = (JPanel) tabbedPane.getSelectedComponent();
							File path = openedFile.get(tabbedPane.indexOfComponent(panel));
							try {
								DicomVRController.parseDCMFile(path);
								File textFile = fileChooser.getSelectedFile();
								PrintWriter pWriter = new PrintWriter(new File(textFile.getAbsolutePath() + ".txt"));
								Attributes tags = DicomVRController.getTagSet();
								pWriter.write(tags.toString());
								javax.swing.JOptionPane.showMessageDialog(null,
										"File saved successfully\nLocation : " + textFile.getAbsolutePath(),
										"Dicom Readaer", JOptionPane.INFORMATION_MESSAGE);
								pWriter.close();
							} catch (Exception ef) {
								JOptionPane.showMessageDialog(null, ef.getMessage(), "Error!!!",
										JOptionPane.ERROR_MESSAGE);
							}

						}
					}
				});
				FileNameExtensionFilter txtFilter = new FileNameExtensionFilter("TEXT File(.txt)", "txt", "TXT");
				fileChooser.setCurrentDirectory(new File("."));
				fileChooser.setAcceptAllFileFilterUsed(false);
				fileChooser.addChoosableFileFilter(txtFilter);
				fileChooser.setFileFilter(txtFilter);
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fileChooser.showSaveDialog(null);
				// fileChooser.showOpenDialog(null);
			}
		});

		mntmConvertToTextFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_MASK));
		mnFile.add(mntmConvertToTextFile);

		mntmDownloadPixelData = new JMenuItem("Download Pixel Data");
		mntmDownloadPixelData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Attributes tagDataSet = DicomVRController.getTagSet();
					File pixelFile = new File(dicomFile.getName() + ".txt");
					PrintWriter pWriter = new PrintWriter(pixelFile);
					byte[] bs = tagDataSet.getBytes(Tag.PixelData);
					if (bs == null) {
						javax.swing.JOptionPane.showMessageDialog(null, "Pixel data not found", "Dicom Readaer",
								JOptionPane.ERROR_MESSAGE);
					} else {
						for (byte pixeldata : bs) {
							pWriter.write(pixeldata);
						}
						pWriter.close();
						javax.swing.JOptionPane.showMessageDialog(null,
								"Downloaded successfully\nLocation : " + pixelFile.getAbsolutePath(), "Dicom Readaer",
								JOptionPane.INFORMATION_MESSAGE);
					}

				} catch (IOException e) {
					javax.swing.JOptionPane.showMessageDialog(null, "Destination file not found", "Dicom Readaer",
							JOptionPane.ERROR_MESSAGE);
				}

			}
		});
		mntmDownloadPixelData.setEnabled(false);
		mntmDownloadPixelData.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
		mnFile.add(mntmDownloadPixelData);

		mntmConvertToTextFile.setEnabled(false);
		mntmConvertToTextFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
		mnFile.add(mntmConvertToTextFile);

		JMenuItem mntmCompareFiles = new JMenuItem("Compare Files");
		mntmCompareFiles.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
		mntmCompareFiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new CompareFiles(frameReader).setVisible(true);
				setEnabled(false);
			}
		});
		mnFile.add(mntmCompareFiles);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		mnFile.add(mntmExit);

		JMenu mnEdit = new JMenu("Edit");
		mnEdit.setForeground(new Color(0, 0, 0));
		menuBar.add(mnEdit);

		mnCopy = new JMenu("Copy");
		mnCopy.setEnabled(false);
		mnEdit.add(mnCopy);

		mntmText = new JMenuItem("Text");
		mnCopy.add(mntmText);

		mntmTag = new JMenuItem("Tag");
		mnCopy.add(mntmTag);

		mntmTagVr = new JMenuItem("Tag VR");
		mnCopy.add(mntmTagVr);

		mntmTagName = new JMenuItem("Tag Name");
		mnCopy.add(mntmTagName);

		mntmTagValue = new JMenuItem("Tag Value");
		mnCopy.add(mntmTagValue);

		mntmFind = new JMenuItem("Find");
		mntmFind.setEnabled(false);

		mntmFind.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (flag == false) {
					try {
						FindDialog dialog = new FindDialog();
						dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
						dialog.setVisible(true);
						flag = true;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});

		mntmFind.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK));
		mnEdit.add(mntmFind);
		contentPane = new JPanel();
		contentPane.setForeground(new Color(0, 0, 0));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBorder(null);
		tabbedPane.setBounds(0, 0, w, h);
		contentPane.add(tabbedPane);

		mntmText.addActionListener(new ActionListener() { // TEXT
			public void actionPerformed(ActionEvent ev) {

				JPanel p = (JPanel) tabbedPane.getSelectedComponent();
				JScrollPane scroll = (JScrollPane) p.getComponent(0);
				JViewport viewport = scroll.getViewport();
				JTree tree = (JTree) viewport.getView();

				tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				if (selectedNode != null) {
					StringSelection stringSelection = new StringSelection(selectedNode.getUserObject().toString());
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					clipboard.setContents(stringSelection, null);
				}

			}
		});

		mntmTagName.addActionListener(new ActionListener() { // TAG NAME

			public void actionPerformed(ActionEvent ev) {

				JPanel p = (JPanel) tabbedPane.getSelectedComponent();
				JScrollPane scroll = (JScrollPane) p.getComponent(0);
				JViewport viewport = scroll.getViewport();
				JTree tree = (JTree) viewport.getView();

				tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				String str = selectedNode.toString();
				String tagName = str.substring(str.indexOf("<") + 1, str.indexOf(">"));
				if (selectedNode != null) {
					StringSelection stringSelection = new StringSelection(tagName);
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					clipboard.setContents(stringSelection, null);
					tree.clearSelection();
				}
			}
		});
		mntmTagValue.addActionListener(new ActionListener() { // TAG VALUE
			public void actionPerformed(ActionEvent ev) {
				JPanel p = (JPanel) tabbedPane.getSelectedComponent();
				JScrollPane scroll = (JScrollPane) p.getComponent(0);
				JViewport viewport = scroll.getViewport();
				JTree tree = (JTree) viewport.getView();

				tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				if (selectedNode != null) {
					String str = selectedNode.toString();
					String[] tagName = str.split(">");
					for (int i = 0; i < tagName.length; i++) {
						StringSelection stringSelection = new StringSelection(tagName[i]);
						Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
						clipboard.setContents(stringSelection, null);
						tree.clearSelection();
					}
				}

			}
		});
		mntmTag.addActionListener(new ActionListener() { // TAG
			public void actionPerformed(ActionEvent ev) {

				JPanel p = (JPanel) tabbedPane.getSelectedComponent();
				JScrollPane scroll = (JScrollPane) p.getComponent(0);
				JViewport viewport = scroll.getViewport();
				JTree tree = (JTree) viewport.getView();

				tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				if (selectedNode != null) {
					String str = selectedNode.toString();
					String tag = str.substring(str.indexOf("(") + 1, str.indexOf(")"));
					StringSelection stringSelection = new StringSelection(tag);
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					clipboard.setContents(stringSelection, null);
					tree.clearSelection();
				}
			}
		});
		mntmTagVr.addActionListener(new ActionListener() { // TAG VR
			public void actionPerformed(ActionEvent ev) {
				JPanel p = (JPanel) tabbedPane.getSelectedComponent();
				JScrollPane scroll = (JScrollPane) p.getComponent(0);
				JViewport viewport = scroll.getViewport();
				JTree tree = (JTree) viewport.getView();

				tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				if (selectedNode != null) {
					String str = selectedNode.toString();
					String tagVR = str.substring(str.indexOf("[") + 1, str.indexOf("]"));
					StringSelection stringSelection = new StringSelection(tagVR);
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					clipboard.setContents(stringSelection, null);
					tree.clearSelection();
				}
			}
		});

	}

	public void display(File dicomFile) {

		////////////////////////////////
		DicomVRController controllerObj;
		try {
			controllerObj = new DicomVRController(new DCMParser());
			controllerObj.parseDCMFile(dicomFile);
			ArrayList<String> patientStudyTags = DicomVRController.getPatientStudyTags();
			ArrayList<String> seriesTags = DicomVRController.getSeriesTags();
			ArrayList<String> imageTags = DicomVRController.getImageTags();

			DefaultMutableTreeNode root = new DefaultMutableTreeNode(patientStudyTags.get(5), true);
			DefaultTreeModel studyTree = new DefaultTreeModel(root);

			String patientStudyColoumns[] = { "(0010,0020) [LO] <patientId> ", "(0010,0010) [PN] <patientName> ",
					"(0010,0030) [DA] <patientDOB> ", "(0008,0050) [SH] <accessionNumber> ",
					"(0020,0010) [SH] <studyId> ", "(0008,1030) [LO] <studyDescription> ",
					"(0032,0032) [DA] <studyDateTime> " };
			String seriesColumns[] = { "(0020,000E) [UI] <seriesId> ", "(0020,0010) [SH] <studyId> ",
					"(0020,0011) [IS] <seriesNumber> ", "(0008,0060) [CS] <modality> ",
					"(0008,103E) [LO] <seriesDescription> " };
			String imageColumns[] = { "(0008,0018) [UI] <SOP Instance UID> ", "(0020,0010) [SH] <studyId> ",
					"(0020,000E)[UI] <seriesId> ", "(0008,0008) [CS] <imageType> ", "(0028,0010)[US] <rows> ",
					"(0028,0011) [US] <columns> ", "(0028,0100)[US] <bitsAllocated> ",
					"(0028,0101) [US] <bitsStored> " };
			/////////////////////////////////

			for (int i = 0; i < patientStudyTags.size(); ++i) {
				studyTree.insertNodeInto(new DefaultMutableTreeNode(
						patientStudyColoumns[i] + "[" + patientStudyTags.get(i) + "]", false), root, i);

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
							new DefaultMutableTreeNode(imageColumns[i] + "[" + imageTags.get(i) + "]", false),
							imageParent, j);
					j++;
				}
			}

			//////////////////////////////////
			tree = new JTree(studyTree);
			paintTab(tree, tabbedPane.getTabCount(), patientStudyTags.get(1) + "_" + patientStudyTags.get(5));

		} catch (DicomStreamException e) {
			javax.swing.JOptionPane.showMessageDialog(null, dicomFile.getName() + ":" + e.getMessage(), "Dicom Reader",
					JOptionPane.WARNING_MESSAGE);
		} catch (Exception e) {
			javax.swing.JOptionPane.showMessageDialog(null, "Logger initiation failed", "Dicom Reader",
					JOptionPane.ERROR_MESSAGE);
		}

	}

	public static void paintTab(JTree tree, int index, String tabName) {
		JPanel tabPanel = new JPanel(new BorderLayout());
		tabPanel.setOpaque(false);

		JScrollPane scrollPane = new JScrollPane();
		Toolkit t = Toolkit.getDefaultToolkit();
		Dimension d = t.getScreenSize();
		int h = d.height;
		int w = d.width;
		scrollPane.setBounds(0, 0, w, h);
		tabbedPane.setBounds(0, 0, w, h);
		tabPanel.add(scrollPane);

		scrollPane.setViewportView(tree);
		tabbedPane.addTab(tabName, null, tabPanel, null);
		tabPanel.setLayout(null);

		ClosableTab tabHeader = new ClosableTab(tabbedPane, index);
		tabHeader.apply();

		enableComponents();
		tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
	}

	public static void findString(String searchString) throws Exception {

		JPanel p = (JPanel) tabbedPane.getSelectedComponent();
		JScrollPane scroll = (JScrollPane) p.getComponent(0);
		JViewport viewport = scroll.getViewport();
		JTree tree = (JTree) viewport.getView();

		TreeCellColorRenderer treeColor = new TreeCellColorRenderer(searchString);
		tree.setCellRenderer(treeColor);
		if (!treeColor.isFlag() && searchString != null) {
			throw new Exception(searchString + ": no macth found");
		}

	}

	public static void enableComponents() {
		if (tabbedPane.getTabCount() > 0) {
			mntmDownloadPixelData.setEnabled(true);
			mnCopy.setEnabled(true);
			mntmFind.setEnabled(true);
			mntmConvertToTextFile.setEnabled(true);
		}

		else {
			mntmDownloadPixelData.setEnabled(false);
			mnCopy.setEnabled(false);
			mntmFind.setEnabled(false);
			mntmConvertToTextFile.setEnabled(false);
		}

	}

	 public static ArrayList<File> getOpenedFile() {
	 return openedFile;
	 }

	public static JTabbedPane getJTabbedPane() {
		return tabbedPane;
	}

	public static void convertToText(File textFile) {

	}

}