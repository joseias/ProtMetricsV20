package protmetrics.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;

import protmetrics.dao.AtomLine;
import protmetrics.dao.PdbClass;
import protmetrics.dao.PropertyMatrix;
import protmetrics.dao.PropertyVector;
import protmetrics.dao.PropertyVectorElement;
/// <summary>
/// Summary description for DataLoader.
/// </summary>

public class DataLoader {

    public DataLoader() {

    }

    public static PropertyMatrix loadPropertyMatrix(String a_filePath) throws Exception {

        try {

            LineNumberReader m_sr = new LineNumberReader(new FileReader(a_filePath));
            //System.IO.StreamReader m_sr=new System.IO.StreamReader(a_filePath);
            String m_actualLine;
            String[] m_actualLineElements;
            char[] m_sep = {' '};
            //PropertyMatrix m_result;
            PropertyVector[] m_vectors;

            m_actualLine = m_sr.readLine();
            if (m_actualLine != null) {
                //-> Obtener los indices de las propiedades.
                m_actualLineElements = m_actualLine.trim().split("[\\s]+", 0);
                m_actualLineElements = BioUtils.ProcSplitString(m_actualLineElements);
                m_vectors = new PropertyVector[m_actualLineElements.length];

                m_actualLine = m_sr.readLine();

                //-> Obtener los nombres de las propiedades
                if (m_actualLine != null) //2
                {
                    m_actualLineElements = m_actualLine.trim().split("[\\s]+", 0);
                    m_actualLineElements = BioUtils.ProcSplitString(m_actualLineElements);

                    //-> Crear los PropertyVectors
                    for (int i = 0; i < m_actualLineElements.length; i++) {
                        m_vectors[i] = new PropertyVector(m_actualLineElements[i]);
                    }

                    m_actualLine = m_sr.readLine();
                    while (m_actualLine != null) {
                        m_actualLineElements = m_actualLine.trim().split("[\\s]+", 0);
                        m_actualLineElements = BioUtils.ProcSplitString(m_actualLineElements);

                        for (int e = 3; e < m_actualLineElements.length; e++) {

                            m_vectors[e - 3].addVectorElement(new PropertyVectorElement(Integer.parseInt(m_actualLineElements[0]), m_actualLineElements[1], m_actualLineElements[2], Double.parseDouble(m_actualLineElements[e])));
                        }
                        m_actualLine = m_sr.readLine();
                    }//while(m_actualLine!=null)//2
                    return new PropertyMatrix(m_vectors);
                }//if(m_actualLine!=null)

            }//if(actualLine!=null)
        }//try
        catch (FileNotFoundException m_fnfe) {
            //m_fnfe.printStackTrace();
            throw m_fnfe;

        } catch (IOException m_ioe) {
            //m_ioe.printStackTrace();
            throw m_ioe;
        } catch (Exception m_e) {
            //m_e.printStackTrace();
            throw m_e;
        }
        return null;

    }//public static PropertyMatrix loadPropertyMatrix(String a_filePath)

    public static double[][] CreateInterCADistanceMatrix(String a_CAFileFullPath) {

        //-> a_CALocationVector : Es un vector de Nx3 que contiene la localizacion de cada CA.
        double m_x;
        double m_y;
        double m_z;

        String[] m_CADataPI;
        String[] m_CADataPJ;
        String[] m_CAData = DataLoader.ExtractCAData(a_CAFileFullPath);
        char[] m_sep = {' '};
        double[][] m_result = new double[m_CAData.length][m_CAData.length];

        for (int i = 1; i < m_CAData.length; i++) {
            for (int j = i + 1; j < m_CAData.length; j++) {
                m_CADataPI = m_CAData[i].trim().split("[\\s]+", 0);
                m_CADataPJ = m_CAData[j].trim().split("[\\s]+", 0);

                m_x = Math.pow(Double.parseDouble(m_CADataPI[3]) - Double.parseDouble(m_CADataPJ[3]), 2);
                m_y = Math.pow(Double.parseDouble(m_CADataPI[4]) - Double.parseDouble(m_CADataPJ[4]), 2);
                m_z = Math.pow(Double.parseDouble(m_CADataPI[5]) - Double.parseDouble(m_CADataPJ[5]), 2);

                m_result[i][j] = m_result[j][i] = MyMath.Round(Math.sqrt(m_x + m_y + m_z), 4);
            }//for(int j=i+1;j<m_CAData.length;j++)
        }//for(int i=1;i<m_CAData.length;i++)
        return m_result;
    }//public void CreateInterCADistanceFile(double[,] a_CALocationVector)

