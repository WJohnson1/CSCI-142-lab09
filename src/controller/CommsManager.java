package controller;

import controller.commands.Command;
import controller.responses.*;

import java.io.IOException;
import java.lang.Runnable;

/**
 * The CommsManager is responsible for handling all communications
 * between the two players. It sends commands and parses responses.
 */
public class CommsManager implements Runnable {

    public static final char TERMINATOR = '!';
    private Peer peer;
    private BattleShip theGame;
    private boolean runCommsLoop = true;

    /**
     * Constructor (Client)
     * 
     * @param address The address of the server
     * @param port The server's port
     * @param game Reference to the controller
     * 
     * @throws IOException when bad things happen to the socket
     */
    public CommsManager (String address, int port, BattleShip game) throws IOException {
        peer = new Client(address, port);
        theGame = game;
    }

    /**
     * Constructor (Server)
     * 
     * @param port The server's port
     * @param game Reference to the controller
     * 
     * @throws IOException when bad things happen to the socket
     */
    public CommsManager (int port, BattleShip game) throws IOException {
        peer = new Server(port);
        theGame = game;
    }

    /**
     * Used to let the manager know it can stop listening for incomming
     * messages.
     */
    public void stopListening () {
        runCommsLoop = false;
    }

    /**
     * Ends the comms managers response thread.
     */
    public void end () {
        runCommsLoop = false;
        peer.end ();
    }

    /**
     * Sends a command message to the other player.
     * 
     * @param cmd The command message that will be sent
     */
    public void sendCommand (Command cmd) {
        cmd.execute (peer);
    }

    /**
     * Retreives a response message from the other player
     * 
     * @return The message from the other player
     */
    public String readResponse () {
        java.lang.StringBuilder command = new java.lang.StringBuilder();
        char ch;

        do {
            ch = peer.readChar();
            command.append (ch);
        }while (ch != TERMINATOR && ch != '\0');

        return command.toString();
    }

    /**
     * The CommsManager runs a seperate thread that listens
     * for asyncronous messages from the other player.
     */
    @Override
    public void run() {

        // Factory method
        ResponseFactory factory = new ResponseFactory();

        while (runCommsLoop) {
            String rspString = readResponse();
            Response rsp = factory.create (rspString);

            if (rsp != null)
                rsp.execute (theGame);
        }

        // There is the possibility of one remaining message.
        // If it exists handle it.
        if (peer.available()) {
            String rspString = readResponse();
        
            if (!rspString.equals ("\0")) {
                Response rsp = factory.create (rspString);
                rsp.execute (theGame);
            }
        }
        
    }

}
