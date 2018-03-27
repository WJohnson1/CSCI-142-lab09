package controller.commands;

import controller.Peer;
import model.Location;

/**
 * Miss Command - used to signal an attack missed.
 * 
 * @author Bruce Herring
 */
public class MissCmd implements Command {
    public static final String CMD = "MIS";
    private final Location loc;

    /**
     * Constructor
     * 
     * @param loc The location where the miss occured.
     */
    public MissCmd (Location loc) {
        this.loc = loc;
    }

    /**
     * See {@link Command} Interface
     */
    public void execute (Peer peer) {
        peer.write (toString ());
    }

     /**
     * Returns the command
     * 
     * @return Returns the entire command as a string.
     */
    public String toString () {
        return CMD + " " + loc.getRow() + " " + loc.getCol() + " " + controller.CommsManager.TERMINATOR;
    }
}