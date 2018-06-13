package protmetrics.dao.files.pdb;

import javafx.geometry.Point3D;
import protmetrics.utils.BioUtils;

public class PdbAtomLine extends PdbLine {

    private final int aminoNumberPos;
    private String sequence;
    private final Point3D location;
    private final String aminoNumber;
    private String aminoType;
    private final String atomType;

    public PdbAtomLine(String a_line, String[] a_lineTokens) {
        super(a_line, a_lineTokens);
        //El numero del aminoacido debe ser el segundo numero en a_lineTokens
        this.LineTokens = a_lineTokens;

        //Si no lo encuentra entonces !!!! El CAOS
        aminoNumberPos = this.getAminoNumberLocation(a_lineTokens);
        if (aminoNumberPos != -1) {
            aminoNumber = a_lineTokens[aminoNumberPos];
        }
        else {
            aminoNumber = "~";
        }

        //Parece que son los 3 que siguen a AminoNumber
        double m_X = Double.parseDouble(a_lineTokens[aminoNumberPos + 1]);
        double m_Y = Double.parseDouble(a_lineTokens[aminoNumberPos + 2]);
        double m_Z = Double.parseDouble(a_lineTokens[aminoNumberPos + 3]);
        location = new Point3D(m_X, m_Y, m_Z);

        //Hasta ahora no parece haber nada que contradiga que esto esta en a_lineTokens[4] segun Michael
        aminoType = a_lineTokens[3];

        //Parece estar en la a_lineTokens[2]
        atomType = a_lineTokens[2];

        //Basados en que delante del numero de aminoacido o esta la secuencia o el tipo de amino
        if (aminoNumberPos != -1) {
            if (BioUtils.isAminoSequence(a_lineTokens[aminoNumberPos - 1]) == false) {
                sequence = a_lineTokens[aminoNumberPos - 1];
            }
            else // Hay una sola secuencia
            {
                sequence = "~";
            }

        }

    }

    public int getAminoNumberLocation(String[] a_lineTokens) {

        int m_result = -1;
        int m_numbersFind = 0;
        int m_pos = 0;
        if (a_lineTokens == null) {
            System.out.println("ES NULL");
        }
        int m_length = a_lineTokens.length;

        while (m_pos < m_length && m_numbersFind < 2) {
            if (BioUtils.isInteger(a_lineTokens[m_pos])) {
                m_numbersFind++;
            }
            if (m_numbersFind == 2) {
                m_result = m_pos;
            }
            m_pos++;
        }//while
        return m_result;

    }

    public void upDatePdbLine() {
        String m_result = this.LineTokens[0];
        boolean m_changed = false;
        int m_tlength = LineTokens.length;
        for (int i = 1; i < m_tlength; i++) {
            //Esto sera BRUTAL
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
                //Notar que si no hay secuencia no se puede cambiar CLARO ESTA!!!!
            }
            if (i == aminoNumberPos + 1) {
                m_result = m_result + "	 " + this.location.getX();
                this.LineTokens[aminoNumberPos + 1] = Double.toString(this.location.getX());
                m_changed = true;
            }
            if (i == aminoNumberPos + 2) {
                m_result = m_result + "	 " + this.location.getY();
                this.LineTokens[aminoNumberPos + 2] = Double.toString(this.location.getY());
                m_changed = true;
            }
            if (i == aminoNumberPos + 3) {
                m_result = m_result + "	 " + this.location.getZ();
                this.LineTokens[aminoNumberPos + 3] = Double.toString(this.location.getZ());
                m_changed = true;
            }
            if (m_changed == false) {
                m_result = m_result + "	 " + this.LineTokens[i];
            }

        }//for(int i=0;i<m_tlength;i++)
        this.Line = m_result;
    }//public  String UpDateString()

    public Point3D getLocation() {
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

    public String getSequence(){
        return this.sequence;
    }
}// PdbAtomLine class
