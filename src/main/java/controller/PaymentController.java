package controller;

import model.Payment;
import service.PaymentService;

public class PaymentController {

    private PaymentService paymentService = new PaymentService();

    public void processPayment(Payment payment) {

        System.out.println("\n========================================");
        System.out.println("             PAYMENT PROCESS            ");
        System.out.println("========================================");

        paymentService.processPayment(payment);
    }

    public void viewPayment(int paymentId) {

        System.out.println("\n========================================");
        System.out.println("              PAYMENT DETAILS           ");
        System.out.println("========================================");

        Payment payment = paymentService.viewPayment(paymentId);

        if (payment != null) {

            System.out.println("Payment ID     : " + payment.getPaymentId());
            System.out.println("Amount         : " + payment.getAmount());
            System.out.println("Payment Date   : " + payment.getPaymentDate());
            System.out.println("Payment Method : " + payment.getPaymentMethod());
            System.out.println("Status         : " + payment.getStatus());
            System.out.println("========================================");

        } else {
            System.out.println("No payment found for the given ID.");
        }
    }

    public void viewPaymentHistory(int bookingId) {

        System.out.println("\n========================================");
        System.out.println("           PAYMENT HISTORY              ");
        System.out.println("========================================");

        paymentService.viewPaymentHistory(bookingId);
    }
}