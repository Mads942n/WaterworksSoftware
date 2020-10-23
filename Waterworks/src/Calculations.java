import java.util.Locale;

public class Calculations {

    /**
     * Determines the costumer type and calls the calculations
     * of taxes and prices depending on the costumer segment
     * value.
     * @param CustomerID customer id
     * @param ReadingCardID reading card id
     */
    public static void CustomerType(int CustomerID, int ReadingCardID) {

        DB.selectSQL("Select fldSegment from tblCustomer where fldCustomerID = " + CustomerID);

        String CustomerTypeString = DB.getData();
        flushPendingSQLData();  // Discards additional data from the SQL query, as they are not needed

        switch (CustomerTypeString) {
            case "Agriculture":
                AgricultureCalc(CustomerID, CustomerTypeString, ReadingCardID);
                break;
            case "Industry":
                IndustryCalc(CustomerID, CustomerTypeString, ReadingCardID);
                break;
            case "Household":
                HouseholdCalc(CustomerID, CustomerTypeString, ReadingCardID);
                break;
            default:
                System.out.println("Invalid segment");
                break;
        }
    }

    /**
     * Performs the necessary calculations including price and tax,
     * then inserts the calculations into the tblBilling and
     * inserts the old data into tblReadingCardWaterMetes. Lastly
     * performs a reset of the fldCurrentPeriodReading for determined
     * water meter
     * @param CustomerID id
     * @param CustomerTypeString segment
     * @param ReadingCardID card id
     */
    public static void AgricultureCalc(int CustomerID,String CustomerTypeString,int ReadingCardID) {
        double waterConsumption = WaterConsumption(CustomerID);             // gets the water consumption of the period
        double waterPrice = PriceCalculation(waterConsumption);             // gets the price for the used water
        double waterTax = TaxCalculation(waterPrice,CustomerTypeString);    // gets tax of water
        double waterReadingFee = DetermineStaffReading(ReadingCardID);      // gets staff reading fee
        double waterTotalPrice = waterPrice + waterTax + waterReadingFee;   // calculate the total price

        // Store the result of the calculations in a double array
        double[] billingPriceInfo = {
                waterConsumption,
                waterTax,
                waterReadingFee,
                waterPrice,
                waterTotalPrice
        };

        InsertCalcToDB(billingPriceInfo,CustomerID);        // inserts the calculations into tblBilling
        InsertOldReadingsToDB(CustomerID,ReadingCardID);    // inserts old readings
        ResetWaterMeter(CustomerID);                        // resets the water meter fldCurrentPeriodReading to 0
    }

    /**
     * Performs the necessary calculations including price and tax,
     * then inserts the calculations into the tblBilling and
     * inserts the old data into tblReadingCardWaterMetes. Lastly
     * performs a reset of the fldCurrentPeriodReading for determined
     * water meter
     * @param CustomerID id
     * @param CustomerTypeString segment
     * @param ReadingCardID card id
     */
    public static void IndustryCalc(int CustomerID,String CustomerTypeString,int ReadingCardID) {
        double waterConsumption = WaterConsumption(CustomerID);             // gets the water consumption of the period
        double waterPrice = PriceCalculation(waterConsumption);             // gets the price for the used water
        double waterTax = TaxCalculation(waterPrice,CustomerTypeString);    // gets tax of water
        double waterReadingFee = DetermineStaffReading(ReadingCardID);      // gets staff reading fee
        double waterTotalPrice = waterPrice + waterTax + waterReadingFee;   // calculate the total price

        // Store the result of the calculations in a double array
        double[] billingPriceInfo = {
                waterConsumption,
                waterTax,
                waterReadingFee,
                waterPrice,
                waterTotalPrice
        };

        InsertCalcToDB(billingPriceInfo,CustomerID);        // inserts the calculations into tblBilling
        InsertOldReadingsToDB(CustomerID,ReadingCardID);    // inserts old readings
        ResetWaterMeter(CustomerID);                        // resets the water meter fldCurrentPeriodReading to 0
    }

