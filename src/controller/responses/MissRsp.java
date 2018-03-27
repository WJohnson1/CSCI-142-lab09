package controller.responses;

import controller.BattleShip;
import model.Location;

/**
 * Miss Response - used to update the system when the other player
 * misses.
 * 
 * @author Bruce Herring
 */
public class MissRsp implements Response {
    public static final String CMD = "MIS";
    private final Location loc;

    /**
     * Constructor
     * 
     * @param args String representation of row and column
     */
    public MissRsp (String[] args) {
        int row = Integer.parseInt (args[1]);
        int col = Integer.parseInt (args[2]);

        loc = new Location (row, col);
    }

    /**
     * Let the game know the attacker missed.
     * 
     * @param theGame Reference to the controller
     */
    public void execute (BattleShip theGame) {
        theGame.attackResult (false, loc);
    }

    /**
     * Returns the response
     * 
     * @return Returns the entire response as a string.
     */
    public String toString () {
        return CMD + " " + controller.CommsManager.TERMINATOR;
    }
}