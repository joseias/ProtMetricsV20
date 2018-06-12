package protmetrics.dao.files.XYZ;

import protmetrics.dao.json.BondAtomDescJSON;
import protmetrics.dao.json.BondDescJSON;
import protmetrics.dao.intervals.IntervalData;
import protmetrics.dao.intervals.IntervalTree;

public class BondAtomDesc {

    AtomType atomA;
    AtomType atomB;
    IntervalTree<Double> bondInfo;

    public AtomType getAtomA() {
        return atomA;
    }

    public void setAtomA(AtomType atomA) {
        this.atomA = atomA;
    }

    public AtomType getAtomB() {
        return atomB;
    }

    public void setAtomB(AtomType atomB) {
        this.atomB = atomB;
    }

    public IntervalTree<Double> getDescriptions() {
        return bondInfo;
    }

    public void setDescriptions(IntervalTree<Double> descriptions) {
        this.bondInfo = descriptions;
    }

    public static BondAtomDesc fromBondAtomDescJSON(BondAtomDescJSON badj) {
        AtomType atomA = AtomType.getTypeFromCode(badj.getAtomA());
        AtomType atomB = AtomType.getTypeFromCode(badj.getAtomB());

        IntervalTree<Double> bondInfo = new IntervalTree<Double>();

        for (BondDescJSON bdj : badj.getBondDesc()) {
            IntervalData<Double> idd = IntervalData.fromBondDescJSON(bdj);
            bondInfo.addInterval(idd);
        }
        BondAtomDesc result = new BondAtomDesc();
        result.setAtomA(atomA);
        result.setAtomB(atomB);
        result.setDescriptions(bondInfo);

        return result;
    }
}
