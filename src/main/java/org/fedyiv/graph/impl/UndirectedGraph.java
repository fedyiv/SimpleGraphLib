package org.fedyiv.graph.impl;

import org.fedyiv.graph.AbstractGraph;


public class UndirectedGraph<T> extends AbstractGraph<T> {


    @Override
    public void addEdge(T vertex1, T vertex2) {
        var vertexWrapper1 = getOrCreateVertexWrapper(vertex1);
        var vertexWrapper2 = getOrCreateVertexWrapper(vertex2);

        vertexWrapper1.addAdjacentVertex(vertexWrapper2);
        vertexWrapper2.addAdjacentVertex(vertexWrapper1);
    }

}
