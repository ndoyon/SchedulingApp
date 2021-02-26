package DAO;


import Model.User;
import Util.DBConnection;
import Util.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;






/** This class is for CRUD for users in the DB. */
public class UserDB {

    private static final ObservableList<User> userList = FXCollections.observableArrayList();

    private static User currentUser;

    public static User getCurrentUser() {
        return currentUser;
    }


/** This method gets the user ID.
 * The method gets the user id from the String User name.
 * @param name String for username
 * @return Returns User ID
 * */
    public static Integer getUserId(String name) throws SQLException {
        try {
            ResultSet rs = DBConnection.conn.createStatement().executeQuery("select User_ID from users where User_Name = '" + name + "'");
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


/** This method gets the username from the DB.
 * The method gets the username from the DB using the User ID.
 * @param id User ID
 * @return Returns string user name
 * */
    public static String getUserName(int id) throws SQLException {
        String query = "select User_Name from users where User_ID = " + id + "";

        try {
            ResultSet rs = DBConnection.conn.createStatement().executeQuery(query);
            if (rs.next()) {
                String name = rs.getString(1);
                return name;
            }

            return null;

        } catch (SQLException e) {
            System.out.println("Error getting user name from database" + e);
            e.printStackTrace();
            throw e;
        }
    }

 /** This method is for logging in.
  * The method attempts login to the application, also runs logger for log.txt.
  * @param Username User name
  * @param Password users password
  * @return Returns true or false for login attempt.
  * */
    public static Boolean loginAttempt(String Username, String Password) {
        try {
            Statement statement = DBConnection.conn.createStatement();
            String loginCheck = "SELECT * FROM users WHERE User_Name='" + Username + "' AND Password='" + Password + "'";
            ResultSet rs = statement.executeQuery(loginCheck);
            if (rs.next()) {
                currentUser = new User();
                currentUser.setUserName(rs.getString("User_Name"));
                Logger.logs(Username, true);
                statement.close();

                return Boolean.TRUE;

            } else {
                Logger.logs(Username, false);
                return Boolean.FALSE;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


/** This method gets all users.
 * The method gets the user information from the DB.
 * @return Returns null
 * */
    public static Object allUserList(){
        {
            try {
                Statement statement = DBConnection.conn.createStatement();
                ResultSet rs = statement.executeQuery("SELECT * FROM users");
                while (rs.next()) {
                    int id = Integer.parseInt(rs.getString("user_id").trim());
                    String name = rs.getString("user_name").trim();
                    String password = rs.getString("password").trim();
                    userList.add(new User(id, name, password));
                }
            } catch (SQLException | NumberFormatException exception) {
                System.out.println(exception);
            }
        }
        return null;
    }
}
