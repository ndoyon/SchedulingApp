package View_Controller;

import DAO.AppointmentDB;
import DAO.UserDB;
import Model.Appointment;
import Util.DBConnection;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.ResourceBundle;



/** This class is for the login controller. */
public class LoginController implements Initializable {

    @FXML
    private TextField passwordLogin;
    @FXML
    private TextField usernameLogin;

    @FXML
    private Button loginButton;
    @FXML
    private Button exitProgram;

    @FXML
    private Label countryLabel;
    @FXML
    private Label locationLabel;
    @FXML
    private Label titleLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label passwordLabel;



    Locale locale = Locale.getDefault();

    private ResourceBundle rb;

    private String errorTitle;
    private String errorText;



    /** This method is initialize and displays labels and text.
     * The scene shows labels and text with correct locale and language bundle based off system.
     * @param url url
     * @param resourceBundle resource bundle*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.locale = Locale.getDefault();
        rb = ResourceBundle.getBundle("View_Controller/Nat", this.locale);

        countryLabel.setText(locale.getDisplayCountry());
        titleLabel.setText(rb.getString("login"));
        loginButton.setText(rb.getString("loginButton"));
        exitProgram.setText(rb.getString("exit"));
        usernameLabel.setText(rb.getString("username"));
        usernameLogin.setPromptText(rb.getString("username"));
        passwordLabel.setText(rb.getString("password"));
        passwordLogin.setPromptText(rb.getString("password"));
        locationLabel.setText(rb.getString("location"));
        errorTitle = rb.getString("errorTitle");
        errorText = rb.getString("errorText");
    }


    public interface grabAllAppointments {
        String getMessage (String s);
    }

    /** This method attempts log in on mouse click.
     * This method utilizes a lambda function method to check for 15 minute warning on appointments and login.
     * @param event mouse click event for logging in.
     * */
    @FXML
    public void loginClicked(MouseEvent event) throws IOException {

        String userName = usernameLogin.getText();
        String password = passwordLogin.getText();

        boolean verifiedUser = UserDB.loginAttempt(userName, password);

        if (verifiedUser) {
            checkAppointments();

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();
        }

        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(errorTitle);
            alert.setContentText(errorText);
            alert.showAndWait();
        }

    }


/** This method closes the DB connection and application.
* @param event mouseclick closes app.
* */
  @FXML
  public void exitClicked(MouseEvent event) {
      DBConnection.closeConnection();
      System.exit(0);
        }


     /** This is a method created with lambda expression.
      * This method and lambda expression are used to conveniently check all appointments for appointment times in the 15 minute window.
      * if an appointment is within 15 minutes of login, user will be alerted.
      * */
    private void checkAppointments() {
        ObservableList<Appointment> appointments = AppointmentDB.getAllAppointments();

        //Check if any upcoming appointments are within the 15 minute login window using a Lambda function

        LocalDateTime now = LocalDateTime.now();
        appointments.forEach( (appointment -> {
            long timeDifference = ChronoUnit.MINUTES.between(now, appointment.getStart());
            boolean hasBeenAlerted = false;
            if(timeDifference >= 0 && timeDifference <= 15 && !hasBeenAlerted){
                hasBeenAlerted = true;
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("There is an Upcoming Appointment");
                alert.setContentText("Appointment: " + appointment.getApptID() + " starts at " + appointment.getStart());
                alert.showAndWait();
            }
        }));
    }

}