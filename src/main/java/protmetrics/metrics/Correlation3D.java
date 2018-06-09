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
import protmetrics.dao.PdbClass;
import protmetrics.dao.PropertyMatrix;
import protmetrics.dao.PropertyVector;
import protmetrics.dao.dm.DMAtt;
import protmetrics.dao.dm.DMAttValue;
import protmetrics.dao.dm.DMDataSet;
import protmetrics.dao.dm.DMInstance;
import protmetrics.utils.BioUtils;
import protmetrics.utils.DataLoader;
import protmetrics.utils.Formats;
import protmetrics.utils.MyMath;
import protmetrics.utils.PdbFilter;

/**
 * *
 * [1] M. Wagener, J. Sadowski, J. Gasteiger. Autocorrelation of molecular
 * properties for modelling corticosteroid binding globulin and cytosolic ah
 * receptor activity by neural networks. J. Am. Chem. Soc., 117, 7769 (1995)
 *
 * @author Docente
 */
public class Correlation3D {

    public static final String INDEX_ID = "Correlation3D";

    public DMDataSet calc3DCorrelationIndex(Properties p) throws Exception {

        int attOrder = 0;
        DMDataSet ds = new DMDataSet(Correlation3D.INDEX_ID);
        DMAtt attPN = new DMAtt(DMAtt.getSPECIAL_ATT_NAME(), String.class, attOrder++);
        ds.addAtt(attPN);

        PropertyMatrix propMatrix = (PropertyMatrix) p.get(Constants.PROP_MATRIX);
        boolean doProduct = (Boolean) Boolean.parseBoolean(p.getProperty(Constants.DO_PRODUCT));
        boolean doMax = (Boolean) Boolean.parseBoolean(p.getProperty(Constants.DO_MAX));
        boolean doMin = (Boolean) Boolean.parseBoolean(p.getProperty(Constants.DO_MIN));

        double maxDist = Double.parseDouble(p.getProperty(Constants.MAX_DIST));
        double minDist = Double.parseDouble(p.getProperty(Constants.MIN_DIST));
        double step = Double.parseDouble(p.getProperty(Constants.STEP));

        double deltha = step / 2;

        ArrayList<ProtWrapper> protList = (ArrayList<ProtWrapper>) p.get(Constants.PROTEIN_LIST);
        //-> Para cada Pdb calcular el indice según los parámetros.
        for (ProtWrapper pw : protList) {
            DMInstance inst = new DMInstance(pw.getName());
            inst.setAttValue(attPN, new DMAttValue(inst.getInstID()));

            //-> Para cada propiedad, calcular los indices a distintos pasos
            for (PropertyVector pv : propMatrix.PropertyVectorsColumns) {
                double currentDist = minDist;

                /* Para cada paso, calcular el indice AutoCorrelacion 3D */
                while (currentDist <= maxDist) {
                    double dLower = currentDist - deltha;
                    double dUpper = currentDist + deltha;
                    if (doProduct) {
                        /* Calcular el indice */
                        DMAttValue r = this.get3DProd(pv, pw.getInterCADistMatrix(), dLower, dUpper);

                        String attName = pv.PropertyName + "_[" + MyMath.Round(dLower, 2) + " - " + MyMath.Round(dUpper, 2) + "]";
                        DMAtt att = new DMAtt(attName, Double.class, attOrder++);
                        ds.addAtt(att);
                        inst.setAttValue(att, r);
                    }

                    if (doMax) {
                        /* Calcular el indice */
                        DMAttValue r = this.get3DMax(pv, pw.getInterCADistMatrix(), dLower, dUpper);

                        String attName = pv.PropertyName + "_Max_[" + MyMath.Round(dLower, 2) + " - " + MyMath.Round(dUpper, 2) + "]";
                        DMAtt att = new DMAtt(attName, Double.class, attOrder++);
                        ds.addAtt(att);
                        inst.setAttValue(att, r);
                    }

                    if (doMin) {
                        /* Calcular el indice */
                        DMAttValue r = this.get3DMin(pv, pw.getInterCADistMatrix(), dLower, dUpper);

                        String attName = pv.PropertyName + "_Min_[" + MyMath.Round(dLower, 2) + " - " + MyMath.Round(dUpper, 2) + "]";
                        DMAtt att = new DMAtt(attName, Double.class, attOrder++);
                        ds.addAtt(att);
                        inst.setAttValue(att, r);
                    }

                    currentDist = currentDist + step;
                }
            }
            ds.addInstance(inst);
        }
        return ds;
    }

    /**
     * Index as described in [1] (\geq for the upper distance)
     *
     * @param pv
     * @param interCAMatrix
     * @param dLower
     * @param dUpper
     * @return
     */
    public DMAttValue get3DProd(PropertyVector pv, IEDMatrix interCAMatrix, double dLower, double dUpper) {

        boolean[] found = {true};
        double pi;
        double pj;
        double dij;
        double sum = 0;
        int L = 0;

        for (int i = 1; i < interCAMatrix.getRows()-1; i++) {
            pi = pv.getValueFromName(interCAMatrix.getElementAt(i), found);
            for (int j = i+1; j < interCAMatrix.getColumns(); j++) {
                pj = pv.getValueFromName(interCAMatrix.getElementAt(j), found);
                dij = interCAMatrix.getValueAt(i, j);

                if ((dij > dLower) && (dij <= dUpper)) {
                    sum = sum + pi * pj;
                    L++;
                }
            }
        }
        double result = L > 0 ? sum / L : 0;
        return new DMAttValue(Double.toString(MyMath.Round(result, 2)));
    }

