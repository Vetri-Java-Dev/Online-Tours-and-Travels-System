package controller;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import exception.PackageAlreadyExistsException;
import exception.PackageNotFoundException;
import model.TourPackage;
import service.TourPackageService;
import util.ColorText;
import comparator.PriceComparator;
import comparator.DurationComparator;

public class PackageController {

    private Scanner sc = new Scanner(System.in);
    private TourPackageService service = new TourPackageService();

    // ================= ADMIN: PACKAGE MENU =================
    public void adminPackageMenu() {

        while(true) {

            System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
            System.out.println(ColorText.warning("║") + ColorText.bold("         MANAGE TOUR PACKAGES         ") + ColorText.warning("║"));
            System.out.println(ColorText.warning("╠══════════════════════════════════════╣"));
            System.out.println(ColorText.warning("║") + "  1. Add Package                      " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  2. View Packages                    " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  3. Update Package                   " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  4. Delete Package                   " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  5. Back                             " + ColorText.warning("║"));
            System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));

            System.out.print(ColorText.bold("  Enter choice: "));
            int choice = Integer.parseInt(sc.nextLine());

            switch(choice) {

                case 1:
                    System.out.print(ColorText.bold("  Package ID   : "));
                    int id = Integer.parseInt(sc.nextLine());
                    
                    System.out.print(ColorText.bold("  Destination  : "));
                    String dest = sc.nextLine();
                    
                    System.out.print(ColorText.bold("  Price (INR)  : "));
                    int price = Integer.parseInt(sc.nextLine());
                    
                    System.out.print(ColorText.bold("  Duration     : "));
                    int duration = Integer.parseInt(sc.nextLine());
                    
                    System.out.print(ColorText.bold("  Seats        : "));
                    int seats = Integer.parseInt(sc.nextLine());
                    
                    try {
                        service.createPackage(id, dest, price, duration, seats);
                        System.out.println(ColorText.success("  Package added successfully!"));
                    }
                    catch (PackageAlreadyExistsException e) {
                        System.out.println(ColorText.error("  " + e.getMessage()));
                    }
                    break;

                case 2: service.displayPackages(); break;

                case 3:

                    System.out.print(ColorText.bold("  Package ID      : "));

                    int uid;

                    try{
                        uid=Integer.parseInt(sc.nextLine());
                    }
                    catch(Exception e){
                        while(true){
                            if(sc.hasNextInt()){
                                uid=sc.nextInt();
                                break;
                            }
                            else{
                                System.out.print("Enter Valid Package Id : ");
                            }
                        }
                    }

                    System.out.print(ColorText.bold("  New Destination : "));
                    String nd = sc.nextLine();

                    System.out.print(ColorText.bold("  New Price       : "));
                    double np = Double.parseDouble(sc.nextLine());

                    System.out.print(ColorText.bold("  New Duration    : "));
                    int ndur = Integer.parseInt(sc.nextLine());

                    try {
                        service.updatePackage(uid, nd, np, ndur);
                        System.out.println(ColorText.success("  Package updated successfully!"));
                    }
                    catch (PackageNotFoundException e) {
                        System.out.println(ColorText.error("  " + e.getMessage()));
                    }
                    break;

                case 4:
                    System.out.print(ColorText.bold("  Package ID : "));
                    int deleteId = Integer.parseInt(sc.nextLine());
                    
                    System.out.print(ColorText.bold("  Confirm delete? (yes/no): "));
                    if(sc.nextLine().equalsIgnoreCase("yes")) {
                        try {
                            service.deletePackage(deleteId);
                            System.out.println(ColorText.success("  Package deleted successfully!"));
                        }
                        catch (PackageNotFoundException e) {
                            System.out.println(ColorText.error("  " + e.getMessage()));
                        }
                    }
                    else {
                        System.out.println(ColorText.yellow("  Deletion cancelled."));
                    }
                    break;

                case 5: return;
                default: System.out.println(ColorText.error("  Invalid choice."));
            }
        }
    }

    // ================= CUSTOMER: SEARCH PACKAGE =================
    public void customerSearchPackage(int customerId) {
    	System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
        System.out.println(ColorText.warning("║") + ColorText.bold("           SEARCH PACKAGE             ") + ColorText.warning("║"));
        System.out.println(ColorText.warning("╠══════════════════════════════════════╣"));
        System.out.println(ColorText.warning("║") + "  1.  Search by Destination           " + ColorText.warning("║"));
        System.out.println(ColorText.warning("║") + "  2.  Sort Packages                   " + ColorText.warning("║"));
        System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));
        System.out.print(ColorText.bold("  Enter choice: "));
        int opt = Integer.parseInt(sc.nextLine());  // ✅ fixed

        List<TourPackage> list = service.getAllPackages();

        if (opt == 1) {
            System.out.print(ColorText.bold("  Destination: "));
            list = service.searchByDestination(sc.nextLine());
            if (list.isEmpty()) {
                System.out.println(ColorText.error("  No packages found"));
                return;
            }
        } else {   
            System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
            System.out.println(ColorText.warning("║") + ColorText.bold("            SORT PACKAGES             ") + ColorText.warning("║"));
            System.out.println(ColorText.warning("╠══════════════════════════════════════╣"));
            System.out.println(ColorText.warning("║") + "  1. Price    - Low to High           " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  2. Price    - High to Low           " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  3. Duration - Low to High           " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  4. Duration - High to Low           " + ColorText.warning("║"));
            System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));
            System.out.print(ColorText.bold("  Enter choice: "));

            int sortChoice = Integer.parseInt(sc.nextLine());

            switch (sortChoice) {
                case 1: Collections.sort(list, new PriceComparator(true));     break;
                case 2: Collections.sort(list, new PriceComparator(false));    break;
                case 3: Collections.sort(list, new DurationComparator(true));  break;
                case 4: Collections.sort(list, new DurationComparator(false)); break;
                default: System.out.println(ColorText.error("  Invalid choice.")); return;
            }
        }

        for (TourPackage t : list)
            System.out.println("  ID: " + t.getPackageId() + " - " + ColorText.cyan(t.getDestination()) + " - Rs." + t.getPrice());

        System.out.print("\nDo you want to book any package? (yes/no): ");
        String choice = sc.nextLine();

        if (choice.equalsIgnoreCase("yes")) {
            new BookingController().createBooking(customerId);
        } else {
            System.out.println(ColorText.yellow("  Redirecting to Dashboard..."));
        }
    }
}
