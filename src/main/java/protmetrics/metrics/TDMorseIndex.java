package protmetrics.metrics;

import protmetrics.dao.IEDMatrix;
import protmetrics.dao.PropertyVector;
import protmetrics.utils.MyMath;

public class TDMorseIndex
	{

		public TDMorseIndex()
		{
			//
			// TODO: Add constructor logic here
			//
		}

		public static double GetTDMorseIndex(PropertyVector a_pv, IEDMatrix a_interCAMatrix,double a_S)
		{

			boolean[] m_found={true};
			double m_TDMorseIndexSum=0;
			double m_factor;
			double m_pMult;
			double m_radium;

			for(int f=2;f<a_interCAMatrix.getRows();f++)
			{
				for(int c=1;c<f;c++)
				{  //														Af																								Aj																									e(-B(r-rij))
					m_pMult=a_pv.getValueFromName(a_interCAMatrix.getElementAt(f),m_found)*a_pv.getValueFromName(a_interCAMatrix.getElementAt(c),m_found);
					m_radium=a_interCAMatrix.getValueAt(f,c);
					m_factor=Math.sin(a_S*m_radium)/(a_S*m_radium);
					m_TDMorseIndexSum=m_TDMorseIndexSum+m_pMult*m_factor;
				}
			}
			return MyMath.Round(m_TDMorseIndexSum,2);

		}//public double GetTDMorseIndex(PropertyVector a_pv, IEDMatrix a_interCAMatrix,double m_rdfRadium,double m_rdfF,double m_rdfB)
	}