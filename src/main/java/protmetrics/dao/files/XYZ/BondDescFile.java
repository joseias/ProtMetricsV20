package protmetrics.dao.files.XYZ;

import java.util.List;

import org.jgrapht.graph.Pseudograph;
import protmetrics.dao.intervals.IntervalData;
import protmetrics.dao.json.BondAtomDescJSON;
import protmetrics.dao.json.BondDescFileJSON;

public class BondDescFile {

    private Pseudograph<AtomType, DefaultEdgeJ> desc;

    public BondDescFile(String bdFilePath) throws Exception {

        this.desc = new Pseudograph<>(DefaultEdgeJ.class);
        BondDescFileJSON bdfj = BondDescFileJSON.buildFromFile(bdFilePath);
        /*Load atom bonds descriptions*/
        for (BondAtomDescJSON badj : bdfj.getBondAtomDesc()) {
            BondAtomDesc bad = BondAtomDesc.fromBondAtomDescJSON(badj);

            /*Add vertices*/
            this.desc.addVertex(bad.getAtomA());
            this.desc.addVertex(bad.getAtomB());

            DefaultEdgeJ cce = new DefaultEdgeJ();
            cce.setValue(bad.getDescriptions());

            this.desc.addEdge(bad.getAtomA(), bad.getAtomB(), cce);
        }
    }

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

    public Pseudograph<AtomType, DefaultEdgeJ> getDesc() {
        return desc;
    }

    public void setDesc(Pseudograph<AtomType, DefaultEdgeJ> desc) {
        this.desc = desc;
    }
}
