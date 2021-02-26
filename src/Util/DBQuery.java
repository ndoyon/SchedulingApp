package Util;


import java.sql.*;


/** This class is for a DB query and prepared statement. */
public class DBQuery {

    private static PreparedStatement statement; //statement reference

    //create statement object
    public static void setPreparedStatement(Connection conn, String sqlStatement) throws SQLException {

        statement = conn.prepareStatement(sqlStatement);
    }
    //return statement object
    public static PreparedStatement getPreparedStatement()
    {
        return statement;
    }


}
