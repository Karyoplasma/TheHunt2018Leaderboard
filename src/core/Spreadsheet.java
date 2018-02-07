package core;

/**
 * The spreadsheets with the progression data of the participants
 * @author Karyoplasma
 *
 */
public enum Spreadsheet {
	//define the sheets, enum name is not relevant and is never used anywhere
	Sheet1("1k7QESQ0MpQtxMza_xQvDlXfURxcGXQD4uLaNQVfF01w", "D1:EW1", "D160:EW160"),
	Sheet2("1QsjZSLLEXfmpcw1CR3vUX0HCWtLKN05xwLUVH4kie34", "D1:EW1", "D160:EW160");

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
