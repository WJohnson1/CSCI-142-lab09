package view;

import javafx.scene.Node;
import javafx.scene.text.Text;

/**
 *
 * The class for the console writer for the battleship game
 *
 * @author William JOhnson
 */
public class MyConsoleWriter implements controller.ConsoleWriter {
    private Node console;

    /**
     * Constructor for the console writer
     * @param console the node that the gui will display messages
     */
    public MyConsoleWriter(Node console){

        this.console = console;
    }

    /**
     * Write a message to the console
     * @param text The message to write
     */
    @Override
    public void write(String text) {
        javafx.application.Platform.runLater(() -> ((Text) console).setText(text));
    }

}

