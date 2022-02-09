package protmetrics.dao.dm;

import java.util.Objects;

public class DMAtt {

    private final String attName;
    private final Class attType;
    private final int attOrder;

    private static String SPECIAL_ATT_NAME = "StructID";

    public DMAtt(String attName, Class attType, int order) {
        this.attName = attName;
        this.attType = attType;
        this.attOrder = order;
    }

    /**
     * @return the attName
     */
    public String getAttName() {
        return attName;
    }

    /**
     * @return the attType
     */
    public Class getAttType() {
        return attType;
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
     * @return the attOrder
     */
    public int getAttOrder() {
        return attOrder;
    }

    /**
     * @return the SPECIAL_ATT_NAME
     */
    public static String getSPECIAL_ATT_NAME() {
        return SPECIAL_ATT_NAME;
    }

    /**
     * @param aSPECIAL_ATT_NAME the SPECIAL_ATT_NAME to set
     */
    public static void setSPECIAL_ATT_NAME(String aSPECIAL_ATT_NAME) {
        SPECIAL_ATT_NAME = aSPECIAL_ATT_NAME;
    }
}
