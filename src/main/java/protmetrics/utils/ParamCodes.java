package protmetrics.utils;

/**
 * Constants for command-line arguments.
 */
public class ParamCodes {

    private ParamCodes() {
    }
    /**
     * -MT: methods any of {RDF, 3DM, 3DC}
     */
    public static final String MT = "-Mt";
    /**
     * -O: full name of output file, with no extension.
     */
    public static final String OUTFILE = "-O";

    /**
     * -P: path to the directory containing PDBs
     */
    public static final String PDBFILE = "-P";

    /**
     * -M: path to property matrix file
     */
    public static final String PROPERTY_MATRIX_DIR = "-M";
    /**
     * -I: properties Indices
     */
    public static final String PROPERTY_INDICES = "-I";

    /**
     * -RDF_B: B for RDF Index, 150 by default
     */
    public static final String RDF_B = "-rdfB";

    /**
     * -RDF_F: F for RDF Index, 0.1 by default
     */
    public static final String RDF_F = "-rdfF";

    /**
     * -Mx: max Ratio for RDF and 3DMorse Indices. RDF 10 by default 3DMorse and
     * 3DCorrelation 31 by default.
     *
     */
    public static final String RMX = "-rMx";

    /**
     * -Mn: min ratio for RDF and 3DMorse Indices. RDF 2 by default 3DMorse 1 by
     * default.
     */
    public static final String RMN = "-rMn";

    /**
     * -S: a step for RDF,3DMorse, and 3DCorrelation Indices, 1 by default
     */
    public static final String S = "-S";

    /**
     * -FMT: output file format: 0 for arff, 1 for csv and 2 for bif, 2 by
     * default.
     */
    public static final String FMT = "-fmt";
}
