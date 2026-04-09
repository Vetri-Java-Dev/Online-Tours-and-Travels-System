package controller;

import java.util.List;
import java.util.Scanner;

import exception.*;
import model.User;
import service.MessageService;
import service.UserService;
import util.ColorText;

public class MessageController {

    private Scanner sc = new Scanner(System.in);
    private MessageService messageService = new MessageService();
    private UserService userService = new UserService();

    // ================= ADMIN: MESSAGE MENU =================
    public void adminMessageMenu() {
        while(true) {

            System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
            System.out.println(ColorText.warning("║") + ColorText.bold("          MESSAGE CUSTOMER            ") + ColorText.warning("║"));
            System.out.println(ColorText.warning("╠══════════════════════════════════════╣"));
            System.out.println(ColorText.warning("║") + "  1. View Messages                    " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  2. Reply to Customer                " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  3. Back                             " + ColorText.warning("║"));
            System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));

            System.out.print(ColorText.bold("  Enter choice: "));
            int choice = Integer.parseInt(sc.nextLine());

            switch(choice) {
                case 1:
                    List<String> messages = messageService.viewMessages();
                    System.out.println(ColorText.warning("\n  ╔═════════════════════════════════════════════════════╗"));
                    System.out.println(ColorText.warning("  ║") + ColorText.bold("              INBOX — CUSTOMER MESSAGES              ") + ColorText.warning("║"));
                    System.out.println(ColorText.warning("  ╠═════════════════════════════════════════════════════╣"));
                    
                    if (messages == null || messages.isEmpty()) {
                        System.out.println(ColorText.warning("  ║") + ColorText.yellow("  No unread messages.                                ") + ColorText.warning("║"));
                    }
                    else {
                        int idx = 1;
                        
                        for (String msg : messages) {
                            String[] parts = msg.split(" : ", 2);
                            String sender  = parts.length > 0 ? parts[0].trim() : "Unknown";
                            String content = parts.length > 1 ? parts[1].trim() : msg;
                            System.out.printf(ColorText.warning("  ║") + " " + ColorText.cyan(String.format("[%2d]", idx)) + " " + ColorText.bold("From :") + " %-39s" + ColorText.warning("║") + "%n", truncate(sender, 39));
                            
                            while (content.length() > 51) {
                                System.out.printf(ColorText.warning("  ║") + "        %-51s" + ColorText.warning("║") + "%n", content.substring(0, 51));
                                content = content.substring(51);
                            }
                            
                            System.out.printf(ColorText.warning("  ║") + "        %-51s" + ColorText.warning("║") + "%n", content);
                            if (idx < messages.size())
                                System.out.println(ColorText.warning("  ╠═════════════════════════════════════════════════════╣"));
                            idx++;
                        }
                    }
                    System.out.println(ColorText.warning("  ╚═════════════════════════════════════════════════════╝"));
                    break;

                case 2:
                    try {
                        List<User> users = userService.getAllUsers();
                        
                        System.out.println(ColorText.warning("\n  ╔═════════════════════════════════════════════════════╗"));
                        System.out.println(ColorText.warning("  ║") + ColorText.bold("              REGISTERED CUSTOMERS                ") + ColorText.warning("║"));
                        System.out.println(ColorText.warning("  ╠════════╦════════════════════════════════════════════╣"));
                        System.out.println(ColorText.warning("  ║") + ColorText.cyan("  ID    ") + ColorText.warning("║") + ColorText.cyan("  Name                                     ") + ColorText.warning("║"));
                        System.out.println(ColorText.warning("  ╠════════╬════════════════════════════════════════════╣"));
                        
                        for (User u : users) {
                            System.out.printf(ColorText.warning("  ║") + "  %-6d" + ColorText.warning("║") + "  %-40s" + ColorText.warning("║") + "%n",
                                    u.getUserId(), truncate(u.getName(), 40));
                        }
                        System.out.println(ColorText.warning("  ╚════════╩════════════════════════════════════════════╝"));

                        System.out.print(ColorText.bold("\n  Enter Customer ID : "));
                        int cid = Integer.parseInt(sc.nextLine());
                        
                        System.out.print(ColorText.bold("  Message           : "));
                        messageService.replyToCustomer(cid, sc.nextLine());
                        
                        System.out.println(ColorText.warning("\n  ╔══════════════════════════════════════╗"));
                        System.out.println(ColorText.warning("  ║") + ColorText.success("  ✔  Reply sent successfully!          ") + ColorText.warning("║"));
                        System.out.println(ColorText.warning("  ╚══════════════════════════════════════╝"));
                    }
                    catch (UserNotFoundException e) {
                        System.out.println(ColorText.error("  " + e.getMessage()));
                    }
                    break;

                case 3: return;
                default: System.out.println(ColorText.error("  Invalid choice."));
            }
        }
    }

    private String truncate(String s, int len) {
        if (s == null) return "";
        return s.length() > len ? s.substring(0, len - 1) + "…" : s;
    }

    // ================= CUSTOMER: MESSAGE MENU =================
    public void customerMessageMenu(int customerId) {
        while (true) {
            System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
            System.out.println(ColorText.warning("║") + ColorText.bold("           MESSAGE ADMIN              ") + ColorText.warning("║"));
            System.out.println(ColorText.warning("╠══════════════════════════════════════╣"));
            System.out.println(ColorText.warning("║") + "  1.  Send Message                    " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  2.  View Replies                    " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  3.  Back                            " + ColorText.warning("║"));
            System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));
            System.out.print(ColorText.bold("  Enter choice: "));
            int ch = Integer.parseInt(sc.nextLine());

            switch (ch) {
                case 1:
                    System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
                    System.out.println(ColorText.warning("║") + ColorText.bold("       SEND MESSAGE TO ADMIN          ") + ColorText.warning("║"));
                    System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));
                    System.out.print(ColorText.bold("  Your Message : "));
                    try {
                        messageService.sendToAdmin(customerId, sc.nextLine());
                        System.out.println(ColorText.success(" Message sent to Admin!"));
                    } catch (UserNotFoundException e) {
                        System.out.println(ColorText.error("  " + e.getMessage()));
                    }
                    break;

                case 2:
                    try {
                        List<String> replies = messageService.viewReplies(customerId);
                        System.out.println(ColorText.warning("\n╔══════════════════════════════════════════════════╗"));
                        System.out.println(ColorText.warning("║") + ColorText.bold("             REPLIES FROM ADMIN                   ") + ColorText.warning("║"));
                        System.out.println(ColorText.warning("╠══════════════════════════════════════════════════╣"));
                        if (replies == null || replies.isEmpty()) {
                            System.out.println(ColorText.warning("║") + ColorText.yellow("  No replies from admin yet.                      ") + ColorText.warning("║"));
                        }
                        else {
                            int idx = 1;
                            for (String r : replies) {
                                System.out.printf(ColorText.warning("║") + " " + ColorText.cyan(String.format("[%2d]", idx)) + " %-44s" + ColorText.warning("║") + "%n",
                                        r.length() > 44 ? r.substring(0, 44) : r);
                                String rest = r.length() > 44 ? r.substring(44) : "";
                                while (!rest.isEmpty()) {
                                    System.out.printf(ColorText.warning("║") + "      %-44s" + ColorText.warning("║") + "%n",
                                            rest.length() > 44 ? rest.substring(0, 44) : rest);
                                    rest = rest.length() > 44 ? rest.substring(44) : "";
                                }
                                if (idx < replies.size())
                                    System.out.println(ColorText.warning("╠══════════════════════════════════════════════════╣"));
                                idx++;
                            }
                        }
                        System.out.println(ColorText.warning("╚══════════════════════════════════════════════════╝"));
                    } catch (UserNotFoundException e) {
                        System.out.println(ColorText.error("  " + e.getMessage()));
                    }
                    break;

                case 3: return;
                default: System.out.println(ColorText.error("  Invalid choice."));
            }
        }
    }
}
