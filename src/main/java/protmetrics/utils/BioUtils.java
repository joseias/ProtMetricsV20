package protmetrics.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jgrapht.Graph;

import protmetrics.dao.AMutation;
import protmetrics.dao.files.XYZ.GAtom;
import protmetrics.dao.files.pdb.PdbAtomLine;
import protmetrics.dao.files.pdb.PdbFile;
import protmetrics.dao.intervals.Interval;
import protmetrics.dao.intervals.Interval.IntervalType;
import protmetrics.errors.SomeErrorException;
import protmetrics.metrics.Wiener3D.IntervalTypeCodes;

/// <summary>
/// Summary description for BioUtils.
/// </summary>
public class BioUtils {

    public static final String SEPARATOR = ",";

    public BioUtils() {
        //
        // TODO: add constructor logic here
        //
    }

    /// <summary>
    /// Ver este metodo con mas calma, parece que al picar una linea con split mete alguna cacharra
    /// </summary>
    /// <param name="a_input"></param>
    /// <returns></returns>
    public static String[] ProcSplitString(String[] a_input) {
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
    }//public String[] ProcSplitString(String a_input)

    public static void CreateInterCADistanceFile(double[][] a_CADistanceData, String a_FileName) {

        try {

            //System.IO.StreamWriter m_sw=new System.IO.StreamWriter(a_FileName);
            PrintStream m_sw = new PrintStream(a_FileName);
            int m_maxlength = 0;
            String m_Line;

            //-> Ver el numero mas grande que se va a representar, esto podria calcularse
            //en el metodo CreateInterCADistanceMatrix
            for (int i = 1; i < a_CADistanceData.length; i++) {
                for (int j = i; j < a_CADistanceData[0].length; j++) {
                    if (Double.toString(a_CADistanceData[i][j]).length() > m_maxlength) {
                        m_maxlength = Double.toString(a_CADistanceData[i][j]).length();
                    }//if(a_CADistanceData[i,j].toString().length>m_maxlength)
                }//for(int j=1;j<a_CADistanceData.Getlength(0);j++ )
            }//for(int i=1;i<a_CADistanceData.Getlength(0);i++)

            for (int k = 1; k < a_CADistanceData.length; k++) {
                m_Line = " ";
                for (int h = 1; h < a_CADistanceData[0].length; h++) {
                    m_Line = m_Line + BioUtils.FormatNumber(Double.toString(a_CADistanceData[h][k]), m_maxlength - Double.toString(a_CADistanceData[h][k]).length()) + " ";
                }//or(int h=1;h<a_CADistanceData.Getlength(1);h++)
                m_sw.println(m_Line);
            }//for(int k=1;k<a_CADistanceData.Getlength(0);k++)
            m_sw.close();
        } catch (IOException m_ioe) {
            m_ioe.printStackTrace();
        } catch (Exception m_e) {
            m_e.printStackTrace();
        }
    }//public void CreateInterCADistanceFile(double[] a_CADistanceData,String a_FileName)

    public static void CreateInterCASDistanceFile(String[][] a_CADistanceData, String a_FileName) {

        //System.IO.StreamWriter m_sw=new System.IO.StreamWriter(a_FileName);
        try {

            PrintStream m_sw = new PrintStream(a_FileName);
            int m_maxlength = 0;
            String m_Line;

            //-> Ver el numero mas grande que se va a representar, esto podria calcularse
            //en el metodo CreateInterCADistanceMatrix
            for (int i = 0; i < a_CADistanceData.length; i++) {
                for (int j = i; j < a_CADistanceData[0].length; j++) {
                    if (a_CADistanceData[i][j].length() > m_maxlength) {
                        m_maxlength = a_CADistanceData[i][j].length();
                    }
                    //if(a_CADistanceData[i,j].toString().length>m_maxlength)
                }
                //for(int j=1;j<a_CADistanceData.Getlength(0);j++ )
            }//for(int i=1;i<a_CADistanceData.Getlength(0);i++)

            for (int k = 0; k < a_CADistanceData.length; k++) {
                m_Line = " ";
                for (int h = 0; h < a_CADistanceData[0].length; h++) {
                    m_Line = m_Line + BioUtils.FormatNumber(a_CADistanceData[h][k], m_maxlength - a_CADistanceData[h][k].length()) + "  ";
                }//or(int h=1;h<a_CADistanceData.Getlength(1);h++)
                m_sw.println(m_Line);
            }//for(int k=1;k<a_CADistanceData.Getlength(0);k++)
            m_sw.close();

        } catch (IOException m_ioe) {
            m_ioe.printStackTrace();
        } catch (Exception m_e) {
            m_e.printStackTrace();
        }

    }//public void CreateInterCADistanceFile(double[] a_CADistanceData,String a_FileName)

