package protmetrics.utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import protmetrics.dao.files.xyz.GAtom;
import protmetrics.dao.files.pdb.PdbAtomLine;
import protmetrics.dao.files.pdb.PdbFile;
import protmetrics.dao.intervals.Interval;
import protmetrics.dao.intervals.Interval.IntervalType;
import protmetrics.metrics.Wiener3D.IntervalTypeCodes;

/**
 * Class with utility functions.
 */
public class BioUtils {

    /**
     */
    public static final String SEPARATOR = ",";

    /**
     * Removes "" from an array of string tokens.
     * @param input input string array.
     * @return the tokens filtered.
     */
    public static String[] procSplitString(String[] input) {
        ArrayList<String> m_aux = new ArrayList();

        for (int i = 0; i < input.length; ++i) {
            if (!"".equals(input[i])) {
                m_aux.add(input[i]);
            }
        }
        String[] m_result = new String[m_aux.size()];
        for (int j = 0; j < m_result.length; ++j) {
            m_result[j] = (String) m_aux.get(j);
        }
        return m_result;
    }

    /**
     * @param aminoTLCode amino acid code in three letters format.
     * @return amino acid code in one letter format.
     */
    public static String aminoThreetoOne(String aminoTLCode) {

        if ((aminoTLCode.toUpperCase()).equals("ALA")) {
            return "A";
        }
        if ((aminoTLCode.toUpperCase()).equals("ARG")) {
            return "R";
        }
        if ((aminoTLCode.toUpperCase()).equals("ASP")) {
            return "D";
        }
        if ((aminoTLCode.toUpperCase()).equals("ASN")) {
            return "N";
        }
        if ((aminoTLCode.toUpperCase()).equals("CYS")) {
            return "C";
        }
        if ((aminoTLCode.toUpperCase()).equals("GLU")) {
            return "E";
        }
        if ((aminoTLCode.toUpperCase()).equals("GLN")) {
            return "Q";
        }
        if ((aminoTLCode.toUpperCase()).equals("GLY")) {
            return "G";
        }
        if ((aminoTLCode.toUpperCase()).equals("HIS")) {
            return "H";
        }
        if ((aminoTLCode.toUpperCase()).equals("ILE")) {
            return "I";
        }
        if ((aminoTLCode.toUpperCase()).equals("LEU")) {
            return "L";
        }
        if ((aminoTLCode.toUpperCase()).equals("LYS")) {
            return "K";
        }
        if ((aminoTLCode.toUpperCase()).equals("MET")) {
            return "M";
        }
        if ((aminoTLCode.toUpperCase()).equals("PHE")) {
            return "F";
        }
        if ((aminoTLCode.toUpperCase()).equals("PRO")) {
            return "P";
        }
        if ((aminoTLCode.toUpperCase()).equals("SER")) {
            return "S";
        }
        if ((aminoTLCode.toUpperCase()).equals("THR")) {
            return "T";
        }
        if ((aminoTLCode.toUpperCase()).equals("TRP")) {
            return "W";
        }
        if ((aminoTLCode.toUpperCase()).equals("TYR")) {
            return "Y";
        }
        if ((aminoTLCode.toUpperCase()).equals("VAL")) {
            return "V";
        }

        return "";
    }

