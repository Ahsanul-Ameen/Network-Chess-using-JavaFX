package sample.GameChess.PlayGame.Controllers;

/**
 *
 * This is the sample controller class which handles the whole non Networking issues
 * @author  Ahsanul Ameen Sabit
 */
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.control.Control;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;



import sample.GameChess.PlayGame.PieceClasses.*;

public class OfflineControl extends Control {
    public static final int WHITE_PLAYER = 1, BLACK_PLAYER = 2, EMPTY_PLAYER = 0;

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


    public int getHash() {
        return this.hash;
    }
    /**______________________(ADDED _____ EXTERNALLY)_____________________________*/



    public OfflineControl(){

        pos = new Translate();
        setSkin(new OfflineControlSkin(this));
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

        setOnMouseClicked(event -> {

                    hash = event.getTarget().hashCode();//clean totally
                    ImageView [][]selectView = chessboard.getImageviews();
                    Rectangle[][] targetSelect = chessboard.getBoard();


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
                            System.out.println("Current player: WHITE\n");
                        }
                        else{System.out.println("Current player: BLACK\n");}
                    }


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
                                        //message = selectedPiece.icoord()+" "+selectedPiece.jcoord()+" "+targetedPiece.icoord()+" "+targetedPiece.jcoord();
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
                                        //message = selectedPiece.icoord()+" "+selectedPiece.jcoord()+" "+targetedPiece.icoord()+" "+targetedPiece.jcoord();
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
                                        //message = selectedPiece.icoord()+" "+selectedPiece.jcoord()+" "+targetedPiece.icoord()+" "+targetedPiece.jcoord();
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
                                        //message = selectedPiece.icoord()+" "+selectedPiece.jcoord()+" "+targetedPiece.icoord()+" "+targetedPiece.jcoord();
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
                                        //message = selectedPiece.icoord()+" "+selectedPiece.jcoord()+" "+targetedPiece.icoord()+" "+targetedPiece.jcoord();
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
                                        //message = selectedPiece.icoord()+" "+selectedPiece.jcoord()+" "+targetedPiece.icoord()+" "+targetedPiece.jcoord();
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
                                        //message = selectedPiece.icoord()+" "+selectedPiece.jcoord()+" "+targetedPiece.icoord()+" "+targetedPiece.jcoord();
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
                                        //message = selectedPiece.icoord()+" "+selectedPiece.jcoord()+" "+targetedPiece.icoord()+" "+targetedPiece.jcoord();
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
                                        //message = selectedPiece.icoord()+" "+selectedPiece.jcoord()+" "+targetedPiece.icoord()+" "+targetedPiece.jcoord();
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
                                        //message = selectedPiece.icoord()+" "+selectedPiece.jcoord()+" "+targetedPiece.icoord()+" "+targetedPiece.jcoord();
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
                                        //message = selectedPiece.icoord()+" "+selectedPiece.jcoord()+" "+targetedPiece.icoord()+" "+targetedPiece.jcoord();
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
                                       //message = selectedPiece.icoord()+" "+selectedPiece.jcoord()+" "+targetedPiece.icoord()+" "+targetedPiece.jcoord();
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
                                            System.out.print("\nSTALEMATE FOUND!!\n");
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

                    if(chessboard.getClicklogic().equals("true")){
                        System.out.print("\nNo of WhitePieces: " + whitepieces(chessboard.getState())
                                + "\nNo of BlackPieces: " + blackpieces(chessboard.getState()));
                        System.out.print("\n/*********************/\n");
                    }


                    // If completed move, return to first click ..
                    if(chessboard.getClicklogic().equals("null")){
                        chessboard.changeclickfalse();
                    }
        });

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
