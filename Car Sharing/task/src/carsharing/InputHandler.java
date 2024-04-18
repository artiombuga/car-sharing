package carsharing;

import java.util.Scanner;

public class InputHandler {
    Scanner scanner = new Scanner(System.in);

    public String promptForString(String prompt) {
        System.out.println(prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt) {
        while (true) {
            try {
                System.out.println(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    public void closeScanner() {
        scanner.close();
    }

}
