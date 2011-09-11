import java.awt.*;
import java.awt.event.*;

import javax.swing.*;



/**
 * @author Tsu
 */
public class editmyprofile extends JFrame {
	private BuddyList bl;
	public editmyprofile(final BuddyListGui blg) {
		this.bl = blg.bl;
		this.addWindowListener(new WindowListener() {
            public void windowClosed(WindowEvent e) {}
            public void windowActivated(WindowEvent e) {}
            public void windowClosing(WindowEvent e) {
            	System .out.print("fgjdshhfhje");
            	blg.setEnabled(true);
            }
            public void windowDeactivated(WindowEvent e) {}
            public void windowDeiconified(WindowEvent e) {}
            public void windowIconified(WindowEvent e) {}
            public void windowOpened(WindowEvent e) {}
        });
		initComponents();
		this.setSize(400, 180);
	}

	private void button3ActionPerformed(ActionEvent e) {
		// TODO add your code here
		bl.tc.setProfile_name(textField1.getText());
		bl.tc.setProfile_text(textPane1.getText());
		this.dispose();
    	bl.blg.setEnabled(true);
    	bl.blg.setVisible(true);
	}

	private void button4ActionPerformed(ActionEvent e) {
		// TODO add your code here
		this.dispose();
    	bl.blg.setEnabled(true);
    	bl.blg.setVisible(true);
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		panel1 = new JPanel();
		button1 = new JButton();
		button2 = new JButton();
		label1 = new JLabel();
		textField1 = new JTextField();
		label2 = new JLabel();
		scrollPane1 = new JScrollPane();
		textPane1 = new JTextPane();
		button3 = new JButton();
		button4 = new JButton();

		//======== this ========
		Container contentPane = getContentPane();

		//======== panel1 ========
		{
			panel1.setMinimumSize(new Dimension(64, 64));
			panel1.setMaximumSize(new Dimension(64, 64));
			panel1.setPreferredSize(new Dimension(64, 64));

			GroupLayout panel1Layout = new GroupLayout(panel1);
			panel1.setLayout(panel1Layout);
			panel1Layout.setHorizontalGroup(
				panel1Layout.createParallelGroup()
					.addGap(0, 64, Short.MAX_VALUE)
			);
			panel1Layout.setVerticalGroup(
				panel1Layout.createParallelGroup()
					.addGap(0, 64, Short.MAX_VALUE)
			);
		}

		//---- button1 ----
		button1.setText("Set Avatar");

		//---- button2 ----
		button2.setText("Remove Avatar");

		//---- label1 ----
		label1.setText("Name");

		//---- label2 ----
		label2.setText("Text");

		//======== scrollPane1 ========
		{
			scrollPane1.setViewportView(textPane1);
		}

		//---- button3 ----
		button3.setText("Ok");
		button3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				button3ActionPerformed(e);
			}
		});

		//---- button4 ----
		button4.setText("Cancel");
		button4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				button4ActionPerformed(e);
			}
		});

		GroupLayout contentPaneLayout = new GroupLayout(contentPane);
		contentPane.setLayout(contentPaneLayout);
		contentPaneLayout.setHorizontalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(contentPaneLayout.createSequentialGroup()
					.addGroup(contentPaneLayout.createParallelGroup()
						.addGroup(contentPaneLayout.createSequentialGroup()
							.addGap(31, 31, 31)
							.addComponent(panel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addGroup(contentPaneLayout.createParallelGroup()
								.addComponent(label1)
								.addComponent(label2)))
						.addGroup(contentPaneLayout.createSequentialGroup()
							.addContainerGap()
							.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
								.addComponent(button1, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(button2, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addGroup(contentPaneLayout.createParallelGroup()
						.addGroup(contentPaneLayout.createSequentialGroup()
							.addComponent(button4)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(button3, GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE))
						.addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE)
						.addComponent(textField1, GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE))
					.addContainerGap())
		);
		contentPaneLayout.setVerticalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(contentPaneLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(contentPaneLayout.createParallelGroup()
						.addGroup(contentPaneLayout.createSequentialGroup()
							.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(label1)
								.addComponent(textField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addGroup(contentPaneLayout.createParallelGroup()
								.addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
								.addComponent(label2)))
						.addGroup(contentPaneLayout.createSequentialGroup()
							.addComponent(panel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(button1)))
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(button2)
						.addComponent(button3)
						.addComponent(button4))
					.addGap(96, 96, 96))
		);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JPanel panel1;
	private JButton button1;
	private JButton button2;
	private JLabel label1;
	private JTextField textField1;
	private JLabel label2;
	private JScrollPane scrollPane1;
	private JTextPane textPane1;
	private JButton button3;
	private JButton button4;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
