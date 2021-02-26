package Model;


/** This is the Contact class. */
public class Contact {
    private int contactID;
    private String contactName;
    private String contactEmail;

/**This is the Contact class's constructor. */
    public Contact(int contactID, String contactName, String contactEmail) {
        this.contactID = contactID;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }

/** This method gets the  contact id.
 * @return Returns contact ID
 * */
    public int getContactID() {
        return contactID;
    }

    /** This method sets the contact ID.
     * @param contactID integer contact ID
     * */
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

/** This method gets the  contact name.
* @return Returns contact name
* */
    public String getContactName() {
        return contactName;
    }

    /** This method sets the Contact Name.
     * @param contactName String Contact Name
     * */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

/** This method gets the contact email.
 *@return Returns contact email
 * */
    public String getContactEmail() {
        return contactEmail;
    }

    /** This method sets the contact email.
     * @param contactEmail String contact Email
     * */
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
}