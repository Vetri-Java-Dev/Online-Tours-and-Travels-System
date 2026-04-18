package util;

import java.util.Scanner;

public class InputUtil {
    private static final Scanner scanner = new Scanner(System.in);

    public static int getInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            
            if (input.isEmpty()) {
            	continue;
            }
            
            try {
                return Integer.parseInt(input);
            }
            catch (NumberFormatException e) {
                System.out.println(ColorText.error("  Invalid input. Please enter a valid number."));
            }
        }
    }

    public static double getDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            
            if (input.isEmpty()) {
            	continue;
            }
            try{
                return Double.parseDouble(input);
            }
            catch (NumberFormatException e) {
                System.out.println(ColorText.error("  Invalid input. Please enter a valid decimal number."));
            }
        }
    }

    public static String getString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
    
    public static void close() {
        if (scanner != null) {
            scanner.close();
        }
    }
}
