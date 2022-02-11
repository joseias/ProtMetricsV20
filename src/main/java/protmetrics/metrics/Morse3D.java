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
import protmetrics.errors.SomeErrorException;
import protmetrics.utils.BioUtils;
import protmetrics.utils.Formats;
import protmetrics.utils.MyMath;
import protmetrics.utils.Filter;

/**
 * Implements a variation of the 3D Morse molecular descriptor considering only
 * alpha carbons.
 *
 * [1] Schuur, J. H., Selzer, P., Gasteiger, J. (1996). The coding of the
 * three-dimensional structure of molecules by molecular transforms and its
 * application to structure-spectra correlations and studies of biological
 * activity. Journal of Chemical Information and Computer Sciences, 36(2),
 * 334-344.
 */
public class Morse3D {

    /**
     */
    public static final String INDEX_ID = "3DMorse";

    /**
     * @param prop properties.
     * @return dataset representing the descriptor for each of the input files.
     */
    public DMDataSet calc3DMorseIndex(Properties prop) {
        String msg;
        int attOrder = 0;
        DMDataSet ds = new DMDataSet(Morse3D.INDEX_ID);
        DMAtt attPN = new DMAtt(DMAtt.getSName(), String.class.getSimpleName(), attOrder++);
        ds.addAtt(attPN);

        PropertyMatrix propMatrix = (PropertyMatrix) prop.get(Constants.PROP_MATRIX);

        double maxDist = Double.parseDouble(prop.getProperty(Constants.MAX_DIST));
        double minDist = Double.parseDouble(prop.getProperty(Constants.MIN_DIST));
        double step = Double.parseDouble(prop.getProperty(Constants.STEP));

        int stepDesp = (int) Math.round((maxDist - minDist) / step) + 1;
        /* if within the distance interval */

        ArrayList<ProtWrapper> protList = (ArrayList<ProtWrapper>) prop.get(Constants.PROTEIN_LIST);

        /* for each PDB, compute the index */
        for (ProtWrapper pw : protList) {
            msg = String.format("Computing 3D Morse Index for %s", pw.name);
            Logger.getLogger(Morse3D.class.getName()).log(Level.INFO, msg);
            DMInstance inst = new DMInstance(pw.getName());
            inst.setAttValue(attPN, new DMAttValue(inst.getInstID()));

            /* for each property, compute the indices at different steps */
            for (PropertyVector pv : propMatrix.getPropertyVectorsColumns()) {
                double rstep = minDist;

                /* for each step, compute the indice*/
                for (int j = 0; j < stepDesp; ++j) {
                    DMAttValue r = this.get3DMorse(pv, pw.getInterCADistMatrix(), rstep);

                    String attName = pv.getPropertyName() + "_" + (double) rstep;
                    DMAtt att = new DMAtt(attName, Double.class.getSimpleName(), attOrder++);
                    ds.addAtt(att);
                    inst.setAttValue(att, r);

                    rstep = rstep + step;
                }
            }
            ds.addInstance(inst);
            msg = String.format("Computing 3D Morse Index for %s. Done!", pw.name);
            Logger.getLogger(Morse3D.class.getName()).log(Level.INFO, msg);
        }

        return ds;
    }

    /**
     * @param pv the property vector.
     * @param interCAMatrix inter CA matrix of the molecule.
     * @param eS the eS parameter.
     * @return descriptor value for the given molecule.
     */
    public DMAttValue get3DMorse(PropertyVector pv, IEDMatrix interCAMatrix, double eS) {

        boolean[] found = {true};
        double sum = 0;
        double factor;
        double coef;
        double pMult;
        double radius;
        double p1;
        double p2;

        for (int f = 2; f < interCAMatrix.getRows(); ++f) {
            p1 = pv.getValueFromName(interCAMatrix.getElementAt(f), found);
            for (int c = 1; c < f; ++c) {
                p2 = pv.getValueFromName(interCAMatrix.getElementAt(c), found);

                pMult = p1 * p2;
                radius = interCAMatrix.getValueAt(f, c);
                coef = eS * radius;
                factor = Math.sin(coef) / (coef);
                sum = sum + pMult * factor;
            }
        }

        return new DMAttValue(Double.toString(MyMath.round(sum, 2)));
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
        if (!BioUtils.checkFileExist(pdbsPath)) {
            throw new IOException(String.format(msgioe, pdbsPath));
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
                throw new IllegalArgumentException("Step mut be > 0 for 3D MORSE...");
            }
        }

        /* output format */
        if (!prop.containsKey(Constants.OUTPUT_FORMAT)) {
            throw new IllegalArgumentException("Output format not specified for 3D MORSE...");
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
        Logger.getLogger(Morse3D.class.getName()).log(Level.INFO, "Computing 3D Morse Index...");
        try {
            Args a = new Args();
            JCommander.newBuilder()
                    .addObject(a)
                    .build()
                    .parse(args);

            if (a.cfgPath != null) {
                Morse3D tdc = new Morse3D();
                Properties prop = tdc.init(a.cfgPath);

                DMDataSet ds = tdc.calc3DMorseIndex(prop);
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
            Logger.getLogger(Morse3D.class.getName()).log(Level.SEVERE, null, ex);
        }
        Logger.getLogger(Morse3D.class.getName()).log(Level.INFO, "Computing 3D Morse Index. Done!");
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

        private Constants() {
        }

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