    /***
     * 
     * @param pv
     * @param interCAMatrix
     * @param dLower
     * @param dUpper
     * @return 
     */
    public DMAttValue get3DMax(PropertyVector pv, IEDMatrix interCAMatrix, double dLower, double dUpper) {

        boolean[] found = {true};
        double pi;
        double pj;
        double dij;
        double max = Double.MIN_VALUE;
        int L = 0;

        for (int i = 1; i < interCAMatrix.getRows()-1; i++) {
            pi = pv.getValueFromName(interCAMatrix.getElementAt(i), found);
            for (int j = i+1; j < interCAMatrix.getColumns(); j++) {
                pj = pv.getValueFromName(interCAMatrix.getElementAt(j), found);
                dij = interCAMatrix.getValueAt(i, j);

                if ((dij > dLower) && (dij <= dUpper)) {
                    max = Math.max(max, pi * pj);
                    L++;
                }
            }
        }

        double result = L != 0 ? MyMath.Round(max, 2) : 0;
        return new DMAttValue(Double.toString(result));
    }

    /***
     * 
     * @param pv
     * @param interCAMatrix
     * @param dLower
     * @param dUpper
     * @return 
     */
    public DMAttValue get3DMin(PropertyVector pv, IEDMatrix interCAMatrix, double dLower, double dUpper) {

        boolean[] found = {true};
        double pi;
        double pj;
        double dij;
        double min = Double.MAX_VALUE;
        int L = 0;

        for (int i = 1; i < interCAMatrix.getRows()-1; i++) {
            pi = pv.getValueFromName(interCAMatrix.getElementAt(i), found);
            for (int j = i+1; j < interCAMatrix.getColumns(); j++) {
                pj = pv.getValueFromName(interCAMatrix.getElementAt(j), found);
                dij = interCAMatrix.getValueAt(i, j);

                if ((dij > dLower) && (dij <= dUpper)) {
                    min = Math.min(min, pi * pj);
                    L++;
                }
            }
        }

        double result = L != 0 ? MyMath.Round(min, 2) : 0;
        return new DMAttValue(Double.toString(result));
    }

    public Properties init(String cfgPath) throws Exception {
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

        File pdbFilesDir = new File(pdbsPath);
        PdbFilter pdbFilter = new PdbFilter("." + Formats.PDB);
        File[] pdbFiles = pdbFilesDir.listFiles(pdbFilter);

        ArrayList<ProtWrapper> protList = new ArrayList<>(pdbFiles.length);
        for (File pdbFile : pdbFiles) {
            PdbClass pdbC = new PdbClass(pdbFile.getAbsolutePath());
            String[][] interCADistMatrix = BioUtils.getInterCADistMatrixN(pdbC);
            IEDMatrix pdbInterDistMatrix = new IEDMatrix(interCADistMatrix);

            protList.add(new ProtWrapper(pdbC.getProteinName(), pdbInterDistMatrix));
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
            throw new IllegalArgumentException("Min Distance not specified for 3D Correlation...");
        }

        /*Max radius*/
        if (!p.containsKey(Constants.MAX_DIST)) {
            throw new IllegalArgumentException("Max Distance not specified for 3D Correlation...");
        }

        /*Step*/
        if (!p.containsKey(Constants.STEP)) {
            throw new IllegalArgumentException("Step not specified for 3D Correlation...");
        }
        else{
            double step = Double.parseDouble(p.getProperty(Constants.STEP));
            if(step<=0){
                throw new IllegalArgumentException("Step mut be > 0 for 3D Correlation...");
            }
        }

        /*Output formar*/
        if (!p.containsKey(Constants.OUTPUT_FORMAT)) {
            throw new IllegalArgumentException("Output format not specified for 3D Correlation...");
        }

        PropertyMatrix pmAll = DataLoader.loadPropertyMatrix(pmPath);
        int[] indices = BioUtils.getSelectedIndices(p.getProperty(Constants.SELECTED_INDICES));
        PropertyMatrix pm = pmAll.getSubPropertyMatrix(indices);
        p.put(Constants.PROP_MATRIX, pm);

        return p;
    }

    public static void main(String[] args) {
        try {
            Args a = new Args();
            JCommander.newBuilder()
                    .addObject(a)
                    .build()
                    .parse(args);

            if (a.cfgPath != null) {
                Correlation3D tdc = new Correlation3D();
                Properties p = tdc.init(a.cfgPath);

                DMDataSet ds = tdc.calc3DCorrelationIndex(p);
                String format = p.getProperty(Constants.OUTPUT_FORMAT);
                String outFile = p.getProperty(Constants.OUTPUT_FILE_PATH);
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
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @return the interCADistMatrix
         */
        public IEDMatrix getInterCADistMatrix() {
            return interCADistMatrix;
        }
    }
}
