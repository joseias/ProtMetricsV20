package protmetrics.utils.filters;

import java.util.List;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedPseudograph;

import protmetrics.dao.files.XYZ.GAtom;

public abstract class ExtAtomsFilter {
	public abstract List<GAtom> getExteriorVertices(WeightedPseudograph<GAtom,DefaultWeightedEdge> graph);
}
