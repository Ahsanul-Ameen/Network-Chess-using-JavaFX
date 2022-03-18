package sample.GameChess.PlayGame.clientS;


import sample.GameChess.PlayGame.HuntNow;

public interface Message{

    default void serverNotConnected() {
        /**
         *   Override your own code here
         *   Here in GUI show that server not connected
         */
        HuntNow.serverError();//showed
    }

    default void otherPlayerDisconnected(){
        /**
         * Override your code here
         * Here show Player disconnected/game disconnected in GUI
         */
        HuntNow.clientError();
    }


}
