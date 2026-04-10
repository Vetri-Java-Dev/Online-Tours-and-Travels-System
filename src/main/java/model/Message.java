/*
 * Author         : Harini R G
 * Description    : Message is a model class that represents communication details 
 *                  between users such as sender ID, receiver ID, message content, 
 *                  and sender role. It is used to transfer message data between 
 *                  different layers of the application.
 * Module         : Message Module (Model Layer)
 * Java version   : 25
 */
package model;
public class Message {

    private int senderId;
    private int receiverId;
    private String content;
    private String senderRole;

    public Message(int senderId, int receiverId, String content, String senderRole) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.senderRole = senderRole;
    }

    public int getSenderId() {
        return senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public String getContent() {
        return content;
    }

    public String getSenderRole() {
        return senderRole;
    }
}