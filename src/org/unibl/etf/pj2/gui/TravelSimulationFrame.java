package org.unibl.etf.pj2.gui;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import org.unibl.etf.pj2.ePJ2.entity.Automobile;
import org.unibl.etf.pj2.ePJ2.entity.Bicycle;
import org.unibl.etf.pj2.ePJ2.entity.Malfunction;
import org.unibl.etf.pj2.ePJ2.entity.MalfunctionedVehicle;
import org.unibl.etf.pj2.ePJ2.entity.Scooter;
import org.unibl.etf.pj2.ePJ2.entity.Vehicle;
import org.unibl.etf.pj2.ePJ2.rental.Rental;
import org.unibl.etf.pj2.ePJ2.stats.DailyReport;
import org.unibl.etf.pj2.ePJ2.stats.SummaryReport;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

public class TravelSimulationFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	public static void startSimulation() {
		String filePath = "./src/org/unibl/etf/pj2/ePJ2/helper/Properties.properties";
		Properties properties;
		properties = new Properties();
		try {
			FileInputStream fis = new FileInputStream(filePath);
			properties.load(fis);

		} catch (Exception e) {
		}
		;
		Automobile a = new Automobile("IDAuta", "Mercedes", "Model",new BigDecimal("20000.00"), "icon", LocalDate.now(), "opis");
		a.setMalfunction(new Malfunction("Pokvareno auto", LocalDateTime.now()));new SummaryReport();	
		
		
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Simulacija poslovanja");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(800, 600);

			JTabbedPane tabbedPane = new JTabbedPane();
			JPanel mapPanel = new JPanel();
			mapPanel.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
			mapPanel.setBounds(10, 11, 721, 522);
			mapPanel.setLayout(new GridLayout(20, 20, 0, 0));
			tabbedPane.addTab("Mapa", mapPanel);

