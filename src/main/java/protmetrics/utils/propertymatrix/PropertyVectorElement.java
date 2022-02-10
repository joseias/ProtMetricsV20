package protmetrics.utils.propertymatrix;

/**
 * And element within a PropertyVector.
 */
public class PropertyVectorElement {

    /**
     */
    public int ElementNumber;

    /**
     */
    public String ElementSimpleName;

    /**
     */
    public String ElementFullName;

    /**
     */
    public double ElementValue;

    /**
     * @param number number within the vector.
     * @param simpleName property simple name.
     * @param fullName property full name.
     * @param value property value.
     */
    public PropertyVectorElement(int number, String simpleName, String fullName, double value) {
        this.ElementNumber = number;
        this.ElementSimpleName = simpleName;
        this.ElementFullName = fullName;
        this.ElementValue = value;
    }

    /**
     * @param name property name (full or simple)
     * @param found used as output value to check if was found.
     * @return the value of the property.
     */
    public double getValueFromName(String name, boolean[] found) {
        if ((this.ElementSimpleName.equals(name)) || (this.ElementFullName.equals(name))) {
            found[0] = true;
            return this.ElementValue;
        }
        found[0] = false;
        return 0;
    }
}
