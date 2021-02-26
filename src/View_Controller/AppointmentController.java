package View_Controller;

import DAO.AppointmentDB;
import Model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.util.ResourceBundle;




/** This class is for the main appointment controller page. */
public class AppointmentController implements Initializable {

    @FXML
    private TableView<Appointment> appTableView;


    @FXML
    private Button exitToMain;
    @FXML
    private Button addAppBtn;
    @FXML
    private Button editAppBtn;
    @FXML
    private Button deleteApptBtn;


    @FXML
    private TableColumn<Appointment, String> appDescription;
    @FXML
    private TableColumn<Appointment, Integer> appContact;
    @FXML
    private TableColumn<Appointment, Integer> appCustID;
    @FXML
    private TableColumn<Appointment, String> appTitle;
    @FXML
    private TableColumn<Appointment, LocalDateTime> appStart;
    @FXML
    private TableColumn<Appointment, String> appLocation;
    @FXML
    private TableColumn<Appointment, String> appType;
    @FXML
    private TableColumn<Appointment, Integer> appID;
    @FXML
    private TableColumn<Appointment, LocalDateTime> appEnd;


    @FXML
    private RadioButton appWeeklyBtn;
    @FXML
    private RadioButton appMonthlyBtn;
    @FXML
    private RadioButton allApptBtn;

    private ToggleGroup ApptToggleGroup;



    /** This method when clicked changes scene to adding an appointment.
     * @param event mouse click event to change scene.
     * */
    @FXML
    void addAppBtnClick(MouseEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/View_Controller/AddAppointment.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }

    /** This method when clicked changes scene to modifying an appointment.
     * @param event mouse click event to change scene.
     * */
    @FXML
    void modAppBtnClick(MouseEvent event) throws IOException {

        Parent parent = FXMLLoader.load(getClass().getResource("/View_Controller/ModifyAppointment.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }

    /** This method when clicked changes scene to the main screen.
     * @param event mouse click event to change scene.
     **/
    @FXML
    void exitBtnClick(MouseEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }

/** This method when clicked deletes an appointment.
 * The method deletes an appointment from the tableview when clicked and displays a confirmation alert.
 * @param event deletion on mouse click.
 * */
    @FXML
    void deleteApptClick(MouseEvent event) {
        Appointment selectedAppointment = appTableView.getSelectionModel().getSelectedItem();
        int id = selectedAppointment.getApptID();

        if (selectedAppointment != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Are you sure you want to delete Appointment: " + id + " scheduled for " + selectedAppointment.getStart() + "?");
            alert.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> {
                                AppointmentDB.deleteAppointment(selectedAppointment);

                            }
                    );
        } else  {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection was made");
            alert.setHeaderText("No Appointment selected for Deletion");
            alert.setContentText("Please select an Appointment in the Table to delete");
            alert.showAndWait();
        }

    }





/** This method refreshes the table view. */
    public void refreshTable(){

        appTableView.refresh();

    }


    /** This method generates a table view from observable list.
     * Displays tableview view and refreshes the table.
     * @param view observable list.
     * */
    private void generateTable(ObservableList<Appointment> view)
    {
        appTableView.setItems(view);
        appTableView.refresh();
    }

    /** This method is to change the radio selection on click.
     * When each radio button is clicked the tableview will change views.
     * */
    public void radioBtnChange() {
        ObservableList<Appointment> filterAppointments = FXCollections.observableArrayList();

        if(this.ApptToggleGroup.getSelectedToggle().equals(this.allApptBtn))
            for (Appointment a : AppointmentDB.selectAllAppts())
                filterAppointments.add(a);
        if(this.ApptToggleGroup.getSelectedToggle().equals(this.appMonthlyBtn))
            for (Appointment a : AppointmentDB.selectAllAppts())
                if (a.getStart().atZone(ZoneId.systemDefault()).toLocalDate().getMonthValue() == LocalDate.now().getMonthValue())
                    filterAppointments.add(a);
                generateTable(filterAppointments);
        if(this.ApptToggleGroup.getSelectedToggle().equals(this.appWeeklyBtn))
            for (Appointment a : AppointmentDB.selectAllAppts())
            if (!a.getStart().isBefore(LocalDateTime.now()) && a.getStart().isBefore(LocalDateTime.now().plus(Duration.ofDays(7))))
                filterAppointments.add(a);
                generateTable(filterAppointments);
    }


    /** This is an initialize method for displaying.
     * Displays the tableview and sets toggle group for radio buttons.
     * @param url url
     * @param rb resource bundle
     * */
    @Override
    public void initialize(URL url, ResourceBundle rb) {



        appDescription.setCellValueFactory(new PropertyValueFactory<>("appDescription"));
        appContact.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        appCustID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        appTitle.setCellValueFactory(new PropertyValueFactory<>("appTitle"));
        appStart.setCellValueFactory(new PropertyValueFactory<>("start"));
        appEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
        appLocation.setCellValueFactory(new PropertyValueFactory<>("appLocation"));
        appType.setCellValueFactory(new PropertyValueFactory<>("appType"));
        appID.setCellValueFactory(new PropertyValueFactory<>("apptID"));

        refreshTable();
        appTableView.setItems(AppointmentDB.selectAllAppts());

        ApptToggleGroup = new ToggleGroup();
        this.appMonthlyBtn.setToggleGroup(ApptToggleGroup);
        this.appWeeklyBtn.setToggleGroup(ApptToggleGroup);
        this.allApptBtn.setToggleGroup(ApptToggleGroup);


    }
}
