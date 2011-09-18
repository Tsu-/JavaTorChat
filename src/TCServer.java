import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


public class TCServer extends Thread{

	private boolean isRunning;
	private ServerSocket sc;
	private TorChat tc;

	public TCServer(TorChat tc) {
		this.tc = tc;
		isRunning = true;
		try {
			sc = new ServerSocket(Config.LOCAL_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		 // can be anything, but must be set in torrc
	}
	
	public void run() {
		try {
//			checkServer();
			while(isRunning && !sc.isClosed()) {
//				System.out.println("Server up.");
				Socket s = sc.accept();
//				System.out.println("New connection from " + s.getRemoteSocketAddress());
				new Connection(s, tc).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void checkServer() {
		new Thread(new Runnable() {
			public void run() {
				Socket t = new Socket(new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("127.0.0.1", 11109)));

				System.out.println("test " + TorChat.us + ".onion:11009");
				try {
					t.connect(new InetSocketAddress(TorChat.us + ".onion", 11009));
					OutputStream o = t.getOutputStream();
					o.write("works\r\n".getBytes());
					o.flush();
				} catch (IOException e) {
					e.printStackTrace();
				} // connect to self
			}
		}).start();
		try {
			while(sc == null)
				Thread.sleep(1000);
			System.out.println("s");
			Socket s = sc.accept();
			Scanner sc = new Scanner(s.getInputStream());
			String l = sc.nextLine();
			System.out.println("RESPONSE " + l);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
