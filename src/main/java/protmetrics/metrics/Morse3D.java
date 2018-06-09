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

public class Morse3D {

    public static final String INDEX_ID = "3DMorse";

    public DMDataSet calc3DMorseIndex(Properties p) throws Exception {

        int attOrder = 0;
        DMDataSet ds = new DMDataSet(Morse3D.INDEX_ID);
        DMAtt attPN = new DMAtt(DMAtt.getSPECIAL_ATT_NAME(), String.class, attOrder++);
        ds.addAtt(attPN);

        PropertyMatrix propMatrix = (PropertyMatrix) p.get(Constants.PROP_MATRIX);

        int maxDist = Integer.parseInt(p.getProperty(Constants.MAX_DIST));
        int minDist = Integer.parseInt(p.getProperty(Constants.MIN_DIST));
        int step = Integer.parseInt(p.getProperty(Constants.STEP));

        int stepDesp = (int) Math.round((maxDist - minDist) / step) + 1; //-> Para ver el rango de distancia.;

        ArrayList<ProtWrapper> protList = (ArrayList<ProtWrapper>) p.get(Constants.PROTEIN_LIST);

        //-> Para cada Pdb calcular el indice según los parámetros.
        for (ProtWrapper pw : protList) {
            DMInstance inst = new DMInstance(pw.getName());
            inst.setAttValue(attPN, new DMAttValue(inst.getInstID()));

            //-> Para cada propiedad, calcular los indices a distintos pasos
            for (PropertyVector pv : propMatrix.PropertyVectorsColumns) {
                double rstep = minDist;

                //-> Por cada paso en el rango.
                for (int j = 0; j < stepDesp; j++) {
                    DMAttValue r = this.get3DMorse(pv, pw.getInterCADistMatrix(), rstep);

                    String attName = pv.PropertyName + "_" + (double) rstep;
                    DMAtt att = new DMAtt(attName, Double.class, attOrder++);
                    ds.addAtt(att);
                    inst.setAttValue(att, r);

                    rstep = rstep + step;
                }
            }
            ds.addInstance(inst);
        }

        return ds;
    }

    public DMAttValue get3DMorse(PropertyVector pv, IEDMatrix interCAMatrix, double S) {

        boolean[] found = {true};
        double sum = 0;
        double factor;
        double coef;
        double pMult;
        double radius;
        double p1;
        double p2;

        for (int f = 2; f < interCAMatrix.getRows(); f++) {
            p1 = pv.getValueFromName(interCAMatrix.getElementAt(f), found);
            for (int c = 1; c < f; c++) {
                /* Af Aj e(-B(r-rij)) */
                p2 = pv.getValueFromName(interCAMatrix.getElementAt(c), found);

                pMult = p1 * p2;
                radius = interCAMatrix.getValueAt(f, c);
                coef = S * radius;
                factor = Math.sin(coef) / (coef);
                sum = sum + pMult * factor;
            }
        }

        return new DMAttValue(Double.toString(MyMath.Round(sum, 2)));
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
            Logger.getLogger(Morse3D.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

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

        public static final String DO_PRODUCT = "DO_PRODUCT";
        public static final String DO_MAX = "DO_MAX";
        public static final String DO_MIN = "DO_MIN";

    }

    static class Args {

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
