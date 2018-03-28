package model;

import java.util.HashMap;
import java.util.Map;

import java.util.Set;
import java.util.HashSet;

import controller.Observer;
import controller.ShipData;
import controller.ShipData.Orientation;
import model.Peg.Color;

/**
 * Top level container for all game data. Provides the
 * interface from the controller via ModelActions.
 * 
 * @author Bruce Herring
 */
public class Board implements controller.ModelActions{
   private ShipModel[] ships;
   private int shipIndex;
   private Target targetInfo;

   private Map<Location, ShipModel> shipMap = new HashMap <>(); 
   private Set<Observer<ShipData>> observers = new HashSet<> ();

   /**
    * Creates the boards data. This is a basically a deferred
    * constructor that is enforced by the interface.
    *
    * @param rows Number of rows on the game board
    * @param cols Number of columns on the game board
    * @param numShips The expected number of ships for the board
    */
   public void createModel (int rows, int cols, int numShips) {
      targetInfo = new Target (rows, cols);
      ships = new ShipModel[numShips];
   }

   /**
    * Marks a target cell as hit or miss.
    *
    * @param loc The location to be marked
    * @param wasHit True is the location was a hit, false for a miss
    */
   public void markTarget (Location loc, boolean wasHit) {
      Color color = wasHit ? Color.RED : Color.WHITE;
      targetInfo.setPeg(loc, color);
   }

   /**
    * Registers an observer for undates on a target cell.
    *
    * @param loc The location of the observed cell
    * @param ob Observer that is being registered
    */
   public void registerForTargetUpdates (Location loc, Observer<Boolean> ob) {
      targetInfo.registerPeg(loc, ob);
   }

   /**
    * Registers an observer for undates to the ships status.
    *
    * @param ob Observer that is being registered
    */
   public void registerForShipChanges (Observer<ShipData> ob) {
      observers.add (ob);
   }
   
   /**
    * Notifies observers when a ships status has changed.
    * Status is either a ship was added or sunk.
    *
    * @param ship Ship data for the ship that changed
    */
   public void notifyObservers (ShipModel ship) {
      for (Observer<ShipData> o : observers) {
         o.update (ship);
      }
   } 

   /**
    * Adds a ship to the board.
    *
    * @param name The ships name
    * @param size Number of hit locations on the ship
    * @param loc The location of the ships upper-left corner
    * @param or The ships orientation
    */
   public void addShip (String name, int size, Location loc, Orientation or) {
      if (shipIndex < ships.length) {
         ships [shipIndex] = new ShipModel (name, size, loc, or);

         for (int i = 0; i < size; i++){
            Location pegLoc;
            if (or == ShipData.Orientation.HORIZONTAL) {
               pegLoc = new Location (loc.getRow(), loc.getCol() + i);
            }
            else {
               pegLoc = new Location (loc.getRow() + i, loc.getCol());
            }

            if (shipMap.containsKey(pegLoc)){
               throw new IllegalArgumentException ("Ships cannot overlap!");
            }
            shipMap.put(pegLoc, ships [shipIndex]);
         }

         notifyObservers(ships[shipIndex]);
         shipIndex++;

         
      }
      else {
         throw new IndexOutOfBoundsException(shipIndex);
      }
   }

   /**
    * Checks to see if an attack hit. If it did, mark the hit.
    */
   public boolean checkIfAttackHit (Location atk) {
      if (shipMap.containsKey(atk)) {
         ShipModel ship = shipMap.get(atk);
         ship.hit(atk);

         // This was the last hit. Let observers know a ship's status
         // was changed to sunk.
         if (ship.sunk()) {
            notifyObservers(ship);
         }

         return true;
      }

      return false;
   }
}