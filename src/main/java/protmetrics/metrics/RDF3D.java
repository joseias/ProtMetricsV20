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
import protmetrics.dao.files.pdb.PdbFile;
import protmetrics.utils.propertymatrix.PropertyMatrix;
import protmetrics.utils.propertymatrix.PropertyVector;
import protmetrics.dao.dm.DMAtt;
import protmetrics.dao.dm.DMAttValue;
import protmetrics.dao.dm.DMDataSet;
import protmetrics.dao.dm.DMInstance;
import protmetrics.utils.BioUtils;
import protmetrics.utils.Formats;
import protmetrics.utils.MyMath;
import protmetrics.utils.Filter;

public class RDF3D {
    public static final String INDEX_ID = "RDF";
    
    public DMDataSet calcRDFIndex(Properties p) throws Exception {

        int attOrder = 0;
        DMDataSet ds = new DMDataSet(INDEX_ID);
        DMAtt attPN = new DMAtt(DMAtt.getSPECIAL_ATT_NAME(), String.class, attOrder++);
        ds.addAtt(attPN);

        PropertyMatrix propMatrix = (PropertyMatrix) p.get(Constants.PROP_MATRIX);

        double maxDist = Double.parseDouble(p.getProperty(Constants.MAX_DIST));
        double minDist = Double.parseDouble(p.getProperty(Constants.MIN_DIST));
        double step = Double.parseDouble(p.getProperty(Constants.STEP));
        double rdfB = Double.parseDouble(p.getProperty(Constants.RDF_B));
        double rdfF = Double.parseDouble(p.getProperty(Constants.RDF_F));
        
        
        int stepDesp = (int) Math.round((maxDist - minDist) / step) + 1; //-> Para ver el rango de distancia.;

        ArrayList<ProtWrapper> protList = (ArrayList<ProtWrapper>) p.get(Constants.PROTEIN_LIST);

        //-> Para cada Pdb calcular el indice según los parámetros.
        for (ProtWrapper pw : protList) {
            DMInstance inst = new DMInstance(pw.getName());
            inst.setAttValue(attPN, new DMAttValue(inst.getInstID()));

            //-> Para cada propiedad, calcular los indices a distintos pasos
            for (PropertyVector pv : propMatrix.getPropertyVectorsColumns()) {
                double rstep = minDist;

                //-> Por cada paso en el rango.
                for (int j = 0; j < stepDesp; j++) {
                    DMAttValue r = this.getRDF(pv, pw.getInterCADistMatrix(), rstep, rdfF, rdfB);

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

    public DMAttValue getRDF(PropertyVector pv, IEDMatrix interCAMatrix, double radius, double rdfF, double rdfB) {

        boolean[] m_found = {true};
        double rdfIndexSum = 0;
        double exp;
        double pMult;

        for (int f = 1; f < interCAMatrix.getRows() - 1; f++) {
            for (int c = f + 1; c < interCAMatrix.getColumns(); c++) {
                /* Af Aj e(-B(r-rij)) */
                pMult = pv.getValueFromName(interCAMatrix.getElementAt(f), m_found) * pv.getValueFromName(interCAMatrix.getElementAt(c), m_found);

                exp = Math.exp(-rdfB * (Math.pow(radius - interCAMatrix.getValueAt(f, c), 2)));

                rdfIndexSum = rdfIndexSum + rdfF * pMult * exp;
            }
        }
        return new DMAttValue(Double.toString(MyMath.round(rdfIndexSum, 2)));
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
        Filter pdbFilter = new Filter("." + Formats.PDB);
        File[] pdbFiles = pdbFilesDir.listFiles(pdbFilter);

        ArrayList<ProtWrapper> protList = new ArrayList<>(pdbFiles.length);
        for (File pdbFile : pdbFiles) {
            PdbFile pdbC = new PdbFile(pdbFile.getAbsolutePath());
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
            throw new IllegalArgumentException("Min Distance not specified for RDF...");
        }

        /*Max radius*/
        if (!p.containsKey(Constants.MAX_DIST)) {
            throw new IllegalArgumentException("Max Distance not specified for RDF...");
        }

        /*Step*/
        if (!p.containsKey(Constants.STEP)) {
            throw new IllegalArgumentException("Step not specified for RDF...");
        } else {
            double step = Double.parseDouble(p.getProperty(Constants.STEP));
            if (step <= 0) {
                throw new IllegalArgumentException("Step mut be > 0 for RDF...");
            }
        }

        /*Max rdfF*/
        if (!p.containsKey(Constants.RDF_F)) {
            throw new IllegalArgumentException("RDF F not specified for RDF...");
        }
        
        /*Max rdfB*/
        if (!p.containsKey(Constants.RDF_B)) {
            throw new IllegalArgumentException("RDF B not specified for RDF...");
        }
        
        /*Output formar*/
        if (!p.containsKey(Constants.OUTPUT_FORMAT)) {
            throw new IllegalArgumentException("Output format not specified for RDF...");
        }

        PropertyMatrix pmAll = new PropertyMatrix(pmPath);
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
                RDF3D rdf = new RDF3D();
                Properties p = rdf.init(a.cfgPath);

                DMDataSet ds = rdf.calcRDFIndex(p);
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
            Logger.getLogger(RDF3D.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static class Constants {

        public static final String PROPERTY_VECTOR = "PROPERTY_VECTOR";
        public static final String PROP_MATRIX = "PROP_MATRIX";

        public static final String SEQUENCE = "SEQUENCE";
        public static final String STEP = "STEP";
        public static final String MAX_DIST = "MAX_DIST";
        public static final String MIN_DIST = "MIN_DIST";
        public static final String RDF_F = "RDF_F";
        public static final String RDF_B = "RDF_B";
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
