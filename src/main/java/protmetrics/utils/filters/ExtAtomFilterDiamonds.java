package protmetrics.utils.filters;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedPseudograph;
import protmetrics.dao.files.XYZ.GAtom;

public class ExtAtomFilterDiamonds extends ExtAtomsFilter{

	@Override
	public  List<GAtom> getExteriorVertices(WeightedPseudograph<GAtom,DefaultWeightedEdge> graph){	
        List<GAtom> result=new ArrayList<>(graph.vertexSet().size());
        for(GAtom vertex:graph.vertexSet()){
            if(isExternal(graph, vertex)){
                boolean add = result.add(vertex);
            }
        }
        
        return result;
    }
    
	/**
	 * Determines if a carbon is external, i.e. have degree 3, or have degree 4 but don�t have 4 neighbors of degree 4.
	 * @param graph
	 * @param vertex
	 * @return
	 */
    public boolean isExternal(WeightedPseudograph<GAtom,DefaultWeightedEdge> graph, GAtom vertex){
        GAtom targetAtom;
        int extCount=0;
        if(graph.degreeOf(vertex) <= 3){
                return true;
            }
            else{
                if(graph.degreeOf(vertex) == 4){
                    Set<DefaultWeightedEdge> edgesOf=graph.edgesOf(vertex);
                    /*Determine if all neighbors have degree 4*/
                    for(DefaultWeightedEdge edge: edgesOf){
                       targetAtom=graph.getEdgeTarget(edge).equals(vertex)?graph.getEdgeSource(edge):graph.getEdgeTarget(edge);
                       extCount=graph.degreeOf(targetAtom)==4?extCount+1:extCount;
                    }
                }
            }

        return extCount<4;
    }

}