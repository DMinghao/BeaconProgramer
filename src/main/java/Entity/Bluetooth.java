/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;


/**
 * @apiNote An entity class that mirrors the Bluetooth table property in the remote database
 * It contents {@link Integer ID}, {@link String Name}, {@link Integer ExhibitID} (ExID)
 * @author mdu18
 */
public class Bluetooth {
    
    private int ID;
    private String Name;
    private int ExID;

    public Bluetooth(int ID, String Name, int ExID) {
        this.ID = ID;
        this.Name = Name;
        this.ExID = ExID;
    }

    public Bluetooth() {
    }

    /**
     * Get the value of ExID
     *
     * @return the value of ExID
     */
    public int getExID() {
        return ExID;
    }

    /**
     * Set the value of ExID
     *
     * @param ExID new value of ExID
     */
    public void setExID(int ExID) {
        this.ExID = ExID;
    }


    /**
     * Get the value of Name
     *
     * @return the value of Name
     */
    public String getName() {
        return Name;
    }

    /**
     * Set the value of Name
     *
     * @param Name new value of Name
     */
    public void setName(String Name) {
        this.Name = Name;
    }


    /**
     * Get the value of ID
     *
     * @return the value of ID
     */
    public int getID() {
        return ID;
    }

    /**
     * Set the value of ID
     *
     * @param ID new value of ID
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    @Override
    public String toString() {
        return "Bluetooth{" + "ID = " + ID + ", Name = " + Name + ", ExID = " + ExID + '}';
    }
    
    
}
