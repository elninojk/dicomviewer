package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JLabel;

public class FindDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			FindDialog dialog = new FindDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public FindDialog() {
		this.setTitle("Find");
		setBounds(100, 100, 373, 114);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		this.setAlwaysOnTop(true);
		
		textField = new JTextField();
		textField.setBounds(21, 11, 180, 20);
		contentPanel.add(textField);
		textField.setColumns(10);
		{
			JButton findButton = new JButton("Find");
			findButton.setBounds(238, 10, 96, 23);
			contentPanel.add(findButton);
			findButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					////////////////
					ReaderHome.findString(textField.getText(), 1);
					///////////////
				}
			});
			findButton.setActionCommand("OK");
			getRootPane().setDefaultButton(findButton);
		}
		JButton closeButton = new JButton("Close");
		closeButton.setBounds(238, 44, 96, 23);
		contentPanel.add(closeButton);
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(21, 42, 180, 23);
		lblNewLabel.setText("4/6");
		contentPanel.add(lblNewLabel);
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				buttonPane.setLayout(null);
			}
		}
	}
}
