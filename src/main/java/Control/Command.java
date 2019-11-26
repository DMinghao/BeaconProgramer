/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import Entity.SyncPipe;
import Entity.Bluetooth;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import jssc.SerialPortList;

/**
 *
 * @author duter
 */
public class Command {

    private static Process p;
    private static Thread cmdErr;
    private static Thread cmdOut;
    private static PrintWriter stdin;

    private final static String MAIN_PATH = System.getProperty("user.dir");
    private final static String BEACON_C_PATH = MAIN_PATH + "\\Beacon\\Workspace\\Projects\\ble_app_blinky\\main.c";
    private final static String MAKEFILE = MAIN_PATH + "\\Beacon\\Workspace\\Projects\\ble_app_blinky\\pca10059\\s140\\armgcc";

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

    public static String getBeaconPort() {
        String[] portNames = SerialPortList.getPortNames();

        if (portNames.length == 0 || portNames.length > 1) {
            if (portNames.length == 0) {
                System.out.println("/nBeacon not found...");
            }
            if (portNames.length > 1) {
                System.out.println("/nMultiple devices detected, you can only program one beacon at a time...");
            }
            System.out.println("Press Enter to exit...");
            try {
                System.in.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.exit(-1);
        }
        System.out.println("Beacon Port Name : " + portNames[0]);
        return portNames[0];

    }

    public static void CompileBeaconC(Bluetooth beacon) {
        NameBeacon(beacon);

        ArrayList<String> cmd = new ArrayList<String>();

        cmd.add("cd " + MAKEFILE);
        cmd.add("rmdir /q /s _build");
        cmd.add("make");
        cmd.add("xcopy s140_nrf52_7.0.1_softdevice.hex _build");

        RunCommand(cmd);
        ResetBeaconC(beacon);
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

    public static void ProgramBeacon(String BeaconPort) {
        ArrayList<String> cmd = new ArrayList<String>();
        cmd.add("cd " + MAKEFILE + "\\_build");
        cmd.add("powershell");
        cmd.add("nrfutil pkg generate --hw-version 52 --debug-mode --sd-req 0xCA --sd-id 0xCA --application .\\nrf52840_xxaa.hex --softdevice .\\s140_nrf52_7.0.1_softdevice.hex beacon.zip");
        cmd.add("nrfutil dfu usb-serial -pkg beacon.zip -p " + BeaconPort + " -b 115200");
        RunCommand(cmd);
    }

}
