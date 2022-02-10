package protmetrics.dao.json;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Represents the description of the bond between atoms in json format.
 */
public class BondAtomDescJSON {

    @SerializedName("AtomA")
    @Expose
    private String atomA;

    @SerializedName("AtomB")
    @Expose
    private String atomB;

    @SerializedName("bondDesc")
    @Expose
    private List<BondDescJSON> bondDesc = new ArrayList<BondDescJSON>();

    /**
     * @return the atomA.
     */
    public String getAtomA() {
        return atomA;
    }

    /**
     * @param atomA the AtomA.
     */
    public void setAtomA(String atomA) {
        this.atomA = atomA;
    }

    /**
     * @return the atomB.
     */
    public String getAtomB() {
        return atomB;
    }

    /**
     * @param atomB the AtomB.
     */
    public void setAtomB(String atomB) {
        this.atomB = atomB;
    }

    /**
     * @return the bondDesc.
     */
    public List<BondDescJSON> getBondDesc() {
        return bondDesc;
    }

    /**
     * @param bondDesc the bondDesc.
     */
    public void setBondDesc(List<BondDescJSON> bondDesc) {
        this.bondDesc = bondDesc;
    }
}
