package protmetrics.dao.intervals;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

/**
 * The Node class contains the interval tree information for one single node.
 * based in https://github.com/kevinjdolan/intervaltree (WTFPL)
 *
 * @param <T> type of the data within the node.
 */
public class IntervalNode<T> {

    private SortedMap<IntervalData<T>, List<IntervalData<T>>> intervals;
    private double center;
    private IntervalNode<T> leftNode;
    private IntervalNode<T> rightNode;

    /**
     *
     */
    public IntervalNode() {
        intervals = new TreeMap<>();
        center = 0;
        leftNode = null;
        rightNode = null;
    }

    /**
     * @param intervalList list of interval data.
     */
    public IntervalNode(List<IntervalData<T>> intervalList) {

        intervals = new TreeMap<>();

        SortedSet<Double> endpoints = new TreeSet<>();

        for (IntervalData<T> interval : intervalList) {
            endpoints.add(interval.getStart());
            endpoints.add(interval.getEnd());
        }

        double median = getMedian(endpoints);
        center = median;

        List<IntervalData<T>> left = new ArrayList<>();
        List<IntervalData<T>> right = new ArrayList<>();

        for (IntervalData<T> interval : intervalList) {
            if (interval.getEnd() < median) {
                left.add(interval);
            } else if (interval.getStart() > median) {
                right.add(interval);
            } else {
                List<IntervalData<T>> posting = intervals.get(interval);
                if (posting == null) {
                    posting = new ArrayList<>();
                    intervals.put(interval, posting);
                }
                posting.add(interval);
            }
        }

        if (!left.isEmpty()) {
            leftNode = new IntervalNode<>(left);
        }
        if (!right.isEmpty()) {
            rightNode = new IntervalNode<>(right);
        }
    }

    /**
     * Perform a stabbing query on the node.
     *
     * @param time the time to query at.
     * @return	all intervals containing time.
     */
    public List<IntervalData<T>> stab(double time) {
        List<IntervalData<T>> result = new ArrayList<>();

        for (Entry<IntervalData<T>, List<IntervalData<T>>> entry : intervals.entrySet()) {
            if (entry.getKey().contains(time)) {
                for (IntervalData<T> interval : entry.getValue()) {
                    result.add(interval);
                }
            } else if (entry.getKey().getStart() > time) {
                break;
            }
        }

        if (time < center && leftNode != null) {
            result.addAll(leftNode.stab(time));
        } else if (time > center && rightNode != null) {
            result.addAll(rightNode.stab(time));
        }
        return result;
    }

    /**
     * Perform an interval intersection query on the node.
     *
     * @param target the interval to intersect.
     * @return all intervals containing time.
     */
    public List<IntervalData<T>> query(IntervalData<?> target) {
        List<IntervalData<T>> result = new ArrayList<>();

        for (Entry<IntervalData<T>, List<IntervalData<T>>> entry : intervals.entrySet()) {
            if (entry.getKey().intersects(target)) {
                for (IntervalData<T> interval : entry.getValue()) {
                    result.add(interval);
                }
            } else if (entry.getKey().getStart() > target.getEnd()) {
                break;
            }
        }

        if (target.getStart() < center && leftNode != null) {
            result.addAll(leftNode.query(target));
        }
        if (target.getEnd() > center && rightNode != null) {
            result.addAll(rightNode.query(target));
        }
        return result;
    }

    /**
     * @return interval center.
     */
    public double getCenter() {
        return center;
    }

    /**
     * @param center interval center to set.
     */
    public void setCenter(double center) {
        this.center = center;
    }

    /**
     * @return left node.
     */
    public IntervalNode<T> getLeft() {
        return leftNode;
    }

    /**
     * @param left the left node to set.
     */
    public void setLeft(IntervalNode<T> left) {
        this.leftNode = left;
    }

    /**
     * @return right node.
     */
    public IntervalNode<T> getRight() {
        return rightNode;
    }

    /**
     * @param right the right node to set.
     */
    public void setRight(IntervalNode<T> right) {
        this.rightNode = right;
    }

    /**
     * @param set the set to look on.
     * @return the median of the set, not interpolated
     */
    private Double getMedian(SortedSet<Double> set) {
        int i = 0;
        int middle = set.size() / 2;
        for (Double point : set) {
            if (i == middle) {
                return point;
            }
            ++i;
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(center + ": ");
        for (Entry<IntervalData<T>, List<IntervalData<T>>> entry : intervals.entrySet()) {
            sb.append("[" + entry.getKey().getStart() + "," + entry.getKey().getEnd() + "]:{");
            for (IntervalData<T> interval : entry.getValue()) {
                sb.append("(" + interval.getStart() + "," + interval.getEnd() + "," + interval.getData() + ")");
            }
            sb.append("} ");
        }
        return sb.toString();
    }
}
