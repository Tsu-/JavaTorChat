import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.WindowConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;


public class BuddyList {
	public HashMap<String, Buddy> buddies = new HashMap<String, Buddy>();
	public HashMap<String, gui> buddyGui = new HashMap<String, gui>();
	public HashMap<String, Buddy> buddiesByCookie = new HashMap<String, Buddy>();
	protected BuddyListGui blg;
	protected DefaultMutableTreeNode root;
	TorChat tc;
	private DefaultMutableTreeNode buddyNode;
	protected HashMap<String, BuddyGroup> buddyGroups = new HashMap<String, BuddyGroup>();
	protected TreeNode[] path;
	private boolean hideOffBuds;
	private int hiddenBuddies;

	public BuddyList(BuddyList bl) {
		this.tc = bl.tc;
		this.buddyGroups = bl.buddyGroups;
		this.blg = bl.blg;
		this.root = bl.root;
	}
	
	public BuddyList(TorChat tc, BuddyListGui blg) {
		this.tc = tc;
		this.blg = blg;
		blg.bl = this;
		
		blg.addWindowListener(new WindowListener() {
            public void windowClosed(WindowEvent e) {
            }
            public void windowActivated(WindowEvent e) {
            }
            public void windowClosing(WindowEvent e) {
            	try {
					saveBuddies();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
                System.exit(0);
            }
            public void windowDeactivated(WindowEvent e) {
            }
            public void windowDeiconified(WindowEvent e) {
            }
            public void windowIconified(WindowEvent e) {
            }
            public void windowOpened(WindowEvent e) {
            }
        });
		if (Config.TESTING)
			blg.setTitle("TESTING: " + TorChat.us);
		else
			blg.setTitle("Tor is loading...");
		javax.swing.ToolTipManager.sharedInstance().registerComponent(blg.getTree1());
		blg.getTree1().setCellRenderer(new CustomIconRenderer(blg.getTree1()));
		blg.setVisible(true);
		root = new DefaultMutableTreeNode("(root)");
		buddyNode = new DefaultMutableTreeNode("Buddies");
		root.add(buddyNode);
		path = root.getPath();
		blg.getTree1().setModel(new DefaultTreeModel(root));
		blg.getTree1().setLargeModel(true);
	}

	public boolean hasBuddy(String string) {
//		System.out.println("[BL] has Buddy " + string + "? " + buddies.containsKey(string));
		return buddies.containsKey(string);
	}
	
	public void addBuddy(Buddy b) {
		if (b.getAddress().equals(TorChat.us))
			tc.bUs = b;
		
		buddies.put(b.getAddress(), b);
		buddiesByCookie.put(b.getCookie(), b);
		
		DefaultMutableTreeNode x = new DefaultMutableTreeNode(b);
		if (b.node != null)
			b.node.removeFromParent();
		b.node = x;
		if (!hideOffBuds || (hideOffBuds && b.getStatus() != Buddy.STATUS_OFFLINE))
			buddyNode.insert(x, buddyNode.getChildCount());
		else {
			hiddenBuddies--;
			if (hiddenBuddies == 0)
				buddyNode.setUserObject("Buddies");
			else
				buddyNode.setUserObject("Buddies (" + hiddenBuddies + " hidden)");
			}

		if (buddies.size() == 1) // TODO need an if
			blg.getTree1().expandRow(0);
	}
	
	public void addBuddy(Buddy b, boolean bo) { // bo, whether to updateUI
		buddies.put(b.getAddress(), b);
		buddiesByCookie.put(b.getCookie(), b);
		
		DefaultMutableTreeNode x = new DefaultMutableTreeNode(b);
		if (b.node != null)
			b.node.removeFromParent();
		b.node = x;
//		if (!hideOffBuds || (hideOffBuds && b.getStatus() != Buddy.STATUS_OFFLINE))
//			buddyNode.insert(x, buddyNode.getChildCount());
//		if (bo)
//			blg.getTree1().updateUI();

		checkBuddyHOB(b, x, bo, false);
		if (buddies.size() == 1) // TODO need an if
			blg.getTree1().expandRow(0);
	}
	
	public void updateCookieForBBC(String oldC, Buddy b) {
		if (buddiesByCookie.containsKey(oldC))
			buddiesByCookie.remove(oldC);
		buddiesByCookie.put(oldC, b);
	}

	public Buddy getBuddy(String string) {
		return buddies.get(string);
	}

	public void onStatusUpdate(Buddy buddy, int status) {
//		System.err.println(buddy + ", " + status);
//		buddy.node.set
		checkBuddyHOB(buddy, buddy.node, false, true);
		if (hasGui(buddy.getAddress())) {
			getGui(buddy.getAddress()).setIconImage(CustomIconRenderer.getStatusIcon(status).getImage());
		}
		try {
			blg.getTree1().revalidate();
			blg.getTree1().repaint();
//			if (blg.isVisible() && blg.getTree1().isVisible())
//				blg.getTree1().updateUI();
		} catch (Exception e) {
		}
	}
	
	private void checkBuddyHOB(Buddy b, DefaultMutableTreeNode node, boolean bool, boolean toCount) { // bool force no updateUI
		if (node == null)
			node = b.node;
		if (b.node != null) {
			if (!hideOffBuds && b.node.getParent() == null) {
				buddyNode.insert(b.node, buddyNode.getChildCount());
				hiddenBuddies--;
				if (!bool) {
					if (toCount)
						if (hiddenBuddies == 0)
							buddyNode.setUserObject("Buddies");
						else
							buddyNode.setUserObject("Buddies (" + hiddenBuddies + " hidden)");
					blg.getTree1().updateUI();
				}
			} else if (hideOffBuds && b.node.getParent() != null && b.getStatus() == Buddy.STATUS_OFFLINE) {
				b.node.removeFromParent();
				hiddenBuddies++;
				if (!bool) {
					if (toCount)
						if (hiddenBuddies == 0)
							buddyNode.setUserObject("Buddies");
						else
							buddyNode.setUserObject("Buddies (" + hiddenBuddies + " hidden)");
					blg.getTree1().updateUI();
				}
			} else if (hideOffBuds && b.node.getParent() == null && b.getStatus() != Buddy.STATUS_OFFLINE) {
				buddyNode.insert(b.node, buddyNode.getChildCount());
				hiddenBuddies--;
				if (!bool) {
					if (toCount)
						if (hiddenBuddies == 0)
							buddyNode.setUserObject("Buddies");
						else
							buddyNode.setUserObject("Buddies (" + hiddenBuddies + " hidden)");
					blg.getTree1().updateUI();
				}
			}
		}
	}

	private boolean hasGui(String s) {
		return buddyGui.containsKey(s);
	}

	public gui getGui(String s) {
		if (buddyGui.containsKey(s) && buddyGui.get(s).isDisplayable()) {
			buddyGui.get(s).setIconImage(CustomIconRenderer.getStatusIcon(buddies.get(s).getStatus()).getImage());
			buddyGui.get(s).setTitle("Chat with " + buddies.get(s).getDisplayName(true));
			return buddyGui.get(s);
		} else {
			gui g = new gui(getBuddy(s));
			g.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			g.setTitle("Chat with " + buddies.get(s).getDisplayName(true));
			buddyGui.put(s, g);
			g.setIconImage(CustomIconRenderer.getStatusIcon(buddies.get(s).getStatus()).getImage());
			return g;
		}
	}
	
	void loadBuddiesOL() { // the list on an onion host i cant remember atm
		if (!this.getClass().equals(BuddyList.class)) {
			System.err.println("[ERROR] " + this.getClass() + " trying to access loadBuddiesOL()V!");
			return;
		}
		
		try {
			System.out.println("Loading");
			URLConnection conn = new URL("http://g7l35kk2ysi7ggon.onion/buddy-list.txt").openConnection(new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("127.0.0.1", Config.SOCKS_PORT)));
			Scanner s = new Scanner(conn.getInputStream());
			String l;
			ArrayList<String> a = new ArrayList<String>();
//			FileOutputStream fos = new FileOutputStream("out.txt");
			while (s.hasNextLine() && (l = s.nextLine()) != null) {
				if (l.equals(""))
					continue;
				String[] spl = l.split(" ");
					if (hasBuddy(spl[0])) {
						// do nothing
					} else { // TODO fix up temporary here
						a.add(l);
						System.out.println(l);
						Buddy b = new Buddy(spl[0], spl.length > 1 ? spl[1] : "", true, this, tc);
						addBuddy(b);
					}
			}
//			fos.close();
			System.out.println(a.size() + " buddies loaded from g7l35kk2ysi7ggon.");
		} catch (IOException e) {
			e.printStackTrace();
		}

		blg.getTree1().updateUI();
	}
	
