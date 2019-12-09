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

public class BioUtils {

    public static final String SEPARATOR = ",";

    public BioUtils() {
        //
        // TODO: add constructor logic here
        //
    }

    public static String[] procSplitString(String[] a_input) {
        ArrayList m_aux = new ArrayList();

        for (int i = 0; i < a_input.length; i++) {
            if (a_input[i] != "") {
                m_aux.add(a_input[i]);
            }//f(a_input[i]!="")
        }//for(int i=0;i<a_input.length;i++)
        String[] m_result = new String[m_aux.size()];
        for (int j = 0; j < m_result.length; j++) {
            m_result[j] = (String) m_aux.get(j);
        }
        return m_result;
    }//public String[] procSplitString(String a_input)

    public static String aminoThreetoOne(String a_aminoTLCode) {

        if ((a_aminoTLCode.toUpperCase()).equals("ALA")) {
            return "A";
        }
        if ((a_aminoTLCode.toUpperCase()).equals("ARG")) {
            return "R";
        }
        if ((a_aminoTLCode.toUpperCase()).equals("ASP")) {
            return "D";
        }
        if ((a_aminoTLCode.toUpperCase()).equals("ASN")) {
            return "N";
        }
        if ((a_aminoTLCode.toUpperCase()).equals("CYS")) {
            return "C";
        }
        if ((a_aminoTLCode.toUpperCase()).equals("GLU")) {
            return "E";
        }
        if ((a_aminoTLCode.toUpperCase()).equals("GLN")) {
            return "Q";
        }
        if ((a_aminoTLCode.toUpperCase()).equals("GLY")) {
            return "G";
        }
        if ((a_aminoTLCode.toUpperCase()).equals("HIS")) {
            return "H";
        }
        if ((a_aminoTLCode.toUpperCase()).equals("ILE")) {
            return "I";
        }
        if ((a_aminoTLCode.toUpperCase()).equals("LEU")) {
            return "L";
        }
        if ((a_aminoTLCode.toUpperCase()).equals("LYS")) {
            return "K";
        }
        if ((a_aminoTLCode.toUpperCase()).equals("MET")) {
            return "M";
        }
        if ((a_aminoTLCode.toUpperCase()).equals("PHE")) {
            return "F";
        }
        if ((a_aminoTLCode.toUpperCase()).equals("PRO")) {
            return "P";
        }
        if ((a_aminoTLCode.toUpperCase()).equals("SER")) {
            return "S";
        }
        if ((a_aminoTLCode.toUpperCase()).equals("THR")) {
            return "T";
        }
        if ((a_aminoTLCode.toUpperCase()).equals("TRP")) {
            return "W";
        }
        if ((a_aminoTLCode.toUpperCase()).equals("TYR")) {
            return "Y";
        }
        if ((a_aminoTLCode.toUpperCase()).equals("VAL")) {
            return "V";
        }

        return "";
    }

    public static String aminoOnetoThree(String a_aminoOLCode) {

        if ((a_aminoOLCode.toUpperCase()).equals("A")) {
            return "ALA";
        }
        if ((a_aminoOLCode.toUpperCase()).equals("R")) {
            return "ARG";
        }
        if ((a_aminoOLCode.toUpperCase()).equals("D")) {
            return "ASP";
        }
        if ((a_aminoOLCode.toUpperCase()).equals("N")) {
            return "ASN";
        }
        if ((a_aminoOLCode.toUpperCase()).equals("C")) {
            return "CYS";
        }
        if ((a_aminoOLCode.toUpperCase()).equals("E")) {
            return "GLU";
        }

        if ((a_aminoOLCode.toUpperCase()).equals("Q")) {
            return "GLN";
        }
        if ((a_aminoOLCode.toUpperCase()).equals("G")) {
            return "GLY";
        }
        if ((a_aminoOLCode.toUpperCase()).equals("H")) {
            return "HIS";
        }
        if ((a_aminoOLCode.toUpperCase()).equals("I")) {
            return "ILE";
        }
        if ((a_aminoOLCode.toUpperCase()).equals("L")) {
            return "LEU";
        }
        if ((a_aminoOLCode.toUpperCase()).equals("K")) {
            return "LYS";
        }
        if ((a_aminoOLCode.toUpperCase()).equals("M")) {
            return "MET";
        }
        if ((a_aminoOLCode.toUpperCase()).equals("F")) {
            return "PHE";
        }
        if ((a_aminoOLCode.toUpperCase()).equals("P")) {
            return "PRO";
        }
        if ((a_aminoOLCode.toUpperCase()).equals("S")) {
            return "SER";
        }
        if ((a_aminoOLCode.toUpperCase()).equals("T")) {
            return "THR";
        }
        if ((a_aminoOLCode.toUpperCase()).equals("W")) {
            return "TRP";
        }
        if ((a_aminoOLCode.toUpperCase()).equals("Y")) {
            return "TYR";
        }
        if ((a_aminoOLCode.toUpperCase()).equals("V")) {
            return "VAL";
        }

        return "";

    }

