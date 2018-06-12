package protmetrics.dao.converters;

import java.util.List;
import java.util.Properties;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedPseudograph;

import protmetrics.dao.files.XYZ.BondDescFile;
import protmetrics.dao.files.XYZ.GAtom;
import protmetrics.dao.files.XYZ.GHeader;
import protmetrics.dao.files.XYZ.XYZFile;
import protmetrics.dao.files.mol.MolFile;
import protmetrics.dao.intervals.IntervalData;
import protmetrics.metrics.Wiener3D;
import protmetrics.metrics.Wiener3D.MolType;
import protmetrics.utils.BioUtils;
import protmetrics.utils.MyMath;

/**
 * *
 * In the original v1.0, used until 2016/06/26 intervals seems to be wrong,
 * asuming that doubleBond < aromaticBond < singleBond which is not true in
 * conf.properties usually used SINGLE_BOND=1.46 DOUBLE_BOND=1.31
 * AROMATIC_BOND=1.27 THRESHOLD=0.02 @author Docente
 *
 */
public class XYZMOLConverter {
    
    public MolFile convert(XYZFile input, Properties prop) throws Exception {
        Boolean loadBondType = Boolean.parseBoolean(prop.getProperty(Wiener3D.Constants.LOAD_BOND_TYPE, "true"));
        
        WeightedPseudograph<GAtom, DefaultWeightedEdge> bonds = new WeightedPseudograph<>(DefaultWeightedEdge.class);
        
        String bdfPath = prop.getProperty(Wiener3D.Constants.BOND_DESC_FILE);
        BondDescFile bdf = new BondDescFile(bdfPath);
        
        List<IntervalData<Double>> items;
        DefaultWeightedEdge wedgeTmp;
        GAtom atomi, atomj;
        
        int precision = (Integer) prop.get(Wiener3D.Constants.PRECISION);
        
        for (int i = 0; i < input.getAtoms().size() - 1; i++) {
            atomi = input.getAtoms().get(i);
            for (int j = i + 1; j < input.getAtoms().size(); j++) {
                atomj = input.getAtoms().get(j);
                
                double distance = BioUtils.getDistance(atomi, atomj);
                distance = MyMath.round(distance, precision);
                
                items = bdf.getDescItems(atomi.getType(), atomj.getType(), distance);
                
                if (items != null) {
                    if (items.size() > 1) {
                        String error = "Some of the intervals specified for " + atomi.getType() + "-" + atomi.getType() + " overlaps...";
                        Exception e = new Exception(error);
                        throw e;
                    }
                    else {
                        if (items.size() == 1) {
                            bonds.addVertex(atomi);
                            bonds.addVertex(atomj);
                            wedgeTmp = new DefaultWeightedEdge();
                            
                            if (loadBondType == true) {
                                bonds.setEdgeWeight(wedgeTmp, items.get(0).getData());
                            }
                            else {
                                bonds.setEdgeWeight(wedgeTmp, Wiener3D.Constants.DEFAULT_EDGE_WEIGHT);
                            }
                            
                            bonds.addEdge(atomi, atomj, wedgeTmp);
                        }
                    }
                }
                else {
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
