

/**
 *
 * @author tha
 */
public class DBTester {
    
    public static void main (String [] args){

        DB.selectSQL("Select * from tblcustomer");
        
        do{
           String data = DB.getDisplayData();
           if (data.equals(DB.NOMOREDATA)){
               break;
           }else{
               System.out.print(data);
           }   
        } while(true);
        

        do{
           String data = DB.getData();
           if (data.equals(DB.NOMOREDATA)){
               break;
           }else{
               System.out.print(data);
           }   
        } while(true);
       
    }
    
}
