package protmetrics.dao.dm;

/**
 * Represents an attribute value within the result matrix (DataSet) when computing an index.
 */
public class DMAttValue {

    private String value;

    /**
     * @param value the value of the attribute.
     */
    public DMAttValue(String value) {
        this.value = value;
    }

    /**
     * @return the value.
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set.
     */
    public void setValue(String value) {
        this.value = value;
    }
}
