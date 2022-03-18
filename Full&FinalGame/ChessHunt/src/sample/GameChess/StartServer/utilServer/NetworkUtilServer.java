package sample.GameChess.StartServer.utilServer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class NetworkUtilServer {
    boolean avaiable=true;
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public NetworkUtilServer(Socket s) {
        try {
            this.socket = s;
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e) {
            System.out.println("In NetworkUtilServer : ");
        }
    }

    public Object read() {
        try {
            while (true){
                if(ois.available()>=0){
                    Object o=ois.readObject();
                    //System.out.println( o);
                  return  o;
                }
                if(ois.available()==-1){
                    return "EXIT";
                }
            }
        } catch (Exception e) {
            System.out.println("Reading Error in networkUtil : ");
        }
        return null;
    }

    public <T> boolean write(T o) {
        try {
            //System.out.println( o);

            oos.writeObject(o);
            return true;
        } catch (Exception e) {
            System.out.println("Writing  Error in network : ");
            closeConnection();
            return false;
        }
    }

    public void closeConnection() {
        try {
            ois.close();
            oos.close();
            /**
             * write your code here
             * Here show Player disconnected/game disconnected in GUI
             */

        } catch (Exception e) {
            System.out.println("Closing Error in network : ");
        }
    }

    public boolean isAvaiable() {
        return avaiable;
    }
}

