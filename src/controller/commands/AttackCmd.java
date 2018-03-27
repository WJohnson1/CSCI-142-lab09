package controller.commands;

import controller.Peer;
import model.Location;

/**
 * Attack Command - used to signal an attack has been made.
 * 
 * @author Bruce Herring
 */
public class AttackCmd implements Command {

    public static final String CMD = "ATK";
    public final model.Location loc;

    /**
     * Constructor
     * 
     * @param loc The location that is being attacked
     */
    public AttackCmd (Location loc) {
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