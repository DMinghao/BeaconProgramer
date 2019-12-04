/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

/**
 * @apiNote An entity class that mirrors the Exhibit table property in the remote database.
 * It contents {@link Integer ID}, {@link String Description} (Des), {@link Integer SectionID} (ScID)
 * @author mdu18
 */
public class Exhibit {
    
    private int ID;
    private String Des;
    private int ScID;

    public Exhibit(int ID, String Des, int ScID) {
        this.ID = ID;
        this.Des = Des;
        this.ScID = ScID;
    }

    public Exhibit() {
    }

    /**
     * Get the value of SectionID (ScID)
     *
     * @return the value of SectionID (ScID)
     */
    public int getScID() {
        return ScID;
    }

    /**
     * Set the value of SectionID (ScID)
     *
     * @param ScID new value of SectionID (ScID)
     */
    public void setScID(int ScID) {
        this.ScID = ScID;
    }


    /**
     * Get the value of Description (Des)
     *
     * @return the value of Description (Des)
     */
    public String getDes() {
        return Des;
    }

    /**
     * Set the value of Description (Des)
     *
     * @param Des new value of Description (Des)
     */
    public void setDes(String Des) {
        this.Des = Des;
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
        return "Exhibit " + "ID = " + ID + "\nDescription = " + Des + "\nSection ID = " + ScID;
    }

}
