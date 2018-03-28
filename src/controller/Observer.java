package controller;

/**
 * Generic Observer Pattern using a push update.
 * 
 * @author Bruce Herring
 */
public interface Observer <E> {

   /**
    * Push update
    * @param pushValue The value that will be pushed to the observer
    */
   void update (E pushValue);
}