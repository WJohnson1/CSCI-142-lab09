package controller.commands;

/**
 * Lost Command - used to signal a player has lost the game.
 * 
 * @author Bruce Herring
 */
public class LostCmd implements Command {
    public static final String CMD = "LST";

    /**
     * See {@link Command} Interface
     */
    public void execute (controller.Peer peer) {
        peer.write (toString ());
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