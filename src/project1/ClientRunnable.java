package project1;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Create a Runnable object with DatagramSocket as a parameter for a Thread to run on the Client
 */
public class ClientRunnable implements Runnable {
    private DatagramSocket socket;
    private DatagramPacket buffer;

    /**
     * Create a Runnable object with a DatagramSocket object as its argument
     * @param socket the socket to be passed
     */
    public ClientRunnable(DatagramSocket socket){
        this.socket = socket;
        this.buffer = new DatagramPacket(new byte[1000], 1000);
    }

    /**
     * Reset the byte[]
     * @param data the byte[] to reset
     */
    public static void resetData(byte[] data){
        for(int i = 0; i < data.length; i++){
            if(data[i] == 0){
                break;
            }
            data[i] = 0;
        }
    }

    /**
     * Broadcast messages from the server to the client
     */
    @Override
    public void run() {
        Gson gson = new Gson();
        String message = "";
        String messageProcessed = "";
        while (true){
            try {
                this.socket.receive(this.buffer);
                message = new String(this.buffer.getData()).trim();
                messageProcessed = Server.jsonProcess(message);
                Message messageInfo = gson.fromJson(messageProcessed, Message.class);
                System.out.println(messageInfo.getUser() + ": " + messageInfo.getMessage());
                resetData(this.buffer.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
