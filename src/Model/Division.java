package Model;

/** This is the Divison class. */
public class Division {
    private int divisionID;
    private int countryID;
    private String divisionName;

    /** This is the Divisions class  firstconstructor. */
    public Division(int divisionID, int countryID, String divisionName) {
        this.divisionID = divisionID;
        this.countryID = countryID;
        this.divisionName = divisionName;
    }



/** This method gets the Division ID.
 * @return Returns Division ID
 * */
    public int getDivisionID() {
        return divisionID;
    }

    /** This method sets the division ID.
     *@param divisionID integer division id
     **/
    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

/** This method gets the country ID.
 * @return  Returns integer Country ID
 * */
    public int getCountryID() {
        return countryID;
    }

    /** This method sets the country ID.
     *@param countryID integer country id
     **/
    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

/** This method gets the Division Name.
 *@return Returns string division name
 * */
    public String getDivisionName() {
        return divisionName;
    }

    /** This method sets the division name.
     *@param divisionName string division name
     **/
    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }
}