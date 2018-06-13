package protmetrics.dao.files.pdb;

public class PdbLine {

    String[] LineTokens;
    public String Line;

    public PdbLine(String a_line) {
        Line = a_line;
        LineTokens = null;
    }

    public PdbLine(String a_line, String[] a_lineTokens) {
        Line = a_line;
        LineTokens = a_lineTokens;
    }
}
