package controller.commands;

import controller.Peer;
import model.Location;

/**
 * Hit Command - used to signal a player ship has been hit.
 * 
 * @author Bruce Herring
 */
public class HitCmd implements Command {
    public static final String CMD = "HIT";
    private final Location loc;

    /**
     * Constructor
     * 
     * @param loc The location that was hit.
     */
    public HitCmd (model.Location loc) {
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
        return CMD + " " + loc.getRow() + " " + loc.getCol() + " " + 
               controller.CommsManager.TERMINATOR;
    }
}