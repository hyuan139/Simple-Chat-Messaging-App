package project1;

public class Message {
    private int type;
    private String username;
    private String content;

    /**
     * Create a message with type, username, and message content
     * @param type the type of the message (join, post, leave)
     * @param username the username of the client
     * @param content the message content
     */
    public Message(int type, String username, String content) {
        this.type = type;
        this.username = username;
        this.content = content;
    }

    /**
     * Set the type of the message
     * @param type the type of the message (join, post, leave)
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * Set the message content of the message
     * @param message the content of the message
     */
    public void setContent(String message) {
        this.content = message;
    }

    /**
     * Get the type of the message (join, post, leave)
     * @return the type of the message
     */
    public int getType() {
        return this.type;
    }

    /**
     * Get the username of the client
     * @return the username
     */
    public String getUser() {
        return this.username;
    }

    /**
     * Get the message content of the message
     * @return the message content
     */
    public String getMessage() {
        return this.content;
    }

}
