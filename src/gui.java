import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;
/*
 * Created by JFormDesigner on Mon Aug 22 22:58:27 EST 2011
 */



/**
 * @author Tsu
 */
@SuppressWarnings("serial")
public class gui extends JFrame {
	/**
	 * @return the textArea1
	 */
	public JTextArea getTextArea1() {
		return textArea1;
	}

	/**
	 * @return the textArea2
	 */
	public JTextArea getTextArea2() {
		return textArea2;
	}

	private Buddy b;
	public gui(Buddy b) {
		this.b = b;
//		b.registerGui(this);
		initComponents();
		textArea1.setLineWrap(true);
		textArea2.setLineWrap(true);
		textArea1.setWrapStyleWord(true);
		textArea2.setWrapStyleWord(true);
	}

	private void textArea2KeyReleased(KeyEvent e) {
//		System.out.println(e.getKeyCode());
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
					b.sendMessage(msg);
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

		GroupLayout contentPaneLayout = new GroupLayout(contentPane);
		contentPane.setLayout(contentPaneLayout);
		contentPaneLayout.setHorizontalGroup(
			contentPaneLayout.createParallelGroup()
				.addComponent(scrollPane2, GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
				.addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
		);
		contentPaneLayout.setVerticalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
					.addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addComponent(scrollPane2, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE))
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
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
