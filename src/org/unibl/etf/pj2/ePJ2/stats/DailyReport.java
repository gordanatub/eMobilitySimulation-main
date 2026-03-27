package org.unibl.etf.pj2.ePJ2.stats;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.unibl.etf.pj2.ePJ2.rental.Receipt;
/** Dnevni izvjestaj poslovanja.
 * @author Gordana Tubonjic
 * @version 1.0 
 */
public class DailyReport extends Report {

	/**
	 * Podrazumijevani konstruktor.
	 */
	public DailyReport() {
		super();
	}

	/**Konstruktor sa parametrima.
	 * @param receipts Lista racuna na osnovu kojih se generise dnevni izvjestaj
	 */
	public DailyReport(List<Receipt> receipts) {
		super(receipts);
	}

	/**Metoda generise listu dnevnih izvjestaja na osnovu liste racuna koji su prosljedjeni kao parametar
	 * @param receipts Racuni na osnovu kojih se generisu izvjestaji
	 * @return List<DailyReport> Lista izvjestaja 
	 */
	public List<DailyReport> generateList(List<Receipt> receipts) {
		List<DailyReport> reports = new ArrayList<DailyReport>();
		receipts.sort(Comparator.comparing(Receipt::getDateAndTime));
		int numOfRentals = receipts.size();
		// CHANGED
		int i = 0;
		while (i < numOfRentals) {
			List<Receipt> sameTimeReceipts = new ArrayList<>();
			LocalDate receiptsDate = receipts.get(i).getDateAndTime().toLocalDate();
			while (i < numOfRentals && receipts.get(i).getDateAndTime().toLocalDate().equals(receiptsDate)) {
				sameTimeReceipts.add(receipts.get(i));
				i++;
			}
			reports.add(new DailyReport(sameTimeReceipts));
		}
		return reports;
	}
	
}
