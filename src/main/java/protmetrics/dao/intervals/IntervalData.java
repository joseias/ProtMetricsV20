package protmetrics.dao.intervals;

import protmetrics.dao.json.BondDescJSON;
import protmetrics.utils.BioUtils;

/**
 * Represents the data associated with an interval.
 * based in https://github.com/kevinjdolan/intervaltree (WTFPL)
 * @param <Type>
 */
public class IntervalData<Type> extends Interval {

    /**
     *
     */
    protected Type data;

    /**
     *
     * @param intrvl
     * @param type
     */
    public IntervalData(Interval inter, Type data) {
        super(inter.start, inter.end, inter.getItype());
        this.data = data;
    }

    /**
     *
     * @param d
     * @param d1
     * @param type
     */
    public IntervalData(double start, double end, Type data) {
        super(start, end);
        this.data = data;
    }

    /**
     *
     * @return
     */
    public Type getData() {
        return data;
    }

    /**
     *
     * @param data
     */
    public void setData(Type data) {
        this.data = data;
    }

    /**
     *
     * @param bd
     * @return
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