    public static String[][] CreateInterCASDistanceMatrix(String a_CAFileFullPath) throws Exception {

        //-> a_CALocationVector : Es un vector de Nx3 que contiene la localizacion de cada CA.
        double m_x;
        double m_y;
        double m_z;

        String[] m_CADataPI;
        String[] m_CADataPJ;
        String[] m_CADataE;
        String[] m_CAData = DataLoader.ExtractCAData(a_CAFileFullPath);
        char[] m_sep = {' '};
        String[][] m_result = new String[m_CAData.length][m_CAData.length];

        m_result[0][0] = "ELEMENTS";
        for (int f = 1; f < m_CAData.length; f++) {
            m_CADataE = m_CAData[f].trim().split("[\\s]+", 0);
            m_result[f][0] = m_result[0][f] = m_CADataE[0] + "_" + m_CADataE[1] + "_" + m_CADataE[2];
        }
        for (int i = 1; i < m_CAData.length; i++) {
            for (int j = i; j < m_CAData.length; j++) {
                m_CADataPI = m_CAData[i].trim().split("[\\s]+", 0);
                m_CADataPJ = m_CAData[j].trim().split("[\\s]+", 0);

                m_x = Math.pow(Double.parseDouble(m_CADataPI[3]) - Double.parseDouble(m_CADataPJ[3]), 2);
                m_y = Math.pow(Double.parseDouble(m_CADataPI[4]) - Double.parseDouble(m_CADataPJ[4]), 2);
                m_z = Math.pow(Double.parseDouble(m_CADataPI[5]) - Double.parseDouble(m_CADataPJ[5]), 2);

                m_result[i][j] = m_result[j][i] = Double.toString(MyMath.Round(Math.sqrt(m_x + m_y + m_z), 4));
            }//for(int j=i+1;j<m_CAData.length;j++)
        }//for(int i=1;i<m_CAData.length;i++)
        return m_result;
    }//public void CreateInterCADistanceFile(double[,] a_CALocationVector)

    public static String[][] CreateInterCASDistanceMatrixN(String a_CAFileFullPath) throws Exception {

        //-> a_CALocationVector : Es un vector de Nx3 que contiene la localizacion de cada CA.
        double m_x;
        double m_y;
        double m_z;

        String[] m_CADataPI;
        String[] m_CADataPJ;
        String[] m_CADataE;
        PdbClass m_pdb = null;

        m_pdb = new PdbClass(a_CAFileFullPath);

        AtomLine[] m_CAData = m_pdb.getCALines();

        char[] m_sep = {' '};
        String[][] m_result = new String[m_CAData.length + 1][m_CAData.length + 1];

        m_result[0][0] = "ELEMENTS";
        for (int f = 0; f < m_CAData.length; f++) {
            //El arreglo de CA comienza en 0
            m_result[f + 1][0] = m_result[0][f + 1] = (f + 1) + "_" + m_CAData[f].GetAtomType() + "_" + m_CAData[f].GetAminoType();

        }
        for (int i = 0; i < m_CAData.length; i++) {
            for (int j = i; j < m_CAData.length; j++) {
                //m_CADataPI=m_CAData[i].trim().split("[\\s]+",0);
                //m_CADataPJ=m_CAData[j].trim().split("[\\s]+",0);

                m_x = Math.pow(m_CAData[i].GetLocation().X - m_CAData[j].GetLocation().X, 2);
                //System.out.println("XLocation["+i+"]"+m_CAData[i].GetLocation().X);
                //System.out.println("XLocation["+j+"]"+m_CAData[j].GetLocation().X);
                m_y = Math.pow(m_CAData[i].GetLocation().Y - m_CAData[j].GetLocation().Y, 2);
                m_z = Math.pow(m_CAData[i].GetLocation().Z - m_CAData[j].GetLocation().Z, 2);
                //System.out.println("X "+m_x+" Y "+m_y+" Z "+m_z);
                m_result[i + 1][j + 1] = m_result[j + 1][i + 1] = Double.toString(MyMath.Round(Math.sqrt(m_x + m_y + m_z), 4));
            }//for(int j=i+1;j<m_CAData.length;j++)
        }//for(int i=1;i<m_CAData.length;i++)
        return m_result;
    }//public static string[,] CreateInterCASDistanceMatrixNew(string a_CAFileFullPath)
    /// <summary>
    /// Retorna un String[] que representan los datos de cada carbono alfa
    /// en el siguiente formato
    /// Nota-> El arreglo contiene los datos a partir de la posicion 1.
    /// [Nro_CA] CA [Aminoacido] [Coord X] [Coord Y] [Coord Z]
    ///
    /// </summary>
    /// <param name="a_CAFileFullPath">
    /// Direccion del fichero .pdb que contiene los datos de la proteina.
    /// </param>
    /// <returns></returns>

