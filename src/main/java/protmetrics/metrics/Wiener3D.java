package protmetrics.metrics;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.FloydWarshallShortestPaths;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedPseudograph;
import protmetrics.dao.converters.XYZMOLConverter;
import protmetrics.dao.dm.DMAtt;
import protmetrics.dao.dm.DMAttValue;
import protmetrics.dao.dm.DMDataSet;
import protmetrics.dao.dm.DMInstance;
import protmetrics.dao.files.xyz.GAtom;
import protmetrics.dao.files.xyz.XYZFile;
import protmetrics.utils.BioUtils;
import protmetrics.utils.Formats;
import protmetrics.utils.MyMath;
import protmetrics.utils.Filter;
import protmetrics.dao.files.mol.MolFile;
import protmetrics.utils.filters.ExtAtomsFilter;

/**
 * Implements the Topological Autocorrelation Vectors molecular descriptor.
 * Path goes through the interior of the molecule.
 *
 * [1]Fernandez, M., Abreu, J. I., Shi, H., Barnard, A. S. (2016). Machine
 * learning prediction of the energy gap of graphene nanoflakes using
 * topological autocorrelation vectors. ACS combinatorial science, 18(11),
 * 661-664.
 *
 *
 */
public class Wiener3D {

    /**
     */
    public static final String INDEX_ID = "Wiener3D";

    /**
     * @param prop properties.
     * @return dataset representing the descriptor for each of the input files.
     * @throws Exception for problems while computing the descriptor.
     */
    public DMDataSet calcWiener3D(Properties prop) throws Exception {

        int attOrder = 0;
        DMDataSet ds = new DMDataSet(Wiener3D.INDEX_ID);
        DMAtt attPN = new DMAtt(DMAtt.getSPECIAL_ATT_NAME(), String.class, attOrder++);
        ds.addAtt(attPN);

        int maxPathL = (Integer) prop.get(Constants.MAX_DIST);
        int minPathL = (Integer) prop.get(Constants.MIN_DIST);

        boolean normalized = (Boolean) prop.get(Constants.GINDEX_NORMALIZED);
        boolean onlyExt = (Boolean) prop.get(Constants.GINDEX_ONLY_EXT);
        int extNodeTagL = (Integer) prop.get(Constants.GINDEX_EXT_ATOM_TAG);
        int intNodeTagL = (Integer) prop.get(Constants.GINDEX_INT_ATOM_TAG);

        int precision = (Integer) prop.get(Constants.PRECISION);

        ExtAtomsFilter extAtomFilter = (ExtAtomsFilter) prop.get(Constants.EXTERIOR_ATOM_FILTER);

        ArrayList<MolFile> protList = (ArrayList<MolFile>) prop.get(Constants.INSTANCES);

        /* for each PDB, compute the index */
        for (MolFile mol : protList) {
            Logger.getLogger(Wiener3D.class.getName()).log(Level.INFO, String.format("Computing 3D Wiener Index for %s", mol.getID()));
            DMInstance inst = new DMInstance(mol.getID());
            inst.setAttValue(attPN, new DMAttValue(inst.getInstID()));
            int currentPathL = minPathL;

            WeightedPseudograph<GAtom, DefaultWeightedEdge> wpsg = mol.getBonds();

            /* for efficiency, only one instance of this class must be created since it caches precomputed values */
            FloydWarshallShortestPaths<GAtom, DefaultWeightedEdge> fwsPath = new FloydWarshallShortestPaths<>(wpsg);

            while (currentPathL <= maxPathL) {
                /* compute the index (still a bit inefficient since path lengh are explored progressively) */
                DMAttValue r = this.getWienerThrough(wpsg, fwsPath, extAtomFilter, currentPathL, normalized, onlyExt, extNodeTagL, intNodeTagL, precision);

                String attName = "PL_" + Integer.toString(currentPathL);
                DMAtt att = new DMAtt(attName, Double.class, attOrder++);
                ds.addAtt(att);
                inst.setAttValue(att, r);

                currentPathL++;
            }

            ds.addInstance(inst);
            Logger.getLogger(Wiener3D.class.getName()).log(Level.INFO, String.format("Computing 3D Wiener Index for %s. Done!", mol.getID()));
        }

        return ds;
    }

