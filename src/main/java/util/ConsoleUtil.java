package util;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

public class ConsoleUtil {

    private static LineReader reader;

    // Initialize once
    static {
        try {
            Terminal terminal = TerminalBuilder.builder()
                    .system(true)
                    .jna(false)
                    .jansi(false)
                    .build();

            reader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .build();

        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Method to read password with masking
    public static String readPassword(String prompt) {
        return reader.readLine(prompt,'*');
    }
}