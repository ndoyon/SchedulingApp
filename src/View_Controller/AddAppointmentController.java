package View_Controller;

import DAO.AppointmentDB;
import DAO.ContactDB;
import DAO.CustomerDB;
import DAO.UserDB;
import Util.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;




/** This class is the adding appointment controller and scene. */
public class AddAppointmentController implements Initializable {


    @FXML
    private ComboBox<LocalTime> appStartTimeCmbBx;
    @FXML
    private ComboBox<String> appContactCmbBx;
    @FXML
    private ComboBox<String> appTypeCmbBx;
    @FXML
    private ComboBox<LocalTime> appEndTimeCmbBx;
    @FXML
    private ComboBox<String> customerNameComboBox;
    @FXML
    private ComboBox<String> userNameComboBox;


    @FXML
    private Button cancelAddApp;
    @FXML
    private Button saveAddApp;


    @FXML
    private TextField appTitleField;
    @FXML
    private TextField appIDField;
    @FXML
    private TextField appLocationField;


    @FXML
    private DatePicker appDate;


    @FXML
    private Label appIDLabel;

    @FXML
    private TextArea appDescriptionField;


    private List<String> customers = new ArrayList<>();
    private List<String> users = new ArrayList<>();
    private List<String> contacts = new ArrayList<>();
    static ObservableList<LocalTime> times = FXCollections.observableArrayList();
    static ObservableList<String> types = FXCollections.observableArrayList();


/** This method is the initialize method for displaying the combo boxes.
 * @param rb resource bundle
 * @param url url
 * */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        appStartTimeCmbBx.setItems(getTimes());
        appEndTimeCmbBx.setItems(getTimes());
        appTypeCmbBx.setItems(getTypes());

        loadApptComboBoxes();
        generateApptID();
    }


