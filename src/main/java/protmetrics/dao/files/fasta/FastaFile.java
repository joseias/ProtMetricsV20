package protmetrics.dao.files.fasta;

/*
 * FastaFile.java
 *
 * Created on 15 de junio de 2007, 10:18
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;

import protmetrics.errors.SomeErrorException;

/**
 *
 * @author personal
 */
public class FastaFile {

    /**
     * Creates a new instance of FastaFile
     */
    String[][] Sequences;

    String c_fastaPath = "";

    public FastaFile(String a_fastaPath) throws Exception {
        c_fastaPath = a_fastaPath;
        try {

            this.initFasta(a_fastaPath);
        } catch (SomeErrorException m_see) {
            throw m_see;
        } catch (Exception m_e) {
            throw m_e;
        }
    }

    private void initFasta(String a_fastaPath) throws Exception {
        try {
            LineNumberReader m_lnr = new LineNumberReader(new FileReader(a_fastaPath));
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
                        //m_actualLine=m_lnr.readLine();
                    }
                    if (m_actualLine == null) {
                        m_pSequences.add(m_protSequence);
                        m_pNames.add(m_protName);
                    }
                } else {
                    m_actualLine = m_lnr.readLine();
                }
            }//while

            Sequences = new String[m_pNames.size()][2];
            for (int i = 0; i < m_pNames.size(); i++) {
                Sequences[i][0] = m_pNames.get(i);
                Sequences[i][1] = m_pSequences.get(i);
            }

        }//try
        catch (IOException m_ioe) {
            //m_ioe.printStackTrace();
            throw m_ioe;
        } catch (Exception m_e) {
            //m_e.printStackTrace();
            throw new SomeErrorException("ERROR AT->" + c_fastaPath);

        }
    }

    public String[][] getSequences() {
        return Sequences;
    }

    public void setSequences(String[][] sequences) {
        Sequences = sequences;
    }

}
