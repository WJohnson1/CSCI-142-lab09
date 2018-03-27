package model;

import java.util.Set;
import java.util.HashSet;

import controller.Observer;

/**
 * Data container for a target cell (where a peg would go
 * in the Milton-Bradly version).
 */
public class Peg {
   /**
    * The possible peg types (color).
    */
   public enum Color {
       NONE, WHITE, RED;
   }

   private Color pegColor = Color.NONE;

   // TODO: Create collection of observers

   /**
    * Set a pegs color.
    *
    * @param color Color to set the peg to
    */
   public void setColor (Color color) {
       pegColor = color;
       notifyObservers ();
   }

   /**
    * Register an observer
    * 
    * @param ob The observer to register
    */
   public void register (Observer<Boolean> ob) {
      // TODO: Add an observer to the observers collection
   }

   /**
    * Notifies observers of a color change.
    */
   public void notifyObservers () {
      // Convert the color to a hit flag to keep the observer
      // from having to know about Peg.Color.
      
      // TODO: Notify all of the observers
   }
}