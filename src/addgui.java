import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;



/**
 * @author Tsu
 */
public class addgui extends JFrame {
	private BuddyList bl;
	private Buddy b;
	public addgui(final BuddyListGui blg) {
		this.bl = blg.bl;
		this.addWindowListener(new WindowListener() {
            public void windowClosed(WindowEvent e) {}
            public void windowActivated(WindowEvent e) {}
            public void windowClosing(WindowEvent e) {
            	blg.setEnabled(true);
            }
            public void windowDeactivated(WindowEvent e) {}
            public void windowDeiconified(WindowEvent e) {}
            public void windowIconified(WindowEvent e) {}
            public void windowOpened(WindowEvent e) {}
        });
		initComponents();
	}

	public addgui(BuddyListGui blg, Buddy b) {
		// TODO Auto-generated constructor stub
		this(blg);
		this.b = b;
		textField1.setText(b.getAddress());
		textField2.setText(b.getName());
	}

	private void button1ActionPerformed(ActionEvent e) {
		System.out.println("a");
		if (!textField1.getText().equals("")) {
			System.out.println("z");
			if (b != null) {
				System.out.println("y");
				b.setAddress(textField1.getText());
				b.setName(textField2.getText());
			} else {
				System.out.println("x");
				if (!bl.hasBuddy(textField1.getText())) {
					Buddy b = new Buddy(textField1.getText(), textField2.getText(), false, bl, bl.tc);
					bl.addBuddy(b, true);
					try {
						bl.saveBuddies();
					} catch (IOException ee) {
						ee.printStackTrace();
					}
				}
			}

			this.dispose();
	    	bl.blg.setEnabled(true);
	    	bl.blg.setVisible(true);
		}
	}

	private void button2ActionPerformed(ActionEvent e) {
		this.dispose();
    	bl.blg.setEnabled(true);
    	bl.blg.setVisible(true);
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		label1 = new JLabel();
		label2 = new JLabel();
		textField1 = new JTextField();
		textField2 = new JTextField();
		button1 = new JButton();
		button2 = new JButton();

		//======== this ========
		Container contentPane = getContentPane();

		//---- label1 ----
		label1.setText("TorChat ID");

		//---- label2 ----
		label2.setText("Display Name");

		//---- button1 ----
		button1.setText("Ok");
		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				button1ActionPerformed(e);
			}
		});

		//---- button2 ----
		button2.setText("Cancel");
		button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				button2ActionPerformed(e);
			}
		});

		GroupLayout contentPaneLayout = new GroupLayout(contentPane);
		contentPane.setLayout(contentPaneLayout);
		contentPaneLayout.setHorizontalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(contentPaneLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(contentPaneLayout.createParallelGroup()
						.addComponent(label2)
						.addComponent(label1))
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addGroup(contentPaneLayout.createParallelGroup()
						.addGroup(contentPaneLayout.createSequentialGroup()
							.addComponent(button2)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(button1, GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE))
						.addComponent(textField2, GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
						.addComponent(textField1, GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE))
					.addContainerGap())
		);
		contentPaneLayout.setVerticalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(contentPaneLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(textField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(label1))
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(textField2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(label2))
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
					.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(button1)
						.addComponent(button2))
					.addContainerGap())
		);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JLabel label1;
	private JLabel label2;
	private JTextField textField1;
	private JTextField textField2;
	private JButton button1;
	private JButton button2;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
