package protmetrics.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    private BioUtils() {
    }

    /**
     * Removes "" from an array of string tokens.
     *
     * @param input the input string array.
     * @return the tokens filtered.
     */
    public static String[] procSplitString(String[] input) {
        ArrayList<String> aux = new ArrayList<>();

        for (int i = 0; i < input.length; ++i) {
            if (!"".equals(input[i])) {
                aux.add(input[i]);
            }
        }
        String[] result = new String[aux.size()];
        for (int j = 0; j < result.length; ++j) {
            result[j] = aux.get(j);
        }
        return result;
    }

    /**
     * @param aminoTLCode amino acid code in three letters format.
     * @return amino acid code in one letter format.
     */
    public static String aminoThree2One(String aminoTLCode) {

        String code = aminoTLCode.toUpperCase();
        switch (code) {
            case "ALA":
                return "A";

            case "ARG":
                return "R";

            case "ASP":
                return "D";

            case "ASN":
                return "N";

            case "CYS":
                return "C";

            case "GLU":
                return "E";

            case "GLN":
                return "Q";

            case "GLY":
                return "G";

            case "HIS":
                return "H";

            case "ILE":
                return "I";

            case "LEU":
                return "L";

            case "LYS":
                return "K";

            case "MET":
                return "M";

            case "PHE":
                return "F";

            case "PRO":
                return "P";

            case "SER":
                return "S";

            case "THR":
                return "T";

            case "TRP":
                return "W";

            case "TYR":
                return "Y";

            case "VAL":
                return "V";

            default:
                return "";
        }
    }

    /**
     * @param aminoOLCode amino acid code in one letter format.
     * @return amino acid code in three letters format.
     */
    public static String aminoOnetoThree(String aminoOLCode) {
        String code = aminoOLCode.toUpperCase();

        switch (code) {
            case "A":
                return "ALA";

            case "R":
                return "ARG";

            case "D":
                return "ASP";

            case "N":
                return "ASN";

            case "C":
                return "CYS";

            case "E":
                return "GLU";

            case "Q":
                return "GLN";

            case "G":
                return "GLY";

            case "H":
                return "HIS";

            case "I":
                return "ILE";

            case "L":
                return "LEU";

            case "K":
                return "LYS";

            case "M":
                return "MET";

            case "F":
                return "PHE";

            case "P":
                return "PRO";

            case "S":
                return "SER";

            case "T":
                return "THR";

            case "W":
                return "TRP";

            case "Y":
                return "TYR";

            case "V":
                return "VAL";
            default:
                return "";
        }

    }

    /**
     * @param chars string to be evaluated.
     * @return if the string is numeric.
     */
    public static boolean isInteger(String chars) {

        boolean result = true;
        int length = chars.length();
        int pos = 0;

        while (pos < length && result) {
            result = Character.isDigit(chars.charAt(pos));
            pos++;
        }
        return result;

    }

    /**
     * @param aminoTLCode amino acid three letters code.
     * @return if the input string is an amino acid three letters code.
     */
    public static boolean isAminoSequence(String aminoTLCode) {

        String code = aminoTLCode.toUpperCase();

        switch (code) {
            case "ALA":
            case "ARG":
            case "ASP":
            case "ASN":
            case "CYS":
            case "GLU":
            case "GLN":
            case "GLY":
            case "HIS":
            case "ILE":
            case "LEU":
            case "LYS":
            case "MET":
            case "PHE":
            case "PRO":
            case "SER":
            case "THR":
            case "TRP":
            case "TYR":
            case "VAL":
                return true;
            default:
                return false;
        }
    }

    /**
     * @param path path to the configuration file.
     * @return properties object.
     * @throws IOException for problems while loading the file.
     */
    public static Properties loadProperties(String path) throws IOException {
        String msgioe = "%s does not exist...";

        /* properties file*/
        if (!BioUtils.checkFileExist(path)) {
            throw new IOException(String.format(msgioe, path));
        } else {
            Properties prop = new Properties();

            try (FileReader fr = new FileReader(path)) {
                prop.load(fr);
                return prop;
            }
        }
    }

    /**
     * @param indices indices of the selected properties encoded as 1,2,5,...
     * @return an array with the indices of the selected properties.
     */
    public static int[] getSelectedIndices(String indices) {

        String[] tokens = indices.split(SEPARATOR);
        return Arrays.stream(tokens).mapToInt(Integer::parseInt).toArray();
    }

    /**
     * *
     * Creates the inter CA distance matrix, for 3D indices...
     *
     * @param pdb the pdb file wrapper.
     * @return inter CA distance matrix with values as string.
     */
    public static String[][] getInterCADistMatrixN(PdbFile pdb) {

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
        String msge = "Interval %s specification is wrong, check it...";

        String regex = "([\\[\\(])(([0-9]+.[0-9]+),([0-9]+.[0-9]+))([\\]\\)])";

        Pattern r = Pattern.compile(regex);
        Matcher m = r.matcher(desc);

        if (m.find()) {
            String lo = m.group(1);
            String ll = m.group(3);
            String ul = m.group(4);
            String ro = m.group(5);

            double lld = Double.parseDouble(ll);
            double uld = Double.parseDouble(ul);

            boolean lcrc = lo.equals(IntervalTypeCodes.LCLOSED) && ro.equals(IntervalTypeCodes.RCLOSED);
            boolean lcro = lo.equals(IntervalTypeCodes.LCLOSED) && ro.equals(IntervalTypeCodes.ROPEN);
            boolean loro = lo.equals(IntervalTypeCodes.LOPEN) && ro.equals(IntervalTypeCodes.ROPEN);
            boolean lorc = lo.equals(IntervalTypeCodes.LOPEN) && ro.equals(IntervalTypeCodes.RCLOSED);

            IntervalType itype = IntervalType.LCLOSED_ROPEN;

            /* better implementation with nested if, but for readability and because if are exclusive */
            if (lcrc) {
                itype = IntervalType.LCLOSED_RCLOSED;
            }
            if (lcro) {
                itype = IntervalType.LCLOSED_ROPEN;
            }
            if (loro) {
                itype = IntervalType.LOPEN_ROPEN;
            }
            if (lorc) {
                itype = IntervalType.LOPEN_RCLOSED;
            }
            if (!(lcrc || lcro || lorc || loro)) {
                String msg = String.format(msge, desc);
                Logger.getLogger(BioUtils.class.getName()).log(Level.SEVERE, msg);
                return null;
            }

            return new Interval(lld, uld, itype);

        } else {
            String msg = String.format(msge, desc);
            Logger.getLogger(BioUtils.class.getName()).log(Level.SEVERE, msg);
            return null;
        }
    }

    /**
     * 
     * @param path the path to the file.
     * @return if the file exists.
     */
    public static boolean checkFileExist(String path) {
        File file = new File(path);
        return file.exists();
    }
}
