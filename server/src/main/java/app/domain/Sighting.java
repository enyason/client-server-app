package app.domain;

import java.util.Date;

public class Sighting {
	
	private String birdname;
	
	private String location;
	
	private Date date;

	/**
	 * @return the bird
	 */
	public String getBirdname() {
		return birdname;
	}

	/**
	 * @param bird the bird to set
	 */
	public void setBirdname(String birdname) {
		this.birdname = birdname;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	
	
	

}
