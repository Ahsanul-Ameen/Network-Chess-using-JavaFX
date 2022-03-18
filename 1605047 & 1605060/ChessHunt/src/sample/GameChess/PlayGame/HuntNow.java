package sample.GameChess.PlayGame;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.PrintStream;

import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import sample.GameChess.PlayGame.Controllers.GameControl;
//import sample.GameChess.PlayGame.Controllers.GameControlOff;
import sample.GameChess.PlayGame.Controllers.PriningInTextAera;

/**
 *This is the launcher of client side game part, show GUI & calls Controller class
 * @author  Ahsanul Ameen Sabit
 */


public class HuntNow extends Application {

	private static Stage huntStage;
	//private Stage stage;

    public boolean online = false;
    public boolean offline = false;

	public static final int WHITE_PLAYER = 1,BLACK_PLAYER = 2,EMPTY_PLAYER = 0;
	public static String MyName = null;
	public static String OpponentsName = "I don't know";
	public static String MyPassWord = "1234";

	
	// private fields for this class
    private int type;
    public static TextArea status;
	private StackPane stackLayout;	
	private GameControl gameController;
	//public GameControlOff gameController1;
	
	public static Label typeLabel = new Label("Want to know your TYPE????");
	
	private static Label bLabel = new Label();
	private static Label wLabel = new Label();
	private static Label myName = new Label();
	private static Label opponensName = new Label();



	private static Stage stage = null;
	private boolean hasLogin = false;

	
	@Override//no need to override.no follow the sequence of javaFx
	public void init() {
		
		/*// initialize the layout, create a CustomControl and add it to the layout
 		stackLayout = new StackPane();
 		gameController = new GameControl();
 		stackLayout.getChildren().add(gameController);

 		typeLabel.setEffect(new InnerShadow());
 		
        bLabel.setTextFill(Color.BLACK);
        wLabel.setTextFill(Color.WHITE);
        bLabel.setText("Black's Part");
        wLabel.setText("White's Part");
        bLabel.setStyle("-fx-font-size: 15;");
        wLabel.setStyle("-fx-font-size: 15;");
        myName.setStyle("-fx-font-size: 15;");
        opponensName.setStyle("-fx-font-size: 12;");
        typeLabel.setTextFill(Color.BLACK);
        typeLabel.setStyle("-fx-font-size: 14;");*/
        
	}

	// overridden start method
	@Override
	public void start(Stage primaryStage) {
		 /*stackLayout = new StackPane();
            gameController = new GameControl();
            stackLayout.getChildren().add(gameController);*/
		// set the title and scene, and show the stage
		huntStage = primaryStage;
		showLogin();
	}