    /**
     * Performs the necessary calculations including price and tax,
     * then inserts the calculations into the tblBilling and
     * inserts the old data into tblReadingCardWaterMetes. Lastly
     * performs a reset of the fldCurrentPeriodReading for determined
     * water meter
     * @param CustomerID id
     * @param CustomerTypeString segment
     * @param ReadingCardID card id
     */
    public static void HouseholdCalc(int CustomerID,String CustomerTypeString,int ReadingCardID) {
        double waterConsumption = WaterConsumption(CustomerID);             // gets the water consumption of the period
        double waterPrice = PriceCalculation(waterConsumption);             // gets the price for the used water
        double waterTax = TaxCalculation(waterPrice,CustomerTypeString);    // gets tax of water
        double waterReadingFee = DetermineStaffReading(ReadingCardID);      // gets staff reading fee
        double waterTotalPrice = waterPrice + waterTax + waterReadingFee;   // calculate the total price

        // Store the result of the calculations in a double array
        double[] billingPriceInfo = {
                waterConsumption,
                waterTax,
                waterReadingFee,
                waterPrice,
                waterTotalPrice
        };

        InsertCalcToDB(billingPriceInfo,CustomerID);        // inserts the calculations into tblBilling
        InsertOldReadingsToDB(CustomerID,ReadingCardID);    // inserts old readings
        ResetWaterMeter(CustomerID);                        // resets the water meter fldCurrentPeriodReading to 0
    }

    /**
     * Inserts old data into tblReadingCardWaterMeter. The function
     * loops through each water meter ID, depending on the amount of
     * water meters. Each loop extracts fldLastPeriodReading and
     * fldCurrentPeriodReading and performs and insert statement
     * afterwards.
     * @param customerID id used for getting number of water meters
     * @param readingCardID card id used for insertion into database
     */
    public static void InsertOldReadingsToDB(int customerID, int readingCardID) {
        final int METER_LAST_READING_INDEX = 0;
        final int METER_CURRENT_READING_INDEX = 1;
        final int METER_FIELDS = 2;
        int noOfWaterMeters = getNoOfWaterMeters(customerID);                   // get number of water meters
        String[] waterMeterID = getWaterMeterIDs(customerID,noOfWaterMeters);   // get water meter IDs
        String fldCurrentReading;
        String fldLastReading;
        String sql;

        for (int i = 0; i < noOfWaterMeters; i++) {
            // perform a select SQL statement to get data from water meter
            String waterMeterInfo = sendSelectSQL(
                    "fldLastPeriodReading,fldCurrentPeriodReading",
                    "tblWaterMeter",
                    "fldWaterMeterID",
                    waterMeterID[i]
            );
            String[] waterMeterFields = ReadCardInfo.getFields(waterMeterInfo,METER_FIELDS);    // see ReadCardInfo.getFields
            fldCurrentReading = waterMeterFields[METER_CURRENT_READING_INDEX];          // store current reading of water meter
            fldLastReading = waterMeterFields[METER_LAST_READING_INDEX];                // store last reading of water meter

            // insert into the tblReadingCardWaterMeter
            sql = String.format(
                    Locale.ENGLISH,
                    "insert into tblReadingCardWaterMeter " +
                    "(fldReadingCardID,fldWaterMeterID,fldLastPeriodReading,fldCurrentPeriodReading) " +
                    "values (%d,%s,%s,%s)",
                    readingCardID,waterMeterID[i],fldLastReading,fldCurrentReading
            );
            DB.insertSQL(sql);
        }
    }


    /**
     * Sends a SQL select statement with the WHERE clause.
     * The function formats the data into a comma delimited
     * string, ex "AAAA,BBBB,CCCC,DDDD".
     * Only works with one row output from the database.
     * @param selectFields fields to select, ex "fldName,fldAddress"
     * @param tableName table name
     * @param conditionFieldName the condition field for the where clause
     * @param conditionValue the value used for comparison
     * @return a comma delimited string with the extracted data
     */
    public static String sendSelectSQL(String selectFields, String tableName, String conditionFieldName, String conditionValue){
        String sql;
        String sqlData = "";

        // prepare command
        sql = String.format(
                "select %s from %s where %s = %s",
                selectFields,tableName,conditionFieldName,conditionValue
        );

        DB.selectSQL(sql);

        do{
            String data = DB.getDisplayData();
            if (data.equals(DB.NOMOREDATA)){
                break;
            }else{
                sqlData += data;
            }
        } while(true);

        sqlData = sqlData.replaceAll("\\s+", ",");  // remove excess amount of spaces and seperate with comma
        sqlData = sqlData.substring(0,sqlData.length()-1);          // remove the last comma

        return sqlData;
    }

