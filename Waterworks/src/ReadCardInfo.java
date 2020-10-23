import java.util.Arrays;
import java.util.Scanner;


public class ReadCardInfo {
    static Scanner in = new Scanner(System.in);

    /**
     * This function gets the reading card info in a string
     * and water meter info in a string array, since there might
     * be multiple water meters for a depending on the segment.
     * Afterwards the function will handle the card and water meter
     * info by calling handleCardInfo and handleWaterMeterInfo
     */
    public static void acceptCardAndMeterInfo() {
        String cardInfo = getCardInfo();                // get card info as "readingCardId,staffCheckValue"
        String[] waterMeterInfo = getWaterMeterInfo();  // get the water meters as "waterMeterID,currentReading"

        handleCardInfo(cardInfo);               // updates the reading card table if necessary
        handleWaterMeterInfo(waterMeterInfo);   // updates the water meter table readings
    }

    /**
     * Determines the reading card id and staff check value
     * then combines them into a comma delimited string, "id,fldStaffReading"
     * @return the comma delimited string
     */
    public static String getCardInfo() {
        String readingCardID = getReadingCardID();      // gets the ID of reading card
        String staffCheckValue = getStaffCheckValue();  // gets the staff value

        return readingCardID + "," + staffCheckValue;   // returns "readingCardId,staffCheckValue"
    }

    /**
     * This function gets the info for all water meters entered
     * by the user
     * @return a string array of water meter info in format "id,reading"
     */
    public static String[] getWaterMeterInfo() {
        int noOfWaterMeters = getNoOfWaterMeters();             // ask user how many water meters is present
        String[] waterMeterInfo = new String[noOfWaterMeters];  // string array of watermeter info

        // gets the string info of each water meters and
        // assigns them to waterMeterInfo string array
        for (int i = 0; i < waterMeterInfo.length; i++) {
            String waterMeterID = getWaterMeterID(i);                           // get water meter id
            String waterMeterCurrentReading = getWaterMeterCurrentReading();    // get current reading of water meter
            waterMeterInfo[i] = waterMeterID + "," + waterMeterCurrentReading;  // creates the string "id,reading"
        }

        return waterMeterInfo;
    }

    /**
     * Asks the user to input a reading card ID
     * @return the user inputted id
     */
    public static String getReadingCardID(){
        boolean acceptingCardID = true;
        String readingCardID;

        do{
            System.out.println("Enter reading card ID");
            readingCardID = in.next();

            // validate the input
            if(validateStringNumber(readingCardID)){
                System.out.printf("Reading card ID: %s\n",readingCardID);
                acceptingCardID = false;    // stop accepting input if validation is OK
            }
        }while(acceptingCardID);

        return readingCardID;
    }

    /**
     * Gets the value of the fldStaffReading from the
     * reading card, entered by the user
     * @return the string value of fldStafReading
     */
    public static String getStaffCheckValue() {
        boolean staffCheck = true;
        String staffCheckValue;

        do{
            System.out.print(
                    "Did the staff do the card reading?\n" +
                            "Enter 0 for no\n" +
                            "Enter 1 for yes\n"
            );
            staffCheckValue = in.next();

            if(validateStaffInfo(staffCheckValue)){
                staffCheck = false; // stop accepting input if validation is OK
            }
        }while(staffCheck);

        return staffCheckValue;
    }

    /**
     * Asks the user, how many water meters are present on
     * the reading card
     * @return the user inputted amount of water meters
     */
    public static int getNoOfWaterMeters() {
        boolean acceptingWaterMeterAmount = true;
        int noOfWaterMeters = 0;

        do{
            System.out.println("Enter amount of water meters");

            if(in.hasNextInt()){
                noOfWaterMeters = in.nextInt();
                acceptingWaterMeterAmount = false;  // stop accepting input if validation is OK
                System.out.printf("Water meters: %d\n",noOfWaterMeters);
            }
            else{
                System.out.println("Invalid amount of water meters");
                in.next();  // flush the scanner, so the old invalid input is not used again
            }
        }while(acceptingWaterMeterAmount);

        return noOfWaterMeters;
    }

    /**
     * Asks the user for a water meter id
     * @param waterMeterNo water meter number for console output
     * @return the user inputted water meter id
     */
    public static String getWaterMeterID(int waterMeterNo) {
        boolean acceptingWaterMeterID = true;
        String waterMeterID = "";

        do{
            System.out.printf("Enter ID of water meter no. %d\n",waterMeterNo+1);

            if(in.hasNext()){
                waterMeterID = in.next();

                if(validateStringNumber(waterMeterID)){
                    acceptingWaterMeterID = false;  // stop accepting input if validation is OK
                    System.out.printf("Water meter ID: %s\n",waterMeterID);
                }
            }
            else{
                System.out.println("Invalid water meter ID");
            }
        }while(acceptingWaterMeterID);

        return waterMeterID;
    }

    /**
     * Asks the user to input a water meter reading for a specific
     * water meter id
     * @return returns user water meter reading input
     */
    public static String getWaterMeterCurrentReading() {
        boolean acceptingWaterMeterReading = true;
        String waterMeterReading = "";

        do{
            System.out.println("Enter water meter reading");

            if(in.hasNext()){
                waterMeterReading = in.next();

                if(validateStringNumber(waterMeterReading)){
                    acceptingWaterMeterReading = false;     // stop accepting input if validation is OK
                    System.out.printf("Water meter reading: %s\n",waterMeterReading);
                }
            }
            else{
                System.out.println("Invalid water meter ID");
            }
        }while(acceptingWaterMeterReading);

        return waterMeterReading;
    }

