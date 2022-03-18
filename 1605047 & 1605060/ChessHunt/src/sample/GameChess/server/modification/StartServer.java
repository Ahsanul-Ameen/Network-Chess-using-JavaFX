package sample.GameChess.server.modification;

public class StartServer {

    //life is LAMBDA.....!!!
    public StartServer(){
       new Thread(()->{
                 new Server();
       }).start();
    }


}
