package protmetrics.dao.files.pdb;

/**
 * Represent a line within a .pdb file.
 * 
 */
public class PdbLine {

    String[] LineTokens;

    /**
     *
     */
    public String Line;

    /**
     *
     * @param string
     */
    public PdbLine(String a_line) {
        Line = a_line;
        LineTokens = null;
    }

    /**
     *
     * @param string
     * @param strings
     */
    public PdbLine(String a_line, String[] a_lineTokens) {
        Line = a_line;
        LineTokens = a_lineTokens;
    }
}
