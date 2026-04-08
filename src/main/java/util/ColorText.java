package util;

public class ColorText {

    // RESET
    public static final String RESET = "\u001B[0m";

    // TEXT COLORS
    public static final String BLACK  = "\u001B[30m";
    public static final String RED    = "\u001B[31m";
    public static final String GREEN  = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE   = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN   = "\u001B[36m";
    public static final String WHITE  = "\u001B[37m";

    // BOLD
    public static final String BOLD = "\u001B[1m";

    // ---------------- METHODS ----------------

    public static String red(String text) {
        return RED + text + RESET;
    }

    public static String green(String text) {
        return GREEN + text + RESET;
    }

    public static String yellow(String text) {
        return YELLOW + text + RESET;
    }

    public static String blue(String text) {
        return BLUE + text + RESET;
    }

    public static String cyan(String text) {
        return CYAN + text + RESET;
    }

    public static String bold(String text) {
        return BOLD + text + RESET;
    }

    public static String success(String text) {
        return GREEN + BOLD + text + RESET;
    }

    public static String error(String text) {
        return RED + BOLD + text + RESET;
    }

    public static String warning(String text) {
        return YELLOW + BOLD + text + RESET;
    }
}
