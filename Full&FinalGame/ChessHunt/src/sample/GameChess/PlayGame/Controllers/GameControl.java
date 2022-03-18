package sample.GameChess.PlayGame.Controllers;


/**
 *
 * This is the sample controller class which handles the whole netWorking issues
 * @author  Ahsanul Ameen Sabit
 */

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.control.Control;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;


import sample.GameChess.PlayGame.HuntNow;
import sample.GameChess.PlayGame.PieceClasses.*;
import sample.GameChess.PlayGame.clientS.Player;

public class GameControl extends Control {
    public static final int WHITE_PLAYER = 1, BLACK_PLAYER = 2, EMPTY_PLAYER = 0;
    public Player player;

    private  static boolean isMyTurn = false;
    private int myType = BLACK_PLAYER;

    private String message = null;//the full and final move message
    private int si,sj;
    private int ti,tj;
    private Piece selectedPiece;
    private Piece targetedPiece;
    private boolean isJunkSelected;
    private boolean winner=false;
    private boolean stale=false;


    private BoardImplementation chessboard;
    private Translate pos;
    private Logics Logics;
    private int staleCountBlack=8;
    private int staleCountWhite=8;
    private int hash;


    public void makeTurnTrue(){
        String s = (myType==1)?"WHITE":"BLACK";
        HuntNow.typeLabel.setText("YOUR Type is : "+s);
    }


    public int getHash() {
        return this.hash;
    }
    /**______________________(ADDED _____ EXTERNALLY)_____________________________*/



