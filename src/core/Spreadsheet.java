package core;

/**
 * The spreadsheets with the progression data of the participants
 * @author Karyoplasma
 *
 */
public enum Spreadsheet {
	//define the sheets, enum name is not relevant and is never used anywhere
	Sheet1("1wJzqmO-O3LS-xHBxomOegZ1IVpB-VJa85frdoNOE2l4", "D1:EW1", "D160:EW160"),
	Sheet2("1iiegLvVbG3YxDAJym6IPBYPkEJ_nsH8l7WX6xoqY6Ig", "D1:EW1", "D160:EW160"),
	Sheet3("1vucL8W7FrsJbTG1VpJ4P0jDvYp4q4vMZHJ-H7H-lppw", "D1:EU1", "D160:EU160"),
	Sheet4("1ZQxGOgho8y4GZQQ70Ub6WoqenfRLEAgIfWnUNbEj0jE", "D1:EX1", "D160:EX160"),
	Sheet5("1e0ScWuYTCbmDaLZE7xhAqSmBNyFclt9FQlFYVjMiIrw", "D1:EW1", "D160:EW160"),
	Sheet6("1c11bvY5btj87ZRRFCp-qgxfZ5uTN9B0196eqWyTRV80", "D1:FP1", "D160:FP160");

	private String sheetID, nameRange, dataRange;
	
	/**
	 * Constructor of the Spreadsheet, private for non-instantiability
	 * @param sheetID The ID of the sheet (that long, non-sensical string in the Google URL)
	 * @param nameRange Where in the Google spreadsheet can you find the name of the participants
	 * @param dataRange Where in the Google spreadsheet can you find the point progress of the participants
	 */
	private Spreadsheet(String sheetID, String nameRange, String dataRange) {
		
		this.sheetID = sheetID;
		this.nameRange = nameRange;
		this.dataRange = dataRange;
	}

	/**
	 * Getter for the sheet ID
	 * @return The sheet ID
	 */
	public String getSheetID() {
		return sheetID;
	}

	/**
	 * Getter for the name range
	 * @return Where the names can be found 
	 */
	public String getNameRange() {
		return nameRange;
	}

	/**
	 * Getter of the data range
	 * @return Where the point progress can be found
	 */
	public String getDataRange() {
		return dataRange;
	}
}
