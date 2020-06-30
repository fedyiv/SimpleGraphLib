package org.fedyiv.graph;

import java.util.List;

public interface Graph<T> {

    void addVertex(T vertex);
    void addEdge(T vertex1, T vertex2);
    List<T> getPath(T vertex1, T vertex2);

    boolean containsVertex(T vertex);
    boolean containsEdge(T vertex1, T vertex2);
    int numberOfVertices();
    int numberOfOutgoingEdgesWithFromVertex(T vertex);

}
