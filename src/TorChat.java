import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Scanner;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class TorChat {
	private String profile_name = "";
	private String client = "JavaTorChat";
	private String profile_text = "";
	private String version = "dev";
	private String status = "available";

	public String getProfile_name() {
		return profile_name;
	}

	public void setProfile_name(String profile_name) {
		this.profile_name = profile_name;
	}

	public String getProfile_text() {
		return profile_text;
	}

	public void setProfile_text(String profile_text) {
		this.profile_text = profile_text;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getClient() {
		return client;
	}

	public String getVersion() {
		return version;
	}

	private BuddyListGui blg = new BuddyListGui();
	private BuddyList bl = new BuddyList(this, blg);
	protected Process torProccess;

	public BuddyList getBl() {
		return bl;
	}

	public static String us = "whi3dc7xmy7bqg7w";

	public TorChat() throws IOException {
		if (!Config.TESTING)
			loadTor();
		TCServer tsc = new TCServer(this);
		tsc.start();
		bl.loadBuddies();
		GroupConvo g = new GroupConvo();
		
		System.out.println(new BigInteger("fs6qgtdynha3p6rn", 36).toString());
//		bl.loadBuddiesOL();
		

		debug();
	}

	private void debug() {
		Scanner s = new Scanner(System.in);
		while(s.hasNextLine()) {
			String l = s.nextLine();
//			if (l.startsWith("version ")) {
//				this.version = l.split(" ", 2)[1];
//			}
//			if (l.startsWith("client ")) {
//				this.client = l.split(" ", 2)[1];
//			}
			if (l.startsWith("lblol")) {
				bl.loadBuddiesOL();
			}
//			if (l.startsWith("ping ")) {
//				Buddy b = bl.getBuddy(l.split(" ")[1]);
//				try {
//					b.sendStatus();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
		}
	}

	private void loadTor() {
		final Thread uzz = Thread.currentThread();
		new Thread() {
			public void run() {
				System.out.println("Attempting to start Tor!");
				ProcessBuilder pb = new ProcessBuilder(new String[] {
						"Tor/tor.exe", "-f", "torrc.txt" });

				pb.redirectErrorStream(true);
				pb.directory(new File("Tor/").getAbsoluteFile());

				System.out.println(String.format("Launching %s %s", new File(
						".Tor/tor.exe").getAbsoluteFile().getPath(),
						"-f torrc.txt"));
				StringBuilder sb = new StringBuilder();
				for (String s : pb.command()) {
					if (sb.length() > 0) {
						sb.append(' ');
					}
					if (s.contains(" ")) {
						sb.append('"');
						sb.append(s);
						sb.append('"');
					} else {
						sb.append(s);
					}
				}
				try {
					torProccess = pb.start();
					Runtime.getRuntime().addShutdownHook(new Thread() {
						public void run() {
							torProccess.destroy();
						}
					});
					if (torProccess != null) {
						BufferedReader input = new BufferedReader(
								new InputStreamReader(
										torProccess.getInputStream()));
						String line;
						while ((line = input.readLine()) != null) {
							if (line.contains("Bootstrapped 100%: Done."))
								uzz.interrupt();
							if (!line.contains("hidden service is unavailable")
									&& !line.contains("Tried for 120 seconds to get a connection to"))
								System.out.println(String.format("Tor: %s", line));
						}
						torProccess.waitFor();
						if (torProccess.exitValue() != 0)
							System.out.println(String.format(
									"Tor exited with status %d",
									torProccess.exitValue()));
					}
				} catch (InterruptedException e) {
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
		System.out.println("Pausing main thread, waiting for Tor to start.");
		try {
			Thread.sleep(Long.MAX_VALUE);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println("Resuming main thread!");
		}
		if (new File("Tor/hidden_service/hostname").exists()) {
			try {
				String x = new Scanner(new FileInputStream("Tor/hidden_service/hostname")).nextLine();
				if (x != null && !x.equals("")) {
					TorChat.us = x.replace(".onion", "");
					System.out.println("Using hostname " + x.replace(".onion", ""));
					bl.blg.setTitle(TorChat.us);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public BuddyListGui getBlg() {
		return blg;
	}

	public static void main(String[] args) throws IOException {
		// Thread.setDefaultUncaughtExceptionHandler(eh);
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			public void uncaughtException(Thread t, Throwable e) {
				if (e.getStackTrace()[0].getFileName().startsWith(
						"javax.swing.plaf")) {
					// do nothing
					System.err.println("a");
				} else {
					e.printStackTrace();
				}
			}
		});

		setLAF(); // set the look and feel
		new TorChat();
	}

	private static void setLAF() {
		try {
			// Set System L&F
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
			// handle exception
		} catch (ClassNotFoundException e) {
			// handle exception
		} catch (InstantiationException e) {
			// handle exception
		} catch (IllegalAccessException e) {
			// handle exception
		}
	}

	public void exit() {
		// TODO Auto-generated method stub
		try {
			bl.saveBuddies();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		torProccess.destroy();
		System.exit(0);
	}

}
