package protmetrics.metrics;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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
import protmetrics.errors.SomeErrorException;
import protmetrics.utils.filters.ExtAtomsFilter;

/**
 * Implements the Topological Autocorrelation Vectors molecular descriptor. Path
 * goes through the interior of the molecule.
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
     * @return the data set representing the descriptor for each of the input files.
     */
    public DMDataSet calcWiener3D(Properties prop) {
        String msg;
        int attOrder = 0;
        DMDataSet ds = new DMDataSet(Wiener3D.INDEX_ID);
        DMAtt attPN = new DMAtt(DMAtt.getSName(), String.class.getSimpleName(), attOrder++);
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
            msg = String.format("Computing 3D Wiener Index for %s", mol.getID());
            Logger.getLogger(Wiener3D.class.getName()).log(Level.INFO, msg);
            DMInstance inst = new DMInstance(mol.getID());
            inst.setAttValue(attPN, new DMAttValue(inst.getInstID()));
            int currentPathL = minPathL;

            WeightedPseudograph<GAtom, DefaultWeightedEdge> wpsg = mol.getBonds();

            /* for efficiency, only one instance of this class must be created since it caches precomputed values */
            FloydWarshallShortestPaths<GAtom, DefaultWeightedEdge> fwsPath = new FloydWarshallShortestPaths<>(wpsg);

            while (currentPathL <= maxPathL) {
                /* compute the index (still aa bit inefficient since path lengh are explored progressively) */
                WienerArguments wa = new WienerArguments(currentPathL, normalized, onlyExt, extNodeTagL, intNodeTagL, precision);
                DMAttValue res = this.getWienerThrough(wpsg, fwsPath, extAtomFilter, wa);

                String attName = "PL_" + Integer.toString(currentPathL);
                DMAtt att = new DMAtt(attName, Double.class.getSimpleName(), attOrder++);
                ds.addAtt(att);
                inst.setAttValue(att, res);

                currentPathL++;
            }

            ds.addInstance(inst);
            msg = String.format("Computing 3D Wiener Index for %s. Done!", mol.getID());
            Logger.getLogger(Wiener3D.class.getName()).log(Level.INFO, msg);
        }

        return ds;
    }

    /**
     * @param wpsg the WeightedPseudograph representing the molecule.
     * @param fwsPath the algorithm to compute the path between atoms.
     * @param extAtomFilter the atom filter to be used.
     * @param wa other arguments
     * @return descriptor value for the given molecule.
     */
    public DMAttValue getWienerThrough(WeightedPseudograph<GAtom, DefaultWeightedEdge> wpsg,
            FloydWarshallShortestPaths<GAtom, DefaultWeightedEdge> fwsPath,
            ExtAtomsFilter extAtomFilter, WienerArguments wa
    ) {

        Set<GAtom> extVertices = extAtomFilter.getExteriorVertices(wpsg);
        Set<GAtom> procVertices = wpsg.vertexSet();

        if (wa.isOnlyExt()) {
            procVertices = extVertices;
        }

        double count = 0;
        double eL = 1;

        GAtom[] procVerticesA = new GAtom[procVertices.size()];
        procVerticesA = procVertices.toArray(procVerticesA);

        int pathLength;
        GraphPath<GAtom, DefaultWeightedEdge> path;
        for (int i = 0; i < procVerticesA.length - 1; ++i) {
            double iP = extVertices.contains(procVerticesA[i]) ? wa.getExtNodeTagL() : wa.getIntNodeTagL();

            for (int j = i + 1; j < procVerticesA.length; ++j) {
                double jP = extVertices.contains(procVerticesA[j]) ? wa.getExtNodeTagL() : wa.getIntNodeTagL();
                path = fwsPath.getPath(procVerticesA[i], procVerticesA[j]);

                if (path != null) {
                    /* the weights are supposed to be integers */
                    pathLength = (int) path.getWeight();
                    if (pathLength == wa.getPathL()) {
                        count = count + (iP * jP);
                        eL++;
                    }
                }
            }
        }

        if (wa.isNormalized()) {
            count = count / eL;
        }

        return new DMAttValue(Double.toString(MyMath.round(count, wa.getPrecision())));
    }

    /**
     * @param path the path to the configuration file.
     * @return Properties object encoding configuration.
     * @throws IOException for problems while loading the file.
     * @throws IllegalArgumentException for missing configuration options.
     * @throws ClassNotFoundException for errors while loading the atom filter.
     * @throws InstantiationException for errors while loading the atom filter.
     * @throws IllegalAccessException for errors while loading the atom filter.
     * @throws NoSuchMethodException for errors while loading the atom filter.
     * @throws InvocationTargetException for errors while loading the atom
     * filter.
     * @throws SomeErrorException for other errors.
     *
     */
    public Properties init(String path) throws IOException, IllegalArgumentException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, SomeErrorException {
        String msgioe = "%s does not exist...";
        String msgcfg = "%s not specified for 3D Wiener...";

        Properties prop = BioUtils.loadProperties(path);

        /* precision */
        if (!prop.containsKey(Constants.PRECISION)) {
            throw new IllegalArgumentException(Constants.PRECISION + " ");
        } else {
            prop.put(Constants.PRECISION, Integer.parseInt(prop.getProperty(Constants.PRECISION)));
        }

        if (!prop.containsKey(Constants.BOND_DESC_FILE)) {
            throw new IllegalArgumentException(String.format(msgcfg, Constants.BOND_DESC_FILE));
        } else {
            String bdPath = prop.getProperty(Constants.BOND_DESC_FILE);
            if (!BioUtils.checkFileExist(bdPath)) {
                throw new IOException(String.format(msgioe, bdPath));
            }
        }

        if (!prop.containsKey(Wiener3D.Constants.LOAD_BOND_TYPE)) {
            throw new IllegalArgumentException(String.format(msgcfg, Constants.LOAD_BOND_TYPE));
        }

        if (!prop.containsKey(Constants.LOAD_XYZ_DEPLETED)) {
            throw new IllegalArgumentException(String.format(msgcfg, Constants.LOAD_XYZ_DEPLETED));
        } else {
            prop.put(Constants.LOAD_XYZ_DEPLETED, Boolean.parseBoolean(prop.getProperty(Constants.LOAD_XYZ_DEPLETED, "true")));
        }

        /* XYZs folder */
        if (!prop.containsKey(Constants.XYZ_DIRECTORY_PATH)) {
            throw new IllegalArgumentException(String.format(msgcfg, Constants.XYZ_DIRECTORY_PATH));
        } else {
            String xyzsPath = prop.getProperty(Constants.XYZ_DIRECTORY_PATH);
            File xyzFilesDir = new File(xyzsPath);
            if (!xyzFilesDir.exists()) {
                throw new IOException(String.format(msgioe, xyzsPath));
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
            throw new IllegalArgumentException(String.format(msgcfg, Constants.OUTPUT_FORMAT));
        }

        /* min radius */
        if (!prop.containsKey(Constants.MIN_DIST)) {
            throw new IllegalArgumentException(String.format(msgcfg, Constants.MIN_DIST));
        } else {
            prop.put(Constants.MIN_DIST, Integer.parseInt(prop.getProperty(Constants.MIN_DIST)));
        }

        /* max radius */
        if (!prop.containsKey(Constants.MAX_DIST)) {
            throw new IllegalArgumentException(String.format(msgcfg, Constants.MAX_DIST));
        } else {
            prop.put(Constants.MAX_DIST, Integer.parseInt(prop.getProperty(Constants.MAX_DIST)));
        }

        if (!prop.containsKey(Constants.GINDEX_NORMALIZED)) {
            throw new IllegalArgumentException(String.format(msgcfg, Constants.GINDEX_NORMALIZED));
        } else {
            prop.put(Constants.GINDEX_NORMALIZED, Boolean.parseBoolean(prop.getProperty(Constants.GINDEX_NORMALIZED)));
        }

        if (!prop.containsKey(Constants.GINDEX_ONLY_EXT)) {
            throw new IllegalArgumentException(String.format(msgcfg, Constants.GINDEX_ONLY_EXT));
        } else {
            prop.put(Constants.GINDEX_ONLY_EXT, Boolean.parseBoolean(prop.getProperty(Constants.GINDEX_ONLY_EXT)));
        }

        if (!prop.containsKey(Constants.GINDEX_INT_ATOM_TAG)) {
            throw new IllegalArgumentException(String.format(msgcfg, Constants.GINDEX_INT_ATOM_TAG));
        } else {
            prop.put(Constants.GINDEX_INT_ATOM_TAG, Integer.parseInt(prop.getProperty(Constants.GINDEX_INT_ATOM_TAG)));
        }

        if (!prop.containsKey(Constants.GINDEX_EXT_ATOM_TAG)) {
            throw new IllegalArgumentException(String.format(msgcfg, Constants.GINDEX_EXT_ATOM_TAG));
        } else {
            prop.put(Constants.GINDEX_EXT_ATOM_TAG, Integer.parseInt(prop.getProperty(Constants.GINDEX_EXT_ATOM_TAG)));
        }

        if (!prop.containsKey(Constants.EXTERIOR_ATOM_FILTER)) {
            throw new IllegalArgumentException(String.format(msgcfg, Constants.EXTERIOR_ATOM_FILTER));
        } else {
            String extAtomFilterCN = prop.getProperty(Constants.EXTERIOR_ATOM_FILTER);

            Class<?> extAFC = Class.forName(extAtomFilterCN);
            ExtAtomsFilter extAtomFilter = (ExtAtomsFilter) extAFC.getDeclaredConstructor().newInstance();
            prop.put(Constants.EXTERIOR_ATOM_FILTER, extAtomFilter);
        }

        if (!prop.containsKey(Constants.MAX_EDGES_BY_ATOM)) {
            throw new IllegalArgumentException(String.format(msgcfg, Constants.MAX_EDGES_BY_ATOM));
        } else {
            prop.put(Constants.MAX_EDGES_BY_ATOM, Double.parseDouble(prop.getProperty(Constants.MAX_EDGES_BY_ATOM)));
        }

        if (!prop.containsKey(Constants.MOL_TYPE)) {
            throw new IllegalArgumentException(String.format(msgcfg, Constants.MOL_TYPE));
        }

        return prop;
    }

    /**
     * @param args arguments for the call.
     * @throws NoSuchMethodException for errors while loading the atom filter.
     * @throws InvocationTargetException for errors while loading the atom
     * filter.
     */
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException {
        Logger.getLogger(Wiener3D.class.getName()).log(Level.INFO, "Computing 3D Wiener Index...");
        try {
            Args aa = new Args();
            JCommander.newBuilder()
                    .addObject(aa)
                    .build()
                    .parse(args);

            if (aa.cfgPath != null) {
                Wiener3D wtd = new Wiener3D();
                Properties prop = wtd.init(aa.cfgPath);

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
                    default:
                        break;
                }
            } else {
                throw new IllegalArgumentException("Configuration file not specified... must supply -cfg option...");
            }
        } catch (IOException | ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InstantiationException | SomeErrorException ex) {
            Logger.getLogger(Wiener3D.class.getName()).log(Level.SEVERE, null, ex);
        }
        Logger.getLogger(Wiener3D.class.getName()).log(Level.INFO, "Computing 3D Wiener Index. Done!");
    }

    /**
     * Contains constants required by this class.
     */
    public static class Constants {

        private Constants() {
        }
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

        private IntervalTypeCodes() {
        }
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
    public enum MolType {

        /**
         *
         */
        V2000,
        /**
         *
         */
        V3000
    }

    private static class Args {

        @Parameter(names = {"--cfg", "-cfg"})
        String cfgPath = null;
    }

    private class WienerArguments {

        private final int pathL;
        private final boolean normalized;
        private final boolean onlyExt;
        private final int extNodeTagL;
        private final int intNodeTagL;
        private final int precision;

        WienerArguments(int pathL, boolean normalized, boolean onlyExt, int extNodeTagL, int intNodeTagL, int precision) {
            this.pathL = pathL;
            this.normalized = normalized;
            this.onlyExt = onlyExt;
            this.extNodeTagL = extNodeTagL;
            this.intNodeTagL = intNodeTagL;
            this.precision = precision;
        }

        /**
         * @return the pathL
         */
        public int getPathL() {
            return pathL;
        }

        /**
         * @return the normalized
         */
        public boolean isNormalized() {
            return normalized;
        }

        /**
         * @return the onlyExt
         */
        public boolean isOnlyExt() {
            return onlyExt;
        }

        /**
         * @return the extNodeTagL
         */
        public int getExtNodeTagL() {
            return extNodeTagL;
        }

        /**
         * @return the intNodeTagL
         */
        public int getIntNodeTagL() {
            return intNodeTagL;
        }

        /**
         * @return the precision
         */
        public int getPrecision() {
            return precision;
        }
    }
}
