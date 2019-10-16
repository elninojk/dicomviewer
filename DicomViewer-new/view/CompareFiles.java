package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import org.dcm4che3.data.Tag;
import org.dcm4che3.io.DicomInputHandler;
import org.slf4j.impl.StaticLoggerBinder;

import controller.DicomViewerController;
import model.DCMParser;

import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.JScrollPane;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.JLabel;

public class CompareFiles extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private  static JTable table;
	static DicomViewerController controller;
	static DefaultTableModel dtmCompare;
	static boolean table1Selected = false;
	static boolean tableS2elected = false;
	
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
		setBounds(100, 100, 848, 586);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		JComboBox comboBox = new JComboBox();
		comboBox.addActionListener(new ActionListener() {
			
			
			
			// START FROM HERE
			
			public void actionPerformed(ActionEvent arg0) {
				comboBox.getSelectedItem();
			}
		});
		comboBox.setBounds(27, 15, 188, 20);
		contentPanel.add(comboBox);
		
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setBounds(565, 15, 173, 20);
		contentPanel.add(comboBox_1);
		JButton btnNewButton = new JButton("New button");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser jfile1=new JFileChooser();
				
				FileNameExtensionFilter fileFilter=new FileNameExtensionFilter(".dcm", "dcm");
				jfile1.setFileFilter(fileFilter);
				jfile1.showOpenDialog(null);
				File path1 = jfile1.getSelectedFile();
				
				table.setBackground(Color.white);
				table1Selected = true;			
				tableDisplay(path1,table,1);
							
				String location = path1.getAbsolutePath();
				comboBox.addItem(location);
			}
		});
		btnNewButton.setBounds(225, 14, 23, 23);
		contentPanel.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("New button");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser jfile2=new JFileChooser();
				FileNameExtensionFilter fileFilter=new FileNameExtensionFilter(".dcm", "dcm");
				jfile2.setFileFilter(fileFilter);
				jfile2.showOpenDialog(null);
				File path1 =  jfile2.getSelectedFile();
				
				table.setBackground(Color.white);
				tableS2elected= true;
				tableDisplay(path1,table,2);
				
				String location = path1.getAbsolutePath();
				comboBox_1.addItem(location);
				
			}
		});
		btnNewButton_1.setBounds(761, 14, 23, 23);
		contentPanel.add(btnNewButton_1);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 69, 812, 375);
		contentPanel.add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		 dtmCompare = new DefaultTableModel(new String[] { "Tag", "Value", " Value" }, 0);
		table.setModel(dtmCompare);
		
		
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
	public static void tableDisplay(File path , JTable table , int coloumn)
	{
		try {
			int j =0;
			System.out.println(path);
//			DefaultTableModel dtmCompare = new DefaultTableModel(new String[] { "Tag", "Size", "Value", "Size"," Value" }, 0);
			controller.parseDCMFile(path);
			ArrayList<String> patient = new ArrayList<String>();
			ArrayList<String> series = new ArrayList<String>();
			ArrayList<String> image = new ArrayList<String>();
			patient = controller.getPatientStudyTags();
			series = controller.getSeriesTags();
			image = controller.getImageTags();
			String[] str = {"PatientID","PatientName","PatientBirthDate","AccessionNumber","StudyInstanceUID","StudyDescription","StudyDate and StudyTime","SeriesInstanceUID","StudyInstanceUID",
					"SeriesNumber","Modality","SeriesDescription","SOPInstanceUID","StudyInstanceUID","SeriesInstanceUID","ImageType","Rows","Columns","BitsAllocated","BitsStored"};
			for (int i = 0; i < patient.size(); i++) {
				Object rowSeries[] = { str[j]};
				if(dtmCompare.getRowCount() < (patient.size()+series.size()+image.size()))
						dtmCompare.addRow(rowSeries);
				dtmCompare.setValueAt(patient.get(i), j, coloumn);
//				dtmCompare.setValueAt(patient.get(i).length(), j, coloumn-1);
				table.setModel(dtmCompare);
				j++;
			
			}
			for (int i = 0; i < series.size(); i++) {
				Object rowSeries[] = {str[j]};
				if(dtmCompare.getRowCount() < (patient.size()+series.size()+image.size()))
					dtmCompare.addRow(rowSeries);
				dtmCompare.setValueAt(series.get(i), j, coloumn);
//				dtmCompare.setValueAt(series.get(i).length(), j, coloumn-1);
				table.setModel(dtmCompare);
				j++;
			
			}
			for (int i = 0; i < image.size(); i++) {
				Object rowSeries[] = { str[j]};
				if(dtmCompare.getRowCount() < (patient.size()+series.size()+image.size()))
					dtmCompare.addRow(rowSeries);
				dtmCompare.setValueAt(image.get(i), j, coloumn);
//				dtmCompare.setValueAt(image.get(i).length(), j, coloumn-1);
				table.setModel(dtmCompare);
				j++;
			
			}
			System.out.println("reach 1" );
			System.out.println(table1Selected + "  " + tableS2elected);
			if(table1Selected && tableS2elected)
			{
				table.getColumnModel().getColumn(0).setCellRenderer(new ColoredTableCellRenderer() );;
				table.getColumnModel().getColumn(1).setCellRenderer(new ColoredTableCellRenderer() );;
				table.getColumnModel().getColumn(2).setCellRenderer(new ColoredTableCellRenderer() );;
				table.setBackground(Color.green);
				
				
			
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
//	public static void compare()
//	{	
//		int missMatch = 0;
//		for(int i= 0; i< dtmCompare.getRowCount();i++)
//		{
//			try
//			{	
//				
//				if(!(dtmCompare.getValueAt(i,1).equals(dtmCompare.getValueAt(i,2))))
//				{
//					
//					System.out.println(dtmCompare.getValueAt(i,1) + "   "+ dtmCompare.getValueAt(i,2));
////					dtmCompare.set
//					
//					table.getColumnModel().getColumn(0).setCellRenderer(new ColoredTableCellRenderer() );;
//					
//					
//					
//					
//				}
//			}
//			catch(Exception e)
//			{
//				e.printStackTrace();
//			}
//			}
//				
//		}
}

class ColoredTableCellRenderer
extends DefaultTableCellRenderer
{
public  Component getTableCellRendererComponent
    (JTable table, Object value, boolean selected, boolean focused, int row, int column)
{
    setEnabled(table == null || table.isEnabled()); // see question above
    
    if(table.getModel().getValueAt(row,1) != null && table.getModel().getValueAt(row,2) != null){
    	if(!(table.getModel().getValueAt(row,1).equals(table.getModel().getValueAt(row,2))))
        {
    		
        	System.out.println(row);
            setBackground(Color.yellow);
        }
        else
        	
            setBackground(Color.green);
    	
    }

    else
        setBackground(Color.pink);
    

    super.getTableCellRendererComponent(table, value, selected, focused, row, column);

    return this;
}
}


//class ColoredTableCellRenderer
//extends DefaultTableCellRenderer
//{
//public Component getTableCellRendererComponent
//    (JTable table, Object value, boolean selected, boolean focused, int row, int column)
//{
//    setEnabled(table == null || table.isEnabled()); // see question above
//
//    if ((row % 2) == 0)
//        setBackground(Color.green);
//    else
//        setBackground(Color.lightGray);
//
//    super.getTableCellRendererComponent(table, value, selected, focused, row, column);
//
//    return this;
//}
//}

