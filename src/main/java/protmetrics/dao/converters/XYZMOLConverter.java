package protmetrics.dao.converters;

import java.util.List;
import java.util.Properties;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedPseudograph;
import protmetrics.dao.files.mol.MolFile;
import protmetrics.dao.files.xyz.BondDescFile;
import protmetrics.dao.files.xyz.GAtom;
import protmetrics.dao.files.xyz.GHeader;
import protmetrics.dao.files.xyz.XYZFile;
import protmetrics.dao.intervals.IntervalData;
import protmetrics.metrics.Wiener3D;
import protmetrics.metrics.Wiener3D.MolType;
import protmetrics.utils.BioUtils;
import protmetrics.utils.MyMath;

/**
 * Implements conversion utilities from XYZ files.
 */
public class XYZMOLConverter {

    /**
     *
     * @param input .xyz file wrapper.
     * @param prop properties object.
     * @return a .mol file object from a .xyz file object.
     * @throws Exception
     */
    public MolFile convert(XYZFile input, Properties prop) throws Exception {

        boolean loadBondType = Boolean.parseBoolean(prop.getProperty(Wiener3D.Constants.LOAD_BOND_TYPE));

        WeightedPseudograph<GAtom, DefaultWeightedEdge> bonds = new WeightedPseudograph<>(DefaultWeightedEdge.class);

        String bdfPath = prop.getProperty(Wiener3D.Constants.BOND_DESC_FILE);
        BondDescFile bdf = new BondDescFile(bdfPath);

        List<IntervalData<Double>> items;
        DefaultWeightedEdge wedgeTmp;
        GAtom atomi, atomj;

        int precision = (Integer) prop.get(Wiener3D.Constants.PRECISION);

        for (int i = 0; i < input.getAtoms().size() - 1; ++i) {
            atomi = input.getAtoms().get(i);
            for (int j = i + 1; j < input.getAtoms().size(); ++j) {
                atomj = input.getAtoms().get(j);

                double distance = BioUtils.getDistance(atomi, atomj);
                distance = MyMath.round(distance, precision);

                items = bdf.getDescItems(atomi.getType(), atomj.getType(), distance);

                if (items != null) {
                    if (items.size() > 1) {
                        String error = "Some of the intervals specified for " + atomi.getType() + "-" + atomi.getType() + " overlaps...";
                        Exception e = new Exception(error);
                        throw e;
                    } else {
                        if (items.size() == 1) {
                            bonds.addVertex(atomi);
                            bonds.addVertex(atomj);
                            wedgeTmp = new DefaultWeightedEdge();

                            if (loadBondType == true) {
                                bonds.setEdgeWeight(wedgeTmp, items.get(0).getData());
                            } else {
                                bonds.setEdgeWeight(wedgeTmp, Wiener3D.Constants.DEFAULT_EDGE_WEIGHT);
                            }

                            bonds.addEdge(atomi, atomj, wedgeTmp);
                        }
                    }
                } else {
                    String error = "Intervals for atoms of type " + atomi.getType() + "-" + atomi.getType() + " seems to be not specified...";
                    Exception e = new Exception(error);
                    throw e;
                }
            }
        }
        GHeader header = new GHeader(input.getSize(), bonds.edgeSet().size(), MolType.V3000);
        return new MolFile(input.getID(), header, bonds);
    }
}
