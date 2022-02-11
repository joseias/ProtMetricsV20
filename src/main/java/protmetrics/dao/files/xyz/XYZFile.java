package protmetrics.dao.files.xyz;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import javax.vecmath.Point3d;
import protmetrics.utils.GlobalConstants;

/**
 * Wrapper to represent a .xyz file.
 */
public class XYZFile {

    private List<GAtom> atoms;
    private String id;

    /**
     * @return list of atoms within the .xyz file.
     */
    public List<GAtom> getAtoms() {
        return atoms;
    }

    /**
     * @param atoms list of atoms to set.
     */
    public void setAtoms(List<GAtom> atoms) {
        this.atoms = atoms;
    }

    /**
     * @return size of the atoms list.
     */
    public int getSize() {
        return getAtoms().size();
    }

    /**
     * @param path the path to the .xyz file.
     * @param hDepleted if consider H depleted.
     * @throws IOException for problems while loading the file.
     * @throws NumberFormatException for casting errors.
     */
    public XYZFile(String path, boolean hDepleted) throws IOException, NumberFormatException {

        File xyzFile = new File(path);
        this.id = xyzFile.getName();
        try (LineNumberReader lnr = new LineNumberReader(new FileReader(xyzFile))) {
            int size;
            /* capturing the number of atoms*/
            String currentLine = lnr.readLine();
            size = Integer.parseInt(currentLine.replaceAll(GlobalConstants.SPACE_PATTERN, GlobalConstants.EMPTY_STRING));
            atoms = new ArrayList<>(size);

            currentLine = lnr.readLine();

            /* first atom line*/
            currentLine = lnr.readLine();

            while (currentLine != null) {
                String[] tokens = currentLine.trim().split(GlobalConstants.SPACE_PATTERN);

                Point3d aloc = new Point3d(Double.parseDouble(tokens[1]),
                        Double.parseDouble(tokens[2]),
                        Double.parseDouble(tokens[3]));

                AtomType at = null;
                if (tokens[0].equals("H")) {
                    if (!hDepleted) {
                        at = AtomType.getTypeFromCode(tokens[0]);
                    }
                } else {
                    at = AtomType.getTypeFromCode(tokens[0]);
                }

                if (at != null) {
                    this.atoms.add(new GAtom(aloc, at, this.getAtoms().size() + 1));
                }
                currentLine = lnr.readLine();
            }
        }
    }

    /**
     * @return the id.
     */
    public String getID() {
        return id;
    }

    /**
     * @param id the id to set.
     */
    public void setID(String id) {
        this.id = id;
    }
}
