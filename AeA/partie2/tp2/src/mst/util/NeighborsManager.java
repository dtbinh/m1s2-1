package mst.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import mst.bean.Edge;
import mst.bean.Vertex;
import mst.bean.WeightedGraph;
import mst.sort.EdgesComparator;

public class NeighborsManager {

	private Map<Vertex, List<VertexNeighbor>> verticesToNeighbors;

	public NeighborsManager() {
		verticesToNeighbors = new HashMap<Vertex, List<VertexNeighbor>>();
	}

	public void addNeighbors(Vertex vertex1, Vertex vertex2, int weight) {
		List<VertexNeighbor> vertex1Neighbors = verticesToNeighbors
				.get(vertex1);
		if (vertex1Neighbors == null) {
			vertex1Neighbors = new LinkedList<VertexNeighbor>();
		}
		vertex1Neighbors.add(new VertexNeighbor(vertex2, weight));
		verticesToNeighbors.put(vertex1, vertex1Neighbors);
	}

	public List<VertexNeighbor> getNeighbors(Vertex vertex) {
		final List<VertexNeighbor> neighbors = verticesToNeighbors.get(vertex);
		if (neighbors == null) {
			return new LinkedList<VertexNeighbor>();
		}
		return neighbors;
	}

	public void addNeighbors(Edge edge) {
		addNeighbors(edge.getVertex1(), edge.getVertex2(), edge.getWeight());
		addNeighbors(edge.getVertex2(), edge.getVertex1(), edge.getWeight());
	}

	public void initNeighbors(WeightedGraph graph) {
		verticesToNeighbors.clear();
		if (graph.getEdges().isEmpty()) {
			return;
		}
		final Queue<Edge> graphEdges = new PriorityQueue<Edge>(graph.getEdges()
				.size(), new EdgesComparator());
		graphEdges.addAll(graph.getEdges());
		while (!graphEdges.isEmpty()) {
			final Edge poll = graphEdges.poll();
			addNeighbors(poll);
		}

	}

}
