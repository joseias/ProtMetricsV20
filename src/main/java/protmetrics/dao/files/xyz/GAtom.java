/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package protmetrics.dao.files.XYZ;

import javafx.geometry.Point3D;

/**
 *
 * @author sijfg
 */
public class GAtom {

    private Point3D location;
    private int ID;
    private AtomType type;

    public GAtom(Point3D location, AtomType type, int id) {
        this.location = location;
        this.type = type;
        this.ID = id;
    }

    public AtomType getType() {
        return type;
    }

    public void setType(AtomType type) {
        this.type = type;
    }

    /**
     * @return the ID
     */
    public int getID() {
        return ID;
    }

    /**
     * @param ID the ID to set
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    @Override
    public int hashCode() {
        return this.getID();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GAtom other = (GAtom) obj;

        if (this.getID() != other.getID()) {
            return false;
        }

        return this.location.equals(other.getLocation());
    }

    /**
     * @return the location
     */
    public Point3D getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(Point3D location) {
        this.location = location;
    }
}
