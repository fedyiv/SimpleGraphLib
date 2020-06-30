package org.fedyiv.graph;

import java.util.List;
import java.util.function.Function;

public interface Graph<T> {

    void addVertex(T vertex);
    void addEdge(T vertex1, T vertex2);
    List<T> getPath(T vertex1, T vertex2);
    void traverse(Function<T,T> func);

    boolean containsVertex(T vertex);
    boolean containsEdge(T vertex1, T vertex2);
    int numberOfVertices();
    int numberOfOutgoingEdgesFromVertex(T vertex);

}
