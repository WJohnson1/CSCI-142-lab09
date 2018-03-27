package controller;

import model.Location;

/**
 * Abstract data storage class that is used for passing ship
 * data around. 
 * 
 * It may be used as the basis for a model component.
 *
 * @author Bruce Herring
 */
public abstract class ShipData {

   /**
    * Orientation enumeration
    */
   public enum Orientation {
      HORIZONTAL, VERTICAL;
   }

   protected Location bow;
   protected int size;
   protected String name;
   protected Orientation orientation;

   /**
    * Constructor
    *
    * @param name The name of the ship
    * @param loc The {@link Location} of the ships bow (upper left corner)
    * @param size Number of hit sites on the ship
    * @param or Ship's orientation
    */
   public ShipData (String name, Location loc, int size, Orientation or) {
      this.name = name;
      this.bow = loc;
      this.size = size;
      this.orientation = or;
   }

   /**
    * Name accessor
    *
    * @return Name
    */
   public String getName () {
      return name;
   }

   /**
    * Size accessor
    *
    * @return Number of hit sites
    */
   public int getSize () {
      return size;
   }

   /**
    * Bow accessor
    *
    * @return Upper left corner location
    */
   public Location getBowLocation () {
      return bow;
   }

   /**
    * Orientation accessor
    *
    * @return Orientation
    */   
   public Orientation getOrientation () {
      return orientation;
   }

   /**
    * Sunk status
    *
    * @return True if the ship is sunk, false otherwise
    */
   abstract public boolean sunk (); 
}