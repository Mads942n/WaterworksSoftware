import java.util.Scanner;

public class ReadCardInfo {


        public static String input() {
            String customerCard;
            Scanner in = new Scanner(System.in);
            System.out.print("Type customer card number:");
            //Scanner in = new Scanner(System.in);
            // Scanner CN = new Scanner(System.in);
            String cardNumber = in.nextLine();
            customerCard = in.nextLine();
            return customerCard;
        }
    }