			int gridSize = 20;
			JLabel grid[][] = new JLabel[gridSize][gridSize];
			for (int i = 0; i < gridSize; i++)
				for (int j = 0; j < gridSize; j++) {
					grid[i][j] = new JLabel();
					grid[i][j].setOpaque(true);
					grid[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
						if(i>5 && i<14 && j>5 && j< 14)
						{
							grid[i][j].setBackground(Color.GRAY);
							
						}
						else
					grid[i][j].setBackground(Color.LIGHT_GRAY);
					grid[i][j].setForeground(Color.WHITE);
					mapPanel.add(grid[i][j]);
				}
			new Automobile();
			
			// Ekran za prikaz vozila
			List<Rental> rentals = null; 

			try {
				rentals = Rental.parseRentals(properties.getProperty("rentals"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			;
			
			for(Rental r : rentals)
				r.setGrid(grid);
			
			Rental.scheduleRentalSimulation(rentals);
			
			List<Vehicle> vehicles = Vehicle.parseVehicles(properties.getProperty("vehicles"));

			List<Automobile> automobiles = new ArrayList<Automobile>();
			List<Scooter> scooters = new ArrayList<Scooter>();
			List<Bicycle> bicycles = new ArrayList<Bicycle>();

			for (Vehicle v : vehicles) {
				if (v instanceof Automobile)
					automobiles.add((Automobile) v);
				else if (v instanceof Scooter)
					scooters.add((Scooter) v);
				else if (v instanceof Bicycle)
					bicycles.add((Bicycle) v);
			}

			JPanel vehiclePanel = new JPanel();
			vehiclePanel.setLayout(new BoxLayout(vehiclePanel, BoxLayout.Y_AXIS));

			//Auta
			JPanel autoPanel = new JPanel();
			autoPanel.setLayout(new BorderLayout());

			JLabel autoPanelTitle = new JLabel("AUTOMOBILI", JLabel.CENTER);
			autoPanelTitle.setFont(new Font("Arial", Font.BOLD, 14));
			autoPanel.add(autoPanelTitle, BorderLayout.NORTH);

			
			String[] automobileColumns = { "ID", "PROIZVODJAC", "MODEL", "CIJENA IZNAJMLJIVANJA", "DATUM NABAVKE", "OPIS" };

			Object[][] automobilesData = automobiles.stream().map(w -> new Object[] { w.getID(), w.getManufacturer(),
					w.getModel(), w.getPurchasePrice(), w.getPurchaceDate(),  w.getDescription() })
					.toArray(Object[][]::new);

			JTable autoTable = new JTable(new DefaultTableModel(automobilesData, automobileColumns));
			JScrollPane tableScrollPane = new JScrollPane(autoTable);
			autoTable.setFillsViewportHeight(true);
			vehiclePanel.add(tableScrollPane, BorderLayout.CENTER);
			vehiclePanel.add(autoPanel);
			vehiclePanel.add(Box.createRigidArea(new Dimension(0, 10))); // Razmak između panela

			//Trotineti
			JPanel scooterPanel = new JPanel();
			scooterPanel.setLayout(new BorderLayout());

			JLabel scooterPanelTitle = new JLabel("TROTINETI", JLabel.CENTER);
			scooterPanelTitle.setFont(new Font("Arial", Font.BOLD, 14));
			scooterPanel.add(scooterPanelTitle, BorderLayout.NORTH);

			String[] scooterColumns = { "ID", "PROIZVODJAC", "MODEL", "CIJENA IZNAJMLJIVANJA", "KVAR", "MAKS. BRZINA" };

			Object[][] scootersData = scooters.stream().map(w -> new Object[] { w.getID(), w.getManufacturer(),
					w.getModel(), w.getPurchasePrice(), w.isMalfunctioned(), w.getMaxSpeed() })
					.toArray(Object[][]::new);

			JTable scooterTable = new JTable(new DefaultTableModel(scootersData, scooterColumns));
			JScrollPane tableScrollPane2 = new JScrollPane(scooterTable);
			scooterTable.setFillsViewportHeight(true);
			vehiclePanel.add(tableScrollPane2, BorderLayout.CENTER);
			
			vehiclePanel.add(scooterPanel);
			vehiclePanel.add(Box.createRigidArea(new Dimension(0, 10))); 
			//Bicikli
			JPanel bicyclePanel = new JPanel();
			bicyclePanel.setLayout(new BorderLayout());

			JLabel bicyclePanelTitle = new JLabel("BICIKLI", JLabel.CENTER);
			bicyclePanelTitle.setFont(new Font("Arial", Font.BOLD, 14));
			bicyclePanel.add(bicyclePanelTitle, BorderLayout.NORTH);

			String[] bicycleColumns = { "ID", "PROIZVODJAC", "MODEL", "CIJENA IZNAJMLJIVANJA", "AUTONOMIJA" };

			Object[][] bicycleData = bicycles.stream().map(w -> new Object[] { w.getID(), w.getManufacturer(),
					w.getModel(), w.getPurchasePrice(), w.getAutonomy() })
					.toArray(Object[][]::new);

			JTable bicycleTable = new JTable(new DefaultTableModel(bicycleData, bicycleColumns));
			JScrollPane bicycleScrollPane3 = new JScrollPane(bicycleTable);
			bicycleTable.setFillsViewportHeight(true);
			vehiclePanel.add(bicycleScrollPane3, BorderLayout.CENTER);
			vehiclePanel.add(bicyclePanel);
			vehiclePanel.add(Box.createRigidArea(new Dimension(0, 10))); // Razmak između panela

			tabbedPane.addTab("Vozila", vehiclePanel);

			//Kvarovi
			List<Rental> rentals2 = null; 

			try {
				rentals2 = Rental.parseRentals(properties.getProperty("rentals"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			;
			
			JPanel faultsPanel = new JPanel(new BorderLayout());
			String[] faultColumns = { "Tip vozila", "ID", "Vrijeme", "Opis kvara" };
			List<Rental> malfunctionedVehicles = rentals2.stream().filter(v -> (v.isMalfunctioned()))
					.collect(Collectors.toList());
			
			Object[][] faultData = malfunctionedVehicles.stream()
					.map(w -> new Object[] { w.getVehicle().getClass().getSimpleName(), w.getVehicle().getID(),
							w.getDateAndTime(), w.getVehicle().getManufacturer() })
					.toArray(Object[][]::new);
			JTable faultTable = new JTable(faultData, faultColumns);
			faultsPanel.add(new JScrollPane(faultTable), BorderLayout.CENTER);
			tabbedPane.addTab("Kvarovi", faultsPanel);

			//Rezultati poslovanja
			JPanel resultsPanel = new JPanel();
			tabbedPane.addTab("Rezultati", resultsPanel);

			resultsPanel.setLayout(new BorderLayout());
			
			JTabbedPane nestedTabbedPane = new JTabbedPane();

			//Sumarni izvjestaj
			JPanel summaryPanel = new JPanel();
			summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));

			String[] summaryColumnNames = { "Opis", "Vrijednost" };
			DefaultTableModel summaryTableModel = new DefaultTableModel(summaryColumnNames, 0);
			JTable summaryTable = new JTable(summaryTableModel);
			JScrollPane summaryScrollPane = new JScrollPane(summaryTable);

			JButton summaryButton = new JButton("Generiši izvještaj");
			summaryButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {

					SummaryReport summaryReport = new SummaryReport(Rental.getReceipts());
					summaryReport.generate();
					summaryTableModel.setRowCount(0);
					summaryTableModel.addRow(new Object[] { "Ukupan prihod", summaryReport.getTotalIncome() });
					summaryTableModel.addRow(new Object[] { "Ukupan popust", summaryReport.getTotalDiscount() });
					summaryTableModel.addRow(new Object[] { "Ukupno promocije", summaryReport.getTotalPromotion() });
					summaryTableModel.addRow(new Object[] { "Vožnje širi  grad", summaryReport.getTotalWideRidesIncome() });
					summaryTableModel.addRow(new Object[] { "Vožnje uži grad", summaryReport.getTotalNarrowRidesIncome() });
					summaryTableModel.addRow(new Object[] { "Održavanje", summaryReport.getTotalMaintenanceCost() });
					summaryTableModel.addRow(new Object[] { "Popravke kvarova", summaryReport.getTotalRepairCosts() });
					summaryTableModel.addRow(new Object[] { "Troškovi kompanije", summaryReport.totalExpenses() });
					summaryTableModel.addRow(new Object[] { "Porez", summaryReport.totalTax() });
				}
			});

			summaryPanel.add(summaryScrollPane);
			summaryPanel.add(summaryButton, BorderLayout.CENTER);

			//Dnevni izvjestaj
			JPanel dailyPanel = new JPanel();
			dailyPanel.setLayout(new BoxLayout(dailyPanel, BoxLayout.Y_AXIS));

			String[] dailyColumnNames = { "Datum", "Prihod", "Popust", "Promocije", "Iznos voznji", "Odrzavanje", "Popravke" };
			DefaultTableModel dailyTableModel = new DefaultTableModel(dailyColumnNames, 0);
			JTable dailyTable = new JTable(dailyTableModel);
			JScrollPane dailyScrollPane = new JScrollPane(dailyTable);

			JButton dailyButton = new JButton("Generiši izvještaj");
			dailyButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dailyTableModel.setRowCount(0);
					List<DailyReport> dailyReport = new DailyReport().generateList(Rental.getReceipts());
					for (DailyReport r : dailyReport)
						dailyTableModel.addRow(new Object[] { r.getDateAndTime().toLocalDate(), r.getTotalIncome(),
								r.getTotalDiscount(), r.getTotalPromotion(), r.getTotalIncomeFromRide(), r.getTotalMaintenanceCost(),r.getTotalRepairCosts() });
				}
			});