    //show login window
    public void showLogin(){
        stage = huntStage;
        stage.setTitle("Login NOW");
        GridPane grid = new GridPane();

        grid.setAlignment(Pos.CENTER);
        grid.setHgap(20);
        grid.setVgap(40);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Scene scene = new Scene(grid, 500, 370);
        stage.setScene(scene);
        stage.setMinHeight(350);
        stage.setMinWidth(500);

        Text scenetitle = new Text("Welcome to ChessHunt Online...");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 30));
        scenetitle.setFill(Color.AQUA);
        scenetitle.setUnderline(true);


        grid.add(scenetitle, 0, 0, 4, 1);

        Label userName = new Label("UserName: ");
        grid.add(userName, 1, 1);
        userName.setStyle("-fx-background-color: #D3D3D3");

        TextField userTextField = new TextField();
        userTextField.setStyle("-fx-background-color: #D3D3D3");
        userTextField.setText(null);
        grid.add(userTextField, 3, 1);
        userTextField.setPromptText("admin");

        Label pw = new Label("PassWord: ");
        pw.setStyle("-fx-background-color: #D3D3D3");
        grid.add(pw, 1, 2);

        PasswordField pwBox = new PasswordField();
        pwBox.setPromptText("  1234");
        pwBox.setText(null);
        grid.add(pwBox, 3, 2,1,1);

        Button btn = new Button("Log-In");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 3, 4);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);
        //grid.getChildren().add(new ImageView(image));
        grid.setStyle("-fx-background-image: url(sample/CursorPng/images.jpg)");

        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                String s = userTextField.getText();
                MyName = userTextField.getText();
                userTextField.setText(null);
                MyPassWord = pwBox.getText();
                pwBox.setText(null);

                if(s!=null){
                    actiontarget.setFill(Color.FIREBRICK);
                    actiontarget.setText("You are Logged Inn");
                    hasLogin = true;
                    online = true;
                    huntPage();
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Invalid Inputs");
                    alert.setHeaderText("USERNAME is null");
                    alert.setContentText("Please give a valid username....");
                    alert.showAndWait();
                }
            }
        });

        stage.show();
    }

	public void huntPage(){
		if(hasLogin && online){

            // initialize the layout, create a CustomControl and add it to the layout
            stackLayout = new StackPane();
            gameController = new GameControl();
            stackLayout.getChildren().add(gameController);

            typeLabel.setEffect(new InnerShadow());

            bLabel.setTextFill(Color.BLACK);
            wLabel.setTextFill(Color.WHITE);
            bLabel.setText("Black's Part");
            wLabel.setText("White's Part");
            bLabel.setStyle("-fx-font-size: 15;");
            wLabel.setStyle("-fx-font-size: 15;");
            myName.setStyle("-fx-font-size: 15;");
            opponensName.setStyle("-fx-font-size: 12;");
            typeLabel.setTextFill(Color.BLACK);
            typeLabel.setStyle("-fx-font-size: 14;");

			huntStage.setTitle("     Chess_Hunt....Online [ 1605047 & 1605060]");
			huntStage.setScene(new Scene(stackLayout, 650, 550));

			// create text box
			TextArea status = new TextArea();
			status.setEditable(false);
			status.setEffect(new InnerShadow());
			status.setPromptText("It shows all status updates" +
                    " but for the networking issue" +
                    " we dissabled it..." +
                    "and feel sorry for that");
			status.setStyle("-fx-font-size: 15;");
			status.setWrapText(true);
			status.setPrefWidth(150);
			status.setPrefHeight(800);
			//making situation for printing in the TextArea
			PrintStream ps = System.out;
			System.setOut(new PriningInTextAera(status, ps));

			//add status in a VBox
			VBox vb1 = new VBox();
			vb1.getChildren().add(status);
			vb1.setPrefWidth(150);

			BorderPane bp = new BorderPane();
			HBox hb1 = new HBox();

			HBox hb2 = new HBox();

			//make it very much clear
			myName.setText(MyName);
			opponensName.setText(OpponentsName);


			hb1.setAlignment(Pos.CENTER);//make it resizable
			hb1.getChildren().addAll(bLabel,typeLabel);
			hb1.setSpacing(60);
			hb1.setStyle("-fx-background-color: #D3D3D3");
			hb1.setLayoutY(30);

			hb2.setAlignment(Pos.CENTER);//make it resizable
			hb2.getChildren().addAll(wLabel);
			hb2.setSpacing(60);
			hb2.setStyle("-fx-background-color: #2C2C2C");
			hb2.setLayoutY(30);

			huntStage.setScene(new Scene(bp, 550, 450 ));
			bp.setCenter(stackLayout);
			bp.setTop(hb1);
			bp.setBottom(hb2);
			bp.setRight(vb1);

			huntStage.setMaxHeight(700);
			huntStage.setMaxWidth(900);
			huntStage.setMinWidth(500);
			huntStage.setMinHeight(400);
			huntStage.show();

			typeLabel.setOnMouseClicked(e->{
			    gameController.makeTurnTrue();
            });

			stage.setOnCloseRequest(e->{
				gameController.player.closeConnection();
			});
		}
	}

	public void showOfflineGame(){//intentionally made disable due to time problem!
       if(offline){
           // initialize the layout, create a CustomControl and add it to the layout
           stackLayout = new StackPane();
          // gameController1 = new GameControlOff();
           //stackLayout.getChildren().add(gameController1);

           typeLabel.setEffect(new InnerShadow());

           bLabel.setTextFill(Color.BLACK);
           wLabel.setTextFill(Color.WHITE);
           bLabel.setText("Black's Part");
           wLabel.setText("White's Part");
           bLabel.setStyle("-fx-font-size: 15;");
           wLabel.setStyle("-fx-font-size: 15;");
           myName.setStyle("-fx-font-size: 15;");
           opponensName.setStyle("-fx-font-size: 12;");
           typeLabel.setTextFill(Color.BLACK);
           typeLabel.setStyle("-fx-font-size: 14;");

           huntStage.setTitle("     Chess_Hunt....Offline [ 1605047 & 1605060]");
           huntStage.setScene(new Scene(stackLayout, 650, 550));

           // create text box
           TextArea status = new TextArea();
           status.setEditable(false);
           status.setEffect(new InnerShadow());
           status.setPromptText("All Status Updates");
           status.setStyle("-fx-font-size: 15;");
           status.setWrapText(true);
           status.setPrefWidth(150);
           status.setPrefHeight(800);
           //making situation for printing in the TextArea
           PrintStream ps = System.out;
           System.setOut(new PriningInTextAera(status, ps));

           //add status in a VBox
           VBox vb1 = new VBox();
           vb1.getChildren().add(status);
           vb1.setPrefWidth(150);

           BorderPane bp = new BorderPane();
           HBox hb1 = new HBox();

           HBox hb2 = new HBox();

           //make it very much clear
           myName.setText(MyName);
           opponensName.setText(OpponentsName);

       }
    }


	// overridden stop method
	@Override
	public void stop(){}
	
	public void appendToStatus(String Text) {
       status.appendText(Text);     
    }
	 
	public String getStatusText() {
	  return status.getText();
	}
	
    //added just now
    public static void serverError(){
        System.out.println("Ops BRO!!! Server has gone !!!!");
    }

    //added just now
    public static void clientError(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Game Dismissed");
        alert.setHeaderText("Look, opponent has gone");
        alert.setContentText("Ooops, there was an error!");

        alert.showAndWait();
    }

    public static Stage getStage(){
		return stage;
	}

	//Launch Pp Pp
	public static void main(String[] args) {
		launch(args);
	}
}
