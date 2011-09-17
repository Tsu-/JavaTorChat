import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Image;
import java.awt.geom.Rectangle2D;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

@SuppressWarnings("serial")
public class CustomIconRenderer extends DefaultTreeCellRenderer {
	public static final ImageIcon awayIcon;
	public static final ImageIcon handshakeIcon;
	public static final ImageIcon offlineIcon;
	public static final ImageIcon onlineIcon;
	public static final ImageIcon xaIcon;
	private JTree t;
	
	static {
		System.out.println("Loading status icons.");
		awayIcon = new ImageIcon(CustomIconRenderer.class.getResource("/images/away.png"));
		handshakeIcon = new ImageIcon(CustomIconRenderer.class.getResource("/images/connecting.png"));
		offlineIcon = new ImageIcon(CustomIconRenderer.class.getResource("/images/offline.png"));
		onlineIcon = new ImageIcon(CustomIconRenderer.class.getResource("/images/online.png"));
		xaIcon = new ImageIcon(CustomIconRenderer.class.getResource("/images/xa.png"));
//		awayIcon = new ImageIcon("images/away.png");
//		handshakeIcon = new ImageIcon("images/connecting.png");
//		offlineIcon = new ImageIcon("images/offline.png");
//		onlineIcon = new ImageIcon("images/online.png");
//		xaIcon = new ImageIcon("images/xa.png");
		
	}

	public CustomIconRenderer(JTree t) {
		this.t = t;
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {

		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus);
		
		Object nodeObj = ((DefaultMutableTreeNode) value).getUserObject();
		if (nodeObj instanceof Buddy) {
			Buddy b = ((Buddy) nodeObj);
			int status = b.getStatus();
			String s = "<html>" + b.getAddress();
			if (!b.getProfile_text().equals(""))
				s += "<BR>" + b.getProfile_name();
			if (!b.getProfile_text().equals(""))
				s += "<BR>" + b.getProfile_text();
			if (!b.getClient().equals(""))
				s += "<BR>" + b.getClient() + " " + b.getVersion(); 
					
			s += "</html>";
			this.setToolTipText(s);
			setIcon(getStatusIcon(status));
		}
		return this;
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension dim = super.getPreferredSize();
		FontMetrics fm = getFontMetrics(getFont());
		char[] chars = getText().toCharArray();
		Rectangle2D r = fm.getStringBounds(getText(), t.getGraphics());
//		System.out.println(this.getText());

		int w = getIconTextGap() + 16;
		for (char ch : chars) {
			w += fm.charWidth(ch);
		}
		w += getText().length();
		dim.width = w;
		return new Dimension((int) r.getWidth() + 22, (int) r.getHeight());
	}

	public static ImageIcon getStatusIcon(int status) {
		// TODO Auto-generated method stub
		if (status == Buddy.STATUS_AWAY) {
			return awayIcon;
		} else if (status == Buddy.STATUS_HANDSHAKE) {
			return handshakeIcon;
		} else if (status == Buddy.STATUS_OFFLINE) {
			return offlineIcon;
		} else if (status == Buddy.STATUS_ONLINE) {
			// System.out .println("onlie");
			return onlineIcon;
		} else if (status == Buddy.STATUS_XA) {
			return xaIcon;
		}
		return null;
	}
}