    public static String FormatNumber(String a_word, int a_whiteSpaces) {
        String m_result = a_word;
        for (int i = 0; i < a_whiteSpaces; i++) {
            m_result = " " + m_result;
        }//for(int i=0;i<a_whiteSpaces;i++)
        return m_result;
    }//public String FormatNumber(String a_word,int a_whiteSpaces)

    public static void CreateCAFile(String a_pdbFullPath) {
        //System.IO.FileInfo m_fi=new System.IO.FileInfo(a_pdbFullPath);
        try {

            File m_fi = new File(a_pdbFullPath);
            PrintStream m_sw = new PrintStream(m_fi.getParent() + "\\CAExtractedFrom_" + m_fi.getName() + ".txt");
            LineNumberReader m_sr = new LineNumberReader(new FileReader(a_pdbFullPath));
            //System.IO.StreamReader m_sr=new System.IO.StreamReader(a_pdbFullPath);
            //System.IO.StreamWriter m_sw=new System.IO.StreamWriter(m_fi.DirectoryName+"\\CAExtractedFrom_"+m_fi.Name+".txt");

            m_sw.println("Carbonos alfas encontrados en " + m_fi.getName() + " y correspondiente aminoacido.");

            String m_actualLine = m_sr.readLine();
            //boolean m_lineFound=false;
            String[] m_tokens;
            char[] m_sep = {' '};
            int m_cca = 1; //-> Cat
            //-> Buscar la linea donde empiezan las coordenadas de la molecula.
            while (m_actualLine != null) {

                if (m_actualLine != null) {
                    m_tokens = BioUtils.ProcSplitString(m_actualLine.trim().split("[\\s]+", 0));
                    if (m_tokens.length != 0) {
                        if (m_tokens[0].equals("ATOM")) {
                            if (m_tokens[2].equals("CA")) {

                                m_sw.println(m_cca + " " + m_tokens[2] + " " + m_tokens[3] + " " + m_tokens[6] + " " + m_tokens[7] + " " + m_tokens[8]);
                                m_cca++;
                            }//if(m_tokens[2].equals("CA"))
                        }//if(m_tokens[0].equals("ATOM"))
                    }//if(m_tokens.length!=0)

                }//if(m_actualLine!=null)
                m_actualLine = m_sr.readLine();
            }//while(m_sr.CanRead!=true)
            m_sr.close();
            m_sw.close();
        } catch (FileNotFoundException m_fnfe) {
            m_fnfe.printStackTrace();
        } catch (IOException m_ioe) {
            m_ioe.printStackTrace();
        } catch (Exception m_e) {
            m_e.printStackTrace();
        }
    }//public void CreateCAFile(String a_pdbFullPath)

    public static void PrintMatrix(String a_fileDirPath, String a_fileName, Object[][] a_matrix) {

        int m_maxString = 0;
        //System.IO.StreamWriter m_sw=new System.IO.StreamWriter(a_fileDirPath+"\\"+a_fileName);
        try {

            PrintStream m_sw = new PrintStream(a_fileDirPath + "\\" + a_fileName);
            String m_actualLine;

            m_maxString = BioUtils.MatrixMaxlength(a_matrix);
            for (int f = 0; f < a_matrix.length; f++) {
                m_actualLine = "";
                for (int c = 0; c < a_matrix[0].length; c++) {
                    m_actualLine = m_actualLine + " " + BioUtils.FormatNumber(a_matrix[f][c].toString(), m_maxString - a_matrix[f][c].toString().length());
                }
                m_sw.println(m_actualLine);
            }
            m_sw.close();
        } catch (IOException m_ioe) {
            m_ioe.printStackTrace();
        } catch (Exception m_e) {
            m_e.printStackTrace();
        }

    }//public static void PrintMatrix(String a_fileDirPath,String a_fileName,Object[,]a_matrix)

