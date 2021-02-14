package view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;


import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import dao.ImageDAO;

import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.Font;

public class ImageDetails extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private JLabel lblNewLabel;

	/**
	 * Launch the application.
	 */
	

	/**
	 * Create the dialog.
	 */
	
	public ImageDetails(String study,String series,String image)
	{
		setResizable(false);
		Toolkit t = Toolkit.getDefaultToolkit();
        Dimension d = t.getScreenSize();
        int h = d.height ;
        int w = d.width;
        setBounds(w/2-917/2, h/2-481/2, 917, 400);
		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 0, 0, 0);
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		getContentPane().setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(27, 46, 856, 296);
		getContentPane().add(scrollPane);
		
		table = new JTable();
		table.setBounds(100, 100, 850, 500);
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
		
		lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(0, 0, 0, 0);
		getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Image Details");
		lblNewLabel_1.setFont(new Font("Wide Latin", Font.PLAIN, 15));
		lblNewLabel_1.setBounds(366, 11, 230, 24);
		getContentPane().add(lblNewLabel_1);
		table.setVisible(true);
		
	} catch (Exception ex) {
		ex.printStackTrace();
	}
		
	}
}
