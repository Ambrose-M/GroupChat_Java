import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class ReadThread implements Runnable 
{ 
	private MulticastSocket socket; 
	private InetAddress group; 
	private int port; 
	private static final int MAX_LENGTH = 1000; 
	
	ReadThread(MulticastSocket socket, InetAddress group, int port) 
	{ 
		this.socket = socket; 
		this.group = group; 
		this.port = port; 
	} 
	
	@Override
	public void run() 
	{ 
		
		while(GroupChat.open) 
		{ 
				String message; 
				
				byte[] buffer = new byte[ReadThread.MAX_LENGTH]; 
				
				DatagramPacket datagram = new DatagramPacket(buffer, buffer.length, group,port); 
			try
			{ 
				socket.receive(datagram); 
				
				message = new String(buffer, 0, datagram.getLength(),"UTF-8"); 
				
				//Decrypt message
				GroupChat.cipher.decrypt(message);
				
				if(!message.startsWith(GroupChat.username)) 
				{
					System.out.println(message); 
				}

			} 
			catch(IOException e) 
			{ 
				System.out.println("Socket closed."); 
			} 
		} 
	} 
}