    public static String[] ExtractCAData(String a_CAFileFullPath) {

        String[] m_result = null;
        try {

            ArrayList m_CAData = new ArrayList(); //-> Contiene el String de datos de cada CA provicionalmente.
            LineNumberReader m_sr = new LineNumberReader(new FileReader(a_CAFileFullPath));
            String[] m_tokens;

            char[] m_sep = {' '};

            int m_cca = 1; //-> Cantidad de CA procesados.

            String m_Data; //-> String que se guardara en m_CAData.
            String m_actualLine = m_sr.readLine();

            while (m_actualLine != null) {
                //-> Debug
                //System.out.println("Pdb Line->"+m_actualLine);
                m_tokens = BioUtils.ProcSplitString(m_actualLine.trim().split("[\\s]+", 0));
                if (m_tokens.length != 0) {
                    //-> Debug
                    //System.out.println("IsAtom->"+m_tokens[0]);
                    if (m_tokens[0].equals("ATOM")) {
                        switch (m_tokens.length) {
                            case 11:

                                if (m_tokens[2].equals("CA")) {
                                    m_Data = m_cca + " " + m_tokens[2] + " " + m_tokens[3] + " " + m_tokens[5] + " " + m_tokens[6] + " " + m_tokens[7];
                                    m_CAData.add(m_Data);
                                    m_cca++;
                                }//if(m_tokens[2].equals("CA"))

                                break;

                            case 12:

                                if (m_tokens[2].equals("CA")) {
                                    m_Data = m_cca + " " + m_tokens[2] + " " + m_tokens[3] + " " + m_tokens[6] + " " + m_tokens[7] + " " + m_tokens[8];
                                    m_CAData.add(m_Data);
                                    m_cca++;
                                }//if(m_tokens[2].equals("CA"))

                                break;
                        }

                    }//if(m_tokens[0].equals("ATOM"))
                }//if(m_tokens.length!=0)
                m_actualLine = m_sr.readLine();
            }//while(m_sr.CanRead!=true)
            m_sr.close();
            int m_resultLength = m_CAData.size() + 1;
            m_result = new String[m_CAData.size() + 1];
            for (int i = 0; i < m_CAData.size(); i++) {
                m_result[i + 1] = (String) m_CAData.get(i);
            }
        }//try
        catch (FileNotFoundException m_fnfe) {
            m_fnfe.printStackTrace();
        } catch (IOException m_ioe) {
            m_ioe.printStackTrace();
        } catch (Exception m_e) {
            m_e.printStackTrace();
        }

        return m_result;
    }//public String[] ExtractCAData(String a_CAFileFullPath)

    public static PropertyVector[] InitMVector(int a_vectorlength) {
        PropertyVector[] m_result = new PropertyVector[a_vectorlength];
        for (int e = 0; e < m_result.length; e++) {
            m_result[e] = new PropertyVector("Esto no sirvio");
        }
        return m_result;
    }//public PropertyVector[] InitMVector(int a_vectorlength)
}
