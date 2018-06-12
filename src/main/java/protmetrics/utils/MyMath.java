package protmetrics.utils;

public class MyMath
{
    public static double round(double number,double pecision)
    {
        double m_result=number*Math.pow(10,pecision);
        m_result=Math.ceil(m_result);
        return m_result/Math.pow(10,pecision);
    }

}