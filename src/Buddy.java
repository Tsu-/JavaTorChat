import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.tree.DefaultMutableTreeNode;

public class Buddy {
	public static final int STATUS_OFFLINE = 0;
	public static final int STATUS_HANDSHAKE = 1;
	public static final int STATUS_ONLINE = 2;
	public static final int STATUS_AWAY = 3;
	public static final int STATUS_XA = 4;

	private String address;
	
	private Socket ourSock;
	private OutputStream conn_out;
	
	private Socket theirSock;
	private InputStream conn_in;
//	private boolean connected;
	private boolean recievedPong = false;
//	private boolean sentPong;

	private boolean sentPing;

	private Thread connectThread;
//	private gui gui;
	BuddyList bl; // our buddylist
	private String name; // our nickname of them
	private String profile_name; // their nickname
	private String profile_text; // their profile text
//	private String profile_avatar_data; // gah, this shit is fcking screwed
//	private String profile_avatar_data_alpha; // gah
//	private Object profile_avatar_object; // gah
	private String random1; // the cookie we send them
//	private String random2; // not used?
	private int status; // their current status
	private String client; // their client

	private String version; // their version
//	private boolean timer; // TODO FIXME
	private long last_status_time; // TODO FIXME last time we got a status from them
	private int count_failed_connects; // TODO FIXME number of failed connections
	private int count_unanswered_pings; // TODO FIXME number of unanswered pings
	private boolean active; // currently active?
	/* Notes on active,
	 * Using it as a check to see if a connection attempt is being made atm
	 * false - attempt being made
	 * true - no attempt
	 * 
	 * so its kind of in reverse
	 */
	
//	private boolean temporary; // is this a temporary buddy (not on our buddy list and hasnt sent add_me)
	private Random random; // instance of Random class
	private Timer timerObj = new Timer(); // timer we register tasks with
	
	private ArrayList<String> delayedMessages = new ArrayList<String>();
//	private TorChat tc;
	public DefaultMutableTreeNode node;
	private boolean timerScheduled;

	private static char lf = (char) 10;
	protected HashMap<String, BuddyGroup> groups = new HashMap<String, BuddyGroup>();
	protected Thread prevTask;
	private Thread _attatchThread;
	private int pstatus;

	public Buddy(String address, String name, boolean temporary, BuddyList bl, TorChat tc) {
//		System.out.println(String.format("(2) initializing buddy %s, temporary=%s", address, temporary));
//		this.tc = tc;
		this.random = new Random();
		this.address = address;
        this.bl = bl;
        this.address = address;
        this.name = name;
        this.profile_name = "";
        this.profile_text = "";
//        this.profile_avatar_data = "";        // uncompressed 64*64*24 bit RGB.
//        this.profile_avatar_data_alpha = "";  // uncompressed 64*64*8 bit alpha. (optional)
//        this.profile_avatar_object = null;    // tc_gui.py will cache a wx.Bitmap here, tc_client will not touch this
        
        this.random1 = generateCookie();
//        this.random2 = generateCookie(); // not used? (in orig TorgChat either)
//        this.conn_out = None;
//        this.conn_in = None;
        setStatus(STATUS_OFFLINE);
        this.client = "";
        this.version = "";
//        this.timer = false;
        this.last_status_time = System.currentTimeMillis() + (Config.DEAD_CONNECTION_TIMEOUT / 2);
        this.count_failed_connects = 0;
        this.count_unanswered_pings = 0;
        this.active = true;
//        this.temporary = temporary;
        this.startTimer();
	}
	
