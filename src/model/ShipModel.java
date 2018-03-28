package model;

import model.Peg.Color;
import controller.Observer;
import java.util.Set;
import java.util.HashSet;;

/**
 * Data storage for the ship.
 * 
 * @author Bruce Herring
 */
public class ShipModel extends controller.ShipData {

   private Peg[] pegs;
   private int hitCount;

   private Set<Observer<Integer>> observers = new HashSet<> ();

   /**
    * Constructor
    *
    * @param name The ship's name
    * @param shipSize The number of hit sites on the ship
    * @param bow The upper-left corner of the ship
    * @param orient The ship's orientation
    */
   public ShipModel (String name, int shipSize, Location bow, Orientation orient) {
      super (name, bow, shipSize, orient);
      pegs = new Peg [shipSize];
      for (int i = 0; i < shipSize; i++) {
         pegs[i] = new Peg ();
      }
   }  

   /**
    * Registers an observer for updates.
    * 
    * @param ob The observer to be registered
    */
   public void register (Observer<Integer> ob) {
      observers.add (ob);
   }

   /**
    * Notify all observers that a ship was hit.
    * 
    * @param loc The location on the player board that was hit
    */
   public void notifyObservers (int loc) {
      for (Observer<Integer> o : observers) {
         o.update (loc);
      }
   }

   /**
    * Accessor for number of hit locations on a ship.
    *
    * @return The ship's size
    */
   @Override
   public int getSize () {
      return pegs.length;
   }

   /**
    * The ship's sunk status.
    *
    * @return True if the ship is sunk
    */
   @Override
   public boolean sunk () {
      return hitCount == pegs.length;
   }

   /**
    * Method used to record a hit on the ship.
    *
    * @param hitLoc The location on the ship that was hit (0 is bow-most slot)
    */
   public void hit (Location hitLoc) {
      int hitIndex = hitLoc.getRow() - bow.getRow() + hitLoc.getCol() - bow.getCol();
      pegs [hitIndex].setColor (Color.RED);
      hitCount++;

      notifyObservers (hitIndex);
   }
}