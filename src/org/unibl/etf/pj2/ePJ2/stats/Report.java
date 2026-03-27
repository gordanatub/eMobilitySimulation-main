package org.unibl.etf.pj2.ePJ2.stats;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;

import org.unibl.etf.pj2.ePJ2.entity.Automobile;
import org.unibl.etf.pj2.ePJ2.entity.Bicycle;
import org.unibl.etf.pj2.ePJ2.entity.Scooter;
import org.unibl.etf.pj2.ePJ2.entity.Vehicle;
import org.unibl.etf.pj2.ePJ2.rental.Receipt;
import org.unibl.etf.pj2.ePJ2.rental.Rental;

/**
 * Apstrakcija izvjestaja
 * 
 * @author Gordana Tubonjic
 * @version 1.0
 */
public abstract class Report {
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

	private BigDecimal totalIncome = BigDecimal.ZERO;;
	private BigDecimal totalDiscount = BigDecimal.ZERO;;
	private BigDecimal totalPromotion = BigDecimal.ZERO;;
	private BigDecimal totalIncomeFromRide = BigDecimal.ZERO;;
	private BigDecimal totalNarrowRidesIncome = BigDecimal.ZERO;
	private BigDecimal totalWideRidesIncome = BigDecimal.ZERO;
	private BigDecimal totalMaintenanceCost = BigDecimal.ZERO;;
	private BigDecimal totalRepairCosts = BigDecimal.ZERO;
	private LocalDateTime dateAndTime;

	/**
	 * Podrazumijevani konstruktor
	 */
	public Report() {
	}

	/**
	 * Konstruktor sa parametrima
	 * 
	 * @param receipts Lista racuna na osnovu kojih se generise izvjestaj
	 */
	public Report(List<Receipt> receipts) {
		dateAndTime = receipts.get(0).getDateAndTime();
		totalIncome = agregateValues(receipts, Receipt::getTotal);
		totalDiscount = agregateValues(receipts, Receipt::getDiscount);
		totalPromotion = agregateValues(receipts, Receipt::getPromotion);
		totalIncomeFromRide = agregateValues(receipts, Receipt::getDistancePrice);
		totalMaintenanceCost = totalIncome.multiply(new BigDecimal("0.2"));
		totalRepairCosts = agregateValues(receipts, r -> repairCost(r.getVehicle()));
	}