    /**
     * Resets an arbitrary amount of water meters by setting
     * fldLastPeriodReading to fldCurrentPeriodReading and
     * fldCurrentPeriodReading to 0
     * @param customerID used for determining number of water meters and their ID
     */
    public static void ResetWaterMeter(int customerID){
        int noOfWaterMeters = getNoOfWaterMeters(customerID);   // gets the amount of water meters for the customer ID
        String[] waterMeterID = getWaterMeterIDs(customerID,noOfWaterMeters);   // get the water meter IDs
        String sql;

        // for each water meter id, perform the update
        for (String id:waterMeterID) {
            sql = String.format(
                    "update tblWaterMeter set fldLastPeriodReading = fldCurrentPeriodReading where fldWaterMeterID = %s\n" +
                    "update tblWaterMeter set fldCurrentPeriodReading = 0 where fldWaterMeterID = %s",
                    id,id
            );

            DB.updateSQL(sql);
        }
    }


    /**
     * Using the customer id and amount of water meters,
     * this function will perform a select query and format
     * the database output and the store the Ids in a water
     * meter array.
     * @param customerID used as condtion value in where clause
     * @param noOfWaterMeters used as the size of water meter ID array
     * @return a water meter ID array
     */
    public static String[] getWaterMeterIDs(int customerID,int noOfWaterMeters){
        int waterMeterIndex = 0;
        String[] waterMeterID = new String[noOfWaterMeters];

        String sql = String.format(
                "select fldWaterMeterID from tblWaterMeter where fldCustomerID = %d",
                customerID
        );
        DB.selectSQL(sql);

        do{
            String data = DB.getDisplayData();
            if (data.equals(DB.NOMOREDATA)){
                break;
            }else{
                data = data.replace("\n","");   // get rid of excessive newlines
                waterMeterID[waterMeterIndex] = data;           // store the water meter id
                waterMeterIndex++;                              // next index in water meter id array
            }
        } while(true);

        return waterMeterID;
    }

    /**
     * Determines the amount of water meters for a given
     * customer id, by performing a count SQL select on
     * tblWaterMeter
     * @param customerID customer id
     * @return number of water meters
     */
    public static int getNoOfWaterMeters(int customerID){
        String stringNoOfWaterMeters;
        int noOfWaterMeters;

        String sql = String.format(
                "select count(fldWaterMeterID) from tblWaterMeter where fldCustomerID = %d",
                customerID
        );

        DB.selectSQL(sql);

        stringNoOfWaterMeters = DB.getData();
        noOfWaterMeters = Integer.parseInt(stringNoOfWaterMeters);
        flushPendingSQLData();

        return noOfWaterMeters;
    }

    /**
     * Determines if a fee should be added to the total
     * price, by extracting the fldStaffReading flag from
     * tblReadingCard
     * @param readingCardID card id
     * @return returns the fee amount if true
     */
    public static double DetermineStaffReading(int readingCardID) {
        final double STAFF_FEE = 250;

        String sql = String.format(
                "select fldStaffReading from tblReadingCard where fldReadingCardID = %d",
                readingCardID
        );
        DB.selectSQL(sql);

        String staffReadingValue = DB.getData();
        flushPendingSQLData();

        if(staffReadingValue.equals("1")){
            return STAFF_FEE;
        }
        else {
            return 0;
        }
    }


    /**
     * Calculates the price of water consumption
     * @param waterConsumption water used
     * @return the price
     */
    public static double PriceCalculation(double waterConsumption) {
        final double PRICE_PER_M3 = 22.5;

        return PRICE_PER_M3 * waterConsumption;
    }


    /**
     * Calculates the tax of price of used water. Checks
     * which customer type context, before performing the
     * calculation, since the different segments have
     * different tax rates.
     * @param price price of used water
     * @param CustomerTypeString customer type
     * @return tax of used water
     */
    public static double TaxCalculation(double price, String CustomerTypeString){
        final double TAX_RATE_HOUSEHOLD = 0.25;
        final double TAX_RATE_INDUSTRY = 0.10;
        final double TAX_RATE_AGRICULTURE = 0.15;
        double tax = 0;

        switch (CustomerTypeString) {
            case "Agriculture":
                tax = price * TAX_RATE_AGRICULTURE;
                break;
            case "Industry":
                tax = price * TAX_RATE_INDUSTRY;
                break;
            case "Household":
                tax = price * TAX_RATE_HOUSEHOLD;
                break;
        }
        return tax;
    }