    public static void PrintMatrix(String a_filePath, Object[][] a_matrix) {

        int m_maxString = 0;
        //System.IO.StreamWriter m_sw=new System.IO.StreamWriter(a_fileDirPath+"\\"+a_fileName);
        try {

            PrintStream m_sw = new PrintStream(a_filePath + ".bif");
            String m_actualLine;

            m_maxString = BioUtils.MatrixMaxlength(a_matrix);
            for (int f = 0; f < a_matrix.length; f++) {
                m_actualLine = "";
                for (int c = 0; c < a_matrix[0].length; c++) {
                    m_actualLine = m_actualLine + " " + BioUtils.FormatNumber(a_matrix[f][c].toString(), m_maxString - a_matrix[f][c].toString().length());
                }
                m_sw.println(m_actualLine);
            }
            m_sw.close();
        } catch (IOException m_ioe) {
            m_ioe.printStackTrace();
        } catch (Exception m_e) {
            m_e.printStackTrace();
        }

    }//public static void PrintMatrix(String a_fileDirPath,String a_fileName,Object[,]a_matrix)

    public static void PrintMatrixArff(String a_fileDirPath, String a_fileName, String a_relationName, Object[][] a_matrix) {
        /*Imprime la matrix de objetos, se asume que es resultado de alguno de los indices, sino
            puede producirse errores.*/
        int m_rows = a_matrix.length;
        int m_columns = a_matrix[0].length;

        try {
            PrintStream m_ps = new PrintStream(a_fileDirPath + "\\" + a_fileName + ".arff");
            String m_actualLine = "";

            //-> Imprimir el encabezado
            m_ps.println("@relation " + a_relationName);
            m_ps.println("@attribute ProteinName string");

            for (int c = 1; c < m_columns; c++) {
                m_ps.println("@attribute " + a_matrix[1][c].toString() + " real");
            }
            m_ps.println(" ");
            m_ps.println("@data");
            for (int r = 2; r < m_rows; r++) {
                m_actualLine = m_actualLine + a_matrix[r][0];
                for (int c = 1; c < m_columns; c++) {
                    m_actualLine = m_actualLine + "," + a_matrix[r][c];
                }
                m_ps.println(m_actualLine);
                m_actualLine = "";
            }
            m_ps.close();
        } catch (IOException m_ioe) {
            m_ioe.printStackTrace();
        } catch (Exception m_e) {
            m_e.printStackTrace();
        }
    }

    public static void PrintMatrixArff(String a_filePath, String a_relationName, Object[][] a_matrix) {
        /*Imprime la matrix de objetos, se asume que es resultado de alguno de los indices, sino
            puede producirse errores.*/
        int m_rows = a_matrix.length;
        int m_columns = a_matrix[0].length;

        try {
            PrintStream m_ps = new PrintStream(a_filePath + ".arff");
            String m_actualLine = "";

            //-> Imprimir el encabezado
            m_ps.println("@relation " + a_relationName);
            m_ps.println("@attribute ProteinName string");

            for (int c = 1; c < m_columns; c++) {
                m_ps.println("@attribute " + a_matrix[1][c].toString() + " real");
            }
            m_ps.println(" ");
            m_ps.println("@data");
            for (int r = 2; r < m_rows; r++) {
                m_actualLine = m_actualLine + a_matrix[r][0];
                for (int c = 1; c < m_columns; c++) {
                    m_actualLine = m_actualLine + "," + a_matrix[r][c];
                }
                m_ps.println(m_actualLine);
                m_actualLine = "";
            }
            m_ps.close();
        } catch (IOException m_ioe) {
            m_ioe.printStackTrace();
        } catch (Exception m_e) {
            m_e.printStackTrace();
        }
    }

