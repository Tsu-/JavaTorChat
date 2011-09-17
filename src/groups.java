import java.awt.*;
import java.awt.event.*;
import java.util.Map.Entry;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.TreePath;



/**
 * @author Tsu
 */
public class groups extends JFrame {
	private Buddy b;
	private BuddyList bl;
	private int selected1;
	private int selected2;
	private DefaultListModel model2;
	private DefaultListModel model;
	public groups(Buddy b, final BuddyListGui blg) {
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
		this.b = b;
		initComponents();
		model2 = new DefaultListModel();
		list2.setModel(model2);
		int i = 0;
		for (Entry<String, BuddyGroup> e : b.groups.entrySet()) {
			model2.add(i++, e.getValue());
		}
		

		model = new DefaultListModel();
		list1.setModel(model);
		i = 0;
		for (Entry<String, BuddyGroup> e : bl.buddyGroups.entrySet()) {
			if (!b.groups.containsValue(e.getValue()))
				model.add(i++, e.getValue());
		}
	}

	private void list1ValueChanged(ListSelectionEvent e) {
		selected1 = e.getFirstIndex();
	}

	private void list1MousePressed(MouseEvent e) {
		// TODO add your code here
		if (e.getClickCount() == 2) {
			int index = list1.locationToIndex(e.getPoint());
			if (index >= 0) {
				Object o = list1.getModel().getElementAt(index);
	            System.out.println("Double-clicked on: " + o.toString());
			}
		}
	}

	private void list2ValueChanged(ListSelectionEvent e) {
		// TODO add your code here
		selected2 = e.getFirstIndex();
	}

	private void list2MousePressed(MouseEvent e) {
		// TODO add your code here
		if (e.getClickCount() == 2) {
			int index = list2.locationToIndex(e.getPoint());
			if (index >= 0) {
				Object o = list2.getModel().getElementAt(index);
	            System.out.println("Double-clicked on: " + o.toString());
			}
		}
	}

	private void button1ActionPerformed(ActionEvent e) {
		// TODO add your code here
		model.add(model.getSize(), list2.getSelectedValue());
		model2.remove(list2.getSelectedIndex());
	}

	private void button2ActionPerformed(ActionEvent e) {
		// TODO add your code here
		String g = JOptionPane.showInputDialog(this, "Enter group name", "Create group", JOptionPane.QUESTION_MESSAGE);
		if (bl.buddyGroups.containsKey(g)) {
			return;
		} else {
			BuddyGroup bg = new BuddyGroup(bl.tc, g);
//			bg.addBuddy(b);
			model.add(model2.getSize(), bg);
		}
	}

	private void button3ActionPerformed(ActionEvent e) {
		// TODO add your code here
		for (BuddyGroup bg : b.groups.values()) {
			boolean found = false;
			for (int i = 0 ; i < list2.getModel().getSize() ; i++) {
				list2.getModel().getElementAt(i);
				BuddyGroup bgg = ((BuddyGroup) list2.getModel().getElementAt(i));
				if (bg.equals(bgg))
					found = true;
//				.addBuddy(b);
			}
			if (!found)
				bg.remove(b);
		}
		for (int i = 0 ; i < list2.getModel().getSize() ; i++) {
			BuddyGroup bg = (BuddyGroup) list2.getModel().getElementAt(i);
			boolean found = false;
			for (BuddyGroup bgg : b.groups.values()) {
				if (bg.equals(bgg))
					found = true;
			}
			if (!found)
				((BuddyGroup) list2.getModel().getElementAt(i)).addBuddy(b);
		}

		this.dispose();
    	bl.blg.setEnabled(true);
    	bl.blg.setVisible(true);
	}

	private void button4ActionPerformed(ActionEvent e) {
		this.dispose();
    	bl.blg.setEnabled(true);
    	bl.blg.setVisible(true);
	}

	private void button5ActionPerformed(ActionEvent e) {
		// TODO add your code here
		model2.add(model2.getSize(), list1.getSelectedValue());
		model.remove(list1.getSelectedIndex());
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		scrollPane1 = new JScrollPane();
		list1 = new JList();
		scrollPane2 = new JScrollPane();
		list2 = new JList();
		button1 = new JButton();
		button2 = new JButton();
		button3 = new JButton();
		button4 = new JButton();
		button5 = new JButton();

		//======== this ========
		Container contentPane = getContentPane();

		//======== scrollPane1 ========
		{

			//---- list1 ----
			list1.setModel(new AbstractListModel() {
				String[] values = {
					"test",
					"herp",
					"derp",
					"sdf",
					"her",
					"er",
					"s",
					"df"
				};
				public int getSize() { return values.length; }
				public Object getElementAt(int i) { return values[i]; }
			});
			list1.addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					list1ValueChanged(e);
				}
			});
			list1.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					list1MousePressed(e);
				}
			});
			scrollPane1.setViewportView(list1);
		}

		//======== scrollPane2 ========
		{

			//---- list2 ----
			list2.setModel(new AbstractListModel() {
				String[] values = {
					"herpaderp",
					"herpaderpaderp"
				};
				public int getSize() { return values.length; }
				public Object getElementAt(int i) { return values[i]; }
			});
			list2.addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					list2ValueChanged(e);
				}
			});
			list2.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					list2MousePressed(e);
				}
			});
			scrollPane2.setViewportView(list2);
		}

		//---- button1 ----
		button1.setText("<");
		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				button1ActionPerformed(e);
			}
		});

		//---- button2 ----
		button2.setText("+");
		button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				button2ActionPerformed(e);
			}
		});

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

		//---- button5 ----
		button5.setText(">");
		button5.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				button5ActionPerformed(e);
			}
		});

		GroupLayout contentPaneLayout = new GroupLayout(contentPane);
		contentPane.setLayout(contentPaneLayout);
		contentPaneLayout.setHorizontalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(contentPaneLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
						.addGroup(contentPaneLayout.createSequentialGroup()
							.addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
								.addGroup(contentPaneLayout.createParallelGroup()
									.addComponent(button5)
									.addComponent(button2))
								.addComponent(button1))
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(scrollPane2, GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE))
						.addGroup(contentPaneLayout.createSequentialGroup()
							.addComponent(button4)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(button3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
					.addContainerGap())
		);
		contentPaneLayout.setVerticalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(contentPaneLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(contentPaneLayout.createParallelGroup()
						.addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
							.addComponent(button1)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(button5)
							.addGap(65, 65, 65)
							.addComponent(button2))
						.addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
						.addComponent(scrollPane2, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE))
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(button3)
						.addComponent(button4))
					.addContainerGap())
		);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JScrollPane scrollPane1;
	private JList list1;
	private JScrollPane scrollPane2;
	private JList list2;
	private JButton button1;
	private JButton button2;
	private JButton button3;
	private JButton button4;
	private JButton button5;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
