package protmetrics.utils.propertymatrix;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import protmetrics.utils.BioUtils;

/**
 * Wrapper for the amino acid property matrix.
 */
public class PropertyMatrix {

    private PropertyVector[] propertyVectorsColumns;

    /**
     * @param path the path to the configuration file.
     * @throws IOException for problems while loading the file.
     */
    public PropertyMatrix(String path) throws IOException {

        try (LineNumberReader sr = new LineNumberReader(new FileReader(path))) {
            String actualLine;
            String[] actualLineElements;
            String sep = "[\\s]+";

            actualLine = sr.readLine();
            if (actualLine != null) {
                /* get property indices */
                actualLineElements = actualLine.trim().split(sep, 0);
                actualLineElements = BioUtils.procSplitString(actualLineElements);
                propertyVectorsColumns = new PropertyVector[actualLineElements.length];

                actualLine = sr.readLine();

                /* get property indices */
                if (actualLine != null) /* 2 */ {
                    actualLineElements = actualLine.trim().split(sep, 0);
                    actualLineElements = BioUtils.procSplitString(actualLineElements);

                    /* create property vectors*/
                    for (int i = 0; i < actualLineElements.length; ++i) {
                        propertyVectorsColumns[i] = new PropertyVector(actualLineElements[i]);
                    }

                    actualLine = sr.readLine();
                    while (actualLine != null) {
                        actualLineElements = actualLine.trim().split(sep, 0);
                        actualLineElements = BioUtils.procSplitString(actualLineElements);

                        for (int e = 3; e < actualLineElements.length; e++) {

                            propertyVectorsColumns[e - 3].addVectorElement(new PropertyVectorElement(Integer.parseInt(actualLineElements[0]), actualLineElements[1], actualLineElements[2], Double.parseDouble(actualLineElements[e])));
                        }
                        actualLine = sr.readLine();
                    }
                }
            }

        } catch (IOException ioe) {
            Logger.getLogger(PropertyMatrix.class.getName()).log(Level.INFO, "Error while building PropertyMatrix...");
            throw ioe;
        }
    }

    /**
     * @param matrixColums property vectors to build the matrix.
     */
    public PropertyMatrix(PropertyVector[] matrixColums) {
        this.propertyVectorsColumns = matrixColums;
    }

    /**
     * *
     * Index of properties (the first property has index 0) returns a subset of
     * the PropertyVectorsColumns as a new PropertyMatrix.
     *
     * @param selectedPVC int array of the selected property vectors.
     * @return the sub-property matrix from selected property vector columns.
     */
    public PropertyMatrix getSubPropertyMatrix(int[] selectedPVC) {
        int length = selectedPVC.length;
        PropertyVector[] pvc = new PropertyVector[length];

        for (int i = 0; i < length; ++i) {
            /* not checking if value is < PropertyVectorColumns length */
            pvc[i] = this.getPropertyVectorsColumns()[selectedPVC[i]];
        }
        return new PropertyMatrix(pvc);
    }

    /**
     * @return the property vector columns.
     */
    public PropertyVector[] getPropertyVectorsColumns() {
        return propertyVectorsColumns;
    }
}