    /**
     * @param wpsg the WeightedPseudograph representing the molecule.
     * @param fwsPath the algorithm to compute the path between atoms.
     * @param extAtomFilter the atom filter to be used.
     * @param pathL the max path lenght.
     * @param normalized if descriptor must be normalized by molecule size.
     * @param onlyExt if consider only exterior atoms.
     * @param extNodeTagL weight of exterior atoms.
     * @param intNodeTagL weight of internal atoms.
     * @param precision the precision when round values.
     * @return descriptor value for the given molecule.
     */
    public DMAttValue getWienerThrough(WeightedPseudograph<GAtom, DefaultWeightedEdge> wpsg,
            FloydWarshallShortestPaths<GAtom, DefaultWeightedEdge> fwsPath,
            ExtAtomsFilter extAtomFilter,
            int pathL,
            boolean normalized,
            boolean onlyExt,
            int extNodeTagL,
            int intNodeTagL,
            int precision) {

        Set<GAtom> extVertices = extAtomFilter.getExteriorVertices(wpsg);
        Set<GAtom> procVertices = wpsg.vertexSet();

        if (onlyExt) {
            procVertices = extVertices;
        }

        double count = 0;
        double L = 0;

        GAtom[] procVerticesA = new GAtom[procVertices.size()];
        procVerticesA = procVertices.toArray(procVerticesA);

        int pathLength;
        GraphPath<GAtom, DefaultWeightedEdge> path;
        for (int i = 0; i < procVerticesA.length - 1; ++i) {
            double iP = extVertices.contains(procVerticesA[i]) ? extNodeTagL : intNodeTagL;

            for (int j = i + 1; j < procVerticesA.length; ++j) {
                double jP = extVertices.contains(procVerticesA[j]) ? extNodeTagL : intNodeTagL;
                path = fwsPath.getPath(procVerticesA[i], procVerticesA[j]);

                if (path != null) {
                    /* the weights are supposed to be integers */
                    pathLength = (int) path.getWeight();
                    if (pathLength == pathL) {
                        count = count + (iP * jP);
                        L++;
                    }
                }
            }
        }

        if (normalized == true) {
            count = count / L;
        }

        return new DMAttValue(Double.toString(MyMath.round(count, precision)));
    }

