import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.util.Random;

public class Client  implements Runnable {


	private static DataInputStream input;
	private static DataOutputStream output;

	public void run() {
		Random random = new Random();
		BigInteger k = GenerateVariablse.getK();
		BigInteger g = GenerateVariablse.getG();
		BigInteger N = GenerateVariablse.getN();
		
		try {
			Socket clientSocket = new Socket("127.0.0.1",1234);
			output = new DataOutputStream(clientSocket.getOutputStream());
			input = new DataInputStream(clientSocket.getInputStream());
			
			//данные при регистрации клиента, отправленные на сервер
			String username = "kalash";
			String password = "kalash135";
			String salt = GenerateVariablse.randomString(10);
			int x = GenerateVariablse.Hash((password + salt).hashCode());
			BigInteger passwordVerifier = g.pow(x).mod(N);
			
			System.out.println("[CLIENT]: Input data registration");
			System.out.println("_______________________");
			System.out.println("Username: " + username);
			System.out.println("Password: " + password);
			System.out.println("Salt: " + salt);
			System.out.println("_______________________");
			
			output.writeUTF(salt);
			output.writeInt(passwordVerifier.intValue());
			output.flush();
		
			int a = random.nextInt(98) + 2;
			BigInteger A = g.pow(a).mod(N);
			
	
			output.writeInt(A.intValue());
			output.flush();
			System.out.println("[CLIENT]: A = " + A);
			
			BigInteger B = BigInteger.valueOf(input.readInt());
			
			BigInteger u = GenerateVariablse.Scramble(A.add(B));
			BigInteger S = B.subtract(k.multiply(g.pow(x).mod(N))).pow(BigInteger.valueOf(a).add(u.multiply(BigInteger.valueOf(x))).intValue()).mod(N);
			BigInteger M = N.xor(g).add(BigInteger.valueOf(salt.hashCode())).add(A).add(B).add(S).mod(N);
			System.out.println("[CLIENT]: S = " + S + ", M = " + M);
			output.writeInt(M.intValue());
			output.flush();
			
			int R = A.add(M).add(S).intValue();
			System.out.println("[CLIENT]: R = " + R);
			int serverR = input.readInt();
			
			if (R != serverR) {
				throw new Exception("server R != client R");
			}
			
			System.out.println("[CLIENT]: Authentication to server successfull!");
			
			output.close();
			input.close();
			clientSocket.close();
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
