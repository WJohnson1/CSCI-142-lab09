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

   private Set<Observer<Boolean>> observers = new HashSet<> ();

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
      observers.add (ob);
   }

   /**
    * Notifies observers of a color change.
    */
   public void notifyObservers () {
      for (Observer<Boolean> ob : observers) {
        // Convert the color to a hit flag to keep the observer
        // from having to know about Peg.Color.
         ob.update ((pegColor == Color.RED) ? true : false);
      }
   }
}