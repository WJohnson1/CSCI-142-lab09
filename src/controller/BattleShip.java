package controller;

import controller.ShipData.Orientation;
import controller.commands.*;

import javafx.application.Application;
import model.Board;
import model.Location;

import java.io.IOException;
import java.util.Scanner;
import java.io.File;

/**
 *  BattleShip is both the entry point to the game and the
 *  primary controller class with the MVC pattern. It controls
 *  access between the View and the Model as well as managing
 *  all network communications.
 *
 *  @author Bruce Herring
 */
public class BattleShip implements Observer<ShipData> {

    public static final int NUM_ROWS = 10;
    public static final int NUM_COLS = 10;
    public static final int NUM_SHIPS = 5;

    private int shipCount = 0;
    private int sunkShips = 0;
    private ModelActions theBoard = new Board();
    private ConsoleWriter console;
    private boolean myTurn = true;
    private Thread commsThread = null;

    private CommsManager comms;
    private boolean loopback = false;

    private void constructorHelper (ConsoleWriter console) {
        theBoard.createModel (NUM_ROWS, NUM_COLS, NUM_SHIPS);
        this.console = console;
    }

    /**
     * Constructor for a Client BattleShip. This version expects
     * a server to be running at the time of creation.
     *
     * @param console The view console that is used to display status
     *                to the user.
     * @param address The address for the server. Wse "localhost" to
     *                connect to a server running on the same machine
     * @param port The server's port.
     */
    public BattleShip(ConsoleWriter console, String address, int port) {
        constructorHelper (console);

        // Try to set up the communication system.
        try {
            comms = new CommsManager (address, port, this);
        }
        catch (IOException e) {
            console.write ("Cannot create a connection to the peer!\n\t" +
                           "Error: " + e.getMessage());
        }

        // The server get's to go first.
        myTurn = false;
    }

    /**
     * Constructor for a Server Battleship. This version will accept
     * connections from a single Client Battleship.
     *
     * @param console The view console that is used to display status
     *                to the user.
     * @param port The port to listen on. Recommended to use a value
     *             between 10000 and 100000.
     */
    public BattleShip(ConsoleWriter console, int port) {
        constructorHelper (console);

        // Try to set up the communications system.
        try {
            comms = new CommsManager (port, this);
        }
        catch (IOException e) {
            console.write ("Cannot create a connection to the peer!\n\t" +
                    "Error: " + e.getMessage());
        }
    }

    /**
     * Constructor for a Loopback BattleShip. This version does not
     * communicate with another BattleShip game. It plays the game
     * with only one board and will always be the winner. It should
     * be used for development/debugging.
     *
     * @param console The view console that is used to display status
     *                to the user.
     */
    public BattleShip(ConsoleWriter console) {
        constructorHelper (console);
        loopback = true;
    }


    /**
     * Toggle the turn. When not it's turn the BattleShip will
     * need to ignore user input.
     */
    public void toggleTurn () {
        myTurn = !myTurn;
    }

    /**
     * Check to see if it's this BattleShips turn.
     *
     * @return True if it's our turn, false otherwise
     */
    public boolean isMyTurn () {
        return myTurn;
    }

    /**
     * Send a message to the console.
     *
     * @param message String to send to the console.
     */
    public void sendMessage (String message) {
        console.write (message);
    }

    /**
     * Sends a command to the other player.
     * 
     * @param cmd Command to send
     */
    public void sendCommand (Command cmd) {
        comms.sendCommand (cmd);
    }

    /**
     * Used to notify the BattleShip that it's won the game.
     */
    public void won () {
        // Let the user know they won.
        sendMessage ("You Won!");

        // Disable further input.
        myTurn = false;
        if (!loopback) {
            comms.stopListening();
        }
    }

    /**
     * Use when a player quites before the game has finished.
     */
    public void endEarly () {
        // Disable further input.
        myTurn = false;
        if (!loopback) {
            comms.stopListening ();
            comms.end ();
        }
    }

