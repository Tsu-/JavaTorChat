import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Random;


public class GroupConvo {
	private HashMap<String, Buddy> p = new HashMap<String, Buddy>();
	private String id;
	private boolean isHost;
	private Buddy host = null;
	private gconvo gc; 
	
	public GroupConvo(Buddy self) {
		p.put(self.getAddress(), self);
		this.id = Long.toString(new Random().nextLong(), 36);
		self.bl.tc.gconvos.put(id, this);
		System.out.println("new group with id " + id);
		this.host = self;
		isHost = true;
		init();
	}
	
	private void init() {
		// TODO Auto-generated method stub
		this.gc = new gconvo(this);
		gc.setVisible(true);
		
		gc.addWindowListener(new WindowListener() {
            public void windowClosed(WindowEvent e) {}
            public void windowActivated(WindowEvent e) {}
            public void windowClosing(WindowEvent e) {
            	closeGroup();
            }
            public void windowDeactivated(WindowEvent e) {}
            public void windowDeiconified(WindowEvent e) {}
            public void windowIconified(WindowEvent e) {}
            public void windowOpened(WindowEvent e) {}
        });
		
		display(null, "host: " + host + " | isHost " + isHost);
		display(null, "participants: " + this.csvParticipants());
	}

	protected void closeGroup() {
		// TODO Auto-generated method stub
		for (Buddy b : p.values()) {
			if (!b.equals(host)) {
				try {
					b.sendCommand("group close " + id);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		host.bl.tc.gconvos.remove(id);
	}

	public GroupConvo(String idd, Buddy host, String[] strings) {
		if (host.bl.tc.gconvos.containsKey(id)) {
			System.err.println("[ERROR] " + host + " asked us to join " + idd + " which is currently being hosted by " + host.bl.tc.gconvos.get(idd).host);
			return;
		}
		host.bl.tc.gconvos.put(idd, this);
		this.id = idd;
		this.host  = host;
		System.out.println("joined group with id " + id);
		isHost = false;
		for (String sx : strings) {
			try {
				if (host.bl.hasBuddy(sx))
					addParticipant(host.bl.getBuddy(sx));
				else {
					Buddy bb = new Buddy(sx, "", true, host.bl, host.bl.tc);
					host.bl.addBuddy(bb);
					addParticipant(bb);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		init();
	}
	
	public HashMap<String, Buddy> getParticipants() {
		return p;
	}

	public void addParticipant(Buddy b) throws IOException {
		System.out.println("Adding " + b.getAddress() + " to group " + id);
		p.put(b.getAddress(), b);
		try {
			if (isHost) {
				b.sendCommand("group join " + id + " " + csvParticipants());
				for (Buddy bx : p.values()) {
					if (!bx.equals(b) && !bx.equals(host)) {
						try {
							bx.sendCommand("group notice " + id + " Invited " + (b.getDisplayName(false).equals("") ? b.getAddress() : (b.getDisplayName(false) + " (" + b.getAddress() + ")") + "."));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		} catch (IOException e) {
			p.remove(b.getAddress());
			throw e;
		}
	}

	private String csvParticipants() {
		// TODO Auto-generated method stub
		String x = "";
		for (String s : p.keySet()) {
			if (x.equals(""))
				x += s;
			else
				x += "," + s;
		}
		return x;
	}

	public void sendMessage(String msg) throws IOException {
		// TODO Auto-generated method stub
		display(host.bl.tc.bUs, msg);
		if (isHost)
			relayMessage(host, msg);
		else
			host.sendCommand("group msg " + id + " " + msg);
	}
	
	public void relayMessage(Buddy f, String msg) {
		// TODO Auto-generated method stub
		for (Buddy b : p.values()) {
			if (!b.equals(f) && !b.equals(host)) {
				try {
					b.sendCommand("group msg " + id + ((char)01) + f.getDisplayName(true) + " " + msg);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public boolean isHost() {
		// TODO Auto-generated method stub
		return isHost;
	}

	public void display(Buddy buddy, String msg) {
		// TODO Auto-generated method stub
		if (!gc.isVisible())
			gc.setVisible(true);
		gc.getTextArea1().insert((buddy == null ? "" : buddy.getDisplayName(true) + ": ") + msg.replaceAll("\\\\n", "\n") + "\n", gc.getTextArea1().getText().length());
		gc.getTextArea1().setCaretPosition(gc.getTextArea1().getText().length());
	}

	public String getId() {
		// TODO Auto-generated method stub
		return id;
	}

	public Buddy getHost() {
		// TODO Auto-generated method stub
		return host;
	}

}
