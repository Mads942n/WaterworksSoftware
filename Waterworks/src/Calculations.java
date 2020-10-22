public class Calculations {



    public static String CustomerType(int CustomerID) {

        DB.selectSQL("Select fldSegment from tblCustomer where fldCustomerID = " + CustomerID);

        String CustomerTypeString = DB.getData();

        System.out.println(CustomerTypeString);

        if(CustomerTypeString.equals("Agriculture")){
            AgricultureCalc(CustomerID,CustomerTypeString);
        }
        else if(CustomerTypeString.equals("Industry")){
            IndustryCalc(CustomerID,CustomerTypeString);
        }
        else if(CustomerTypeString.equals("Household")){
            HouseholdCalc(CustomerID,CustomerTypeString);
        }
        else{
            System.out.println("Invalid segment");
        }

        return CustomerTypeString;
    }

    public static void AgricultureCalc(int CustomerID,String CustomerTypeString) {

        double waterConsumption = WaterConsumption(CustomerID);
        double tax = TaxCalculation(waterConsumption,CustomerTypeString);
        //call water consumption


        //calculate tax.


    }

    public static void IndustryCalc(int CustomerID,String CustomerTypeString) {
        double waterConsumption = WaterConsumption(CustomerID);
        double tax = TaxCalculation(waterConsumption,CustomerTypeString);

    }

    public static void HouseholdCalc(int CustomerID,String CustomerTypeString) {
        double waterConsumption = WaterConsumption(CustomerID);
        double tax = TaxCalculation(waterConsumption,CustomerTypeString);

    }

    public static double TaxCalculation(double waterConsumption, String CustomerTypeString){

        return 0;
    }




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
        System.out.println(WaterCurrentPeriodCalc);


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
        System.out.println(WaterLastPeriodCalc);


        /**
         * Here at the end i will subtract the last period reading for the current period reading.
         */
        WaterCalc = WaterLastPeriodCalc - WaterCurrentPeriodCalc;

        System.out.println(WaterCalc);
        return WaterCalc;
    }



    public static void InsertCalcToDB () {


    }
}




