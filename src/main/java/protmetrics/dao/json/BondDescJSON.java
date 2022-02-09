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
     *
     * @return The spec
     */
    public String getSpec() {
        return spec;
    }

    /**
     *
     * @param spec The spec
     */
    public void setSpec(String spec) {
        this.spec = spec;
    }

    /**
     *
     * @return The w
     */
    public double getW() {
        return w;
    }

    /**
     *
     * @param w The w
     */
    public void setW(double w) {
        this.w = w;
    }

}
