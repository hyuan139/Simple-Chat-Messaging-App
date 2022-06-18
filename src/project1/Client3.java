package project1;

import com.google.gson.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

/**
 * Interact with server on local device
 */
public class Client3 {

    // Main application of the client
    public static void main(String[] args) throws IOException {
        // client side don't specify port number
        // OS determines the port number instead
        DatagramSocket dgSocket = new DatagramSocket();
        // to be able to receive messages from server
        DatagramPacket buffer = new DatagramPacket(new byte[1000], 1000);
        Gson gson = new Gson();

        System.out.println("Starting chat messaging. Enter \"q\" to quit.");
        Scanner scan = new Scanner(System.in);
        System.out.print("Username: ");
        String user = scan.nextLine();
        // initially create a message with type join, user input for username, and empty string for content
        Message messageInfo = new Message(0, user, "");
        String messageJson = gson.toJson(messageInfo);
        byte[] data = messageJson.getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 8989);
        dgSocket.send(packet);

        // Create a thread to listen for messages coming from the server
        Thread thread = new Thread(new ClientRunnable(dgSocket));
        thread.start();

        String message = "";
        // message type 1 for subsequent messages
        // get user input for message content
        while(true){
            message = scan.nextLine();
            // exit loop condition when "q" is entered
            if(message.equalsIgnoreCase("q")){
                break;
            }
            messageInfo.setType(1);
            messageInfo.setContent(message);
            messageJson = gson.toJson(messageInfo);
            data = messageJson.getBytes();
            // prepare the packet to send to server
            packet = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 8989);
            dgSocket.send(packet);
        }
        // message type 2 for leave
        messageInfo.setType(2);
        messageInfo.setContent("");
        messageJson = gson.toJson(messageInfo);
        data = messageJson.getBytes();
        packet = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 8989);
        dgSocket.send(packet);

        thread.stop();
        dgSocket.close();
    }
}