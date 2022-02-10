package protmetrics.metrics;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import protmetrics.dao.IEDMatrix;
import protmetrics.dao.dm.DMAtt;
import protmetrics.dao.dm.DMAttValue;
import protmetrics.dao.dm.DMDataSet;
import protmetrics.dao.dm.DMInstance;
import protmetrics.dao.files.pdb.PdbFile;
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
 * [1] Fernandez, M., Abreu, J. I., Caballero, J., Garriga, M., Fernandez,
 * len. (2007). Comparative modeling of the conformational stability of
 * chymotrypsin inhibitor 2 protein mutants using amino acid sequence
 * autocorrelation (AASA) and amino acid 3D autocorrelation (AA3DA) vectors and
 * ensembles of Bayesian-regularized genetic neural networks. Molecular
 * Simulation, 33(13), 1045-1056.
 *
 * [2] Wagener, M., Sadowski, J., Gasteiger, J. (1995). Autocorrelation of
 * molecular surface properties for modeling corticosteroid binding globulin and
 * cytosolic Ah receptor activity by neural networks. Journal of the American
 * Chemical Society, 117(29), 7769-7775.
 */
public class Correlation3D {

    /**
     */
    public static final String INDEX_ID = "AA3DA";

    /**
     * @param prop properties.
     * @return dataset representing the descriptor for each of the input files.
     * @throws Exception for problems while computing the descriptor.
     */
    public DMDataSet calc3DCorrelationIndex(Properties prop) throws Exception {

        int attOrder = 0;
        DMDataSet ds = new DMDataSet(Correlation3D.INDEX_ID);
        DMAtt attPN = new DMAtt(DMAtt.getSPECIAL_ATT_NAME(), String.class, attOrder++);
        ds.addAtt(attPN);

        PropertyMatrix propMatrix = (PropertyMatrix) prop.get(Constants.PROP_MATRIX);
        boolean doProduct = (Boolean) Boolean.parseBoolean(prop.getProperty(Constants.DO_PRODUCT));
        boolean doMax = (Boolean) Boolean.parseBoolean(prop.getProperty(Constants.DO_MAX));
        boolean doMin = (Boolean) Boolean.parseBoolean(prop.getProperty(Constants.DO_MIN));

        double maxDist = Double.parseDouble(prop.getProperty(Constants.MAX_DIST));
        double minDist = Double.parseDouble(prop.getProperty(Constants.MIN_DIST));
        double step = Double.parseDouble(prop.getProperty(Constants.STEP));

        double deltha = step / 2;

        ArrayList<ProtWrapper> protList = (ArrayList<ProtWrapper>) prop.get(Constants.PROTEIN_LIST);

        /* for each PDB, compute the index */
        for (ProtWrapper pw : protList) {
            Logger.getLogger(Correlation2D.class.getName()).log(Level.INFO, String.format("Computing 3D Correlation Index for %s", pw.name));
            DMInstance inst = new DMInstance(pw.getName());
            inst.setAttValue(attPN, new DMAttValue(inst.getInstID()));

            /* for each property, compute the indices at different steps */
            for (PropertyVector pv : propMatrix.getPropertyVectorsColumns()) {
                double currentDist = minDist;

                /* for each step, compute the indice */
                while (currentDist <= maxDist) {
                    double dLower = currentDist - deltha;
                    double dUpper = currentDist + deltha;
                    if (doProduct) {
                        /* compute the indice */
                        DMAttValue r = this.get3DProd(pv, pw.getInterCADistMatrix(), dLower, dUpper);

                        String attName = pv.PropertyName + "_[" + MyMath.round(dLower, 2) + " - " + MyMath.round(dUpper, 2) + "]";
                        DMAtt att = new DMAtt(attName, Double.class, attOrder++);
                        ds.addAtt(att);
                        inst.setAttValue(att, r);
                    }

                    if (doMax) {
                        /* compute the indice */
                        DMAttValue r = this.get3DMax(pv, pw.getInterCADistMatrix(), dLower, dUpper);

                        String attName = pv.PropertyName + "_Max_[" + MyMath.round(dLower, 2) + " - " + MyMath.round(dUpper, 2) + "]";
                        DMAtt att = new DMAtt(attName, Double.class, attOrder++);
                        ds.addAtt(att);
                        inst.setAttValue(att, r);
                    }

                    if (doMin) {
                        /* compute the indice */
                        DMAttValue r = this.get3DMin(pv, pw.getInterCADistMatrix(), dLower, dUpper);

                        String attName = pv.PropertyName + "_Min_[" + MyMath.round(dLower, 2) + " - " + MyMath.round(dUpper, 2) + "]";
                        DMAtt att = new DMAtt(attName, Double.class, attOrder++);
                        ds.addAtt(att);
                        inst.setAttValue(att, r);
                    }

                    currentDist = currentDist + step;
                }
            }
            ds.addInstance(inst);
            Logger.getLogger(Correlation2D.class.getName()).log(Level.INFO, String.format("Computing 3D Correlation Index for %s. Done!", pw.name));
        }
        return ds;
    }