			dailyPanel.add(dailyScrollPane);
			dailyPanel.add(dailyButton, BorderLayout.CENTER);

			nestedTabbedPane.addTab("Sumarni izvještaj", summaryPanel);
			nestedTabbedPane.addTab("Dnevni izvještaj", dailyPanel);

			resultsPanel.add(nestedTabbedPane, BorderLayout.CENTER);

			tabbedPane.addTab("Rezultati", resultsPanel);

			// Prikaz pokvarenih vozila i cijene popravke

			JPanel maintainancePanel = new JPanel(new BorderLayout());
			
			tabbedPane.addTab("Popravke", maintainancePanel);
			String[] maintainColumns = { "Tip vozila", "ID", "Proizvodjac", "Model", "Vrijeme", "Opis kvara", "Cijena popravke" };
			SummaryReport.findMalfunctionedVehicles(rentals2);
			
			List<MalfunctionedVehicle> malfunctionedVehicles2 = SummaryReport.deserializeMalfunctionedVehicles(Paths.get( properties.getProperty("malfunctionCostsPath")).toFile());
			Object[][] maintainData = malfunctionedVehicles2.stream()
					.map(w -> new Object[] { w.getVehicle().getClass().getSimpleName(), w.getVehicle().getID(),  w.getVehicle().getManufacturer(), w.getVehicle().getModel(),
							w.getVehicle().getMalfunction().getDateAndTime(), w.getVehicle().getMalfunction().getDescription(), w.getRepairCost() }) 
					.toArray(Object[][]::new);	

			JTable maintainTable = new JTable(maintainData, maintainColumns);
			maintainancePanel.add(new JScrollPane(maintainTable), BorderLayout.CENTER);
			tabbedPane.addTab("Popravke", maintainancePanel);
		

			frame.add(tabbedPane);
			frame.setVisible(true);
			
		});
	};
}
