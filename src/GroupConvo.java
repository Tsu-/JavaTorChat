import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Random;


public class GroupConvo {
	private HashMap<String, Buddy> p = new HashMap<String, Buddy>();
	private String id; 
	
	public GroupConvo() {
		this.id = Long.toString(new Random().nextLong(), 36);
		 System.out.println("new group with id " + id);
	}
	
	public GroupConvo(String id) {
		 this.id = id;
		 System.out.println("joined group with id " + id);
	}
	
	public void addParticipant(Buddy b) {
		p.put(b.getAddress(), b);
	}

	public void sendMessage(String msg) {
		// TODO Auto-generated method stub
		
	}
	
	public void relayMessage(Buddy f, String msg) {
		// TODO Auto-generated method stub
		for (Buddy b : p.values()) {
			if (!b.equals(f)) {
				try {
					b.sendCommand("group " + id + " " + new BigInteger(f.getAddress()).toString(36) + " " + msg);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
