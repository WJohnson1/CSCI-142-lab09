package controller.responses;

import controller.BattleShip;
import model.Location;

/**
 * Hit Response - used to update the system when the other player
 * hits one of our ships.
 * 
 * @author Bruce Herring
 */
public class HitRsp implements Response {
    public static final String CMD = "HIT";
    private final Location loc;

    /**
     * Constructor
     * 
     * @param args String representation of row and column
     */
    public HitRsp (String[] args) {
        int row = Integer.parseInt (args[1]);
        int col = Integer.parseInt (args[2]);

        loc = new Location (row, col);
    }

    /**
     * Let the game know we were hit, and where.
     * 
     * @param theGame Reference to the controller
     */
    public void execute (BattleShip theGame) {
        theGame.attackResult (true, loc);
    }

    /**
     * Returns the response
     * 
     * @return Returns the entire response as a string.
     */    
    public String toString () {
        return CMD + " " + loc.getRow() + " " + loc.getCol() + " " + 
        controller.CommsManager.TERMINATOR;
    }
}