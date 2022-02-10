package protmetrics.dao.files.xyz;

import java.util.List;

import org.jgrapht.graph.Pseudograph;
import protmetrics.dao.intervals.IntervalData;
import protmetrics.dao.json.BondAtomDescJSON;
import protmetrics.dao.json.BondDescFileJSON;

/**
 * Wrapper for a bond description file. It specifies the ranges of each bond type.
 */
public class BondDescFile {

    private Pseudograph<AtomType, DefaultEdgeJ> desc;

    /**
     * @param path the path to the bond description file.
     * @throws Exception for problems while loading the file.
     */
    public BondDescFile(String path) throws Exception {

        this.desc = new Pseudograph<>(DefaultEdgeJ.class);
        BondDescFileJSON bdfj = BondDescFileJSON.buildFromFile(path);
        /* load atom bonds descriptions */
        for (BondAtomDescJSON badj : bdfj.getBondAtomDesc()) {
            BondAtomDesc bad = BondAtomDesc.fromBondAtomDescJSON(badj);

            /* add vertices */
            this.desc.addVertex(bad.getAtomA());
            this.desc.addVertex(bad.getAtomB());

            DefaultEdgeJ cce = new DefaultEdgeJ();
            cce.setValue(bad.getDescriptions());

            this.desc.addEdge(bad.getAtomA(), bad.getAtomB(), cce);
        }
    }

    /**
     * @param atomA atom A of the bond.
     * @param atomB atom B oft he bond.
     * @param dist distance of the bond.
     * @return list with each of the intervals.
     */
    public List<IntervalData<Double>> getDescItems(AtomType atomA, AtomType atomB, double dist) {
        List<IntervalData<Double>> result;

        DefaultEdgeJ dej = desc.getEdge(atomA, atomB);

        if (dej != null) {
            result = dej.getValue().getIntervals(dist);
        } else {
            result = null;
        }
        return result;
    }

    /**
     * @return the description of the bond as a graph.
     */
    public Pseudograph<AtomType, DefaultEdgeJ> getDesc() {
        return desc;
    }

    /**
     * @param desc the description of the bond as a graph.
     */
    public void setDesc(Pseudograph<AtomType, DefaultEdgeJ> desc) {
        this.desc = desc;
    }
}
