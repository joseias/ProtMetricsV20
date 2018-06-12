/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package protmetrics.dao.files.XYZ;

/**
 *
 * @author sijfg
 */
public class GEdge {
    private GAtom target;
    private GAtom source;
    private double weight;

    public GEdge(GAtom source,GAtom target,double weight){
        this.source=source;
        this.target=target;
        this.weight=weight;
    }
    /**
     * @return the target
     */
    public GAtom getTarget() {
        return target;
    }

    /**
     * @param target the target to set
     */
    public void setTarget(GAtom target) {
        this.target = target;
    }

    /**
     * @return the source
     */
    public GAtom getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(GAtom source) {
        this.source = source;
    }

    /**
     * @return the weight
     */
    public double getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }
}