    /**
     * @param path path to the configuration file.
     * @return Properties object encoding configuration.
     * @throws Exception for problems while loading the file.
     */
    public Properties init(String path) throws Exception {
        /* properties file */
        File cfgFile = new File(path);
        if (cfgFile.exists() == false) {
            throw new IOException(path + " does not exist...");
        }
        Properties prop = BioUtils.loadProperties(path);

        /* precision */
        if (!prop.containsKey(Constants.PRECISION)) {
            throw new IllegalArgumentException(Constants.PRECISION + " not specified for 3D Wiener...");
        } else {
            prop.put(Constants.PRECISION, Integer.parseInt(prop.getProperty(Constants.PRECISION)));
        }

        if (!prop.containsKey(Constants.BOND_DESC_FILE)) {
            throw new IllegalArgumentException(Constants.BOND_DESC_FILE + " not specified for 3D Wiener...");
        } else {
            String bdPath = prop.getProperty(Constants.BOND_DESC_FILE);
            File pmFile = new File(bdPath);
            if (!pmFile.exists()) {
                throw new IOException(pmFile + " does not exist...");
            }
        }

        if (!prop.containsKey(Wiener3D.Constants.LOAD_BOND_TYPE)) {
            throw new IllegalArgumentException(Wiener3D.Constants.LOAD_BOND_TYPE + "  not specified for 3D Wiener...");
        }

        if (!prop.containsKey(Constants.LOAD_XYZ_DEPLETED)) {
            throw new IllegalArgumentException(Constants.LOAD_XYZ_DEPLETED + " not specified for 3D Wiener...");
        } else {
            prop.put(Constants.LOAD_XYZ_DEPLETED, Boolean.parseBoolean(prop.getProperty(Constants.LOAD_XYZ_DEPLETED, "true")));
        }

        if (!prop.containsKey(Constants.BOND_DESC_FILE)) {
            throw new IllegalArgumentException(Constants.BOND_DESC_FILE + " not specified for 3D Wiener...");
        }

        /* XYZs folder */
        if (!prop.containsKey(Constants.XYZ_DIRECTORY_PATH)) {
            throw new IllegalArgumentException(Constants.XYZ_DIRECTORY_PATH + " input folder not specified for 3D Wiener...");
        } else {
            String xyzsPath = prop.getProperty(Constants.XYZ_DIRECTORY_PATH);
            File xyzFilesDir = new File(xyzsPath);
            if (xyzFilesDir.exists() == false) {
                throw new IOException(xyzsPath + " does not exist...");
            } else {
                Filter xyzFilter = new Filter("." + Formats.XYZ);
                File[] xyzFiles = xyzFilesDir.listFiles(xyzFilter);

                ArrayList<MolFile> protList = new ArrayList<>(xyzFiles.length);
                XYZMOLConverter conv = new XYZMOLConverter();
                for (File xyzFile : xyzFiles) {
                    boolean depleted = (Boolean) prop.get(Constants.LOAD_XYZ_DEPLETED);
                    XYZFile xyz = new XYZFile(xyzFile.getAbsolutePath(), depleted);
                    MolFile molFile = conv.convert(xyz, prop);
                    protList.add(molFile);
                }

                prop.put(Constants.INSTANCES, protList);
            }

        }

        /* output format */
        if (!prop.containsKey(Constants.OUTPUT_FORMAT)) {
            throw new IllegalArgumentException(Constants.OUTPUT_FORMAT + "  format not specified for 3D Wiener...");
        }

        /* min radius */
        if (!prop.containsKey(Constants.MIN_DIST)) {
            throw new IllegalArgumentException(Constants.MIN_DIST + " not specified for 3D Wiener...");
        } else {
            prop.put(Constants.MIN_DIST, Integer.parseInt(prop.getProperty(Constants.MIN_DIST)));
        }

        /* max radius */
        if (!prop.containsKey(Constants.MAX_DIST)) {
            throw new IllegalArgumentException(Constants.MAX_DIST + " not specified for 3D Wiener...");
        } else {
            prop.put(Constants.MAX_DIST, Integer.parseInt(prop.getProperty(Constants.MAX_DIST)));
        }

        if (!prop.containsKey(Constants.GINDEX_NORMALIZED)) {
            throw new IllegalArgumentException(Constants.GINDEX_NORMALIZED + " not specified for 3D Wiener...");
        } else {
            prop.put(Constants.GINDEX_NORMALIZED, Boolean.parseBoolean(prop.getProperty(Constants.GINDEX_NORMALIZED)));
        }

        if (!prop.containsKey(Constants.GINDEX_ONLY_EXT)) {
            throw new IllegalArgumentException(Constants.GINDEX_ONLY_EXT + " not specified for 3D Wiener...");
        } else {
            prop.put(Constants.GINDEX_ONLY_EXT, Boolean.parseBoolean(prop.getProperty(Constants.GINDEX_ONLY_EXT)));
        }

        if (!prop.containsKey(Constants.GINDEX_INT_ATOM_TAG)) {
            throw new IllegalArgumentException(Constants.GINDEX_INT_ATOM_TAG + " not specified for 3D Wiener...");
        } else {
            prop.put(Constants.GINDEX_INT_ATOM_TAG, Integer.parseInt(prop.getProperty(Constants.GINDEX_INT_ATOM_TAG)));
        }

        if (!prop.containsKey(Constants.GINDEX_EXT_ATOM_TAG)) {
            throw new IllegalArgumentException(Constants.GINDEX_EXT_ATOM_TAG + " not specified for 3D Wiener...");
        } else {
            prop.put(Constants.GINDEX_EXT_ATOM_TAG, Integer.parseInt(prop.getProperty(Constants.GINDEX_EXT_ATOM_TAG)));
        }

        if (!prop.containsKey(Constants.EXTERIOR_ATOM_FILTER)) {
            throw new IllegalArgumentException(Constants.EXTERIOR_ATOM_FILTER + " not specified for 3D Wiener...");
        } else {
            String extAtomFilterCN = prop.getProperty(Constants.EXTERIOR_ATOM_FILTER);
            Class<?> extAFC = Class.forName(extAtomFilterCN);
            ExtAtomsFilter extAtomFilter = (ExtAtomsFilter) extAFC.newInstance();
            prop.put(Constants.EXTERIOR_ATOM_FILTER, extAtomFilter);
        }

        if (!prop.containsKey(Constants.MAX_EDGES_BY_ATOM)) {
            throw new IllegalArgumentException(Constants.MAX_EDGES_BY_ATOM + " not specified for 3D Wiener...");
        } else {
            prop.put(Constants.MAX_EDGES_BY_ATOM, Double.parseDouble(prop.getProperty(Constants.MAX_EDGES_BY_ATOM)));
        }

        if (!prop.containsKey(Constants.MOL_TYPE)) {
            throw new IllegalArgumentException(Constants.MOL_TYPE + " not specified for 3D Wiener...");
        }

        return prop;
    }

