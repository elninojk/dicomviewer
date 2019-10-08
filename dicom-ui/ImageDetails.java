package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;


import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import daoclasses.ImageDAO;

import javax.swing.JTable;
import javax.swing.JScrollPane;

public class ImageDetails extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ImageDetails dialog = new ImageDetails();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ImageDetails() {
		
	}
	public ImageDetails(String study,String series,String image)
	{
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		getContentPane().setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 414, 239);
		getContentPane().add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		DefaultTableModel dtm=new DefaultTableModel();
		table.setModel(dtm);
		dtm.addColumn("Description");
		dtm.addColumn("Value");
		
		
		
		try {
		
		LinkedHashMap<String, String> imgDetails = ImageDAO.details(study, series, image);
		Set<String> keys = imgDetails.keySet();
		Iterator<String> itr = keys.iterator();
		while(itr.hasNext()) {
			String key = (String)itr.next();
			String value=(String)imgDetails.get(key);
			//System.out.println(key + " " + imgDetails.get(key) + "\n");
			dtm.addRow(new Object[] {key,value});
		}
		
	
		table.setModel(dtm);
		table.setVisible(true);
		
	} catch (Exception ex) {
		ex.printStackTrace();
	}
		
	}
}
