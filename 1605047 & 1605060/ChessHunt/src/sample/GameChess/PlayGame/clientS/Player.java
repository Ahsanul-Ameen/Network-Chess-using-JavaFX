package sample.GameChess.PlayGame.clientS;

import sample.GameChess.PlayGame.utilClient.NetworkUtilClient;

public abstract class Player implements Message{

    private boolean networkConnection = true;
    public boolean playerGiveMove = false;
    private NetworkUtilClient networkUtil = null;
    private boolean myMove = false;
    private Object receiveData;
    protected String ipAdress="localhost";
    private int port=56789;



    //The constructor of Player Class....
    public Player(){
        connectToServer();
    }//done properly


    public void connectToServer(){

        Thread thread = new Thread(() -> {
            setIp();

            networkUtil = new NetworkUtilClient(ipAdress, port);

            if (networkUtil.isAvaiable()) //actually excption na khaile false hoi na
            {
                String str = (String) networkUtil.read();
                if (str != null) {
                    String string[] = str.split("\\+");
                    if (string[0].equalsIgnoreCase("start")) {
                        if (string[1].equalsIgnoreCase("firstPlayer")) {
                            initialize();
                            myMove = true;
                            playerGiveMove = false;
                        } else {
                            myMove = false;
                        }
                    }
                    startGame();
                }
            } else {
                this.serverNotConnected();
            }
        });
        thread.start();
    }//done properly

    public void startGame(){
        while (networkConnection){
            if(myMove){
                System.out.println("//Please give a move//");
                networkUtil.write(WriteMoveData());
                myMove=false;
            }
            else {
                System.out.println("Receive a move data");
                receiveData= networkUtil.read();
                if(receiveData==null ){
                    return;
                }
                else{
                    try{
                        if(((String) receiveData).equals("EXIT")){
                            return;
                        }
                    }catch (Exception e){ }
                }
                readMove(receiveData);
                myMove=true;
            }
        }
    }//done properly


    public <T> T WriteMoveData(){//already implemented
        while(networkConnection){
            if(playerGiveMove){
                //System.out.println("Giving move(in WriteMoveData)");
                playerGiveMove=false;
                return WriteMove();
            }
            else{
                System.out.print("");
                //wait();
            }
        }
        return null;
    }

    public void closeConnection(){
        networkConnection = false;
        networkUtil.closeConnection();
    }//done properly

    public  abstract void initialize();//overridden properly

    /**while(true){
     *         if(playerGiveMove){  //playerGiveMove is a flag for determining whether player give move or not
     *             return "Move";  //this will be your move object.
     *         }
     *     }
     * a sample declaration of this move method is
     * public Object WriteMove(){  //you can also use generics
     *
     * }
     */


    public abstract <T>  T WriteMove();

    public abstract <T> void readMove(T data);

    public abstract void setIp();//done in application

}

