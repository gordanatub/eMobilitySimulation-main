package org.unibl.etf.pj2.ePJ2.helper;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.unibl.etf.pj2.ePJ2.entity.Vehicle;
/**
 * Apstrakcija koordinata grada u kom se mogu iznajmljivati vozila.
 * @author Gordana Tubonjic
 * @version 1.0
 */
public class Coordinates {

	private int x;
	private int y;


	/**Podrazumijevani konstruktor kreira objekat sa vrijednosti koordinata (0,0) 
	 */
	public Coordinates() {
		this.setX(0);
		this.setY(0);
	}

	/**Konstruktor sa parametrima
	 * @param x Cjelobrojna pozitivna vrijednost ne veca od 20
	 * @param y Cjelobrojna pozitivna vrijednost ne veca od 20
	 */
	public Coordinates(int x, int y) {
		if (x < 0 || x > 20 || y < 0 || y > 20)
			try {
				throw new Exception("Koordinate van grada!");
			} catch (Exception e) {
				Logger.getLogger(Vehicle.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
			}
		else {
			this.setX(x);
			this.setY(y);
		}
	}

	@Override
	public String toString() {
		return "" + this.x + " : " + this.y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

}
