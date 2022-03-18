package sample.GameChess.PlayGame.Controllers;
import java.io.OutputStream;
import java.io.PrintStream;

import javafx.scene.control.TextArea;

public class PriningInTextAera extends PrintStream {
    public static final int WHITE_PLAYER = 1,BLACK_PLAYER = 2,EMPTY_PLAYER = 0;

    //The TextArea to which the output stream will be redirected.
    //A very beautiful OverRidden has taken place here
    private TextArea status;

    public PriningInTextAera(TextArea area, OutputStream out) {
	super(out);
	status = area;
    }

    //what is the problem in prissmTextLayout????????????/can anyone give me a solution??
    //plat form runlater is very slow yet?not responding
    //ORACLE YOU SHOULD MAKE A VERY GOOD TUTORIAL ABOUT THIS
   /* public void println(String string) {
	    status.appendText("\n");
        //javafx.application.Platform.runLater( () -> status.appendText(string+"\n") );
        new Thread(()->{
            status.appendText(string+"\n");
        });
    }*/


    /*public void print(String string) {
	status.appendText(string);
      *//*  new Thread(()->{
            status.appendText(string);
        });*//*
	//javafx.application.Platform.runLater(()->status.appendText(string));
    }*/
}
