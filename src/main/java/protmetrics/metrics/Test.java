/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package protmetrics.metrics;

import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.io.PDBFileReader;

/**
 *
 * @author Docente
 */
public class Test {
    
    public static void main(String[] args){

        String filename =  ".\\PDBs\\mol_1.pdb" ;

        PDBFileReader pdbreader = new PDBFileReader();

        try{
            Structure struc = pdbreader.getStructure(filename);
            int i=0;
 
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
}
