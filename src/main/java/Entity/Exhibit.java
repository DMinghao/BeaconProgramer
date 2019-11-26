/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

/**
 *
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

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getDes() {
        return Des;
    }

    public void setDes(String Des) {
        this.Des = Des;
    }

    public int getScID() {
        return ScID;
    }

    public void setScID(int ScID) {
        this.ScID = ScID;
    }

    @Override
    public String toString() {
        return "Exhibit " + "ID = " + ID + "\nDescription = " + Des + "\nSection ID = " + ScID;
    }

}
