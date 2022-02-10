package protmetrics.dao.files.xyz;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.vecmath.Point3d;
import protmetrics.utils.GlobalConstants;

/**
 * Wrapper to represent a .xyz file.
 */
public class XYZFile {

    private List<GAtom> atoms;
    private String ID;

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
        this.ID = xyzFile.getName();

        LineNumberReader m_lnr = new LineNumberReader(new FileReader(xyzFile));
        int size;
        /* capturing the number of atoms*/
        String currentLine = m_lnr.readLine();
        size = Integer.parseInt(currentLine.replaceAll(GlobalConstants.SPACE_PATTERN, GlobalConstants.EMPTY_STRING));
        atoms = new ArrayList<>(size);

        currentLine = m_lnr.readLine();

        /* first atom line*/
        currentLine = m_lnr.readLine();

        while (currentLine != null) {
            String[] tokens = currentLine.trim().split(GlobalConstants.SPACE_PATTERN);

            Point3d aloc = new Point3d(Double.parseDouble(tokens[1]),
                    Double.parseDouble(tokens[2]),
                    Double.parseDouble(tokens[3]));

            AtomType at = null;
            switch (tokens[0]) {
                case "H":
                    if (!hDepleted) {
                        at = AtomType.getTypeFromCode(tokens[0]);
                    }
                    break;
                default:
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;
            }

            if (at != null) {
                this.atoms.add(new GAtom(aloc, at, getAtoms().size() + 1));
            }
            currentLine = m_lnr.readLine();
        }

        m_lnr.close();
    }

    /**
     * @return the ID.
     */
    public String getID() {
        return ID;
    }

    /**
     * @param ID the ID to set.
     */
    public void setID(String ID) {
        this.ID = ID;
    }
}
