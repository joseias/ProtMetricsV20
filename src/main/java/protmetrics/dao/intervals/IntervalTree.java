/* based in https://github.com/kevinjdolan/intervaltree (WTFPL) */
package protmetrics.dao.intervals;

import java.util.ArrayList;
import java.util.List;

/**
 * An Interval Tree is essentially a map from intervals to objects, which can be
 * queried for all data associated with a particular interval of time
*/
public class IntervalTree<Type> {

    private IntervalNode<Type> head;
    private List<IntervalData<Type>> intervalList;
    private boolean inSync;
    private int size;

    /**
     * Instantiate a new interval tree with no intervals
     */
    public IntervalTree() {
        this.head = new IntervalNode<>();
        this.intervalList = new ArrayList<>();
        this.inSync = true;
        this.size = 0;
    }

    /**
     * Instantiate and build an interval tree with a preset list of intervals
     *
     * @param intervalList the list of intervals to use
     */
    public IntervalTree(List<IntervalData<Type>> intervalList) {
        this.head = new IntervalNode<>(intervalList);
        this.intervalList = new ArrayList<>();
        this.intervalList.addAll(intervalList);
        this.inSync = true;
        this.size = intervalList.size();
    }

    /**
     * Perform a stabbing query, returning the interval objects Will rebuild the
     * tree if out of sync
     *
     * @param time the time to stab
     * @return	all intervals that contain time
     */
    public List<IntervalData<Type>> getIntervals(double time) {
        build();
        return head.stab(time);
    }

    /**
     * Add an interval object to the interval tree's list Will not rebuild the
     * tree until the next query or call to build
     *
     * @param interval the interval object to add
     */
    public void addInterval(IntervalData<Type> interval) {
        intervalList.add(interval);
        inSync = false;
    }

    /**
     * Build the interval tree to reflect the list of intervals, Will not run if
     * this is currently in sync
     */
    public void build() {
        if (!inSync) {
            head = new IntervalNode<Type>(intervalList);
            inSync = true;
            size = intervalList.size();
        }
    }

    @Override
    public String toString() {
        return nodeString(head, 0);
    }

    private String nodeString(IntervalNode<Type> node, int level) {
        if (node == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append("\t");
        }
        sb.append(node).append("\n");
        sb.append(nodeString(node.getLeft(), level + 1));
        sb.append(nodeString(node.getRight(), level + 1));
        return sb.toString();
    }
}
