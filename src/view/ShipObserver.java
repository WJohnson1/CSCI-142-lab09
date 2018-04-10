package view;

import controller.Observer;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.util.List;

/**
 *
 * The class for the observer for each ship in the battleship game
 *
 * @author William JOhnson
 */
public class ShipObserver implements Observer<Integer> {
    private GridPane gridPane;
    private String orientation;
    private int row;
    private int column;

    /**
     * Constructer for a ship observer
     * @param gridPane the gridpane of the ship
     * @param orientation the orientation of the ship
     * @param row the row of the ship
     * @param column the column of the ship
     */
    public ShipObserver(GridPane gridPane, String orientation, int row, int column){
        this.gridPane = gridPane;
        this.orientation = orientation;
        this.column = column;
        this.row = row;
    }

    /**
     * Updates the color of the cells in the ship
     * @param pushValue The value that will be pushed to the observer
     */
    @Override
    public void update(Integer pushValue) {
        List cells = gridPane.getChildren();
        if (orientation.equals("HORIZONTAL")){
            Button button = (Button) cells.get(10*row + column + pushValue);
            button.setStyle("-fx-background-color: #ff0000");
            System.out.println("HIT");
        }
        if (orientation.equals("VERTICAL")){
            Button button = (Button) cells.get(10*(row + pushValue) + column);
            button.setStyle("-fx-background-color: #ff0000");
            System.out.println("HIT");
        }
    }

}
