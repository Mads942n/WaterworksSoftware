

/**
 *
 * @author tha
 */
public class DBTester {
    
    public static void main (String [] args){

        DB.selectSQL("Select * from tblCustomer");
        
        do{
           String data = DB.getDisplayData();
           if (data.equals(DB.NOMOREDATA)){
               break;
           }else{
               System.out.print(data);
           }   
        } while(true);
    }
}
