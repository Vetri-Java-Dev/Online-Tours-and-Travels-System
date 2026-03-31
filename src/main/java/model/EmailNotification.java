package model;

public class EmailNotification {

    private int notificationId;
    private String email;
    private String subject;
    private String message;

    public EmailNotification(int notificationId, String email, String subject, String message) {
        this.notificationId = notificationId;
        this.email = email;
        this.subject = subject;
        this.message = message;
    }
    
    public int getNotificationId() {
        return notificationId;
    }

    public String getEmail() {
        return email;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }

    public void sendEmail() {
        System.out.println("Sending Email to: " + email);
        System.out.println("Subject: " + subject);
        System.out.println("Message: " + message);
        System.out.println("Email sent successfully.");
    }
}