	void loadBuddies() throws IOException { // should ONLY be called on original BuddyList
		if (!this.getClass().equals(BuddyList.class)) {
			System.err.println("[ERROR] " + this.getClass() + " trying to access loadBuddies()V!");
			return;
		}
		if (new File("buds.txt").exists()) {
			Scanner s = new Scanner(new FileInputStream("buds.txt"));
			String l;
			while (s.hasNextLine() && (l = s.nextLine()) != null) {
				String[] spl = l.split("" + ((char)01)); // 01 so it can atleast be used to copy paste edit the file
				if (hasBuddy(spl[0])) {
					// do nothing
				} else {
					Buddy b = new Buddy(spl[0], spl[1].equals("" + ((char)02)) ? "" : spl[1], false, this, tc);
					addBuddy(b);
					for (int i = 2; i < spl.length ; i++) {
						String g = spl[i];
						if (buddyGroups.containsKey(g)) {
							buddyGroups.get(g).addBuddy(b);
						} else {
							BuddyGroup bg = new BuddyGroup(tc, g);
							bg.addBuddy(b);
						}
					}
				}
			}
		}
		if (!hasBuddy(TorChat.us)) {
			Buddy b = new Buddy(TorChat.us, tc.getProfile_name(), false, this, tc);
			addBuddy(b);
		}
		blg.getTree1().updateUI();
	}
	