	private void startTimer() {
//		Thread.dumpStack();
		if (!this.active) {
//			System.out.println(String.format("(2) %s is not active. Will not start a new timer", this.address));
            return;
		}
		long t;
        if (this.status == STATUS_OFFLINE) {
        	if (this.count_failed_connects < 3) {
            	t = (long) (Methods.random(random, 50, 200) / 10.0);
            } else if (this.count_failed_connects < 7) {
            	t = (long) (Methods.random(random, 700, 1900) / 10.0);
            } else if (this.count_failed_connects < 10) {
            	t = (long) (Methods.random(random, 1300, 2800) / 10.0);
            } else
            	if (this.count_failed_connects < 20) {
                    t = (long) Methods.random(random, 2500, 3500);
                } else {
                    // more than an hour. The other one will ping us if it comes
                    // online which will immediately connect and reset the counting
                	// FIXME if the pings are lost from both sides, may not reconect
                	// for a long time
                    t = (long) Methods.random(random, 5000, 6000);
                }
//            System.out.println(String.format("(2) %s had %d failed connections. Setting timer to %d seconds" , this.address, this.count_failed_connects, t));
        } else {
        	// whenever we are connected to someone we use a fixed timer.
        	// otherwise we would create a unique pattern of activity
            // over time that could be identified at the other side
            if (this.status == STATUS_HANDSHAKE) {
                // ping more agressively during handshake
                // to trigger more connect back attempts there
                t = Config.KEEPALIVE_INTERVAL / 4;
            } else {
                // when fully connected we can slow down to normal 
                t = Config.KEEPALIVE_INTERVAL;
            }
        }
                

//        if (this.timer)
//            this.timer.cancel();
        // assuming function of previous lines to be quivelant to
        // TODO FIXME need to clear timer here, will try work around, cuz lazy 
        timerObj.purge(); // removes cancel tasks, not ones that havnt executed
//        if (prevTask.isAlive()) {
//        	prevTask.stop();
//        	timerScheduled = false;
//        }
        if (!timerScheduled) {
//        	System.out.println("Task scheduled on " + this);
	        timerScheduled = true;
	        TimerTask timerTask = new TimerTask() {
	    		public void run() {
	    			prevTask = Thread.currentThread();
	    			try {
	    				if (ourSock != null && ourSock.isConnected() && checkConnected()) {
	    					keepAlive();
//	    					sendStatus(); // in keepAlive
//	    					System.out.println("Sent status to " + address);
	    				} else {
//	    					System.out.println("Didnt send status to " + address);
	    					connect();
	    				}
	    			} catch (IOException e) {
	    				e.printStackTrace();
	    			}
	    			timerScheduled = false;
	    	        startTimer();
	    		}
	    	};
			timerObj.schedule(timerTask, t * 1000);
        }
//        System.out.println("k, done");
//        if (t == null) {
//        	timerObj.scheduleAtFixedRate(timerTask, 0, 100000);
//			System.out.println("[INFO] Registered Status Timer");
//		}
	}

	private String generateCookie() {
		String s = "";
		String a = "abcdefghijklmnopqrstuvwxyz1234567890";
		for (int i = 0 ; i < 77 ; i++)
			s += a.charAt(random.nextInt(a.length()));
		return s;
	}

	public String getAddress() {
		return address;
	}
	
