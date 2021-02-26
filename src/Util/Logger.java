package Util;

import java.io.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZonedDateTime;


/** This is the logger class for the text file of project. */
public class Logger {

    private static final String FILENAME = "loginActivity.txt";

    public Logger() {}

    /** This method creates and logs.
     * The method logs login attempts in a text file.
     * @param success boolean
     * @param username username
     * */
    public static void logs (String username, boolean success){
        try (FileWriter fw = new FileWriter(FILENAME, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter outputFile = new PrintWriter(bw)) {
            outputFile.println(ZonedDateTime.now() + " " + username + (success ? " Success" : " Failure"));
        } catch (IOException e) {
            System.out.println("Logger Error: " + e.getMessage());
        }

    }
}
