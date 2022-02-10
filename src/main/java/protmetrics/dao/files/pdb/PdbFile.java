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

    private int[] caLinesIndex; /* indices of the lines with CA */
    private PdbLine[] lines;
    private String proteinName;
    private String path = "";

    /**
     * @param path Path to the .pdb file.
     * @throws Exception for problems while loading the file.
     */
    public PdbFile(String path) throws Exception {
        this.path = path;
        try {
            this.intPDB(path, "~");
        } catch (Exception m_see) {
            throw m_see;
        }
    }

    /**
     * @return the CA lines within the .pdb file.
     */
    public PdbAtomLine[] getCALines() {

        PdbAtomLine[] m_result = new PdbAtomLine[this.caLinesIndex.length];
        int m_resultlength = m_result.length;

        for (int index = 0; index < m_resultlength; index++) {
            m_result[index] = new PdbAtomLine(this.lines[this.caLinesIndex[index]].line, this.lines[this.caLinesIndex[index]].lineTokens);
        }
        return m_result;

    }

    /**
     * @param path the path to the .pdb file.
     * @param seq sequence to be searched.
     * @throws Exception for problems while loading the file.
     */
    public void intPDB(String path, String seq) throws Exception {

        try {

            java.io.LineNumberReader m_sr = new LineNumberReader(new FileReader(path));

            String m_actualLine = m_sr.readLine();

            String[] m_tokens;

            char[] m_sep = {' '};

            PdbAtomLine m_alaux;

            ArrayList m_lines = new ArrayList();
            ArrayList<Integer> m_CALinesIndex = new ArrayList();

            int m_totalPdbLines = 0;

            /* read the protein name, check if always is the first line */
            m_tokens = BioUtils.procSplitString(m_actualLine.trim().split("[\\s]+", 0));
            this.proteinName = m_tokens[1];
            while (m_actualLine != null) {

                m_tokens = BioUtils.procSplitString(m_actualLine.trim().split("[\\s]+", 0));

                if (m_tokens[0].equals("ATOM")) {

                    /* if it is from the selected sequence*/
                    m_alaux = new PdbAtomLine(m_actualLine, m_tokens);
                    if (m_alaux.getSequence().equals(seq)) {

                        /* if it is CA line, add it to m_CALinesIndex */
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
            m_sr.close();
        } catch (IOException m_ioe) {
            throw m_ioe;
        } catch (Exception m_e) {
            throw new SomeErrorException("ERROR AT->" + path);
        }
    }

    private int[] toInt(ArrayList AL) {
        int[] m_result = new int[AL.size()];
        Integer m_aux;
        for (int i = 0; i < m_result.length; ++i) {
            m_aux = (Integer) AL.get(i);
            m_result[i] = m_aux;
        }
        return m_result;
    }

    private PdbLine[] toPDBLine(ArrayList AL) {
        PdbLine[] m_result = new PdbLine[AL.size()];
        for (int i = 0; i < m_result.length; ++i) {
            m_result[i] = (PdbLine) AL.get(i);
        }
        return m_result;
    }

    /**
     * @return the sequence within the .pdb file.
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
     * @return the name of the protein in .pdb file.
     */
    public String getProteinName() {
        return proteinName;
    }
}
