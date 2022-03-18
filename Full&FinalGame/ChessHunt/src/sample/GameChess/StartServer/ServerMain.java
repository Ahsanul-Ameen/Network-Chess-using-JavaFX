package sample.GameChess.StartServer;

/**
 *This is the Server part showing GUI
 * @author  Ahsanul Ameen Sabit
 * @author Mizanur Rahman Shuvo
 */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sample.GameChess.StartServer.modification.StartServer;

import java.net.InetAddress;
import java.net.UnknownHostException;


public class ServerMain extends Application {
    private boolean flag = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        InetAddress inetAddress = null;

        try {
            inetAddress = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            //e.printStackTrace();
            System.out.println("Problem in printing IP address in server GUI");
        }

        primaryStage.setTitle("Hello I'm SERVER!");
        Label label = new Label("IP: "+inetAddress+" Port: 56789");
        new StartServer();///////////////////////////////
        InetAddress finalInetAddress = inetAddress;
        label.setOnMouseClicked(e->{
            if(!flag){
                label.setText("SERVER STARTED..at "+ finalInetAddress);
                //new StartServer();///////////////////////////////
                label.setTextFill(Color.BLUEVIOLET);
                flag = true;
            }
            else{
                label.setText("Server is Running...!");
            }
        });

        StackPane root = new StackPane();
        root.getChildren().add(label);
        root.setStyle("-fx-background-image: url(sample/GameChess/StartServer/modification/empty.png)");/////////////////////////
        primaryStage.setScene(new Scene(root, 200, 100));
        primaryStage.setResizable(true);
        primaryStage.setMaxHeight(400);
        primaryStage.setMaxWidth(500);
        primaryStage.show();
        primaryStage.setOnCloseRequest(e->System.exit(1));
        //new Server();
    }
}





















//_________________________________________________________________________________________________________________
/*
package sample.GameChess.StartServer;


import sample.GameChess.StartServer.modification.Server;

public class ServerMain {
    public static void main(String[] args) {
        new Server();
    }
}
*/
