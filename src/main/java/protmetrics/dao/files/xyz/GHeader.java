package protmetrics.dao.files.xyz;

import protmetrics.metrics.Wiener3D.MolType;

/**
 * Represents the header of a .xyz file.
 */
public class GHeader {

    private int size;
    private int edges;
    private MolType type;

    /**
     * @param size the size of the molecule.
     * @param edges number of edges.
     * @param type the type of the .xyz file.
     */
    public GHeader(int size, int edges, MolType type) {
        this.size = size;
        this.edges = edges;
        this.type = type;
    }

    /**
     * @return the size.
     */
    public int getSize() {
        return size;
    }

    /**
     * @param size the size to set.
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * @return the edges.
     */
    public int getEdges() {
        return edges;
    }

    /**
     * @param edges the edges to set.
     */
    public void setEdges(int edges) {
        this.edges = edges;
    }

    /**
     * @return the type.
     */
    public MolType getType() {
        return type;
    }

    /**
     * @param type the type to set.
     */
    public void setType(MolType type) {
        this.type = type;
    }
}
