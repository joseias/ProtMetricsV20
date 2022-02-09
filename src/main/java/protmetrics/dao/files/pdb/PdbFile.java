package protmetrics.dao.files.pdb;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;

import protmetrics.errors.SomeErrorException;
import protmetrics.utils.BioUtils;

/**
 * Wrapper to represent a .pdb file.
 * 
 */
public final class PdbFile {

    private int[] caLinesIndex; // indices of the lines with CA 
    private int[] atomLinesIndex; //indices of the lines of type ATOM 
    private PdbLine[] lines;
    private String proteinName;
    private String pdbPath = "";

    /**
     *
     */
    public PdbFile() {

    }

    /**
     *
     * @param string
     * @throws Exception
     */
    public PdbFile(String a_pdbFilePath) throws Exception {
        pdbPath = a_pdbFilePath;
        try {
            this.intPDB(a_pdbFilePath, "~");
        } catch (Exception m_see) {
            throw m_see;
        }

    }

    /**
     *
     * @return
     */
    public PdbAtomLine[] getCALines() {

        PdbAtomLine[] m_result = new PdbAtomLine[this.caLinesIndex.length];
        int m_resultlength = m_result.length;

        for (int index = 0; index < m_resultlength; index++) {
            m_result[index] = new PdbAtomLine(this.lines[this.caLinesIndex[index]].Line, this.lines[this.caLinesIndex[index]].LineTokens);
        }
        return m_result;

    }

    /**
     *
     * @param a_pdbFilePath
     * @param a_seq
     * @throws Exception
     */
    public void intPDB(String a_pdbFilePath, String a_seq) throws Exception {

        try {

            java.io.LineNumberReader m_sr = new LineNumberReader(new FileReader(a_pdbFilePath));

            String m_actualLine = m_sr.readLine();

            String[] m_tokens;

            char[] m_sep = {' '};

            PdbAtomLine m_alaux;

            ArrayList m_lines = new ArrayList();
            ArrayList m_CALinesIndex = new ArrayList();
            ArrayList m_ATOMLinesIndex = new ArrayList();

            int m_totalPdbLines = 0;

            /* read the protein name, check if always is the first line */
            m_tokens = BioUtils.procSplitString(m_actualLine.trim().split("[\\s]+", 0));
            this.proteinName = m_tokens[1];
            while (m_actualLine != null) {

                m_tokens = BioUtils.procSplitString(m_actualLine.trim().split("[\\s]+", 0));

                if (m_tokens[0].equals("ATOM")) {

                    /* if it is from the selected sequence*/
                    m_alaux = new PdbAtomLine(m_actualLine, m_tokens);
                    if (m_alaux.getSequence().equals(a_seq)) {
                        /* is an ATOM line, add it to m_ATOMLinesIndex*/
                        m_ATOMLinesIndex.add(m_totalPdbLines);

                        /*if it is CA line, add it to m_CALinesIndex*/
                        if (m_alaux.getAtomType().equals("CA")) {
                            m_CALinesIndex.add(m_totalPdbLines);
                        }
                        m_lines.add(m_alaux);
                    }
                } else {
                    m_lines.add(new PdbLine(m_actualLine));
                }

                m_actualLine = m_sr.readLine();
                m_totalPdbLines++;

            }

            this.lines = this.toPDBLine(m_lines);
            this.caLinesIndex = this.toInt(m_CALinesIndex);
            this.atomLinesIndex = this.toInt(m_ATOMLinesIndex);
            m_sr.close();
        } catch (IOException m_ioe) {
            throw m_ioe;
        } catch (Exception m_e) {
            throw new SomeErrorException("ERROR AT->" + pdbPath);
        }
    }

    private int[] toInt(ArrayList a_AL) {
        int[] m_result = new int[a_AL.size()];
        Integer m_aux;
        for (int i = 0; i < m_result.length; ++i) {
            m_aux = (Integer) a_AL.get(i);
            m_result[i] = m_aux;
        }
        return m_result;
    }

    private PdbLine[] toPDBLine(ArrayList a_AL) {
        PdbLine[] m_result = new PdbLine[a_AL.size()];
        for (int i = 0; i < m_result.length; ++i) {
            m_result[i] = (PdbLine) a_AL.get(i);
        }
        return m_result;
    }

    /**
     *
     * @return
     */
    public String getSequence() {
        PdbAtomLine[] m_CALines = this.getCALines();
        String m_result = "";
        for (PdbAtomLine m_CALine : m_CALines) {
            m_result = m_result + BioUtils.aminoThreetoOne(m_CALine.getAminoType());
        }
        return m_result;
    }

    /**
     *
     * @return
     */
    public String getProteinName() {
        return proteinName;
    }
}
