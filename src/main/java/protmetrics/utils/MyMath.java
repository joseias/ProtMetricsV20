package protmetrics.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MyMath {

    public static double round(double value, int precision) {
//        double m_result=value*Math.pow(10,precision);
//        m_result=Math.ceil(m_result);
//        return m_result/Math.pow(10,precision);

        if (precision < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(precision, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
