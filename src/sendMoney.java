import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class sendMoney {


    public static String send(int amount, String accountNumber){
        int bal = 0;
        String returnVal = "";

        //grab our balance
        try {
            Class.forName(FinalFiles.dataBaseDriver);
            Connection connection = DriverManager.getConnection(FinalFiles.LinuxDatabaseURL);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT Balance FROM Balance WHERE accountNumber = ? ");
            preparedStatement.setString(1, accountNumber);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                bal = resultSet.getInt("Balance");
            }


        }catch (Exception exception){
            System.out.println(exception);

        }
        //Increase the Balance by the amount deposited
        bal = bal + amount;

        try{
            Class.forName(FinalFiles.dataBaseDriver);
            Connection connection = DriverManager.getConnection(FinalFiles.LinuxDatabaseURL);
            //statement to update the balance
            PreparedStatement preparedStatement = connection.prepareStatement("Update Balance SET Balance = ? WHERE accountNumber=?");
            preparedStatement.setInt(1, bal);
            preparedStatement.setString(2, accountNumber);

            //Statement to update the deposit table
            PreparedStatement preparedStatement2 = connection.prepareStatement("insert into deposits VALUES(?, ?, ?)");
            preparedStatement2.setString(1, accountNumber);
            preparedStatement2.setInt(2, amount);
            preparedStatement2.setString(3, FinalFiles.Date);

            preparedStatement.executeUpdate();
            preparedStatement2.executeUpdate();

        }catch (Exception exception){
            System.out.println(exception);
        }
        returnVal = " You Have " + amount+ " deposited into " + accountNumber + " Your Balance is: " + bal;
        return returnVal;



    }



}
