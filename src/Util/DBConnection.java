package Util;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/** This is the Data base connection class. */
public class DBConnection  {

    //JDBC URL parts
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String dnsAddress = "//wgudb.ucertify.com:3306/WJ07I9J";

    //makes up JDBC URL
    private static final String jdbcURL = protocol + vendorName + dnsAddress;

    //Driver interface reference
    private static final String MYSQLJDBCDRIVER = "com.mysql.cj.jdbc.Driver";
    public static Connection conn = null;

    private static final String username = "U07I9J"; //username
    private static  String password = "53689033308"; //password



/** This is a Lambda expression for showing Database connection in a print statement.
 * The lambda expression makes it convenient so I do not need to create a string around showing the connection is successful. */
    public interface connectionInterfaceOpen {
        String getMessage (String v);
    }


    /** This method starts the connection with the database.
     * The method starts the connection with credentials and runs the lambda expression to print out if the connection is successful.
     * */
    public static void  startConnection(){
        try {
            Class.forName(MYSQLJDBCDRIVER);
            conn = DriverManager.getConnection(jdbcURL, username, password);

            connectionInterfaceOpen message = v -> "Connection " + v;
            System.out.println(message.getMessage("is Successful!"));

        }
        catch(ClassNotFoundException e)
        {
            System.out.println(e.getMessage());
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }

    }



/** This is a lambda expression for showing a connection is closed with the DB.
 *  The lambda expression makes it convenient so I do not need to create a string around showing the connection is closed. */
    public interface connectionInterfaceClose {
        String getMessage (String s);
    }

/** This is the method that closes the connection to the DB.
 * This method also runs the lambda expression to print that the connection is closed.
 * */
    public static void closeConnection(){

        try {
            conn.close();

            connectionInterfaceClose message = s -> "Connection " + s;
            System.out.println(message.getMessage("is Closed!"));


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
