package View_Controller;

import DAO.CustomerDB;
import Model.Customer;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;



/** This class is to display the customer controller. */
public class CustomerController implements Initializable {

    @FXML
    private TableView<Customer> customerTableView;
    @FXML
    private TableColumn<Customer, String> customerAddress;
    @FXML
    private TableColumn<Customer, String> customerPhone;
    @FXML
    private TableColumn<Customer, Integer> customerID;
    @FXML
    private TableColumn<Customer, String> customerPostal;
    @FXML
    private TableColumn<Customer, String> customerName;
    @FXML
    private TableColumn<Customer, Integer> customerDivision;


    @FXML
    private Button deleteCustomerBtn;
    @FXML
    private Button modifyCustomerBtn;
    @FXML
    private Button addCustomerBtn;
    @FXML
    private Button exitCustomerBtn;





/** This method changes scene to adding a customer.
 * @param event changes scene on mouse click.
 * */
    @FXML
    void addCustomerClick(MouseEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/View_Controller/AddCustomer.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }

/** This method changes scene to modifying a customer.
 * @param event changes scene on mouse click.
 * */
    @FXML
    void modifyCustomerClick(MouseEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/View_Controller/ModifyCustomer.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }


    /** This method is a mouse click for deleting a customer.
     * When mouse is clicked a confirmation alert will prompt and delete the customer, only if they do not have any appointments.
     * @param event delete and confirmation on mouse click.
     * */
    @FXML
    void deleteCustomerClick(MouseEvent event) {

        Customer customer = customerTableView.getSelectionModel().getSelectedItem();
        int id = customer.getCustomerID();
        String name = customer.getCustomerName();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Customer deletion, ok?");
        alert.setContentText("This will delete customer: " + name + " and clear all appointments.");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {

            if(CustomerDB.deleteCustomer(id)) {
                CustomerDB.getAllCustomers().remove(customer);
                System.out.println("Return deletion");
            }

            Alert alertz = new Alert(Alert.AlertType.INFORMATION, "customer has been successfully deleted!", ButtonType.OK);
            alertz.showAndWait().filter(response -> response == ButtonType.OK);

            System.out.println("Delete successful! Refresh page.");

        }else {
            Alert alerts = new Alert(Alert.AlertType.WARNING, "Cannot delete customer with upcoming appointments.", ButtonType.OK);
            alerts.showAndWait().filter(response -> response == ButtonType.OK);
        }
        }



/** This method is to change scenes to the main screen.
 * @param event changes scene on mouse click.
 * */
    @FXML
    void exitCustomerClick(MouseEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }

    /** this method refreshes the tableview. */
    public void refreshTable(){

        customerTableView.refresh();

    }


    /** This method is an initialize for displaying customer tableview.
     * Displays customer table view and refreshes.
     * @param url url
     * @param resourceBundle resourcebundle
     * */
    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {




        customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerAddress.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        customerPostal.setCellValueFactory(new PropertyValueFactory<>("customerPostal"));
        customerDivision.setCellValueFactory(new PropertyValueFactory<>("divisionID"));
        customerPhone.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));


        refreshTable();

        customerTableView.setItems(CustomerDB.selectAllCustomers());


    }
}


