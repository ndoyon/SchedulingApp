package View_Controller;

import DAO.CountryDB;
import DAO.CustomerDB;
import DAO.DivisionDB;
import Model.Customer;
import Util.DBConnection;
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
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;



/** This class is for modifying customers controller. */
public class ModifyCustomerController implements Initializable {

    @FXML
    private TableView<Customer> customerTableView;


    @FXML
    private Button modCustomerBtn;
    @FXML
    private Button cancelBtn;


    @FXML
    private ComboBox<String> customerDivisionComboBx;
    @FXML
    private ComboBox<String> customerCountryComboBox;


    @FXML
    private TableColumn<Customer, String> customerAddress;
    @FXML
    private TableColumn<Customer, String> customerPhone;
    @FXML
    private TableColumn<Customer, String> customerName;
    @FXML
    private TableColumn<Customer, Integer> customerDivision;
    @FXML
    private TableColumn<Customer, String> customerPostal;
    @FXML
    private TableColumn<Customer, Integer> customerID;


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
    private Label countryAddressLbl;


    private List<String> countries = new ArrayList<>();
    private List<String> divisions = new ArrayList<>();




    /** This method sets the data in the appropriate fields.
     * On tableview selection will prefill the data int the fields.
     * */
    private void setData() {
        customerTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                modCustomerBtn.setDisable(false);


                Customer customer = customerTableView.getSelectionModel().getSelectedItem();

                int id = customer.getCustomerID();
                String name = customer.getCustomerName();
                String address = customer.getCustomerAddress();
                String zip = customer.getCustomerPostal();
                String phone = customer.getCustomerPhone();


                // get division value where division_id = id AND SET
                int divisionId = customer.getDivisionID();

                try {
                    String divisionName = DivisionDB.getDivisionName(divisionId);
                    int countryId = CountryDB.getCountryIdFromDivisionId(divisionId);
                    String countryName = CountryDB.getCountryName(countryId);

                    customerDivisionComboBx.setValue(divisionName);
                    customerCountryComboBox.setValue(countryName);


                } catch (SQLException | ClassNotFoundException e) {

                }

                customerIDField.setText(String.valueOf(id));
                customerNameField.setText(name);
                customerAddressField.setText(address);
                customerZipField.setText(zip);
                customerPhoneField.setText(phone);
            }
        });

    }



    /** This method loads the combo boxes.
     * Combo boxes are loaded with Division and Country from the DB.
     * */
    @FXML
    private void loadComboBoxes() {
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

        }
    }


    /** This method updates the combo boxes.
     * The update sets the division choices based off the country selected.
     * @param event mouse click for division selection.
     * */
    @FXML
    private void updateComboBox(MouseEvent event) {
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




    /** This method changes the scene back to the main customer screen.
     * @param event mouseclick changes scenes.
     * */
    @FXML
    void cancelToCustomer(MouseEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/View_Controller/Customer.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }


    /** This method performs the modification of customer to the DB.
     * On mouse click the customer is validated and the customer information is updated into the DB.
     * @param event mouseclick to update the customer.
     * */
    @FXML
    void modCustomerConfirm(MouseEvent event) throws SQLException, IOException {

        try {
            String name = customerNameField.getText();
            String address = customerAddressField.getText();
            String zip = customerZipField.getText();
            String phone = customerPhoneField.getText();
            String country = customerCountryComboBox.getValue();
            Customer customer = customerTableView.getSelectionModel().getSelectedItem();
            int customerID = customer.getCustomerID();
            String customerDiv = customerDivisionComboBx.getValue();
            int divisionId = DivisionDB.getSelectedDivisionId(customerDiv);




            boolean isValidCustomer = isCustomerValid(name, address, country, zip, phone, customerDiv);

            if (isValidCustomer) {
                CustomerDB.updateCustomer(customerID, name, address, zip, phone, divisionId);

                ObservableList<Customer> customerList = CustomerDB.getAllCustomers();

                customerTableView.setItems(customerList);

                Parent parent = FXMLLoader.load(getClass().getResource("/View_Controller/Customer.fxml"));
                Scene scene = new Scene(parent);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();


            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Unable to update customer.", ButtonType.OK);
                alert.showAndWait().filter(response -> response == ButtonType.OK);
            }


        } catch (SQLException e) {
            System.out.println("Error occurred while updating customer data: " + e);
            e.printStackTrace();
            throw e;
        }



    }


    /** This method is a boolean validation for customer.
     * Checks if customer fields are filled.
     * @return true or false
     * */
    private boolean isCustomerValid(String name, String address, String country, String zip, String phone, String customerDiv) {
        if (name.isBlank() || address.isBlank() || country.isBlank() || zip.isBlank() || phone.isBlank() || customerDiv.isBlank()) {
            return false;
        }
        return true;
    }


    /** This method is initialize displaying customer tableview.
     * This method loads combo boxes and sets the tableview and data.
     * @param url url
     * @param rb resource bundle
     * */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerAddress.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        customerPostal.setCellValueFactory(new PropertyValueFactory<>("customerPostal"));
        customerDivision.setCellValueFactory(new PropertyValueFactory<>("divisionID"));
        customerPhone.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));

        loadComboBoxes();

        customerTableView.setItems(CustomerDB.selectAllCustomers());

        setData();
    }

}



