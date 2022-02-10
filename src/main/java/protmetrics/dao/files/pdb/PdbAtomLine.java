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
    private final String aminoNumber;
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
        if (aminoNumberPos != -1) {
            aminoNumber = lineTokens[aminoNumberPos];
        } else {
            aminoNumber = "~";
        }

        /* they seem to be the 3 numbers after the amino acid number  */
        double m_X = Double.parseDouble(lineTokens[aminoNumberPos + 1]);
        double m_Y = Double.parseDouble(lineTokens[aminoNumberPos + 2]);
        double m_Z = Double.parseDouble(lineTokens[aminoNumberPos + 3]);
        location = new Point3d(m_X, m_Y, m_Z);

        aminoType = lineTokens[3];

        atomType = lineTokens[2];

        /* before the amino acid number we found the sequence or the type */
        if (aminoNumberPos != -1) {
            if (BioUtils.isAminoSequence(lineTokens[aminoNumberPos - 1]) == false) {
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

        int m_result = -1;
        int m_numbersFind = 0;
        int m_pos = 0;

        int m_length = lineTokens.length;

        while (m_pos < m_length && m_numbersFind < 2) {
            if (BioUtils.isInteger(lineTokens[m_pos])) {
                m_numbersFind++;
            }
            if (m_numbersFind == 2) {
                m_result = m_pos;
            }
            m_pos++;
        }
        return m_result;

    }

    /**
     */
    public void upDatePdbLine() {
        String m_result = this.lineTokens[0];
        boolean m_changed = false;
        int m_tlength = lineTokens.length;
        for (int i = 1; i < m_tlength; ++i) {
            m_changed = false;
            if (i == 2) {
                m_result = m_result + "	 " + this.atomType;
                this.lineTokens[2] = this.atomType;
                m_changed = true;
            }
            if (i == 3) {
                m_result = m_result + "	 " + this.aminoType;
                this.lineTokens[3] = this.aminoType;
                m_changed = true;
            }
            if (i == aminoNumberPos - 1) {
                if (BioUtils.isAminoSequence(this.lineTokens[aminoNumberPos - 1]) == false) {
                    m_result = m_result + "	 " + this.sequence;
                    this.lineTokens[aminoNumberPos - 1] = this.sequence;
                    m_changed = true;
                }
                /* note that if no sequence, no changes can be done */
            }
            if (i == aminoNumberPos + 1) {
                m_result = m_result + "	 " + this.location.x;
                this.lineTokens[aminoNumberPos + 1] = Double.toString(this.location.x);
                m_changed = true;
            }
            if (i == aminoNumberPos + 2) {
                m_result = m_result + "	 " + this.location.y;
                this.lineTokens[aminoNumberPos + 2] = Double.toString(this.location.y);
                m_changed = true;
            }
            if (i == aminoNumberPos + 3) {
                m_result = m_result + "	 " + this.location.z;
                this.lineTokens[aminoNumberPos + 3] = Double.toString(this.location.z);
                m_changed = true;
            }
            if (m_changed == false) {
                m_result = m_result + "	 " + this.lineTokens[i];
            }

        }
        this.line = m_result;
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
