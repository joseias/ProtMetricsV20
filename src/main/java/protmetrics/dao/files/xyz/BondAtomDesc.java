package protmetrics.dao.files.xyz;

import protmetrics.dao.json.BondAtomDescJSON;
import protmetrics.dao.json.BondDescJSON;
import protmetrics.dao.intervals.IntervalData;
import protmetrics.dao.intervals.Type;

/**
 * Represents the description of the bond between two atoms A and B (encoded as
 * AtomType)
 */
public class BondAtomDesc {

    AtomType atomA;
    AtomType atomB;
    Type<Double> bondInfo;

    /**
     * @return the atom A of the bound.
     */
    public AtomType getAtomA() {
        return atomA;
    }

    /**
     * @param atomA set the atom A of the bound.
     */
    public void setAtomA(AtomType atomA) {
        this.atomA = atomA;
    }

    /**
     * @return the atom B of the bound.
     */
    public AtomType getAtomB() {
        return atomB;
    }

    /**
     * @param atomB set the atom B of the bound.
     */
    public void setAtomB(AtomType atomB) {
        this.atomB = atomB;
    }

    /**
     * @return bond description.
     */
    public Type<Double> getDescriptions() {
        return bondInfo;
    }

    /**
     * @param descriptions bond description to set.
     */
    public void setDescriptions(Type<Double> descriptions) {
        this.bondInfo = descriptions;
    }

    /**
     * @param badj bond description JSON wrapper.
     * @return bond description.
     */
    public static BondAtomDesc fromBondAtomDescJSON(BondAtomDescJSON badj) {
        AtomType atomA = AtomType.getTypeFromCode(badj.getAtomA());
        AtomType atomB = AtomType.getTypeFromCode(badj.getAtomB());

        Type<Double> bondInfo = new Type<>();

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
