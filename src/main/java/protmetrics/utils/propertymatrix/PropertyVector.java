package protmetrics.utils.propertymatrix;

import java.util.*;

/**
 * Represents values for a give property for each amino acid.
 */
public class PropertyVector {

    /**
     */
    public String PropertyName;

    /**
     */
    public ArrayList VectorElements;

    /**
     * @param propertyName name of the property.
     */
    public PropertyVector(String propertyName) {
        this.PropertyName = propertyName;
        VectorElements = new ArrayList();
    }

    /**
     * @param name name of the property to get its value.
     * @param found used as output value to check if was found.
     * @return the value of the given name.
     */
    public double getValueFromName(String name, boolean[] found) {
        double m_result = 0;
        boolean[] m_ofound = {false};

        PropertyVectorElement m_pve;
        for (int m = 0; m < this.VectorElements.size(); m++) {
            m_pve = (PropertyVectorElement) this.VectorElements.get(m);
            m_result = m_pve.getValueFromName(name, m_ofound);
            if (m_ofound[0] == true) {
                found[0] = true;
                return m_result;
            } else {

            }
        }
        found[0] = false;
        return m_result;
    }

    /**
     * @param element vector element to be added.
     */
    public void addVectorElement(PropertyVectorElement element) {
        this.VectorElements.add(element);
    }
}
