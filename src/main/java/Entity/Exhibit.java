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
 * @apiNote An entity class that mirrors the Exhibit table property in the remote database.
 * It contents {@link Integer ID}, {@link String Description} (Des), {@link Integer SectionID} (ScID)
 * @author mdu18
 */
public class Exhibit {
    
    private int ID;
    private String Des;
    private int ScID;

    /**
     *
     * @param ID
     * @param Des
     * @param ScID
     */
    public Exhibit(int ID, String Des, int ScID) {
        this.ID = ID;
        this.Des = Des;
        this.ScID = ScID;
    }

    /**
     * Default constructor
     */
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
