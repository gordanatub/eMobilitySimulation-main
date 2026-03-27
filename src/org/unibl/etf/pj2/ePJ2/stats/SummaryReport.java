package org.unibl.etf.pj2.ePJ2.stats;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.unibl.etf.pj2.ePJ2.entity.MalfunctionedVehicle;
import org.unibl.etf.pj2.ePJ2.entity.Vehicle;
import org.unibl.etf.pj2.ePJ2.rental.Receipt;
import org.unibl.etf.pj2.ePJ2.rental.Rental;
/** Apstrakcija sumarnog izvjestaja poslovanja.
 * @author Gordana Tubonjic
 * @version 1.0 
 */
public class SummaryReport extends Report {
 
	private BigDecimal totalTax = BigDecimal.ZERO;
	private BigDecimal totalExpences = BigDecimal.ZERO;

	private static List<Receipt> receipts = Collections.synchronizedList(new ArrayList<Receipt>());

	/**
	 * Podrazumijevani konstruktor.
	 */
	public SummaryReport() {
	}

	/**Konstruktor sa parametrima.
	 * @param receipts Lista racuna na osnovu kog se kreira izvjestaj
	 */
	public SummaryReport(List<Receipt> receipts) {
		SummaryReport.receipts = receipts;
	}

	/**Metoda genersise izvjestaj na osnovu liste racuna u statickom polju receipts. 
	 * @return SummaryReport Trenutni objekat
	 */
	public SummaryReport generate() {
		if (receipts != null) {
			super.generate(SummaryReport.receipts);
			this.totalTax = totalTax();
			this.totalExpences = totalExpenses();
		}
		return this;
	}

	/**Metoda dodaje racun u polje liste racuna.
	 * @param receipt Racun koji se dodaje u listu
	 */
	public void addReceipt(Receipt receipt) {
		receipts.add(receipt);
		setTotalIncome(getTotalIncome().add(receipt.getTotal()));
		setTotalDiscount(getTotalDiscount().add(receipt.getDiscount()));
		setTotalPromotion(getTotalPromotion().add(receipt.getPromotion()));
		setTotalIncomeFromRide(getTotalIncomeFromRide().add(receipt.getBasePrice()));
		setTotalMaintenanceCost(getTotalMaintenanceCost().add(totalMaintenance()));
		setTotalRepairCosts(getTotalRepairCosts().add(repairCost(receipt.getRental().getVehicle())));
		if (receipt.getRental().isOutterCity())
			setTotalWideRidesIncome(getTotalWideRidesIncome().add(receipt.getDistancePrice()));
		else
			setTotalNarrowRidesIncome(getTotalNarrowRidesIncome().add(receipt.getDistancePrice()));

	}

	/**Metoda racuna ukupne troskove poslovanja
	 * @return BigDecimal Iznos troskova
	 */
	public BigDecimal totalExpenses() {
		return getTotalIncome().multiply(new BigDecimal("0.02"));
	}

	/**Metoda racuna ukupan porez poslovanja. 
	 * @return BigDecimal Iznos poreza poslovanja
	 */
	public BigDecimal totalTax() {
		return (getTotalIncome().subtract(getTotalMaintenanceCost()).subtract(totalExpenses()))
				.multiply(new BigDecimal("0.1"));
	}

	
	/**Metoda pronalazi i serijalizuje pokvarena vozila i cijene njihovih popravki.
	 * @param rentals Lista iznajmljivanja iz kojih se pretrazuju pokvarena vozila
	 */
	public static void findMalfunctionedVehicles(List<Rental> rentals) {
		try {
			List<Vehicle> vehicles = rentals.stream().filter(r -> r.getVehicle().isMalfunctioned())
					.map(Rental::getVehicle).collect(Collectors.toList());
			
			
			
			BigDecimal repairCost = BigDecimal.ZERO;
			for (Vehicle v : vehicles) {
				repairCost = Receipt.repairCost(v);
				MalfunctionedVehicle malVehicle = new MalfunctionedVehicle(v, repairCost);
				String fileName = v.getID() + "_" + v.getMalfunction().getDateAndTime()
						.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));

				FileOutputStream pisac = new FileOutputStream(
						properties.getProperty("malfunctionCostsPath") + fileName + ".bin");
				ObjectOutputStream upisObjekta = new ObjectOutputStream(pisac);

				upisObjekta.writeObject(malVehicle); 
				upisObjekta.close();
				pisac.close();
			}
		} catch (FileNotFoundException e) {
			System.out.println("Fajl nije pronadjen!");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("IOException!");
		}
	}

	/**Metod deserijalizuje objekte pokvarenih Vozila i cijena njihovijh popravki 
	 * @param folder Direktorijum u kom se nalaze fajlovi sa serijalizovanim objektima MalfunctionedVehicle
	 * @return List<MalfunctionedVehicle> Lista pokvarenih vozila i cijena popravki
	 */
	public static List<MalfunctionedVehicle> deserializeMalfunctionedVehicles(File folder) {
		FileInputStream citac;
		File[] files = folder.listFiles();
		List<MalfunctionedVehicle> malVehicles = new ArrayList<MalfunctionedVehicle>();

		for (File f : files) {
			try {
				citac = new FileInputStream(f);
				ObjectInputStream citanjeObjekta = new ObjectInputStream(citac);
				MalfunctionedVehicle r = (MalfunctionedVehicle) citanjeObjekta.readObject();
				malVehicles.add(r);
				citanjeObjekta.close();
				citac.close();
			} catch (FileNotFoundException e) {
				System.out.println("Fajl nije pronadjen!");
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Exception!");
			}
		}
		return malVehicles;
	}

	@Override
	public String toString() {
		return "";
	}

	public BigDecimal getTotalTax() {
		return totalTax;
	}

	public void setTotalTax(BigDecimal totalTax) {
		this.totalTax = totalTax;
	}

	public BigDecimal getTotalExpences() {
		return totalExpences;
	}

	public void setTotalExpences(BigDecimal totalExpences) {
		this.totalExpences = totalExpences;
	}

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

}
