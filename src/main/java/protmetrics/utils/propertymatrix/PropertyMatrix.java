package protmetrics.utils.propertymatrix;
/// <summary>
/// Summary description for PropertyMatrix.

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import protmetrics.utils.BioUtils;

/// </summary>
public class PropertyMatrix {

    private PropertyVector[] propertyVectorsColumns;

    public PropertyMatrix(String pmFilePath) throws Exception {

        LineNumberReader m_sr = new LineNumberReader(new FileReader(pmFilePath));

        String m_actualLine;
        String[] m_actualLineElements;
        char[] m_sep = {' '};

        m_actualLine = m_sr.readLine();
        if (m_actualLine != null) {
            //-> Obtener los indices de las propiedades.
            m_actualLineElements = m_actualLine.trim().split("[\\s]+", 0);
            m_actualLineElements = BioUtils.procSplitString(m_actualLineElements);
            propertyVectorsColumns = new PropertyVector[m_actualLineElements.length];

            m_actualLine = m_sr.readLine();

            //-> Obtener los nombres de las propiedades
            if (m_actualLine != null) //2
            {
                m_actualLineElements = m_actualLine.trim().split("[\\s]+", 0);
                m_actualLineElements = BioUtils.procSplitString(m_actualLineElements);

                //-> Crear los PropertyVectors
                for (int i = 0; i < m_actualLineElements.length; i++) {
                    propertyVectorsColumns[i] = new PropertyVector(m_actualLineElements[i]);
                }

                m_actualLine = m_sr.readLine();
                while (m_actualLine != null) {
                    m_actualLineElements = m_actualLine.trim().split("[\\s]+", 0);
                    m_actualLineElements = BioUtils.procSplitString(m_actualLineElements);

                    for (int e = 3; e < m_actualLineElements.length; e++) {

                        propertyVectorsColumns[e - 3].addVectorElement(new PropertyVectorElement(Integer.parseInt(m_actualLineElements[0]), m_actualLineElements[1], m_actualLineElements[2], Double.parseDouble(m_actualLineElements[e])));
                    }
                    m_actualLine = m_sr.readLine();
                }
            }
        }
    }

    public PropertyMatrix(PropertyVector[] a_matrixColums) {
        this.propertyVectorsColumns = a_matrixColums;
    }

    public PropertyMatrix getSubPropertyMatrix(int[] a_selectedPVC) {
        /*Devuelve una nueva PropertyMatrix cuyo PropertyVectorsColumns es 
            un subconjunto de los de esta, especificados por los valores en el arreglo*/
        int m_length = a_selectedPVC.length;
        PropertyVector[] m_PVC = new PropertyVector[m_length];

        for (int i = 0; i < m_length; i++) {
            /*Ahora no se chequeara que el valor indicado sea menor que la longitud
                de PropertyVectorColumns*/
            m_PVC[i] = this.getPropertyVectorsColumns()[a_selectedPVC[i]];
        }
        return new PropertyMatrix(m_PVC);
    }

    /**
     * @return the propertyVectorsColumns
     */
    public PropertyVector[] getPropertyVectorsColumns() {
        return propertyVectorsColumns;
    }

}