	/**
	 * Metoda agregira vrijednosti iz liste racuna.
	 * 
	 * @param receipts Lista racuna
	 * @param mapper   Funkcija koja mapira objekat racuna u BigDecimal vrijednost
	 * @return BigDecimal Agregirana vrijednost
	 */
	public static BigDecimal agregateValues(List<Receipt> receipts, Function<Receipt, BigDecimal> mapper) {
		return receipts.stream().map(mapper).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	/**
	 * Metoda racuna ukupan prihod poslovanja
	 * 
	 * @param receipts Lista racuna na kojima se zasniva poslovanje
	 * @return BigDecimal Ukupan prihod poslovanja ili BigDecimal.ZERO ako se
	 *         proslijedi prazna lista
	 */
	public static BigDecimal totalIncome(List<Receipt> receipts) {
		BigDecimal total = BigDecimal.ZERO;
		if (receipts != null) {
			total = receipts.stream().map(r -> {
				return r.getTotal();
			}).reduce(BigDecimal.ZERO, BigDecimal::add);
		}
		return total;
	}

	/**
	 * Metod generise vrijednosti i smjesta ih u atribute objekta na osnovu liste
	 * racuna
	 * 
	 * @param receipts Lista racuna na osnovu kojih se generise izvjestaj
	 * @return Report Trenutni objekat sa inicijalizovanim atributima
	 */
	public Report generate(List<Receipt> receipts) {

		totalIncome = agregateValues(receipts, Receipt::getTotal);
		totalDiscount = agregateValues(receipts, Receipt::getDiscount);
		totalPromotion = agregateValues(receipts, Receipt::getPromotion);
		totalIncomeFromRide = agregateValues(receipts, Receipt::getTotal);
		totalMaintenanceCost = totalIncome.multiply(new BigDecimal("0.2"));
		totalRepairCosts = agregateValues(receipts, r -> repairCost(r.getVehicle()));
		totalNarrowRidesIncome = receipts.stream().filter(r -> !r.getRental().isOutterCity()).map(r -> {
			return r.getTotal();
		}).reduce(BigDecimal.ZERO, BigDecimal::add);
		totalWideRidesIncome = receipts.stream().filter(r -> r.getRental().isOutterCity()).map(r -> {
			return r.getTotal();
		}).reduce(BigDecimal.ZERO, BigDecimal::add);

		return this;
	}

	/**
	 * Metoda racuna ukupne troskove odrzavanja Vozila
	 * 
	 * @return BigDecimal Ukupni troskovi odrzavanja
	 */
	public BigDecimal totalMaintenance() {
		return totalIncome.multiply(new BigDecimal(properties.getProperty("DISCOUNT")));
	}

	/**
	 * Metoda racuna ukupnu cijenu popravke vozila
	 * 
	 * @param rentals Lista iznajmljivanja na osnovu kog se racuna cijena popravke
	 * @return BigDecimal Ukupna cijena popravke ili BigDecimal.ZERO ako se
	 *         proslijedi prazna lista izvodjenja
	 */
	public BigDecimal totalRepairCost(List<Rental> rentals) {
		BigDecimal total = BigDecimal.ZERO;
		if (rentals != null) {
			total = (BigDecimal) rentals.stream().map(rental -> {
				return repairCost(rental.getVehicle());
			}).reduce(BigDecimal.ZERO, BigDecimal::add);
		}
		return total;
	}

	/**
	 * Metoda izracunava cijenu popravke jednog vozila
	 * 
	 * @param vehicle Vozilo za koje se racuna iznos popravke
	 * @return BigDecimal Cijena popravke
	 */
	public static BigDecimal repairCost(Vehicle vehicle) {

		BigDecimal cost = BigDecimal.ZERO;
		BigDecimal quotient = BigDecimal.ZERO;
		if (vehicle != null) {
			if (!vehicle.isMalfunctioned()) {
				return cost;
			}

			if (vehicle instanceof Automobile)
				quotient = new BigDecimal(properties.getProperty("CAR_REPAIR_Q"));
			else if (vehicle instanceof Bicycle)
				quotient = new BigDecimal(properties.getProperty("BIKE_REPAIR_Q"));
			else if (vehicle instanceof Scooter)
				quotient = new BigDecimal(properties.getProperty("SCOOTER_REPAIR_Q"));

			cost = vehicle.getPurchasePrice().multiply(quotient);
		}

		return cost;
	}

	public BigDecimal getTotalNarrowRidesIncome() {
		return totalNarrowRidesIncome;
	}

	public BigDecimal getTotalWideRidesIncome() {
		return totalWideRidesIncome;
	}

	public BigDecimal getTotalIncome() {
		return totalIncome;
	}

	public void setTotalIncome(BigDecimal totalIncome) {
		this.totalIncome = totalIncome;
	}

	public BigDecimal getTotalDiscount() {
		return totalDiscount;
	}

	public void setTotalDiscount(BigDecimal totalDiscount) {
		this.totalDiscount = totalDiscount;
	}

	public BigDecimal getTotalPromotion() {
		return totalPromotion;
	}

	public void setTotalPromotion(BigDecimal totalPromotion) {
		this.totalPromotion = totalPromotion;
	}

	public BigDecimal getTotalIncomeFromRide() {
		return totalIncomeFromRide;
	}

	public void setTotalIncomeFromRide(BigDecimal totalIncomeFromRide) {
		this.totalIncomeFromRide = totalIncomeFromRide;
	}

	public BigDecimal getTotalMaintenanceCost() {
		return totalMaintenanceCost;
	}

	public void setTotalMaintenanceCost(BigDecimal totalMaintenanceCost) {
		this.totalMaintenanceCost = totalMaintenanceCost;
	}

	public BigDecimal getTotalRepairCosts() {
		return totalRepairCosts;
	}

	public void setTotalRepairCosts(BigDecimal totalRepairCosts) {
		this.totalRepairCosts = totalRepairCosts;
	}

	public LocalDateTime getDateAndTime() {
		return dateAndTime;
	}

	public void setDateAndTime(LocalDateTime dateAndTime) {
		this.dateAndTime = dateAndTime;
	}

	public void setTotalNarrowRidesIncome(BigDecimal totalNarrowRidesIncome) {
		this.totalNarrowRidesIncome = totalNarrowRidesIncome;
	}

	public void setTotalWideRidesIncome(BigDecimal totalWideRidesIncome) {
		this.totalWideRidesIncome = totalWideRidesIncome;
	}

}
