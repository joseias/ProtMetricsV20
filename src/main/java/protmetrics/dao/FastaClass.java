package protmetrics.dao;

/*
 * FastaClass.java
 *
 * Created on 15 de junio de 2007, 10:18
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Vector;

import protmetrics.errors.SomeErrorException;

/**
 *
 * @author personal
 */
public class FastaClass {

    /**
     * Creates a new instance of FastaClass
     */
    String[][] Sequences;

    String c_fastaPath = "";

    public FastaClass(String a_fastaPath) throws Exception {
        c_fastaPath = a_fastaPath;
        try {

            this.initFasta(a_fastaPath);
        } catch (SomeErrorException m_see) {
            throw m_see;
        } catch (Exception m_e) {
            throw m_e;
        }
    }

    private void initFastaOld(String a_fastaPath) throws Exception {
        try {
            LineNumberReader m_lnr = new LineNumberReader(new FileReader(a_fastaPath));
            String m_actualLine = m_lnr.readLine();
            String[] m_tokens;
            String m_protName;
            String m_protSequence;
            Vector<String> m_pNames = new Vector<String>();
            Vector<String> m_pSequences = new Vector<String>();
            while (m_actualLine != null) {
                m_tokens = m_actualLine.trim().split("[\\s]+", 0);
                if (m_tokens[0].charAt(0) == '>') {
                    m_protName = m_tokens[0].substring(1, m_tokens[0].length());

                    m_actualLine = m_lnr.readLine();
                    m_tokens = m_actualLine.trim().split("[\\s]+", 0);
                    if (m_tokens[0].charAt(0) != '>') {
                        m_protSequence = m_actualLine;
                        m_pSequences.add(m_protSequence);
                        m_pNames.add(m_protName);
                        m_actualLine = m_lnr.readLine();
                    } else {
                        m_actualLine = m_lnr.readLine();
                    }
                } else {
                    m_actualLine = m_lnr.readLine();
                }

            }//while
            Sequences = new String[m_pNames.size()][2];
            for (int i = 0; i < m_pNames.size(); i++) {
                Sequences[i][0] = m_pNames.elementAt(i).toString();
                Sequences[i][1] = m_pSequences.elementAt(i).toString();
            }

        }//try
        catch (IOException m_ioe) {
            //m_ioe.printStackTrace();
            throw m_ioe;

        } catch (Exception m_e) {
            //m_e.printStackTrace();
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
            Vector<String> m_pNames = new Vector<String>();
            Vector<String> m_pSequences = new Vector<String>();
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
                Sequences[i][0] = m_pNames.elementAt(i).toString();
                Sequences[i][1] = m_pSequences.elementAt(i).toString();
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
