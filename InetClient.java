/*--------------------------------------------------------
1. Name / Date: Kevin Patrick Westropp 1/13/2013

2. Precise command-line compilation examples / instructions:

> javac InetServer.java
> javac InetClient.java
> javac InetAddresser.java

3. Precise examples / instructions to run this program:

In separate shell windows:

> java InetServer
> java InetClient
> java InetAddresser //this is a standalone without the server

All acceptable commands are displayed on the various consoles.

This runs across machines, in which case you have to pass the IP address of
the server to the clients. For exmaple, if the server is running at
140.192.1.22 then you would type:

> java JokeClient 140.192.1.22
> java JokeClientAdmin 140.192.1.22

4. Java version used, if not typical for the class: Java 1.7

5. List of files needed for running the program.
 a. InetServer.java
 b. InetClient.java
 c. InetAddresser.java

5. Notes: Ran it on two machines, both running Linux.
Also ran it on my PC in Netbeans, worked in both instances,
although I noticed if you don't put .com or a valid suffix
it will come back as a default IP address. I also had some
come back failed when I ran it on my Linux machines which I 
am guessing is from a bad connection (over Wifi). 
----------------------------------------------------------*/
/**
 * InetClient.java
 * Attributed to Elliott, after Hughes, Shoffner, Winslow with alterations by Westropp
 */

import java.io.*;
import java.net.*;

/**
 * This class is to be used with the Inet Server - a multi-threader server,
 * used to lookup IP addresses and DNS names.
 * @author Kevin Patrick Westropp
 */
public class InetClient {

    public static void main(String args[]) {
        String serverName;  // String variable for storing server name = localhost - whatever computer is running InetServer. */
        if (args.length < 1) {
            serverName = "localhost";
        } else {
            serverName = args[0];
        }
        System.out.println("Kevin Westropp's Inet Client.\n");
        /* PrintLocalAddress(); // Uncomment to print local address */
        System.out.println("Using server: " + serverName + ", Port: 1699");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        try {
            String name; // String variable for saving user input. */
            do {
                System.out.print("Enter a hostname or an IP address, (quit) to end: "); // Prompt user for a hostname/IP to lookup. */
                System.out.flush(); // forces output to be written to System.out */
                name = in.readLine(); // reads the input/response from user
                if (name.indexOf("quit") < 0) { // if name(input from user) is not quit then 
                    getRemoteAddress(name, serverName);
                }
            } while (name.indexOf("quit") < 0);
            System.out.println("Cancelled by user request.");
        } catch (IOException x) {
            x.printStackTrace(); // prints Exception Stack Trace. */
        }
    }

    /**
     * This method prints out the local IP address and name.
     */
    static void printLocalAddress() {
        try {
            InetAddress me = InetAddress.getLocalHost(); // gets local host and assign it to variable me */
            System.out.println("My local name is:      " + me.getHostName());
            System.out.println("My local IP address is: " + toText(me.getAddress()));
        } catch (UnknownHostException x) {
            System.out.println("I appear to be unknown to myself. Firewall?:");
            x.printStackTrace(); // prints Exception Stack Trace. */
        }
    }

    /**
     * This method makes a string of the IP address
     * using a StringBuffer/StringBuilder.
     * @param ip array
     * @return String of ip array
     */
    static String toText(byte ip[]) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < ip.length; i++) {
            if (i > 0) {
                result.append(".");
            }
            result.append(0xff & ip[i]);
        }
        return result.toString();
    }

    /**
     * This method gets the Remote Address requested by the user.
     * @param name of input by user
     * @param serverName = local host
     */
    static void getRemoteAddress(String name, String serverName) {
        Socket sock;
        BufferedReader fromServer;
        PrintStream toServer;
        String textFromServer;

        try {
            /*Open a connection to server port, choose your own port number - 1699 */
            sock = new Socket(serverName, 1699);

            //Create filter I/O streams for the socket */
            fromServer = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            toServer = new PrintStream(sock.getOutputStream());

            // Send machine name or IP address to server
            toServer.println(name); 
            toServer.flush();

            // Read two or three lines of response from the server,
            // and block while synchronously waiting. */
            for (int i = 1; i <= 3; i++) {
                textFromServer = fromServer.readLine();
                if (textFromServer != null) {
                    System.out.println(textFromServer);
                }
            }
            
            sock.close(); // close the socket/connection 
        } catch (IOException x) { 
            System.out.println("Socket error.");
            x.printStackTrace(); // prints stack trace of exception. */
        }
    }
}
