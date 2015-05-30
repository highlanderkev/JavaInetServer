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
 * InetServer.java
 * Attributed to Elliott, after Hughes, Shoffner, Winslow with alterations by Westropp
 */

import java.io.*; 
import java.net.*;

/**
 * Class definition for worker class, extends the Java thread class.
 * This class handles the each thread created by the Server Class.
 * @author Kevin Patrick Westropp
 */
class Worker extends Thread {

    /* Class member, socket, local to Worker. */
    Socket sock;

    /** 
     * Constructor to assign s to local socket
     */ 
    Worker(Socket s) {
        sock = s;
    }     

    /**
     * Get I/O streams from the socket,
     * override annotation to override java.lang.Thread
     * which is inherited from parent class.
     */
    @Override
    public void run() {
        PrintStream out = null; // intialize and set new PrintStream out to null
        BufferedReader in = null; // intialize and set new BufferedReader in to null
        try {
            out = new PrintStream(sock.getOutputStream()); 
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            // Note that is is remotely possible that this branch will not execute
            if (InetServer.controlSwitch != true) {
                System.out.println("Listener is now shutting down as per client request.");
                System.out.println("Server is now shutting down. Goodbye!");
            } else {
                try {
                    String name;
                    name = in.readLine();
                    if (name.indexOf("shutdown") > -1) {
                        InetServer.controlSwitch = false;
                        System.out.println("Worker has captured a shutdown request.");
                        System.out.println("Shutdown request has been noted by worker.");
                        System.out.println("Please send final shutdown request to listener.");
                    } else {
                        System.out.println("Looking up " + name);
                        printRemoteAddress(name, out);
                    }
                } catch (IOException x) {
                    System.out.println("Server read error");
                    x.printStackTrace();    // prints Exception Stack Trace. */
                }
            }

            sock.close();   // close this connection, but not the server. */
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }

    /**
     * This method prints out the remote address
     * requested by the user/client.
     * @param name of remote address to lookup
     * @param out not used in current context
     */
    static void printRemoteAddress(String name, PrintStream out) {
        try {
            System.out.println("Looking up " + name + "...");
            InetAddress machine = InetAddress.getByName(name);
            System.out.println("Host name : " + machine.getHostName());
            System.out.println("Host IP : " + toText(machine.getAddress()));
        } catch (UnknownHostException ex) {
            System.out.println("Failed in attempt to look up " + name);
        }
    }

    /**
     * This method handles translation of the IP address 
     * into proper format - make portable for 128 bit format. 
     * @param ip address
     * @return Properly formatted String of IP address
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
}

/**
 * A multi-threaded server for InetClient which can handle 
 * multiple requests/clients for IP Address and DNS Name lookup,
 * by using the worker class above.
 */
public class InetServer {

    public static boolean controlSwitch = true; // set to true on startup

    public static void main(String a[]) throws IOException {
        int q_len = 6; // Number of requests for OpSys to queue */
        int port = 1699; // start listening on port 1699 */
        Socket sock; // intialize Socket variable sock */

        ServerSocket servsock = new ServerSocket(port, q_len); //intialize and set a new server socket to servsock */

        System.out.println("Kevin Westropp's Inet server starting up, listening at port 1699.\n");
        while (controlSwitch) {
            // wait for the next client connection;
            sock = servsock.accept(); // accept/listen for incoming connections
            new Worker(sock).start(); // new worker(thread) to handle client
            // Uncomment to see shutdown bug 
            // try{ Thread.sleep(10000); } catch(InterruptedException ex){}
        }
    }
}
