package protmetrics.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Implement some utility math functions.
 */
public class MyMath {

    /**
     *
     * @param value
     * @param precision
     * @return
     */
    public static double round(double value, int precision) {

        if (precision < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(precision, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
