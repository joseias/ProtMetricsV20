package protmetrics.dao.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Description of an atom bond in json format.
 */
public class BondDescJSON {

    @SerializedName("spec")
    @Expose
    private String spec;

    @SerializedName("w")
    @Expose
    private double w;

    /**
     * @return the spec of the bond.
     */
    public String getSpec() {
        return spec;
    }

    /**
     * @param spec the spec of the bond to set.
     */
    public void setSpec(String spec) {
        this.spec = spec;
    }

    /**
     * @return the weight of the bond.
     */
    public double getW() {
        return w;
    }

    /**
     * @param w the w.
     */
    public void setW(double w) {
        this.w = w;
    }
}
