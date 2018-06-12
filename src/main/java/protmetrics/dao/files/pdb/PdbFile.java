package protmetrics.dao.files.pdb;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;

import protmetrics.errors.SomeErrorException;
import protmetrics.utils.BioUtils;
/// <summary>
/// Summary description for PdbFile.
/// </summary>

public class PdbFile {

    public int[] caLinesIndex; //Numeros de lineas donde estan los CA.
    public int[] atomLinesIndex; //Numero de las lineas que son ATOM.
    public PdbLine[] lines;
    public String proteinName;
    public String pdbPath = "";

    public PdbFile() {

    }

    public PdbFile(String a_pdbFilePath) throws Exception {
        pdbPath = a_pdbFilePath;
        try {
            this.intPDB(a_pdbFilePath, "~");
        } catch (Exception m_see) {
            throw m_see;
        }

    }//public PdbFile(String a_pdbFilePath)

    public PdbFile(String a_pdbFilePath, String a_seq) throws Exception {
        pdbPath = a_pdbFilePath;
        try {
            this.intPDB(a_pdbFilePath, a_seq);
        } catch (Exception m_see) {
            throw m_see;
        }

    }//public PdbFile(String a_pdbFilePath)

    public PdbAtomLine[] getCALines() {

        PdbAtomLine[] m_result = new PdbAtomLine[this.caLinesIndex.length];
        int m_resultlength = m_result.length;

        for (int index = 0; index < m_resultlength; index++) {
            m_result[index] = new PdbAtomLine(this.lines[this.caLinesIndex[index]].Line, this.lines[this.caLinesIndex[index]].LineTokens);
        }
        return m_result;

    }//public AtomLines[] getCALines()

    public void intPDB(String a_pdbFilePath, String a_seq) throws Exception {

        try {

            //C#-> StreamReader m_sr=new StreamReader(a_pdbFilePath);
            java.io.LineNumberReader m_sr = new LineNumberReader(new FileReader(a_pdbFilePath));

            String m_actualLine = m_sr.readLine();

            String[] m_tokens;

            char[] m_sep = {' '};

            PdbAtomLine m_alaux;

            ArrayList m_lines = new ArrayList();
            ArrayList m_CALinesIndex = new ArrayList();
            ArrayList m_ATOMLinesIndex = new ArrayList();

            int m_totalPdbLines = 0;

            /*Leer el nombre de la proteina, ver si siempre esta en la misma posicion de la primera linea.*/
            m_tokens = BioUtils.ProcSplitString(m_actualLine.trim().split("[\\s]+", 0));
            this.proteinName = m_tokens[1];
            while (m_actualLine != null) {
                //Aqui se ira poniendo la inicializacion de todas la variable necesarias en PdbFile

                //Parece que hacer m_actualLines.Split(m_sep) da alguna cacharra, verificar
                //m_tokens=BioUtils.ProcSplitString(m_actualLine.split(new String(new String(m_sep))));
                m_tokens = BioUtils.ProcSplitString(m_actualLine.trim().split("[\\s]+", 0));

                if (m_tokens[0].equals("ATOM")) {

                    //Si es de la secuencia seleccionada
                    m_alaux = new PdbAtomLine(m_actualLine, m_tokens);
                    if (m_alaux.Sequence.equals(a_seq)) {
                        //Es una linea ATOM asi que adicionarala a m_ATOMLineIndex
                        m_ATOMLinesIndex.add(m_totalPdbLines);

                        // Si es un CA entonces adicionarla a m_CALinesIndex
                        if (m_alaux.GetAtomType().equals("CA")) {
                            m_CALinesIndex.add(m_totalPdbLines);
                        }
                        m_lines.add(m_alaux);
                    }
                } else {
                    m_lines.add(new PdbLine(m_actualLine));
                }

                m_actualLine = m_sr.readLine();
                m_totalPdbLines++;

            }//while(m_actualLine!=null)

            this.lines = this.toPDBLine(m_lines);
            this.caLinesIndex = this.toInt(m_CALinesIndex);
            this.atomLinesIndex = this.toInt(m_ATOMLinesIndex);
            m_sr.close();
        }
        catch (IOException m_ioe) {
            throw m_ioe;
        } catch (Exception m_e) {
            throw new SomeErrorException("ERROR AT->" + pdbPath);
        }
    }//public void intPDB(String a_pdbFilePath)

    private int[] toInt(ArrayList a_AL) {
        int[] m_result = new int[a_AL.size()];
        Integer m_aux;
        for (int i = 0; i < m_result.length; i++) {
            m_aux = (Integer) a_AL.get(i);
            m_result[i] = m_aux.intValue();
        }
        return m_result;
    } //private int[] ToInt(ArrayList a_AL)

    private PdbLine[] toPDBLine(ArrayList a_AL) {
        PdbLine[] m_result = new PdbLine[a_AL.size()];
        for (int i = 0; i < m_result.length; i++) {
            m_result[i] = (PdbLine) a_AL.get(i);
        }
        return m_result;
    }//private PdbLine[] toPDBLine(ArrayList a_AL)

    public String getSequence() {
        PdbAtomLine[] m_CALines = this.getCALines();
        String m_result = "";
        for (int i = 0; i < m_CALines.length; i++) {
            //System.out.println(m_CALines[i].GetAminoType());
            m_result = m_result + BioUtils.AminoThreetoOne(m_CALines[i].GetAminoType());
        }
        return m_result;
    }

    public String getProteinName(){
        return proteinName;
    }
}//class
