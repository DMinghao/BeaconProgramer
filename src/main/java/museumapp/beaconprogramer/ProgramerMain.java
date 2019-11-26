/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumapp.beaconprogramer;

import Entity.Bluetooth;

/**
 *
 * @author mdu18
 */
public class ProgramerMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        // TODO code application logic here
        
        new Database(); 
        
        Bluetooth beacon = Database.NewBeacon();
        
        Command.CompileBeaconC(beacon);
        
        String BeaconPort = Command.getBeaconPort();
        
        Command.ProgramBeacon(BeaconPort);
        
        Database.InsertBluetooth(beacon);
    }
}