    public static boolean isInteger(String a_chars) {

        boolean m_result = true;
        int m_length = a_chars.length();
        int m_pos = 0;

        while (m_pos < m_length && m_result == true) {
            m_result = Character.isDigit(a_chars.charAt(m_pos));
            m_pos++;
        }
        return m_result;

    }//public boolean isInteger(String a_chars)

    public static boolean isAminoSequence(String a_aminoTLCode) {

        if ((a_aminoTLCode.toUpperCase()).equals("ALA")) {
            return true;
        }
        if ((a_aminoTLCode.toUpperCase()).equals("ARG")) {
            return true;
        }
        if ((a_aminoTLCode.toUpperCase()).equals("ASP")) {
            return true;
        }
        if ((a_aminoTLCode.toUpperCase()).equals("ASN")) {
            return true;
        }
        if ((a_aminoTLCode.toUpperCase()).equals("CYS")) {
            return true;
        }
        if ((a_aminoTLCode.toUpperCase()).equals("GLU")) {
            return true;
        }
        if ((a_aminoTLCode.toUpperCase()).equals("GLN")) {
            return true;
        }
        if ((a_aminoTLCode.toUpperCase()).equals("GLY")) {
            return true;
        }
        if ((a_aminoTLCode.toUpperCase()).equals("HIS")) {
            return true;
        }
        if ((a_aminoTLCode.toUpperCase()).equals("ILE")) {
            return true;
        }
        if ((a_aminoTLCode.toUpperCase()).equals("LEU")) {
            return true;
        }
        if ((a_aminoTLCode.toUpperCase()).equals("LYS")) {
            return true;
        }
        if ((a_aminoTLCode.toUpperCase()).equals("MET")) {
            return true;
        }
        if ((a_aminoTLCode.toUpperCase()).equals("PHE")) {
            return true;
        }
        if ((a_aminoTLCode.toUpperCase()).equals("PRO")) {
            return true;
        }
        if ((a_aminoTLCode.toUpperCase()).equals("SER")) {
            return true;
        }
        if ((a_aminoTLCode.toUpperCase()).equals("THR")) {
            return true;
        }
        if ((a_aminoTLCode.toUpperCase()).equals("TRP")) {
            return true;
        }
        if ((a_aminoTLCode.toUpperCase()).equals("TYR")) {
            return true;
        }
        if ((a_aminoTLCode.toUpperCase()).equals("VAL")) {
            return true;
        }

        return false;
    }//public static boolean isAminoSequence(String a_sequence)

    public static Properties loadProperties(String confFilePath) {
        try {
            Properties p = new Properties();
            p.load(new FileReader(confFilePath));
            return p;
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

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
     * @param pdb
     * @return
     * @throws Exception
     */
    public static String[][] getInterCADistMatrixN(PdbFile pdb) throws Exception {

        double x;
        double y;
        double z;

        PdbAtomLine[] caData = pdb.getCALines();
        String[][] result = new String[caData.length + 1][caData.length + 1];

        result[0][0] = "ELEMENTS";
        for (int f = 0; f < caData.length; f++) {
            //El arreglo de CA comienza en 0
            result[f + 1][0] = result[0][f + 1] = (f + 1) + "_" + caData[f].getAtomType() + "_" + caData[f].getAminoType();

        }
        for (int i = 0; i < caData.length; i++) {
            for (int j = i; j < caData.length; j++) {
                double caDist = caData[i].getLocation().distance(caData[j].getLocation());
                result[i + 1][j + 1] = result[j + 1][i + 1] = Double.toString(MyMath.round(caDist, 4));
            }
        }
        return result;
    }

    private static int precision = -1;

    public static double getDistance(GAtom a, GAtom b) {
        return a.getLocation().distance(b.getLocation());
    }

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

            /*Better implementation with nested if, but for readability and because if are exclusive*/
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
