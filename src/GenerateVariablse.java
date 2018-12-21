import java.math.BigInteger;
import java.util.Random;

public class GenerateVariablse {
	
	private static BigInteger g = BigInteger.valueOf(311);
	private static BigInteger N = BigInteger.valueOf(473);
	private static BigInteger k = BigInteger.valueOf(3);
	
	public static BigInteger getG() {
		return g;
	}
	
	public static BigInteger getK() {
		return k;
	}
	
	public static BigInteger getN() {
		return N;
	}
	
	public static String randomString(int length) {
		String All = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		StringBuilder sb = new StringBuilder( length );
		Random random = new Random();
		   for( int i = 0; i < length; i++ ) 
		      sb.append( All.charAt( random.nextInt(All.length()) ) );
		   return sb.toString();
	}
	
	public static BigInteger Scramble(BigInteger c) {
		return c.mod(BigInteger.valueOf(100));
	}
	
	public static int Hash(int c) {
		return Math.abs(c) % 100;
	}
}
