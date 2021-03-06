package protmetrics.dao.intervals;

/**
 * The Interval class maintains an interval with some associated data
 * https://github.com/kevinjdolan/intervaltree/tree/master/src/intervalTree
 *
 * @author Kevin Dolan
 *
 * @param <Type> The type of data being stored
 */
public class Interval implements Comparable<Interval> {

    protected double start;
    protected double end;
    protected IntervalType itype;
    private boolean lopen;
    private boolean ropen;

    public Interval(double start, double end) {
        this.start = start;
        this.end = end;
        this.setItype(IntervalType.LCLOSED_ROPEN);
    }

    public Interval(double start, double end, IntervalType itype) {
        this.start = start;
        this.end = end;
        this.setItype(itype);
    }

    public boolean isLopen() {
        return lopen;
    }

    public boolean isRopen() {
        return ropen;
    }

    public double getStart() {
        return start;
    }

    public double getEnd() {
        return end;
    }

    public IntervalType getItype() {
        return itype;
    }

    public void setItype(IntervalType itype) {
        this.itype = itype;
        switch (itype) {
            case LCLOSED_RCLOSED:
                lopen = false;
                ropen = false;
                break;

            case LCLOSED_ROPEN:
                lopen = false;
                ropen = true;
                break;

            case LOPEN_RCLOSED:
                lopen = true;
                ropen = false;
                break;

            case LOPEN_ROPEN:
                lopen = true;
                ropen = true;
                break;
        }
    }

    /**
     * @param time
     * @return	true if this interval contains time (inclusive)
     */
    public boolean contains(double value) {
        /*JComment: Add the = */
        if (lopen == true && ropen == true) {
            return value > start && value < end;
        }

        if (lopen == true && ropen == false) {
            return value > start && value <= end;
        }

        if (lopen == false && ropen == true) {
            return value >= start && value < end;
        }

        return value >= start && value <= end;
    }

    /**
     * @param other
     * @return	return true if this interval intersects other
     */
    public boolean intersects(Interval other) {
        double istart = Math.max(this.start, other.getStart());
        double iend = Math.min(this.end, other.getEnd());

        if (istart < iend) {
            return true;
        } else {
            if (istart == iend) {
                return this.contains(istart) && other.contains(istart);
            } else {
                return false;
            }
        }
    }

    /**
     * Return -1 if this interval's start time is less than the other, 1 if
     * greater In the event of a tie, -1 if this interval's end time is less
     * than the other, 1 if greater, 0 if same
     *
     * @param other
     * @return 1 or -1
     */
    public int compareTo(Interval other) {
        if (start < other.getStart()) {
            return -1;
        } else if (start > other.getStart()) {
            return 1;
        } else if (end < other.getEnd()) {
            return -1;
        } else if (end > other.getEnd()) {
            return 1;
        } else {
            return 0;
        }
    }

    public enum IntervalType {
        LOPEN_ROPEN,
        LOPEN_RCLOSED,
        LCLOSED_ROPEN,
        LCLOSED_RCLOSED;
    }

}
