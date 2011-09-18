
public class Config {
	public static final boolean TESTING = false;
	public static final int DEAD_CONNECTION_TIMEOUT = 240;
	public static final int KEEPALIVE_INTERVAL = 120;
	public static final int MAX_UNANSWERED_PINGS = 4;
	public static final int SOCKS_PORT = TESTING ? 11599 : 11157; // 11599, 11157
	public static final int LOCAL_PORT = TESTING ? 8974 : 8975; // 11599, 11157
}
