import java.util.Random;


public class Methods {
	public static int random(Random r, int min, int max) {
		int n = Math.abs(max - min);
		return Math.min(min, max) + (n == 0 ? 0 : r.nextInt(n));
	}
}
