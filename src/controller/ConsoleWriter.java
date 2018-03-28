package controller;

/**
 * Interface that allows access to the view's console.
 * 
 * @author Bruce Herring
 */
public interface ConsoleWriter {

   /**
    * Write a message to the console.
    *
    * @param text The message to write
    */
   void write (String text);
}