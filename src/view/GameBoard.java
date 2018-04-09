package view;

import controller.*;
import controller.commands.AttackCmd;
import controller.responses.AttackRsp;
import controller.responses.HitRsp;
import controller.responses.MissRsp;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.Board;
import model.Location;
import model.ShipModel;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The provided javadocs only includes items that are
 * inherited from Application. All of which you will
 * not need to implement except for start which is
 * already provided below. Please replace this comment
 * block with something useful when you create your
 * implementation.
 */
public class GameBoard extends Application implements Observer{
    private Node console;
    private BattleShip battleShip;
    private Observer<ShipData> player;
    private Boolean serveravailibilty = false;
    private Boolean clientavailibilty = false;
    private Boolean loopbackavailibility = false;
    private BorderPane borderPane;
    private Server server;
    private Client client;
    private CommsManager comms;
    private boolean turn = false;
    @Override
    public void update(Object pushValue) {
        System.out.println("Hello");
    }

    private class LoopBackEventHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            battleShip.getTheBoard().registerForShipChanges(player);
            int xLoc = (int) (((Button) event.getTarget()).getLayoutX() - 25) / 18;
            int yLoc = (int) (((Button) event.getTarget()).getLayoutY() - 25) / 27;
            String[] p = new String[3];
            p[1] = String.valueOf(xLoc);
            p[2] = String.valueOf(yLoc);
            Location location = new Location(xLoc, yLoc);
            if (battleShip.attack(location)) {
                ((Button) event.getTarget()).setStyle("-fx-background-color: #ff0000");
            }
            else {
                ((Button) event.getTarget()).setStyle("-fx-background-color: #ffffff");
            }
        }
    }

    private class PeerEventHandler implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent event) {
            int xLoc = (int) (((Button) event.getTarget()).getLayoutX() - 25) / 18;
            int yLoc = (int) (((Button) event.getTarget()).getLayoutY() - 25) / 27;
            String[] p = new String[3];
            if (clientavailibilty) {
                System.out.println(battleShip.isMyTurn());
                if (!battleShip.isMyTurn()) {

                    p[1] = String.valueOf(xLoc);
                    p[2] = String.valueOf(yLoc);
                    Location location = new Location(xLoc, yLoc);
                    battleShip.attack(location);
                    AttackCmd a = new AttackCmd(location);
                    System.out.println(a.toString());
                    comms.sendCommand(a);
                    System.out.println("My turn");
                    turn= !turn;
                    battleShip.toggleTurn();
                }
                else{
                    if(comms.readResponse()!=null){
                        battleShip.toggleTurn();
                    }
                }
            }
            else {
                if (battleShip.isMyTurn()) {
                    p[1] = String.valueOf(xLoc);
                    p[2] = String.valueOf(yLoc);
                    Location location = new Location(xLoc, yLoc);
                    AttackCmd a = new AttackCmd(location);
                    System.out.println(a.toString());
                    comms.sendCommand(a);
                    battleShip.toggleTurn();
                    System.out.println("My turn");
                }
                else{
                    String response = comms.readResponse();
                    if(response!=null){

                        battleShip.toggleTurn();
                    }
                }
            }
//            battleShip.toggleTurn();

        }
    }

    @Override
    public void init(){
        try {
            borderPane = new BorderPane();
            Text t = new Text("Game Console");
            borderPane.setCenter(t);
            this.console = borderPane.getCenter();
            MyConsoleWriter myConsoleWriter = new MyConsoleWriter(this.console);
            Application.Parameters params = getParameters();
            List<String> param = new ArrayList<>();
            if (params != null) {
                System.out.print("program arguments: ");
                for (String parm : params.getRaw()) {
                    System.out.print(parm + " ");
                    param.add(parm);
                }
                System.out.println();
            }
            if (param.size() == 0) {
                battleShip = new BattleShip(myConsoleWriter);
                player = new Observer<ShipData>() {
                    public void update(ShipData pushValue) {
                        if (pushValue.sunk()){
                            battleShip.update(pushValue);
                        }
                    }
                };
                this.loopbackavailibility = true;
                battleShip.addShips(player);
            } else if (param.size() == 1) {
                battleShip = new BattleShip(myConsoleWriter, Integer.parseInt(param.get(0)));
                comms = new CommsManager(Integer.parseInt(param.get(0)),battleShip);
                this.serveravailibilty = true;
                player = new Observer<ShipData>() {
                    @Override
                    public void update(ShipData pushValue) {
                        battleShip.update(pushValue);
                    }
                };
                battleShip.addShips(player);
            } else {
                battleShip = new BattleShip(myConsoleWriter, param.get(0), Integer.parseInt(param.get(1)));
                comms = new CommsManager(param.get(0),Integer.parseInt(param.get(1)),battleShip);
                this.clientavailibilty = true;
                player = new Observer<ShipData>() {
                    @Override
                    public void update(ShipData pushValue) {
                        battleShip.update(pushValue);

                    }
                };
                battleShip.addShips(player);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void start(Stage stage) {

        //Pane screen = new Pane();
        GridPane gridPane = new GridPane();
        for (int i = 0; i < 10;i++){
            for (int j = 0; j<10;j++){
                Button b = new Button();
                b.setStyle("-fx-background-color: #0000ff");
                if (loopbackavailibility){
                    b.setOnAction(new LoopBackEventHandler());
                }
                else{
                    b.setOnAction(new PeerEventHandler());
                    //comms.run();
                }
                gridPane.add(b,j,i);
            }
        }
        gridPane.setHgap(1.5);
        gridPane.setVgap(1.5);
        gridPane.setPadding(new Insets(25,25,25,25));

        Label label;
        if (loopbackavailibility) {
            label = new Label("Battleship Game");
        }
        else if (clientavailibilty){
            label = new Label("Client's Battleship Game");
        }
        else{
            label = new Label("Server's Battleship Game");
        }
        label.setAlignment(Pos.TOP_CENTER);
        GridPane gridPane1 = new GridPane();
        for (int i = 0; i < 10;i++){
            for (int j = 0; j<10;j++){
                Button b = new Button();
                b.setStyle("-fx-background-color: #0000ff");
                if (loopbackavailibility){
                    b.setOnAction(new LoopBackEventHandler());
                }
                else{
                    b.setOnAction(new PeerEventHandler());
                    //comms.run();
                }
                gridPane1.add(b,j,i);
            }
        }
        gridPane1.setHgap(1.5);
        gridPane1.setVgap(1.5);
        gridPane1.setPadding(new Insets(25,25,25,25));
        Scene scene = new Scene( borderPane );
        //borderPane.getChildren().add(console);
        borderPane.setLeft(label);
        borderPane.setTop(gridPane);
        borderPane.setBottom(gridPane1);

        stage.setMaxWidth( 6000 );

        stage.setTitle( "GameBoard" );
        stage.setScene( scene );
        stage.show();
    }
    @Override
    public void stop() {
        System.out.println( "stopping " + this.getClass().getSimpleName() );
        battleShip.endEarly();
        if (serveravailibilty || clientavailibilty) {
            comms.end();
        }
    }
    public static void main(String[] args) {
        Application.launch(args);
    }
}
