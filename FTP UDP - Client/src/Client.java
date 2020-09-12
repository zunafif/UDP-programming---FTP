import java.io.*;
import java.net.*;
import java.util.Scanner;


public class Client {

	public static void main(String[] args) {
		DatagramSocket socket = null;
		DatagramPacket in = null;
		DatagramPacket out = null;
		byte[] inBuf, outBuf;
		final int PORT = 50000; 
		String msg = null;
		Scanner src = new Scanner(System.in);
		
		String dire = System.getProperty("user.dir");
		System.out.print("\nDirecotry file download : " + dire);
		
		try {
			
			InetAddress address = InetAddress.getByName("127.0.0.1");
			socket = new DatagramSocket();
			
			msg = "";
			outBuf = msg.getBytes();
			out = new DatagramPacket(outBuf, 0, outBuf.length, address, PORT);
			socket.send(out);
			
			inBuf = new byte[65535];
			in = new DatagramPacket(inBuf, inBuf.length);
			socket.receive(in);
			
			String data = new String(in.getData(), 0, in.getLength());
			System.out.println(data);
			
			String filename = src.nextLine(); 
			outBuf = filename.getBytes();
			out = new DatagramPacket(outBuf, 0, outBuf.length, address, PORT);
			socket.send(out);
			
			inBuf = new byte[100000];
			in = new DatagramPacket(inBuf, inBuf.length);
			socket.receive(in);
			
			data = new String(in.getData(), 0, in.getLength());
			if (data.endsWith("ERROR")) {
				System.out.println("Data tidak ditemukan\n");
				socket.close();
			}
			else {
				try {
					BufferedWriter pw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename)));
					pw.write(data);
					pw.close();
					
					Thread.sleep(10000);
					
					System.out.println("File write successfull. closing socket");
					socket.close();
				}
				catch (IOException e) {
					System.out.println("File Error \n");
					socket.close();
				}
			}
		}
		catch (Exception e) {
			System.out.println("\nNetwork Error. Coba lagi nanti\n");
		}
	}
}