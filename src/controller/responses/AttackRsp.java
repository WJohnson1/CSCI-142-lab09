package controller.responses;

import controller.BattleShip;


/**
 * Attack Response - used to update the system when the other player
 * attacks us.
 * 
 * @author Bruce Herring
 */
public class AttackRsp implements Response {

    public static final String CMD = "ATK";
    public final model.Location loc;

    /**
     * Constructor
     * 
     * @param args String representation of row and column
     */
    public AttackRsp (String[] args) {

        int row = Integer.parseInt (args[1]);
        int col = Integer.parseInt (args[2]);

        loc = new model.Location (row, col);
    }

    /**
     * Checks to see if the attack was a hit or miss. Returns
     * the appropriate response.
     * 
     * @param theGame Reference to the controller
     */
    public void execute (BattleShip theGame) {
        theGame.checkAttack (loc);
    }

    /**
     * Returns the response
     * 
     * @return Returns the entire response as a string.
     */
    public String toString () {
        return CMD + " " + loc.getRow() + " " + loc.getCol()  + " " + 
               controller.CommsManager.TERMINATOR;
    }
}