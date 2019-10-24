package view;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;

public class FindDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JLabel lblNewLabel;

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
		this.setLocation(800, 150);

		lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(21, 42, 180, 23);
		lblNewLabel.setText("");
		contentPanel.add(lblNewLabel);
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				buttonPane.setLayout(null);
			}
		}

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
					lblNewLabel.setText("");
					if ("".equals(textField.getText()) || textField.getText() == null) {
						lblNewLabel.setText("Empty String");
						lblNewLabel.setVisible(true);
					} else {
						
						try {
							ReaderHome.findString(textField.getText());
						} catch (Exception e) {
							lblNewLabel.setText("String Not Found");;
						}
					}
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
				try {
					ReaderHome.findString(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
				dispose();
			}
		});

	}

	
}
