package DAO;

import Model.Appointment;
import Util.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.time.*;


/** This Class creates a database CRUD for appointments. */
public class AppointmentDB {

    static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    static ObservableList<Appointment> contactSchedule = FXCollections.observableArrayList();

/** This method gets all Appointments from Observable List Appointments.
 * @return Returns allAppointments observable Array list.
 * */

    public static ObservableList<Appointment> getAllAppointments() {
        return allAppointments;
    }

/** This method selects all Appointments.
 * The method grabs all appointments from mySQL database and creates new appointment.
 * @return Returns allappointment Array with new appointment.
 */
    public static ObservableList<Appointment> selectAllAppts() {
        allAppointments.clear();


        try {

            ResultSet rs = DBConnection.conn.createStatement().executeQuery("SELECT Appointment_ID, title, description, location, type, start, end, Customer_ID, User_ID, Contact_ID FROM appointments");

            while (rs.next()) {
                Appointment appointment = new Appointment(
                        rs.getInt("appointment_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("location"),
                        rs.getString("type"),
                        rs.getTimestamp("start").toLocalDateTime(),
                        rs.getTimestamp("end").toLocalDateTime(),
                        rs.getInt("customer_id"),
                        rs.getInt("user_id"),
                        rs.getInt("contact_id"));

                allAppointments.add(appointment);

            }
            return allAppointments;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
    }


/** This method grabs next Appointment ID.
 * This method will grab the next ID available in the database to use in adding an appointment.
 * @return Returns appointment ID.
 * */
    public static Integer nextID() {
        Integer appointmentID = 1;
        try {
            Statement stmt = DBConnection.conn.createStatement();
            String query = "SELECT Appointment_ID FROM appointments ORDER BY Appointment_ID";
            ResultSet rs = stmt.executeQuery(query);


            while (rs.next()) {
                if (rs.getInt("Appointment_ID") == appointmentID) {
                    appointmentID++;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return appointmentID;

    }

/** This method adds appointments to the DB.
 * The method will add appointments to the mySQL database and add the appointment to the observable list.
 * @param appType type of appointment
 * @param appDescription appointment description
 * @param appLocation appointment Location
 * @param appTitle appointment title
 * @param contactID Contact ID tied to the appointment
 * @param customerID Customer ID tied to the appointment
 * @param endTime Local time ending of appointment
 * @param startTime Local time of start of appointment
 * @param userID User ID tied to appointment
 * */
    public static void addAppointment(String appTitle, String appDescription, String appLocation, String appType, LocalDateTime startTime, LocalDateTime endTime, int customerID, int userID, int contactID){
        int appointmentID = nextID();

        try {
            PreparedStatement ps = DBConnection.conn.prepareStatement(
                    "INSERT INTO appointments (appointment_id, title, description, location, type, start, end, create_date, created_by, last_update, last_updated_by, Customer_ID, User_ID, Contact_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            Appointment appointment = new Appointment();
            ps.setString(1, String.valueOf(appointmentID));
            ps.setString(2, appTitle);
            ps.setString(3, appDescription);
            ps.setString(4, appLocation);
            ps.setString(5, appType);
            ps.setTimestamp(6, Timestamp.valueOf(startTime));
            ps.setTimestamp(7, Timestamp.valueOf(endTime));
            ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(9, UserDB.getCurrentUser().getUserName());
            ps.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(11, UserDB.getCurrentUser().getUserName());
            ps.setInt(12, customerID);
            ps.setInt(13, userID);
            ps.setInt(14, contactID);
            ps.executeUpdate();

            allAppointments.add(appointment);


        } catch (SQLException exception) {
            System.out.println(exception);
        }
    }

/** This method updates an appointment to the DB.
 * Updates an appointment based off the appointment ID.
 * @param appID integer for application ID
 * @param appType type of appointment
 * @param appDescription appointment description
 * @param appLocation appointment Location
 * @param appTitle appointment title
 * @param contactID Contact ID tied to the appointment
 * @param customerID Customer ID tied to the appointment
 * @param endTime Local time ending of appointment
 * @param startTime Local time of start of appointment
 * @param userID User ID tied to appointment
 * @param date local date tied to appointment
 * @return Returns Application ID
 * */
    public static Integer updateAppointment(int appID, String appTitle, LocalDateTime startTime, LocalDateTime endTime, String date, String appType, String appDescription, String appLocation, int contactID, int customerID, int userID) {

        try {
            PreparedStatement ps = DBConnection.conn.prepareStatement("UPDATE appointments SET " +
                    "title = ?, " +
                    "description = ?, " +
                    "location = ?, " +
                    "type = ?, " +
                    "start = ?, " +
                    "end = ?, " +
                    "last_update = ?, " +
                    "last_updated_by = ?, " +
                    "customer_id = ?, " +
                    "user_id = ?, " +
                    "contact_id = ? " +
                    "WHERE appointment_id = ?");

            ps.setString(1, appTitle);
            ps.setString(2, appDescription);
            ps.setString(3, appLocation);
            ps.setString(4, appType);
            ps.setTimestamp(5, Timestamp.valueOf(startTime));
            ps.setTimestamp(6, Timestamp.valueOf(endTime));
            ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(8, String.valueOf(UserDB.getCurrentUser()));
            ps.setInt(9, customerID);
            ps.setInt(10, userID);
            ps.setInt(11, contactID);
            ps.setInt(12, appID);

            ps.executeUpdate();

            return appID;


        } catch (SQLException e) {
            System.out.println("No Change! SQLException" + e.getMessage());
            return null;
        }
    }


/** This method deletes an appointment from the DB.
 * The method deletes the appointment by grabbing the appointment ID from observable list.
 * @param appointment is what gets removed by appointment ID*/
    public static void deleteAppointment(Appointment appointment) {
        try {
            String query = "DELETE FROM appointments " +
                    "WHERE Appointment_ID = ?";

            PreparedStatement stmt = DBConnection.conn.prepareStatement(query);
            stmt.setInt(1, appointment.getApptID());
            stmt.executeUpdate();

            allAppointments.remove(appointment);

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}





