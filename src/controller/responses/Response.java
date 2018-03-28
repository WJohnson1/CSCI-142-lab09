package controller.responses;

/**
 * Command pattern interface. Used by all responses.
 * 
 * @author Bruce Herring
 */
public interface Response {

   /**
    * Performs the response.
    * 
    * @param game Reference to the controller
    */
   void execute (controller.BattleShip game);
}