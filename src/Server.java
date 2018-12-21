import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class Server implements Runnable{

	private static DataInputStream input;
	private static DataOutputStream output;
	private static boolean running = false;
	private static Thread thread;
	
	public void start() {
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public void stop() {
		running = true;
		thread.interrupt();
	}
	
	public void run() {
		Random random = new Random();
		BigInteger k = GenerateVariablse.getK();
		BigInteger g = GenerateVariablse.getG();
		BigInteger N = GenerateVariablse.getN();
		
		try {
			ServerSocket serverSocket = new ServerSocket(1234);
			
			while (running) {
				Socket clientSocket = serverSocket.accept();
				
				output = new DataOutputStream(clientSocket.getOutputStream());
				input = new DataInputStream(clientSocket.getInputStream());
				
				String salt = input.readUTF();
				BigInteger passwordVerifier = BigInteger.valueOf(input.readInt());
				System.out.println("[SERVER]: Registration sucsessfull");
				
				int b = random.nextInt(98) + 2;
				BigInteger B = k.multiply(passwordVerifier).add(g.pow(b).mod(N)).mod(N);
				output.writeInt(B.intValue());
				output.flush();
				System.out.println("[SERVER]: B = " + B);
				
				BigInteger A = BigInteger.valueOf(input.readInt());
				
				BigInteger u = GenerateVariablse.Scramble(A.add(B));
				BigInteger S = A.multiply(passwordVerifier.pow(u.intValue()).mod(N)).pow(b).mod(N);
				BigInteger M = N.xor(g).add(BigInteger.valueOf(salt.hashCode())).add(A).add(B).add(S).mod(N);
				
				System.out.println("[SERVER]: S = " + S + ", M = " + M);
				
				BigInteger clientM = BigInteger.valueOf(input.readInt());
				
				if (!clientM.equals(M)) {
					throw new Exception("client M != server M");
				}
				
				int R = A.add(M).add(S).intValue();
				System.out.println("[SERVER]: R = " + R);
				output.writeInt(R);
				output.flush();
				
				System.out.println("[SERVER]: Client logged in!");
				
				
				output.close();
				input.close();
				clientSocket.close();
			}
			serverSocket.close();
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}
