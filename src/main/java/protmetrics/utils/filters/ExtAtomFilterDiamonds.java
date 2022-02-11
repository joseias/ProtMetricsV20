package protmetrics.utils.filters;

import java.util.HashSet;
import java.util.Set;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedPseudograph;
import protmetrics.dao.files.xyz.GAtom;

/**
 * Utility class to get only exterior vertices diamond structures.
 */
public class ExtAtomFilterDiamonds implements ExtAtomsFilter {

    /**
     * @param graph the molecule graph.
     * @return set of exterior atoms.
     */
    @Override
    public Set<GAtom> getExteriorVertices(WeightedPseudograph<GAtom, DefaultWeightedEdge> graph) {
        Set<GAtom> result = new HashSet<>(graph.vertexSet().size());
        for (GAtom vertex : graph.vertexSet()) {
            if (isExterior(graph, vertex)) {
                result.add(vertex);
            }
        }

        return result;
    }

    /**
     * Determines if carbon is exterior, i.e. have degree 3, or have degree 4
     * but don�t have 4 neighbors of degree 4.
     *
     * @param graph the molecule graph.
     * @param vertex the vertex to check if it is exterior.
     * @return if the vertex is exterior.
     */
    public boolean isExterior(WeightedPseudograph<GAtom, DefaultWeightedEdge> graph, GAtom vertex) {
        GAtom targetAtom;
        int extCount = 0;
        if (graph.degreeOf(vertex) <= 3) {
            return true;
        } else {
            if (graph.degreeOf(vertex) == 4) {
                Set<DefaultWeightedEdge> edgesOf = graph.edgesOf(vertex);

                /* determines if all neighbors have degree 4*/
                for (DefaultWeightedEdge edge : edgesOf) {
                    targetAtom = graph.getEdgeTarget(edge).equals(vertex) ? graph.getEdgeSource(edge) : graph.getEdgeTarget(edge);
                    extCount = graph.degreeOf(targetAtom) == 4 ? extCount + 1 : extCount;
                }
            }
        }

        return extCount < 4;
    }
}
