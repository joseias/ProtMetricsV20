package protmetrics.dao.files.xyz;

import org.jgrapht.graph.DefaultEdge;
import protmetrics.dao.intervals.IntervalTree;

/**
 * An edge within the bond description graph.
 */
public class DefaultEdgeJ extends DefaultEdge {

    private static final long serialVersionUID = 1L;

    IntervalTree<Double> value;

    /**
     * @return IntervalTree representing the range of the bond type values.
     */
    public IntervalTree<Double> getValue() {
        return value;
    }

    /**
     * @param value IntervalTree representing the range of the bond type values.
     */
    public void setValue(IntervalTree<Double> value) {
        this.value = value;
    }
}
