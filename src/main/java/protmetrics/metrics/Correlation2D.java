package protmetrics.metrics;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import protmetrics.dao.dm.DMAtt;
import protmetrics.dao.dm.DMAttValue;
import protmetrics.dao.dm.DMDataSet;
import protmetrics.dao.dm.DMInstance;
import protmetrics.dao.files.fasta.FastaFile;
import protmetrics.dao.files.pdb.PdbFile;
import protmetrics.errors.SomeErrorException;
import protmetrics.utils.BioUtils;
import protmetrics.utils.Filter;
import protmetrics.utils.Formats;
import protmetrics.utils.MyMath;
import protmetrics.utils.propertymatrix.PropertyMatrix;
import protmetrics.utils.propertymatrix.PropertyVector;

/**
 * Implements the 2D Amino Acid Sequence Autocorrelation Vectors molecular
 * descriptor.
 *
 * [1] Caballero, J., Fernandez, L., Abreu, J. I., Fernández, M. (2006). Amino
 * Acid Sequence Autocorrelation vectors and ensembles of Bayesian-Regularized
 * Genetic Neural Networks for prediction of conformational stability of human
 * lysozyme mutants. Journal of Chemical Information and Modeling, 46(3),
 * 1255-1268.
 *
 * [2] Moreau, G. (1980). The autocorrelation of a topological structure: A new
 * molecular descriptor. Nouv. J. Chim, 1980, 4, 359-360.
 */
public class Correlation2D {

    /**
     */
    public static final String INDEX_ID = "AASA-2D";

    /**
     * @param prop properties.
     * @return dataset representing the descriptor for each of the input files.
     */
    public DMDataSet calc2DCorrelationIndex(Properties prop) {
        String msg;
        int attOrder = 0;
        DMDataSet ds = new DMDataSet(Correlation2D.INDEX_ID);
        DMAtt attPN = new DMAtt(DMAtt.getSName(), String.class.getSimpleName(), attOrder++);
        ds.addAtt(attPN);

        PropertyMatrix propMatrix = (PropertyMatrix) prop.get(Constants.PROP_MATRIX);
        boolean doProduct = Boolean.parseBoolean(prop.getProperty(Constants.DO_PRODUCT));
        boolean doMax = Boolean.parseBoolean(prop.getProperty(Constants.DO_MAX));
        boolean doMin = Boolean.parseBoolean(prop.getProperty(Constants.DO_MIN));

        int maxDist = Integer.parseInt(prop.getProperty(Constants.MAX_DIST));
        int minDist = Integer.parseInt(prop.getProperty(Constants.MIN_DIST));
        int step = Integer.parseInt(prop.getProperty(Constants.STEP));

        ArrayList<ProtWrapper> protList = (ArrayList<ProtWrapper>) prop.get(Constants.PROTEIN_LIST);

        for (ProtWrapper pw : protList) {
            msg = String.format("Computing 2D Correlation Index for %s", pw.name);
            Logger.getLogger(Correlation2D.class.getName()).log(Level.INFO, msg);
            DMInstance inst = new DMInstance(pw.getName());
            inst.setAttValue(attPN, new DMAttValue(inst.getInstID()));

            String seq = pw.getSequence();
            for (PropertyVector pv : propMatrix.getPropertyVectorsColumns()) {
                int currentDist = minDist;

                while (currentDist <= maxDist) {
                    if (doProduct) {
                        /* compute the index*/
                        DMAttValue r = this.get2DProd(pv, seq, currentDist);

                        String attName = pv.getPropertyName() + "_" + (double) currentDist;
                        DMAtt att = new DMAtt(attName, Double.class.getSimpleName(), attOrder++);
                        ds.addAtt(att);
                        inst.setAttValue(att, r);
                    }

                    if (doMax) {
                        /* compute the index*/
                        DMAttValue r = this.get2DMax(pv, seq, currentDist);

                        String attName = pv.getPropertyName() + "_Max_" + (double) currentDist;
                        DMAtt att = new DMAtt(attName, Double.class.getSimpleName(), attOrder++);
                        ds.addAtt(att);
                        inst.setAttValue(att, r);
                    }

                    if (doMin) {
                        /* compute the index*/
                        DMAttValue r = this.get2DMin(pv, seq, currentDist);

                        String attName = pv.getPropertyName() + "_Min_" + (double) currentDist;
                        DMAtt att = new DMAtt(attName, Double.class.getSimpleName(), attOrder++);
                        ds.addAtt(att);
                        inst.setAttValue(att, r);
                    }

                    currentDist = currentDist + step;
                }
            }
            ds.addInstance(inst);
            msg = String.format("Computing 2D Correlation Index for %s. Done", pw.name);
            Logger.getLogger(Correlation2D.class.getName()).log(Level.INFO, msg);
        }
        return ds;
    }