    /**
     * Inserts the calculated prices and tax into
     * tblBilling.
     * @param billingPriceInfo double array containing various price info
     * @param customerID customer id
     */
    public static void InsertCalcToDB (double[] billingPriceInfo,int customerID) {
        final int WATER_CONSUMPTION_INDEX = 0,
                WATER_TAX_INDEX = 1,
                WATER_READING_FEE_INDEX = 2,
                WATER_PRICE_INDEX = 3,
                WATER_TOTAL_PRICE_INDEX = 4;

        String sql = String.format(
                Locale.ENGLISH,
                "insert into " +
                "tblBilling (fldCustomerID,fldWaterConsumption,fldWaterTax,fldReadingFee,fldWaterCost,fldTotalCost) " +
                "values (%d,%.2f,%.2f,%.2f,%.2f,%.2f)",
                customerID,
                billingPriceInfo[WATER_CONSUMPTION_INDEX],
                billingPriceInfo[WATER_TAX_INDEX],
                billingPriceInfo[WATER_READING_FEE_INDEX],
                billingPriceInfo[WATER_PRICE_INDEX],
                billingPriceInfo[WATER_TOTAL_PRICE_INDEX]
        );
        DB.insertSQL(sql);
    }

    /**
     * Performs various loops and SQL select statements
     * to calculate the water consumption of a given
     * customer id
     * @param CustomerID the customer id
     * @return the total water consumption of water meters
     */
    public static float WaterConsumption(int CustomerID) {

        /**
         * @author Mads Bruhn
         *
         * In the beginning of this function i start by, declaring all the variables i am going to need.
         */
        float WaterCalc = 0;
        float WaterCurrentPeriodCalc = 0;
        float WaterLastPeriodCalc = 0;
        String data;
        String data2;
        int i = 0;
        int x = 0;

        /**
         * This float array, is used to contain the numbers gotten from the fldCurrentPeriodReading,
         * And the other float array is for the fldLastPeriodReading.
         */
        float[] CurrentPeriodReading = new float[5];
        float[] LastPeriodReading = new float[5];



        /**
         * In this next line i have a DB selectSQl statement, which will send a SQL request to the DB.
         * Which will execute and retrieve the selected data.
         */
        DB.selectSQL("Select fldCurrentPeriodReading from tblWaterMeter where fldCustomerID = " + CustomerID);


        /**
         * This do while, loop, will continue to retrieve data from the DB, until there is no more data,
         * That fits the criteria of the previous select statement.
         *
         * In the else part of the loop, it will put the retrieved data into a float array.
         */
        do{
            data = DB.getDisplayData();
            i++;
            if (data.equals(DB.NOMOREDATA)){
                break;
            }else{
                //System.out.print(data);
                CurrentPeriodReading[i - 1] = Float.parseFloat(data);

            }
        } while(true);

        /**
         * In this simple for loop, i count between 0 and 5 (The max number of different array data).
         * This simply adds the numbers up from the float array.
         */
        for (int j = 0; j < 5; j++) {

            WaterCurrentPeriodCalc = WaterCurrentPeriodCalc + CurrentPeriodReading[j];

        }
        //System.out.println(WaterCurrentPeriodCalc);


        /**
         * This DB select statement, only changes one thing from the last one,
         * which is choosing which field to retrieve data from.
         */
        DB.selectSQL("Select fldLastPeriodReading from tblWaterMeter where fldCustomerID = " + CustomerID);

        /**
         * This do while loop is the same as the one above, only that the variables have changed.
         */
        do{
            data2 = DB.getDisplayData();
            x++;
            if (data2.equals(DB.NOMOREDATA)){
                break;
            }else{
                //System.out.print(data2);
                LastPeriodReading[x - 1] = Float.parseFloat(data2);

            }
        } while(true);


        /**
         * This for loop is almost the same as seen above, but just the variables have changed.
         */
        for (int j = 0; j < 5; j++) {

            WaterLastPeriodCalc = WaterLastPeriodCalc + LastPeriodReading[j];

        }
        //System.out.println(WaterLastPeriodCalc);


        /**
         * Here at the end i will subtract the last period reading for the current period reading.
         */
        WaterCalc = WaterCurrentPeriodCalc - WaterLastPeriodCalc;

        //System.out.println(WaterCalc);
        return WaterCalc;
    }

    /**
     * Discards unneeded output data from SQL queries
     */
    public static void flushPendingSQLData(){
        do{
            String data = DB.getDisplayData();
            if (data.equals(DB.NOMOREDATA)){
                break;
            }
        } while(true);
    }
}