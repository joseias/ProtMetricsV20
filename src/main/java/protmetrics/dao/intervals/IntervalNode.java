package protmetrics.dao.intervals;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

/**
 * The Node class contains the interval tree information for one single node
 * https://github.com/kevinjdolan/intervaltree/tree/master/src/intervalTree
 *
 * @author Kevin Dolan
 */
public class IntervalNode<Type> {

    private SortedMap<IntervalData<Type>, List<IntervalData<Type>>> intervals;
    private double center;
    private IntervalNode<Type> leftNode;
    private IntervalNode<Type> rightNode;

    public IntervalNode() {
        intervals = new TreeMap<IntervalData<Type>, List<IntervalData<Type>>>();
        center = 0;
        leftNode = null;
        rightNode = null;
    }

    public IntervalNode(List<IntervalData<Type>> intervalList) {

        intervals = new TreeMap<IntervalData<Type>, List<IntervalData<Type>>>();

        SortedSet<Double> endpoints = new TreeSet<Double>();

        for (IntervalData<Type> interval : intervalList) {
            endpoints.add(interval.getStart());
            endpoints.add(interval.getEnd());
        }

        double median = getMedian(endpoints);
        center = median;

        List<IntervalData<Type>> left = new ArrayList<IntervalData<Type>>();
        List<IntervalData<Type>> right = new ArrayList<IntervalData<Type>>();

        for (IntervalData<Type> interval : intervalList) {
            if (interval.getEnd() < median) {
                left.add(interval);
            } else if (interval.getStart() > median) {
                right.add(interval);
            } else {
                List<IntervalData<Type>> posting = intervals.get(interval);
                if (posting == null) {
                    posting = new ArrayList<IntervalData<Type>>();
                    intervals.put(interval, posting);
                }
                posting.add(interval);
            }
        }

        if (left.size() > 0) {
            leftNode = new IntervalNode<Type>(left);
        }
        if (right.size() > 0) {
            rightNode = new IntervalNode<Type>(right);
        }
    }

    /**
     * Perform a stabbing query on the node
     *
     * @param time the time to query at
     * @return	all intervals containing time
     */
    public List<IntervalData<Type>> stab(double time) {
        List<IntervalData<Type>> result = new ArrayList<IntervalData<Type>>();

        for (Entry<IntervalData<Type>, List<IntervalData<Type>>> entry : intervals.entrySet()) {
            if (entry.getKey().contains(time)) {
                for (IntervalData<Type> interval : entry.getValue()) {
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
     * Perform an interval intersection query on the node
     *
     * @param target the interval to intersect
     * @return	all intervals containing time
     */
    public List<IntervalData<Type>> query(IntervalData<?> target) {
        List<IntervalData<Type>> result = new ArrayList<IntervalData<Type>>();

        for (Entry<IntervalData<Type>, List<IntervalData<Type>>> entry : intervals.entrySet()) {
            if (entry.getKey().intersects(target)) {
                for (IntervalData<Type> interval : entry.getValue()) {
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

    public double getCenter() {
        return center;
    }

    public void setCenter(double center) {
        this.center = center;
    }

    public IntervalNode<Type> getLeft() {
        return leftNode;
    }

    public void setLeft(IntervalNode<Type> left) {
        this.leftNode = left;
    }

    public IntervalNode<Type> getRight() {
        return rightNode;
    }

    public void setRight(IntervalNode<Type> right) {
        this.rightNode = right;
    }

    /**
     * @param set the set to look on
     * @return	the median of the set, not interpolated
     */
    private Double getMedian(SortedSet<Double> set) {
        int i = 0;
        int middle = set.size() / 2;
        for (Double point : set) {
            if (i == middle) {
                return point;
            }
            i++;
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(center + ": ");
        for (Entry<IntervalData<Type>, List<IntervalData<Type>>> entry : intervals.entrySet()) {
            sb.append("[" + entry.getKey().getStart() + "," + entry.getKey().getEnd() + "]:{");
            for (IntervalData<Type> interval : entry.getValue()) {
                sb.append("(" + interval.getStart() + "," + interval.getEnd() + "," + interval.getData() + ")");
            }
            sb.append("} ");
        }
        return sb.toString();
    }

}
