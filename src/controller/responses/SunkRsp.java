package controller.responses;

import controller.BattleShip;

/**
 * Sunk Response - used to update the system when the other player
 * lost a ship.
 * 
 * @author Bruce Herring
 */
public class SunkRsp implements Response {
    public static final String CMD = "SNK";


    /**
     * Let the user know we sank a ship.
     * 
     * @param theGame Reference to the controller
     */
    public void execute (BattleShip theGame) {
        theGame.sendMessage ("You sank a battle ship!");
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