package protmetrics.dao;
/// <summary>
/// Summary description for PropertyMatrix.
/// </summary>

public class PropertyMatrix {

    public PropertyVector[] PropertyVectorsColumns;

    public PropertyMatrix(PropertyVector[] a_matrixColums) {
        this.PropertyVectorsColumns = a_matrixColums;
    }

    public PropertyMatrix getSubPropertyMatrix(int[] a_selectedPVC) {
        /*Devuelve una nueva PropertyMatrix cuyo PropertyVectorsColumns es 
            un subconjunto de los de esta, especificados por los valores en el arreglo*/
        int m_length = a_selectedPVC.length;
        PropertyVector[] m_PVC = new PropertyVector[m_length];

        for (int i = 0; i < m_length; i++) {
            /*Ahora no se chequeara que el valor indicado sea menor que la longitud
                de PropertyVectorColumns*/
            m_PVC[i] = this.PropertyVectorsColumns[a_selectedPVC[i]];
        }
        return new PropertyMatrix(m_PVC);
    }

}//PropertyMatrix Class
