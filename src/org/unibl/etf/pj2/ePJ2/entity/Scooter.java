package org.unibl.etf.pj2.ePJ2.entity;

import java.math.BigDecimal;
/** Apstrakcija elektricnog trotineta.
 * @author Gordana Tubonjic
 * @version 1.0 
 */
public class Scooter extends Vehicle {


	private static final long serialVersionUID = -3859460421445775617L;
	private double maxSpeed;

	/**
	 * Podrazumijevani konstruktor.
	 */
	public Scooter() {
		super();
	}

	/**Konstruktor sa parametrima.
	 * @param id Identifikacioni parametar po kome se razlikuju vozila
	 * @param manu String reprezentacija proizvodjaca vozila
	 * @param mdl String reprezentacija modela vozila
	 * @param price BigDecimal cijena po kojoj je kupljeno vozilo
	 * @param icon Putanja ka simbolickoj reprezentaciji vozila u vidu slicice 
	 * @param speed Maksimalna brzina kojom se moze kretati trotinet.
	 */
	public Scooter(String id, String manu, String mdl, BigDecimal price, String icon, double speed) {
		super(id, manu, mdl, price, icon);
		this.setMaxSpeed(speed);
	}

	/**
	 * Metoda koja radi duboko kopiranje trenutnog objekta
	 * @return Scooter Metoda vraca duboku kopiju trenutnog objekta
	 */
	public Scooter clone() {
		
		return new Scooter(super.getID(), super.getManufacturer(), super.getModel(), super.getPurchasePrice(), super.getVehicleIcon(),
				this.getMaxSpeed());
		
	}
	public double getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(double maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

}