    /**
     * @param args arguments for the call.
     */
    public static void main(String[] args) {
        Logger.getLogger(Wiener3D.class.getName()).log(Level.INFO, "Computing 3D Wiener Index...");
        try {
            Args a = new Args();
            JCommander.newBuilder()
                    .addObject(a)
                    .build()
                    .parse(args);

            if (a.cfgPath != null) {
                Wiener3D wtd = new Wiener3D();
                Properties prop = wtd.init(a.cfgPath);

                DMDataSet ds = wtd.calcWiener3D(prop);
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
            Logger.getLogger(Wiener3D.class.getName()).log(Level.SEVERE, null, ex);
        }
        Logger.getLogger(Wiener3D.class.getName()).log(Level.INFO, "Computing 3D Wiener Index. Done!");
    }

    /**
     * Contains constants required by this class.
     */
    public static class Constants {

        /**
         *
         */
        public static final int DEFAULT_PRECISION = 2;

        /**
         *
         */
        public static final String PRECISION = "PRECISION";

        /**
         *
         */
        public static final String XYZ_DIRECTORY_PATH = "XYZ_DIRECTORY_PATH";

        /**
         *
         */
        public static final String OUTPUT_FILE_PATH = "OUTPUT_FILE_PATH";

        /**
         *
         */
        public static final String OUTPUT_FORMAT = "OUTPUT_FORMAT";

        /**
         *
         */
        public static final String EXTERIOR_ATOM_FILTER = "EXTERIOR_ATOM_FILTER";

        /**
         *
         */
        public static final String BOND_DESC_FILE = "BOND_DESC_FILE";

        /**
         *
         */
        public static final String MAX_DIST = "MAX_DIST";

        /**
         *
         */
        public static final String MIN_DIST = "MIN_DIST";

        /**
         *
         */
        public static final String GINDEX_INT_ATOM_TAG = "GINDEX_INT_ATOM_TAG";

        /**
         *
         */
        public static final String GINDEX_EXT_ATOM_TAG = "GINDEX_EXT_ATOM_TAG";

        /**
         *
         */
        public static final String LOAD_XYZ_DEPLETED = "LOAD_XYZ_DEPLETED";

        /**
         *
         */
        public static final String GINDEX_ONLY_EXT = "GINDEX_ONLY_EXT";

        /**
         *
         */
        public static final String GINDEX_NORMALIZED = "GINDEX_NORMALIZED";

        /**
         *
         */
        public static final String LOAD_BOND_TYPE = "LOAD_BOND_TYPE";

        /**
         *
         */
        public static final String MOL_TYPE = "MOL_TYPE";

        /**
         *
         */
        public static final String V2K = "V2000";

        /**
         *
         */
        public static final String V3K = "V3000";

        /**
         *
         */
        public static final String MAX_EDGES_BY_ATOM = "MAX_EDGES_BY_ATOM";

        /**
         *
         */
        public static final double DEFAULT_EDGE_WEIGHT = 1;

        /**
         *
         */
        public static final String INSTANCES = "INSTANCES";
    }

    /**
     * Encodes interval codes.
     */
    public static class IntervalTypeCodes {

        /**
         *
         */
        public static final String LOPEN = "(";

        /**
         *
         */
        public static final String LCLOSED = "[";

        /**
         *
         */
        public static final String ROPEN = ")";

        /**
         *
         */
        public static final String RCLOSED = "]";
    }

    /**
     * Enum to encode molecule type.
     */
    public static enum MolType {

        /**
         *
         */
        V2000,

        /**
         *
         */
        V3000
    };

    private static class Args {

        @Parameter(names = {"--cfg", "-cfg"})
        String cfgPath = null;
    }
}
