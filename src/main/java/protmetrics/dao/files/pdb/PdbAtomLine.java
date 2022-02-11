package protmetrics.dao.files.pdb;

import javax.vecmath.Point3d;

import protmetrics.utils.BioUtils;

/**
 * Represents a line containing atom information within a .pdb file.
 */
public final class PdbAtomLine extends PdbLine {

    private final int aminoNumberPos;
    private String sequence;
    private final Point3d location;
    private String aminoType;
    private final String atomType;

    /**
     * @param line string representing a line within a .pdb file.
     * @param lineTokens tokens of the line.
     */
    public PdbAtomLine(String line, String[] lineTokens) {
        super(line, lineTokens);

        /* the amino acid number must be the 2nd number in lineTokens */
        this.lineTokens = lineTokens;

        aminoNumberPos = this.getAminoNumberLocation(lineTokens);

        /* they seem to be the 3 numbers after the amino acid number  */
        double x = Double.parseDouble(lineTokens[aminoNumberPos + 1]);
        double y = Double.parseDouble(lineTokens[aminoNumberPos + 2]);
        double z = Double.parseDouble(lineTokens[aminoNumberPos + 3]);
        location = new Point3d(x, y, z);

        aminoType = lineTokens[3];

        atomType = lineTokens[2];

        /* before the amino acid number we found the sequence or the type */
        if (aminoNumberPos != -1) {
            if (!BioUtils.isAminoSequence(lineTokens[aminoNumberPos - 1])) {
                sequence = lineTokens[aminoNumberPos - 1];
            } else {
                /* only one sequence */
                sequence = "~";
            }
        }
    }

    /**
     * @param lineTokens tokens within the line.
     * @return the location of the amino acid number within the line.
     */
    public int getAminoNumberLocation(String[] lineTokens) {

        int result = -1;
        int numbersFind = 0;
        int pos = 0;

        int length = lineTokens.length;

        while (pos < length && numbersFind < 2) {
            if (BioUtils.isInteger(lineTokens[pos])) {
                numbersFind++;
            }
            if (numbersFind == 2) {
                result = pos;
            }
            pos++;
        }
        return result;

    }

    /**
     */
    public void upDatePdbLine() {
        StringBuilder result = new StringBuilder(this.lineTokens[0]);

        boolean changed;
        int tlength = lineTokens.length;
        for (int i = 1; i < tlength; ++i) {
            changed = false;
            if (i == 2) {
                result.append(" ").append(this.atomType);
                this.lineTokens[2] = this.atomType;
                changed = true;
            }
            if (i == 3) {
                result.append(" ").append(this.aminoType);
                this.lineTokens[3] = this.aminoType;
                changed = true;
            }
            if ((i == aminoNumberPos - 1) && (!BioUtils.isAminoSequence(this.lineTokens[aminoNumberPos - 1]))) {
                result.append(" ").append(this.sequence);
                this.lineTokens[aminoNumberPos - 1] = this.sequence;
                changed = true;
                /* note that if no sequence, no changes can be done */
            }
            if (i == aminoNumberPos + 1) {
                result.append("\\t").append(this.location.x);
                this.lineTokens[aminoNumberPos + 1] = Double.toString(this.location.x);
                changed = true;
            }
            if (i == aminoNumberPos + 2) {
                result.append("\\t").append(this.location.x);
                this.lineTokens[aminoNumberPos + 2] = Double.toString(this.location.y);
                changed = true;
            }
            if (i == aminoNumberPos + 3) {
                result.append("\\t").append(this.location.z);
                this.lineTokens[aminoNumberPos + 3] = Double.toString(this.location.z);
                changed = true;
            }
            if (!changed) {
                result.append("\\t").append(this.lineTokens[i]);
            }
        }
        this.line = result.toString();
    }

    /**
     * @return the 3d coordinates of the atom.
     */
    public Point3d getLocation() {
        return this.location;
    }

    /**
     * @param newAT new amino acid type.
     */
    public void setAminoType(String newAT) {
        this.aminoType = newAT;
        this.upDatePdbLine();
    }

    /**
     * @return the amino acid type of this atom line.
     */
    public String getAminoType() {
        return this.aminoType;
    }

    /**
     * @return the atom type of type.
     */
    public String getAtomType() {
        return this.atomType;
    }

    /**
     * @return the sequence within the atom line.
     */
    public String getSequence() {
        return this.sequence;
    }
}
