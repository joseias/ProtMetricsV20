package protmetrics.dao.dm;

import java.util.HashMap;
import java.util.Set;

/**
 * Represents an instance in matrix data (column or row).
 */
public class DMInstance {

    HashMap<DMAtt, DMAttValue> attValues;
    String instID;

    /**
     *
     * @param id id of the instance.
     */
    public DMInstance(String id) {
        this.instID = id;
        attValues = new HashMap<>();
    }

    /**
     *
     * @param att attribute of the instance.
     * @param value value of the instance.
     */
    public void setAttValue(DMAtt att, DMAttValue value) {
        attValues.put(att, value);
    }

    /**
     *
     * @param att attribute to get its value.
     * @return the value of the attribute.
     */
    public DMAttValue getAttValue(DMAtt att) {
        return attValues.get(att);
    }

    /**
     *
     * @return attributes within this instance.
     */
    public Set<DMAtt> getAtts() {
        return attValues.keySet();
    }

    /**
     *
     * @return id of the instance.
     */
    public String getInstID() {
        return this.instID;
    }
}
