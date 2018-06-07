/*
 * Correlation2D.java
 *
 * Created on 19 de junio de 2007, 12:44
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package protmetrics.metrics;

import com.beust.jcommander.JCommander;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import protmetrics.dao.FastaClass;
import protmetrics.dao.PdbClass;
import protmetrics.dao.PropertyMatrix;
import protmetrics.dao.PropertyVector;
import protmetrics.utils.BioUtils;
import protmetrics.utils.DataLoader;
import protmetrics.utils.MyMath;
import protmetrics.utils.PdbFilter;
import com.beust.jcommander.Parameter;
import java.util.ArrayList;
import protmetrics.dao.ProtWrapper;
import protmetrics.utils.Formats;

/**
 *
 * @author personal
 */
public class Correlation2D {

    public static class Constants {

        public static final String PROPERTY_VECTOR = "PROPERTY_VECTOR";
        public static final String PROP_MATRIX = "PROP_MATRIX";

        public static final String SEQUENCE = "SEQUENCE";
        public static final String STEP = "STEP";
        public static final String MAX_DIST = "MAX_DIST";
        public static final String MIN_DIST = "MIN_DIST";
        public static final String SELECTED_INDICES = "SELECTED_INDICES";

        public static final String PROP_MATRIX_PATH = "PROP_MATRIX_PATH";
        public static final String PDBS_DIRECTORY_PATH = "PDBS_DIRECTORY_PATH";
        public static final String OUTPUT_FILE_PATH = "OUTPUT_FILE_PATH";
        public static final String OUTPUT_FORMAT = "OUTPUT_FORMAT";

        public static final String PROTEIN_LIST = "PROTEIN_LIST";

        //String a_filesPath, PropertyMatrix a_propMatrix, String a_outputPath, int a_MaxDist, int a_MinDist, int a_step
    }

    static class Args {

        @Parameter(names = {"--cfg", "-cfg"})
        String cfgPath = null;
    }

    /***
     * Compute the 2D Correlation Index for one property.
     * @param pv
     * @param seq
     * @param step
     * @return 
     */
    public static double get2DCorrelationIndex(PropertyVector pv, String seq, int step) {

        int m_cantSumandos = 0;

        boolean[] m_found = {true};
        double m_result = 0;
        double m_P1;
        double m_P2;

        for (int i = 0; i < seq.length(); i++) {
            m_P1 = pv.getValueFromName(seq.substring(i, i + 1), m_found);
            if (i + step < seq.length()) {
                m_P2 = pv.getValueFromName(seq.substring(i + step, i + step + 1), m_found);
                m_result = m_result + m_P1 * m_P2;
                m_cantSumandos++;
            }
        }
        m_result = m_result / m_cantSumandos;
        return MyMath.Round(m_result, 2);
    }

    public String[][] calc2DCorrelationIndex(Properties p) throws Exception {

        String[][] result = null;
        try {
            PropertyMatrix propMatrix = (PropertyMatrix) p.get(Constants.PROP_MATRIX);
            int maxDist = Integer.parseInt(p.getProperty(Constants.MAX_DIST));
            int minDist = Integer.parseInt(p.getProperty(Constants.MIN_DIST));
            int step = Integer.parseInt(p.getProperty(Constants.STEP));

            int stepDesp = (int) Math.round((maxDist - minDist) / step) + 1;

            ArrayList<ProtWrapper> protList = (ArrayList<ProtWrapper>) p.get(Constants.PROTEIN_LIST);

            result = BioUtils.formatRDFIndexResultMatrix(propMatrix, maxDist, minDist, step, protList.size());

            for (int t = 0; t < protList.size(); t++) {
                result[t + 2][0] = protList.get(t).getName();
                String seq = protList.get(t).getSequence();
                for (int propv = 0; propv < propMatrix.PropertyVectorsColumns.length; propv++) {
                    int rstep = minDist;
                    PropertyVector pv = propMatrix.PropertyVectorsColumns[propv];
                    for (int j = 0; j < stepDesp; j++) {
                        //-> Calcular el indice.                       
                        double r = Correlation2D.get2DCorrelationIndex(pv, seq, rstep);

                        result[t + 2][j + propv * stepDesp + 1] = Double.toString(r);
                        rstep = rstep + step;
                    }
                }
            }
        } catch (NumberFormatException m_e) {
            throw m_e;
        }
        return result;
    }

