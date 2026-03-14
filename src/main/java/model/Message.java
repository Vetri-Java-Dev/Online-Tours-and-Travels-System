package model;

public class Message {

    private int messageId;
    private String content;
    private int senderId;
    private int receiverId;

    public Message(int messageId, String content, int senderId, int receiverId) {
        this.messageId = messageId;
        this.content = content;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    public int getMessageId() {
        return messageId;
    }

    public String getContent() {
        return content;
    }

    public int getSenderId() {
        return senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }
}