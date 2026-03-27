package org.unibl.etf.pj2.ePJ2.rental;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import org.unibl.etf.pj2.ePJ2.entity.Automobile;
import org.unibl.etf.pj2.ePJ2.entity.Bicycle;
import org.unibl.etf.pj2.ePJ2.entity.Customer;
import org.unibl.etf.pj2.ePJ2.entity.Malfunction;
import org.unibl.etf.pj2.ePJ2.entity.Scooter;
import org.unibl.etf.pj2.ePJ2.entity.Vehicle;
import org.unibl.etf.pj2.ePJ2.helper.Coordinates;
/** Apstrakcija iznajmljivanja vozila.
 * @author Gordana Tubonjic
 * @version 1.0 
 */
public class Rental extends Thread {
	private Vehicle vehicle;
	private LocalDateTime dateAndTime;

	private Customer customer;
	private volatile Coordinates takeOnLocation;
	private volatile Coordinates returnLocation;
	public Integer duration;

	private volatile Coordinates currentLocation;
	private volatile JLabel[][] grid;
	private static int numOfRentals = 0;
	private boolean tenthFlag = false;
	
	private static List<Receipt> receipts = Collections.synchronizedList(new ArrayList<Receipt>());

	/**
	 * Podrazumijevani konstruktor kreira objekat Rental i uvecava brojac instanci za jedan. 
	 */
	public Rental() {
		setNumOfRentals(getNumOfRentals() + 1);
		if (numOfRentals % 10 == 0)
			setTenth(true);
	}
	/**Konstruktor sa parametrima.
	 * @param beg Koordinate pocetka kretanja vozila koje je iznajmljeno
	 * @param end Koordinate kraja kretanja vozila koje je iznajmljeno
	 * @param vehicle Vozilo koje je iznajmljeno
	 */
	public Rental(Coordinates beg, Coordinates end, Vehicle vehicle) {
		this.takeOnLocation = beg;
		this.returnLocation = end;
		this.vehicle = vehicle;
		currentLocation = new Coordinates(beg.getX(), beg.getY());
		;
		setNumOfRentals(getNumOfRentals() + 1);
		if (numOfRentals % 10 == 0)
			setTenth(true);
	}
	public Rental(LocalDateTime ldt, String customer2, Vehicle idVehicle, Coordinates start, Coordinates end,
			Integer duration, boolean malfunction, boolean promotion) {
		this.takeOnLocation = start;
		this.returnLocation = end;
		this.vehicle = idVehicle;
		this.currentLocation = new Coordinates(start.getX(), start.getY());
		this.duration = duration;
		this.vehicle.setMalfunctioned(malfunction);
		this.dateAndTime = ldt;
		this.customer = new Customer(customer2);
		setNumOfRentals(getNumOfRentals() + 1);
		if (numOfRentals % 10 == 0)
			setTenth(true);
		}

	/**Metoda azurira celiju grida 
	 * @param x Prva koordianta celije
	 * @param y Druga koordinata celije
	 * @param text Tekst koji ce biti postavljen na celiju grida
	 * @param icon Ikonica koja ce biti postavljena na celiju grida
	 */
	private void updateGridCell(int x, int y, String text, ImageIcon icon) {
		grid[x][y].setText(text);
		grid[x][y].setIcon(icon);
		grid[x][y].setHorizontalTextPosition(JLabel.CENTER);
		grid[x][y].setVerticalTextPosition(JLabel.CENTER);
		grid[x][y].setForeground(Color.WHITE);
		grid[x][y].setFont(new Font("Arial", Font.BOLD, 8));
	}
	
	/**Metoda racuna duzinu puta kretanja od pocetne do krajnje tacke iznajmljivanja
	 * @return int Broj celija koje vozilo predje od pocetka do kraja
	 */
	public int pathLength() {
		return  Math.abs(returnLocation.getX() - takeOnLocation.getX())
				+ Math.abs(returnLocation.getY() - takeOnLocation.getY());
	}
	
	/**Metoda provjerava da li je vozilo stiglo do odredista
	 * @return boolean True ako je trenutna pozicija jednaka krajnoj poziciji 
	 */
	public boolean vehicleArrived() {
		return currentLocation.getX() == returnLocation.getX() && currentLocation.getY() == returnLocation.getY();
	}

