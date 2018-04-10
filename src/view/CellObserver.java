package view;

import controller.BattleShip;
import controller.Observer;
import javafx.application.Platform;
import javafx.scene.control.Button;
import model.Location;

/**
 *
 * The class for the observer for each cell in the battleship game
 *
 * @author William JOhnson
 */
public class CellObserver extends Button implements Observer<Boolean>{
    private Boolean alreadyplayed;
    private Location location;
    private BattleShip battleShip;

    /**
     * Constructor for the cell observer
     * @param b the battleship of the cell
     * @param l the location of the cell
     */
    public CellObserver(BattleShip b, Location l){
        this.location = l;
        this.alreadyplayed = false;
        this.battleShip = b;
        this.battleShip.registerTarget(this.location,this);
        this.setStyle("-fx-background-color: #0000ff");
        this.setOnAction(event -> Action());
    }

    /**
     * Updates the cell after its been pushed
     * @param pushValue The value that will be pushed to the observer
     */
    @Override
    public void update(Boolean pushValue) {
        Platform.runLater(() -> {
            this.updateCell(pushValue);
        });
    }

    /**
     * Changes the color of the cell. The color will be
     * changed to red if the hit was successful and the
     * color will be changed to white if the hit wasn't
     * successful
     *
     * @param pushValue whether or not the hit was successful
     */
    private void updateCell(Boolean pushValue) {
        if (pushValue){
            this.setStyle("-fx-background-color: #ff0000");
            System.out.println("HIT");
        }
        else{
            this.setStyle("-fx-background-color: #ffffff");
            System.out.println("MISS");
        }
    }

    /**
     * Attacks the cell if it is the users turn and the
     * cell hasn't already been played
     */
    public void Action(){
        if(this.battleShip.isMyTurn() && !alreadyplayed){
            this.alreadyplayed = true;
            this.battleShip.attack(this.location);
            this.battleShip.done();
        }
    }
}
