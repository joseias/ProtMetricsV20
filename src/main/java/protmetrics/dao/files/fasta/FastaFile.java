package protmetrics.dao.files.fasta;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;

import protmetrics.errors.SomeErrorException;

/**
 * Wrapper to represent a .fasta file.
 */
public class FastaFile {

    String[][] Sequences;

    String c_fastaPath = "";

    /**
     * @param path path to the .fasta file.
     * @throws Exception for problems while loading the file.
     */
    public FastaFile(String path) throws Exception {
        c_fastaPath = path;
        try {
            this.initFasta(path);
        } catch (SomeErrorException m_see) {
            throw m_see;
        } catch (Exception m_e) {
            throw m_e;
        }
    }

    /**
     * @param path path to the .fasta file.
     * @throws Exception for problems while loading the file.
     */
    private void initFasta(String path) throws Exception {
        try {
            LineNumberReader m_lnr = new LineNumberReader(new FileReader(path));
            String m_actualLine = m_lnr.readLine();
            String[] m_tokens;
            String m_protName = "";
            String m_protSequence = "";
            ArrayList<String> m_pNames = new ArrayList<>();
            ArrayList<String> m_pSequences = new ArrayList<>();
            boolean m_newProt = false;
            while (m_actualLine != null) {
                m_tokens = m_actualLine.trim().split("[\\s]+", 0);
                m_newProt = false;
                m_protSequence = "";
                if (m_tokens[0].charAt(0) == '>') {
                    m_protName = m_tokens[0].substring(1, m_tokens[0].length());

                    m_actualLine = m_lnr.readLine();
                    while ((m_actualLine != null) && (m_newProt == false)) {
                        m_tokens = m_actualLine.trim().split("[\\s]+", 0);
                        if (m_tokens[0].charAt(0) != '>') {
                            m_protSequence = m_protSequence + m_actualLine;
                            m_actualLine = m_lnr.readLine();
                        } else {
                            m_newProt = true;
                            m_pSequences.add(m_protSequence);
                            m_pNames.add(m_protName);
                            m_protName = m_tokens[0].substring(1, m_tokens[0].length());

                        }
                    }
                    if (m_actualLine == null) {
                        m_pSequences.add(m_protSequence);
                        m_pNames.add(m_protName);
                    }
                } else {
                    m_actualLine = m_lnr.readLine();
                }
            }

            Sequences = new String[m_pNames.size()][2];
            for (int i = 0; i < m_pNames.size(); ++i) {
                Sequences[i][0] = m_pNames.get(i);
                Sequences[i][1] = m_pSequences.get(i);
            }

        } catch (IOException m_ioe) {
            throw m_ioe;
        } catch (Exception m_e) {
            throw new SomeErrorException("ERROR AT->" + c_fastaPath);

        }
    }

    /**
     * @return sequences of the molecules in the .fasta file.
     */
    public String[][] getSequences() {
        return Sequences;
    }

    /**
     * @param sequences sequences of the molecules to set.
     */
    public void setSequences(String[][] sequences) {
        Sequences = sequences;
    }
}
