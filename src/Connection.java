import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;


public class Connection extends Thread {
	
	private Socket s;
	private TorChat tc;

	public Connection(Socket s, TorChat tc) {
		this.s = s;
		this.tc = tc;
	}
	
	public void run() {
//		System.out.println(this + " new Thread Conenction");
		try {
			Scanner sc = new Scanner(s.getInputStream());
			String l = sc.nextLine();
			
			System.out.println(s.getRemoteSocketAddress() + " | " + l);
//			if (l.split(" ")[1].equals("6k7b5iqj6bwwzd4f")) {
			System.out.println(l);
			if (tc.getBl().hasBuddy(l.split(" ")[1])) {
				 tc.getBl().getBuddy(l.split(" ")[1]).attatch(s, l, sc);
//				TorChat.t.attatch(s, l, sc);
			} else {
				Buddy b = new Buddy(l.split(" ")[1], "", tc.getBl().hasBuddy(l.split(" ")[1]), tc.getBl(), tc);
				tc.getBl().addBuddy(b, true);
				b.connect();
				b.attatch(s, l, sc);
			}
//			}
		} catch (IOException e) {
			e.printStackTrace();
		}
//		System.out.println(this + " died");
	}

}
