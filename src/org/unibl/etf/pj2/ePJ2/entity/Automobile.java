package org.unibl.etf.pj2.ePJ2.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Apstrakcija fizickog automobila.
 * @author Gordana Tubonjic
 * @version 1.0
 */
public class Automobile extends Vehicle {


	private static final long serialVersionUID = 3628981763510830346L;
	private LocalDate purchaceDate;
	private String description;
	private int passangers;

	/**
	 * Podrazumijevani konstruktor
	 */
	public Automobile() {
		super();
	}

	/**
	 * Konstruktor sa parametrima.
	 * @param id Identifikacioni parametar po kome se razlikuju vozila
	 * @param manu String reprezentacija proizvodjaca vozila
	 * @param mdl String reprezentacija modela vozila
	 * @param price BigDecimal cijena po kojoj je kupljeno vozilo
	 * @param icon Putanja ka simbolickoj reprezentaciji vozila u vidu slicice
	 * @param date Datum kupovine vozila
	 * @param desc Opis automobila
	 */
	public Automobile(String id, String manu, String mdl, BigDecimal price, String icon, LocalDate date, 
			String desc) {
		super(id, manu, mdl, price, icon);
		this.setDescription(desc);
		this.setPurchaceDate(date);
	}

	/**
	 * Metoda koja radi duboko kopiranje trenutnog objekta
	 * @return Automobile Metoda vraca duboku kopiju trenutnog objekta
	 */
	public Automobile clone() {
		
		return new Automobile(super.getID(), super.getManufacturer(), super.getModel(), super.getPurchasePrice(), super.getVehicleIcon(),
				this.getPurchaceDate(), this.getDescription());
		
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPassangers() {
		return passangers;
	}

	public void setPassangers(int passangers) {
		this.passangers = passangers;
	}

	public LocalDate getPurchaceDate() {
		return purchaceDate;
	}

	public void setPurchaceDate(LocalDate purchaceDate) {
		this.purchaceDate = purchaceDate;
	}

}
