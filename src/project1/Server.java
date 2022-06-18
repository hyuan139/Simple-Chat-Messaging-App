package project1;

import com.google.gson.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;

/**
 * Interact with clients on local device
 */
public class Server {

    /**
     * Get the message type sent from the packet from a client
     * @param buffer
     * @return the message type
     */
    public static int getMessageType(DatagramPacket buffer){
        Gson gson = new Gson();
        String messageObj = new String(buffer.getData()).trim();
        String messageObjProcessed = jsonProcess(messageObj);
        Message messageInfo = gson.fromJson(messageObjProcessed, Message.class);
        if(messageInfo.getType() == 0){
            // join
            return 0;
        }
        else if(messageInfo.getType() == 1){
            // post
            return 1;
        }
        // leave
        return 2;
    }

    /**
     * Ensures the JSON as a string to be in the correct format
     * @param s
     * @return the processed string
     */
    public static String jsonProcess(String s){
        return s.substring(0, s.indexOf("}") + 1);
    }

    // Main application of the server
    public static void main(String[] args) throws IOException {
        // UDP Socket
        DatagramSocket dgSocket = new DatagramSocket(8989);
        // Buffer to receive packet
        DatagramPacket buffer = new DatagramPacket(new byte[1000], 1000);
        // <port, client info>
        HashMap<Integer, ClientInfo> clients = new HashMap<>();
        String message = "";
        String messageProcessed = "";
        String messageOut = "";

        Gson gson = new Gson();

        while(true){
            // receive function, a packet will be stored in the buffer, let the socket know about the buffer; makes it ready for incoming messages
            dgSocket.receive(buffer);
            int type = getMessageType(buffer);
            // join
            if(type == 0){
                ClientInfo client = new ClientInfo(buffer.getAddress(), buffer.getPort());
                // display saved entry for confirmation
                System.out.println("Join: " + gson.toJson(client).toString());
                // add client to data structure
                clients.put(buffer.getPort(), client);
            }
            // message type post
            else if(type == 1){
                message = new String(buffer.getData()).trim();
                messageProcessed = jsonProcess(message);
                Message messageInfo = gson.fromJson(messageProcessed, Message.class);
                messageOut = gson.toJson(messageInfo);
                byte[] data = messageOut.getBytes();
                // send message to all clients excluding the original sender
                for(Integer key: clients.keySet()){
                    if(key != buffer.getPort()){
                        DatagramPacket packet = new DatagramPacket(data, data.length, clients.get(key).getIP(), key);
                        dgSocket.send(packet);
                    }
                }
                System.out.println("Post: " + gson.toJson(clients.get(buffer.getPort())).toString() + "; Content: " + messageInfo.getMessage());
            }
            // leave
            else{
                // display removed entry for confirmation
                System.out.println("Leave: " + gson.toJson(clients.get(buffer.getPort())).toString());
                // then remove from data structure
                clients.remove(buffer.getPort());
            }
            // exit loop condition
            if(clients.size() == 0){
                break;
            }
        }

        // close socket
        dgSocket.close();
    }
}