    public GameControl(){

        pos = new Translate();
        setSkin(new GameControlSkin(this));
        chessboard = new BoardImplementation();
        Logics = new Logics();
        getChildren().addAll(chessboard);

        // Places background squares
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                chessboard.placeBoard(i, j);
            }
        }

        // Places chess piece images
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                chessboard.placeImageViews(i, j);
            }
        }



        setOnKeyPressed(new EventHandler<KeyEvent>(){//when pressing space bar
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.SPACE)
                    System.out.print("Game Reset!");
                chessboard = new BoardImplementation();
                getChildren().addAll(chessboard);

                // Places background squares
                for(int i = 0; i < 8; i++){
                    for(int j = 0; j < 8; j++){
                        chessboard.placeBoard(i, j);
                    }
                }

                // Places chess piece images
                for(int i = 0; i < 8; i++){
                    for(int j = 0; j < 8; j++){
                        chessboard.placeImageViews(i, j);
                    }
                }
                // Reset game variables
                stale=false;
                winner=false;
                staleCountWhite=8;
                staleCountBlack=8;
                chessboard.changeclickfalse();
            }
        });//don't Extract & become confused

        setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try{
                    if(isMyTurn){

                        hash = event.getTarget().hashCode();//clean totally
                        ImageView [][]selectView = chessboard.getImageviews();
                        Rectangle[][] targetSelect = chessboard.getBoard();

                        message = null;//intentionally made null just in mouse handler

                        boolean founds = false;//clean
                        for(int x=0;x<8 && !founds;x++){
                            for(int y=0;y<8 && !founds;y++){
                                if(selectView[x][y].hashCode() == hash && selectView[x][y]!=null){
                                    si = x; sj = y;
                                    founds = true;
                                }
                            }
                        }

                        boolean foundt = false;//clean
                        for(int x=0;x<8 && !foundt;x++){
                            for(int y=0;y<8 && !foundt;y++){
                                if(selectView[x][y].hashCode() == hash || targetSelect[x][y].hashCode() == hash){
                                    ti = x; tj = y;
                                    foundt = true;
                                }
                            }
                        }


                        // Prints current player..//clean
                        if(chessboard.getClicklogic().equals("false") && !winner && !stale){
                            if(chessboard.currentplayer()==WHITE_PLAYER){
                                System.out.print("Current player: WHITE\n");
                            }
                            else{System.out.print("Current player: BLACK\n");}
                        }


                        /**Explanation:
                         *
                         * 1. if clickLogic == null  mane hoilo move dewa complete, akhon aitake false banao
                         *
                         * 2. if clickLogic == false mane hoilo ami akhon just select korte parbo and then
                         *      junc-tunk select na korle clickLogic =  true banabo but jucselection true
                         *      hoile lav nai, clickLogic false e thakbe, mane next click a just select piece hobe not target
                         *
                         * 3. if clickLogic == true then valid move dibo target select kore and then clickLogic  = null banabo
                         *      but valid move na dewa porjonto true e thakbe
                         *
                         * */

/**+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

                        // Second click
                        if(chessboard.getClicklogic() == "true"){
                            Piece[][] boardstate = chessboard.getState();//copied
                            targetedPiece = chessboard.selectTarget(ti,tj);
/**________________________________________________start_copying_from_here_again______________________________________________________*/
                            // If pawn selected ..
                            if(selectedPiece.toString().equals("Pawn") && selectedPiece != null && targetedPiece != null
                                    && !selectedPiece.equals(targetedPiece)){
                                // Only executes if legal move ..
                                if(chessboard.getStroke(targetedPiece.icoord(), targetedPiece.jcoord(), Color.CORNFLOWERBLUE)
                                        || chessboard.getStroke(targetedPiece.icoord(), targetedPiece.jcoord(), Color.AQUAMARINE)){
                                    // If check is false
                                    if(!Logics.checkstatus()){
                                        Piece[][] oldstate = new Piece[8][8];
                                        // Transfer pieces to backup variable
                                        for(int x=0;x < 8; x++){
                                            for(int y=0; y < 8; y++){
                                                oldstate[x][y] = boardstate[x][y];
                                            }
                                        }
                                        // Do move
                                        boardstate = selectedPiece.movepawn(selectedPiece, targetedPiece, boardstate);
                                        // If move results in no check, do move
                                        if(!Logics.check4check(chessboard.otherplayer(), boardstate)){
                                            chessboard.setBoard(boardstate);
                                            chessboard.drawMove(selectedPiece.icoord(), selectedPiece.jcoord(), targetedPiece.icoord(), targetedPiece.jcoord());
                                            message = selectedPiece.icoord()+" "+selectedPiece.jcoord()+" "+targetedPiece.icoord()+" "+targetedPiece.jcoord();
                                            chessboard.changePlayer();
                                            chessboard.changeclicknull();
                                            // Successful move
                                            staleCountWhite=8;
                                            staleCountBlack=8;
                                        }
                                        // If check, reverse move
                                        else{
                                            //message = null;
                                            chessboard.changeclicknull();
                                            chessboard.setBoard(oldstate);
                                            Logics.flipcheck();

                                            // Stalemate check
                                            if(selectedPiece.type()==1){
                                                staleCountWhite--;
                                            }
                                            if(selectedPiece.type()==2){
                                                staleCountBlack--;
                                            }
                                            if(staleCountWhite==0 || staleCountBlack==0){
                                                System.out.print("STALEMATE FOUND!!\n");
                                                stale=true;
                                            }

                                        }
                                    }

                                    // If in check ..
                                    if(Logics.checkstatus()){
                                        // Do move
                                        Piece[][] oldstate = new Piece[8][8];
                                        for(int x=0;x < 8; x++){
                                            for(int y=0; y < 8; y++){
                                                oldstate[x][y] = boardstate[x][y];
                                            }
                                        }
                                        boardstate = selectedPiece.movepawn(selectedPiece, targetedPiece, boardstate);
                                        // Check if still in check, if not, do move
                                        if(!Logics.check4check(chessboard.otherplayer(), boardstate)){
                                            chessboard.setBoard(boardstate);
                                            chessboard.drawMove(selectedPiece.icoord(), selectedPiece.jcoord(), targetedPiece.icoord(), targetedPiece.jcoord());
                                            message = selectedPiece.icoord()+" "+selectedPiece.jcoord()+" "+targetedPiece.icoord()+" "+targetedPiece.jcoord();
                                            chessboard.changePlayer();
                                            chessboard.changeclicknull();
                                            staleCountWhite=8;
                                            staleCountBlack=8;
                                            // If still in check, undo move
                                        }
                                        else{
                                            // message = null;
                                            chessboard.changeclicknull();
                                            chessboard.setBoard(oldstate);
                                            Logics.flipcheck();

                                            // Stalemate check
                                            if(selectedPiece.type()==1){
                                                staleCountWhite--;
                                            }
                                            if(selectedPiece.type()==2){
                                                staleCountBlack--;
                                            }
                                            if(staleCountWhite==0 || staleCountBlack==0){
                                                System.out.print("STALEMATE FOUND!!\n");
                                                stale=true;
                                            }
                                        }
                                    }

                                }
                                else{

                                    // Stalemate check
                                    if(selectedPiece.type()==1){
                                        staleCountWhite--;
                                    }
                                    if(selectedPiece.type()==2){
                                        staleCountBlack--;
                                    }
                                    if(staleCountWhite==0 || staleCountBlack==0){
                                        System.out.print("STALEMATE FOUND!!\n");
                                        stale=true;
                                    }
                                }
                            }

                            // If bishop selected ..
                            if(selectedPiece.toString().equals("Bishop") && selectedPiece != null && targetedPiece != null && !selectedPiece.equals(targetedPiece)){
                                if(chessboard.getStroke(targetedPiece.icoord(), targetedPiece.jcoord(), Color.CORNFLOWERBLUE) || chessboard.getStroke(targetedPiece.icoord(), targetedPiece.jcoord(), Color.AQUAMARINE)){
                                    // If check is false
                                    if(!Logics.checkstatus()){
                                        Piece[][] oldstate = new Piece[8][8];
                                        // Transfer pieces to backup variable
                                        for(int x=0;x < 8; x++){
                                            for(int y=0; y < 8; y++){
                                                oldstate[x][y] = boardstate[x][y];
                                            }
                                        }
                                        // Do move
                                        boardstate = selectedPiece.movebishop(selectedPiece, targetedPiece, boardstate);
                                        // If move results in no check, do move
                                        if(!Logics.check4check(chessboard.otherplayer(), boardstate)){
                                            chessboard.setBoard(boardstate);
                                            chessboard.drawMove(selectedPiece.icoord(), selectedPiece.jcoord(), targetedPiece.icoord(), targetedPiece.jcoord());
                                            message = selectedPiece.icoord()+" "+selectedPiece.jcoord()+" "+targetedPiece.icoord()+" "+targetedPiece.jcoord();
                                            chessboard.changePlayer();
                                            chessboard.changeclicknull();
                                            staleCountWhite=8;
                                            staleCountBlack=8;
                                        }
                                        // If check, reverse move
                                        else{
                                            //message = null;
                                            chessboard.changeclicknull();
                                            chessboard.setBoard(oldstate);
                                            Logics.flipcheck();

                                            // Stalemate check
                                            if(selectedPiece.type()==1){
                                                staleCountWhite--;
                                            }
                                            if(selectedPiece.type()==2){
                                                staleCountBlack--;
                                            }
                                            if(staleCountWhite==0 || staleCountBlack==0){
                                                System.out.print("STALEMATE FOUND!!\n");
                                                stale=true;
                                            }
                                        }
                                    }

                                    // If in check ..
                                    if(Logics.checkstatus()){
                                        // Do move
                                        Piece[][] oldstate = new Piece[8][8];
                                        for(int x=0;x < 8; x++){
                                            for(int y=0; y < 8; y++){
                                                oldstate[x][y] = boardstate[x][y];
                                            }
                                        }
                                        boardstate = selectedPiece.movebishop(selectedPiece, targetedPiece, boardstate);
                                        // Check if still in check, if not, do move
                                        if(!Logics.check4check(chessboard.otherplayer(), boardstate)){
                                            chessboard.setBoard(boardstate);
                                            chessboard.drawMove(selectedPiece.icoord(), selectedPiece.jcoord(), targetedPiece.icoord(), targetedPiece.jcoord());
                                            message = selectedPiece.icoord()+" "+selectedPiece.jcoord()+" "+targetedPiece.icoord()+" "+targetedPiece.jcoord();
                                            chessboard.changePlayer();
                                            chessboard.changeclicknull();
                                            staleCountWhite=8;
                                            staleCountBlack=8;
                                            // If still in check, undo move
                                        }
                                        else{
                                            //message = null;
                                            chessboard.changeclicknull();
                                            chessboard.setBoard(oldstate);
                                            Logics.flipcheck();

                                            // Stalemate check
                                            if(selectedPiece.type()==1){
                                                staleCountWhite--;
                                            }
                                            if(selectedPiece.type()==2){
                                                staleCountBlack--;
                                            }
                                            if(staleCountWhite==0 || staleCountBlack==0){
                                                System.out.print("STALEMATE FOUND!!\n");
                                                stale=true;
                                            }
                                        }
                                    }
                                }
                                else{

                                    // Stalemate check
                                    if(selectedPiece.type()==1){
                                        staleCountWhite--;
                                    }
                                    if(selectedPiece.type()==2){
                                        staleCountBlack--;
                                    }
                                    if(staleCountWhite==0 || staleCountBlack==0){
                                        System.out.print("STALEMATE FOUND!!\n");
                                        stale=true;
                                    }
                                }
                            }

                            // If queen selected ..
                            if(selectedPiece.toString().equals("Queen") && selectedPiece != null && targetedPiece != null && !selectedPiece.equals(targetedPiece)){
                                if(chessboard.getStroke(targetedPiece.icoord(), targetedPiece.jcoord(), Color.CORNFLOWERBLUE) || chessboard.getStroke(targetedPiece.icoord(), targetedPiece.jcoord(), Color.AQUAMARINE)){
                                    // If check is false
                                    if(!Logics.checkstatus()){
                                        Piece[][] oldstate = new Piece[8][8];
                                        // Transfer pieces to backup variable
                                        for(int x=0;x < 8; x++){
                                            for(int y=0; y < 8; y++){
                                                oldstate[x][y] = boardstate[x][y];
                                            }
                                        }
                                        // Do move
                                        boardstate = selectedPiece.movequeen(selectedPiece, targetedPiece, boardstate);
                                        // If move results in no check, do move
                                        if(!Logics.check4check(chessboard.otherplayer(), boardstate)){
                                            chessboard.setBoard(boardstate);
                                            chessboard.drawMove(selectedPiece.icoord(), selectedPiece.jcoord(), targetedPiece.icoord(), targetedPiece.jcoord());
                                            message = selectedPiece.icoord()+" "+selectedPiece.jcoord()+" "+targetedPiece.icoord()+" "+targetedPiece.jcoord();
                                            chessboard.changePlayer();
                                            chessboard.changeclicknull();
                                            staleCountWhite=8;
                                            staleCountBlack=8;
                                        }
                                        // If check, reverse move
                                        else{
                                            //message = null;
                                            chessboard.changeclicknull();
                                            chessboard.setBoard(oldstate);
                                            Logics.flipcheck();
                                            if(selectedPiece.type()==1){
                                                staleCountWhite--;
                                            }
                                            if(selectedPiece.type()==2){
                                                staleCountBlack--;
                                            }
                                            if(staleCountWhite==0 || staleCountBlack==0){
                                                System.out.print("STALEMATE FOUND!!\n");
                                                stale=true;
                                            }
                                        }
                                    }

                                    // If in check ..
                                    if(Logics.checkstatus()){
                                        // Do move
                                        Piece[][] oldstate = new Piece[8][8];
                                        for(int x=0;x < 8; x++){
                                            for(int y=0; y < 8; y++){
                                                oldstate[x][y] = boardstate[x][y];
                                            }
                                        }
                                        boardstate = selectedPiece.movequeen(selectedPiece, targetedPiece, boardstate);
                                        // Check if still in check, if not, do move
                                        if(!Logics.check4check(chessboard.otherplayer(), boardstate)){
                                            chessboard.setBoard(boardstate);
                                            chessboard.drawMove(selectedPiece.icoord(), selectedPiece.jcoord(), targetedPiece.icoord(), targetedPiece.jcoord());
                                            message = selectedPiece.icoord()+" "+selectedPiece.jcoord()+" "+targetedPiece.icoord()+" "+targetedPiece.jcoord();
                                            chessboard.changePlayer();
                                            chessboard.changeclicknull();
                                            staleCountWhite=8;
                                            staleCountBlack=8;
                                            // If still in check, undo move
                                        }
                                        else{
                                            //message = null;
                                            chessboard.changeclicknull();
                                            chessboard.setBoard(oldstate);
                                            Logics.flipcheck();

                                            // Stalemate check
                                            if(selectedPiece.type()==1){
                                                staleCountWhite--;
                                            }
                                            if(selectedPiece.type()==2){
                                                staleCountBlack--;
                                            }
                                        }
                                        if(staleCountWhite==0 || staleCountBlack==0){
                                            System.out.print("STALEMATE FOUND!!\n");
                                            stale=true;
                                        }
                                    }
                                }
                                else{

                                    // Stalemate check
                                    if(selectedPiece.type()==1){
                                        staleCountWhite--;
                                    }
                                    if(selectedPiece.type()==2){
                                        staleCountBlack--;
                                    }
                                    if(staleCountWhite==0 || staleCountBlack==0){
                                        System.out.print("STALEMATE FOUND!!\n");
                                        stale=true;
                                    }
                                }
                            }

                            // If rook selected ..
                            if(selectedPiece.toString().equals("Rook") && selectedPiece != null && targetedPiece != null && !selectedPiece.equals(targetedPiece)){
                                if(chessboard.getStroke(targetedPiece.icoord(), targetedPiece.jcoord(), Color.CORNFLOWERBLUE) || chessboard.getStroke(targetedPiece.icoord(), targetedPiece.jcoord(), Color.AQUAMARINE)){
                                    // If check is false
                                    if(!Logics.checkstatus()){
                                        Piece[][] oldstate = new Piece[8][8];
                                        // Transfer pieces to backup variable
                                        for(int x=0;x < 8; x++){
                                            for(int y=0; y < 8; y++){
                                                oldstate[x][y] = boardstate[x][y];
                                            }
                                        }
                                        // Do move
                                        boardstate = selectedPiece.moverook(selectedPiece, targetedPiece, boardstate);
                                        // If move results in no check, do move
                                        if(!Logics.check4check(chessboard.otherplayer(), boardstate)){
                                            chessboard.setBoard(boardstate);
                                            chessboard.drawMove(selectedPiece.icoord(), selectedPiece.jcoord(), targetedPiece.icoord(), targetedPiece.jcoord());
                                            message = selectedPiece.icoord()+" "+selectedPiece.jcoord()+" "+targetedPiece.icoord()+" "+targetedPiece.jcoord();
                                            chessboard.changePlayer();
                                            chessboard.changeclicknull();
                                            staleCountWhite=8;
                                            staleCountBlack=8;
                                        }
                                        // If check, reverse move
                                        else{
                                            //message = null;
                                            chessboard.changeclicknull();
                                            chessboard.setBoard(oldstate);
                                            Logics.flipcheck();

                                            // Stalemate check
                                            if(selectedPiece.type()==1){
                                                staleCountWhite--;
                                            }
                                            if(selectedPiece.type()==2){
                                                staleCountBlack--;
                                            }
                                            if(staleCountWhite==0 || staleCountBlack==0){
                                                System.out.print("STALEMATE FOUND!!\n");
                                                stale=true;
                                            }
                                        }
                                    }

                                    // If in check ..
                                    if(Logics.checkstatus()){
                                        // Do move
                                        Piece[][] oldstate = new Piece[8][8];
                                        for(int x=0;x < 8; x++){
                                            for(int y=0; y < 8; y++){
                                                oldstate[x][y] = boardstate[x][y];
                                            }
                                        }
                                        boardstate = selectedPiece.moverook(selectedPiece, targetedPiece, boardstate);
                                        // Check if still in check, if not, do move
                                        if(!Logics.check4check(chessboard.otherplayer(), boardstate)){
                                            chessboard.setBoard(boardstate);
                                            chessboard.drawMove(selectedPiece.icoord(), selectedPiece.jcoord(), targetedPiece.icoord(), targetedPiece.jcoord());
                                            message = selectedPiece.icoord()+" "+selectedPiece.jcoord()+" "+targetedPiece.icoord()+" "+targetedPiece.jcoord();
                                            chessboard.changePlayer();
                                            chessboard.changeclicknull();
                                            staleCountWhite=8;
                                            staleCountBlack=8;
                                            // If still in check, undo move
                                        }
                                        else{
                                            //message = null;
                                            chessboard.changeclicknull();
                                            chessboard.setBoard(oldstate);
                                            Logics.flipcheck();

                                            // Stalemate check
                                            if(selectedPiece.type()==1){
                                                staleCountWhite--;
                                            }
                                            if(selectedPiece.type()==2){
                                                staleCountBlack--;
                                            }
                                            if(staleCountWhite==0 || staleCountBlack==0){
                                                System.out.print("STALEMATE FOUND!!\n");
                                                stale=true;
                                            }
                                        }
                                    }
                                }
                                else{

                                    // Stalemate check
                                    if(selectedPiece.type()==1){
                                        staleCountWhite--;
                                    }
                                    if(selectedPiece.type()==2){
                                        staleCountBlack--;
                                    }
                                    if(staleCountWhite==0 || staleCountBlack==0){
                                        System.out.print("STALEMATE FOUND!!\n");
                                        stale=true;
                                    }
                                }
                            }

                            // If king selected ..
                            if(selectedPiece.toString().equals("King") && selectedPiece != null && targetedPiece != null && !selectedPiece.equals(targetedPiece)){
                                if(chessboard.getStroke(targetedPiece.icoord(), targetedPiece.jcoord(), Color.CORNFLOWERBLUE) || chessboard.getStroke(targetedPiece.icoord(), targetedPiece.jcoord(), Color.AQUAMARINE)){
                                    // If check is false
                                    if(!Logics.checkstatus()){
                                        Piece[][] oldstate = new Piece[8][8];
                                        // Transfer pieces to backup variable
                                        for(int x=0;x < 8; x++){
                                            for(int y=0; y < 8; y++){
                                                oldstate[x][y] = boardstate[x][y];
                                            }
                                        }
                                        // Do move
                                        boardstate = selectedPiece.moveking(selectedPiece, targetedPiece, boardstate);
                                        // If move results in no check, do move
                                        if(!Logics.check4check(chessboard.otherplayer(), boardstate)){
                                            chessboard.setBoard(boardstate);
                                            chessboard.drawMove(selectedPiece.icoord(), selectedPiece.jcoord(), targetedPiece.icoord(), targetedPiece.jcoord());
                                            message = selectedPiece.icoord()+" "+selectedPiece.jcoord()+" "+targetedPiece.icoord()+" "+targetedPiece.jcoord();
                                            chessboard.changePlayer();
                                            chessboard.changeclicknull();
                                            staleCountWhite=8;
                                            staleCountBlack=8;
                                        }
                                        // If check, reverse move
                                        else{
                                            //message = null;
                                            chessboard.changeclicknull();
                                            chessboard.setBoard(oldstate);
                                            Logics.flipcheck();

                                            // Stalemate check
                                            if(selectedPiece.type()==1){
                                                staleCountWhite--;
                                            }
                                            if(selectedPiece.type()==2){
                                                staleCountBlack--;
                                            }
                                            if(staleCountWhite==0 || staleCountBlack==0){
                                                System.out.print("STALEMATE FOUND!!\n");
                                                stale=true;
                                            }
                                        }
                                    }

                                    // If in check ..
                                    if(Logics.checkstatus()){
                                        // Do move
                                        Piece[][] oldstate = new Piece[8][8];
                                        for(int x=0;x < 8; x++){
                                            for(int y=0; y < 8; y++){
                                                oldstate[x][y] = boardstate[x][y];
                                            }
                                        }
                                        boardstate = selectedPiece.moveking(selectedPiece, targetedPiece, boardstate);
                                        // Check if still in check, if not, do move
                                        if(!Logics.check4check(chessboard.otherplayer(), boardstate)){
                                            chessboard.setBoard(boardstate);
                                            chessboard.drawMove(selectedPiece.icoord(), selectedPiece.jcoord(), targetedPiece.icoord(), targetedPiece.jcoord());
                                            message = selectedPiece.icoord()+" "+selectedPiece.jcoord()+" "+targetedPiece.icoord()+" "+targetedPiece.jcoord();
                                            chessboard.changePlayer();
                                            chessboard.changeclicknull();
                                            staleCountWhite=8;
                                            staleCountBlack=8;
                                            // If still in check, undo move
                                        }
                                        else{
                                            //message = null;
                                            chessboard.changeclicknull();
                                            chessboard.setBoard(oldstate);
                                            Logics.flipcheck();

                                            // Stalemate check
                                            if(selectedPiece.type()==1){
                                                staleCountWhite--;
                                            }
                                            if(selectedPiece.type()==2){
                                                staleCountBlack--;
                                            }
                                            if(staleCountWhite==0 || staleCountBlack==0){
                                                System.out.print("STALEMATE FOUND!!\n");
                                                stale=true;
                                            }
                                        }
                                    }
                                }
                                else{

                                    // Stalemate check
                                    if(selectedPiece.type()==1){
                                        staleCountWhite--;
                                    }
                                    if(selectedPiece.type()==2){
                                        staleCountBlack--;
                                    }
                                    if(staleCountWhite==0 || staleCountBlack==0){
                                        System.out.print("STALEMATE FOUND!!\n");
                                        stale=true;
                                    }
                                }
                            }

                            // If knight selected ..
                            if(selectedPiece.toString().equals("Knight") && selectedPiece != null && targetedPiece != null && !selectedPiece.equals(targetedPiece)){
                                if(chessboard.getStroke(targetedPiece.icoord(), targetedPiece.jcoord(), Color.CORNFLOWERBLUE) || chessboard.getStroke(targetedPiece.icoord(), targetedPiece.jcoord(), Color.AQUAMARINE)){
                                    // If check is false
                                    if(!Logics.checkstatus()){
                                        Piece[][] oldstate = new Piece[8][8];
                                        // Transfer pieces to backup variable
                                        for(int x=0;x < 8; x++){
                                            for(int y=0; y < 8; y++){
                                                oldstate[x][y] = boardstate[x][y];
                                            }
                                        }
                                        // Do move
                                        boardstate = selectedPiece.moveknight(selectedPiece, targetedPiece, boardstate);
                                        // If move results in no check, do move
                                        if(!Logics.check4check(chessboard.otherplayer(), boardstate)){
                                            chessboard.setBoard(boardstate);
                                            chessboard.drawMove(selectedPiece.icoord(), selectedPiece.jcoord(), targetedPiece.icoord(), targetedPiece.jcoord());
                                            message = selectedPiece.icoord()+" "+selectedPiece.jcoord()+" "+targetedPiece.icoord()+" "+targetedPiece.jcoord();
                                            chessboard.changePlayer();
                                            chessboard.changeclicknull();
                                            staleCountWhite=8;
                                            staleCountBlack=8;
                                        }
                                        // If check, reverse move
                                        else{
                                            //message = null;
                                            chessboard.changeclicknull();
                                            chessboard.setBoard(oldstate);
                                            Logics.flipcheck();

                                            // Stalemate check
                                            if(selectedPiece.type()==1){
                                                staleCountWhite--;
                                            }
                                            if(selectedPiece.type()==2){
                                                staleCountBlack--;
                                            }
                                            if(staleCountWhite==0 || staleCountBlack==0){
                                                System.out.print("STALEMATE FOUND!!\n(rare yet)");
                                                stale=true;
                                            }
                                        }
                                    }

                                    // If in check ..
                                    if(Logics.checkstatus()){
                                        // Do move
                                        Piece[][] oldstate = new Piece[8][8];
                                        for(int x=0;x < 8; x++){
                                            for(int y=0; y < 8; y++){
                                                oldstate[x][y] = boardstate[x][y];
                                            }
                                        }
                                        boardstate = selectedPiece.moveknight(selectedPiece, targetedPiece, boardstate);
                                        // Check if still in check, if not, do move
                                        if(!Logics.check4check(chessboard.otherplayer(), boardstate)){
                                            chessboard.setBoard(boardstate);
                                            chessboard.drawMove(selectedPiece.icoord(), selectedPiece.jcoord(), targetedPiece.icoord(), targetedPiece.jcoord());
                                            message = selectedPiece.icoord()+" "+selectedPiece.jcoord()+" "+targetedPiece.icoord()+" "+targetedPiece.jcoord();
                                            chessboard.changePlayer();
                                            chessboard.changeclicknull();
                                            staleCountWhite=8;
                                            staleCountBlack=8;
                                            // If still in check, undo move
                                        }
                                        else{
                                            //message = null;
                                            chessboard.changeclicknull();
                                            chessboard.setBoard(oldstate);
                                            Logics.flipcheck();

                                            // Stalemate check
                                            if(selectedPiece.type()==1){
                                                staleCountWhite--;
                                            }
                                            if(selectedPiece.type()==2){
                                                staleCountBlack--;
                                            }
                                            if(staleCountWhite==0 || staleCountBlack==0){
                                                System.out.print("STALEMATE FOUND!!\n");
                                                stale=true;
                                            }
                                        }
                                    }
                                }
                                else{

                                    // Stalemate check
                                    if(selectedPiece.type()==WHITE_PLAYER){
                                        staleCountWhite--;
                                    }
                                    if(selectedPiece.type()==BLACK_PLAYER){
                                        staleCountBlack--;
                                    }
                                    if(staleCountWhite==0 || staleCountBlack==0){
                                        //shob khaia laicho naki??what the hell it is?
                                        System.out.print("STALEMATE found!!");
                                        stale=true;
                                    }
                                }
                            }

                            // If screw up ..//liar liar pants on fire// do you know the rhyme?????
                            else{
                                chessboard.changeclicknull();
                                chessboard.clearHighLights();
                            }

                            chessboard.clearHighLights();
                            chessboard.changeclicknull();

                            // Check for checkmate ..
                            if(Logics.ischeckMateOccurs(chessboard.otherplayer(), chessboard.getState())=="true"){
                                winner=true;
                            }

                            getScene().setCursor(Cursor.DEFAULT);

                            // highlight check..

                            if(Logics.checkstatus()){
                                chessboard.checkHighLight(Logics.checkCoordI(), Logics.checkCoordJ());
                                if(!winner){
                                    System.out.print("\nCHECK FOUND!!\n\n\n");}
                                chessboard.changeclicknull();
                            }
                        }

/**_____________________________________________up_to_here_________________________________________________*///done





//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                        // First click
                        if(chessboard.getClicklogic().equals("false") && !stale && !winner){
                            selectedPiece = chessboard.selectPiece(si,sj);

                            if(selectedPiece.toString().equals("Empty") || !chessboard.pieceselect()){
                                isJunkSelected=true;
                            }
                            else{isJunkSelected=false;}

                            if(!selectedPiece.equals("Empty") && !isJunkSelected){
                                getScene().setCursor(new ImageCursor(selectedPiece.image()));
                                chessboard.changeclicktrue();
                                // Highlights valid moves..
                                chessboard.validMoves(selectedPiece);}

                            // Check 4 check .....for otherPlayer//he will be the current Player
                            if(!Logics.checkstatus()){
                                Logics.check4check(chessboard.otherplayer(), chessboard.getState());}
                        }

/**__________________________________again_start_copying_from_here________________________________________________*/
//************************no need
                        if(chessboard.getClicklogic().equals("true")){
                            System.out.print("No of WhitePieces: " + whitepieces(chessboard.getState())
                                    + "\nNo of BlackPieces: " + blackpieces(chessboard.getState()));
                            System.out.print("\n/*********************/\n");
                        }


                        // If completed move, return to first click ..
                        if(chessboard.getClicklogic().equals("null")){
                            /*isMyTurn = false;
                            player.playerGiveMove = true;
                            System.out.print("\n[ "+message+" ]\n");*/
                            chessboard.changeclickfalse();
                        }

                        if(message!=null){
                            isMyTurn = false;
                            player.playerGiveMove = true;
                            System.out.print("\n[ "+message+" ]\n");
                        }

/**_____________________________________________up_to_here________________________________________________________*/

                    }//only for if(isMyTurn)
                    else {
                            int it = 3-myType;
                            if(it==1){
                                System.out.print("WAIT! It's White's turn"+'\n');
                            }
                            else{
                                System.out.print("WAIT! It's Black's turn\n");
                            }
                        }

                }catch (Exception e){
                    System.out.print(e);
                }

            }
        });

        //finally call it separately
        runClient();//in HuntNow
    }



    public void runClient(){
        player =new Player() {//Anonymous object of [Player] class have to implement every methods of Player class
            @Override
            public void initialize() {
                System.out.print("in initialize method\n");
                //HuntNow.status.appendText("Initialising\nYOU're WHITE\n");
                isMyTurn = true;
                myType = WHITE_PLAYER;
            }

            @Override
            public <T> T WriteMove() {
                return (T) message;

            }

            @Override
            public <T> void readMove(T data) {
                System.out.println("Data received: "+(String)data);
                System.out.println("It's your turn");
                try {
                    //do something with your data
                    String[] coords = ((String) data).split(" ");
                    int si = Integer.parseInt(coords[0]);
                    int sj = Integer.parseInt(coords[1]);
                    int ti = Integer.parseInt(coords[2]);
                    int tj = Integer.parseInt(coords[3]);
                    System.out.print("selecting");
                    doTheSameThing(si,sj,ti,tj);//first click
                    System.out.print("targeting");
                    doTheSameThing(si,sj,ti,tj);//second click
                    } catch (Exception e) {
                        System.out.print("Errors in ReadMove");
                }
            }

            @Override//done
            public void setIp() {
                ipAdress="localhost";
            }

        };
    }


    //hey I've tested.. doTheSameThing works very good
    public void doTheSameThing(int si , int sj , int ti , int tj){
        try {

            isMyTurn  = false;
            // Prints current player..//clean
            if(chessboard.getClicklogic().equals("false") && !winner && !stale){
                if(chessboard.currentplayer()==WHITE_PLAYER){
                    System.out.print("Current player: WHITE\n");
                }
                else{System.out.print("Current player: BLACK\n");}
            }

            /////////////////////////////////////////////////second player starts
            // Second click
            if(chessboard.getClicklogic() == "true"){
                Piece[][] boardstate = chessboard.getState();//copied
                targetedPiece = chessboard.selectTarget(ti,tj);
/**________________________________________________start_copying_from_here_again______________________________________________________*/
                // If pawn selected ..
                if(selectedPiece.toString().equals("Pawn") && selectedPiece != null && targetedPiece != null
                        && !selectedPiece.equals(targetedPiece)){
                    // Only executes if legal move ..
                    if(chessboard.getStroke(targetedPiece.icoord(), targetedPiece.jcoord(), Color.CORNFLOWERBLUE)
                            || chessboard.getStroke(targetedPiece.icoord(), targetedPiece.jcoord(), Color.AQUAMARINE)){
                        // If check is false
                        if(!Logics.checkstatus()){
                            Piece[][] oldstate = new Piece[8][8];
                            // Transfer pieces to backup variable
                            for(int x=0;x < 8; x++){
                                for(int y=0; y < 8; y++){
                                    oldstate[x][y] = boardstate[x][y];
                                }
                            }
                            // Do move
                            boardstate = selectedPiece.movepawn(selectedPiece, targetedPiece, boardstate);
                            // If move results in no check, do move
                            if(!Logics.check4check(chessboard.otherplayer(), boardstate)){
                                chessboard.setBoard(boardstate);
                                chessboard.drawMove(selectedPiece.icoord(), selectedPiece.jcoord(), targetedPiece.icoord(), targetedPiece.jcoord());
                                message = selectedPiece.icoord()+" "+selectedPiece.jcoord()+" "+targetedPiece.icoord()+" "+targetedPiece.jcoord();
                                chessboard.changePlayer();
                                chessboard.changeclicknull();
                                // Successful move
                                staleCountWhite=8;
                                staleCountBlack=8;
                            }
                            // If check, reverse move
                            else{
                                //message = null;
                                chessboard.changeclicknull();
                                chessboard.setBoard(oldstate);
                                Logics.flipcheck();

                                // Stalemate check
                                if(selectedPiece.type()==1){
                                    staleCountWhite--;
                                }
                                if(selectedPiece.type()==2){
                                    staleCountBlack--;
                                }
                                if(staleCountWhite==0 || staleCountBlack==0){
                                    System.out.print("STALEMATE FOUND!!\n");
                                    stale=true;
                                }

                            }
                        }

                        // If in check ..
                        if(Logics.checkstatus()){
                            // Do move
                            Piece[][] oldstate = new Piece[8][8];
                            for(int x=0;x < 8; x++){
                                for(int y=0; y < 8; y++){
                                    oldstate[x][y] = boardstate[x][y];
                                }
                            }
                            boardstate = selectedPiece.movepawn(selectedPiece, targetedPiece, boardstate);
                            // Check if still in check, if not, do move
                            if(!Logics.check4check(chessboard.otherplayer(), boardstate)){
                                chessboard.setBoard(boardstate);
                                chessboard.drawMove(selectedPiece.icoord(), selectedPiece.jcoord(), targetedPiece.icoord(), targetedPiece.jcoord());
                                message = selectedPiece.icoord()+" "+selectedPiece.jcoord()+" "+targetedPiece.icoord()+" "+targetedPiece.jcoord();
                                chessboard.changePlayer();
                                chessboard.changeclicknull();
                                staleCountWhite=8;
                                staleCountBlack=8;
                                // If still in check, undo move
                            }
                            else{
                                // message = null;
                                chessboard.changeclicknull();
                                chessboard.setBoard(oldstate);
                                Logics.flipcheck();

                                // Stalemate check
                                if(selectedPiece.type()==1){
                                    staleCountWhite--;
                                }
                                if(selectedPiece.type()==2){
                                    staleCountBlack--;
                                }
                                if(staleCountWhite==0 || staleCountBlack==0){
                                    System.out.print("STALEMATE FOUND!!\n");
                                    stale=true;
                                }
                            }
                        }

                    }
                    else{

                        // Stalemate check
                        if(selectedPiece.type()==1){
                            staleCountWhite--;
                        }
                        if(selectedPiece.type()==2){
                            staleCountBlack--;
                        }
                        if(staleCountWhite==0 || staleCountBlack==0){
                            System.out.print("STALEMATE FOUND!!\n");
                            stale=true;
                        }
                    }
                }

                // If bishop selected ..
                if(selectedPiece.toString().equals("Bishop") && selectedPiece != null && targetedPiece != null && !selectedPiece.equals(targetedPiece)){
                    if(chessboard.getStroke(targetedPiece.icoord(), targetedPiece.jcoord(), Color.CORNFLOWERBLUE) || chessboard.getStroke(targetedPiece.icoord(), targetedPiece.jcoord(), Color.AQUAMARINE)){
                        // If check is false
                        if(!Logics.checkstatus()){
                            Piece[][] oldstate = new Piece[8][8];
                            // Transfer pieces to backup variable
                            for(int x=0;x < 8; x++){
                                for(int y=0; y < 8; y++){
                                    oldstate[x][y] = boardstate[x][y];
                                }
                            }
                            // Do move
                            boardstate = selectedPiece.movebishop(selectedPiece, targetedPiece, boardstate);
                            // If move results in no check, do move
                            if(!Logics.check4check(chessboard.otherplayer(), boardstate)){
                                chessboard.setBoard(boardstate);
                                chessboard.drawMove(selectedPiece.icoord(), selectedPiece.jcoord(), targetedPiece.icoord(), targetedPiece.jcoord());
                                message = selectedPiece.icoord()+" "+selectedPiece.jcoord()+" "+targetedPiece.icoord()+" "+targetedPiece.jcoord();
                                chessboard.changePlayer();
                                chessboard.changeclicknull();
                                staleCountWhite=8;
                                staleCountBlack=8;
                            }
                            // If check, reverse move
                            else{
                                //message = null;
                                chessboard.changeclicknull();
                                chessboard.setBoard(oldstate);
                                Logics.flipcheck();

                                // Stalemate check
                                if(selectedPiece.type()==1){
                                    staleCountWhite--;
                                }
                                if(selectedPiece.type()==2){
                                    staleCountBlack--;
                                }
                                if(staleCountWhite==0 || staleCountBlack==0){
                                    System.out.print("STALEMATE FOUND!!\n");
                                    stale=true;
                                }
                            }
                        }

                        // If in check ..
                        if(Logics.checkstatus()){
                            // Do move
                            Piece[][] oldstate = new Piece[8][8];
                            for(int x=0;x < 8; x++){
                                for(int y=0; y < 8; y++){
                                    oldstate[x][y] = boardstate[x][y];
                                }
                            }
                            boardstate = selectedPiece.movebishop(selectedPiece, targetedPiece, boardstate);
                            // Check if still in check, if not, do move
                            if(!Logics.check4check(chessboard.otherplayer(), boardstate)){
                                chessboard.setBoard(boardstate);
                                chessboard.drawMove(selectedPiece.icoord(), selectedPiece.jcoord(), targetedPiece.icoord(), targetedPiece.jcoord());
                                message = selectedPiece.icoord()+" "+selectedPiece.jcoord()+" "+targetedPiece.icoord()+" "+targetedPiece.jcoord();
                                chessboard.changePlayer();
                                chessboard.changeclicknull();
                                staleCountWhite=8;
                                staleCountBlack=8;
                                // If still in check, undo move
                            }
                            else{
                                //message = null;
                                chessboard.changeclicknull();
                                chessboard.setBoard(oldstate);
                                Logics.flipcheck();

                                // Stalemate check
                                if(selectedPiece.type()==1){
                                    staleCountWhite--;
                                }
                                if(selectedPiece.type()==2){
                                    staleCountBlack--;
                                }
                                if(staleCountWhite==0 || staleCountBlack==0){
                                    System.out.print("STALEMATE FOUND!!\n");
                                    stale=true;
                                }
                            }
                        }
                    }
                    else{

                        // Stalemate check
                        if(selectedPiece.type()==1){
                            staleCountWhite--;
                        }
                        if(selectedPiece.type()==2){
                            staleCountBlack--;
                        }
                        if(staleCountWhite==0 || staleCountBlack==0){
                            System.out.print("STALEMATE FOUND!!\n");
                            stale=true;
                        }
                    }
                }

                // If queen selected ..
                if(selectedPiece.toString().equals("Queen") && selectedPiece != null && targetedPiece != null && !selectedPiece.equals(targetedPiece)){
                    if(chessboard.getStroke(targetedPiece.icoord(), targetedPiece.jcoord(), Color.CORNFLOWERBLUE) || chessboard.getStroke(targetedPiece.icoord(), targetedPiece.jcoord(), Color.AQUAMARINE)){
                        // If check is false
                        if(!Logics.checkstatus()){
                            Piece[][] oldstate = new Piece[8][8];
                            // Transfer pieces to backup variable
                            for(int x=0;x < 8; x++){
                                for(int y=0; y < 8; y++){
                                    oldstate[x][y] = boardstate[x][y];
                                }
                            }
                            // Do move
                            boardstate = selectedPiece.movequeen(selectedPiece, targetedPiece, boardstate);
                            // If move results in no check, do move
                            if(!Logics.check4check(chessboard.otherplayer(), boardstate)){
                                chessboard.setBoard(boardstate);
                                chessboard.drawMove(selectedPiece.icoord(), selectedPiece.jcoord(), targetedPiece.icoord(), targetedPiece.jcoord());
                                message = selectedPiece.icoord()+" "+selectedPiece.jcoord()+" "+targetedPiece.icoord()+" "+targetedPiece.jcoord();
                                chessboard.changePlayer();
                                chessboard.changeclicknull();
                                staleCountWhite=8;
                                staleCountBlack=8;
                            }
                            // If check, reverse move
                            else{
                                //message = null;
                                chessboard.changeclicknull();
                                chessboard.setBoard(oldstate);
                                Logics.flipcheck();
                                if(selectedPiece.type()==1){
                                    staleCountWhite--;
                                }
                                if(selectedPiece.type()==2){
                                    staleCountBlack--;
                                }
                                if(staleCountWhite==0 || staleCountBlack==0){
                                    System.out.print("STALEMATE FOUND!!\n");
                                    stale=true;
                                }
                            }
                        }

                        // If in check ..
                        if(Logics.checkstatus()){
                            // Do move
                            Piece[][] oldstate = new Piece[8][8];
                            for(int x=0;x < 8; x++){
                                for(int y=0; y < 8; y++){
                                    oldstate[x][y] = boardstate[x][y];
                                }
                            }
                            boardstate = selectedPiece.movequeen(selectedPiece, targetedPiece, boardstate);
                            // Check if still in check, if not, do move
                            if(!Logics.check4check(chessboard.otherplayer(), boardstate)){
                                chessboard.setBoard(boardstate);
                                chessboard.drawMove(selectedPiece.icoord(), selectedPiece.jcoord(), targetedPiece.icoord(), targetedPiece.jcoord());
                                message = selectedPiece.icoord()+" "+selectedPiece.jcoord()+" "+targetedPiece.icoord()+" "+targetedPiece.jcoord();
                                chessboard.changePlayer();
                                chessboard.changeclicknull();
                                staleCountWhite=8;
                                staleCountBlack=8;
                                // If still in check, undo move
                            }
                            else{
                                //message = null;
                                chessboard.changeclicknull();
                                chessboard.setBoard(oldstate);
                                Logics.flipcheck();

                                // Stalemate check
                                if(selectedPiece.type()==1){
                                    staleCountWhite--;
                                }
                                if(selectedPiece.type()==2){
                                    staleCountBlack--;
                                }
                            }
                            if(staleCountWhite==0 || staleCountBlack==0){
                                System.out.print("STALEMATE FOUND!!\n");
                                stale=true;
                            }
                        }
                    }
                    else{

                        // Stalemate check
                        if(selectedPiece.type()==1){
                            staleCountWhite--;
                        }
                        if(selectedPiece.type()==2){
                            staleCountBlack--;
                        }
                        if(staleCountWhite==0 || staleCountBlack==0){
                            System.out.print("STALEMATE FOUND!!\n");
                            stale=true;
                        }
                    }
                }

                // If rook selected ..
                if(selectedPiece.toString().equals("Rook") && selectedPiece != null && targetedPiece != null && selectedPiece.equals(targetedPiece)==false){
                    if(chessboard.getStroke(targetedPiece.icoord(), targetedPiece.jcoord(), Color.CORNFLOWERBLUE) || chessboard.getStroke(targetedPiece.icoord(), targetedPiece.jcoord(), Color.AQUAMARINE)==true){
                        // If check is false
                        if(!Logics.checkstatus()){
                            Piece[][] oldstate = new Piece[8][8];
                            // Transfer pieces to backup variable
                            for(int x=0;x < 8; x++){
                                for(int y=0; y < 8; y++){
                                    oldstate[x][y] = boardstate[x][y];
                                }
                            }
                            // Do move
                            boardstate = selectedPiece.moverook(selectedPiece, targetedPiece, boardstate);
                            // If move results in no check, do move
                            if(!Logics.check4check(chessboard.otherplayer(), boardstate)){
                                chessboard.setBoard(boardstate);
                                chessboard.drawMove(selectedPiece.icoord(), selectedPiece.jcoord(), targetedPiece.icoord(), targetedPiece.jcoord());
                                message = selectedPiece.icoord()+" "+selectedPiece.jcoord()+" "+targetedPiece.icoord()+" "+targetedPiece.jcoord();
                                chessboard.changePlayer();
                                chessboard.changeclicknull();
                                staleCountWhite=8;
                                staleCountBlack=8;
                            }
                            // If check, reverse move
                            else{
                                //message = null;
                                chessboard.changeclicknull();
                                chessboard.setBoard(oldstate);
                                Logics.flipcheck();

                                // Stalemate check
                                if(selectedPiece.type()==1){
                                    staleCountWhite--;
                                }
                                if(selectedPiece.type()==2){
                                    staleCountBlack--;
                                }
                                if(staleCountWhite==0 || staleCountBlack==0){
                                    System.out.print("STALEMATE FOUND!!\n");
                                    stale=true;
                                }
                            }
                        }

                        // If in check ..
                        if(Logics.checkstatus()){
                            // Do move
                            Piece[][] oldstate = new Piece[8][8];
                            for(int x=0;x < 8; x++){
                                for(int y=0; y < 8; y++){
                                    oldstate[x][y] = boardstate[x][y];
                                }
                            }
                            boardstate = selectedPiece.moverook(selectedPiece, targetedPiece, boardstate);
                            // Check if still in check, if not, do move
                            if(!Logics.check4check(chessboard.otherplayer(), boardstate)){
                                chessboard.setBoard(boardstate);
                                chessboard.drawMove(selectedPiece.icoord(), selectedPiece.jcoord(), targetedPiece.icoord(), targetedPiece.jcoord());
                                message = selectedPiece.icoord()+" "+selectedPiece.jcoord()+" "+targetedPiece.icoord()+" "+targetedPiece.jcoord();
                                chessboard.changePlayer();
                                chessboard.changeclicknull();
                                staleCountWhite=8;
                                staleCountBlack=8;
                                // If still in check, undo move
                            }
                            else{
                                //message = null;
                                chessboard.changeclicknull();
                                chessboard.setBoard(oldstate);
                                Logics.flipcheck();

                                // Stalemate check
                                if(selectedPiece.type()==1){
                                    staleCountWhite--;
                                }
                                if(selectedPiece.type()==2){
                                    staleCountBlack--;
                                }
                                if(staleCountWhite==0 || staleCountBlack==0){
                                    System.out.print("STALEMATE FOUND!!\n");
                                    stale=true;
                                }
                            }
                        }
                    }
                    else{

                        // Stalemate check
                        if(selectedPiece.type()==1){
                            staleCountWhite--;
                        }
                        if(selectedPiece.type()==2){
                            staleCountBlack--;
                        }
                        if(staleCountWhite==0 || staleCountBlack==0){
                            System.out.print("STALEMATE FOUND!!\n");
                            stale=true;
                        }
                    }
                }

                // If king selected ..
                if(selectedPiece.toString().equals("King") && selectedPiece != null && targetedPiece != null && !selectedPiece.equals(targetedPiece)){
                    if(chessboard.getStroke(targetedPiece.icoord(), targetedPiece.jcoord(), Color.CORNFLOWERBLUE) || chessboard.getStroke(targetedPiece.icoord(), targetedPiece.jcoord(), Color.AQUAMARINE)){
                        // If check is false
                        if(!Logics.checkstatus()){
                            Piece[][] oldstate = new Piece[8][8];
                            // Transfer pieces to backup variable
                            for(int x=0;x < 8; x++){
                                for(int y=0; y < 8; y++){
                                    oldstate[x][y] = boardstate[x][y];
                                }
                            }
                            // Do move
                            boardstate = selectedPiece.moveking(selectedPiece, targetedPiece, boardstate);
                            // If move results in no check, do move
                            if(!Logics.check4check(chessboard.otherplayer(), boardstate)){
                                chessboard.setBoard(boardstate);
                                chessboard.drawMove(selectedPiece.icoord(), selectedPiece.jcoord(), targetedPiece.icoord(), targetedPiece.jcoord());
                                message = selectedPiece.icoord()+" "+selectedPiece.jcoord()+" "+targetedPiece.icoord()+" "+targetedPiece.jcoord();
                                chessboard.changePlayer();
                                chessboard.changeclicknull();
                                staleCountWhite=8;
                                staleCountBlack=8;
                            }
                            // If check, reverse move
                            else{
                                //message = null;
                                chessboard.changeclicknull();
                                chessboard.setBoard(oldstate);
                                Logics.flipcheck();

                                // Stalemate check
                                if(selectedPiece.type()==1){
                                    staleCountWhite--;
                                }
                                if(selectedPiece.type()==2){
                                    staleCountBlack--;
                                }
                                if(staleCountWhite==0 || staleCountBlack==0){
                                    System.out.print("STALEMATE FOUND!!\n");
                                    stale=true;
                                }
                            }
                        }

                        // If in check ..
                        if(Logics.checkstatus()){
                            // Do move
                            Piece[][] oldstate = new Piece[8][8];
                            for(int x=0;x < 8; x++){
                                for(int y=0; y < 8; y++){
                                    oldstate[x][y] = boardstate[x][y];
                                }
                            }
                            boardstate = selectedPiece.moveking(selectedPiece, targetedPiece, boardstate);
                            // Check if still in check, if not, do move
                            if(!Logics.check4check(chessboard.otherplayer(), boardstate)){
                                chessboard.setBoard(boardstate);
                                chessboard.drawMove(selectedPiece.icoord(), selectedPiece.jcoord(), targetedPiece.icoord(), targetedPiece.jcoord());
                                message = selectedPiece.icoord()+" "+selectedPiece.jcoord()+" "+targetedPiece.icoord()+" "+targetedPiece.jcoord();
                                chessboard.changePlayer();
                                chessboard.changeclicknull();
                                staleCountWhite=8;
                                staleCountBlack=8;
                                // If still in check, undo move
                            }
                            else{
                                //message = null;
                                chessboard.changeclicknull();
                                chessboard.setBoard(oldstate);
                                Logics.flipcheck();

                                // Stalemate check
                                if(selectedPiece.type()==1){
                                    staleCountWhite--;
                                }
                                if(selectedPiece.type()==2){
                                    staleCountBlack--;
                                }
                                if(staleCountWhite==0 || staleCountBlack==0){
                                    System.out.print("STALEMATE FOUND!!\n");
                                    stale=true;
                                }
                            }
                        }
                    }
                    else{

                        // Stalemate check
                        if(selectedPiece.type()==1){
                            staleCountWhite--;
                        }
                        if(selectedPiece.type()==2){
                            staleCountBlack--;
                        }
                        if(staleCountWhite==0 || staleCountBlack==0){
                            System.out.print("STALEMATE FOUND!!\n");
                            stale=true;
                        }
                    }
                }

                // If knight selected ..
                if(selectedPiece.toString().equals("Knight") && selectedPiece != null && targetedPiece != null && !selectedPiece.equals(targetedPiece)){
                    if(chessboard.getStroke(targetedPiece.icoord(), targetedPiece.jcoord(), Color.CORNFLOWERBLUE) || chessboard.getStroke(targetedPiece.icoord(), targetedPiece.jcoord(), Color.AQUAMARINE)){
                        // If check is false
                        if(!Logics.checkstatus()){
                            Piece[][] oldstate = new Piece[8][8];
                            // Transfer pieces to backup variable
                            for(int x=0;x < 8; x++){
                                for(int y=0; y < 8; y++){
                                    oldstate[x][y] = boardstate[x][y];
                                }
                            }
                            // Do move
                            boardstate = selectedPiece.moveknight(selectedPiece, targetedPiece, boardstate);
                            // If move results in no check, do move
                            if(!Logics.check4check(chessboard.otherplayer(), boardstate)){
                                chessboard.setBoard(boardstate);
                                chessboard.drawMove(selectedPiece.icoord(), selectedPiece.jcoord(), targetedPiece.icoord(), targetedPiece.jcoord());
                                message = selectedPiece.icoord()+" "+selectedPiece.jcoord()+" "+targetedPiece.icoord()+" "+targetedPiece.jcoord();
                                chessboard.changePlayer();
                                chessboard.changeclicknull();
                                staleCountWhite=8;
                                staleCountBlack=8;
                            }
                            // If check, reverse move
                            else{
                                //message = null;
                                chessboard.changeclicknull();
                                chessboard.setBoard(oldstate);
                                Logics.flipcheck();

                                // Stalemate check
                                if(selectedPiece.type()==1){
                                    staleCountWhite--;
                                }
                                if(selectedPiece.type()==2){
                                    staleCountBlack--;
                                }
                                if(staleCountWhite==0 || staleCountBlack==0){
                                    System.out.print("STALEMATE FOUND!!\n(rare yet)\n");
                                    stale=true;
                                }
                            }
                        }

                        // If in check ..
                        if(Logics.checkstatus()){
                            // Do move
                            Piece[][] oldstate = new Piece[8][8];
                            for(int x=0;x < 8; x++){
                                for(int y=0; y < 8; y++){
                                    oldstate[x][y] = boardstate[x][y];
                                }
                            }
                            boardstate = selectedPiece.moveknight(selectedPiece, targetedPiece, boardstate);
                            // Check if still in check, if not, do move
                            if(!Logics.check4check(chessboard.otherplayer(), boardstate)){
                                chessboard.setBoard(boardstate);
                                chessboard.drawMove(selectedPiece.icoord(), selectedPiece.jcoord(), targetedPiece.icoord(), targetedPiece.jcoord());
                                message = selectedPiece.icoord()+" "+selectedPiece.jcoord()+" "+targetedPiece.icoord()+" "+targetedPiece.jcoord();
                                chessboard.changePlayer();
                                chessboard.changeclicknull();
                                staleCountWhite=8;
                                staleCountBlack=8;
                                // If still in check, undo move
                            }
                            else{
                                //message = null;
                                chessboard.changeclicknull();
                                chessboard.setBoard(oldstate);
                                Logics.flipcheck();

                                // Stalemate check
                                if(selectedPiece.type()==1){
                                    staleCountWhite--;
                                }
                                if(selectedPiece.type()==2){
                                    staleCountBlack--;
                                }
                                if(staleCountWhite==0 || staleCountBlack==0){
                                    System.out.print("STALEMATE FOUND!!\n");
                                    stale=true;
                                }
                            }
                        }
                    }
                    else{

                        // Stalemate check
                        if(selectedPiece.type()==WHITE_PLAYER){
                            staleCountWhite--;
                        }
                        if(selectedPiece.type()==BLACK_PLAYER){
                            staleCountBlack--;
                        }
                        if(staleCountWhite==0 || staleCountBlack==0){
                            //shob khaia laicho naki??what the hell it is?
                            System.out.print("STALEMATE found!!");
                            stale=true;
                        }
                    }
                }

                // If screw up ..//liar liar pants on fire// do you know the rhyme?????
                else{
                    chessboard.changeclicknull();
                    chessboard.clearHighLights();
                }

                chessboard.clearHighLights();
                chessboard.changeclicknull();

                // Check for checkmate ..
                if(Logics.ischeckMateOccurs(chessboard.otherplayer(), chessboard.getState())=="true"){
                    winner=true;
                }

                getScene().setCursor(Cursor.DEFAULT);

                // highlight check..

                if(Logics.checkstatus()){
                    chessboard.checkHighLight(Logics.checkCoordI(), Logics.checkCoordJ());
                    if(!winner){
                        System.out.print("\nCHECK FOUND!!\n");}
                    chessboard.changeclicknull();
                }
            }
            ////////////////////////////////////////////////second player ends

            ///////////////////////////////////////////////first click starts
            // First click
            if(chessboard.getClicklogic().equals("false") && !stale && !winner){
                selectedPiece = chessboard.selectPiece(si,sj);

                if(selectedPiece.toString().equals("Empty") || !chessboard.pieceselect()){
                    isJunkSelected=true;
                }
                else{isJunkSelected=false;}

                if(!selectedPiece.equals("Empty") && !isJunkSelected){
                    getScene().setCursor(new ImageCursor(selectedPiece.image()));
                    chessboard.changeclicktrue();
                    // Highlights valid moves..
                    chessboard.validMoves(selectedPiece);}

                // Check 4 check .....for otherPlayer//he will be the current Player
                if(!Logics.checkstatus()){
                    Logics.check4check(chessboard.otherplayer(), chessboard.getState());}
            }
            ////////////////////////////////////////////////////first click ends

            if(chessboard.getClicklogic().equals("true")){
                System.out.print("No of WhitePieces: " + whitepieces(chessboard.getState())
                        + "\nNo of BlackPieces: " + blackpieces(chessboard.getState()));
                System.out.print("\n/*********************/\n");
            }


            // If completed move, return to first click ..
            if(chessboard.getClicklogic().equals("null")){
                isMyTurn = true;
                System.out.print(message);
                chessboard.changeclickfalse();
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // Piece counting -- could expand on this but only need total number
    public int whitepieces(Piece[][] boardstate){
        int whitepieces=0;

        // Count white pieces
        for(int x=0; x < 8; x++){
            for(int y=0; y < 8; y++){
                if(boardstate[x][y].type()==1){
                    whitepieces++;
                }
            }
        }
        // Return int
        return whitepieces;
    }

    public int blackpieces(Piece[][] boardstate){
        int blackpieces=0;

        // Count white pieces
        for(int x=0; x < 8; x++){
            for(int y=0; y < 8; y++){
                if(boardstate[x][y].type()==2){
                    blackpieces++;
                }
            }
        }
        // Return int
        return blackpieces;
    }

    public void blacktimeout(){
        winner=true;
        System.out.print("\nBlack timeout: White wins!");
    }

    public void whitetimeout(){
        winner=true;
        System.out.print("\nWhite timeout: Black wins!");
    }

    public void highlightcheck(int x, int y){
        chessboard.checkHighLight(x,y);
    }

    @Override
    public void resize(double width, double height){
        super.resize(width, height);
        chessboard.resize(width, height);
    }

    @Override
    public void relocate(double x, double y){
        super.relocate(x, y);
        pos.setX(x);
        pos.setY(x);
    }
}