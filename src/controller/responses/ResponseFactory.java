package controller.responses;

/**
 * Factory Method pattern implementation.
 * 
 * @author Bruce Herring
 */
public class ResponseFactory {

   /**
    * Creates the response based on the incoming response
    * message.
    * 
    * @param response The response message
    * @return The {@link Response} that was created
    */
   public Response create (String response) {
      String [] args = response.split (" ");

      switch (args[0]) {
         case AttackRsp.CMD:
             return new AttackRsp (args);
         case MissRsp.CMD:
             return new MissRsp (args);
         case HitRsp.CMD:
             return new HitRsp (args);
         case DoneRsp.CMD:
             return new DoneRsp ();
         case SunkRsp.CMD:
             return new SunkRsp ();
         case LostRsp.CMD:
             return new LostRsp ();
         default:
             return null;
     }
   }

}