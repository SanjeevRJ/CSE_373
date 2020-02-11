package datastructures.concrete;

import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IEdge;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import datastructures.interfaces.IDisjointSet;
import datastructures.interfaces.IPriorityQueue;
import misc.Sorter;
import misc.exceptions.NoPathExistsException;

/**
 * Represents an undirected, weighted graph, possibly containing self-loops, parallel edges,
 * and unconnected components.
 *
 * Note: This class is not meant to be a full-featured way of representing a graph.
 * We stick with supporting just a few, core set of operations needed for the
 * remainder of the project.
 */
public class Graph<V, E extends IEdge<V> & Comparable<E>> {
    // NOTE 1:
    //
    // Feel free to add as many fields, private helper methods, and private
    // inner classes as you want.
    //
    // And of course, as always, you may also use any of the data structures
    // and algorithms we've implemented so far.
    //
    // Note: If you plan on adding a new class, please be sure to make it a private
    // static inner class contained within this file. Our testing infrastructure
    // works by copying specific files from your project to ours, and if you
    // add new files, they won't be copied and your code will not compile.
    //
    //
    // NOTE 2:
    //
    // You may notice that the generic types of Graph are a little bit more
    // complicated than usual.
    //
    // This class uses two generic parameters: V and E.
    //
    // - 'V' is the type of the vertices in the graph. The vertices can be
    //   any type the client wants -- there are no restrictions.
    //
    // - 'E' is the type of the edges in the graph. We've constrained Graph
    //   so that E *must* always be an instance of IEdge<V> AND Comparable<E>.
    //
    //   What this means is that if you have an object of type E, you can use
    //   any of the methods from both the IEdge interface and from the Comparable
    //   interface
    //
    // If you have any additional questions about generics, or run into issues while
    // working with them, please ask ASAP either on Piazza or during office hours.
    //
    // Working with generics is really not the focus of this class, so if you
    // get stuck, let us know we'll try and help you get unstuck as best as we can.

    private IList<E> edges;
    private IList<V> vertices;
    IDictionary<V, IList<E>> graph;
    /**
     * Constructs a new graph based on the given vertices and edges.
     *
     * @throws IllegalArgumentException if any of the edges have a negative weight
     * @throws IllegalArgumentException if one of the edges connects to a vertex not
     *                                  present in the 'vertices' list
     * @throws IllegalArgumentException if vertices or edges are null or contain null
     */
    public Graph(IList<V> vertices, IList<E> edges) {
        this.vertices = vertices;
        graph = new ChainedHashDictionary<>();
        for (V vertex : vertices) {
            if (vertex == null) {
                throw new IllegalArgumentException();
            }
            graph.put(vertex, new DoubleLinkedList<>());
        }

        this.edges = new DoubleLinkedList<>();
        for (E edge : edges) {
            if (edge == null) {
                throw new IllegalArgumentException();
            }
            if (edge.getWeight() < 0) {
                throw new IllegalArgumentException();
            }
            if (!vertices.contains(edge.getVertex1()) || !vertices.contains(edge.getVertex2())) {
                throw new IllegalArgumentException();
            }

            IList<E> list1 = graph.get(edge.getVertex1());
            IList<E> list2 = graph.get(edge.getVertex2());
            if (!list1.contains(edge)) {
                list1.add(edge);
            }
            if (!list2.contains(edge)) {
                list2.add(edge);
            }
            graph.put(edge.getVertex1(), list1);
            graph.put(edge.getVertex2(), list2);

            this.edges.add(edge);
        }
    }

    /**
     * Sometimes, we store vertices and edges as sets instead of lists, so we
     * provide this extra constructor to make converting between the two more
     * convenient.
     *
     * @throws IllegalArgumentException if any of the edges have a negative weight
     * @throws IllegalArgumentException if one of the edges connects to a vertex not
     *                                  present in the 'vertices' list
     * @throws IllegalArgumentException if vertices or edges are null or contain null
     */
    public Graph(ISet<V> vertices, ISet<E> edges) {
        // You do not need to modify this method.
        this(setToList(vertices), setToList(edges));
    }

