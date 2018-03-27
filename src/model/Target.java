package model; 

import controller.Observer;

/**
 * Data container for the target portion of the game board.
 * 
 * @author Bruce Herring
 */
public class Target {
   private Peg[][] pegs;

   /** 
    * Constructor
    *
    * @param rows The number of rows on the target grid
    * @param cols The number of columns on the target grid
    */
   public Target (int rows, int cols) {
      pegs = new Peg[rows][cols];
      for (int row = 0; row < rows; row++) {
         for (int col = 0; col < cols; col++) {
            pegs[row][col] = new Peg ();
         }
      }
   }

   /**
    * Sets a pegs hit status (color).
    *
    * @param loc The target call location that is being set
    * @param color The hit status
    */
   public void setPeg (Location loc, Peg.Color color) {
      pegs[loc.getRow()][loc.getCol()].setColor (color);
   }

   /**
    * Register an observer for a specific target cell.
    *
    * @param loc The cell location that is being observed
    * @param ob The observer
    */
   public void registerPeg (Location loc, Observer<Boolean> ob) {
      pegs[loc.getRow()][loc.getCol()].register (ob);
   }
}