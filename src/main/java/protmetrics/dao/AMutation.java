package protmetrics.dao;

/*
    * Representa una mutación, especificada por el nombre
    * del aminoacido original, la posicion y el nombre del
    * nuevo aminoacido.
 */
public class AMutation {

    public String OriginalAmino;
    public String MutationPosition;
    public String NewAmino;
    public String Sequence;
    //Por ahora no se emplea, luego podria, hay que arreglarlo en BioUtil.LoadMutation

    public AMutation(String a_originalAmino, String a_position, String a_newAmino, String a_sequence) {
        OriginalAmino = a_originalAmino;
        MutationPosition = a_position;
        NewAmino = a_newAmino;
        Sequence = a_sequence;

    }

    public String toString() {
        return OriginalAmino.toString() + "_" + MutationPosition + "_" + NewAmino;
    }

}
