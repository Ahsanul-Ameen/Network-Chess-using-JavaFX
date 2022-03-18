package sample.GameChess.server;

/**
 *This is the Server part showing GUI
 * @author  Ahsanul Ameen Sabit
 * @author Mizanur Rahman Shuvo
 */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sample.GameChess.server.modification.StartServer;


public class ServerMain extends Application {
    private boolean flag = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Hello I'm SERVER!");
        Button button  = new Button("Start SerVer");
        button.setOnAction(e->{
            if(!flag){
                button.setText("SERVER STARTED..127.0.0.1");
                new StartServer();///////////////////////////////
                button.setTextFill(Color.BLUEVIOLET);
                flag = true;
            }
            else{
                button.setText("Already Running...!");
            }
        });

        StackPane root = new StackPane();
        root.getChildren().add(button);
        root.setStyle("-fx-background-image: url(sample/GameChess/server/modification/empty.png)");/////////////////////////
        primaryStage.setScene(new Scene(root, 200, 100));
        primaryStage.setResizable(true);
        primaryStage.show();
        primaryStage.setOnCloseRequest(e->System.exit(1));
        //new Server();
    }
}





















//_________________________________________________________________________________________________________________
/*
package sample.GameChess.server;


import sample.GameChess.server.modification.Server;

public class ServerMain {
    public static void main(String[] args) {
        new Server();
    }
}
*/
