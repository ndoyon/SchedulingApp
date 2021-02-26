package DAO;

import Model.Division;
import Util.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



/** This class gets the division CRUD from the DB.*/
public class DivisionDB {
    private final ObservableList<Division> divisionList = FXCollections.observableArrayList();


/** This method gets Division ID.
 * The method gets the division ID from the division name.
 * @param divisionName String Division Name
 * @return Returns Division ID
 * */
    public static int getSelectedDivisionId(String divisionName) throws SQLException {
        String query = "select Division_ID from first_level_divisions where Division = '"+divisionName+"' ";

        try {
            ResultSet rs = DBConnection.conn.createStatement().executeQuery(query);
            if(rs.next()) {
                int id = Integer.parseInt(rs.getString(1));
                return id;
            }
            return 0;

        } catch (SQLException e) {
            System.out.println("Error getting division id from database" + e);
            throw e;
        }
    }


/** This method gets Division Name.
 * The method gets the division name from the Division ID.
 * @param id Division ID
 * @return String division name.
 * */
    public static String getDivisionName(int id) throws ClassNotFoundException, SQLException {
        String query = "select Division from first_level_divisions where Division_ID = "+id+"";

        try {
            ResultSet rs = DBConnection.conn.createStatement().executeQuery(query);
            if(rs.next()) {
                String name = rs.getString(1);
                return name;
            }

            return null;

        } catch (SQLException e) {
            System.out.println("Error getting division name from database" + e);
            e.printStackTrace();
            throw e;
        }
    }
}
