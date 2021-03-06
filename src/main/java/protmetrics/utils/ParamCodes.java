/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package protmetrics.utils;

/**
 *
 * @author fernan
 */
public class ParamCodes {

    /**
     * -Mt: Methods any of {RDF, 3DM, 3DC}
     */
    public static String Mt = "-Mt";
    /**
     * -O: Full name of output file , with no extension.
     */
    public static String outFile = "-O";

    /**
     * -P: Path to directory containing PDBs
     */
    public static String pdbFile = "-P";

    /**
     * -M: Path to property matrix file
     */
    public static String propertyMatrixDir = "-M";
    /**
     * -I: Properties Indices
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
     * -Mx: Max Ratio for RDF and 3DMorse Indices. RDF 10 by default 3DMorse and
     * 3DCorrelation 31 by default
     *
     */
    public static String rMx = "-rMx";

    /**
     * -Mn: Min ratio for RDF and 3DMorse Indices. RDF 2 by default 3DMorse 1 by
     * default
     */
    public static String rMn = "-rMn";

    /**
     * -S: Step for RDF,3DMorse and 3DCorrelation Indices, 1 by default
     */
    public static String S = "-S";

    /**
     * -fmt: Output file format: 0 for arff, 1 for csv and 2 for bif, 2 by
     * default
     */
    public static String fmt = "-fmt";

}
