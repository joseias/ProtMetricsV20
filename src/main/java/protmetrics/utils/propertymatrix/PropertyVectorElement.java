package protmetrics.utils.propertymatrix;

/**
 * And element within a PropertyVector.
 */
public class PropertyVectorElement {

    /**
     */
    private final int number;

    /**
     */
    private final String simpleName;

    /**
     */
    private final String fullName;

    /**
     */
    private final double value;

    /**
     * @param number number within the vector.
     * @param simpleName property simple name.
     * @param fullName property full name.
     * @param value property value.
     */
    public PropertyVectorElement(int number, String simpleName, String fullName, double value) {
        this.number = number;
        this.simpleName = simpleName;
        this.fullName = fullName;
        this.value = value;
    }

    /**
     * @param name property name (full or simple)
     * @param found used as output value to check if was found.
     * @return the value of the property.
     */
    public double getValueFromName(String name, boolean[] found) {
        if ((this.getElementSimpleName().equals(name)) || (this.getElementFullName().equals(name))) {
            found[0] = true;
            return this.getElementValue();
        }
        found[0] = false;
        return 0;
    }

    /**
     * @return the number
     */
    public int getElementNumber() {
        return number;
    }

    /**
     * @return the simpleName
     */
    public String getElementSimpleName() {
        return simpleName;
    }

    /**
     * @return the fullName
     */
    public String getElementFullName() {
        return fullName;
    }

    /**
     * @return the value
     */
    public double getElementValue() {
        return value;
    }
}
