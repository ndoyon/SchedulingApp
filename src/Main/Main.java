package Main;

import DAO.AppointmentDB;
import DAO.CustomerDB;
import Util.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.*;

/** This class creates an app that displays appointments for customers */
public class Main extends Application {

    /** This method is for the Login Screen. This is where the scene changes and dispalys the login. */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/View_Controller/Login.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    /** This is the main method. This is the method that gets called when you run the program.
     * @param args args for main
     * */
    public static void main(String[] args){

        DBConnection.startConnection();// connect to database
        System.out.println("Connected to DB");
        CustomerDB.selectAllCustomers();
        AppointmentDB.selectAllAppts();
        launch(args);
        DBConnection.closeConnection();
    }
}