    /**
     * *
     * Compute the 2D Correlation Index for one property.
     *
     * @param pv the property vector.
     * @param seq the sequence.
     * @param lag the step.
     * @return descriptor value for the given molecule.
     */
    public DMAttValue get2DProd(PropertyVector pv, String seq, int lag) {

        boolean[] found = {true};
        double pi;
        double pj;
        double sum = 0;
        int eL = 0;

        for (int i = 0; i < seq.length(); ++i) {
            pi = pv.getValueFromName(seq.substring(i, i + 1), found);
            if (i + lag < seq.length()) {
                pj = pv.getValueFromName(seq.substring(i + lag, i + lag + 1), found);
                sum = sum + pi * pj;
                eL++;
            }
        }

        double result = eL > 0 ? sum / eL : 0;
        return new DMAttValue(Double.toString(MyMath.round(result, 2)));
    }

    /**
     * *
     * Compute the 2D Correlation Index Max for one property.
     *
     * @param pv the property vector.
     * @param seq the sequence.
     * @param lag the step.
     * @return descriptor value for the given sequence, property, and step.
     */
    public DMAttValue get2DMax(PropertyVector pv, String seq, int lag) {

        boolean[] found = {true};
        double pi;
        double pj;
        double max = Double.MIN_VALUE;
        int eL = 0;

        for (int i = 0; i < seq.length(); ++i) {
            pi = pv.getValueFromName(seq.substring(i, i + 1), found);
            if (i + lag < seq.length()) {
                pj = pv.getValueFromName(seq.substring(i + lag, i + lag + 1), found);
                max = Math.max(max, pi * pj);
                eL++;
            }
        }
        double result = eL != 0 ? MyMath.round(max, 2) : 0;
        return new DMAttValue(Double.toString(result));
    }

    /**
     * *
     * Compute the 2D Correlation Index Max for one property.
     *
     * @param pv the property vector.
     * @param seq the sequence.
     * @param lag the step.
     * @return descriptor value for the given sequence, property, and step.
     */
    public DMAttValue get2DMin(PropertyVector pv, String seq, int lag) {

        boolean[] found = {true};
        double pi;
        double pj;
        double min = Double.MAX_VALUE;
        int eL = 0;

        for (int i = 0; i < seq.length(); ++i) {
            pi = pv.getValueFromName(seq.substring(i, i + 1), found);
            if (i + lag < seq.length()) {
                pj = pv.getValueFromName(seq.substring(i + lag, i + lag + 1), found);
                min = Math.min(min, pi * pj);
                eL++;
            }
        }
        double result = eL != 0 ? MyMath.round(min, 2) : 0;
        return new DMAttValue(Double.toString(result));
    }

