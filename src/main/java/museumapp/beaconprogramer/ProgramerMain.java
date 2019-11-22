/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumapp.beaconprogramer;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;
import jssc.*;

/**
 *
 * @author mdu18
 */
public class ProgramerMain {

    private static ArrayList<Exhibit> EList;

    private static ArrayList<Bluetooth> BList;

    private static Connection conn;

    private static String BeaconPort;

    private static Process p;
    private static Thread cmdErr;
    private static Thread cmdOut;
    private static PrintWriter stdin;

    private final static String MAIN_PATH = System.getProperty("user.dir");
    private final static String BEACON_C_PATH = MAIN_PATH + "\\Beacon\\Workspace\\Projects\\ble_app_blinky\\main.c";
    private final static String MAKEFILE = MAIN_PATH + "\\Beacon\\Workspace\\Projects\\ble_app_blinky\\pca10059\\s140\\armgcc";

    private static Scanner sc = new Scanner(System.in);

    private static PrintWriter pw() throws IOException {
        try {
            p = Runtime.getRuntime().exec("cmd");
            cmdErr = new Thread(new SyncPipe(p.getErrorStream(), System.err));
            cmdOut = new Thread(new SyncPipe(p.getInputStream(), System.out));
            cmdErr.start();
            cmdOut.start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new PrintWriter(p.getOutputStream());
    }

    private static void RunCommand(ArrayList<String> input) {
        try {
            stdin = pw();
            for (String i : input) {
                stdin.println(i);
            }
            stdin.close();
            p.waitFor();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void InteractiveMode() {
        while (true) {
            ArrayList<String> input = new ArrayList<String>();
            input.add(sc.nextLine());
            RunCommand(input);
        }
    }

    private static void setBeaconPort() {
        String[] portNames = SerialPortList.getPortNames();

        if (portNames.length == 0 || portNames.length > 1) {
            if (portNames.length == 0) {
                System.out.println("Beacon not found...");
            }
            if (portNames.length > 1) {
                System.out.println("Multiple devices detected, you can only program one beacon at a time...");
            }
            System.out.println("Press Enter to exit...");
            try {
                System.in.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.exit(-1);
        }

        BeaconPort = portNames[0];
        System.out.println("Beacon Port Name : " + portNames[0]);
    }

    private static boolean ReadInData() {
        String selectSql = "SELECT * FROM EXHIBIT ";

        try ( Statement statement = conn.createStatement();  ResultSet resultSet = statement.executeQuery(selectSql)) {

            while (resultSet.next()) {
                EList.add(new Exhibit(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3)));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        selectSql = "SELECT * FROM BLUETOOTH";

        try ( Statement statement = conn.createStatement();  ResultSet resultSet = statement.executeQuery(selectSql)) {

            while (resultSet.next()) {
                BList.add(new Bluetooth(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3)));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return (EList != null);
    }

    private static Bluetooth NewBeacon() {
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
        } while (confirm.equalsIgnoreCase("no"));

        Bluetooth b;
        confirm = "";
        do {
            do {
                System.out.println("Please enter the new bluetooth beacon name");
                String beaconName = sc.nextLine();
                b = BList.stream().filter(x -> beaconName == x.getName()).findFirst().orElse(null);
                if (b != null) {
                    System.out.println("The name you entered is taken");
                } else {
                    int newBID = BList.size() + 1;
                    b = new Bluetooth(newBID, beaconName, selected.getID());
                    break;
                }
            } while (b != null);
            System.out.println("Beacon is named with" + b.getName() + " , please confirm. ----- YES/NO");
            confirm = sc.nextLine();
        } while (confirm.equalsIgnoreCase("no"));

        return b;
    }

    private static void NameBeacon(Bluetooth beacon) {
        try {
            Path path = Paths.get(BEACON_C_PATH);
            Charset cset = StandardCharsets.UTF_8;
            String content = new String(Files.readAllBytes(path), cset);
            content = content.replaceAll("#define DEVICE_NAME \"\" ", "#define DEVICE_NAME \"" + beacon.getName() + "\" ");
            Files.write(path, content.getBytes(cset));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void CompileBeaconC(Bluetooth beacon) {
        NameBeacon(beacon);

        ArrayList<String> cmd = new ArrayList<String>();

        cmd.add("cd " + MAKEFILE);
        cmd.add("rmdir /q /s _build");
        cmd.add("make");

        RunCommand(cmd);
        ResetBeaconC(beacon);
    }

    private static void InsertBluetooth(Bluetooth b) {
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

    private static void ResetBeaconC(Bluetooth beacon) {
        try {
            Path path = Paths.get(BEACON_C_PATH);
            Charset cset = StandardCharsets.UTF_8;
            String content = new String(Files.readAllBytes(path), cset);
            content = content.replaceAll("#define DEVICE_NAME \"" + beacon.getName() + "\" ", "#define DEVICE_NAME \"\" ");
            Files.write(path, content.getBytes(cset));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        if (!SysCheck() || IsFirstTime()) {
            System.out.println("\n*************System Check **************"
                    + "\n Please make sure you have following dependencies"
                    + "\n gcc-arm-none-eabi"
                    + "\n Python 2.7"
                    + "\n nRF Util python package"
                    + "\n nRF Command Line Tools"
                    + "\n*******************************************");
            System.exit(-1);
        }

        Database db = new Database();

        conn = db.connect();

        EList = new ArrayList<Exhibit>();

        BList = new ArrayList<Bluetooth>();

//        if (!ReadInData()) {
//            System.out.println("Failed to read from database");
//            System.exit(-1);
//        }
//        System.out.println("All on record exhibits: ");
//        for (Exhibit e : EList) {
//            System.out.println(e.toString() + "\n");
//        }
//        
//        Bluetooth beacon = NewBeacon();
//        CompileBeaconC(beacon);
//        setBeaconPort();
        try {

            String isPython = "";

            System.out.println(isPython);
//            IOUtils.copy(Runtime.getRuntime().exec("cmd python --version").getInputStream(), writer, "UTF-8");
//
//            String isPython = writer.toString();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        //InsertBluetooth(b);
        //nrfutil pkg generate --hw-version 52 --debug-mode --sd-req 0xCA --sd-id 0xCA --application .\nrf52840_xxaa.hex --softdevice .\s140_nrf52_7.0.1_softdevice.hex beacon.zip
        //nrfutil dfu usb-serial -pkg beacon.zip -p COM5 -b 115200
    }

    private static boolean IsFirstTime() {
        URL main = Main.class.getResource("Main.class");
        File javaFile = new File(main.getPath());

        String absolutePath = javaFile.getAbsolutePath();
        String javaFileFolderPath = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));
        String txtFilePath = javaFileFolderPath + "\\wantedTxt.txt";

        File txtFile = new File(txtFilePath);
        if (txtFile.exists() && !txtFile.isDirectory()) return false; 
        return true; 
    }

    private static boolean SysCheck() {
        System.getProperties().list(System.out);
        if (!System.getProperty("os.name").equalsIgnoreCase("Windows 10")) {
            return false;
        }
        return true;
    }
}
