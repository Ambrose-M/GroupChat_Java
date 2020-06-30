import java.net.*;
import java.io.*;
import java.util.*;

public class GroupChat
{
	static String username;
	private static final String EXIT = "exit"; //For exiting the Group Chat
	
	private final static int MAXINFO = 2; //Max size for serverInfo array
	private static String serverInfo[] = new String[MAXINFO]; //holds host IP address and port number
	
	static CaesarCipher cipher = new CaesarCipher(); //Encrypts and decrypts messages
	
	static volatile boolean open = true; //Indicates that the socket is open
	private static Scanner keyboard = new Scanner(System.in); //Reads user input
	

	/**
	 * Runs the GroupChat program.
	 * @param args
	 */
	public static void main(String args[]) 
	{
		System.out.println("Welcome to the Group Chat. When you want to leave type 'exit'.");
		
		getServerInfo();
		
		runGroupChat(serverInfo);
	}
	

	/**
	 * Saves host IP address and port number given by user.
	 * 
	 */
	private static void getServerInfo()
	{
		//FOR TESTING
		System.out.println("Enter host IP address: 239.0.0.0");
		serverInfo[0] = "239.0.0.0";
		
		//ACTUAL
		//System.out.print("Enter host IP address: ");
		// serverInfo[0] = keyboard.nextLine();

		//FOR TESTING
		System.out.println("Enter a port number: 1234");
		serverInfo[1] = "1234"; // for test. uncomment next line.
		
		//ACTUAL
		//System.out.print("Enter a port number: ");
		// serverInfo[1] = keyboard.nextLine();
	}
	
	/**
	 * Sets up socket and thread. Sends message data to Socket. Can exit Group Chat by typing 'exit'.
	 * 
	 * @param serverInfo Contains IP address and port number
	 */
	private static void runGroupChat(String[] serverInfo)
	{
		if (serverInfo.length != 2)
		{
			System.out.println("Two arguments required: <host-IP-address> <port-number>");
		}	
		else 
		{
			try 
			{
				//Initializing address for socket
				InetAddress group = InetAddress.getByName(serverInfo[0]);

				//initializing port for socket
				int port = Integer.parseInt(serverInfo[1]);

				//User enters their name
				System.out.print("Enter your username: ");
				username = keyboard.nextLine();

				//Create socket using port
				MulticastSocket socket = new MulticastSocket(port);

				socket.setTimeToLive(0);

				//Join socket to IP address
				socket.joinGroup(group);

				//Create new thread
				Thread thread = new Thread(new ReadThread(socket, group, port));

				//Start thread
				thread.start();

				System.out.println("Login successful. Start typing...\n");

				//Sends text to server while socket is open.
				while (open) 
				{
					String message;
					message = keyboard.nextLine();

					// type 'exit' to leave group chat
					if (message.equalsIgnoreCase(GroupChat.EXIT)) 
					{
						open = false;

						message = username + " left the Group Chat.";
						
						//Encrypting message before sending over network
						cipher.encrypt(message);

						byte[] buffer = message.getBytes();

						DatagramPacket datagram = new DatagramPacket(buffer, buffer.length, group, port);

						socket.send(datagram);

						socket.leaveGroup(group);
						socket.close();

						System.out.println("You left the Group Chat.");
						break;
					}

					message = username + ": " + message;
					
					//Encrypting message before sending over network
					cipher.encrypt(message);

					byte[] buffer = message.getBytes();

					DatagramPacket datagram = new DatagramPacket(buffer, buffer.length, group, port);

					socket.send(datagram);
				}
				
			//Catching exceptions
			} catch (SocketException se) {
				System.out.println("Error creating socket.");
				se.printStackTrace();
				
			} catch (IOException ie) {
				System.out.println("Error reading or writing socket.");
				ie.printStackTrace();
			}
		}
	}
}