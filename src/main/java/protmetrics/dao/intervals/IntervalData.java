package protmetrics.dao.intervals;

import protmetrics.dao.json.BondDescJSON;
import protmetrics.utils.BioUtils;

/**
 * Represents the data associated with an interval.
 * based in https://github.com/kevinjdolan/intervaltree (WTFPL)
 * @param <Type> the type of the data that the interval holds.
 */
public class IntervalData<Type> extends Interval {

    /**
     */
    protected Type data;

    /**
     * @param inter Interval.
     * @param data Type.
     */
    public IntervalData(Interval inter, Type data) {
        super(inter.start, inter.end, inter.getItype());
        this.data = data;
    }

    /**
     * @param start interval start.
     * @param end interval end.
     * @param data interval data type.
     */
    public IntervalData(double start, double end, Type data) {
        super(start, end);
        this.data = data;
    }

    /**
     * @return interval data.
     */
    public Type getData() {
        return data;
    }

    /**
     * @param data interval data to set.
     */
    public void setData(Type data) {
        this.data = data;
    }

    /**
     * @param bd bond description.
     * @return interval data.
     */
    public static IntervalData<Double> fromBondDescJSON(BondDescJSON bd) {
        Interval tmp = BioUtils.getIntervalFromDesc(bd.getSpec());
        IntervalData<Double> result;

        if (tmp != null) {
            result = new IntervalData<>(tmp, bd.getW());
            return result;
        } else {
            System.err.println("Interval " + bd.getSpec() + " specification is wrong, check it...");
            return null;
        }
    }
}
