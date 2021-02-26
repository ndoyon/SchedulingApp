package DAO;

import Model.Country;
import Util.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.ResultSet;
import java.sql.SQLException;


/** This class grabs country CRUD information from Database. */
public class CountryDB {

/** This method gets country ID.
 * The method gets the country ID from a Division ID in the database.
 * @param id division id
 * @return Returns the country ID
 * */
    public static int getCountryIdFromDivisionId(int id) throws SQLException {
        String query = "select COUNTRY_ID from first_level_divisions where Division_ID = "+id+"";

        try {
            ResultSet rs = DBConnection.conn.createStatement().executeQuery(query);
            if(rs.next()) {
                int countryId = Integer.parseInt(rs.getString(1));
                return countryId;
            }

            return 0;

        } catch (SQLException e) {
            System.out.println("Error getting country id from database" + e);
            e.printStackTrace();
            throw e;
        }
    }

/** This method gets the Country Name.
 * The method grabs the country name from the Country ID in the Database.
 * @param id  country id
 * @return Returns string country name.
 * */
    public static String getCountryName(int id) throws SQLException {
        String query = "select Country from countries where Country_ID = "+id+"";

        try {
            ResultSet rs = DBConnection.conn.createStatement().executeQuery(query);
            if(rs.next()) {
                String countryName = rs.getString(1);
                return countryName;
            }

            return "";

        } catch (SQLException e) {
            System.out.println("Error getting country name from database" + e);
            e.printStackTrace();
            throw e;
        }
    }
}