    /**
     * Determines the card type, and updates the reading card
     * table if necessary
     * @param cardInfo string card info parameter in format: "id,staffReadingValue"
     */
    public static void handleCardInfo(String cardInfo){
        final int READING_CARD_ID_INDEX = 0;    // field index used, when updating the reading card table
        final int READING_CARD_FIELDS = 2;
        String[] cardInfoField = getFields(cardInfo,READING_CARD_FIELDS);   // gets the fields of cardInfo string
        boolean staffFee = determineCardType(cardInfoField);                // determines if is a staff card

        // updates the reading card table by setting the flag of staffReading to 1
        if(staffFee){
            updateReadingCardTable(cardInfoField[READING_CARD_ID_INDEX],"fldStaffReading","1");
        }
    }

    /**
     * Uses a string array containing water meter info "id,currentReading"
     * then updates the SQL water meter table, with the readings
     * @param waterMeter string array with water meter info
     */
    public static void handleWaterMeterInfo(String[] waterMeter) {
        final int WATERMETER_FIELDS = 2;

        // for each water meter,get the info fields into a
        // string array, then update the water meter table
        // with new readings
        for (String waterMeterInfo:waterMeter) {
            String[] waterMeterfields = getFields(waterMeterInfo,WATERMETER_FIELDS);
            updateWaterMeterTable(waterMeterfields);
        }
    }

    /**
     * determines if card type is staff or customer
     * @param cardInfo string array of card info
     * @return returns true if card type is staff, else false
     */
    public static boolean determineCardType(String[] cardInfo){
        final int STAFF_READING_INDEX = 1;
        boolean staffReading = false;

        if(cardInfo[STAFF_READING_INDEX].equals("1")){
            staffReading = handleStaffCard();
        }
        else if(cardInfo[STAFF_READING_INDEX].equals("0")){
            staffReading = handleCustomerCard();
        }
        return staffReading;
    }

    /**
     * Gets called from determineCardType, when the card
     * is determined to be a staff card.
     * @return true
     */
    public static boolean handleStaffCard(){
        return true;
    }

    /**
     * Gets called from determineCardType, when the card
     * is determined to be a customer card.
     * @return false
     */
    public static boolean handleCustomerCard(){
        return false;
    }

    /**
     * Performs an update SQL to update the
     * reading card table, if the reading card is
     * determined to be a staff card
     * @param fldReadingCardID reading card id
     * @param fieldToEdit the field in the table to edit
     * @param editValue the value used for the edit
     */
    public static void updateReadingCardTable(String fldReadingCardID,String fieldToEdit,String editValue){
        String sql = String.format(
                "update tblReadingCard set %s = '%s' where fldReadingCardID = '%s'",
                fieldToEdit,editValue,fldReadingCardID
        );

        DB.updateSQL(sql);
    }

    /**
     * Performs an update SQL query to update the
     * water meter table using a water meter array with
     * fields, water meter id and current reading
     * @param waterMeter the water meter array fields
     */
    public static void updateWaterMeterTable(String[] waterMeter){
        final int WATERMETER_ID_INDEX = 0;
        final int WATERMETER_CURRENTREADING_INDEX = 1;

        String sql = String.format(
                "update tblWaterMeter set fldCurrentPeriodReading = %s where fldWaterMeterID = %s",
                waterMeter[WATERMETER_CURRENTREADING_INDEX],waterMeter[WATERMETER_ID_INDEX]
        );

        DB.updateSQL(sql);
    }

    /**
     * Checks if inputted staff value is valid or not.
     * The value must be either 0 or 1
     * @param staffCheckValue string value of staff value
     * @return returns true if 0 or 1 else false
     */
    public static boolean validateStaffInfo(String staffCheckValue){
        if(staffCheckValue.equals("0") || staffCheckValue.equals("1")){
            return true;
        }
        else{
            System.out.println("Invalid staff info");
            return false;
        }
    }

    /**
     * Validates a string id number by checking if it
     * only contains numbers and is not zero
     * @param number the number to validate
     * @return true if good else false
     */
    public static boolean validateStringNumber(String number){
        boolean isIdOk;

        if(number.equals("0")){
            isIdOk = false;
            System.out.println("Invalid number");
        }
        else if(number.matches("[0-9]+")){
            isIdOk = true;
        }
        else{
            isIdOk = false;
            System.out.println("Invalid number");
        }
        return isIdOk;
    }

    /**
     * Uses a comma delimited string, to separate fields into
     * a string array
     * Ex: "1,AAA,BBB,CCC" becomes String a = {"1","AAA","BBB","CCC"}
     * @param input the string with fields to extract
     * @param numOfFields the number of fields to extract
     * @return string array with the extracted fields
     */
    public static String[] getFields(String input, int numOfFields){
        String[] tblFields = new String[numOfFields];
        Arrays.fill(tblFields,"");
        int fieldIndex = 0;

        for (int i = 0; i < input.length(); i++) {
            Character inputChar = input.charAt(i);

            if(inputChar.equals(',')){
                fieldIndex++;
            }
            else{
                tblFields[fieldIndex] += inputChar;
            }
        }
        return tblFields;
    }

}


