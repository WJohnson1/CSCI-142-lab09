package controller;

/**
 * Interface that provides access to the model components.
 * 
 * @author Bruce Herring
 */
public interface ModelActions {

      /**
    * Create the model. Since a construtor cannot be specified in
    * an interface this will have to suffice. This method should
    * be called after the ModelActions implementation has been
    * created but before any other methods are called. It should
    * provides the information necessary to create the board 
    * components.
    *
    * @param rows The number of rows on the target/player pane
    * @param columns The number of columns on the target/player pane
    * @param numShips The number of ships the player has
    */
    void createModel (int rows, int columns, int numShips);

   /**
    * Register for updates from a single target model site
    *
    * @param loc The location on the target model that is to
    *            be observed.
    * @param ob The observer that is registering
    */
   void registerForTargetUpdates (model.Location loc, Observer<Boolean> ob);

   /**
    * Register for chagnes to the ship's status. Ship status includes when
    * a ship was added or removed (sunk).
    *
    * @param ob The observer that is registering
    */
   void registerForShipChanges (Observer <ShipData> ob);

   /**
    * Adds a ship to the model. Notifies any ship observers that a ship
    * was created.
    * 
    * @param name The ship's name
    * @param size The number of hit sites on the ship
    * @param loc The location of the ship's upper leftmost point
    * @param or The ships orientation (HORIZONTAL/VERTICAL)
    */
   void addShip (String name, int size, model.Location loc, ShipData.Orientation or);

   /**
    * Marks a target site
    * 
    * @param loc The location to mark
    * @param wasHit True if the site was a ship hit, false if it was a miss
    */
   void markTarget (model.Location loc, boolean wasHit);

   /**
    * Checks to see if an attack was a hit. If it was, notify status observers
    * if the ship was sunk.
    *
    * @param loc Location of the attack
    * @return True is attack was a hit, false if it was a miss
    */
   boolean checkIfAttackHit (model.Location loc);

}