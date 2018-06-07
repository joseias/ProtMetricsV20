package protmetrics.metrics;

import java.io.File;
import java.util.ArrayList;

import protmetrics.dao.IEDMatrix;
import protmetrics.dao.PropertyMatrix;
import protmetrics.dao.PropertyVector;
import protmetrics.utils.MyMath;
	/// <summary>
	/// Summary description for ProteinIndex.
	/// </summary>
	public class ProteinIndex
	{

		public ProteinIndex()
		{
			//
			// TODO: Add constructor logic here
			//
		}

		public static String[] GetBiologicalIndex(PropertyMatrix a_pm, String[][] a_interCAMatrix,int a_step,String a_pdbName)
		{

			String[] m_result=new String[a_pm.PropertyVectorsColumns.length+1];
			double[] m_propertyIndex=new double[a_pm.PropertyVectorsColumns.length]; //-> Para ir almacenando el indice relativo a cada propiedad.
			String[] m_elementDataF;
			String[] m_elementDataC;
			char[] m_sep={'_'};

			//String m_elementName;
			PropertyVector m_pv;
		    File m_auxFile=new File(a_pdbName);
			m_result[0]=m_auxFile.getName()+"_"+a_step;
			boolean[] m_found={false};
			for(int f=1;f<a_interCAMatrix.length;f++)
			{
				for(int c=f;c<a_interCAMatrix[0].length;c++)
				{
					if(Math.round(Double.parseDouble(a_interCAMatrix[f][c]))==a_step)
					{
						m_elementDataF=a_interCAMatrix[0][f].trim().split("[\\s]+",0);
						m_elementDataC=a_interCAMatrix[c][0].trim().split("[\\s]+",0);

						for(int p=0;p<a_pm.PropertyVectorsColumns.length;p++)
						{
							m_pv=a_pm.PropertyVectorsColumns[p];
							//-> Ahora no chequeo m_found Se supone que este bien.
							m_propertyIndex[p]=m_propertyIndex[p]+m_pv.getValueFromName(m_elementDataC[2],m_found)*m_pv.getValueFromName(m_elementDataF[2],m_found);
						}
					}
				}

			}
			//-> Despues que se tienen calculados los indices para cada propiedad segun el paso, formar la fila
			for(int p=0;p<a_pm.PropertyVectorsColumns.length;p++)
			{
				m_result[p+1]=a_pm.PropertyVectorsColumns[p].PropertyName+"_"+m_propertyIndex[p];
			}
			return m_result;

		}//public static String[,] GetBiologicalIndex(PropertyMatrix a_pm, String[,] a_interCAMatrix)

		public static ArrayList GetFMatrixRow(PropertyVector a_pv,String a_pdbName)
		{
			return new ArrayList();
		}

		public static double GetBioIndex(PropertyVector a_pv, IEDMatrix a_interCAMatrix,int a_step,int a_minBound,int a_maxBound)
		{

			int m_cantSumandos=0;
			double m_indexSum=0;
			double m_interElementDist;
			boolean[] m_found={true};

			for(int f=1;f<a_interCAMatrix.getRows();f++)
			{
				for(int c=f;c<a_interCAMatrix.getColumns();c++)
				{
					m_interElementDist=a_interCAMatrix.getValueAt(f,c);
					if((m_interElementDist>a_step-a_minBound)&&(m_interElementDist<a_step+a_maxBound))
					{
						//-> Asumiendo que en el vector de propiedades estan todos los elementos relacionados en la matrix de distancia.
						m_indexSum=m_indexSum+a_pv.getValueFromName(a_interCAMatrix.getElementAt(f),m_found)*a_pv.getValueFromName(a_interCAMatrix.getElementAt(c),m_found);
						m_cantSumandos++;
					}
				}
			}
			return MyMath.Round(m_indexSum/m_cantSumandos,2);

		}//public double GetBioIndex(PropertyVector a_pv, String[,] a_interCAMatrix,int a_step)

	}