	/**Metoda brise sav sadrzaj sa celije grida
	 * @param i Prva koordinata
	 * @param j Druga koordinata
	 */
	public void resetCell(int i, int j) {
		grid[i][j].setText(null);
		grid[i][j].setIcon(null);
	}
	/**Metoda simulira kretanje vozila po gridu labela koji reprezentuje grad.
	 * 
	 * */
	@Override
	public void run() {
		int pathLength = pathLength();

		while (!vehicleArrived()) {
			
			resetCell(currentLocation.getX(),currentLocation.getY());
				
				if (currentLocation.getX() < returnLocation.getX())
					currentLocation.setX(currentLocation.getX() + 1);
				else if (currentLocation.getX() > returnLocation.getX())
					currentLocation.setX(currentLocation.getX() - 1);
			
			SwingUtilities.invokeLater(() -> {
				updateGridCell(currentLocation.getX(), currentLocation.getY(),
						vehicle.getID() + ":" + vehicle.getBatteryLvl(), new ImageIcon(this.vehicle.getVehicleIcon()));
				
				});
			
			try {

				Thread.sleep(1000 * duration / pathLength);
			} catch (InterruptedException e) {
	
				e.printStackTrace();
			}
			
				resetCell(currentLocation.getX(),currentLocation.getY());

				if (currentLocation.getY() < returnLocation.getY())
					currentLocation.setY(currentLocation.getY() + 1);
				else if (currentLocation.getY() > returnLocation.getY())
					currentLocation.setY(currentLocation.getY() - 1);
				vehicle.setBatteryLvl(vehicle.getBatteryLvl() - 1);	
		
			
			SwingUtilities.invokeLater(() -> {

				updateGridCell(currentLocation.getX(), currentLocation.getY(),
						vehicle.getID() + ":" + vehicle.getBatteryLvl(), new ImageIcon(this.vehicle.getVehicleIcon()));

			});		
			
			try {

				Thread.sleep(1000 * duration / pathLength);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		
		resetCell(currentLocation.getX(),currentLocation.getY());


		Receipt rec = new Receipt(this);
		rec.printReceipt();
		try {
			receipts.add(rec);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**Metoda radi rasporedjivanje niti iznajmljivanja po datumu i vremenu, tako da se niti iznajmljivanja izvrsavaju istovremeno
	 * akko su istog datuma i vremena 
	 * @param rentals Lista niti iznajmljivanja cije ce izvrsavanje biti pozvano u metodi
	 */
	public static void scheduleRentalSimulation(List<Rental> rentals) {
		Thread thread = new Thread(() -> {
			List<List<Rental>> rentalsGrid = new ArrayList<List<Rental>>();
			rentals.sort(Comparator.comparing(Rental::getDateAndTime));
			int numOfRentals = rentals.size();
			rentalsGrid.add(new ArrayList<Rental>());
			int i = 0;
			while (i < numOfRentals) {
				List<Rental> sameTimeRentals = new ArrayList<>();
				LocalDateTime rentalDate = rentals.get(i).getDateAndTime();
				while (i < numOfRentals && rentals.get(i).getDateAndTime().equals(rentalDate)) {
					sameTimeRentals.add(rentals.get(i));
					i++;
				}
				rentalsGrid.add(sameTimeRentals);
			}

			for (List<Rental> rentList : rentalsGrid) {
				for (Rental rent : rentList) {
					rent.start();
				}

				for (Rental rent : rentList) {
					try {
						rent.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				try {
					
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		});
		thread.start();

	}
	

	

	/**Metoda provjerava da li se vozilo kretalo po periferiji grada.
	 * @return boolean Vraca true ako je krajnja ili pocetna tacka van granica unutrasnjosti grada
	 */
	public boolean isOutterCity() {
		if (takeOnLocation.getX() < 5 || takeOnLocation.getX() > 14 || returnLocation.getX() < 5
				|| returnLocation.getY() > 14)
			return true;
		return false;
	}


	/** Metoda parsira i sortira iznajmljivanja po datumu i vremenu
	 * @param filePath String reprezentacija fajla u kom se nalaze serijalizovani objekti Rental
	 * @return List<Rental> Lista iznajmljivanja sortiranih po vremenu i datumu
	 * @throws IOException Baca izuzetak 
	 *  */
	public static List<Rental> parseRentals(String filePath) throws IOException {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.yyyy HH:mm");
		Set<Rental> listRentals = new HashSet<Rental>();
	    Map<String, Vehicle> vehicleMap = Vehicle.parseVehicles(properties.getProperty("vehicles"))
	            .stream().collect(Collectors.toMap(Vehicle::getID, v -> v));
	    
		try (BufferedReader inRental = new BufferedReader(new FileReader(filePath))) {
			inRental.readLine();
			String rentalString;
			String regex = "\\\"\\d+?\\,\\d+?\\\"|[^\\,]+";
			Pattern pattern = Pattern.compile(regex);
			
			while ((rentalString = inRental.readLine()) != null) {
				Matcher matcher = pattern.matcher(rentalString);
				String[] parameters = matcher.results().map(MatchResult::group).toArray(String[]::new);
				try {
					LocalDateTime ldt = LocalDateTime.parse(parameters[0], formatter);
					String customer = parameters[1];
					String idVehicle = parameters[2];
					Coordinates start = new Coordinates(
							Integer.parseInt(((parameters[3].replace("\"", "")).split(","))[0]),
							Integer.parseInt(((parameters[3].replace("\"", "")).split(","))[1]));
					Coordinates end = new Coordinates(
							Integer.parseInt(((parameters[4].replace("\"", "")).split(","))[0]),
							Integer.parseInt(((parameters[4].replace("\"", "")).split(","))[1]));
					Integer duration = Integer.parseInt(parameters[5]);
					//boolean malfunction = "da".equals(parameters[6]);
					boolean malfunction = (parameters[6]).trim().equalsIgnoreCase("da");
					
					
					boolean promotion = "da".equals(parameters[7]);
	 
					 Vehicle vehicle = vehicleMap.get(idVehicle);
					 if(vehicle == null) throw new Exception();
					 Vehicle newVehicle = null;;
					 if (vehicle instanceof Automobile)
						 newVehicle =(Automobile)vehicle.clone();
					 else if (vehicle instanceof Scooter)
						 newVehicle =(Scooter)vehicle.clone();
					 else if (vehicle instanceof Bicycle)
						 newVehicle =(Bicycle)vehicle.clone();
					if(malfunction)
						{
						newVehicle.setMalfunction(new Malfunction("Pokvareno vozilo_" + idVehicle, ldt));
						newVehicle.setMalfunctioned(malfunction);}
				
					listRentals.add(new Rental(ldt, customer, newVehicle, start, end, duration, malfunction, promotion));
					
				} catch (Exception e) { 
					e.printStackTrace();
				}
			}
		} catch (Exception e) {e.printStackTrace();	}
		 return listRentals.stream()
		            .sorted(Comparator.comparing(Rental::getDateAndTime))
		            .collect(Collectors.toList());
	}

	@Override
	public String toString() {
		return "Rental{" + "vehicle= " + vehicle + ", dateAndTime= " + dateAndTime + ", customer= " + customer
				+ ", takeOnLocation= " + takeOnLocation + ", returnLocation= " + returnLocation + ", duration= " + duration
				+ ", currentLocation= " + currentLocation + '}';
	}
	

	public Vehicle getVehicle() {
		return this.vehicle;
	}

	public int distance() {
		return Math.abs(returnLocation.getX() - takeOnLocation.getX())
				+ Math.abs(returnLocation.getY() - takeOnLocation.getY());
	}

	public boolean isMalfunctioned() {
		return this.vehicle.isMalfunctioned();
	}

	public void setGrid(JLabel[][] grid) {
		this.grid = grid;
	}

	public Customer getCustomer() {
		return this.customer;
	}

	public int getDuration() {
		return this.duration;
	}

	public void setDateAndTime(LocalDateTime dateAndTime) {
		this.dateAndTime = dateAndTime;
	}

	public LocalDateTime getDateAndTime() {
		return this.dateAndTime;
	}

	public static List<Receipt> getReceipts() {
		return receipts;
	}
	public static int getNumOfRentals() {
		return numOfRentals;
	}

	public static void setNumOfRentals(int numOfRentals) {
		Rental.numOfRentals = numOfRentals;
	}

	public boolean isTenth() {
		return tenthFlag;
	}
	
	
	@Override
	// TODO
	public boolean equals(Object o) {
		if (o != null && o instanceof Rental && (this.vehicle.getID().equals((((Rental)o).getVehicle().getID()))) && 
				dateAndTime.equals(((Rental)o).getDateAndTime()))
			return true;
		else
			return false;
	}

	@Override
	public int hashCode() {
	    return Objects.hash(vehicle.getID(), dateAndTime);
	}

	public void setTenth(boolean tenthFlag) {
		this.tenthFlag = tenthFlag;
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
