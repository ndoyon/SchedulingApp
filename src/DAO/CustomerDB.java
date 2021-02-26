package DAO;
import Model.Customer;
import Util.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;

/** This class operates CRUD for customers in the application. */
public class CustomerDB {


    private static ObservableList<Customer> customerList = FXCollections.observableArrayList();


/** This method gets All customers.
 * The method grabs customers from observable list.
 * @return Returns observable customer list.
 * */
    public static ObservableList<Customer> getAllCustomers() {
        return customerList;
    }


/** This method selects all Customers.
 * The method selects all customers from the Database to be used in the application.
 * @return Returns observable customerList.
 * */
    public static ObservableList<Customer> selectAllCustomers() {
        customerList.clear();

        try {

            ResultSet rs = DBConnection.conn.createStatement().executeQuery("SELECT Customer_ID, Division_ID, Customer_Name,Address,Postal_Code,Phone FROM customers");

            while (rs.next()) {
                Customer customer = new Customer(
                        rs.getInt("Customer_ID"),
                        rs.getInt("Division_ID"),
                        rs.getString("Customer_Name"),
                        rs.getString("Address"),
                        rs.getString("Postal_Code"),
                        rs.getString("Phone"));

                customerList.add(customer);
            }
            return customerList;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
    }


 /** This method grabs next Customer Id.
  * The method grabs the next available customer ID from the DB.
  * @return customer ID
  * */
    public static Integer nextID(){
        Integer customerID = 1;
        try {
            Statement stmt = DBConnection.conn.createStatement();
            String query = "SELECT Customer_ID FROM customers ORDER BY Customer_ID";
            ResultSet rs = stmt.executeQuery(query);


            while (rs.next()) {
                if (rs.getInt("Customer_ID") == customerID) {
                    customerID++;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return customerID;

    }


/** This method updates the customer DB.
 * The method updates the customer DB and adds the customer to customer list to be used by the application.
 *@param customerID Customer ID
 *@param address Customer address
 *@param customerName Customer's name
 *@param divisionID Division integer
 *@param phoneNumber Customer phone number
 *@param postalCode Customers zip code
 *@return Returns customer ID
 * */
    public static Integer updateCustomer(Integer customerID, String customerName, String address, String postalCode, String phoneNumber, int divisionID) {

        try {


            PreparedStatement ps = DBConnection.conn.prepareStatement(
                    "UPDATE customers SET "
                            + "Customer_Name = ?, "
                            + "Address = ?, "
                            + "Postal_Code = ?, "
                            + "Phone = ?, "
                            + "Create_Date = ?, "
                            + "Created_By = ?, "
                            + "Division_ID = ? "
                            + "WHERE "
                            + "Customer_ID = ?");

            ps.setString(1, customerName);
            ps.setString(2, address);
            ps.setString(3, postalCode);
            ps.setString(4, phoneNumber);
            ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(6, String.valueOf(UserDB.getCurrentUser()));
            ps.setInt(7, divisionID);
            ps.setInt(8, customerID);

            ps.execute();
            ps.close();
            return customerID;

        } catch (SQLException e) {
            System.out.println("No Change! SQLException" + e.getMessage());
            return null;
        }
    }

/** This method adds a Customer to the DB.
 * The method adds a customer to the Database and adding customer to customer list.
 * @param divisionID Division ID
 * @param customerName Customer's name
 * @param customerAddress  Customer's Address
 * @param customerPhone Customer's phone number
 * @param customerPostal Customer's zip code
 * */
    public static void addCustomer(String customerName, String customerAddress, String customerPostal, String customerPhone, int divisionID) {
        int customerID = nextID();
        String currentUser = UserDB.getCurrentUser().getUserName();


        try {
            PreparedStatement ps = DBConnection.conn.prepareStatement(

                    "INSERT INTO customers(Customer_ID,Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) VALUES(?,?,?,?,?,?,?,?,?,?)");

            Customer customer = new Customer();
            ps.setString(1, String.valueOf(customerID));
            ps.setString(2, customerName);
            ps.setString(3, customerAddress);
            ps.setString(4, customerPostal);
            ps.setString(5, customerPhone);
            ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(7, currentUser);
            ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(9, currentUser);
            ps.setInt(10, divisionID);

            ps.execute();

            if (ps.getUpdateCount() > 0)
                customerList.add(customer);


        } catch (SQLException e) {
            System.out.println("Can't Add, SQLException" + e.getMessage());

        }
    }


/** This method is a boolean for deleting a customer from the DB.
 * The method, if true will delete the customer from the DB, if it's false it will not.
 * @param customerID  Customer's ID
 * @return Returns either true or false
 * */
    public static boolean deleteCustomer(Integer customerID) {

        try {

            PreparedStatement ps = DBConnection.conn.prepareStatement("DELETE FROM appointments WHERE " + "Customer_ID = ?");
            ps.setInt(1, customerID);
            ps.execute();

            ps = DBConnection.conn.prepareStatement("DELETE FROM customers WHERE " + "Customer_ID = ?");
            ps.setInt(1, customerID);
            ps.execute();
            int rowCount = ps.getUpdateCount();
            ps.close();
            if (rowCount > 0)
                return true;

            return false;

        } catch (SQLException e) {
            System.out.println("Can't Delete, SQLException" + e.getMessage());
            return false;
        }
    }


/** This method grabs the customer ID.
 * The method gets the customer ID from the customer name.
 * @param name Customer's name
 * @return Returns customer ID
 * */
    public static Integer getID(String name) throws SQLException {
        try {
            ResultSet rs = DBConnection.conn.createStatement().executeQuery("select Customer_ID from customers where Customer_Name = '" + name + "'");
            if (rs.next()) {
                int id = Integer.parseInt(rs.getString(1));
                return id;
            }

            return 0;

    } catch (SQLException e) {
        System.out.println("Error getting contact id from database" + e);
        e.printStackTrace();
        throw e;
    }
    }


/** This method is to get customers name from the DB.
 * The method gets the customer's name from the DB by customer ID.
 * @param id  Customer's ID
 * @return Returns string customer's name.
 * */
    public static String getCustomerName(int id) throws SQLException {
        String query = "select Customer_Name from customers where Customer_ID = "+id+"";

        try {
            ResultSet rs = DBConnection.conn.createStatement().executeQuery(query);
            if(rs.next()) {
                String name = rs.getString(1);
                return name;
            }

            return null;

        } catch (SQLException e) {
            System.out.println("Error getting customers name from database" + e);
            e.printStackTrace();
            throw e;
        }
    }
}



