package org.unibl.etf.pj2.ePJ2.entity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
/** Apstrakcija fizickog vozila koje se moze iznajmiti.
 * @author Gordana Tubonjic
 * @version 1.0 
 */
public abstract class Vehicle implements Serializable {

	private static final long serialVersionUID = 1L;

	private String ID;
	private String manufacturer;
	private String model;
	private double batteryLvl = 100;
	private BigDecimal purchasePrice;
	private boolean malfunctioned;
	private Malfunction malfunction;
	private String vehicleIcon;

	private static final String filePath = "./src/org/unibl/etf/pj2/ePJ2/helper/Properties.properties";
	private static Properties properties;
	{
		properties = new Properties();
		try {
			FileInputStream fis = new FileInputStream(filePath);
			properties.load(fis);
		} catch (Exception e) {
		}
		;

	}
	/** Podrazumijevani konstruktor.*/
	public Vehicle() {
	}
	/**Konstruktor sa parametrima.
	 * @param id identifikacioni parametar po kome se razlikuju vozila
	 * @param manu String reprezentacija proizvodjaca vozila
	 * @param mdl String reprezentacija modela vozila
	 * @param price BigDecimal cijena po kojoj je kupljeno vozilo
	 * @param icon putanja ka simbolickoj reprezentaciji vozila u vidu slicice
	 * */
	public Vehicle(String id, String manu, String mdl, BigDecimal price, String icon) {
		this.ID = (id);
		this.manufacturer = (manu);
		this.model = (mdl);
		this.batteryLvl = (100.0);
		this.purchasePrice = price;
		this.vehicleIcon = icon;
	}

	/**Metoda koja puni bateriju elektricnog vozila*/
	public void recharge() {
		this.batteryLvl = 100;
	}

	/**Metoda parsira vozila iz fajla.
	 * @param filePath String reprezentacija fajla u kom se nalaze serijalizovani objekti klase Vehicle
	 * @return List<Vehicle> Metoda vraca listu deserijalizovanih vozila.
	 * */
	public static List<Vehicle> parseVehicles(String filePath) {
		BufferedReader inVehicles; 
		Set<Vehicle> listVehicles = new HashSet<Vehicle>();

		try {
			inVehicles = new BufferedReader(new FileReader(filePath));
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.yyyy.");
			inVehicles.readLine();

			String vehicleString;
			while ((vehicleString = inVehicles.readLine()) != null) {
				String[] parameters = vehicleString.split(",");
				if (parameters.length < 9) {
					System.err.println("Invalid data: " + vehicleString);
					continue;
				}
				try {
					String id = parameters[0];
					String manufacturer = parameters[1];
					String model = parameters[2];
					LocalDate purchaseDate = parameters[3].isEmpty() ? null : LocalDate.parse(parameters[3], formatter) ;
					BigDecimal purchacePrice = new BigDecimal(parameters[4]); // (Integer.parseInteger(parameters[4]));
					String autonomy = parameters[5];
					Integer maxSpeed = Integer.parseInt(parameters[6].isEmpty() ? "0" : parameters[6]);
					String description = parameters[7];
					String type = parameters[8];
					Vehicle vehicle;

					switch (type) {
					case "automobil":
						vehicle = new Automobile(id, manufacturer, model, purchacePrice,
								properties.getProperty("CAR_ICON"), purchaseDate, description);
						break;
					case "bicikl":
						vehicle = new Bicycle(id, manufacturer, model, purchacePrice,
								properties.getProperty("BICYCLE_ICON"), autonomy);
						break;
					case "trotinet":
						vehicle = new Scooter(id, manufacturer, model, purchacePrice,
								properties.getProperty("SCOOTER_ICON"), maxSpeed);
						break;
					default:
						throw new IllegalArgumentException();
					}
					
					listVehicles.add(vehicle);
				} catch (IllegalArgumentException e) {
				}
			}
		} catch (FileNotFoundException e) {
		} catch (IOException e1) {
		}
		return new ArrayList<Vehicle>(listVehicles);
	}
	
	/**Apstraktna metoda koja radi duboko kopiranje trenutnog objekta
	 * @return Vehicle Metoda vraca duboku kopiju trenutnog objekta
	 * */
	public abstract Vehicle clone();
	
	@Override
	/**{@inheritDoc}*/
	public String toString() {
		return "Vozilo: " + getClass().getSimpleName() + ", ID: " + this.ID + "cijena " + this.purchasePrice
				+ ", manufacturer: " + this.manufacturer + ", model: " + this.model + ". Malfunction " + malfunction;
	}

	@Override
	/**{@inheritDoc}*/
	public boolean equals(Object o) {
		if (o != null && o instanceof Vehicle && (this.ID.equals(((Vehicle) o).getID())))
			return true;
		else 
			return false;
	}

	@Override
	/**{@inheritDoc}*/
	public int hashCode() {
		return (int)ID.hashCode();
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public double getBatteryLvl() {
		return batteryLvl;
	}

	public void setBatteryLvl(double batteryLvl) {
		this.batteryLvl = batteryLvl;
	}

	public BigDecimal getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(BigDecimal purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public String getVehicleIcon() {
		return vehicleIcon;
	}

	public void setVehicleIcon(String vehicleIcon) {
		this.vehicleIcon = vehicleIcon;
	}

	public boolean isMalfunctioned() {
		return malfunctioned;
	}

	public void setMalfunctioned(boolean malfunction) {
		this.malfunctioned = malfunction;
	}

	public Malfunction getMalfunction() {
		return malfunction;
	}

	public void setMalfunction(Malfunction malfunction) {
		this.malfunction = malfunction;
	}

}
