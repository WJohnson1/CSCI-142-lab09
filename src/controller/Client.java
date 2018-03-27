package controller;

import java.io.*;
import java.net.Socket;

/**
 * The Client class creates a client socket and allows
 * reads and writes to it via the Peer interface.
 *
 * @author Bruce Herring
 */
public class Client extends Peer {

    /**
     * Constructor. Creates and connects the socket to the
     * specified server.
     *
     * @param address Server address.
     * @param port Server port.
     * @throws IOException when bad things happen to the streams
     */
    public Client (String address, int port) throws IOException {

        sock = new Socket (address, port);

        out = new DataOutputStream (sock.getOutputStream());
        input = new DataInputStream(sock.getInputStream());

    }
}