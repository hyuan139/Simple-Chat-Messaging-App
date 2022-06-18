package project1;

import java.net.InetAddress;

public class ClientInfo {
    private InetAddress IP;
    private int port;

    /**
     * Create a client
     * @param IP the IP of the client
     * @param port the port number of the client
     */
    public ClientInfo(InetAddress IP, int port) {
        this.IP = IP;
        this.port = port;

    }

    /**
     * Get the IP
     * @return the IP address
     */
    public InetAddress getIP() {
        return this.IP;
    }

    /**
     * Get the port number
     * @return the port number
     */
    public int getPortNum() {
        return this.port;
    }

}
