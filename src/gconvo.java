import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;



/**
 * @author Tsu
 */
public class gconvo extends JFrame {
	private GroupConvo gc;
	public gconvo(GroupConvo gc) {
		this.gc = gc;
		initComponents();
	}

	private void textArea2KeyReleased(KeyEvent e) {
		// TODO add your code here
		if (e.getKeyCode() == 10) { // enter
			if (!textArea2.getText().trim().equals("")) {
				try {
					String msg = textArea2.getText().trim().replaceAll(((char) 10) + "", "\\\\n").replaceAll("\r", "");
					textArea1.insert("Me: " + textArea2.getText().trim() + "\n", textArea1.getText().length());
//					System.out.println(msg + " | " + textArea2.getText());
					if (msg.trim().endsWith("\\\\n")) {
//						System.out.println("s");
						msg.substring(0, msg.length() - 6);
					}
					gc.sendMessage(msg);
					textArea2.setText("");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} else {
				textArea2.setText("");
				// clear the text area anyways
			}
		}
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		scrollPane1 = new JScrollPane();
		textArea1 = new JTextArea();
		scrollPane2 = new JScrollPane();
		textArea2 = new JTextArea();
		scrollPane3 = new JScrollPane();
		list1 = new JList();

		//======== this ========
		Container contentPane = getContentPane();

		//======== scrollPane1 ========
		{

			//---- textArea1 ----
			textArea1.setEditable(false);
			scrollPane1.setViewportView(textArea1);
		}

		//======== scrollPane2 ========
		{

			//---- textArea2 ----
			textArea2.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					textArea2KeyReleased(e);
				}
			});
			scrollPane2.setViewportView(textArea2);
		}

		//======== scrollPane3 ========
		{
			scrollPane3.setViewportView(list1);
		}

		GroupLayout contentPaneLayout = new GroupLayout(contentPane);
		contentPane.setLayout(contentPaneLayout);
		contentPaneLayout.setHorizontalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(contentPaneLayout.createSequentialGroup()
					.addGroup(contentPaneLayout.createParallelGroup()
						.addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE)
						.addComponent(scrollPane2, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE))
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addComponent(scrollPane3, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE))
		);
		contentPaneLayout.setVerticalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
					.addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addComponent(scrollPane2, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE))
				.addComponent(scrollPane3, GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE)
		);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JScrollPane scrollPane1;
	private JTextArea textArea1;
	private JScrollPane scrollPane2;
	private JTextArea textArea2;
	private JScrollPane scrollPane3;
	private JList list1;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
