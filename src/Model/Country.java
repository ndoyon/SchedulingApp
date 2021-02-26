package Model;


/** This class is the Country class.  */
public class Country {
    private int countryID;
    private String countryName;

    /** This is the Country class constructor. */
    public Country(int countryID, String countryName) {
        this.countryID = countryID;
        this.countryName = countryName;
    }

/** This method gets the country ID.
 * @return Returns Country ID
 * */
    public int getCountryID() {
        return countryID;
    }

    /** This method sets the Country ID.
     * @param countryID integer Country ID
     * */
    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

/** This method gets the Country Name.
 * @return Returns customer name
 * */
    public String getCountryName() {
        return countryName;
    }

    /** This method sets the Country Name.
     * @param countryName integer Country Name
     * */
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
