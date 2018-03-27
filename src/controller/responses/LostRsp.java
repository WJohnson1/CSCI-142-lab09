package controller.responses;

import controller.BattleShip;

/**
 * Lost Response - used to update the system when the other player
 * has lost.
 * 
 * @author Bruce Herring
 */
public class LostRsp implements Response {
    public static final String CMD = "LST";

    /**
     * Let the game know we won!
     * 
     * @param theGame Reference to the controller
     */
    public void execute (BattleShip theGame) {
        theGame.won ();
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