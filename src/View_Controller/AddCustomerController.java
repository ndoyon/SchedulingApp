package View_Controller;

import DAO.CustomerDB;
import DAO.DivisionDB;
import Util.DBConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;





/** This class is for adding customer controller. */
public class AddCustomerController implements Initializable {


        @FXML
        private ComboBox<String> customerDivisionComboBx;
        @FXML
        private ComboBox<String> customerCountryComboBox;


        @FXML
        private TextField customerIDField;
        @FXML
        private TextField customerNameField;
        @FXML
        private TextField customerAddressField;
        @FXML
        private TextField customerZipField;
        @FXML
        private TextField customerPhoneField;


        @FXML
        private Button cancelBtn;
        @FXML
        private Button addCustomerBtn;

        @FXML
        private Label countryAddressLbl;


    private List<String> countries = new ArrayList<>();
    private List<String> divisions = new ArrayList<>();


/** This is the initialize method.
 * This method loads the combo boxes and generates customer ID in field.
 * @param url url
 * @param rb resource bundle
 * */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        loadComboBoxes();
        generateCustomerID();

    }


 /** This method generates Customer ID.
  * The method generates Customer ID from DG and fills in the field.
  * */
    @FXML
    public void generateCustomerID() {

        this.customerIDField.clear();
        customerIDField.setText(CustomerDB.nextID().toString());

    }


/** This method is a mouse event for adding a customer.
 * The method un mouse click will validate and add the customer to the DB through provided fields.
 * @param event mouse click event
 * */
        @FXML
        public void addCustomerConfirm(MouseEvent event)throws  NullPointerException, SQLException {


            try {

                String Name = customerNameField.getText().trim();
                String Address = customerAddressField.getText().trim();
                String Division = customerDivisionComboBx.getValue();
                String Country = customerCountryComboBox.getValue();
                String Zip = customerZipField.getText().trim();
                String Phone = customerPhoneField.getText().trim();

                int DivisionID = DivisionDB.getSelectedDivisionId(Division);

                if (!Name.isEmpty() && !Address.isEmpty() && !Division.isEmpty() && !Country.isEmpty() && !Zip.isEmpty() && !Phone.isEmpty()) {

                    //Executes adding customer query

                    CustomerDB.addCustomer(Name, Address, Zip, Phone, DivisionID);
                    System.out.println("Add successful! Refresh page.");



                    //Pop-up confirming that a new customer has been added
                    customerAddAlert(Name);

                } else {
                    if (Name.isEmpty()) blankField("Name");
                    if (Address.isEmpty()) blankField("Address");
                    if (Division.isEmpty()) blankField("Division");
                    if (Country.isEmpty()) blankField("Country");
                    if (Zip.isEmpty()) blankField("Zip");
                    if (Phone.isEmpty()) blankField("Phone");
                }

                Parent parent = FXMLLoader.load(getClass().getResource("/View_Controller/Customer.fxml"));
                Scene scene = new Scene(parent);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();


            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }


    /** This method loads the combo boxes.
     * Combo boxes are loaded with Division and Country from the DB.
     * */
    @FXML
    public void loadComboBoxes() {
        this.customerCountryComboBox.getItems().clear();
        this.countries.clear();
        String rq1 = "SELECT Country FROM countries";
        String rq2 = "SELECT Division FROM first_level_divisions d";
        try {
            Statement st1 = DBConnection.conn.createStatement();
            Statement st2 = DBConnection.conn.createStatement();
            ResultSet rs1 = st1.executeQuery(rq1);
            ResultSet rs2 = st2.executeQuery(rq2);
            while (rs1.next()) {
                String country = rs1.getString("Country");
                this.customerCountryComboBox.getItems().add(country);
                this.countries.add(country);
            }
            while (rs2.next()) {
                String division = rs2.getString("Division");
                this.customerDivisionComboBx.getItems().add(division);
                this.divisions.add(division);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /** This method updates the combo boxes.
     * The update sets the division choices based off the country selected.
     * @param event mouse click for division selection.
     * */
    @FXML
    public void updateComboBox(MouseEvent event) {
        String selectedCountry = customerCountryComboBox.getValue();
        this.customerDivisionComboBx.getItems().removeAll(this.divisions);
        this.divisions.clear();
        String sql = "SELECT d.Division_ID, d.Division, d.COUNTRY_ID, c.Country_ID, c.Country FROM first_level_divisions d JOIN countries c ON (d.COUNTRY_ID = c.Country_ID) WHERE c.Country = '"+selectedCountry+"';";
        try {
            Statement st1 = DBConnection.conn.prepareStatement(sql);
            ResultSet rs1 = st1.executeQuery(sql);
            while (rs1.next()) {
                String division = rs1.getString("Division");
                this.customerDivisionComboBx.getItems().add(division);
                this.divisions.add(division);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    /** This method cancels the scene back to customer screen.
     * @param event changes scene on mouse click.
     * */
    @FXML
    void cancelToCustomer(MouseEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/View_Controller/Customer.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

        }

/** This method is an alert to display when field is empty.
 * @param s string representing which field is empty.
 * */
    public static void blankField(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.NONE);
        alert.setTitle("Error");
        alert.setHeaderText("Blank Field");
        alert.setContentText(s + " field was left blank.");
        alert.showAndWait();
    }


/** This method is an alert confirmation.
 * Confirms that a customer is being added.
 * @param s string for the customer being added.
 * */
    public static void customerAddAlert(String s) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Customer Added");
        alert.setHeaderText("Customer has been added!");
        alert.setContentText(s + " was added to Customers table.");
        alert.showAndWait();
    }
 }
