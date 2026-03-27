package org.unibl.etf.pj2.ePJ2.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
/**
 * Apstrakcija kvara na vozilu.
 * @author Gordana Tubonjic
 * @version 1.0
 */
public class Malfunction implements Serializable{

	private static final long serialVersionUID = 7541329044122459894L;
	private String description;
	private LocalDateTime dateAndTime;

	
	/**Podrazumijevani konstruktor. Postavlja opis greske kao "Undiagnosed fault in vehicle!".
	 */
	public Malfunction() {
		super();
		this.description = "Undiagnosed fault in vehicle!";
	}

	/**Konstruktor sa parametrima.
	 * @param desc Opis kvara na vozilu
	 * @param dateAndTime Datum i vrijeme kada se kvar desio
	 */
	public Malfunction(String desc, LocalDateTime dateAndTime) {
		super();
		this.setDescription(desc);
		this.setDateAndTime(dateAndTime);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getDateAndTime() {
		return dateAndTime;
	}

	public void setDateAndTime(LocalDateTime dateAndTime) {
		this.dateAndTime = dateAndTime;
	}
	
	@Override
	public String toString() {
		return "Opis: " + description + ", datum kvara: " + dateAndTime ;
	}

}
