package protmetrics.utils.filters;

import java.util.Set;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedPseudograph;

import protmetrics.dao.files.XYZ.GAtom;

public abstract class ExtAtomsFilter {
	public abstract Set<GAtom> getExteriorVertices(WeightedPseudograph<GAtom,DefaultWeightedEdge> graph);
}
