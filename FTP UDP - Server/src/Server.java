import java.io.*;
import java.net.*;

public class Server {

	public static void main(String[] args) {
		
		DatagramSocket socket = null;
		DatagramPacket in = null;
		DatagramPacket out = null;
		byte[] inBuf, outBuf;
		String msg;
		final int PORT = 50000;
		
		try {
			socket = new DatagramSocket(PORT);
			while (true){
				System.out.println("\nRunning... \n");
				
				inBuf = new byte[100];
				in = new DatagramPacket(inBuf, inBuf.length);
				socket.receive(in);
				
				int source_port = in.getPort();
				InetAddress source_address = in.getAddress();
				msg = new String(in.getData(), 0, in.getLength());
				System.out.println("Client : " +source_address + ":" + source_port);
				
				String dir = "D:/Text File";
				File f1 = new File(dir);
				File fl[] = f1.listFiles();
				
				StringBuilder sb = new StringBuilder("\n");
				int c = 0;
				
				for(int i=0; i<fl.length; i++) {
					if(fl[i].canRead())
						c++;
				}
				
				sb.append(c+" Files Found.\n\n");
				
				for(int i=0; i<fl.length; i++) {
					sb.append(fl[i].getName()+" "+fl[i].length()+" Bytes\n");
				}
				
				sb.append("\nMasukkan nama file untuk didownload : ");
				outBuf = (sb.toString()).getBytes();
				out = new DatagramPacket(outBuf, 0, outBuf.length, source_address, source_port);
				socket.send(out);
				
				inBuf = new byte[100];
				in = new DatagramPacket(inBuf, inBuf.length);
				socket.receive(in);
				String filename = new String(in.getData(), 0, in.getLength());
				
				System.out.print("File yang diminta : "+filename);
				
				boolean flis = false;
				int index=-1;
				sb=new StringBuilder("");
				for(int i=0; i<fl.length; i++) {
					if(((fl[i].getName()).toString()).equalsIgnoreCase(filename)){
						index = i;
						flis = true;
					}
				}
				
				if(!flis) {
					System.out.println("ERROR");
					sb.append("ERROR");
					outBuf = (sb.toString()).getBytes();
					out = new DatagramPacket(outBuf, 0, outBuf.length, source_address, source_port);
					socket.send(out);
				}
				else {
					try {
						File ff = new File(fl[index].getAbsolutePath());
						FileReader fr = new FileReader(ff); 
						BufferedReader brf = new BufferedReader(fr); 
						String s = null;
						sb = new StringBuilder();
						
						while((s=brf.readLine())!=null) {
							sb.append(s);
						}
						
						if(brf.readLine()==null) {
							Thread.sleep(10000);
							System.out.println("\nFile Read Successfull. Closing Socket");
						}
						
						outBuf = new byte[100000];
						outBuf = (sb.toString()).getBytes();
						out = new DatagramPacket(outBuf, 0, outBuf.length, source_address, source_port);
						socket.send(out);
					}
					catch (IOException ex) {
						System.out.println(ex);
					}
				}
			}
		}
		catch (Exception e) {
			System.out.println("Error \n");
		}
	}
}