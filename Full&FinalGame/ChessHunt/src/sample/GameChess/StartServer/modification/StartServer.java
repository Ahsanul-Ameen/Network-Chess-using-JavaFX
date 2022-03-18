package sample.GameChess.StartServer.modification;

public class StartServer {

    //life is LAMBDA.....!!!
    public StartServer(){
       new Thread(()->{
                 new Server();
       }).start();
    }


}
