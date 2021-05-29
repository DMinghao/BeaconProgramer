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

    /**
     *
     * @param ID
     * @param Name
     * @param ExID
     */
    public Bluetooth(int ID, String Name, int ExID) {
        this.ID = ID;
        this.Name = Name;
        this.ExID = ExID;
    }

    /**
     * Default constructor
     */
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
