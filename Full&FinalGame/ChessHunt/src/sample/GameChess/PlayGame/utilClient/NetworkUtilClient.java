package sample.GameChess.PlayGame.utilClient;


import sample.GameChess.PlayGame.clientS.Message;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class NetworkUtilClient implements Message {

    boolean avaiable=true;
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public NetworkUtilClient(String s, int port) {
        try {
            this.socket = new Socket(s, port);
            System.out.println("Server Connected in NetworkUtilClient.");
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e) {
            avaiable=false;
            System.out.println("Cannot Connect to StartServer:(& I'm not avaiable now.) ");
        }
    }

    public Object read() {
        try {
            while (true){
                System.out.println("Reading from Server in NetWorkUtilClient.read ");
                if(ois.available()>=0){
                  return  ois.readObject();
                }
                if(ois.available()==-1){
                    return "EXIT";
                }
            }
        } catch (Exception e) {
            System.out.print("Reading Error in NetworkUtilClient.read : ");
        }
        return null;
    }

    public <T> boolean write(T o) {
        try {
            System.out.print("In NetworkUtilClient.write+"+ o);
            oos.writeObject(o);
            return true;
        } catch (Exception e) {
            System.out.print("Writing  Error in NetworkUtilClient.write & closing connection ");
            closeConnection();
            return false;
        }
    }

    public void closeConnection() {
        try {
            ois.close();
            oos.close();
            otherPlayerDisconnected();
        } catch (Exception e) {

            System.out.print("Can't close!..Error in NetworkUtilClient.closeConnection");
        }
    }

    public boolean isAvaiable() {
        return this.avaiable;
    }
}

