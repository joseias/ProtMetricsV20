package protmetrics.dao;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintStream;
import java.util.ArrayList;

import protmetrics.errors.SomeErrorException;
import protmetrics.utils.BioUtils;
/// <summary>
/// Summary description for PdbClass.
/// </summary>

public class PdbClass {

    public int[] caLinesIndex; //Numeros de lineas donde estan los CA.
    public int[] atomLinesIndex; //Numero de las lineas que son ATOM.
    public PdbLine[] lines;
    public String proteinName;
    public String pdbPath = "";

    public PdbClass() {

    }

    public PdbClass(String a_pdbFilePath) throws Exception {
        pdbPath = a_pdbFilePath;
        try {
            this.intPDB(a_pdbFilePath, "~");
        } catch (Exception m_see) {
            throw m_see;
        }

    }//public PdbClass(String a_pdbFilePath)

    public PdbClass(String a_pdbFilePath, String a_seq) throws Exception {
        pdbPath = a_pdbFilePath;
        try {
            this.intPDB(a_pdbFilePath, a_seq);
        } catch (Exception m_see) {
            throw m_see;
        }

    }//public PdbClass(String a_pdbFilePath)

    public AtomLine[] getCALines() {

        AtomLine[] m_result = new AtomLine[this.caLinesIndex.length];
        int m_resultlength = m_result.length;

        for (int index = 0; index < m_resultlength; index++) {
            m_result[index] = new AtomLine(this.lines[this.caLinesIndex[index]].Line, this.lines[this.caLinesIndex[index]].LineTokens);
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

            AtomLine m_alaux;

            ArrayList m_lines = new ArrayList();
            ArrayList m_CALinesIndex = new ArrayList();
            ArrayList m_ATOMLinesIndex = new ArrayList();

            int m_totalPdbLines = 0;

            /*Leer el nombre de la proteina, ver si siempre esta en la misma posicion de la primera linea.*/
            m_tokens = BioUtils.ProcSplitString(m_actualLine.trim().split("[\\s]+", 0));
            this.proteinName = m_tokens[1];
            while (m_actualLine != null) {
                //Aqui se ira poniendo la inicializacion de todas la variable necesarias en PdbClass

                //Parece que hacer m_actualLines.Split(m_sep) da alguna cacharra, verificar
                //m_tokens=BioUtils.ProcSplitString(m_actualLine.split(new String(new String(m_sep))));
                m_tokens = BioUtils.ProcSplitString(m_actualLine.trim().split("[\\s]+", 0));

                if (m_tokens[0].equals("ATOM")) {

                    //Si es de la secuencia seleccionada
                    m_alaux = new AtomLine(m_actualLine, m_tokens);
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
            this.caLinesIndex = this.ToInt(m_CALinesIndex);
            this.atomLinesIndex = this.ToInt(m_ATOMLinesIndex);
            m_sr.close();
        }
        catch (IOException m_ioe) {
            throw m_ioe;
        } catch (Exception m_e) {
            throw new SomeErrorException("ERROR AT->" + pdbPath);
        }
    }//public void intPDB(String a_pdbFilePath)

    public PdbClass pdbMutation(AMutation a_mutation) {

        int[] m_pdbLinePosition = {-1};

        PdbClass m_result = null;

        if (this.canDoMutation(a_mutation, m_pdbLinePosition)) {
            m_result = this.clone();
            PdbLine p = m_result.lines[m_pdbLinePosition[0]];
            ((AtomLine) m_result.lines[m_pdbLinePosition[0]]).SetAminoType(a_mutation.NewAmino);
        }

        return m_result;

    }//public PdbClass(String a_pdbFilePath)

    @Override
    public PdbClass clone() {
        PdbClass m_result = new PdbClass();
        int m_CALineslength = this.caLinesIndex.length;
        int m_ATOMLineslength = this.atomLinesIndex.length;
        int m_Lineslength = this.lines.length;

        m_result.lines = new PdbLine[m_Lineslength];
        m_result.caLinesIndex = new int[m_CALineslength];
        m_result.atomLinesIndex = new int[m_ATOMLineslength];

        for (int index = 0; index < m_Lineslength; index++) {
            //String[] se trabaja como copia?
            m_result.lines[index] = this.lines[index].Clone();
        }
        for (int index = 0; index < m_ATOMLineslength; index++) {
            //String[] se trabaja como copia?
            m_result.atomLinesIndex[index] = this.atomLinesIndex[index];
        }
        for (int index = 0; index < m_CALineslength; index++) {
            //String[] se trabaja como copia?
            m_result.caLinesIndex[index] = this.caLinesIndex[index];
        }
        return m_result;
    }//public PdbClass clone()

    public void printPDB(String a_outPutFilePathAndName) {
        try {
            // StreamWriter m_sw=new StreamWriter(a_outPutFilePathAndName);
            PrintStream m_sw = new PrintStream(a_outPutFilePathAndName);
            int m_lineslength = this.lines.length;
            for (int index = 0; index < m_lineslength; index++) {
                m_sw.println(this.lines[index].Line);
            }
            m_sw.close();
        }//try//try
        catch (IOException m_ioe) {
            m_ioe.printStackTrace();
        } catch (Exception m_e) {
            m_e.printStackTrace();
        }

    }//public void printPDB(String a_outPutFilePathAndName)

    public boolean canDoMutation(AMutation a_mutation, int[] a_pdbLinePosition) {
        boolean m_result = false;
        int m_resultpdbLinePosition = -1;
        int m_CALineslength = this.caLinesIndex.length;
        AtomLine m_alaux;
        for (int index = 0; ((index < m_CALineslength) && (m_result == false)); index++) {
            m_alaux = (AtomLine) this.lines[this.caLinesIndex[index]];
            if (m_alaux.GetAminoNumber() == a_mutation.MutationPosition) {
                if (m_alaux.GetAminoType().equals(a_mutation.OriginalAmino)) {
                    m_result = true;
                    m_resultpdbLinePosition = this.caLinesIndex[index];
                }

            }//if(m_alaux.AminoNumber.equals(a_mutation.MutationPosition))
        }//for(int index=0;((index<m_CALineslength) && (m_result==true));index++)
        a_pdbLinePosition[0] = m_resultpdbLinePosition;
        return m_result;
    }//public boolean canDoMutation(AMutation a_mutation)

    /// <summary>
    /// Esto para si no se quiere utilizar canDoMutation, aunque es ineficiente hacer dos veces lo mismo, SI da mas claridad
    /// </summary>
    /// <param name="a_mutation"></param>
    /// <returns></returns>
    public int locateMutationLine(AMutation a_mutation) {
        int m_result = -1;
        int m_CALineslength = this.caLinesIndex.length;
        AtomLine m_alaux;
        for (int index = 0; ((index < m_CALineslength) && (m_result == -1)); index++) {
            m_alaux = (AtomLine) this.lines[this.caLinesIndex[index]];
            if (m_alaux.GetAminoNumber().equals(a_mutation.MutationPosition)) {
                if (m_alaux.GetAminoType().equals(a_mutation.OriginalAmino)) {
                    m_result = this.caLinesIndex[index];
                }

            }//if(m_alaux.AminoNumber.equals(a_mutation.MutationPosition))
        }//for(int index=0;((index<m_CALineslength) && (m_result==true));index++)
        return m_result;
    }//public int locateMutationLine(AMutation a_mutation)

    private int[] ToInt(ArrayList a_AL) {
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
        AtomLine[] m_CALines = this.getCALines();
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
