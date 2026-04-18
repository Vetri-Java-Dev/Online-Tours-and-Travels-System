package controller;

import java.util.ArrayList;
import java.util.List;

import exception.PackageNotFoundException;
import util.InputUtil;
import model.Itinerary;
import model.ItineraryItem;
import service.ItineraryService;
import util.ColorText;

public class ItineraryController {

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

            int c = InputUtil.getInt(ColorText.bold("  Enter choice: "));

            switch(c) {
                case 1: addItinerary(); break;
                case 2: adminViewItinerary(); break;
                case 3: modifyItinerary(); break;
                case 4:
                    try {
                        itineraryService.deleteItinerary(InputUtil.getInt(ColorText.bold("  Itinerary ID: ")));
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
        	
            int pid = InputUtil.getInt(ColorText.bold("  Package ID     : "));
            int days = InputUtil.getInt(ColorText.bold("  Number of Days : "));
            
            List<ItineraryItem> items = new ArrayList<>();
            for (int i = 1; i <= days; i++) {
                String activity = InputUtil.getString(ColorText.bold("  Day " + i + " Activity : "));
                String location = InputUtil.getString(ColorText.bold("  Day " + i + " Location : "));
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
        try {
            Itinerary it = itineraryService.viewItinerary(InputUtil.getInt(ColorText.bold("  Itinerary ID: ")));
            
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
        try {
            int packageId = InputUtil.getInt(ColorText.bold("  Package ID: "));
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
    }
}