	public void connect() throws IOException {
//		Thread.dumpStack();
		if (connectThread != null && connectThread.isAlive()) {
//			System.out.println("ITS ALIVE!");
			return; // dont want multiple connections at once
		}
//		if (ourSock != null && ourSock.isConnected()) // && !ourSock.isOutputShutdown())
//			return;
		active = false;
        this.count_unanswered_pings = 0;
		connectThread = new Thread() {
			public void run() {
//				System.out.println("Connection thread " + this + " started");
//				while(true) {
				setStatus(Buddy.STATUS_OFFLINE);
//				System.out.println(this + " active " + active);
				try {
					ourSock = new Socket(new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("127.0.0.1", Config.SOCKS_PORT)));
//					System.out.println("proxy " + address + ".onion:11009");
					ourSock.connect(new InetSocketAddress(address + ".onion", 11009));
					setStatus(Buddy.STATUS_HANDSHAKE);
					System.out.println(address + " connected!");
					conn_out = ourSock.getOutputStream();
//					this.i = ourSock.getInputStream();
//					if (checkConnected()) {
						sendPing();
//						connected = true;
//					} else
//						System.out.println("[ERR] Just connected but not connected anymore. WTF?");
//						break;
				} catch (IOException e) {
					if (e.getMessage().equals("SOCKS: TTL expired")
							|| e.getMessage().equals("SOCKS: Host unreachable")
							|| e.getMessage().equals("Connection refused: connect")
							|| e.getMessage().equals("Connection reset")
							|| e.getMessage().equals("SOCKS server general failure")) {
						
					} else {
						e.printStackTrace();
					}
					count_failed_connects++;
					setStatus(Buddy.STATUS_OFFLINE);
				}
//				}

				active = true;
				startTimer();
//				System.out.println(this + " active " + active);
//				System.out.println("Connection thread " + this + " died");
			}
			
		};
		connectThread.start();
	}
	
	public void attatch(Socket ts, String ping, Scanner sc) throws IOException {
		if (connectThread != null && connectThread.isAlive())
			try {
				connectThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		try {
			System.out.println("DC CUZ NEW CON");
//			if (theirSock != null)
//				theirSock.close();
		} catch (Exception e) {}
//		if ((ts.isConnected() || !ts.isClosed())
//				&& ts != theirSock)
//			ts.close();
		if (_attatchThread != null && _attatchThread.isAlive()) {
			_attatchThread.interrupt();
			
		}
		_attatchThread = Thread.currentThread();
		String[] s = ping.split(" ");
		if (s[0].equals("ping")
				&& s[1].equals(address)) { // looks legit
			if (connectThread == null || !connectThread.isAlive() && !checkConnected())
				connect();
			if (connectThread.isAlive())
				try {
					connectThread.join(); // wait for connect //TODO
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			this.theirSock = ts;
			conn_in = theirSock.getInputStream();
			if (!sentPing)
				sendPing();
			sendPong(s[2]);
			sendClient();
			sendVersion();
			sendProfileName();
			sendProfileText();
			if (true) // should be checking if on buddy list
				sendAddMe();
			sendStatus();
			startTimer();
			
//			gui = bl.getGui(address);
			
//			sendCommand("test");
//			System.out.println("Sent our data to " + address);

//			sendStatus();
			trySendDelayedMessages();
			
			String l;

			System.out.println("b");
			while (theirSock.isConnected() && !theirSock.isInputShutdown() && !theirSock.isOutputShutdown()) {
				System.out.println("c");
				// (!connectThread.isAlive() && !checkConnected()) || 
				if (theirSock.isInputShutdown() || theirSock.isOutputShutdown()) {
					System.err.println("Flying f");
					break; // TODO FIXME creates an infinite loop which MURDERS cpu
				}
				System.out.println("a" + sc.hasNextLine() + " " + sc.ioException() + ", " + theirSock.isClosed() + ", " + theirSock.isConnected()); //TODO had c,a loop check it out alters
				if (!sc.hasNextLine()) {
					System.err.println("Like a boss");
					if (ourSock.isClosed())
						this.disconnect(true);
					else // TODO
						this.disconnect(false);
					connect();
					break;
				}
					
				while (sc.hasNextLine() && (l = sc.nextLine()) != null) {
					checkConnected();
					checkTheirSock();
					try {
						executeCommand(l);
					}catch (Exception e) {e.printStackTrace();}
				}
				if (!theirSock.isClosed())
					sc = new Scanner(theirSock.getInputStream());
				try {
					Thread.sleep(9001);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.err.println("shit");
		}
	}
	
	private void trySendDelayedMessages() throws IOException {
		if (checkConnected() && status != STATUS_HANDSHAKE 
				&& this.status != STATUS_OFFLINE 
				&& delayedMessages.size() > 0) { // assume we have fully connected
			for (String ss : delayedMessages)
				sendMessage("[Delayed] " + ss);
			delayedMessages.clear();
		}
	}

	private void sendProfileText() throws IOException {
		sendCommand("profile_text " + bl.tc.getProfile_text());
	}
	
	private void sendProfileName() throws IOException {
		sendCommand("profile_name " + bl.tc.getProfile_name());
	}
	
	private void executeCommand(String l) {
		try {
			String[] s = l.split(" ");
			if (l.startsWith("profile_avatar"))
				return;
			System.out.println(address + " >> " + l);
			if (s[0].equals("ping") && s[1].equals(address)) { // looks legit
				sendPong(s[2]);
			} else if (!this.recievedPong) {
				if (s[0].equals("pong") && s[1].equals(this.random1)) { 
					recievedPong = true;
				}
				return;  // dont want to continue if not fully connected
			} else if (l.startsWith("\\n")) { // continuation of previous message
//				if (gui == null) {
//					gui = new gui(this);
//					gui.setVisible(true);
//				} 
				if (!bl.getGui(address).isVisible())
					bl.getGui(address).setVisible(true);
				bl.getGui(address).getTextArea1().insert((getDisplayName(true) + ": " + l.replaceAll("\\\\n", "\n") + "\n").trim(), bl.getGui(address).getTextArea1().getText().length());
			} else if (s[0].equals("status")) {
//				if (parsingAvatar)
//					parsingAvatar = false;
				last_status_time = System.currentTimeMillis();
				if (s[1].equals("xa"))
					setStatus(Buddy.STATUS_XA);
				else  if (s[1].equals("away"))
					setStatus(Buddy.STATUS_OFFLINE);
				else  if (s[1].equals("available"))
					setStatus(Buddy.STATUS_ONLINE);
				trySendDelayedMessages();
//				avatar.doe();
			} else if (s[0].equals("message")) {
				if (!bl.getGui(address).isVisible())
					bl.getGui(address).setVisible(true);
				// TODO replace all this crap with openWindow effectivly
				bl.getGui(address).getTextArea1().insert(getDisplayName(true) + ": " + l.split(" ", 2)[1].replaceAll("\\\\n", "\n") + "\n", bl.getGui(address).getTextArea1().getText().length());
			} else if (s[0].equals("profile_text")) { 
				profile_text = l.split(" ", 2)[1];
			} else if (s[0].equals("client")) {
				client = l.split(" ", 2)[1];
			} else if (s[0].equals("version")) {
				version = l.split(" ", 2)[1];
			} else if (s[0].equals("profile_name")) { 
				profile_name = l.split(" ", 2)[1];
				if (bl.getGui(address) != null)
					bl.getGui(address).setTitle("Chat with " + getDisplayName(true));
			} 
//			else if (parsingAvataralpha)
//				avatar.parseLineA(l);
//			 else if (s[0].equals("profile_avatar_alpha")) {
//				parsingAvataralpha = true;
//				avatar = new Avatar();
//				avatar.parseLineA(l.split(" ", 2)[1]);
//			}
//			else if (parsingAvatar)
//				avatar.parseLine(l);
//			 else if (s[0].equals("profile_avatar")) {
//				parsingAvatar = true;
//				if (avatar == null)
//					avatar = new Avatar();
//				avatar.parseLine(l.split(" ", 2)[1]);
//			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int getStatus() {
		return status;
	}

	private void setStatus(int status) {
		this.pstatus = this.status;
		this.status = status;
		bl.onStatusUpdate(this, status);
	}

	public String getDisplayName(boolean b) { // b whether to resort to address
//		return (name == null ? this.address : name.equals("") ? this.address : this.profile_name.equals("") ? this.address : this.profile_name);
		return (!this.name.equals("") ? name : !this.profile_name.equals("") ? this.profile_name : b ? this.address : "");
	}
	
	@Override
	public String toString() {
		return this.address + " " + this.getDisplayName(false); // used in JTree
	}

	private void sendCommand(String s) throws IOException {
//		checkConnected();
		
		try {
			conn_out.write((s + "" + lf).getBytes());
			conn_out.flush();
		} catch (Exception e) {
			checkConnected();
			connect();
		}
//		String os = ourSock == null ? "ourSock == null" : (ourSock.isClosed() + ", " + ourSock.isConnected() + ", " + ourSock.isInputShutdown() + ", " + ourSock.isOutputShutdown());
//		String ts = theirSock == null ? "theirSock == null" : (theirSock.isClosed() + ", " + theirSock.isConnected() + ", " + theirSock.isInputShutdown() + ", " + theirSock.isOutputShutdown());
		System.out.println(
//				os + " || " + ts + " || " + 
				address + " << " + s);
	}
	
	private void sendPing() throws IOException {
		try {
//			System.out.println("guess wut");
			sendCommand("ping " + TorChat.us  + " " + random1);
		} catch (IOException e) {
			if (!connectThread.isAlive()) {
				connect();
			}
		}
		sentPing = true;
		count_unanswered_pings++;
	}
		
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	public void sendMessage(String s) throws IOException {
		if (!checkConnected() || this.status == STATUS_HANDSHAKE  || this.status == STATUS_OFFLINE)
			delayedMessages.add(s);
		else
			try {
				sendCommand("message " + s);
			} catch (IOException e) {
				if (!connectThread.isAlive()) {
					delayedMessages.add(s);
					connect();
				}
			}
	}
	
	private boolean checkConnected() {
		try {
			conn_out.write(new byte[0]);
			conn_out.flush();
			if (ourSock != null && ourSock.isConnected() && !ourSock.isOutputShutdown())
				return true;
		} catch (Exception e) {
			return false;
		}

		return false;
	}

	private void sendPong(String s) throws IOException {
		sendCommand("pong " + s);
//		sentPong = true;
	}
	
	void sendStatus() throws IOException {
		sendCommand("status " + bl.tc.getStatus());
	}
	
	private void sendAddMe() throws IOException {
		sendCommand("add_me ");
	}
	
	private void sendVersion() throws IOException {
		sendCommand("version " + bl.tc.getVersion()); //Over9000
	}
	
	private void sendClient() throws IOException {
		sendCommand("client " + bl.tc.getClient());
	}
	
	private boolean checkTheirSock() {
		if (theirSock.isConnected())
			return true;

		return false;
	}

//	public void registerGui(gui g) {
//		this.gui = g;
//		gui.setTitle("Chat with " + getDisplayName());
//	}

	public String getCookie() {
		return random1;
	}

	public void openWindow(boolean b) {
//		System.out.println("Open");
		if (!bl.getGui(address).isVisible())
			bl.getGui(address).setVisible(true);
		if (b)
			bl.getGui(address).toFront();
	}

	private void keepAlive() throws IOException {
//		System.out.println(String.format("(2) %s.keepAlive()", this.address));
		if ((System.currentTimeMillis() - this.last_status_time) > (Config.DEAD_CONNECTION_TIMEOUT*1000)) {
			System.out.println("Dead connection timeout " + (System.currentTimeMillis() - this.last_status_time) + " > " + (Config.DEAD_CONNECTION_TIMEOUT*1000));
			this.disconnect(false); // FIXME
			if (ourSock.isClosed())
				this.connect();
			else
				sendPing();
			return;
		}
        if (this.conn_out == null || this.ourSock == null || this.ourSock.isOutputShutdown()) {
        	this.connect();
        } else {
            if (this.conn_in != null && this.theirSock != null && !this.theirSock.isInputShutdown()) {
//            	System.out.println("Trying to status " + this);
            	if (this.recievedPong)
            		this.sendStatus();
            	else
            		this.sendPing();
//            	if  // TODO need to check time since last status
            } else {
                // still waiting for return connection
                if (this.count_unanswered_pings < Config.MAX_UNANSWERED_PINGS) {
                	this.sendPing();
//                    System.out.println(String.format("(2) unanswered pings to %s so far: %i", this.address, this.count_unanswered_pings));
                } else {
                    // maybe this will help
//                	System.out.println(String.format("(2) too many unanswered pings to %s on same connection", this.address)); 
                	this.disconnect(true);
                }
            }
        }
//		System.out.println(String.format("(2) %s.keepAlive() done", this.address));
	}

	private void disconnect(boolean fullDC) { // false - dc conn_in, true - dc both
		System.out.println("DC " + fullDC + " | " + this);
		this.count_unanswered_pings = 0;
		this.last_status_time = System.currentTimeMillis() + (Config.DEAD_CONNECTION_TIMEOUT / 2);
//		Thread.dumpStack();
		try {
			if (fullDC)
				conn_out.close();
		} catch (Exception e) {}
		try {
			conn_in.close();
		} catch (Exception e) {}
		try {
			if (fullDC)
				ourSock.close();
		} catch (Exception e) {}
		try {
			theirSock.close();
		} catch (Exception e) {}
	}

	public String getProfile_name() {
		return profile_name;
	}

	public String getProfile_text() {
		return profile_text;
	}

	public String getClient() {
		return client;
	}

	public String getVersion() {
		return version;
	}

	public void onAddToGroup(BuddyGroup bg) {
		this.groups.put(bg.getName(), bg);
	}
	
	public void log (String s) {
		int i = Integer.parseInt(s.split("(")[1].split(")")[0]);
		if (i > 2)
			System.out.println(s);
	}

	/**
	 * @return the groups
	 */
	public HashMap<String, BuddyGroup> getGroups() {
		return groups;
	}

	public void delete() {
		node.removeFromParent();
		bl.remove(this);
		for (BuddyGroup bg : this.groups.values()) {
			bg.budNodes.get(this.getAddress()).removeFromParent();
			bg.remove(this);
			try {
				bl.saveBuddies();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		bl.blg.getTree1().updateUI();
	}
}
