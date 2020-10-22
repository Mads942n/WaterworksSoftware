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

        //call water consumption


        //calculate tax.


    }

    public static void IndustryCalc(int CustomerID,String CustomerTypeString) {
        double waterConsumption = WaterConsumption(CustomerID);

    }

    public static void HouseholdCalc(int CustomerID,String CustomerTypeString) {
        double waterConsumption = WaterConsumption(CustomerID);

    }

    public static double TaxCalculation(double waterConsumption){

        return 0;
    }

    public static double WaterConsumption(int CustomerID) {

        DB.selectSQL("Select fldWaterMeterID from tblWaterMeter where fldCustomerID = " + CustomerID);

        do{
            String data = DB.getDisplayData();
            if (data.equals(DB.NOMOREDATA)){
                break;
            }else{
                data = data.replace("\n", "");

            }
        } while(true);

        return 1.1;
    }



    public static void InsertCalcToDB () {


    }
}




