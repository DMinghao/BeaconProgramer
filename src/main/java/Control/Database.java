/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import Entity.Bluetooth;
import Entity.Exhibit;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @version 1.0
 * @author mdu18
 */
public class Database {

    private static ArrayList<Exhibit> EList;
    private static ArrayList<Bluetooth> BList;
    private static Connection conn;
    
    // Connect to database
    private static final String HOST_NAME = "alabamamuseum.database.windows.net"; // update me
    private static final String DB_NAME = "MuseumApp"; // update me
    private static final String USER = "museumadmin"; // update me
    private static final String PASSWORD = "Alabama123"; // update me
    private static final String CONNECT_URL = String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;" 
                                            + "hostNameInCertificate=*.database.windows.net;loginTimeout=30;", HOST_NAME, DB_NAME, USER, PASSWORD);

    private static Scanner sc = new Scanner(System.in);

    /**
     * @apiNote The {@link Database Database} class constructor. 
     * It will initialize {@link Exhibit Exhibit} and {@link Bluetooth Bluetooth} {@link ArrayList Arraylists} and try to establish a database connection by calling {@link connect() connect()}
     */
    public Database() {
    	EList = new ArrayList<Exhibit>(); 
    	BList = new ArrayList<Bluetooth>();
        conn = connect();
        if (!ReadInData()) {
            System.out.println("Failed to read from database");
            System.exit(-1);
        }
    }

    /**
     * @apiNote This method will try to establish a connection to Microsoft Azure database based on hard-coded database login information
     * @return A {@link Connection Connection} object may or may not connected
     */
    private static Connection connect() {
        Connection connection = null;
        
        try {
            connection = DriverManager.getConnection(CONNECT_URL);
            String schema = connection.getSchema();
            System.out.println("Successful connection - Schema: " + schema);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
    }

    /**
     * @apiNote This method will read all {@link Exhibit Exhibit} and {@link Bluetooth Bluetooth} data from the remote database and store them into two separate {@link ArrayList Arraylists}
     * @return {@link Boolean boolean} to indicate if the {@link Exhibit Exhibit} {@link ArrayList Arraylist} is successfully populated with data came from remote database
     */
    private static boolean ReadInData() {
        String selectSql = "SELECT * FROM EXHIBIT ";

        try (Statement statement = conn.createStatement(); ResultSet resultSet = statement.executeQuery(selectSql)) {
            while (resultSet.next()) {
                EList.add(new Exhibit(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3)));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        selectSql = "SELECT * FROM BLUETOOTH";

        try (Statement statement = conn.createStatement(); ResultSet resultSet = statement.executeQuery(selectSql)) {
            while (resultSet.next()) {
                BList.add(new Bluetooth(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3)));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        System.out.println("All on record exhibits: ");
        for (Exhibit e : EList) {
            System.out.println(e.toString() + "\n");
        }

        return (EList != null);
    }

    /**
     * @apiNote This method will ask the user to select a on record {@link Exhibit Exhibit} that the new {@link Bluetooth beacon} will bind to once it's created
     * @return A new {@link Bluetooth Bluetooth} object
     */
    public static Bluetooth NewBeacon() {
        
        Exhibit selected;
        String confirm = "";
        do {
            do {
                System.out.println("Please enter an Exhibit ID to pair your new bluetooth beacon ");
                int SelectedExhibit = Integer.parseInt(sc.nextLine());
                selected = EList.stream().filter(x -> SelectedExhibit == x.getID()).findFirst().orElse(null);
            } while (selected == null);
            System.out.println("Adding a beacon to exhibit " + selected.getID() + " , please confirm. ----- YES/NO");
            confirm = sc.nextLine();
        } while (confirm.equalsIgnoreCase("no") && !confirm.equalsIgnoreCase("yes"));

        Bluetooth b;
        confirm = "";
        do {
            do {
                System.out.println("Please enter the new bluetooth beacon name");
                String beaconName = sc.nextLine();
                b = BList.stream().filter(x -> beaconName.equals(x.getName())).findFirst().orElse(null);
                if (b != null) {
                    System.out.println("The name you entered is taken");
                } else {
                    int newBID = BList.size() + 1;
                    b = new Bluetooth(newBID, beaconName, selected.getID());
                    break;
                }
            } while (b != null);
            System.out.println("Beacon is named with " + b.getName() + " , please confirm. ----- YES/NO");
            confirm = sc.nextLine();
        } while (confirm.equalsIgnoreCase("no") && !confirm.equalsIgnoreCase("yes"));

        return b;
    }

    /**
     * @apiNote This method will ask the user if they want to insert a new beacon record into the database
     * @param b A {@link Bluetooth Bluetooth} object
     */
    public static void InsertBluetooth(Bluetooth b) {
        System.out.println("Do you want to insert the beacon to the database? (Yes / No)");

        if (sc.nextLine().equalsIgnoreCase("yes")) {
            String sql = "INSERT INTO BLUETOOTH (BT_ID, BT_NAME, EX_ID)"
                    + "VALUES ("
                    + b.getID() + ", '"
                    + b.getName() + "', "
                    + b.getExID()
                    + ");";
            try {
                Statement statement = conn.createStatement();
                statement.executeUpdate(sql);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

    }

}
