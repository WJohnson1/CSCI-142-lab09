package controller;

import java.io.*;
import java.net.Socket;

/**
 * Class used to encapsulate interactions with the
 * other player.
 * 
 * @author Bruce Herring
 */
public abstract class Peer {

    protected DataOutputStream out;
    protected DataInputStream input;
    protected Socket sock;

    /**
     * Receives one character from a message sent by
     * the other player.
     * 
     * @return The character that was received or the null
     *         character if something went wrong
     */
    public char readChar () {
        try {
            return input.readChar();
        }
        catch (EOFException eof) {
            // Expect this possibly happen to just
            // return the null character.
        }
        catch (IOException io) {
            System.out.println(io);
        }
        return '\0';
    }

    /**
     * Writes (sends) a message to the other player. 
     * 
     * @param text Message to write
     */
    public void write (String text) {
        try {
            for (int i = 0; i < text.length(); i++)
            {
                out.writeChar (text.charAt(i));
            }
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * Checks to see if the peer has anythig to read.
     * 
     * @return True if there is waiting data
     */
    public boolean available () {
        boolean result = false;
        try {
           result = (input.available () > 0) ? true : false;
        }catch (IOException io) {}

        return result;
    }

    /**
     * Ends the peer connection.
     */
    public void end () {
        try {
            sock.shutdownInput();
            sock.shutdownOutput();
            sock.close ();
        } catch (IOException io) {}
    }
}