    /**
     * Index as described in [1] (\geq for the upper distance)
     *
     * @param pv the property vector.
     * @param interCAMatrix inter CA matrix of the molecule.
     * @param dLower lower bound.
     * @param dUpper upper bound.
     * @return descriptor value for the given molecule.
     */
    public DMAttValue get3DProd(PropertyVector pv, IEDMatrix interCAMatrix, double dLower, double dUpper) {

        boolean[] found = {true};
        double pi;
        double pj;
        double dij;
        double sum = 0;
        int L = 0;

        for (int i = 1; i < interCAMatrix.getRows() - 1; ++i) {
            pi = pv.getValueFromName(interCAMatrix.getElementAt(i), found);
            for (int j = i + 1; j < interCAMatrix.getColumns(); ++j) {
                pj = pv.getValueFromName(interCAMatrix.getElementAt(j), found);
                dij = interCAMatrix.getValueAt(i, j);

                if ((dij > dLower) && (dij <= dUpper)) {
                    sum = sum + pi * pj;
                    L++;
                }
            }
        }
        double result = L > 0 ? sum / L : 0;
        return new DMAttValue(Double.toString(MyMath.round(result, 2)));
    }

    /**
     * @param pv the property vector.
     * @param interCAMatrix inter CA matrix of the molecule.
     * @param dLower lower bound.
     * @param dUpper upper bound.
     * @return descriptor value for the given sequence.
     */
    public DMAttValue get3DMax(PropertyVector pv, IEDMatrix interCAMatrix, double dLower, double dUpper) {

        boolean[] found = {true};
        double pi;
        double pj;
        double dij;
        double max = Double.MIN_VALUE;
        int L = 0;

        for (int i = 1; i < interCAMatrix.getRows() - 1; ++i) {
            pi = pv.getValueFromName(interCAMatrix.getElementAt(i), found);
            for (int j = i + 1; j < interCAMatrix.getColumns(); ++j) {
                pj = pv.getValueFromName(interCAMatrix.getElementAt(j), found);
                dij = interCAMatrix.getValueAt(i, j);

                if ((dij > dLower) && (dij <= dUpper)) {
                    max = Math.max(max, pi * pj);
                    L++;
                }
            }
        }

        double result = L != 0 ? MyMath.round(max, 2) : 0;
        return new DMAttValue(Double.toString(result));
    }

    /**
     * @param pv the property vector.
     * @param interCAMatrix inter CA matrix of the molecule.
     * @param dLower lower bound.
     * @param dUpper upper bound.
     * @return descriptor value for the given sequence.
     */
    public DMAttValue get3DMin(PropertyVector pv, IEDMatrix interCAMatrix, double dLower, double dUpper) {

        boolean[] found = {true};
        double pi;
        double pj;
        double dij;
        double min = Double.MAX_VALUE;
        int L = 0;

        for (int i = 1; i < interCAMatrix.getRows() - 1; ++i) {
            pi = pv.getValueFromName(interCAMatrix.getElementAt(i), found);
            for (int j = i + 1; j < interCAMatrix.getColumns(); ++j) {
                pj = pv.getValueFromName(interCAMatrix.getElementAt(j), found);
                dij = interCAMatrix.getValueAt(i, j);

                if ((dij > dLower) && (dij <= dUpper)) {
                    min = Math.min(min, pi * pj);
                    L++;
                }
            }
        }

        double result = L != 0 ? MyMath.round(min, 2) : 0;
        return new DMAttValue(Double.toString(result));
    }