    public static void PrintMatrixCSV(String a_fileDirPath, String a_fileName, Object[][] a_matrix) {
        /*Imprime la matrix de objetos, se asume que es resultado de alguno de los indices, sino
            puede producirse errores.*/
        int m_rows = a_matrix.length;
        int m_columns = a_matrix[0].length;

        try {
            PrintStream m_ps = new PrintStream(a_fileDirPath + "\\" + a_fileName + ".csv");
            String m_actualLine = "ProteinName,";

            for (int c = 1; c < m_columns; c++) {
                m_actualLine = m_actualLine + a_matrix[1][c].toString() + ",";
            }
            m_ps.println(m_actualLine.substring(0, m_actualLine.length() - 1));

            for (int r = 2; r < m_rows; r++) {
                m_actualLine = "";
                m_actualLine = m_actualLine + a_matrix[r][0];
                for (int c = 1; c < m_columns; c++) {
                    m_actualLine = m_actualLine + "," + a_matrix[r][c];
                }
                m_ps.println(m_actualLine);
                m_actualLine = "";
            }
            m_ps.close();
        } catch (IOException m_ioe) {
            m_ioe.printStackTrace();
        } catch (Exception m_e) {
            m_e.printStackTrace();
        }
    }

    public static void PrintMatrixCSV(String a_filePath, Object[][] a_matrix) {
        /*Imprime la matrix de objetos, se asume que es resultado de alguno de los indices, sino
            puede producirse errores.*/
        int m_rows = a_matrix.length;
        int m_columns = a_matrix[0].length;

        try {
            PrintStream m_ps = new PrintStream(a_filePath + ".csv");
            String m_actualLine = "ProteinName,";

            for (int c = 1; c < m_columns; c++) {
                m_actualLine = m_actualLine + a_matrix[1][c].toString() + ",";
            }
            m_ps.println(m_actualLine.substring(0, m_actualLine.length() - 1));

            for (int r = 2; r < m_rows; r++) {
                m_actualLine = "";
                m_actualLine = m_actualLine + a_matrix[r][0];
                for (int c = 1; c < m_columns; c++) {
                    m_actualLine = m_actualLine + "," + a_matrix[r][c];
                }
                m_ps.println(m_actualLine);
                m_actualLine = "";
            }
            m_ps.close();
        } catch (IOException m_ioe) {
            m_ioe.printStackTrace();
        } catch (Exception m_e) {
            m_e.printStackTrace();
        }
    }

    public static int MatrixMaxlength(Object[][] a_matrix) {

        int m_result = 0;

        for (int f = 0; f < a_matrix.length; f++) {
            for (int c = 0; c < a_matrix[0].length; c++) {
                if (a_matrix[f][c].toString().length() > m_result) {
                    m_result = a_matrix[f][c].toString().length();
                }
            }
        }
        return m_result;

    }//public static int MatrixMaxlength(Object[,]a_matrix)

    public static AMutation[] LoadMutations(String a_mFilePath) throws SomeErrorException {
        AMutation[] m_result = null;
        //System.IO.StreamReader m_reader=new System.IO.StreamReader(a_mFilePath);
        try {

            LineNumberReader m_reader = new LineNumberReader(new FileReader(a_mFilePath));
            ArrayList m_mutations = new ArrayList();
            String m_actualLine = m_reader.readLine();
            AMutation m_mut;

            while (m_actualLine != null) {
                //Por el momento m_mut.Sequence siempre es "A"
                if (!m_actualLine.equals("")) {
                    try {
                        m_mut = new AMutation(BioUtils.AminoOnetoThree(m_actualLine.substring(0, 1)), m_actualLine.substring(1, m_actualLine.length() - 1), BioUtils.AminoOnetoThree(m_actualLine.substring(m_actualLine.length() - 1)), "A");
                    } catch (Exception m_e) {
                        throw new SomeErrorException("ERROR AT->" + a_mFilePath);
                    }
                    //m_mut.OriginalAmino=BioUtils.AminoOnetoThree(m_actualLine.substring(0,1));
//				m_mut.MutationPosition=Integer.parseInt(m_actualLine.substring(1,m_actualLine.length-2));
//				m_mut.NewAmino=BioUtils.AminoOnetoThree(m_actualLine.substring(m_actualLine.length-1,1));

                    m_mutations.add(m_mut);
                    m_actualLine = m_reader.readLine();
                }
                else {
                    m_actualLine = m_reader.readLine();
                }
            }
            m_result = new AMutation[m_mutations.size()];
            for (int i = 0; i < m_result.length; i++) {
                m_result[i] = (AMutation) m_mutations.get(i);
            }
        }//try
        catch (FileNotFoundException m_fnfe) {
            m_fnfe.printStackTrace();
        } catch (IOException m_ioe) {
            m_ioe.printStackTrace();
        } catch (Exception m_e) {
            //throw new SomeErrorException();
            //m_e.printStackTrace();
        }
        return m_result;
    }//public static AMutation[] LoadMutations(String a_mFilePath)

