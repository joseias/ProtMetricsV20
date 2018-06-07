package protmetrics.dao;

import java.util.*;
/// <summary>
/// Summary description for PropertyVector.
/// </summary>

public class PropertyVector {

    public String PropertyName;
    public ArrayList VectorElements;

    public PropertyVector(String a_propertyName, String[] a_propertyColumn) {
        this.PropertyName = a_propertyName;
        this.VectorElements = new ArrayList();
        String[] m_vectorValueEntry;
        char[] m_separator = {' '};
        for (int index = 0; index < a_propertyColumn.length; index++) {
            m_vectorValueEntry = a_propertyColumn[index].split((new String(m_separator)));
            this.VectorElements.add(new PropertyVectorElement(Integer.parseInt(m_vectorValueEntry[0]), m_vectorValueEntry[1], m_vectorValueEntry[2], Double.parseDouble(m_vectorValueEntry[3])));
        }
    }//public PropertyVector(String a_propertyName,String[] a_propertyColumn)

    public PropertyVector(String a_propertyName) {
        this.PropertyName = a_propertyName;
        VectorElements = new ArrayList();
    }

    public double getValueFromNumber(int a_number, boolean[] o_found) {
        double m_result = 0;
        boolean[] m_ofound = {false};
        PropertyVectorElement m_pve;
        for (int m = 0; m < this.VectorElements.size(); m++) {
            m_pve = (PropertyVectorElement) this.VectorElements.get(m);
            m_result = m_pve.GetValueFromNumber(a_number, m_ofound);
            if (m_ofound[0] == true) {
                o_found[0] = true;
                return m_result;
            }
        }
        o_found[0] = false;
        return m_result;
    }//public double getValueFromNumber(int a_number)

    public double getValueFromName(String a_name, boolean[] o_found) {
        double m_result = 0;
        boolean[] m_ofound = {false};

        PropertyVectorElement m_pve;
        for (int m = 0; m < this.VectorElements.size(); m++) {
            m_pve = (PropertyVectorElement) this.VectorElements.get(m);
            m_result = m_pve.GetValueFromName(a_name, m_ofound);
            if (m_ofound[0] == true) {
                o_found[0] = true;
                return m_result;
            } else {

            }
        }
        o_found[0] = false;
        return m_result;
    }//public double getValueFromName()

    public void addVectorElement(PropertyVectorElement a_vectorElement) {
        this.VectorElements.add(a_vectorElement);
    }//public void addVectorElement(PropertyVectorElement a_vectorElement)

    public boolean vEquals(PropertyVector a_otherVector) {

        if ((!a_otherVector.PropertyName.equals(this.PropertyName)) || (a_otherVector.VectorElements.size() != this.VectorElements.size())) {
            return false;
        } else {
            PropertyVectorElement m_pve;
            for (int m = 0; m < this.VectorElements.size(); m++) {
                m_pve = (PropertyVectorElement) this.VectorElements.get(m);
                if (!a_otherVector.containsElement(m_pve)) {
                    return false;
                }
            }
            return true;
        }

    }//public boolean vEquals(PropertyVector a_otherVector)

    public boolean containsElement(PropertyVectorElement a_otherVectorElement) {

        PropertyVectorElement m_pve;
        for (int m = 0; m < this.VectorElements.size(); m++) {
            m_pve = (PropertyVectorElement) this.VectorElements.get(m);
            if ((m_pve.ElementFullName.equals(a_otherVectorElement.ElementFullName)) || (m_pve.ElementSimpleName.equals(a_otherVectorElement.ElementSimpleName))) {
                return true;
            }
        }
        return false;

    }

}//Class
