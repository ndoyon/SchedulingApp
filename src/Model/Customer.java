package Model;


/** This is the customer class for the application. */
public class Customer {

    private int customerID;
    private int divisionID;
    private String customerName;
    private String customerAddress;
    private String customerPostal;
    private String customerPhone;

    /** This is the customer classes constructor with parameters. */
    public Customer(int customerID, int divisionID, String customerName, String customerAddress, String customerPostal, String customerPhone) {
        this.customerID = customerID;
        this.divisionID = divisionID;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerPostal = customerPostal;
        this.customerPhone = customerPhone;
    }

    /** This is the customer classes constructor without parameters. */
    public Customer() {

    }



/** This method gets the customer ID.
 *@return Returns customer ID
 * */
    public int getCustomerID() {
        return customerID;
    }

    /** This method sets the customer ID.
     * @param customerID integer customer id
     * */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

/** This method gets the Division ID.
 * @return Returns Division ID
 * */
    public int getDivisionID() {
        return divisionID;
    }

    /** This method sets the division ID.
     * @param divisionID integer division id
     * */
    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

/** This method gets the customer name.
 * @return Returns customer's name
 * */
    public String getCustomerName() {
        return customerName;
    }

    /** This method sets the customer name.
     * @param customerName string customer name
     * */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

/** This method gets the Customer's Address.
 * @return Returns customer's address
 * */
    public String getCustomerAddress() {
        return customerAddress;
    }

    /** This method sets the customer Address.
     * @param customerAddress string customer address
     * */
    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

/** This method gets the customer postal code.
 * @return Returns customer postal code
 * */
    public String getCustomerPostal() {
        return customerPostal;
    }

    /** This method sets the customer postal code.
     * @param customerPostal iString customer zip
     * */
    public void setCustomerPostal(String customerPostal) {
        this.customerPostal = customerPostal;
    }

/** This method gets the customer phone number.
 * @return Returns customer phone number
 * */
    public String getCustomerPhone() {
        return customerPhone;
    }

    /** This method sets the customer phone.
     * @param customerPhone string customer phone
     * */
    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }
}