    /**
     * @param path the path to the configuration file.
     * @return Properties object encoding configuration.
     * @throws IOException for problems while loading the file.
     * @throws IllegalArgumentException for missing configuration options.
     * @throws SomeErrorException for other errors.
     */
    public Properties init(String path) throws IOException, IllegalArgumentException, SomeErrorException {
        String msgioe = "%s does not exist...";
        String msgcfg = "%s not specified for 3D Wiener...";

        Properties prop = BioUtils.loadProperties(path);

        /* PDBs folder */
        String pdbsPath = prop.getProperty(Constants.PDBS_DIRECTORY_PATH);
        File pdbsFile = new File(pdbsPath);
        if (!pdbsFile.exists()) {
            throw new IOException(String.format(msgioe, pdbsPath));
        }

        File pdbFilesDir = new File(pdbsPath);
        Filter pdbFilter = new Filter("." + Formats.PDB);
        File[] pdbFiles = pdbFilesDir.listFiles(pdbFilter);

        pdbFilter = new Filter("." + Formats.FASTA);
        File[] fastaFiles = pdbFilesDir.listFiles(pdbFilter);

        ArrayList<ProtWrapper> protList = new ArrayList<>(pdbFiles.length + fastaFiles.length);
        for (File pdbFile : pdbFiles) {
            PdbFile pdbC = new PdbFile(pdbFile.getAbsolutePath());
            protList.add(new ProtWrapper(pdbC.getProteinName(), pdbC.getSequence()));
        }

        for (File fastaFile : fastaFiles) {
            FastaFile fc = new FastaFile(fastaFile.getAbsolutePath());
            String[][] ns = fc.getSequences();
            for (String[] ns1 : ns) {
                protList.add(new ProtWrapper(ns1[0], ns1[1]));
            }
        }
        prop.put(Constants.PROTEIN_LIST, protList);

        /* properties matrix path */
        String pmPath = prop.getProperty(Constants.PROP_MATRIX_PATH);
        if (!BioUtils.checkFileExist(pmPath)) {
            throw new IOException(String.format(msgioe, pmPath));
        }

        /* min radius */
        if (!prop.containsKey(Constants.MIN_DIST)) {
            throw new IllegalArgumentException(String.format(msgcfg, Constants.MIN_DIST));
        }

        /* max radius */
        if (!prop.containsKey(Constants.MAX_DIST)) {
            throw new IllegalArgumentException(String.format(msgcfg, Constants.MAX_DIST));
        }

        /* step */
        if (!prop.containsKey(Constants.STEP)) {
            throw new IllegalArgumentException(String.format(msgcfg, Constants.STEP));
        } else {
            double step = Double.parseDouble(prop.getProperty(Constants.STEP));
            if (step <= 0) {
                throw new IllegalArgumentException("Step mut be > 0 for 2D Correlation...");
            }
        }

        /* output format */
        if (!prop.containsKey(Constants.OUTPUT_FORMAT)) {
            throw new IllegalArgumentException(String.format(msgcfg, Constants.OUTPUT_FORMAT));
        }

        PropertyMatrix pmAll = new PropertyMatrix(pmPath);
        int[] indices = BioUtils.getSelectedIndices(prop.getProperty(Constants.SELECTED_INDICES));
        PropertyMatrix pm = pmAll.getSubPropertyMatrix(indices);
        prop.put(Constants.PROP_MATRIX, pm);

        return prop;
    }

    /**
     * @param args arguments for the call.
     */
    public static void main(String[] args) {
        Logger.getLogger(Correlation2D.class.getName()).log(Level.INFO, "Computing 2D Correlation Index...");
        try {
            Args a = new Args();
            JCommander.newBuilder()
                    .addObject(a)
                    .build()
                    .parse(args);

            if (a.cfgPath != null) {
                Correlation2D tdc = new Correlation2D();
                Properties prop = tdc.init(a.cfgPath);

                DMDataSet ds = tdc.calc2DCorrelationIndex(prop);
                String format = prop.getProperty(Constants.OUTPUT_FORMAT);
                String outFile = prop.getProperty(Constants.OUTPUT_FILE_PATH);
                switch (format) {
                    case Formats.ARFF:
                        ds.toARFF(outFile);
                        break;
                    case Formats.CSV:
                        ds.toCSV(outFile);
                        break;
                    default:
                        break;
                }

            } else {
                throw new IllegalArgumentException("Configuration file not specified... must supply -cfg option...");
            }
        } catch (IOException | IllegalArgumentException | SomeErrorException ex) {
            Logger.getLogger(Correlation2D.class.getName()).log(Level.SEVERE, null, ex);
        }
        Logger.getLogger(Correlation2D.class.getName()).log(Level.INFO, "Computing 2D Correlation Index. Done!");
    }

    private static class Constants {

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

        public static final String DO_PRODUCT = "DO_PRODUCT";
        public static final String DO_MAX = "DO_MAX";
        public static final String DO_MIN = "DO_MIN";

    }

    private static class Args {

        @Parameter(names = {"--cfg", "-cfg"})
        String cfgPath = null;
    }

    private static class ProtWrapper {

        private final String name;
        private final String sequence;

        public ProtWrapper(String name, String sequence) {
            this.name = name;
            this.sequence = sequence;
        }

        /**
         * @return the name.
         */
        public String getName() {
            return name;
        }

        /**
         * @return the sequence.
         */
        public String getSequence() {
            return sequence;
        }
    }
}
