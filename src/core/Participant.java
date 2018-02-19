package core;


/**
 * A class that identifies the participants of The Hunt and holds their point value
 * @author Karyoplasma
 *
 */
public class Participant implements Comparable<Participant>{
	
	//everyone has a name and an amount of points
	private int points;
	private String name;

	/**
	 * Constructor for this class
	 * @param name Participant's name
	 * @param points Participant's amount of points
	 */
	public Participant(String name, int points) {
		this.name = name;
		this.points = points;
	}
	
	/**
	 * Getter for the points
	 * @return This participant's points
	 */
	public int getPoints() {
		return points;
	}

	/**
	 * Getter for the name
	 * @return This participant's name
	 */
	public String getName() {
		return name;
	}
	
	/*
	 * Method to qualify this class as a Comparable
	 */
	@Override
	public int compareTo(Participant o) {
		// more points equals higher rank
		if (this.points > o.points) {
			return -1;
		} else {
			//less points equals lower rank
			if (this.points < o.points) {
				return 1;
			}
		}
		// they have the same amount of points, so they are the same rank
		return 0;
	}
	
	/* 
	 * A String representation of this class (not needed anywhere, but left in for kicks)
	 */
	@Override
	
	public String toString() {
		return name + " " + points;
		
	}
	
}
