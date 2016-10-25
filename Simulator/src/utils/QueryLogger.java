package utils;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class QueryLogger {

	private static final Logger LOGGER = Logger.getLogger(
		    Thread.currentThread().getStackTrace()[0].getClassName() );
	private static Formatter simpleFormatter = null;
	private static Handler fileHandler = null;
	
	public static void log(String query){
		
        try{
            // Creating FileHandler
            fileHandler = new FileHandler("./queries.log",true);
             
            // Creating SimpleFormatter
            simpleFormatter = new SimpleFormatter();
             
            // Assigning handler to logger
            LOGGER.addHandler(fileHandler);
             
            // Setting formatter to the handler
            fileHandler.setFormatter(simpleFormatter);
             
            // Setting Level to ALL
            fileHandler.setLevel(Level.ALL);
            LOGGER.setLevel(Level.ALL);
             
            // Logging message of Level finest (this should be publish in the simple format)
            String qry = query.split(": ")[1];
            LOGGER.finest(qry);
        }catch(IOException exception){
            LOGGER.log(Level.SEVERE, "Error occur in FileHandler.", exception);
        }
	}
	
public static void log_temp(String str){
		
        try{
            // Creating FileHandler
            fileHandler = new FileHandler("./temp.log",true);
             
            // Creating SimpleFormatter
            simpleFormatter = new SimpleFormatter();
             
            // Assigning handler to logger
            LOGGER.addHandler(fileHandler);
             
            // Setting formatter to the handler
            fileHandler.setFormatter(simpleFormatter);
             
            // Setting Level to ALL
            fileHandler.setLevel(Level.ALL);
            LOGGER.setLevel(Level.ALL);
             
            // Logging message of Level finest (this should be publish in the simple format)
            LOGGER.finest(str);
        }catch(IOException exception){
            LOGGER.log(Level.SEVERE, "Error occur in FileHandler.", exception);
        }
	}
}