    // You shouldn't need to call this helper method -- it only needs to be used
    // in the constructor above.
    private static <T> IList<T> setToList(ISet<T> set) {
        if (set == null) {
            throw new IllegalArgumentException();
        }
        IList<T> output = new DoubleLinkedList<>();
        for (T item : set) {
            output.add(item);
        }
        return output;
    }

    /**
     * Returns the number of vertices contained within this graph.
     */
    public int numVertices() {
        return vertices.size();
    }

    /**
     * Returns the number of edges contained within this graph.
     */
    public int numEdges() {
        return edges.size();
    }

    /**
     * Returns the set of all edges that make up the minimum spanning tree of
     * this graph.
     *
     * If there exists multiple valid MSTs, return any one of them.
     *
     * Precondition: the graph does not contain any unconnected components.
     */
    public ISet<E> findMinimumSpanningTree() {
        IDisjointSet<V> components = new ArrayDisjointSet<>();
        for (V vertex : vertices) {
            components.makeSet(vertex);
        }

        IList<E> sortedEdges = Sorter.topKSort(edges.size(), edges);
        ISet<E> mst = new ChainedHashSet<>();
        for (E edge : sortedEdges) {
            V vertex1 = edge.getVertex1();
            V vertex2 = edge.getVertex2();
            if (components.findSet(vertex1) != components.findSet(vertex2)) {
                mst.add(edge);
                components.union(vertex1, vertex2);
            }
        }
        return mst;
    }

    /**
     * Returns the edges that make up the shortest path from the start
     * to the end.
     *
     * The first edge in the output list should be the edge leading out
     * of the starting node; the last edge in the output list should be
     * the edge connecting to the end node.
     *
     * Return an empty list if the start and end vertices are the same.
     *
     * @throws NoPathExistsException  if there does not exist a path from the start to the end
     * @throws IllegalArgumentException if start or end is null
     */
    public IList<E> findShortestPathBetween(V start, V end) {
        if (start == null | end == null) {
            throw new IllegalArgumentException();
        }

        IDictionary<V, DistVertex<V, E>> distances = new ChainedHashDictionary<>();
        for (V vertex : vertices) {
            distances.put(vertex, new  DistVertex<V, E>(vertex));
        }

        distances.get(start).distance = 0.0;
        IPriorityQueue<DistVertex<V, E>> nearVerts = new ArrayHeap<>();
        nearVerts.insert(distances.get(start));

        while (!nearVerts.isEmpty()) {
            DistVertex<V, E> curDistVert = nearVerts.removeMin();
            V curVert = curDistVert.vertex;
            IList<E> curEdges = graph.get(curVert);
            for (E edge : curEdges) {
                DistVertex<V, E> otherDistVert = distances.get(edge.getOtherVertex(curVert));
                double oldDist = otherDistVert.distance;
                double newDist = edge.getWeight() + curDistVert.distance;

                if (newDist < oldDist) {
                    otherDistVert.distance = newDist;
                    otherDistVert.predecessor = curDistVert;
                    otherDistVert.shortestEdge = edge;
                }

                if (oldDist == Double.POSITIVE_INFINITY) {
                    nearVerts.insert(otherDistVert);
                }
            }
        }

        IList<E> shortestPath = new DoubleLinkedList<>();
        DistVertex<V, E> curDistVert = distances.get(end);
        while (!curDistVert.vertex.equals(start)) {
            if (curDistVert.predecessor == null) {
                throw new NoPathExistsException();
            }
            shortestPath.insert(0, curDistVert.shortestEdge);
            curDistVert = curDistVert.predecessor;

        }

        return shortestPath;
    }

    private static class DistVertex<V, E> implements Comparable<DistVertex<V, E>> {
        public Double distance;
        public DistVertex<V, E> predecessor;
        public V vertex;
        public E shortestEdge;

        public DistVertex(Double distance, V vertex) {
            this.distance = distance;
            this.vertex = vertex;
        }

        public DistVertex(V vertex) {
            this.distance = Double.POSITIVE_INFINITY;
            this.vertex = vertex;
        }

        @Override
        public int compareTo(DistVertex<V, E> o) {
            return distance.compareTo(o.distance);
        }
    }
}
