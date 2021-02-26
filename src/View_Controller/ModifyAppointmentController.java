package View_Controller;

import DAO.*;
import Model.Appointment;
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
import javafx.scene.control.cell.PropertyValueFactory;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TimeZone;



/** This class is for modifying appointments controller. */
public class ModifyAppointmentController implements Initializable {

    @FXML
    private TableView<Appointment> apptTableView;


    @FXML
    private ComboBox<String> appContactCmbBx;
    @FXML
    private ComboBox<String> appTypeCmbBx;
    @FXML
    private ComboBox<LocalTime> appStartTimeCmbBx;
    @FXML
    private ComboBox<LocalTime> appEndTimeCmbBx;
    @FXML
    private ComboBox<String> customerNameComboBox;
    @FXML
    private ComboBox<String> userNameComboBox;


    @FXML
    private TableColumn<Appointment, Integer> apptIDCol;
    @FXML
    private TableColumn<Appointment, String> apptTitleCol;
    @FXML
    private TableColumn<Appointment, String> apptDesCol;
    @FXML
    private TableColumn<Appointment, LocalDateTime> apptStartcol;
    @FXML
    private TableColumn<Appointment, String> apptLocationCol;
    @FXML
    private TableColumn<Appointment, LocalDateTime> apptEndCol;
    @FXML
    private TableColumn<Appointment, Integer> apptContactCol;
    @FXML
    private TableColumn<Appointment, Integer> apptCustCol;
    @FXML
    private TableColumn<Appointment, String> apptTypeCol;


    @FXML
    private TextField appLocationField;
    @FXML
    private TextField appTitleField;
    @FXML
    private TextField appIDField;


    @FXML
    private TextArea appDescriptionField;


    @FXML
    private DatePicker appDate;


    @FXML
    private Label appIDLabel;


    @FXML
    private Button saveAddApp;
    @FXML
    private Button cancelAddApp;

    private List<String> customers = new ArrayList<>();
    private List<String> users = new ArrayList<>();
    private List<String> contacts = new ArrayList<>();

    static ObservableList<LocalTime> times = FXCollections.observableArrayList();
    static ObservableList<String> types = FXCollections.observableArrayList();