    /**
     * @param aminoOLCode amino acid code in one letter format.
     * @return amino acid code in three letters format.
     */
    public static String aminoOnetoThree(String aminoOLCode) {

        if ((aminoOLCode.toUpperCase()).equals("A")) {
            return "ALA";
        }
        if ((aminoOLCode.toUpperCase()).equals("R")) {
            return "ARG";
        }
        if ((aminoOLCode.toUpperCase()).equals("D")) {
            return "ASP";
        }
        if ((aminoOLCode.toUpperCase()).equals("N")) {
            return "ASN";
        }
        if ((aminoOLCode.toUpperCase()).equals("C")) {
            return "CYS";
        }
        if ((aminoOLCode.toUpperCase()).equals("E")) {
            return "GLU";
        }

        if ((aminoOLCode.toUpperCase()).equals("Q")) {
            return "GLN";
        }
        if ((aminoOLCode.toUpperCase()).equals("G")) {
            return "GLY";
        }
        if ((aminoOLCode.toUpperCase()).equals("H")) {
            return "HIS";
        }
        if ((aminoOLCode.toUpperCase()).equals("I")) {
            return "ILE";
        }
        if ((aminoOLCode.toUpperCase()).equals("L")) {
            return "LEU";
        }
        if ((aminoOLCode.toUpperCase()).equals("K")) {
            return "LYS";
        }
        if ((aminoOLCode.toUpperCase()).equals("M")) {
            return "MET";
        }
        if ((aminoOLCode.toUpperCase()).equals("F")) {
            return "PHE";
        }
        if ((aminoOLCode.toUpperCase()).equals("P")) {
            return "PRO";
        }
        if ((aminoOLCode.toUpperCase()).equals("S")) {
            return "SER";
        }
        if ((aminoOLCode.toUpperCase()).equals("T")) {
            return "THR";
        }
        if ((aminoOLCode.toUpperCase()).equals("W")) {
            return "TRP";
        }
        if ((aminoOLCode.toUpperCase()).equals("Y")) {
            return "TYR";
        }
        if ((aminoOLCode.toUpperCase()).equals("V")) {
            return "VAL";
        }

        return "";

    }

    /**
     * @param chars string to be evaluated.
     * @return if the string is numeric.
     */
    public static boolean isInteger(String chars) {

        boolean m_result = true;
        int m_length = chars.length();
        int m_pos = 0;

        while (m_pos < m_length && m_result == true) {
            m_result = Character.isDigit(chars.charAt(m_pos));
            m_pos++;
        }
        return m_result;

    }

    /**
     * @param aminoTLCode amino acid three letters code.
     * @return if the input string is an amino acid three letters code.
     */
    public static boolean isAminoSequence(String aminoTLCode) {

        if ((aminoTLCode.toUpperCase()).equals("ALA")) {
            return true;
        }
        if ((aminoTLCode.toUpperCase()).equals("ARG")) {
            return true;
        }
        if ((aminoTLCode.toUpperCase()).equals("ASP")) {
            return true;
        }
        if ((aminoTLCode.toUpperCase()).equals("ASN")) {
            return true;
        }
        if ((aminoTLCode.toUpperCase()).equals("CYS")) {
            return true;
        }
        if ((aminoTLCode.toUpperCase()).equals("GLU")) {
            return true;
        }
        if ((aminoTLCode.toUpperCase()).equals("GLN")) {
            return true;
        }
        if ((aminoTLCode.toUpperCase()).equals("GLY")) {
            return true;
        }
        if ((aminoTLCode.toUpperCase()).equals("HIS")) {
            return true;
        }
        if ((aminoTLCode.toUpperCase()).equals("ILE")) {
            return true;
        }
        if ((aminoTLCode.toUpperCase()).equals("LEU")) {
            return true;
        }
        if ((aminoTLCode.toUpperCase()).equals("LYS")) {
            return true;
        }
        if ((aminoTLCode.toUpperCase()).equals("MET")) {
            return true;
        }
        if ((aminoTLCode.toUpperCase()).equals("PHE")) {
            return true;
        }
        if ((aminoTLCode.toUpperCase()).equals("PRO")) {
            return true;
        }
        if ((aminoTLCode.toUpperCase()).equals("SER")) {
            return true;
        }
        if ((aminoTLCode.toUpperCase()).equals("THR")) {
            return true;
        }
        if ((aminoTLCode.toUpperCase()).equals("TRP")) {
            return true;
        }
        if ((aminoTLCode.toUpperCase()).equals("TYR")) {
            return true;
        }
        if ((aminoTLCode.toUpperCase()).equals("VAL")) {
            return true;
        }

        return false;
    }

