import java.util.HashMap;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;


public class BuddyGroup extends BuddyList {
	private String name;
	public HashMap<String, MutableTreeNode> budNodes = new HashMap<String, MutableTreeNode>();

	public BuddyGroup(TorChat tc, String name) {
		super(tc.getBl());
		tc.getBl().buddyGroups.put(name, this);
		this.name = name;

		root = new DefaultMutableTreeNode(name);
		tc.getBl().root.add(root);
		path = root.getPath();
	}
	
	public void addBuddy(Buddy b) {
		if (b.node == null) {
			System.err.println("[ERROR] must be added to normal buddies list first!");
			return;
		}
		b.onAddToGroup(this);
		buddies.put(b.getAddress(), b);
		buddiesByCookie.put(b.getCookie(), b);
		MutableTreeNode x = (MutableTreeNode) b.node.clone();
		budNodes.put(b.getAddress(), x);
		root.insert(x, root.getChildCount());

		blg.getTree1().updateUI();
		if (buddies.size() == 1) // TODO need an if
			blg.getTree1().expandPath(new TreePath(path));
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	public void remove(Buddy b) {
		// TODO Auto-generated method stub
		b.groups.remove(this);
		buddies.remove(b.getAddress());
		budNodes.remove(b.getAddress());
		buddiesByCookie.remove(b.getCookie());
	}

	@Override
	public String toString() {
		return name;
	}
}