    /** This method sets the data for the fields and combo boxes.
     * Provides all data from the table view and displays it in the fields and combo boxes.
     * */
    private void setData() {
        apptTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {


                Appointment appointment = apptTableView.getSelectionModel().getSelectedItem();

                String title = appointment.getAppTitle();
                String location = appointment.getAppLocation();
                String description = appointment.getAppDescription();
                int apptID = appointment.getApptID();
                LocalDateTime start = appointment.getStart();
                LocalDateTime end = appointment.getEnd();


                try {
                    int contactID = appointment.getContactID();
                    String contactName = ContactDB.getContactName(contactID);
                    int userID = appointment.getUserID();
                    String userName = UserDB.getUserName(userID);
                    int customerID = appointment.getCustomerID();
                    String customerName = CustomerDB.getCustomerName(customerID);

                    userNameComboBox.setValue(userName);
                    customerNameComboBox.setValue(customerName);
                    appContactCmbBx.setValue(contactName);
                    appTypeCmbBx.setValue(appointment.getAppType());
                    appStartTimeCmbBx.setValue(LocalTime.from(start));
                    appEndTimeCmbBx.setValue(LocalTime.from(end));



                } catch (SQLException e) {

                }
                appIDField.setText(String.valueOf(apptID));
                appTitleField.setText(title);
                appLocationField.setText(location);
                appDescriptionField.setText(description);
                appDate.setValue(appointment.getStart().atZone(ZoneId.of(TimeZone.getDefault().getID())).toLocalDate());

            }
        });

    }




    /** This method saves the appointment changes.
     * On mouse click the appointment will go through validation grab information from fields and add the changes in the db.
     * @param event on mouse click.
     * */
    @FXML
    void saveModApptClick(MouseEvent event) throws SQLException {

        int addId = Integer.parseInt(appIDField.getText());
        String addTitle = appTitleField.getText();
        String addDescription = appDescriptionField.getText();
        String addLocation = appLocationField.getText();
        String addType = appTypeCmbBx.getValue();
        String contact = appContactCmbBx.getValue();
        String user = userNameComboBox.getValue();
        String customer = customerNameComboBox.getValue();
        String editDate = appDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        int checkAddTime = appStartTimeCmbBx.getSelectionModel().getSelectedIndex();
        int checkEndTime = appEndTimeCmbBx.getSelectionModel().getSelectedIndex();
        String tz = "America/New_York";

        // String addDate = appDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDateTime start = LocalDateTime.of(appDate.getValue(),appStartTimeCmbBx.getValue());
        LocalDateTime end = LocalDateTime.of(appDate.getValue(),appEndTimeCmbBx.getValue());

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
                        AppointmentDB.updateAppointment(addId, addTitle, start, end, editDate, addDescription, addLocation, addType, addCustID, addcontactID, addUserID);


                        apptModAlert(addTitle);


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



/** This method changes scene back to the appointment screen.
 * @param event mouse click event to change scene
 * */
    @FXML
    void cancelAddAppClick(MouseEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/View_Controller/Appointment.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }


    /** This method gets the times for the combo box.
     * Grabs times from array list to display hour and minute intervals for combo box.
     * @return Returns the times
     * */
    public static ObservableList<LocalTime> getTimes() {
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


    /** This method loads the appointment combo box information.
     * Grabs information from the DB for the combo boxes.
     * */
    @FXML
    private void loadApptComboBoxes() {
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


    /** This method gets the types for combo boxes.
     * Provides an array list of types for the combo boxes.
     * @return types
     * */
    public static ObservableList<String> getTypes() {
        types.removeAll(types); //prevents types from copying themselves to the list
        types.add("Intro");
        types.add("Beginner");
        types.add("Moderate");
        types.add("Expert");
        types.add("Competitive");
        types.add("Team");
        return types;
    }


    /** This method is initialize for displaying combo boxes and tableview of appointments.
     * Method sets all data and displays tableview.
     * */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        apptDesCol.setCellValueFactory(new PropertyValueFactory<>("appDescription"));
        apptContactCol.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        apptCustCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        apptTitleCol.setCellValueFactory(new PropertyValueFactory<>("appTitle"));
        apptStartcol.setCellValueFactory(new PropertyValueFactory<>("start"));
        apptEndCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        apptLocationCol.setCellValueFactory(new PropertyValueFactory<>("appLocation"));
        apptTypeCol.setCellValueFactory(new PropertyValueFactory<>("appType"));
        apptIDCol.setCellValueFactory(new PropertyValueFactory<>("apptID"));


        apptTableView.setItems(AppointmentDB.selectAllAppts());
        appStartTimeCmbBx.setItems(getTimes());
        appEndTimeCmbBx.setItems(getTimes());
        appTypeCmbBx.setItems(getTypes());
        setData();
        loadApptComboBoxes();
    }


    /** This method is a validation method for appointments.
     * The method checks for conflicting appointments with an existing customer as boolean.
     * @param appointmentId appointment id
     * @param customerId customer id
     *@param utcEnd UTC end time
     * @param utcStart UTC start time
     * @return Returns true of false for conflicting appointments.
     * */
    private boolean validateAppointment(LocalDateTime utcStart, LocalDateTime utcEnd, int customerId, int appointmentId) throws SQLException {

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


    /** This method is an alert for modify appointments.
     * Method is an alert that confirms that an appointment was added.
     * @param s string value of appointment.
     * */
    public static void apptModAlert (String s){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Appointment Added");
        alert.setHeaderText("Appointment has been added!");
        alert.setContentText(s + " was added to Appointments table.");
        alert.showAndWait();
        }
}
