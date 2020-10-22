

import java.util.Arrays;

public class Calculations {


    public static String CustomerType(int CustomerID) {

        DB.selectSQL("Select fldSegment from tblCustomer where fldCustomerID = " + CustomerID);

        String CustomerTypeString = DB.getData();

        return CustomerTypeString;

    }

    public static void AgricultureCalc() {


        //call water consumption


        //calculate tax.


    }

    public static void IndustryCalc() {


    }

    public static void HouseholdCalc() {


    }

    public static float[] WaterConsumption(int CustomerID) {

        DB.selectSQL("Select fldWaterMeterID from tblWaterMeter where fldCustomerID = " + CustomerID);

        do{
            String data = DB.getDisplayData();
            if (data.equals(DB.NOMOREDATA)){
                break;
            }else{
                System.out.print(data);
            }
        } while(true);


    }


    public static void InsertCalcToDB () {


    }
}




