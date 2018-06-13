package protmetrics.dao.intervals;

import protmetrics.dao.json.BondDescJSON;
import protmetrics.utils.BioUtils;

/**
 * The Interval class maintains an interval with some associated data
 * https://github.com/kevinjdolan/intervaltree/tree/master/src/intervalTree
 *
 * @author Kevin Dolan
 *
 * @param <Type> The type of data being stored
 */
public class IntervalData<Type> extends Interval {

    protected Type data;

    public IntervalData(Interval inter, Type data) {
        super(inter.start, inter.end, inter.getItype());
        this.data = data;
    }

    public IntervalData(double start, double end, Type data) {
        super(start, end);
        this.data = data;
    }

    public Type getData() {
        return data;
    }

    public void setData(Type data) {
        this.data = data;
    }

    public static IntervalData<Double> fromBondDescJSON(BondDescJSON bd) {
        Interval tmp = BioUtils.getIntervalFromDesc(bd.getSpec());
        IntervalData<Double> result;

        if (tmp != null) {
            result = new IntervalData<Double>(tmp, bd.getW());
            return result;
        } else {
            System.err.println("Interval " + bd.getSpec() + " specification is wrong, check it...");
            return null;
        }
    }
}
