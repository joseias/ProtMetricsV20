package protmetrics.dao.files.xyz;

import javax.vecmath.Point3d;

/**
 * Represents an atom within a molecule in .xyz files.
 */
public class GAtom {

    private Point3d location;
    private int ID;
    private AtomType type;

    /**
     * @param location coordinates of the atom.
     * @param type the type of the atom.
     * @param id the id of the atom.
     */
    public GAtom(Point3d location, AtomType type, int id) {
        this.location = location;
        this.type = type;
        this.ID = id;
    }

    /**
     * @return the atom type.
     */
    public AtomType getType() {
        return type;
    }

    /**
     * @param type the type to set.
     */
    public void setType(AtomType type) {
        this.type = type;
    }

    /**
     * @return the ID.
     */
    public int getID() {
        return ID;
    }

    /**
     * @param ID the ID to set.
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
     * @return the location.
     */
    public Point3d getLocation() {
        return location;
    }

    /**
     * @param location the location to set.
     */
    public void setLocation(Point3d location) {
        this.location = location;
    }
}