    /**
     * @param path the path to the configuration file.
     * @return Properties object encoding configuration.
     * @throws Exception for problems while loading the file.
     */
    public Properties init(String path) throws Exception {
        /* properties file*/
        File cfgFile = new File(path);
        if (cfgFile.exists() == false) {
            throw new IOException(path + " does not exist...");
        }
        Properties prop = BioUtils.loadProperties(path);

        /* PDBs folder*/
        String pdbsPath = prop.getProperty(Constants.PDBS_DIRECTORY_PATH);
        File pdbsFile = new File(pdbsPath);
        if (pdbsFile.exists() == false) {
            throw new IOException(pdbsPath + " does not exist...");
        }

        File pdbFilesDir = new File(pdbsPath);
        Filter pdbFilter = new Filter("." + Formats.PDB);
        File[] pdbFiles = pdbFilesDir.listFiles(pdbFilter);

        ArrayList<ProtWrapper> protList = new ArrayList<>(pdbFiles.length);
        for (File pdbFile : pdbFiles) {
            PdbFile pdbC = new PdbFile(pdbFile.getAbsolutePath());
            String[][] interCADistMatrix = BioUtils.getInterCADistMatrixN(pdbC);
            IEDMatrix pdbInterDistMatrix = new IEDMatrix(interCADistMatrix);

            protList.add(new ProtWrapper(pdbC.getProteinName(), pdbInterDistMatrix));
        }

        prop.put(Constants.PROTEIN_LIST, protList);

        /* properties matrix path*/
        String pmPath = prop.getProperty(Constants.PROP_MATRIX_PATH);
        File pmFile = new File(pmPath);
        if (pmFile.exists() == false) {
            throw new IOException(pmPath + " does not exist...");
        }

        /* min radius*/
        if (!prop.containsKey(Constants.MIN_DIST)) {
            throw new IllegalArgumentException("Min Distance not specified for 3D Correlation...");
        }

        /* max radius*/
        if (!prop.containsKey(Constants.MAX_DIST)) {
            throw new IllegalArgumentException("Max Distance not specified for 3D Correlation...");
        }

        /* step*/
        if (!prop.containsKey(Constants.STEP)) {
            throw new IllegalArgumentException("Step not specified for 3D Correlation...");
        } else {
            double step = Double.parseDouble(prop.getProperty(Constants.STEP));
            if (step <= 0) {
                throw new IllegalArgumentException("Step mut be > 0 for 3D Correlation...");
            }
        }

        /* output format*/
        if (!prop.containsKey(Constants.OUTPUT_FORMAT)) {
            throw new IllegalArgumentException("Output format not specified for 3D Correlation...");
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
        Logger.getLogger(Correlation3D.class.getName()).log(Level.INFO, "Computing 3D Correlation Index...");
        try {
            Args a = new Args();
            JCommander.newBuilder()
                    .addObject(a)
                    .build()
                    .parse(args);

            if (a.cfgPath != null) {
                Correlation3D tdc = new Correlation3D();
                Properties prop = tdc.init(a.cfgPath);

                DMDataSet ds = tdc.calc3DCorrelationIndex(prop);
                String format = prop.getProperty(Constants.OUTPUT_FORMAT);
                String outFile = prop.getProperty(Constants.OUTPUT_FILE_PATH);
                switch (format) {
                    case Formats.ARFF:
                        ds.toARFF(outFile);
                        break;
                    case Formats.CSV:
                        ds.toCSV(outFile);
                        break;
                }
            } else {
                throw new IllegalArgumentException("Configuration file not specified... must supply -cfg option...");
            }
        } catch (Exception ex) {
            Logger.getLogger(Correlation3D.class.getName()).log(Level.SEVERE, null, ex);
        }
        Logger.getLogger(Correlation3D.class.getName()).log(Level.INFO, "Computing 3D Correlation Index. Done!");
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
        private final IEDMatrix interCADistMatrix;

        public ProtWrapper(String name, IEDMatrix interCADistMatrix) {
            this.name = name;
            this.interCADistMatrix = interCADistMatrix;
        }

        /**
         * @return the name.
         */
        public String getName() {
            return name;
        }

        /**
         * @return the interCADistMatrix.
         */
        public IEDMatrix getInterCADistMatrix() {
            return interCADistMatrix;
        }
    }
}
