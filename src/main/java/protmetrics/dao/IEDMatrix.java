package protmetrics.dao;

/**
 * Represents the inter alpha carbons distance matrix.
 */
public class IEDMatrix {

    /**
     */
    private final String[][] interCAMatrix;

    /**
     * @param interCAMatrix Inter alpha carbon matrix.
     */
    public IEDMatrix(String[][] interCAMatrix) {
        this.interCAMatrix = interCAMatrix;
    }

    /**
     * @param row row index.
     * @param column column index.
     * @return the value of the inter alpha carbon matrix.
     */
    public double getValueAt(int row, int column) {
        return Double.parseDouble(this.interCAMatrix[row][column]);
    }

    /**
     * @param ecode code representing an element.
     * @return the element from its code.
     */
    public String getElementAt(int ecode) {

        String sep = "_";
        String[] code;
        String name;

        code = this.interCAMatrix[0][ecode].trim().split(sep, 0);
        name = code[code.length - 1];

        return name;
    }

    /**
     * @return the number of rows.
     */
    public int getRows() {
        return this.interCAMatrix[0].length;
    }

    /**
     * @return the number of columns.
     */
    public int getColumns() {
        return this.interCAMatrix.length;
    }
}
