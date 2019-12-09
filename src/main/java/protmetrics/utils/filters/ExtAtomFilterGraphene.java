package protmetrics.utils.filters;

import java.util.HashSet;
import java.util.Set;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedPseudograph;

import protmetrics.dao.files.xyz.GAtom;

public class ExtAtomFilterGraphene extends ExtAtomsFilter {

    @Override
    public Set<GAtom> getExteriorVertices(WeightedPseudograph<GAtom, DefaultWeightedEdge> graph) {
        Set<GAtom> result = new HashSet<>(graph.vertexSet().size());
        for (GAtom vertex : graph.vertexSet()) {
            if (isExternal(graph, vertex)) {
                boolean add = result.add(vertex);
            }
        }

        return result;
    }

    private boolean isExternal(WeightedPseudograph<GAtom, DefaultWeightedEdge> graph, GAtom vertex) {
        GAtom targetAtom;
        int extCount = 0;
        if (graph.degreeOf(vertex) == 2) {
            return true;
        } else {
            if (graph.degreeOf(vertex) == 3) {
                Set<DefaultWeightedEdge> edgesOf = graph.edgesOf(vertex);
                for (DefaultWeightedEdge edge : edgesOf) {
                    targetAtom = graph.getEdgeTarget(edge).equals(vertex) ? graph.getEdgeSource(edge) : graph.getEdgeTarget(edge);
                    extCount = graph.degreeOf(targetAtom) == 2 ? extCount + 1 : extCount;
                }
            }
        }

        return extCount >= 2;
    }
}
