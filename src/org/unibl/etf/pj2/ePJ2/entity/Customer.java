package org.unibl.etf.pj2.ePJ2.entity;

import java.util.Random;

/**
 * Apstrakcija korisnika.
 * @author Gordana Tubonjic
 * @version 1.0
 */
public class Customer {
	private String name;
	private String id;
	private String driversLicence;
	private String passport;
	private boolean foreigner;

	/**Podrazumijevani konstruktor.*/
	public Customer() {
	}

	/**Konstruktor sa parametrima.
	 * @param name Ime korisnika
	 * @param id  String reprezentacija dentifikacionog dokumenta
	 * @param licence String reprezentacija vozacke dozvole
	 */
	public Customer(String name, String id, String licence) {
		super();
		this.setName(name);
		this.setId(id);
		this.setDriversLicence(licence);
		this.foreigner = false;
	}
	/**Konstruktor sa parametrima.
	 * @param name Ime korisnika
	 * @param id  String reprezentacija dentifikacionog dokumenta
	 * @param passport String reprezentacija pasosa
	 * @param flag Oznaka da je korisnik stranac
	 */
	public Customer(String name, String id, String passport, int flag) {
		super();
		this.setName(name);
		this.setId(id);
		this.setPassport(passport);
		this.foreigner = true;
	}
	public Customer(String name) {
		this.name = name;
		this.driversLicence = ""+1000 + new Random().nextInt(5000);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDriversLicence() {
		return driversLicence;
	}

	public void setDriversLicence(String driversLicence) {
		this.driversLicence = driversLicence;
	}

	public String getPassport() {
		return passport;
	}

	public void setPassport(String passport) {
		this.passport = passport;
	}

	public boolean isForeigner() {
		return foreigner;
	}

}
