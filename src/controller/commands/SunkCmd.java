package controller.commands;

public class SunkCmd implements Command {
    public static final String CMD = "SNK";

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