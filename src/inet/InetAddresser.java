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
 * InetAddresser.java
 * Attributed to Elliott, after Hughes, Shoffner, Winslow with alterations by Westropp
 */

import java.io.*;
import java.net.*;

/**
 * Public Class InetAddresser, a stand alone non-networking program for finding
 * IP addresses and DNS names.
 *
 * @author Kevin Patrick Westropp
 */
public class InetAddresser {

    /**
     * Main method prints, local address then waits for input to find and print
     * an IP address and DNS name.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        printLocalAddress();

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        try {
            String name; // local variable to hold user input. */
            do {
                System.out.print("Enter a hostname or an IP address, (quit) to end: ");
                System.out.flush(); // flush output. */
                name = in.readLine(); // save input from user to variable name. */
                if (name.indexOf("quit") < 0) {
                    printRemoteAddress(name); // calls method to print remote address. */
                }
            } while (name.indexOf("quit") < 0);
            System.out.println("exit");
        } catch (IOException x) {
            x.printStackTrace();    // prints Exception Stack Trace. */
        }
    }

    static void printLocalAddress() {
        try {
            System.out.printf("Kevin Westropp's Inet addresser program\n");
            InetAddress me = InetAddress.getLocalHost();
            System.out.println("My local name is:     " + me.getHostName());
            System.out.println("My local IP address is:   " + toText(me.getAddress()));
        } catch (UnknownHostException x) {
            System.out.println("I appear to be unknown to myself. Firewall?:");
            x.printStackTrace();    // prints Exception Stack Trace. */
        }
    }

    /**
     * This method prints out the remote address
     * requested by the user.
     * @param name of remote address to lookup
     */
    static void printRemoteAddress(String name) {
        try {
            System.out.println("Looking up " + name + "...");
            InetAddress machine = InetAddress.getByName(name);
            System.out.println("Host name : " + machine.getHostName());
            System.out.println("Host IP : " + toText(machine.getAddress()));
        } catch (UnknownHostException ex) {
            System.out.println("Failed to lookup " + name);
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
            if (i < 0) {
                result.append(".");
            }
            result.append(0xff & ip[i]);
        }
        return result.toString();
    }
}
