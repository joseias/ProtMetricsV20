package protmetrics.dao.dm;

import java.util.Objects;

/**
 * Represents an attribute within the result matrix (DataSet) when computing an index.
 */
public class DMAtt {

    private final String name;
    private final Class type;
    private final int order;

    private static String SPECIAL_ATT_NAME = "StructID";

    /**
     * @param name attribute name.
     * @param type class of the attribute.
     * @param order the order of the attribute.
     */
    public DMAtt(String name, Class type, int order) {
        this.name = name;
        this.type = type;
        this.order = order;
    }

    /**
     * @return the name of the attribute.
     */
    public String getAttName() {
        return name;
    }

    /**
     * @return the type of the attribute.
     */
    public Class getAttType() {
        return type;
    }

    @Override
    public int hashCode() {
        return this.getAttName().hashCode();
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
        return Objects.equals(this.getAttName(), other.getAttName());
    }

    /**
     * @return the order of the attribute.
     */
    public int getAttOrder() {
        return order;
    }

    /**
     * @return the SPECIAL_ATT_NAME.
     */
    public static String getSPECIAL_ATT_NAME() {
        return SPECIAL_ATT_NAME;
    }

    /**
     * @param specialAttName the SPECIAL_ATT_NAME to set
     */
    public static void setSPECIAL_ATT_NAME(String specialAttName) {
        SPECIAL_ATT_NAME = specialAttName;
    }
}
