package protmetrics.dao.files.xyz;

import org.jgrapht.graph.DefaultEdge;
import protmetrics.dao.intervals.Type;

/**
 * An edge within the bond description graph.
 */
public class DefaultEdgeJ extends DefaultEdge {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    Type<Double> value;

    /**
     * @return Type representing the range of the bond type values.
     */
    public Type<Double> getValue() {
        return value;
    }

    /**
     * @param value Type representing the range of the bond type values.
     */
    public void setValue(Type<Double> value) {
        this.value = value;
    }
}
