/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package protmetrics.dao.dm;

import java.util.HashMap;
import java.util.Set;

/**
 * Represent an instance in matrix data (column or row)
 * @author Docente
 */
public class DMInstance {
    
    HashMap<DMAtt, DMAttValue> attValues;
    String instID;
    
    public DMInstance(String id){
        this.instID = id;
        attValues = new HashMap<>();
    }
    
    public void setAttValue(DMAtt att, DMAttValue value){
        attValues.put(att, value);
    }
    
    public DMAttValue getAttValue(DMAtt att){
          return attValues.get(att);
    }
    
    public Set<DMAtt> getAtts(){
        return attValues.keySet();
    }
    
    public String getInstID(){
        return this.instID;
    }
}
