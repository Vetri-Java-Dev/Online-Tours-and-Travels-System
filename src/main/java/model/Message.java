package model;

public class Message {

    private int senderId;
    private int receiverId;
    private String content;
    private String senderRole;

    // ✅ REQUIRED CONSTRUCTOR
    public Message(int senderId, int receiverId, String content, String senderRole) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.senderRole = senderRole;
    }

    // ✅ GETTERS
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