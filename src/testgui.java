import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle;
/*
 * Created by JFormDesigner on Fri Aug 26 23:59:36 EST 2011
 */



/**
 * @author Tsu
 */
public class testgui extends JFrame {
	public testgui() {
		initComponents();
	}

	private void panel2PropertyChange(PropertyChangeEvent e) {
		// TODO add your code here
		System.out.println(e);
	}

	private void textArea2KeyReleased(KeyEvent e) {
		// TODO add your code here
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		splitPane1 = new JSplitPane();
		panel1 = new JPanel();
		textField1 = new JTextField();
		label13 = new JLabel();
		label14 = new JLabel();
		label15 = new JLabel();
		label16 = new JLabel();
		panel2 = new JPanel();
		textArea1 = new JTextArea();
		textArea2 = new JTextArea();

		//======== this ========
		setFocusableWindowState(false);
		Container contentPane = getContentPane();

		//======== splitPane1 ========
		{
			splitPane1.setOrientation(JSplitPane.VERTICAL_SPLIT);

			//======== panel1 ========
			{
				panel1.setPreferredSize(new Dimension(84, 86));
				panel1.setMaximumSize(new Dimension(84, 86));

				//---- textField1 ----
				textField1.setMinimumSize(new Dimension(64, 64));
				textField1.setMaximumSize(new Dimension(64, 64));
				textField1.setPreferredSize(new Dimension(64, 64));

				//---- label13 ----
				label13.setText("profile_text");
				label13.setVerticalAlignment(SwingConstants.TOP);
				label13.setHorizontalAlignment(SwingConstants.TRAILING);

				//---- label14 ----
				label14.setText("address");

				//---- label15 ----
				label15.setText("name");

				//---- label16 ----
				label16.setText("client");

				GroupLayout panel1Layout = new GroupLayout(panel1);
				panel1.setLayout(panel1Layout);
				panel1Layout.setHorizontalGroup(
					panel1Layout.createParallelGroup()
						.addGroup(panel1Layout.createSequentialGroup()
							.addContainerGap()
							.addComponent(textField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addGroup(panel1Layout.createParallelGroup()
								.addComponent(label14)
								.addComponent(label16)
								.addComponent(label15))
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(label13, GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
							.addContainerGap())
				);
				panel1Layout.setVerticalGroup(
					panel1Layout.createParallelGroup()
						.addGroup(panel1Layout.createSequentialGroup()
							.addContainerGap()
							.addGroup(panel1Layout.createParallelGroup()
								.addGroup(panel1Layout.createSequentialGroup()
									.addComponent(label14)
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
									.addComponent(label16))
								.addComponent(label13, GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
								.addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
									.addComponent(textField1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(label15)))
							.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				);
			}
			splitPane1.setTopComponent(panel1);

			//======== panel2 ========
			{

				//---- textArea1 ----
				textArea1.setEditable(false);

				//---- textArea2 ----
				textArea2.setText("ddsfsdfs");
				textArea2.addKeyListener(new KeyAdapter() {
					@Override
					public void keyReleased(KeyEvent e) {
						textArea2KeyReleased(e);
					}
				});

				GroupLayout panel2Layout = new GroupLayout(panel2);
				panel2.setLayout(panel2Layout);
				panel2Layout.setHorizontalGroup(
					panel2Layout.createParallelGroup()
						.addComponent(textArea1, GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
						.addComponent(textArea2, GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
				);
				panel2Layout.setVerticalGroup(
					panel2Layout.createParallelGroup()
						.addGroup(GroupLayout.Alignment.TRAILING, panel2Layout.createSequentialGroup()
							.addComponent(textArea1, GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(textArea2, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE))
				);
			}
			splitPane1.setBottomComponent(panel2);
		}

		GroupLayout contentPaneLayout = new GroupLayout(contentPane);
		contentPane.setLayout(contentPaneLayout);
		contentPaneLayout.setHorizontalGroup(
			contentPaneLayout.createParallelGroup()
				.addComponent(splitPane1, GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE)
		);
		contentPaneLayout.setVerticalGroup(
			contentPaneLayout.createParallelGroup()
				.addComponent(splitPane1, GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE)
		);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JSplitPane splitPane1;
	private JPanel panel1;
	private JTextField textField1;
	private JLabel label13;
	private JLabel label14;
	private JLabel label15;
	private JLabel label16;
	private JPanel panel2;
	private JTextArea textArea1;
	private JTextArea textArea2;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
