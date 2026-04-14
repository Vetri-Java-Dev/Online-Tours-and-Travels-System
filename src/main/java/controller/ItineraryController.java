package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import exception.PackageNotFoundException;
import model.Itinerary;
import model.ItineraryItem;
import service.ItineraryService;
import util.ColorText;

public class ItineraryController {

    private Scanner sc = new Scanner(System.in);
    private ItineraryService itineraryService = new ItineraryService();

    // ================= ADMIN: ITINERARY MENU =================
    public void adminItineraryMenu() {
        while(true) {

            System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
            System.out.println(ColorText.warning("║") + ColorText.bold("          ITINERARY MANAGEMENT        ") + ColorText.warning("║"));
            System.out.println(ColorText.warning("╠══════════════════════════════════════╣"));
            System.out.println(ColorText.warning("║") + "  1. Add Itinerary                    " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  2. View Itinerary                   " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  3. Modify Itinerary                 " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  4. Delete Itinerary                 " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  5. Back                             " + ColorText.warning("║"));
            System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));

            System.out.print(ColorText.bold("  Enter choice: "));
            int c = Integer.parseInt(sc.nextLine());

            switch(c) {
                case 1: addItinerary(); break;
                case 2: adminViewItinerary(); break;
                case 3: modifyItinerary(); break;
                case 4:
                    System.out.print(ColorText.bold("  Itinerary ID: "));
                    try {
                        itineraryService.deleteItinerary(Integer.parseInt(sc.nextLine()));
                        System.out.println(ColorText.success("  Itinerary deleted successfully!"));
                    }
                    catch (PackageNotFoundException e) {
                        System.out.println(ColorText.error("  " + e.getMessage()));
                    }
                    break;
                case 5: return;
                default: System.out.println(ColorText.error("  Invalid choice."));
            }
        }
    }

    private void addItinerary() {
        try {
        	
            System.out.print(ColorText.bold("  Package ID     : "));
            int pid = Integer.parseInt(sc.nextLine());
            
            System.out.print(ColorText.bold("  Number of Days : "));
            int days = Integer.parseInt(sc.nextLine());
            
            List<ItineraryItem> items = new ArrayList<>();
            for (int i = 1; i <= days; i++) {
                System.out.print(ColorText.bold("  Day " + i + " Activity : "));
                String activity = sc.nextLine();
                System.out.print(ColorText.bold("  Day " + i + " Location : "));
                String location = sc.nextLine();
                items.add(new ItineraryItem(0, i, activity, location));
            }
            
            itineraryService.createItinerary(new Itinerary(0, pid, items));
        }
        catch (PackageNotFoundException e) {
            System.out.println(ColorText.error("  " + e.getMessage()));
        }
    }
    
    private void modifyItinerary() {
    	addItinerary();
    }

    private void adminViewItinerary() {
        System.out.print(ColorText.bold("  Itinerary ID: "));
        try {
            Itinerary it = itineraryService.viewItinerary(Integer.parseInt(sc.nextLine()));
            
            if(it != null && it.getItems() != null) {
                it.getItems().forEach(i -> System.out.println(
                        ColorText.cyan("  Day " + i.getDayNumber()) + " : " + i.getLocation() + " - " + i.getActivity()));
            } else {
                System.out.println(ColorText.error("  Itinerary not found or empty."));
            }
        }
        catch (PackageNotFoundException e) {
            System.out.println(ColorText.error("  " + e.getMessage()));
        }
        catch (Exception e) {
            System.out.println(ColorText.error("  Invalid input."));
        }
    }

    // ================= CUSTOMER: VIEW ITINERARY =================
    public void customerViewItinerary() {
        System.out.print(ColorText.bold("  Package ID: "));
        try {
            int packageId = Integer.parseInt(sc.nextLine());
            Itinerary itinerary = itineraryService.viewItinerary(packageId);

            if (itinerary == null || itinerary.getItems().isEmpty()) {
                System.out.println(ColorText.error("  No itinerary found for this package."));
                return;
            }

            System.out.println(ColorText.bold("\n  PACKAGE ITINERARY"));
            System.out.println(ColorText.warning("  ─────────────────────────────────────"));

            for (ItineraryItem item : itinerary.getItems()) {
                System.out.println(ColorText.cyan("  Day " + item.getDayNumber()));
                System.out.println("    Activity : " + item.getActivity());
                System.out.println("    Location : " + ColorText.yellow("📍 " + item.getLocation()));
                System.out.println(ColorText.warning("  ─────────────────────────────────────"));
            }
        }
        catch (PackageNotFoundException e) {
            System.out.println(ColorText.error("  " + e.getMessage()));
        }
        catch (NumberFormatException e) {
            System.out.println(ColorText.error("  Invalid Package ID format."));
        }
    }
}