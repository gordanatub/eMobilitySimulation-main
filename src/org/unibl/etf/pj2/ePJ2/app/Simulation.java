package org.unibl.etf.pj2.ePJ2.app;
 
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

import org.unibl.etf.pj2.ePJ2.entity.MalfunctionedVehicle;
import org.unibl.etf.pj2.ePJ2.rental.Rental;
import org.unibl.etf.pj2.ePJ2.stats.SummaryReport;
import org.unibl.etf.pj2.gui.TravelSimulationFrame;
/**
 * Klasa Simulation je pocetna tacka programa iz koje pocinje izvrsavanje programa.
 * @author Gordana Tubonjic
 * @version 1.0 
 */


public class Simulation {
	public static void main(String[] args) {		
		Properties properties;
		String filePath = "./src/org/unibl/etf/pj2/ePJ2/helper/Properties.properties";
		properties = new Properties();
		try {
			FileInputStream fis = new FileInputStream(filePath);
			properties.load(fis);
		} catch (Exception e) {
		}
		;
	
		TravelSimulationFrame.startSimulation();

		List<Rental> rentals = null; 
		try {
			rentals = Rental.parseRentals(properties.getProperty("rentals"));
		} catch (IOException e) {
			e.printStackTrace();
		} 
		;
		if(rentals!=null)
			for(Rental r : rentals)
				System.out.println(r);
		SummaryReport.findMalfunctionedVehicles(rentals);
		File folder = Paths.get(properties.getProperty("malfunctionCostsPath")).toFile();
		List<MalfunctionedVehicle> malVehicles =  SummaryReport.deserializeMalfunctionedVehicles(folder);
		malVehicles.stream().forEach(v-> System.out.println(v));
	}
}
