package protmetrics.dao.files.XYZ;

import org.jgrapht.graph.DefaultEdge;
import protmetrics.dao.intervals.IntervalTree;

public class DefaultEdgeJ extends DefaultEdge {

    private static final long serialVersionUID = 1L;

    IntervalTree<Double> value;

    public IntervalTree<Double> getValue() {
        return value;
    }

    public void setValue(IntervalTree<Double> value) {
        this.value = value;
    }
}
