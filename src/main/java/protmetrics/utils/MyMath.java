package protmetrics.utils;

public class MyMath
{
    public static double Round(double a_number,double a_precision)
    {
        double m_result=a_number*Math.pow(10,a_precision);
        m_result=Math.ceil(m_result);
        return m_result/Math.pow(10,a_precision);
    }

}