package DAO;

import Model.Contact;
import Util.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/** This class creates a Database CRUD for Contacts. */
public class ContactDB {

    private static ObservableList<Contact> contactList= FXCollections.observableArrayList();


/** This method  grabs gets Contact ID from a string .
 * The method grab the contact ID from the Database using the contact name.
 * @param name contact name
 * @return returns id
 * */
    public static int getContactID(String name) throws SQLException {

        try {
            ResultSet rs = DBConnection.conn.createStatement().executeQuery("select Contact_ID from contacts where Contact_Name = '"+name+"'");
            if(rs.next()) {
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

    /** This method gets the Contact Name from an integer.
     * The method grabs the contact Name from the Data Base by contact ID
     * @param id contact ID
     * @return  Returns contact name
     * */
    public static String getContactName(int id) throws SQLException {
        String query = "select Contact_Name from contacts where Contact_ID = "+id+"";

        try {
            ResultSet rs = DBConnection.conn.createStatement().executeQuery(query);
            if(rs.next()) {
                String name = rs.getString(1);
                return name;
            }

            return null;

        } catch (SQLException e) {
            System.out.println("Error getting contact name from database" + e);
            e.printStackTrace();
            throw e;
        }
    }
}

