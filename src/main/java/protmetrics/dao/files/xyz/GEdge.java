package protmetrics.dao.files.xyz;

/**
 * Represents an edge within a molecule in .xyz files.
 */
public class GEdge {

    private GAtom target;
    private GAtom source;
    private double weight;

    /**
     * @return the target.
     */
    public GAtom getTarget() {
        return target;
    }

    /**
     * @param target the target to set.
     */
    public void setTarget(GAtom target) {
        this.target = target;
    }

    /**
     * @return the source.
     */
    public GAtom getSource() {
        return source;
    }

    /**
     * @param source the source to set.
     */
    public void setSource(GAtom source) {
        this.source = source;
    }

    /**
     * @return the weight.
     */
    public double getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set.
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }
}
