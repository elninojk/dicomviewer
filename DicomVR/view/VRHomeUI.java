package view;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.ImageIcon;
import java.awt.Color;

public class VRHomeUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5594410933191266763L;
	private JPanel contentPane;
	static VRHomeUI frame ;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				
					frame = new VRHomeUI();
					frame.setVisible(true);
				
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public VRHomeUI() {
		setTitle("Dicom VR");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Toolkit t = Toolkit.getDefaultToolkit();
		Dimension d = t.getScreenSize();
		int h = d.height;
		int w = d.width;
		setBounds(w/2-250, h/2-200, 500, 235);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JButton btnViewer = new JButton("Viewer");
		btnViewer.setForeground(Color.WHITE);
		btnViewer.setBackground(Color.BLACK);
		btnViewer.setFont(new Font("Microsoft Tai Le", Font.BOLD, 14));
		btnViewer.setBounds(32, 96, 115, 39);
		btnViewer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {				
					Viewer.ViewerUI();
				
			}
		});
		
		JButton btnReader = new JButton("Reader");
		btnReader.setBackground(Color.BLACK);
		btnReader.setForeground(Color.WHITE);
		btnReader.setFont(new Font("Microsoft Tai Le", Font.BOLD, 14));
		btnReader.setBounds(186, 96, 124, 39);
		btnReader.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ReaderHome.ReaderUI();
			}
		});
		contentPane.setLayout(null);
		contentPane.add(btnViewer);
		contentPane.add(btnReader);
		
		JLabel lblNewLabel = new JLabel("DICOM VR");
		lblNewLabel.setForeground(Color.BLACK);
		lblNewLabel.setFont(new Font("Microsoft Tai Le", Font.BOLD, 26));
		lblNewLabel.setBounds(10, 11, 166, 39);
		contentPane.add(lblNewLabel);
		
		JButton btnExit = new JButton("Exit");
		btnExit.setBackground(Color.BLACK);
		btnExit.setForeground(Color.WHITE);
		btnExit.setFont(new Font("Microsoft Tai Le", Font.BOLD, 14));
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnExit.setBounds(341, 96, 124, 39);
		contentPane.add(btnExit);
		
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon(VRHomeUI.class.getResource("/view/images (1).jpg")));
		lblNewLabel_1.setBounds(-154, -17, 718, 295);
		contentPane.add(lblNewLabel_1);
	}
}