	void saveBuddies() throws IOException { // should ONLY be called on original BuddyList
		if (!this.getClass().equals(BuddyList.class)) {
			System.err.println("[ERROR] " + this.getClass() + " trying to access saveBuddies()V!");
			return;
		}
		FileOutputStream fos = new FileOutputStream("buds.txt");
		for (Buddy b : buddies.values()) {
			fos.write((b.getAddress() + ((char) 01) + (b.getDisplayName(false).equals("") ? ((char)02) : b.getDisplayName(false))).getBytes());
			for (BuddyGroup bg : b.getGroups().values()) {
				fos.write((((char) 01) + bg.getName()).getBytes());
			}
			fos.write(("\n").getBytes());
		}
	}
	public void clearBuddies(boolean disconnect) {
		
	}

	public void hideOffBuds(boolean bo) {
		System.out.println("hide off buds " + bo);
		hideOffBuds = bo;
		for (Buddy b : buddies.values())
			checkBuddyHOB(b, b.node, true, true);
//		if (bo)
		if (hiddenBuddies == 0)
			buddyNode.setUserObject("Buddies");
		else
			buddyNode.setUserObject("Buddies (" + hiddenBuddies + " hidden)");
		blg.getTree1().updateUI();
//		if (buddies.size() == 1) // TODO need an if
//			blg.getTree1().expandRow(0);
	}

	public void remove(Buddy b) {
		// TODO Auto-generated method stub
		buddies.remove(b.getAddress());
		buddiesByCookie.remove(b.getCookie());
	}

}
