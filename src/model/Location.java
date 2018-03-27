package model;

/**
 * Location is used to store row and column information.
 * 
 * @author Bruce Herring
 */
public class Location {
   private int row;
   private int column;

   /**
    * Constructor
    *
    * @param row Row
    * @param column Column
    */
   public Location (int row, int column) {
      this.row = row;
      this.column = column;
   }

   /**
    * Row accessor
    *
    * @return The row
    */
   public int getRow () {
      return this.row;
   }

    /**
    * Column accessor
    *
    * @return The column
    */  
   public int getCol () {
      return this.column;
   }

   /**
    * Location hash code method
    *
    * @return Hash code
    */
   @Override
   public int hashCode () {
      return (int)Math.pow (row, 13) + column;
   }

   /**
    * Location equivelency 
    *
    * @param o Object to compare it to
    * @return True if both row and column are the same
    */
   @Override
   public boolean equals (Object o) {
      if (!(o instanceof Location))
         return false;

      Location other = (Location)o;
      return row == other.row && column == other.column;
   }
}