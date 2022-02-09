package protmetrics.utils.propertymatrix;

import java.io.FileReader;
import java.io.LineNumberReader;
import protmetrics.utils.BioUtils;

/**
 * Wrapper for the amino acid property matrix.
 */
public class PropertyMatrix {

    private PropertyVector[] propertyVectorsColumns;

    /**
     *
     * @param pmFilePath
     * @throws Exception
     */
    public PropertyMatrix(String pmFilePath) throws Exception {

        LineNumberReader m_sr = new LineNumberReader(new FileReader(pmFilePath));

        String m_actualLine;
        String[] m_actualLineElements;
        char[] m_sep = {' '};

        m_actualLine = m_sr.readLine();
        if (m_actualLine != null) {
            /* get property indices */
            m_actualLineElements = m_actualLine.trim().split("[\\s]+", 0);
            m_actualLineElements = BioUtils.procSplitString(m_actualLineElements);
            propertyVectorsColumns = new PropertyVector[m_actualLineElements.length];

            m_actualLine = m_sr.readLine();

            /* get property indices */
            if (m_actualLine != null) //2
            {
                m_actualLineElements = m_actualLine.trim().split("[\\s]+", 0);
                m_actualLineElements = BioUtils.procSplitString(m_actualLineElements);

                /* create property vectors*/
                for (int i = 0; i < m_actualLineElements.length; ++i) {
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

    /**
     *
     * @param a_matrixColums
     */
    public PropertyMatrix(PropertyVector[] a_matrixColums) {
        this.propertyVectorsColumns = a_matrixColums;
    }

    /**
     * *
     * Index of properties (the first property has index 0) returns a subset of
     * the PropertyVectorsColumns as a new PropertyMatrix
     *
     * @param a_selectedPVC
     * @return
     */
    public PropertyMatrix getSubPropertyMatrix(int[] a_selectedPVC) {
        int m_length = a_selectedPVC.length;
        PropertyVector[] m_PVC = new PropertyVector[m_length];

        for (int i = 0; i < m_length; ++i) {
            /* not checking if value is < PropertyVectorColumns length */
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
