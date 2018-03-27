package controller.commands;

/**
 * Done Command - used to signal a player has ended their turn.
 * 
 * @author Bruce Herring
 */
public class DoneCmd implements Command {
    public static final String CMD = "DNE";

    /**
     * See {@link Command} Interface
     */
    public void execute (controller.Peer peer) {
        peer.write (toString());
    }

    /**
     * Returns the command
     * 
     * @return Returns the entire command as a string.
     */
    public String toString () {
        return CMD + " " + controller.CommsManager.TERMINATOR;
    }
}