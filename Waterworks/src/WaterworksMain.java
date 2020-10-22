import java.util.Scanner;

public class WaterworksMain {
    static Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        start();
    }

    public static void start() {
        boolean online = true;
        int input;
        String stringInput;

        do {
            System.out.print(
                    "Choose options 1-6\n" +
                    "1. Input reading card and water meter info\n" +
                    "2. Generate billing\n" +
                    "3. Generate labels and statistics\n" +
                    "4. Generate reading card\n" +
                    "5. Handle payments\n" +
                    "6. Perform price calculations\n"
            );

            if (in.hasNextInt()) {
                input = in.nextInt();

                switch (input) {
                    case 1:
                        ReadCardInfo.acceptCardAndMeterInfo();
                        break;
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    default:
                        System.out.println("Invalid input");
                }
            } else if (in.hasNext()) {
                stringInput = in.next();

                if (stringInput.equals("x")) {
                    System.out.println("Exiting");
                    online = false;
                }
            }
        } while (online);
    }
}