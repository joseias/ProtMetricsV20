package protmetrics.metrics;

import protmetrics.dao.IEDMatrix;
import protmetrics.dao.PropertyVector;
import protmetrics.utils.MyMath;

/// <summary>
/// Summary description for RDFIndex.
/// </summary>
public class RDFIndex {


    public static double GetRDFIndex(PropertyVector a_pv, IEDMatrix a_interCAMatrix, double m_rdfRadium, double m_rdfF, double m_rdfB) {

        boolean[] m_found = {true};
        double m_rdfIndexSum = 0;
        double m_exp;
        double m_pMult;

        for (int f = 1; f < a_interCAMatrix.getRows() - 1; f++) {
            for (int c = f + 1; c < a_interCAMatrix.getColumns(); c++) {  		//												Af																								Aj																									e(-B(r-rij))
                m_pMult = a_pv.getValueFromName(a_interCAMatrix.getElementAt(f), m_found) * a_pv.getValueFromName(a_interCAMatrix.getElementAt(c), m_found);

                m_exp = Math.exp(-m_rdfB * (Math.pow(m_rdfRadium - a_interCAMatrix.getValueAt(f, c), 2)));

                m_rdfIndexSum = m_rdfIndexSum + m_rdfF * m_pMult * m_exp;
            }
        }
        return MyMath.Round(m_rdfIndexSum, 2);
    }//public double GetRDFIndex(PropertyVector a_pv, IEDMatrix a_interCAMatrix,double m_rdfRadium,double m_rdfF,double m_rdfB)

}
