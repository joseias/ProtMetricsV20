package protmetrics.utils;

/**
 * Constants for command-line arguments.
 */
public class ParamCodes {

    /**
     * -Mt: methods any of {RDF, 3DM, 3DC}
     */
    public static String Mt = "-Mt";
    /**
     * -O: full name of output file, with no extension.
     */
    public static String outFile = "-O";

    /**
     * -P: path to the directory containing PDBs
     */
    public static String pdbFile = "-P";

    /**
     * -M: path to property matrix file
     */
    public static String propertyMatrixDir = "-M";
    /**
     * -I: properties Indices
     */
    public static String propertyIndices = "-I";

    /**
     * -rdfB: B for RDF Index, 150 by default
     */
    public static String rdfB = "-rdfB";

    /**
     * -rdfF: F for RDF Index, 0.1 by default
     */
    public static String rdfF = "-rdfF";

    /**
     * -Mx: max Ratio for RDF and 3DMorse Indices. RDF 10 by default 3DMorse and
     * 3DCorrelation 31 by default.
     *
     */
    public static String rMx = "-rMx";

    /**
     * -Mn: min ratio for RDF and 3DMorse Indices. RDF 2 by default 3DMorse 1 by
     * default.
     */
    public static String rMn = "-rMn";

    /**
     * -S: a step for RDF,3DMorse, and 3DCorrelation Indices, 1 by default
     */
    public static String S = "-S";

    /**
     * -fmt: output file format: 0 for arff, 1 for csv and 2 for bif, 2 by
     * default.
     */
    public static String fmt = "-fmt";
}
