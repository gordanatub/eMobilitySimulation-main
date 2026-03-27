# eMobilitySimulation
The system handles all aspects of e-mobility operations from rental simulation to financial analysis and maintenance cost tracking.

This is a Java-based simulation project for an electric vehicle rental system. It models the rental process for vehicles such as automobiles, bicycles, and scooters, including features like customer management, vehicle malfunctions, receipt generation, and reporting. The project uses multithreading for simulating concurrent rentals and includes a basic GUI for visualizing vehicle movements on a grid representing a city.
The project appears to be developed as part of a university assignment (based on package naming from University of Banja Luka, ETF). Comments in the code are primarily in Serbian, with English variable and class names.
![image alt](https://github.com/gordanatubonjic/eMobilitySimulation/blob/main/screenshots/Map.png?raw=true)
![image alt](https://github.com/gordanatubonjic/eMobilitySimulation/blob/main/screenshots/Vehicles.png?raw=true)
![image alt](https://github.com/gordanatubonjic/eMobilitySimulation/blob/main/screenshots/Malfunctions.png?raw=true)
![image alt](https://github.com/gordanatubonjic/eMobilitySimulation/blob/main/screenshots/Report.png?raw=true)
![image alt](https://github.com/gordanatubonjic/eMobilitySimulation/blob/main/screenshots/Repairs.png?raw=true)

Table of Contents

- Features
- Project Structure
- Requirements
- Setup and Installation
- Usage
- Configuration
- Contributing
- License

Features

* Vehicle Management: Supports electric automobiles, bicycles, and scooters with attributes like ID, manufacturer, model, battery level, and purchase price.
* Customer Handling: Manages local and foreign customers with IDs, driver's licenses, or passports.
* Rental Simulation: Uses threads to simulate vehicle movement from pickup to drop-off locations on a grid-based city map.
* Malfunction Handling: Vehicles can malfunction during rentals, triggering repair costs and logging.
* Receipt Generation: Calculates costs based on duration, distance (inner/outer city), discounts, and promotions; outputs to text files.
* Reporting: Generates daily and summary reports on income, discounts, promotions, maintenance, repairs, taxes, and expenses.
* Serialization/Deserialization: Handles saving and loading malfunctioned vehicles as binary files.
* GUI Integration: A simple Swing-based frame (TravelSimulationFrame) to visualize the simulation grid.
* Properties Configuration: Uses a properties file for customizable parameters like unit prices, icons, and file paths.

Project Structure
The project is organized under the package org.unibl.etf.pj2.ePJ2. Key classes include:

app/Simulation.java: Entry point; loads properties, starts GUI, parses rentals, and generates reports.

entity/:

* Vehicle.java (abstract): Base for all vehicles.
* Automobile.java, Bicycle.java, Scooter.java: Concrete vehicle implementations.
* Customer.java: Represents renters.
* Malfunction.java: Logs vehicle faults.
* MalfunctionedVehicle.java: Pairs vehicles with repair costs for serialization.


rental/:

* Rental.java: Thread-based class for simulating each rental (movement, malfunctions).
* Receipt.java: Calculates and prints bills.


stats/:

* Report.java (abstract): Base for reports.
* DailyReport.java: Aggregates daily metrics.
* SummaryReport.java: Generates overall business summaries, including taxes and expenses.


gui/TravelSimulationFrame.java: (Assumed based on code references) GUI for simulation visualization.
helper/Properties.properties: Configuration file for paths, prices, icons, etc.

Input files (e.g., vehicles.csv, rentals.csv) are parsed for data.
Requirements

Java JDK 8 or higher.
No external dependencies (uses standard Java libraries like Swing for GUI).
A properties file configured with paths to input files (vehicles, rentals) and output directories.

Setup and Installation

Clone the Repository:
textgit clone https://github.com/your-username/electric-vehicle-rental-simulation.git
cd electric-vehicle-rental-simulation

Configure Properties:

Edit src/org/unibl/etf/pj2/ePJ2/helper/Properties.properties to point to your input files (e.g., vehicles=/path/to/vehicles.csv, rentals=/path/to/rentals.csv).
Ensure directories for receipts and malfunction logs exist (e.g., receiptsFolder=/path/to/receipts, malfunctionCostsPath=/path/to/malfunctions).


Compile the Project:

Use an IDE like Eclipse or IntelliJ, or compile via command line:
textjavac -d bin src/org/unibl/etf/pj2/ePJ2/app/*.java src/org/unibl/etf/pj2/ePJ2/entity/*.java src/org/unibl/etf/pj2/ePJ2/rental/*.java src/org/unibl/etf/pj2/ePJ2/stats/*.java src/org/unibl/etf/pj2/ePJ2/gui/*.java




Usage

Run the Simulation:

From an IDE: Run Simulation.main() as a Java application.
Command line:
textjava -cp bin org.unibl.etf.pj2.ePJ2.app.Simulation

This will:

Load properties and start the GUI.
Parse rentals and vehicles.
Simulate rentals concurrently (vehicles move on the grid).
Generate receipts for each rental.
Output malfunctioned vehicles and summary reports to console/files.




Input Files:

vehicles.csv: Format: ID,Manufacturer,Model,PurchaseDate,PurchasePrice,Autonomy/MaxSpeed,Description,Type (e.g., "automobil").
rentals.csv: Format: DateTime,Customer,VehicleID,StartCoords,EndCoords,Duration,Malfunction(Y/N),Promotion(Y/N).


Output:

Receipts: TXT files in the configured receipts folder.
Malfunctioned Vehicles: BIN files in the malfunction costs path.
Reports: Printed to console (income, discounts, repairs, etc.).



Configuration
Key properties in Properties.properties:

vehicles: Path to vehicles input file.
rentals: Path to rentals input file.
CAR_UNIT_PRICE, BIKE_UNIT_PRICE, etc.: Pricing quotients.
CAR_ICON, BICYCLE_ICON, etc.: Paths to vehicle icons for GUI.
DISCOUNT, DISCOUNT_PROM: Discount rates.
receiptsFolder: Output directory for receipts.
malfunctionCostsPath: Output directory for malfunction serializations.

Contributing
Contributions are welcome! Please follow these steps:

Fork the repository.
Create a feature branch (git checkout -b feature/YourFeature).
Commit your changes (git commit -m 'Add YourFeature').
Push to the branch (git push origin feature/YourFeature).
Open a Pull Request.
