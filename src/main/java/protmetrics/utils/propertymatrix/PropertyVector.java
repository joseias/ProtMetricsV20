package protmetrics.utils.propertymatrix;

import java.util.*;

/**
 * Represents values for a given property for each amino acid.
 */
public class PropertyVector {

    /**
     * @return the propertyName
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * @return the vectorElements
     */
    public List<PropertyVectorElement> getVectorElements() {
        return vectorElements;
    }

    /**
     */
    private final String propertyName;

    /**
     */
    private final ArrayList<PropertyVectorElement> vectorElements;

    /**
     * @param propertyName name of the property.
     */
    public PropertyVector(String propertyName) {
        this.propertyName = propertyName;
        this.vectorElements = new ArrayList<>();
    }

    /**
     * @param name the name of the property to get its value.
     * @param found used as output value to check if was found.
     * @return the value of the given name.
     */
    public double getValueFromName(String name, boolean[] found) {
        double result = 0;
        boolean[] ofound = {false};

        PropertyVectorElement pve;
        for (int m = 0; m < this.getVectorElements().size(); m++) {
            pve = (PropertyVectorElement) this.getVectorElements().get(m);
            result = pve.getValueFromName(name, ofound);
            if (ofound[0]) {
                found[0] = true;
                return result;
            }
        }
        found[0] = false;
        return result;
    }

    /**
     * @param element vector element to be added.
     */
    public void addVectorElement(PropertyVectorElement element) {
        this.getVectorElements().add(element);
    }
}
