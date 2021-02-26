package Model;


/** This is the user class of the application. */
public class User {

    private int userID;
    private String userName;
    private String password;

    /** This is the user class constructor. */
    public User(int userID, String userName, String password) {
        this.userID = userID;
        this.userName = userName;
        this.password = password;
    }


    /** This is the user classes additional constructor with no params. */
    public User() {

    }


/** This method gets the User id.
 * @return Returns integer user id
 * */
    public  int getUserID() {
        return userID;
    }

    /** This method sets the user id.
     * @param userID integer user id
     * */
    public void setUserID(int userID) {
        this.userID = userID;
    }

/** This method gets the user name.
 * @return Returns the string user name
 * */
    public String getUserName() {
        return userName;
    }

    /** This method sets the username.
     * @param userName string user name
     * */
    public void setUserName(String userName) {
        this.userName = userName;
    }

/** This method gets the password.
 * @return Returns the user password
 * */
    public String getPassword() {
        return password;
    }

    /** This method sets the password.
     * @param password string password
     * */
    public void setPassword(String password) {
        this.password = password;
    }
}