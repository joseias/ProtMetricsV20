package protmetrics.dao.dm;

import java.util.Objects;

/**
 * Represents an attribute within the result matrix (DataSet) when computing an
 * index.
 */
public class DMAtt {

    private final String name;
    private final String type;
    private final int order;

    private static final String SPECIAL_ATT_NAME = "StructID";

    /**
     * @param name attribute name.
     * @param type class of the attribute.
     * @param order the order of the attribute.
     */
    public DMAtt(String name, String type, int order) {
        this.name = name;
        this.type = type;
        this.order = order;
    }

    /**
     * @return the name of the attribute.
     */
    public String getName() {
        return name;
    }

    /**
     * @return the type of the attribute.
     */
    public String getType() {
        return type;
    }

    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DMAtt other = (DMAtt) obj;
        return Objects.equals(this.getName(), other.getName());
    }

    /**
     * @return the order of the attribute.
     */
    public int getOrder() {
        return order;
    }

    /**
     * @return the SPECIAL_ATT_NAME.
     */
    public static String getSName() {
        return SPECIAL_ATT_NAME;
    }
}
