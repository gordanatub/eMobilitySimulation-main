package org.unibl.etf.pj2.ePJ2.entity;

import java.math.BigDecimal;

/**
 * Apstrakcija fizickog bicikla.
 * @author Gordana Tubonjic
 * @version 1.0
 */

public class Bicycle extends Vehicle {
	
	private static final long serialVersionUID = 8667065086857148558L;
	private String autonomy;
	/**Podrazumijevani konstruktor.*/
	public Bicycle() {
		super();
	}
	
	/**Konstruktor sa parametrima.
	 * @param id Identifikacioni parametar po kome se razlikuju vozila
	 * @param manu String reprezentacija proizvodjaca vozila
	 * @param mdl String reprezentacija modela vozila
	 * @param price BigDecimal cijena po kojoj je kupljeno vozilo
	 * @param icon Putanja ka simbolickoj reprezentaciji vozila u vidu slicice
	 * @param distance Udaljenost koju bicikl moze preci, a da mu se ne potrosi baterija
	 */
	public Bicycle(String id, String manu, String mdl, BigDecimal price, String icon, String distance) {
		super(id, manu, mdl, price, icon);
		this.setAutonomy(distance);
	}
	/**
	 * Metoda koja radi duboko kopiranje trenutnog objekta
	 * @return Bicycle Metoda vraca duboku kopiju trenutnog objekta
	 *//**
	 *
	 */
	public Bicycle clone() {
			
			return new Bicycle(super.getID(), super.getManufacturer(), super.getModel(), super.getPurchasePrice(), super.getVehicleIcon(),
					this.getAutonomy());
			
		}
	public String getAutonomy() {
		return autonomy;
	}

	public void setAutonomy(String autonomy) {
		this.autonomy = autonomy;
	}

}