    public Properties init(String cfgPath) {
        try {

            /*Properties file*/
            File cfgFile = new File(cfgPath);
            if (cfgFile.exists() == false) {
                throw new IOException(cfgPath + " does not exist...");
            }
            Properties p = BioUtils.loadProperties(cfgPath);

            /*PDBs directory*/
            String pdbsPath = p.getProperty(Constants.PDBS_DIRECTORY_PATH);
            File pdbsFile = new File(pdbsPath);
            if (pdbsFile.exists() == false) {
                throw new IOException(pdbsPath + " does not exist...");
            }

            File m_pdbFilesDir = new File(pdbsPath);
            PdbFilter m_pdbFilter = new PdbFilter("." + Formats.PDB);
            File[] pdbFiles = m_pdbFilesDir.listFiles(m_pdbFilter);

            m_pdbFilter = new PdbFilter("." + Formats.FASTA);
            File[] fastaFiles = m_pdbFilesDir.listFiles(m_pdbFilter);

            ArrayList<ProtWrapper> protList = new ArrayList<>(pdbFiles.length + fastaFiles.length);
            for (File pdbFile : pdbFiles) {
                PdbClass pdbC = new PdbClass(pdbFile.getAbsolutePath());
                protList.add(new ProtWrapper(pdbC.getProteinName(), pdbC.getSequence()));
            }

            for (File fastaFile : fastaFiles) {
                FastaClass m_fc = new FastaClass(fastaFile.getAbsolutePath());
                String[][] m_NS = m_fc.getSequences();
                for (String[] m_NS1 : m_NS) {
                    protList.add(new ProtWrapper(m_NS1[0], m_NS1[1]));
                }
            }
            p.put(Constants.PROTEIN_LIST, protList);

            /*Properties matrix path*/
            String pmPath = p.getProperty(Constants.PROP_MATRIX_PATH);
            File pmFile = new File(pmPath);
            if (pmFile.exists() == false) {
                throw new IOException(pmPath + " does not exist...");
            }

            /*Min radius*/
            if (!p.containsKey(Constants.MIN_DIST)) {
                throw new IllegalArgumentException("Min Distance not specified for 2D Correlation...");
            }

            /*Max radius*/
            if (!p.containsKey(Constants.MAX_DIST)) {
                throw new IllegalArgumentException("Max Distance not specified for 2D Correlation...");
            }

            /*Step*/
            if (!p.containsKey(Constants.STEP)) {
                throw new IllegalArgumentException("Step not specified for 2D Correlation...");
            }

            /*Output formar*/
            if (!p.containsKey(Constants.OUTPUT_FORMAT)) {
                throw new IllegalArgumentException("Output format not specified for 2D Correlation...");
            }

            PropertyMatrix pmAll = DataLoader.loadPropertyMatrix(pmPath);
            int[] indices = BioUtils.getSelectedIndices(p.getProperty(Constants.SELECTED_INDICES));
            PropertyMatrix pm = pmAll.getSubPropertyMatrix(indices);
            p.put(Constants.PROP_MATRIX, pm);

            return p;
        } catch (Exception ex) {
            Logger.getLogger(Correlation2D.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static void main(String[] args) {
        try {
            Args a = new Args();
            JCommander.newBuilder()
                    .addObject(a)
                    .build()
                    .parse(args);

            if (a.cfgPath != null) {
                Correlation2D tdc = new Correlation2D();
                Properties p = tdc.init(a.cfgPath);

                String[][] m_result = tdc.calc2DCorrelationIndex(p);
                String format = p.getProperty(Constants.OUTPUT_FORMAT);
                String outFile = p.getProperty(Constants.OUTPUT_FILE_PATH);
                switch (format) {
                    case Formats.ARFF:
                        BioUtils.PrintMatrixArff(outFile, "2D", m_result);
                        break;
                    case Formats.CSV:
                        BioUtils.PrintMatrixCSV(outFile, m_result);
                        break;
                    case Formats.TXT:
                        BioUtils.PrintMatrix(outFile, m_result);
                        break;
                }
            } else {
                throw new IllegalArgumentException("Configuration file not specified... must supply -cfg option...");
            }
        } catch (Exception ex) {
            Logger.getLogger(Correlation2D.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