/** This method is for clicking add appointment button.
 * By clicking the button the appointment is added from the fields provided. This also performs validation and DB queries.
 * @param event mouseevent on click
 * */
    @FXML
    public void addAppClick(MouseEvent event) throws SQLException {


        int addId = Integer.parseInt(appIDField.getText());
        String addTitle = appTitleField.getText();
        String addDescription = appDescriptionField.getText();
        String addLocation = appLocationField.getText();
        String addType = appTypeCmbBx.getValue();
        String contact = appContactCmbBx.getValue();
        String user = userNameComboBox.getValue();
        String customer = customerNameComboBox.getValue();
        int checkAddTime = appStartTimeCmbBx.getSelectionModel().getSelectedIndex();
        int checkEndTime = appEndTimeCmbBx.getSelectionModel().getSelectedIndex();
        LocalDateTime start = LocalDateTime.of(appDate.getValue(), appStartTimeCmbBx.getValue());
        LocalDateTime end = LocalDateTime.of(appDate.getValue(), appEndTimeCmbBx.getValue());
        String tz = "America/New_York";
        int addUserID = UserDB.getUserId(user);
        int addCustID = CustomerDB.getID(customer);
        int addcontactID = ContactDB.getContactID(contact);

        try {
            if (addTitle.isEmpty() || addDescription.isEmpty() || addLocation.isEmpty() || addType.isEmpty() || contact.isEmpty() || user.isEmpty() || customer.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.NONE);
                alert.setTitle("Error!");
                alert.setHeaderText("Blank Fields");
                alert.setContentText("All fields mus be filled out.");
                alert.showAndWait();

            } else {

                if (checkEndTime == checkAddTime) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.initModality(Modality.NONE);
                    alert.setTitle("Error!");
                    alert.setHeaderText("Same times");
                    alert.setContentText("Appointment start and end times cannot be the same.");
                    alert.showAndWait();

                } else if (checkEndTime < checkAddTime) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.initModality(Modality.NONE);
                    alert.setTitle("Error!");
                    alert.setHeaderText("Time issue:");
                    alert.setContentText("Appointment start time must be earlier than the end time.");
                    alert.showAndWait();

                } else if (start.atZone(ZoneId.of(tz)).toLocalTime().isBefore(LocalTime.of(8, 0)) || end.atZone(ZoneId.of(tz)).toLocalTime().isAfter(LocalTime.of(21, 0))) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.initModality(Modality.NONE);
                    alert.setTitle("Appointment Error");
                    alert.setHeaderText("Appointment cannot be added!");
                    alert.setContentText(" Added Appointment is outside business hours.");
                    alert.showAndWait();
                    return;

                } else {

                    if (validateAppointment(start,end,addCustID,addId)) {
                        AppointmentDB.addAppointment(addTitle, addDescription, addLocation, addType, start, end, addCustID, addcontactID, addUserID);


                        apptAddAlert(addTitle);


                        Parent parent = FXMLLoader.load(getClass().getResource("/View_Controller/Appointment.fxml"));
                        Scene scene = new Scene(parent);
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.setScene(scene);
                        stage.show();

                        System.out.println("Add successful! Refresh page.");
                    } else {
                        if (!validateAppointment(start,end,addCustID,addId)) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.initModality(Modality.NONE);
                            alert.setTitle("Appointment Error");
                            alert.setHeaderText("Appointment cannot be added!");
                            alert.setContentText(" Added Appointment overlaps another  existing appointment!");
                            alert.showAndWait();
                        }


                    }
                }

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /** This method cancels adding a appointment.
     * By click the button the scene will change back to main appointment screen and alert.
     * @param event mouse event on click
     * */
        @FXML
       public  void cancelAddAppClick (MouseEvent event) throws IOException {
            int addId = Integer.parseInt(appIDField.getText());
            apptCancelAlert(addId);
            Parent parent = FXMLLoader.load(getClass().getResource("/View_Controller/Appointment.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }

        /** This method gets the times.
         * The method creates times in an observable array that are local. This includes hours and minutes
         * @return Returns the times
         * */
        public static ObservableList<LocalTime> getTimes () {
            try {
                times.removeAll(times);
                for (int i = 0; i < 24; i++) {
                    String hour;
                    if (i < 10) {
                        hour = "0" + i;
                    } else {
                        hour = Integer.toString(i);
                    }
                    times.add(LocalTime.parse(hour + ":00:00"));
                    times.add(LocalTime.parse(hour + ":15:00"));
                    times.add(LocalTime.parse(hour + ":30:00"));
                    times.add(LocalTime.parse(hour + ":45:00"));
                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

            return times;
        }


        /** This method gets the types of appointments.
         * The appointments are added into an observable string for the combo box.
         * @return Returns the types
         * */
        public static ObservableList<String> getTypes () {
            types.removeAll(types); //prevents types from copying themselves to the list
            types.add("Intro");
            types.add("Beginner");
            types.add("Moderate");
            types.add("Expert");
            types.add("Competitive");
            types.add("Team");
            return types;
        }

        /** This method generates appointment ID.
         * Generates appointment ID from the DB and places it into the appointment ID field.
         * */
        @FXML
        public void generateApptID () {

            this.appIDField.clear();
            appIDField.setText(AppointmentDB.nextID().toString());
        }


        /** This method loads the appointment combo boxes.
         * The method selects all information from the DB to display in each combo box.
         * */
        @FXML
        public void loadApptComboBoxes () {
            this.customerNameComboBox.getItems().clear();
            this.customers.clear();
            String rq1 = "SELECT Customer_Name FROM customers";
            String rq2 = "SELECT User_Name FROM users";
            String rq3 = "SELECT Contact_Name FROM contacts";
            try {
                Statement st1 = DBConnection.conn.createStatement();
                Statement st2 = DBConnection.conn.createStatement();
                Statement st3 = DBConnection.conn.createStatement();
                ResultSet rs1 = st1.executeQuery(rq1);
                ResultSet rs2 = st2.executeQuery(rq2);
                ResultSet rs3 = st3.executeQuery(rq3);

                while (rs1.next()) {
                    String customer = rs1.getString("Customer_Name");
                    this.customerNameComboBox.getItems().add(customer);
                    this.customers.add(customer);
                }
                while (rs2.next()) {
                    String user = rs2.getString("User_Name");
                    this.userNameComboBox.getItems().add(user);
                    this.users.add(user);
                }
                while (rs3.next()) {
                    String contact = rs3.getString("Contact_Name");
                    this.appContactCmbBx.getItems().add(contact);
                    this.contacts.add(contact);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        /** This method is an appointment cancel alert.
         * Method projects a cancel alert to confirm cancellation of appointment.
         * @param id integer id displayed
         * */
        public static void apptCancelAlert ( int id){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Appointment Canceled");
            alert.setHeaderText("Appointment will not be created created!");
            alert.setContentText("Appointment " + id + " will not be added to the table.");
            alert.showAndWait();
        }

        /** This method is an appointment add alert.
         * This alert is a confirmation for an appointment to be added.
         * @param s string for appointment name is displayed
         * */
        public static void apptAddAlert (String s){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Appointment Added");
            alert.setHeaderText("Appointment has been added!");
            alert.setContentText(s + " was added to Appointments table.");
            alert.showAndWait();
        }


    /** This method is a validation method for appointments.
     * The method checks for conflicting appointments with an existing customer as boolean.
     * @param appointmentId appointment id
     * @param customerId customer id
     *@param utcEnd UTC end time
     * @param utcStart UTC start time
     * @return Returns true of false for conflicting appointments.
     * */
    public boolean validateAppointment(LocalDateTime utcStart, LocalDateTime utcEnd, int customerId, int appointmentId) throws SQLException {

        ZonedDateTime UtcStart = ZonedDateTime.of(utcStart, ZoneId.of("UTC"));
        ZonedDateTime UtcEnd = ZonedDateTime.of(utcEnd, ZoneId.of("UTC"));


        // Check that proposed meeting hours do not conflict with customer's existing appointments
        String sq1 = "SELECT * FROM appointments WHERE Customer_ID = '"+customerId+"' AND Appointment_ID != '"+appointmentId+"';";
        Statement st1 = DBConnection.conn.createStatement();
        ResultSet qrs1 = st1.executeQuery(sq1);
        String start = "";
        String end = "";
        while (qrs1.next()) {
            start = qrs1.getString("Start");
            end = qrs1.getString("End");
            ZonedDateTime UTCCustomerStart = ZonedDateTime.of(Timestamp.valueOf(start).toLocalDateTime(), ZoneId.of("UTC"));
            ZonedDateTime UTCCustomerEnd = ZonedDateTime.of(Timestamp.valueOf(end).toLocalDateTime(), ZoneId.of("UTC"));
            if ( UtcStart.compareTo(UTCCustomerStart) < 1 && UtcEnd.compareTo(UTCCustomerEnd)  < 1) {

                return false;
            }
        }
        return true;
    }

}


