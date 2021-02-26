package View_Controller;

import Util.DBConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;






/** This class is for the main screen controller. */
public class MainScreenController {

    @FXML
    private Button reportBtn;
    @FXML
    private Button logOutBtn;
    @FXML
    private Button customerBtn;
    @FXML
    private Button appBtn;



    /** This method changes scene to the customer screen.
     * @param event mouse click changes scene.
     * */
    @FXML
    void customerBtnClick(MouseEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/View_Controller/Customer.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }

    /** This method changes scene to the Appointment screen.
     * @param event mouse click changes scene.
     * */
    @FXML
    void appBtnClick(MouseEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/View_Controller/Appointment.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }

    /** This method changes scene to the Reports screen.
     * @param event mouse click changes scene.
     * */
    @FXML
    void reportBtnClick(MouseEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/View_Controller/Report.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }


    /** This method alerts and closes application.
     * On click the application will alert and lose connection to DB and application.
     * @param event on mouse click close app.
     * */
    @FXML
    void logOutBtnClick(MouseEvent event){

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Required");
        alert.setHeaderText("Confirm Exit");
        alert.setContentText("Are you sure you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            DBConnection.closeConnection();
            System.out.println("Program Exit.");
            System.exit(0);
        }
        else{
            System.out.println("Exit canceled.");
        }
    }
}