    public static String AminoThreetoOne(String a_aminoTLCode) {

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

    public static String AminoOnetoThree(String a_aminoOLCode) {

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

    public static boolean IsInteger(String a_chars) {

        boolean m_result = true;
        int m_length = a_chars.length();
        int m_pos = 0;

        while (m_pos < m_length && m_result == true) {
            m_result = Character.isDigit(a_chars.charAt(m_pos));
            m_pos++;
        }
        return m_result;

    }//public boolean IsInteger(String a_chars)

    public static boolean IsAminoSequence(String a_aminoTLCode) {

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
    }//public static boolean IsAminoSequence(String a_sequence)

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

    public static boolean isValid(String path) {
        File file = new File(path);
        return file.exists();
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
            result[f + 1][0] = result[0][f + 1] = (f + 1) + "_" + caData[f].GetAtomType() + "_" + caData[f].GetAminoType();

        }
        for (int i = 0; i < caData.length; i++) {
            for (int j = i; j < caData.length; j++) {
                x = Math.pow(caData[i].GetLocation().getX() - caData[j].GetLocation().getX(), 2);
                y = Math.pow(caData[i].GetLocation().getY() - caData[j].GetLocation().getY(), 2);
                z = Math.pow(caData[i].GetLocation().getZ() - caData[j].GetLocation().getZ(), 2);

                result[i + 1][j + 1] = result[j + 1][i + 1] = Double.toString(MyMath.round(Math.sqrt(x + y + z), 4));
            }
        }
        return result;
    }

    private static int precision = -1;

    public static <V, E> List<V> graphToList(Graph<V, E> graph) {
        Set<V> vertices = graph.vertexSet();
        List<V> result = new ArrayList<>(vertices.size());

        for (V v : vertices) {
            result.add(v);
        }

        return result;
    }

    public static <E> String printVector(E[] elements) {
        StringBuilder buffer = new StringBuilder();
        for (E element : elements) {
            buffer.append(element.toString()).append(",");
        }

        return buffer.substring(0, buffer.length() - 1);
    }

    public static String printVector(double[] elements) {
        StringBuilder buffer = new StringBuilder();
        for (double element : elements) {
            buffer.append(Double.toString(element)).append(",");
        }

        return buffer.substring(0, buffer.length() - 1);
    }

    public static double getDistance(GAtom a, GAtom b) {
        return a.getLocation().distance(b.getLocation());
    }

    public static double round(double value, int precision) {
        if (precision < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(precision, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static Properties getPropertiesFromArgs(String[] args) {
        Properties result = new Properties();

        for (int i = 0; i < args.length; i = i + 2) {
            if ('-' == args[i].charAt(0)) {
                result.put(args[i], args[i + 1]);
            }
        }

        return result;
    }

    /**
     * Applies a regex to verify if the string match with a interval
     * specification
     *
     * @param target
     * @return
     */
    public static boolean matchIntExp(String target) {
        String regex = "([\\[]|[\\(])[0-9]+.[0-9]+,[0-9]+.[0-9]+([\\]]|[\\)])";
        return target.matches(regex);
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

        }
        else {
            System.err.println("Interval " + desc + " specification is wrong, check it...");
            return null;
        }
    }

}
