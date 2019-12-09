package protmetrics.dao.files.mol;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedPseudograph;
import protmetrics.dao.files.xyz.GAtom;
import protmetrics.dao.files.xyz.GHeader;

/**
 *
 * @author sijfg
 */
public class MolFile {

    private String ID;
    private GHeader header;
    private WeightedPseudograph<GAtom, DefaultWeightedEdge> bonds;

    public MolFile(String ID, GHeader header, WeightedPseudograph<GAtom, DefaultWeightedEdge> bonds) {
        this.header = header;
        this.bonds = bonds;
        this.ID = "ID";
    }

    /**
     * @return the header
     */
    public GHeader getHeader() {
        return header;
    }

    /**
     * @param header the header to set
     */
    public void setHeader(GHeader header) {
        this.header = header;
    }

    public WeightedPseudograph<GAtom, DefaultWeightedEdge> getBonds() {
        return bonds;
    }

    public void setBonds(WeightedPseudograph<GAtom, DefaultWeightedEdge> bonds) {
        this.bonds = bonds;
    }

    /**
     * @return the ID
     */
    public String getID() {
        return ID;
    }

    /**
     * @param ID the ID to set
     */
    public void setID(String ID) {
        this.ID = ID;
    }
}
