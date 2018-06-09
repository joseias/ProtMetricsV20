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
import protmetrics.dao.dm.DMAttValue;
import protmetrics.dao.dm.DMAtt;
import protmetrics.dao.dm.DMDataSet;
import protmetrics.dao.dm.DMInstance;
import protmetrics.utils.Formats;

/**
 * [1] G. Moreau, P. Broto. Autocorrelation of a topological structure: 
 * A new molecular descriptor. 
 * Nouv. J. Chim., 4, 359 (1980).

 * @author personal
 */
public class Correlation2D {

    public static final String INDEX_ID = "Correlation2D";

    public DMDataSet calc2DCorrelationIndex(Properties p) throws Exception {

        int attOrder = 0;
        DMDataSet ds = new DMDataSet(Correlation2D.INDEX_ID);
        DMAtt attPN = new DMAtt(DMAtt.getSPECIAL_ATT_NAME(), String.class, attOrder++);
        ds.addAtt(attPN);

        PropertyMatrix propMatrix = (PropertyMatrix) p.get(Constants.PROP_MATRIX);
        boolean doProduct = (Boolean) Boolean.parseBoolean(p.getProperty(Constants.DO_PRODUCT));
        boolean doMax = (Boolean) Boolean.parseBoolean(p.getProperty(Constants.DO_MAX));
        boolean doMin = (Boolean) Boolean.parseBoolean(p.getProperty(Constants.DO_MIN));

        int maxDist = Integer.parseInt(p.getProperty(Constants.MAX_DIST));
        int minDist = Integer.parseInt(p.getProperty(Constants.MIN_DIST));
        int step = Integer.parseInt(p.getProperty(Constants.STEP));

        ArrayList<ProtWrapper> protList = (ArrayList<ProtWrapper>) p.get(Constants.PROTEIN_LIST);

        for (ProtWrapper pw : protList) {
            DMInstance inst = new DMInstance(pw.getName());
            inst.setAttValue(attPN, new DMAttValue(inst.getInstID()));

            String seq = pw.getSequence();
            for (PropertyVector pv : propMatrix.PropertyVectorsColumns) {
                int currentDist = minDist;
                
                while (currentDist <= maxDist) {
                    if (doProduct) {
                        /* Calcular el indice */
                        DMAttValue r = this.get2DProd(pv, seq, currentDist);

                        String attName = pv.PropertyName + "_" + (double) currentDist;
                        DMAtt att = new DMAtt(attName, Double.class, attOrder++);
                        ds.addAtt(att);
                        inst.setAttValue(att, r);
                    }

                    if (doMax) {
                        /* Calcular el indice */
                        DMAttValue r = this.get2DMax(pv, seq, currentDist);

                        String attName = pv.PropertyName + "_Max_" + (double) currentDist;
                        DMAtt att = new DMAtt(attName, Double.class, attOrder++);
                        ds.addAtt(att);
                        inst.setAttValue(att, r);
                    }

                    if (doMin) {
                        /* Calcular el indice */
                        DMAttValue r = this.get2DMin(pv, seq, currentDist);

                        String attName = pv.PropertyName + "_Min_" + (double) currentDist;
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
     * *
     * Compute the 2D Correlation Index for one property.
     *
     * @param pv
     * @param seq
     * @param lag
     * @return
     */
    public DMAttValue get2DProd(PropertyVector pv, String seq, int lag) {

        boolean[] found = {true};
        double pi;
        double pj;
        double sum = 0;        
        int L = 0;

        for (int i = 0; i < seq.length(); i++) {
            pi = pv.getValueFromName(seq.substring(i, i + 1), found);
            if (i + lag < seq.length()) {
                pj = pv.getValueFromName(seq.substring(i + lag, i + lag + 1), found);
                sum = sum + pi * pj;
                L++;
            }
        }

        double result = L > 0 ? sum / L : 0;
        return new DMAttValue(Double.toString(MyMath.Round(result, 2)));
    }

    /**
     * *
     * Compute the 2D Correlation Index Max for one property.
     *
     * @param pv
     * @param seq
     * @param lag
     * @return
     */
    public DMAttValue get2DMax(PropertyVector pv, String seq, int lag) {

        boolean[] found = {true};
        double pi;
        double pj;
        double max = Double.MIN_VALUE;        
        int L = 0;

        for (int i = 0; i < seq.length(); i++) {
            pi = pv.getValueFromName(seq.substring(i, i + 1), found);
            if (i + lag < seq.length()) {
                pj = pv.getValueFromName(seq.substring(i + lag, i + lag + 1), found);
                max = Math.max(max, pi * pj);
                L++;
            }
        }
        double result = L != 0 ? MyMath.Round(max, 2) : 0;
        return new DMAttValue(Double.toString(result));
    }

    /**
     * *
     * Compute the 2D Correlation Index Max for one property.
     *
     * @param pv
     * @param seq
     * @param lag
     * @return
     */
    public DMAttValue get2DMin(PropertyVector pv, String seq, int lag) {

        boolean[] found = {true};
        double pi;
        double pj;
        double min = Double.MAX_VALUE;        
        int L = 0;

        for (int i = 0; i < seq.length(); i++) {
            pi = pv.getValueFromName(seq.substring(i, i + 1), found);
            if (i + lag < seq.length()) {
                pj = pv.getValueFromName(seq.substring(i + lag, i + lag + 1), found);
                min = Math.min(min, pi * pj);
                L++;
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

        pdbFilter = new PdbFilter("." + Formats.FASTA);
        File[] fastaFiles = pdbFilesDir.listFiles(pdbFilter);

        ArrayList<ProtWrapper> protList = new ArrayList<>(pdbFiles.length + fastaFiles.length);
        for (File pdbFile : pdbFiles) {
            PdbClass pdbC = new PdbClass(pdbFile.getAbsolutePath());
            protList.add(new ProtWrapper(pdbC.getProteinName(), pdbC.getSequence()));
        }

        for (File fastaFile : fastaFiles) {
            FastaClass fc = new FastaClass(fastaFile.getAbsolutePath());
            String[][] ns = fc.getSequences();
            for (String[] ns1 : ns) {
                protList.add(new ProtWrapper(ns1[0], ns1[1]));
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
        else{
            double step = Double.parseDouble(p.getProperty(Constants.STEP));
            if(step<=0){
                throw new IllegalArgumentException("Step mut be > 0 for 2D Correlation...");
            }
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

                DMDataSet ds = tdc.calc2DCorrelationIndex(p);
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
            Logger.getLogger(Correlation2D.class.getName()).log(Level.SEVERE, null, ex);
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
        private final String sequence;

        public ProtWrapper(String name, String sequence) {
            this.name = name;
            this.sequence = sequence;
        }

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @return the sequence
         */
        public String getSequence() {
            return sequence;
        }
    }
}
