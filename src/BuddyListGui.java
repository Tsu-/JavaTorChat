import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

/*
 * Created by JFormDesigner on Tue Aug 23 16:39:10 EST 2011
 */

/**
 * @author Tsu
 */
@SuppressWarnings("serial")
public class BuddyListGui extends JFrame {
	private TreePath selected;

	public BuddyListGui() {
		initComponents();
	}

	private void tree1ValueChanged(TreeSelectionEvent e) {
		selected = e.getPath();
		// System.out.println(e.getPath());
	}

	private void myPopupEvent(MouseEvent e) {
		final int x = e.getX();
		final int y = e.getY();
		JTree tree = (JTree) e.getSource();
		TreePath path = tree.getPathForLocation(x, y);
		if (path == null)
			return;

		tree.setSelectionPath(path);
		JPopupMenu popup = new JPopupMenu();
		DefaultMutableTreeNode d = (DefaultMutableTreeNode) path
				.getLastPathComponent();
		final Object o = d.getUserObject();

		ActionListener al = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String ac = e.getActionCommand();
				TreePath path = tree1.getPathForLocation(x, y);
				System.out.println("path = " + path + " | " + ac);
				if (o instanceof Buddy) {
					Buddy b = (Buddy) o;
					if (ac.equals("CHAT"))
						b.openWindow(true);
					if (ac.equals("EDIT CONTACT")) {
						addgui ag = new addgui(bl.blg, b);
						setEnabled(false);
						ag.setVisible(true);
					}
					if (ac.equals("DELETE CONTACT")) {
						b.delete();
					}
					if (ac.equals("EDIT GROUPS")) {
						groups g = new groups(b, bl.blg);
						setEnabled(false);
						g.setVisible(true);
					}
					if (ac.equals("NEW GROUP CONVO")) {
//						groups g = new groups(b, bl.blg);
//						setEnabled(false);
//						g.setVisible(true);
						GroupConvo g = new GroupConvo(bl.tc.bUs);
						try {
							g.addParticipant(b);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					if (ac.startsWith("ADD TO ")) {
						try {
							bl.tc.gconvos.get(ac.split(" ")[2].toLowerCase()).addParticipant(b);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}

				if (ac.equals("ADD NEW CONTACT")) {
					addgui ag = new addgui(bl.blg);
					setEnabled(false);
					ag.setVisible(true);
				}
				if (ac.equals("EDIT MY PROFILE")) {
					editmyprofile emp = new editmyprofile(bl.blg);
					setEnabled(false);
					emp.setVisible(true);
				}
				if (ac.equals("QUIT")) {
					bl.tc.exit();
				}
				// System.out.printf("loc = [%d, %d]%n", loc.x, loc.y);
				// if(ac.equals("ADD CHILD"))
				// addChild(path);
				// if(ac.equals("ADD SIBLING"))
				// addSibling(path);
			}

		};

		if (o instanceof Buddy) {
			// Buddy b = (Buddy) o;
			popup.add(getMenuItem("Chat", al));
			popup.add(new JPopupMenu.Separator());
			popup.add(getMenuItem("Edit contact", al));
			popup.add(getMenuItem("Delete contact", al));
			popup.add(getMenuItem("Edit groups", al));
			popup.add(new JPopupMenu.Separator());
//			if (bl.tc.gconvos.values().size() > 0) {
				JMenu gc = getMenu("Group Chat", al);
				popup.add(gc);
				for (GroupConvo gcc : bl.tc.gconvos.values()) {
					if (gcc.isHost())
						gc.add(getMenuItem("Add to " + gcc.getId(), al));
				}
				gc.add(getMenuItem("New group convo", al));
				popup.add(new JPopupMenu.Separator());
//			}
		}

		popup.add(getMenuItem("Add new contact", al));
		popup.add(getMenuItem("Edit my profile", al));
		popup.add(new JPopupMenu.Separator());
		popup.add(getMenuItem("Settings", al));
		popup.add(getMenuItem("Quit", al));

		// String label = "popup: " + obj.getTreeLabel();
		popup.show(tree, x, y);
	}

	private JMenuItem getMenuItem(String s, ActionListener al) {
		JMenuItem menuItem = new JMenuItem(s);
		menuItem.setActionCommand(s.toUpperCase());
		menuItem.addActionListener(al);
		// menuItem
		return menuItem;
	}
	
	private JMenu getMenu(String s, ActionListener al) {
		JMenu menu = new JMenu(s);
		menu.setActionCommand(s.toUpperCase());
		menu.addActionListener(al);
		// menuItem
		return menu;
	}

	private void tree1KeyReleased(KeyEvent e) {
		// System.out.println(e.getKeyCode());
		if (e.getKeyCode() == 10) {
			if (selected != null)
				doAction(selected);
		}
	}

	private void tree1MousePressed(MouseEvent e) {
		int selRow = tree1.getRowForLocation(e.getX(), e.getY());
		TreePath selPath = tree1.getPathForLocation(e.getX(), e.getY());
		if (selRow != -1) {
			if (e.getClickCount() == 2) {
				doAction(selPath);
				// myDoubleClick(selRow, selPath);
			}
		}
		if (e.isPopupTrigger())
			myPopupEvent(e);
	}

	private void doAction(TreePath x) {
		DefaultMutableTreeNode o = (DefaultMutableTreeNode) x
				.getLastPathComponent();
		if (o.getUserObject() instanceof Buddy) {
			Buddy b = (Buddy) o.getUserObject();
			b.openWindow(true);
		}
		// System.out.println(o instanceof Buddy);
		// System.out.println(o.getClass());
	}

	private void tree1MouseReleased(MouseEvent e) {
		if (e.isPopupTrigger())
			myPopupEvent(e);
	}

	private void hideOfflineBudsActionPerformed(ActionEvent e) {
		System.out.println("Toggle hide offline buds");
		if (e.getSource() instanceof JCheckBoxMenuItem) {
			JCheckBoxMenuItem jc = (JCheckBoxMenuItem) e.getSource();
			if (jc.getActionCommand().equals("Hide offline buddies")) {
				bl.hideOffBuds(jc.isSelected());
			}
		}
	}

	private void menuItemActionPerformed(ActionEvent e) {
		// TODO add your code here
		String ac = e.getActionCommand();
//		System.out.println(ac);
		if (ac.equals("Exit")) {
			bl.tc.exit();
		}
		if (ac.equalsIgnoreCase("ADD NEW CONTACT")) {
			addgui ag = new addgui(bl.blg);
			setEnabled(false);
			ag.setVisible(true);
		}
		if (ac.equalsIgnoreCase("EDIT MY PROFILE")) {
			editmyprofile emp = new editmyprofile(bl.blg);
			setEnabled(false);
			emp.setVisible(true);
		}
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY
		// //GEN-BEGIN:initComponents
		menuBar1 = new JMenuBar();
		menu1 = new JMenu();
		menuItem2 = new JMenuItem();
		menuItem5 = new JMenuItem();
		menuItem4 = new JMenuItem();
		menuItem1 = new JMenuItem();
		menu3 = new JMenu();
		menuItem3 = new JCheckBoxMenuItem();
		scrollPane1 = new JScrollPane();
		tree1 = new JTree();

		//======== this ========
		Container contentPane = getContentPane();

		//======== menuBar1 =======
		{

			//======== menu1 ========
			{
				menu1.setText("File");

				//---- menuItem2 ----
				menuItem2.setText("Add new contact");
				menuItem2.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						menuItemActionPerformed(e);
					}
				});
				menu1.add(menuItem2);

				//---- menuItem5 ----
				menuItem5.setText("Edit my profile");
				menuItem5.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						menuItemActionPerformed(e);
					}
				});
				menu1.add(menuItem5);
				menu1.addSeparator();

				//---- menuItem4 ----
				menuItem4.setText("Settings");
				menu1.add(menuItem4);
				menu1.addSeparator();

				//---- menuItem1 ----
				menuItem1.setText("Exit");
				menuItem1.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						menuItemActionPerformed(e);
					}
				});
				menu1.add(menuItem1);
			}
			menuBar1.add(menu1);

			//======== menu3 ========
			{
				menu3.setText("View");

				//---- menuItem3 ----
				menuItem3.setText("Hide offline buddies");
				menuItem3.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						hideOfflineBudsActionPerformed(e);
					}
				});
				menu3.add(menuItem3);
			}
			menuBar1.add(menu3);
		}
		setJMenuBar(menuBar1);

		//======== scrollPane1 ========
		{

			//---- tree1 ----
			tree1.setRootVisible(false);
			tree1.setModel(new DefaultTreeModel(
				new DefaultMutableTreeNode("(root)") {
					{
						add(new DefaultMutableTreeNode("xzfzvnvqeuiphqaf xTurkishHackerx"));
						add(new DefaultMutableTreeNode("54hphqimrtnez2tk Verf\u00fchrer"));
						add(new DefaultMutableTreeNode("vzojh7jqwxbfmh55 Expect"));
						add(new DefaultMutableTreeNode("whhwrythmuwc6zcp maskNcloak"));
						add(new DefaultMutableTreeNode("fpsh2kpyl44rt2tt F@talErr.0r"));
						add(new DefaultMutableTreeNode("hwbm747nfyl2zqks Asshole"));
						add(new DefaultMutableTreeNode("7oj5u53estwg2pvu infoservAddressbooks"));
						add(new DefaultMutableTreeNode("qmk545l4nuulhytk Doom Prophet"));
						add(new DefaultMutableTreeNode("dzpvwdeb7jjwhxn2 anonfag"));
						add(new DefaultMutableTreeNode("6trhroaljx4zfzev slexplex2"));
						add(new DefaultMutableTreeNode("xpo6lqilqymo2tll Blackbird-NSA"));
						add(new DefaultMutableTreeNode("4fjjxjdmczyeomqz pro3phec"));
						add(new DefaultMutableTreeNode("m5sug27myzfxgbe6 Dreams"));
						add(new DefaultMutableTreeNode("ylvpf24ygwv5rmhb lifesign"));
						add(new DefaultMutableTreeNode("m34tfexauhc7lrmf Spielberg"));
						add(new DefaultMutableTreeNode("djtcxftb2pwmxjvk operator"));
						add(new DefaultMutableTreeNode("iw5zvvzckiumbhuc Thyrst"));
						add(new DefaultMutableTreeNode("imrrmuslbkzha5wl anonymousareusers"));
						add(new DefaultMutableTreeNode("ffrvqlx2on4k6fit anonymousareusers2"));
						add(new DefaultMutableTreeNode("rwfxtt23qnytwwsx Industrializer"));
						add(new DefaultMutableTreeNode("qj276qz22qijfpzg anonymousareusers2"));
						add(new DefaultMutableTreeNode("6k7b5iqj6bwwzd4f myself"));
					}
				}));
			tree1.addTreeSelectionListener(new TreeSelectionListener() {
				@Override
				public void valueChanged(TreeSelectionEvent e) {
					tree1ValueChanged(e);
				}
			});
			tree1.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					tree1KeyReleased(e);
				}
			});
			tree1.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					tree1MousePressed(e);
				}
				@Override
				public void mouseReleased(MouseEvent e) {
					tree1MouseReleased(e);
				}
			});
			scrollPane1.setViewportView(tree1);
		}

		GroupLayout contentPaneLayout = new GroupLayout(contentPane);
		contentPane.setLayout(contentPaneLayout);
		contentPaneLayout.setHorizontalGroup(
			contentPaneLayout.createParallelGroup()
				.addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE)
		);
		contentPaneLayout.setVerticalGroup(
			contentPaneLayout.createParallelGroup()
				.addComponent(scrollPane1, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
		);
		pack();
		setLocationRelativeTo(getOwner());
		// //GEN-END:initComponents
	}

	/**
	 * @return the tree1
	 */
	public JTree getTree1() {
		return tree1;
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY
	// //GEN-BEGIN:variables
	private JMenuBar menuBar1;
	private JMenu menu1;
	private JMenuItem menuItem2;
	private JMenuItem menuItem5;
	private JMenuItem menuItem4;
	private JMenuItem menuItem1;
	private JMenu menu3;
	private JCheckBoxMenuItem menuItem3;
	private JScrollPane scrollPane1;
	private JTree tree1;
	// JFormDesigner - End of variables declaration //GEN-END:variables
	public BuddyList bl;
}
