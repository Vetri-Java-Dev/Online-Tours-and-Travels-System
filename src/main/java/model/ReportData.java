/*
 * Author         : Subhashree R
 * Created Date   : 26-March-2026
 * Description    : Data model consolidating related reporting entities (bookings, payments, availability) for analytical display.
 * Module         : Admin Report Module
 * Java version   : 24
 */
package model;

/**
 * Holds data transfer objects for the three report types.
 */
public class ReportData {

   
    public static class BookingReportRow {
        private int    bookingId;
        private String bookingDate;
        private int    customerId;
        private String customerName;
        private int    packageId;
        private String destination;
        private int    travelers;
        private double totalAmount;
        private String status;

        public BookingReportRow(int bookingId, String bookingDate,
                                int customerId,  String customerName,
                                int packageId,   String destination,
                                int travelers,   double totalAmount,
                                String status) {
            this.bookingId    = bookingId;
            this.bookingDate  = bookingDate;
            this.customerId   = customerId;
            this.customerName = customerName;
            this.packageId    = packageId;
            this.destination  = destination;
            this.travelers    = travelers;
            this.totalAmount  = totalAmount;
            this.status       = status;
        }

        public int    getBookingId()    { return bookingId; }
        public String getBookingDate()  { return bookingDate; }
        public int    getCustomerId()   { return customerId; }
        public String getCustomerName() { return customerName; }
        public int    getPackageId()    { return packageId; }
        public String getDestination()  { return destination; }
        public int    getTravelers()    { return travelers; }
        public double getTotalAmount()  { return totalAmount; }
        public String getStatus()       { return status; }
    }

    
    public static class PaymentReportRow {
        private int    paymentId;
        private int    bookingId;
        private String paymentDate;
        private double amount;
        private String paymentMethod;
        private String paymentStatus;

        public PaymentReportRow(int paymentId, int bookingId, String paymentDate,
                                double amount, String paymentMethod, String paymentStatus) {
            this.paymentId     = paymentId;
            this.bookingId     = bookingId;
            this.paymentDate   = paymentDate;
            this.amount        = amount;
            this.paymentMethod = paymentMethod;
            this.paymentStatus = paymentStatus;
        }

        public int    getPaymentId()     { return paymentId; }
        public int    getBookingId()     { return bookingId; }
        public String getPaymentDate()   { return paymentDate; }
        public double getAmount()        { return amount; }
        public String getPaymentMethod() { return paymentMethod; }
        public String getPaymentStatus() { return paymentStatus; }
    }

    
    public static class PackageAvailabilityRow {
        private int    packageId;
        private String destination;
        private double price;
        private int    duration;
        private int    totalSeats;
        private int    bookedSeats;
        private int    availableSeats;
        private int    cancelledBookings;

        public PackageAvailabilityRow(int packageId, String destination, double price,
                                      int duration, int totalSeats, int bookedSeats,
                                      int availableSeats, int cancelledBookings) {
            this.packageId         = packageId;
            this.destination       = destination;
            this.price             = price;
            this.duration          = duration;
            this.totalSeats        = totalSeats;
            this.bookedSeats       = bookedSeats;
            this.availableSeats    = availableSeats;
            this.cancelledBookings = cancelledBookings;
        }

        public int    getPackageId()         { return packageId; }
        public String getDestination()       { return destination; }
        public double getPrice()             { return price; }
        public int    getDuration()          { return duration; }
        public int    getTotalSeats()        { return totalSeats; }
        public int    getBookedSeats()       { return bookedSeats; }
        public int    getAvailableSeats()    { return availableSeats; }
        public int    getCancelledBookings() { return cancelledBookings; }
    }
}