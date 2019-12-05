/*
 * The MIT License
 *
 * Copyright 2019 Shuyuan Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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

    /**
     * @apiNote A helper method that make a one time use printwriter that pipes cmd commands to system cmd
     * @return A {@link PrintWriter PrintWriter} that prints command to system cmd
     * @throws IOException 
     */
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

    /**
     * @apiNote A helper method that takes in an arraylist of strings as cmd command and execute them in order
     * @param input An arraylist of cmd command strings which will execute in the sequence of that arraylist 
     */
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

    /**
     * @apiNote This method will detect if any unprogrammed beacon is plugged in to the machine. 
     * If there are more than one device is plugged in or no device is detected, the system will exit. 
     * @return A {@link String Beacon Port string} that represented of which the beacon is inserted in
     */
    public static String getBeaconPort() {
        String[] portNames = SerialPortList.getPortNames();

        if (portNames.length == 0 || portNames.length > 1) {
            if (portNames.length == 0) {
                System.out.println("\nBeacon not found...");
            }
            if (portNames.length > 1) {
                System.out.println("\nMultiple devices detected, you can only program one beacon at a time...");
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

    /**
     * @apiNote This method will take in a bluetooth object and call {@link NameBeacon(Bluetooth) NameBeacon}({@link Bluetooth Bluetooth}) to set the beacon name
     *  then it will compile the C file for that beacon and reset the C file to its initial state by calling {@link ResetBeacon(Bluetooth) ResetBeacon}({@link Bluetooth Bluetooth})
     * @param beacon A {@link Bluetooth Bluetooth} object
     */
    public static void CompileBeaconC(Bluetooth beacon) {
        NameBeacon(beacon);

        ArrayList<String> cmd = new ArrayList<String>();

        cmd.add("cd " + MAKEFILE);
        cmd.add("rmdir /q /s _build");
        cmd.add("make");
        cmd.add("copy s140_nrf52_7.0.1_softdevice.hex _build");

        RunCommand(cmd);
        ResetBeacon(beacon);
    }

    /**
     * @apiNote A helper method that takes in a {@link Bluetooth Bluetooth} object to rename the beacon name in the C file 
     * @param beacon A {@link Bluetooth Bluetooth} object 
     */
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

    /**
     * @apiNote A helper method that takes in a {@link Bluetooth Bluetooth} object to reset the beacon name in the C file back to initial state
     * @param beacon A {@link Bluetooth Bluetooth} object 
     */
    private static void ResetBeacon(Bluetooth beacon) {
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
     * @Magic This is where the magic happens 
     * @apiNote This method will utilize the nrfUtil python package to program the beacon on a designated port
     * @param BeaconPort A {@link String string} that represents the port number of which the beacon has plugged in
     */
    public static void ProgramBeacon(String BeaconPort) {
        ArrayList<String> cmd = new ArrayList<String>();
        cmd.add("cd " + MAKEFILE + "\\_build");
        cmd.add("powershell");
        cmd.add("nrfutil pkg generate --hw-version 52 --debug-mode --sd-req 0xCA --sd-id 0xCA --application .\\nrf52840_xxaa.hex --softdevice .\\s140_nrf52_7.0.1_softdevice.hex beacon.zip");
        cmd.add("nrfutil dfu usb-serial -pkg beacon.zip -p " + BeaconPort + " -b 115200");
        RunCommand(cmd);
    }

}
