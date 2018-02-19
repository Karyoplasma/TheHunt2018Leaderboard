package core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.PriorityQueue;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

/**
 * @author Karyoplasma and the Google Quickstart Tutorial (code mostly copied from there)
 *
 */
public class SpreadsheetReader extends Observable{
	
	 /** Application name. */
    private static final String APPLICATION_NAME =
        "The Hunt 2018 Leaderboard";

    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
        System.getProperty("user.home"), ".credentials/sheets.googleapis.com-java-thehunt2018");

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY =
        JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/sheets.googleapis.com-java-quickstart
     */
    private static final List<String> SCOPES =
        Arrays.asList(SheetsScopes.SPREADSHEETS_READONLY);
    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }
    
    /**
     * Create a new SpreadsheetReader and set the observer
     * @param leaderboard The application that gets notified of changes
     */
   
    public SpreadsheetReader(TheHunt2018Leaderboard leaderboard) {
    	addObserver(leaderboard);
    }
    
    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        // Load client secrets.
    	File creds = new File("resources/client_secret.json");
        InputStream in = new FileInputStream("resources/client_secret.json");
        GoogleClientSecrets clientSecrets =
            GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(DATA_STORE_FACTORY)
                .setAccessType("offline")
                .build();
        Credential credential = new AuthorizationCodeInstalledApp(
            flow, new LocalServerReceiver()).authorize("user");
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized Sheets API client service.
     * @return an authorized Sheets API client service
     * @throws IOException
     */
    public static Sheets getSheetsService() throws IOException {
        Credential credential = authorize();
        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
    
    /**
     * Wrapper to easily loop through all defined spreadsheets, notifies the application of a change 
     */
    public void gatherLeaderboardData() {
    	//Initialize a new PriorityQueue
    	PriorityQueue<Participant> leaderboard = new PriorityQueue<Participant>();
    	//accumulate the data of all spreadsheets into the Priority Queue
    	try {
    	Sheets service = getSheetsService();
    	
    	for (Spreadsheet sheet : Spreadsheet.values()) {
    		leaderboard.addAll(getParticipantData(sheet, service));
    	}
    	//notify the application that the leaderboard was updated
    	setChanged();
    	notifyObservers(leaderboard);
    	return;
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    /**
     * Get the data from a spreadsheet
     * @param sheet The spreadsheet to gather data from
     * @param service The Google sheet service instance
     * @return A PriorityQueue with the data
     */
    private static PriorityQueue<Participant> getParticipantData(Spreadsheet sheet, Sheets service){
    	 try {
    		 //instantiate a new PriorityQueue
    		 PriorityQueue<Participant> participantData = new PriorityQueue<Participant>();
    		 //get the participants' names
    		 ValueRange names = service.spreadsheets().values().get(sheet.getSheetID(), sheet.getNameRange()).execute();
    		 //get the participants' points
    		 ValueRange points = service.spreadsheets().values().get(sheet.getSheetID(), sheet.getDataRange()).execute();
    		 //ValueRange is a list of lists, our list only has one entry, so remove the parent list in order to make my brain understand what I am doing
    		 List<Object> namesList = names.getValues().get(0);
    		 List<Object> pointsList = points.getValues().get(0);
    		 //if one of the lists is empty (could be due to a timeout for example), return an empty PriorityQueue
    		 if (names.isEmpty() || points.isEmpty()) {
    			 return participantData;
    		 } else {
    			 // fill the PriorityQueue with the participants
    			 int index = 0;
    			 for (Object n : namesList) {
    				 Participant p;
    				 if (pointsList.get(index).toString().equals("#ERROR!")) {
    					 p = new Participant(n.toString(), 0);
    				 } else {
    					 p = new Participant(n.toString(), Integer.parseInt(pointsList.get(index).toString()));
    				 }
    				 index++;
    				 participantData.add(p);
    			 }
    		 }
    		 //list is filled with the participants' data at this point, return it
    		 return participantData;
		} catch (IOException e) {
			// TODO Auto-generated catch block (never gonna do this)
			e.printStackTrace();
		}
    	//This should only happen if something goes awry (aka something triggers above exception). Could move the return to the catch block but not gonna.
		return null;
    }
}