    /**
     * @param path path to the configuration file.
     * @return properties object.
     */
    public static Properties loadProperties(String path) {
        try {
            Properties prop = new Properties();
            prop.load(new FileReader(path));
            return prop;
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    /**
     * @param indices indices of the selected properties encoded as 1,2,5,...
     * @return an array with the indices of the selected properties.
     */
    public static int[] getSelectedIndices(String indices) {

        String[] tokens = indices.split(SEPARATOR);
        return Arrays.stream(tokens).mapToInt(e -> {
            return Integer.parseInt(e);
        }).toArray();
    }

    /**
     * *
     * Creates the inter CA distance matrix, for 3D indices...
     *
     * @param pdb pdb file wrapper.
     * @return inter CA distance matrix with values as string.
     * @throws Exception for problems computing inter CA distance matrix.
     */
    public static String[][] getInterCADistMatrixN(PdbFile pdb) throws Exception {

        PdbAtomLine[] caData = pdb.getCALines();
        String[][] result = new String[caData.length + 1][caData.length + 1];

        result[0][0] = "ELEMENTS";
        for (int f = 0; f < caData.length; ++f) {
            /* CA array start at 0*/
            result[f + 1][0] = result[0][f + 1] = (f + 1) + "_" + caData[f].getAtomType() + "_" + caData[f].getAminoType();

        }
        for (int i = 0; i < caData.length; ++i) {
            for (int j = i; j < caData.length; ++j) {
                double caDist = caData[i].getLocation().distance(caData[j].getLocation());
                result[i + 1][j + 1] = result[j + 1][i + 1] = Double.toString(MyMath.round(caDist, 4));
            }
        }
        return result;
    }

    /**
     * @param atomA atom A.
     * @param atomB atom B.
     * @return the distance between atoms A and B.
     */
    public static double getDistance(GAtom atomA, GAtom atomB) {
        return atomA.getLocation().distance(atomB.getLocation());
    }

    /**
     * @param desc description of the interval.
     * @return interval from its description.
     */
    public static Interval getIntervalFromDesc(String desc) {
        String regex = "([\\[]|[\\(])(([0-9]+.[0-9]+),([0-9]+.[0-9]+))([\\]]|[\\)])";

        Pattern r = Pattern.compile(regex);
        Matcher m = r.matcher(desc);

        if (m.find()) {
            String lo = m.group(1);
            String ll = m.group(3);
            String ul = m.group(4);
            String ro = m.group(5);

            double lld = Double.parseDouble(ll);
            double uld = Double.parseDouble(ul);

            boolean lclosed_rclosed = lo.equals(IntervalTypeCodes.LCLOSED) && ro.equals(IntervalTypeCodes.RCLOSED);
            boolean lclosed_ropen = lo.equals(IntervalTypeCodes.LCLOSED) && ro.equals(IntervalTypeCodes.ROPEN);
            boolean lopen_ropen = lo.equals(IntervalTypeCodes.LOPEN) && ro.equals(IntervalTypeCodes.ROPEN);
            boolean lopen_rclosed = lo.equals(IntervalTypeCodes.LOPEN) && ro.equals(IntervalTypeCodes.RCLOSED);

            IntervalType itype = IntervalType.LCLOSED_ROPEN;

            /* better implementation with nested if, but for readability and because if are exclusive */
            if (lclosed_rclosed) {
                itype = IntervalType.LCLOSED_RCLOSED;
            }
            if (lclosed_ropen) {
                itype = IntervalType.LCLOSED_ROPEN;
            }
            if (lopen_ropen) {
                itype = IntervalType.LOPEN_ROPEN;
            }
            if (lopen_rclosed) {
                itype = IntervalType.LOPEN_RCLOSED;
            }
            if (!(lclosed_rclosed || lclosed_ropen || lopen_rclosed || lopen_ropen)) {
                System.err.println("Interval " + desc + " specification is wrong, check it...");
                return null;
            }

            return new Interval(lld, uld, itype);

        } else {
            System.err.println("Interval " + desc + " specification is wrong, check it...");
            return null;
        }
    }
}
