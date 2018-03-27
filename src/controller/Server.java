package controller;

import java.io.*;
import java.net.ServerSocket;

/**
 * The Server class waits for an incoming client connection
 * and then creates a local socket for peer communications.
 *
 * @author Bruce Herring
 */
public class Server extends Peer {

    /**
     * Constructor. Waits for a client to connect and
     * then creates a socket for that connection. After
     * accepting the connection the server will close.
     *
     * @param port Port to listen for clients on.
     * @throws IOException if bad things happen to the socket
     */
    public Server (int port) throws IOException {

        try (ServerSocket listener = new ServerSocket(port)) {

            try {
                sock = listener.accept();
                out = new DataOutputStream (sock.getOutputStream ());
                input = new DataInputStream (sock.getInputStream ());
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
