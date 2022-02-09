package protmetrics.dao;

/**
 * Represents the inter alpha carbons distance matrix.
 */
public class IEDMatrix {

    /**
     *
     */
    public String[][] InterCAMatrix;
 
    /**
     *
     * @param interCAMatrix Inter alpha carbon matrix.
     */
    public IEDMatrix(String[][] interCAMatrix) {
        this.InterCAMatrix = interCAMatrix;
    }

    /**
     *
     * @param row row index.
     * @param column column index.
     * @return the value of the inter alpha carbon matrix.
     */
    public double getValueAt(int row, int column) {
        return Double.parseDouble(this.InterCAMatrix[row][column]);
    }

    /**
     *
     * @param elementCode code representing an element.
     * @return the element from its code.
     */
    public String getElementAt(int elementCode) {

        String m_sep = "_";
        String[] m_elementCode;
        String m_elementName;

        m_elementCode = this.InterCAMatrix[0][elementCode].trim().split(m_sep, 0);
        m_elementName = m_elementCode[m_elementCode.length - 1];

        return m_elementName;
    }

    /**
     * @return the number of rows.
     */
    public int getRows() {
        return this.InterCAMatrix[0].length;
    }

    /**
     * @return the number of columns.
     */
    public int getColumns() {
        return this.InterCAMatrix.length;
    }
}
