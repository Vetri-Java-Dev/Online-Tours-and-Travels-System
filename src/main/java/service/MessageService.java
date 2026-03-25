package service;

import dao.MessageDAO;
import model.Message;

import java.util.List;

public class MessageService {

    MessageDAO dao = new MessageDAO();

    public void sendToAdmin(int customerId, String msg) {
        Message m = new Message(customerId, 1, msg, "CUSTOMER");
        dao.sendMessage(m);
    }

    public void replyToCustomer(int customerId, String msg) {
        Message m = new Message(1, customerId, msg, "ADMIN");
        dao.sendMessage(m);
    }

    public List<String> viewMessages() {
        return dao.getUnreadMessages();
    }

    public List<String> viewReplies(int customerId) {
        return dao.getReplies(customerId);
    }
}