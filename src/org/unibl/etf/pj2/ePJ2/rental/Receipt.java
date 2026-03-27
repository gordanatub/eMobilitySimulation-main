package org.unibl.etf.pj2.ePJ2.rental;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import org.unibl.etf.pj2.ePJ2.entity.Automobile;
import org.unibl.etf.pj2.ePJ2.entity.Bicycle;
import org.unibl.etf.pj2.ePJ2.entity.Scooter;
import org.unibl.etf.pj2.ePJ2.entity.Vehicle;
/** Apstrakcija racuna koji obracunava troskove iznajmljivanja.
 * @author Gordana Tubonjic
 * @version 1.0 
 */
public class Receipt {

	private static Properties properties;
	private static final String filePath = "./src/org/unibl/etf/pj2/ePJ2/helper/Properties.properties";
	static {
		properties = new Properties();
		try {
			FileInputStream fis = new FileInputStream(filePath);
			properties.load(fis);
		} catch (Exception e) {
		}
		;
	}

	private LocalDateTime dateAndTime;
	private BigDecimal basePrice;
	private BigDecimal distancePrice;
	private BigDecimal discount;
	private BigDecimal promotion;
	private BigDecimal total;
	
	private Rental rental;

	/**
	 * Podrazumijevani konstruktor
	 */
	public Receipt() {
	}

	/**Konstruktor sa parametrima
	 * @param rental Objekat Rental koji reprezentuje iznajmljivanje na osnovu kog se generise racun.
	 */
	public Receipt(Rental rental) {
		this.rental = rental;
		basePrice = baseCost(rental);
		distancePrice = distanceCost(rental);
		discount = discount(rental);
		promotion = promotion(rental);
		total = totalCost(rental);
		dateAndTime = rental.getDateAndTime();
	}

	public Rental getRental() {
		return rental;
	}

	/**Metoda izracunava osnovnu cijenu iznajmljivanja na osnovu tipa vozila i trajanja voznje
	 * @param rental Objekat klase Rental koje predstavlja iznajmljivanje na osnovu kog se racuna cijena
	 * @return BigDecimal Osnovna cijena iznajmljivanja
	 */
	static public BigDecimal baseCost(Rental rental) {
		Vehicle vehicle = rental.getVehicle();
		BigDecimal cost = new BigDecimal("0");
		BigDecimal quotient = new BigDecimal("0");

		if (vehicle instanceof Automobile)
			quotient = new BigDecimal(properties.getProperty("CAR_UNIT_PRICE"));
		else if (vehicle instanceof Bicycle)
			quotient = new BigDecimal(properties.getProperty("BIKE_UNIT_PRICE"));
		else if (vehicle instanceof Scooter)
			quotient = new BigDecimal(properties.getProperty("SCOOTER_UNIT_PRICE"));
		cost = quotient.multiply(BigDecimal.valueOf(rental.getDuration()));
		return cost;
	}

	/**Metoda izracunava cijenu iznajmljivanja na osnovu dijela grada u kom se odvijala voznja.
	 * @param rental Objekat klase Rental koje predstavlja iznajmljivanje na osnovu kog se racuna cijena
	 * @return BigDecimal Iznos za placanje
	 */
	static public BigDecimal distanceCost(Rental rental) {
		BigDecimal cost = new BigDecimal("0");
		cost = baseCost(rental).multiply(rental.isOutterCity() ? new BigDecimal(properties.getProperty("DISTANCE_WIDE"))
				: new BigDecimal(properties.getProperty("DISTANCE_NARROW")));
		return cost;
	}

	/**Metoda racuna popust na racunu 
	 * @param rental Objekat klase Rental koje predstavlja iznajmljivanje na osnovu kog se racuna cijena
	 * @return BigDecimal Izracunat popust na racunu 
	 */
	static public BigDecimal discount(Rental rental) {
		BigDecimal discount = new BigDecimal("0");
		BigDecimal baseCost = distanceCost(rental);
		discount = baseCost.multiply(new BigDecimal(properties.getProperty("DISCOUNT")));
		return discount;
	}
	/**Metoda racuna promociju na racunu 
	 * @param rental Objekat klase Rental koje predstavlja iznajmljivanje na osnovu kog se racuna promocija
	 * @return BigDecimal Izracunata promocija  na racunu 
	 */
	static public BigDecimal promotion(Rental rental) {
		BigDecimal promotion = new BigDecimal("0");
		BigDecimal baseCost = distanceCost(rental);
		if (rental.isTenth())
			promotion = baseCost.multiply(new BigDecimal(properties.getProperty("DISCOUNT_PROM")));
		return promotion;
	}

