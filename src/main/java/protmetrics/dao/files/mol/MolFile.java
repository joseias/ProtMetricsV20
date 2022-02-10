package protmetrics.dao.files.mol;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedPseudograph;
import protmetrics.dao.files.xyz.GAtom;
import protmetrics.dao.files.xyz.GHeader;

/**
 * Wrapper to represent a .mol file.
 */
public class MolFile {

    private String ID;
    private GHeader header;
    private WeightedPseudograph<GAtom, DefaultWeightedEdge> bonds;

    /**
     * @param id the id of the .mol
     * @param header the header object.
     * @param bonds object representing the graph.
     */
    public MolFile(String id, GHeader header, WeightedPseudograph<GAtom, DefaultWeightedEdge> bonds) {
        this.header = header;
        this.bonds = bonds;
        this.ID = id;
    }

    /**
     * @return the header.
     */
    public GHeader getHeader() {
        return header;
    }

    /**
     * @param header the header to set.
     */
    public void setHeader(GHeader header) {
        this.header = header;
    }

    /**
     * @return bonds within the .mol file.
     */
    public WeightedPseudograph<GAtom, DefaultWeightedEdge> getBonds() {
        return bonds;
    }

    /**
     * @param bonds the bonds to set.
     */
    public void setBonds(WeightedPseudograph<GAtom, DefaultWeightedEdge> bonds) {
        this.bonds = bonds;
    }

    /**
     * @return the ID.
     */
    public String getID() {
        return ID;
    }

    /**
     * @param id the id to set.
     */
    public void setID(String id) {
        this.ID = id;
    }
}
