package protmetrics.dao.files.XYZ;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point3D;
import protmetrics.utils.GlobalConstants;

public class XYZFile {

    private List<GAtom> atoms;
    private String ID;

    public List<GAtom> getAtoms() {
        return atoms;
    }

    public void setAtoms(List<GAtom> atoms) {
        this.atoms = atoms;
    }

    public int getSize() {
        return getAtoms().size();
    }

    public XYZFile(String xyzFilePath, boolean hDepleted) throws IOException, NumberFormatException {

        File xyzFile = new File(xyzFilePath);
        this.ID = xyzFile.getName();

        LineNumberReader m_lnr = new LineNumberReader(new FileReader(xyzFile));
        int size;
        /*Capturing the number of atoms*/
        String currentLine = m_lnr.readLine();
        size = Integer.parseInt(currentLine.replaceAll(GlobalConstants.SPACE_PATTERN, GlobalConstants.EMPTY_STRING));
        atoms = new ArrayList<>(size);

        currentLine = m_lnr.readLine();

        /*First atom line*/
        currentLine = m_lnr.readLine();

        while (currentLine != null) {
            String[] tokens = currentLine.trim().split(GlobalConstants.SPACE_PATTERN);

            Point3D aloc = new Point3D(Double.parseDouble(tokens[1]),
                    Double.parseDouble(tokens[2]),
                    Double.parseDouble(tokens[3]));

            AtomType at = null;
            switch (tokens[0]) {
                case "H":
                    if (!hDepleted) {
                        at = AtomType.getTypeFromCode(tokens[0]);
                    }
                    break;

                case "He":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Li":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Be":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "B":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "C":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "N":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "O":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "F":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Na":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Mg":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Al":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Si":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "P":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "S":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Cl":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Ar":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "K":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Ca":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Sc":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Ti":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "V":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Cr":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Mn":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Fe":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Co":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Ni":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Cu":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Zn":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Ga":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Ge":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "As":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Se":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Br":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Kr":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Rb":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Sr":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Y":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Zr":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Nb":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Mo":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Tc":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Ru":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Rh":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Pd":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Ag":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Cd":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "In":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Sn":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Sb":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Te":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "I":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Xe":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Cs":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Ba":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "La":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Ce":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Pr":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Nd":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Pm":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Sm":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Eu":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Gd":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Tb":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Dy":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Ho":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Er":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Tm":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Yb":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Lu":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Hf":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Ta":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "W":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Re":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Os":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Ir":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Pt":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Au":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Hg":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Tl":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Pb":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Bi":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Po":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "At":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Rn":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Fr":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Ra":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Ac":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Th":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Pa":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "U":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Np":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Pu":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Am":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Cm":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Bk":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Cf":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Es":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Fm":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Md":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "No":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Lr":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Rf":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Db":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Sg":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Bh":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Hs":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Mt":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Ds":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Rg":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Cn":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Uut":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Fl":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Uup":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Lv":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Uus":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                case "Uuo":
                    at = AtomType.getTypeFromCode(tokens[0]);
                    break;

                default:
                    break;
            } //switch

            if (at != null) {
                this.atoms.add(new GAtom(aloc, at, getAtoms().size() + 1));
            }
            currentLine = m_lnr.readLine();
        }// while

        m_lnr.close();
    }

    /**
     * @return the ID
     */
    public String getID() {
        return ID;
    }

    /**
     * @param ID the ID to set
     */
    public void setID(String ID) {
        this.ID = ID;
    }
}