    /**
     * Observer pattern update method. Used to let the BattleShip
     * know something happened to one of the ship models.
     * @param ship The ship that something happened to.
     */
    public void update(ShipData ship) {
        // Currently the only action that we care about in the
        // BattleShip is when one of our ships was sunk.
        if (ship.sunk()) {
            sunkShips++;

            // In loopback we're sinking our own ships so make
            // it a positive experience.
            if (loopback) {
                console.write ("You sank a battle ship!");

                if (sunkShips == shipCount) {
                    won ();
                }
            }
            // Otherwise another BattleShip game sank one of our ships.
            else {
                sendMessage ("Your " + ship.getName() + " was sunk!");

                if (sunkShips == shipCount) {
                    sendMessage ("You Lost!");

                    // Inform the other player we lost. No need to listen
                    // for message after this so shut down the comms thread.
                    comms.sendCommand (new LostCmd ());
                    comms.stopListening();
                }
                else {
                    // Let the other player know they
                    // sunk one of our ships.
                    comms.sendCommand (new SunkCmd ());
                }
            }
        }
    }

    /**
     * Attempt an attack on the other player.
     *
     * @param loc Location that is being attacked.
     */
    public void attack(Location loc) {
        // Don't allow attacks if the game has
        // already been won.
        if (sunkShips == shipCount) {
            return;
        }

        // We pressed something so clear the console of the last status.
        console.write ("");

        boolean isHit;
        if (loopback) {
            // In loopback we can just check with the model if a hit was
            // successful and then update the target pane.
            isHit = theBoard.checkIfAttackHit(loc);
            attackResult (isHit, loc);
        } else {
            // Need to ask the other player if we hit one of their ships.
            comms.sendCommand(new AttackCmd (loc));

        }
    }

    /**
     * Check to see if an attack against use succeeded and inform
     * the other player of the result.
     *
     * @param loc The location that was attacked.
     */
    public void checkAttack (Location loc) {
        boolean isHit = theBoard.checkIfAttackHit (loc);

        Command cmd;
        
        if (isHit) {
            cmd = new HitCmd (loc);
        }
        else {
            cmd = new MissCmd (loc);
        }

        sendCommand (cmd);
    }

    /**
     * Update the target model with the result of an attack.
     *
     * @param wasAHit True if the a ship was hit, false for a miss.
     * @param loc The location that was attacked.
     */
    public void attackResult (boolean wasAHit, Location loc) {

        if (wasAHit) {
            theBoard.markTarget(loc, true);
        } else {
            theBoard.markTarget(loc, false);
        }

    }

    /**
     * Method used to add the ships to the model.
     *
     * @param ob An observer that want's to know when something major
     *           happens to the ship that is being added.
     */
    public void addShips(Observer<ShipData> ob) {
        // The view wants to know when a ship was added.
        theBoard.registerForShipChanges (ob);

        // The controller wants to know when a ship was sunk.
        theBoard.registerForShipChanges (this);

        // Get the ship info from the data file.
        try (Scanner shipFile = new Scanner (new File ("shipData.txt"))) {

            while (shipFile.hasNext()) {

                String[] shipComponents = shipFile.nextLine ().split (" ");
                String name = shipComponents[0];
                int size = Integer.parseInt (shipComponents[1]);
                Location loc = new Location (Integer.parseInt (shipComponents[2]),
                                            Integer.parseInt (shipComponents[3]));
                Orientation or = 
                    shipComponents [4].equals ("VERTICAL") ? Orientation.VERTICAL : Orientation.HORIZONTAL;


                // Add a ship.
                try {
                    theBoard.addShip (name, size, loc, or);
                    shipCount++;
                } catch (Exception e) {
                    console.write("Connot configure the board due to an error:\n\t" +
                            e.getMessage());
                }
            }

        } catch (IOException io) {
            console.write ("Could not open ship data file.");
        }
        // Once we have our ships in place we can start communicating with
        // the other player.
        commsThread = new Thread (comms, "Comms Thread");
        commsThread.start ();

    }

    /**
     * Called to let the controller know the players turn is over.
     */
    public void done () {
        if (!loopback) {
            toggleTurn();

            // Let the other player know we are done.
            comms.sendCommand(new DoneCmd ());
        }
    }

    /**
     * Register a peg observer. This is used by the target
     * view to register for changes to the pegs in the model.
     *
     * @param loc The peg to be observed.
     * @param ob The peg observer.
     */
    public void registerTarget(Location loc, Observer<Boolean> ob) {
        theBoard.registerForTargetUpdates (loc, ob);
    }

    /**
     * Application entry point. It starts the JavaFX application.
     * The application will create everything.
     *
     * @param args Command line arguments.
     */
    public static void main (String[] args) {
        Application.launch (view.GameBoard.class, args);
    }
}