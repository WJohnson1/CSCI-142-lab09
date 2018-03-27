package controller.responses;

import controller.BattleShip;

/**
 * Done Response - used update the system when the other player's
 * turn has ended.
 * 
 * @author Bruce Herring
 */
public class DoneRsp implements Response {
    public static final String CMD = "DNE";

    /**
     * Sets turn to active.
     * 
     * @param theGame Reference to the controller
     */
    public void execute (BattleShip theGame) {
        theGame.toggleTurn ();
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