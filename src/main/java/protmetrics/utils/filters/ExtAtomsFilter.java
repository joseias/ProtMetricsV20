package protmetrics.utils.filters;

import java.util.Set;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedPseudograph;

import protmetrics.dao.files.xyz.GAtom;

/**
 * Abstract class for atom filters.
 */
public abstract class ExtAtomsFilter {

    /**
     * @param graph the molecule graph.
     * @return  the set of exterior vertices.
     */
    public abstract Set<GAtom> getExteriorVertices(WeightedPseudograph<GAtom, DefaultWeightedEdge> graph);
}
