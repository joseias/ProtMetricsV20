package protmetrics.dao.dm;

import java.util.HashMap;
import java.util.Set;

/**
 * Represents an instance in matrix data (column or row)
 *
 */
public class DMInstance {

    HashMap<DMAtt, DMAttValue> attValues;
    String instID;

    public DMInstance(String id) {
        this.instID = id;
        attValues = new HashMap<>();
    }

    public void setAttValue(DMAtt att, DMAttValue value) {
        attValues.put(att, value);
    }

    public DMAttValue getAttValue(DMAtt att) {
        return attValues.get(att);
    }

    public Set<DMAtt> getAtts() {
        return attValues.keySet();
    }

    public String getInstID() {
        return this.instID;
    }
}
