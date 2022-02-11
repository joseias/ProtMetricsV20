package protmetrics.dao.intervals;

import java.util.logging.Level;
import java.util.logging.Logger;
import protmetrics.dao.json.BondDescJSON;
import protmetrics.utils.BioUtils;

/**
 * Represents the data associated with an interval. based in
 * https://github.com/kevinjdolan/intervaltree (WTFPL)
 *
 * @param <T> the type of the data that the interval holds.
 */
public class IntervalData<T> extends Interval {

    /**
     */
    protected T data;

    /**
     * @param inter the interval.
     * @param data the type.
     */
    public IntervalData(Interval inter, T data) {
        super(inter.start, inter.end, inter.getItype());
        this.data = data;
    }

    /**
     * @param start interval start.
     * @param end interval end.
     * @param data interval data type.
     */
    public IntervalData(double start, double end, T data) {
        super(start, end);
        this.data = data;
    }

    /**
     * @return interval data.
     */
    public T getData() {
        return data;
    }

    /**
     * @param data interval data to set.
     */
    public void setData(T data) {
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
            String msg = String.format("Interval specification is wrong, check it...", bd.getSpec());
            Logger.getLogger(Interval.class.getName()).log(Level.SEVERE, msg);
            return null;
        }
    }
}
