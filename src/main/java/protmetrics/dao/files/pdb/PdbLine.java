package protmetrics.dao.files.pdb;

/**
 * Represent a line within a .pdb file.
 */
public class PdbLine {

    /**
     *
     */
    protected String[] lineTokens;

    /**
     */
    protected String line;

    /**
     * @param line string representing a line within a .pdb file.
     */
    public PdbLine(String line) {
        this.line = line;
        this.lineTokens = null;
    }

    /**
     * @param line string representing a line within a .pdb file.
     * @param lineTokens tokens of the line.
     */
    public PdbLine(String line, String[] lineTokens) {
        this.line = line;
        this.lineTokens = lineTokens;
    }

    /**
     * @return the lineTokens
     */
    public String[] getLineTokens() {
        return lineTokens;
    }

    /**
     * @return the line
     */
    public String getLine() {
        return line;
    }
}
