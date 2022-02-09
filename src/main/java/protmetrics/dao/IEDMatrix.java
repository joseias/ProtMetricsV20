package protmetrics.dao;

public class IEDMatrix {

    public String[][] InterCAMatrix;

    public IEDMatrix(String[][] a_interCAMatrix) {
        this.InterCAMatrix = a_interCAMatrix;
    }

    public double getValueAt(int a_row, int a_column) {
        return Double.parseDouble(this.InterCAMatrix[a_row][a_column]);
    }

    public String getElementAt(int a_elementCode) {

        String m_sep = "_";
        String[] m_elementCode;
        String m_elementName;

        m_elementCode = this.InterCAMatrix[0][a_elementCode].trim().split(m_sep, 0);
        m_elementName = m_elementCode[m_elementCode.length - 1];

        return m_elementName;
    }

    public int getRows() {
        return this.InterCAMatrix[0].length;
    }

    public int getColumns() {
        return this.InterCAMatrix.length;
    }
}
