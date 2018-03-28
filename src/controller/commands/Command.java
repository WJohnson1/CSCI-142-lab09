package controller.commands;

/**
 * Command pattern interface. Used by all commands.
 * 
 * @author Bruce Herring
 */
public interface Command {

    /**
     * Performs the command.
     * 
     * @param peer Comm channel to the other player. Used to send the
     *             command to them.
     */
    void execute (controller.Peer peer);
}