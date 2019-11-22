/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumapp.beaconprogramer;

import java.util.logging.Logger;

/**
 *
 * @author mdu18
 */
public class Bluetooth {
    private int ID; 
    private String Name; 
    private int ExID; 

    public Bluetooth() {
    }

    public Bluetooth(int ID, String Name, int ExID) {
        this.ID = ID;
        this.Name = Name;
        this.ExID = ExID;
    }

    public int getExID() {
        return ExID;
    }

    public void setExID(int ExID) {
        this.ExID = ExID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    @Override
    public String toString() {
        return "Bluetooth{" + "ID = " + ID + ", Name = " + Name + ", ExID = " + ExID + '}';
    }
    
    
}
