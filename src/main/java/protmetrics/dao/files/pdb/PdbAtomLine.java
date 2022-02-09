package protmetrics.dao.files.pdb;

import javax.vecmath.Point3d;

import protmetrics.utils.BioUtils;

public final class PdbAtomLine extends PdbLine {

    private final int aminoNumberPos;
    private String sequence;
    private final Point3d location;
    private final String aminoNumber;
    private String aminoType;
    private final String atomType;

    public PdbAtomLine(String a_line, String[] a_lineTokens) {
        super(a_line, a_lineTokens);

        /* the amino acid number must be the 2nd number in a_lineTokens */
        this.LineTokens = a_lineTokens;

        aminoNumberPos = this.getAminoNumberLocation(a_lineTokens);
        if (aminoNumberPos != -1) {
            aminoNumber = a_lineTokens[aminoNumberPos];
        } else {
            aminoNumber = "~";
        }

        /* they seem to be the 3 numbers after the amino acid number  */
        double m_X = Double.parseDouble(a_lineTokens[aminoNumberPos + 1]);
        double m_Y = Double.parseDouble(a_lineTokens[aminoNumberPos + 2]);
        double m_Z = Double.parseDouble(a_lineTokens[aminoNumberPos + 3]);
        location = new Point3d(m_X, m_Y, m_Z);

        aminoType = a_lineTokens[3];

        atomType = a_lineTokens[2];

        /* before the amino acid number we found the sequence or the type */
        if (aminoNumberPos != -1) {
            if (BioUtils.isAminoSequence(a_lineTokens[aminoNumberPos - 1]) == false) {
                sequence = a_lineTokens[aminoNumberPos - 1];
            } else {
                /* only one sequence */
                sequence = "~";
            }
        }
    }

    public int getAminoNumberLocation(String[] a_lineTokens) {

        int m_result = -1;
        int m_numbersFind = 0;
        int m_pos = 0;

        int m_length = a_lineTokens.length;

        while (m_pos < m_length && m_numbersFind < 2) {
            if (BioUtils.isInteger(a_lineTokens[m_pos])) {
                m_numbersFind++;
            }
            if (m_numbersFind == 2) {
                m_result = m_pos;
            }
            m_pos++;
        }
        return m_result;

    }

    public void upDatePdbLine() {
        String m_result = this.LineTokens[0];
        boolean m_changed = false;
        int m_tlength = LineTokens.length;
        for (int i = 1; i < m_tlength; i++) {
            m_changed = false;
            if (i == 2) {
                m_result = m_result + "	 " + this.atomType;
                this.LineTokens[2] = this.atomType;
                m_changed = true;
            }
            if (i == 3) {
                m_result = m_result + "	 " + this.aminoType;
                this.LineTokens[3] = this.aminoType;
                m_changed = true;
            }
            if (i == aminoNumberPos - 1) {
                if (BioUtils.isAminoSequence(this.LineTokens[aminoNumberPos - 1]) == false) {
                    m_result = m_result + "	 " + this.sequence;
                    this.LineTokens[aminoNumberPos - 1] = this.sequence;
                    m_changed = true;
                }
                /* note that if no sequence, no changes can be done */
            }
            if (i == aminoNumberPos + 1) {
                m_result = m_result + "	 " + this.location.x;
                this.LineTokens[aminoNumberPos + 1] = Double.toString(this.location.x);
                m_changed = true;
            }
            if (i == aminoNumberPos + 2) {
                m_result = m_result + "	 " + this.location.y;
                this.LineTokens[aminoNumberPos + 2] = Double.toString(this.location.y);
                m_changed = true;
            }
            if (i == aminoNumberPos + 3) {
                m_result = m_result + "	 " + this.location.z;
                this.LineTokens[aminoNumberPos + 3] = Double.toString(this.location.z);
                m_changed = true;
            }
            if (m_changed == false) {
                m_result = m_result + "	 " + this.LineTokens[i];
            }

        }
        this.Line = m_result;
    }

    public Point3d getLocation() {
        return this.location;
    }

    public void setAminoType(String a_newAT) {
        this.aminoType = a_newAT;
        this.upDatePdbLine();
    }

    public String getAminoType() {
        return this.aminoType;
    }

    public String getAtomType() {
        return this.atomType;
    }

    public String getSequence() {
        return this.sequence;
    }
}