	/**Metoda izracunava cijenu za placanje.
	 * @param rental Objekat klase Rental koje predstavlja iznajmljivanje na osnovu kog se racuna cijena
	 * @return BigDecimal Ukupna cijena za placanje
	 */
	static public BigDecimal totalCost(Rental rental) {
		BigDecimal baseCost = distanceCost(rental);
		BigDecimal totalCost = new BigDecimal("0");
		if (!rental.isMalfunctioned()) {
			BigDecimal discount = discount(rental);
			BigDecimal promotion = promotion(rental);
			totalCost = baseCost.subtract(discount).subtract(promotion);
		}

		return totalCost;
	}

	/**Metoda racuna cijenu popravke vozila.
	 * @param vehicle Vozilo za koje se racuna cijena popravke.
	 * @return BigDecimal Izracunata cijena popravke.
	 */
	public static BigDecimal repairCost(Vehicle vehicle) {
		// TODO Validation null
		BigDecimal cost = new BigDecimal("0");
		BigDecimal quotient = new BigDecimal("0");

		if (vehicle instanceof Automobile)
			quotient = new BigDecimal(properties.getProperty("CAR_REPAIR_Q"));
		else if (vehicle instanceof Bicycle)
			quotient = new BigDecimal(properties.getProperty("BIKE_REPAIR_Q"));
		else if (vehicle instanceof Scooter)
			quotient = new BigDecimal(properties.getProperty("SCOOTER_REPAIR_Q"));

		if (vehicle.isMalfunctioned()) {
			cost = vehicle.getPurchasePrice().multiply(quotient);
		}
		return cost;
	}

	public Vehicle getVehicle() {
		return rental.getVehicle();
	}

	/**Metoda serijalizuje Receipt objekat u tekstualni fajl i smjesta u folder definisan u properties fajlu
	 * 
	 */
	public void printReceipt() {

		try {
			String receiptsFolder = properties.getProperty("receiptsFolder");
			File folder = new File(receiptsFolder);
			if (!folder.exists()) {
				folder.mkdirs();
			}

			// Create the receipt file within the specified folder
			String filePath = receiptsFolder + File.separator + rental.getVehicle().getID()
					+ rental.getDateAndTime().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")).toString()
					+ ".txt";
			PrintWriter racun = new PrintWriter(new BufferedWriter(new FileWriter(filePath)));

			// -----
			racun.println("=======FISKALNI RACUN=======");
			racun.println("Korisnik: " + rental.getCustomer().getName() + "(" + rental.getCustomer().getDriversLicence()
					+ ")");
			racun.println("Iznajmljeno vozilo: " + rental.getVehicle().getID());
			racun.println("Datum iznajmljivanja: " + rental.getDateAndTime());
			racun.println("Trajanje iznajmljivanja: " + rental.getDuration());
			racun.println("Da li se desio kvar: " + (rental.isMalfunctioned() ? "da" : "ne"));
			racun.println("Osnovna cijena po trajanju: " + this.basePrice);
			racun.println("Osnovna cijena po regiji kretanja: " + this.distancePrice);
			racun.println("Popust na iznajmljivanje: " + this.discount);
			racun.println("Promocija na iznajmljivanje: " + this.promotion);
			racun.println("Ukupno za platiti: " + this.total);

			racun.close();

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("An error occurred while generating the receipt.");
		}

	}

	public LocalDateTime getDateAndTime() {
		return dateAndTime;
	}
	public BigDecimal getBasePrice() {
		return basePrice;
	}

	public BigDecimal getDistancePrice() {
		return distancePrice;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public BigDecimal getPromotion() {
		return promotion;
	}

	public BigDecimal getTotal() {
		return total;
	}


}
