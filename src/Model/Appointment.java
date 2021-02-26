package Model;


import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;

/** This class creates an Appointment. */
public class Appointment {
    private int apptID;
    private int customerID;
    private int userID;
    private int contactID;
    private String appTitle;
    private String appDescription;
    private String appLocation;
    private String appType;
    private String username;
    private String contact;
    private LocalDateTime start;
    private LocalDateTime end;
    private LocalDate date;

/** This is the  appointment class constructor. */
    public Appointment(int apptID, String appTitle, String appDescription, String appLocation, String appType, LocalDateTime start, LocalDateTime end, int customerID, int userID, int contactID) {
        this.apptID = apptID;
        this.appTitle = appTitle;
        this.appDescription = appDescription;
        this.appLocation = appLocation;
        this.appType = appType;
        this.start = start;
        this.end = end;
        this.date = date;
        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;


    }

    public Appointment() {

    }

/** This method gets the appointment ID.
 *@return Returns appointment ID
 * */
    public int getApptID() {
        return apptID;
    }

/** This method sets the appointment ID.
 * @param apptID integer appointment ID
 * */
    public void setApptID(int apptID) {
        this.apptID = apptID;
    }

/** This method gets the Customer ID
 * @return Customer ID
 * */
    public int getCustomerID() {
        return customerID;
    }

/** this method sets the Customer ID.
 *@param customerID  integer customer id.
 * */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

/** This method gets the user id.
 * @return Returns user Id
 * */
    public int getUserID() {
        return userID;
    }

 /** this method sets the user ID.
  * @param userID integer user id
  * */
    public void setUserID(int userID) {
        this.userID = userID;
    }

/** this method gets the contact id.
 * @return contact id
 * */
    public int getContactID() {
        return contactID;
    }

    /** this method sets the Contact ID.
     * @param contactID integer contact ID
     * */
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

/** this method gets the application title
 * @return appointment title
 * */
    public String getAppTitle() {
        return appTitle;
    }

    /** this method sets the appointment title.
     * @param appTitle string appTitle
     * */
    public void setAppTitle(String appTitle) {
        this.appTitle = appTitle;
    }

/** This method gets the appointment description.
 * @return appointment Description
 * */
    public String getAppDescription() {
        return appDescription;
    }

    /** this method sets the appointment description.
     * @param appDescription string appointment description
     * */
    public void setAppDescription(String appDescription) {
        this.appDescription = appDescription;
    }

/** This method gets the appointment location.
 * @return string appointment location
 * */
    public String getAppLocation() {
        return appLocation;
    }

    /** this method sets the appointment location.
     * @param appLocation string appointment location
     * */
    public void setAppLocation(String appLocation) {
        this.appLocation = appLocation;
    }

/** This method gets the appointment type.
 * @return string appointment type
 * */
    public String getAppType() {
        return appType;
    }

    /** this method sets the appointment type.
     * @param appType string appointment type
     * */
    public void setAppType(String appType) {
        this.appType = appType;
    }

/** This method gets the appointment username.
 * @return Returns username
 * */
    public String getUsername() {
        return username;
    }

    /** this method sets the appointment username.
     * @param username string appointment username
     * */
    public void setUsername(String username) {
        this.username = username;
    }

/** This method gets the appointment contact.
 * @return Returns the appointment Contact
 * */
    public String getContact() {
        return contact;
    }

    /** this method sets the appointment contact.
     * @param contact string appointment contact
     * */
    public void setContact(String contact) {
        this.contact = contact;
    }

/** This method gets the localdatetime of Start.
 *@return returns localdatetime start
 * */
    public LocalDateTime getStart() {
        return start;
    }

    /** this method sets the appointment start.
     * @param start LocalDateTime appointment start
     * */
    public void setStart(LocalDateTime start) {
        this.start = start;
    }

/** This method gets the appointment end time
 * @return Returns localdatetime of end
 * */
    public LocalDateTime getEnd() {
        return end;
    }

    /** this method sets the appointment end.
     * @param end LocalDateTime appointment end
     * */
    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

/** This method gets the local date of the appointment.
 * @return Returns the date locally
 * */
    public LocalDate getDate() {
        return date;
    }

    /** this method sets the appointment date.
     * @param date string appointment date
     * */
    public void setDate(LocalDate date) {
        this.date = date;
    }
}