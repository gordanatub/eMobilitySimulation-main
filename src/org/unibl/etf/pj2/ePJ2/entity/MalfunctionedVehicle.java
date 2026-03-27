package org.unibl.etf.pj2.ePJ2.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/** Apstrakcija klase koja skladisti par pokvarenog vozila i cijene popravke tog kvara.
 * @author Gordana Tubonjic
 * @version 1.0 
 */
public	class MalfunctionedVehicle implements Serializable{

	private static final long serialVersionUID = 3555226385708815507L;
	private Vehicle vehicle;
	private BigDecimal repairCost = BigDecimal.ZERO;
	
	
	/**Podrazumijevani kokstruktor.
	 */
	public MalfunctionedVehicle() {}
	/**Konstruktor sa parametrima.
	 * @param v Pokvareno vozilo.
	 * @param price Cijena popravke datog kvara na vozilu.
	 */
	public MalfunctionedVehicle(Vehicle v, BigDecimal price) {
		setVehicle(v);
		setRepairCost(price);
	}
	public Vehicle getVehicle() {
		return vehicle;
	}
	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
	public BigDecimal getRepairCost() {
		return repairCost;
	}
	public void setRepairCost(BigDecimal repairCost) {
		this.repairCost = repairCost;
	}
	
	@Override 
	public String toString() {
		return "" + vehicle + "\nCijena popravke: " + repairCost;
	}
	
}
