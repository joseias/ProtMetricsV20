package protmetrics.utils.propertymatrix;

import java.util.*;

public class PropertyVector {

    public String PropertyName;
    public ArrayList VectorElements;

    public PropertyVector(String a_propertyName) {
        this.PropertyName = a_propertyName;
        VectorElements = new ArrayList();
    }

    public double getValueFromName(String a_name, boolean[] o_found) {
        double m_result = 0;
        boolean[] m_ofound = {false};

        PropertyVectorElement m_pve;
        for (int m = 0; m < this.VectorElements.size(); m++) {
            m_pve = (PropertyVectorElement) this.VectorElements.get(m);
            m_result = m_pve.getValueFromName(a_name, m_ofound);
            if (m_ofound[0] == true) {
                o_found[0] = true;
                return m_result;
            } else {

            }
        }
        o_found[0] = false;
        return m_result;
    }

    public void addVectorElement(PropertyVectorElement a_vectorElement) {
        this.VectorElements.add(a_vectorElement);
    }
}
