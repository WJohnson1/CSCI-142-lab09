package view;

import controller.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Location;
import model.ShipModel;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Creates a GUI for a 2-person battleship game based on the model
 * for the battleship
 *
 * @author William JOhnson
 */
public class GameBoard extends Application implements Observer<ShipData>{
    private Node console;
    private BattleShip battleShip;
    private Observer<ShipData> player;
    private Boolean serveravailibilty = false;
    private Boolean clientavailibilty = false;
    private Boolean loopbackavailibility = false;
    private GridPane playergrid;
    private GridPane opponentgrid;
    private BorderPane borderPane;

    /**
     * Changes the observer based on the user's input
     * @param pushValue The value that will be pushed to the observer
     */
    @Override
    public void update(ShipData pushValue) {
        ShipModel shipModel = (ShipModel)pushValue;
        int row = shipModel.getBowLocation().getRow();
        int column = shipModel.getBowLocation().getCol();
        int size = shipModel.getSize();
        if (this.battleShip.isMyTurn()){
            ((Text) this.console).setText("Your Turn");

            this.battleShip.sendMessage("Your Turn");
        }
        String orientation = shipModel.getOrientation().toString();
        shipModel.register(new ShipObserver(playergrid,orientation,row,column));
        displayShips(playergrid,orientation,size,row,column,shipModel.sunk());
    }

    /**
     * Setups the GUI for the GameBoard
     */
    @Override
    public void init(){
        borderPane = new BorderPane();
        Text t = new Text("Game Console");
        borderPane.setCenter(t);
        this.console = borderPane.getCenter();
        MyConsoleWriter myConsoleWriter = new MyConsoleWriter(this.console);
        Application.Parameters params = getParameters();
        List<String> param = new ArrayList<>();
        for (String parm : params.getRaw()) {
            param.add(parm);
        }
        if (param.size() == 0) {
            battleShip = new BattleShip(myConsoleWriter);
            this.loopbackavailibility = true;
        } else if (param.size() == 1) {
            battleShip = new BattleShip(myConsoleWriter, Integer.parseInt(param.get(0)));
            this.serveravailibilty = true;
        } else {
            battleShip = new BattleShip(myConsoleWriter, param.get(0), Integer.parseInt(param.get(1)));
            this.clientavailibilty = true;
        }
        GridPane gridPane1 = new GridPane();
        for (int i = 0; i < 10;i++){
            for (int j = 0; j<10;j++){
                Button b = new Button();
                b.setStyle("-fx-background-color: #0000ff");
                gridPane1.add(b,j,i);
            }
        }
        gridPane1.setHgap(1.5);
        gridPane1.setVgap(1.5);
        gridPane1.setPadding(new Insets(25,25,25,25));

        playergrid = gridPane1;
        battleShip.addShips(this);
    }

    /**
     * Displays the GUI of the gameboard
     * @param stage the GUI that the game will display
     */
    public void start(Stage stage) {

         GridPane gridPane = new GridPane();
         for (int i = 0; i < 10;i++){
             for (int j = 0; j<10;j++){
                 Location location = new Location(i,j);
                 CellObserver p = new CellObserver(battleShip,location);
                 battleShip.registerTarget(location, p);
                 gridPane.add(p,j,i);
             }
         }
         gridPane.setHgap(1.5);
         gridPane.setVgap(1.5);
         gridPane.setPadding(new Insets(25,25,25,25));

         if (this.battleShip.isMyTurn()){
             ((Text) this.console).setText("Your Turn");
             this.battleShip.sendMessage("Your Turn");
        }

        opponentgrid = gridPane;
        borderPane.setTop(opponentgrid);
        borderPane.setBottom(playergrid);
        Scene scene = new Scene( borderPane );

        stage.setMaxWidth( 6000 );
        String title;
        if (loopbackavailibility) {
            title = ("Battleship Game");
        }
        else if (clientavailibilty){
            title = ("Client's Battleship Game");
        }
        else{
            title = ("Server's Battleship Game");
        }
        stage.setTitle( title );
        stage.setScene( scene );
        stage.show();
    }

    /**
     * Displays the board for the game board
     * @param gridPane the gridpane of the player
     * @param orientation the orientation of the ship
     * @param size the size of the ship
     * @param row the row of the ship
     * @param column the column of the ship
     * @param sunk whether or not the cell has been sunk
     */
    public void displayShips(GridPane gridPane, String orientation, int size, int row, int column, boolean sunk){
        List cells = gridPane.getChildren();
        if (orientation.equals("HORIZONTAL")){
            for (int length = 0; length < size; length++){
                if (sunk){
                    Button cell = (Button) cells.get(10*row + column + length);
                    cell.setStyle("-fx-background-color: #ff0000");
                }
                else{
                    Button cell = (Button) cells.get(10*row + column + length);
                    cell.setStyle("-fx-background-color: #008000");
                }
            }
        }
        else if(orientation.equals("VERTICAL")){
            for (int length = 0; length < size; length++) {
                // 10*(pushValue + row) + column +
                if (sunk) {
                    // If the ship has been sunk
                    Button cell = (Button) cells.get(10*(row + length) + column);
                    cell.setStyle("-fx-background-color: #ff0000");
                }
                else {
                    // If the ship has not yet sunk
                    Button cell = (Button) cells.get(10 * (row + length) + column);
                    cell.setStyle("-fx-background-color: #008000");
                }
            }
        }
    }

    /**
     * Ends the battleship game early
     */
    @Override
    public void stop() {
        System.out.println( "stopping " + this.getClass().getSimpleName() );
        battleShip.endEarly();
    }

    /**
     * Runs the game board
     * @param args the arguements for the game board
     */
    public static void main(String[] args) {
        Application.launch(args);
    }
}
