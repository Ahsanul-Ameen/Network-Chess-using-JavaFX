package sample.GameChess.StartServer.modification;


import sample.GameChess.StartServer.utilServer.NetworkUtilServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server  {

    private int clientCount=0;
    private int port=56789;/////////////////////port number is here
    private ServerSocket serverSocket;




    public Server(){

        try {
            serverSocket = new ServerSocket(port);
            while (true){
                serveGame();
            }
        } catch (IOException e) {
            /**
            * show in GUI
            * Server not open
            */
            System.out.println("Exception in opening StartServer socket");
            e.printStackTrace();
        }

    }

    public void serveGame() {
        Socket firstPlayerSocket =  null;
        Socket secondPlayerSocket = null;

        NetworkUtilServer networkUtil1=null;
        NetworkUtilServer networkUtil2=null;

        while (true){
            try {
                if(firstPlayerSocket==null){

                    firstPlayerSocket = serverSocket.accept();
                    networkUtil1=new NetworkUtilServer(firstPlayerSocket);
                    System.out.println((clientCount+1)+" th player connected");
                    clientCount++;
                }
            } catch (Exception e) {
                System.out.println("Client not connected");
                //e.printStackTrace();
                continue;
            }

            try {
                if(networkUtil2==null){
                    System.out.println("Finding second player");
                    secondPlayerSocket = serverSocket.accept();
                    networkUtil2=new NetworkUtilServer(secondPlayerSocket);
                    System.out.println((clientCount+1)+" th player connected");
                    clientCount++;
                }
            }catch (Exception e){

                System.out.println("Client not connected");
                continue;
                //e.printStackTrace();
            }


            if(networkUtil1!=null){
                if(!networkUtil1.write("start+firstPlayer")){
                    System.out.println((clientCount-1)+" Disconnected");
                    clientCount--;
                    networkUtil1=networkUtil2;
                    networkUtil2=null;
                    firstPlayerSocket=secondPlayerSocket;
                    secondPlayerSocket=null;
                    continue;
                }
            }

            if(networkUtil2!=null){
                if(!networkUtil2.write("start+secondPlayer")){
                    System.out.println((clientCount-1)+" Disconnected");
                    clientCount--;
                    networkUtil2=null;
                    secondPlayerSocket=null;
                    continue;
                }
            }

            System.out.println("Start Game");

            new NewGame(networkUtil1,networkUtil2);
            networkUtil1=null;
            networkUtil2=null;
            firstPlayerSocket =  null;
            secondPlayerSocket = null;
        }
    }

    public int getClientCount() {
        return clientCount;
    }

    public void setClientCount(int clientCount) {
        this.clientCount = clientCount;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public String toString() {
        return "Server{" +
                "clientCount=" + clientCount +
                ", port=" + port +
                ", serverSocket=" + serverSocket +
                '}';
    }
}

