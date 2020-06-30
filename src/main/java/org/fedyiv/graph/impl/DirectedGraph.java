package org.fedyiv.graph.impl;

import org.fedyiv.graph.AbstractGraph;


public class DirectedGraph<T> extends AbstractGraph<T> {

    @Override
    public void addEdge(T vertex1, T vertex2) {

        rwl.writeLock().lock();
        try {

            var vertexWrapper1 = getOrCreateVertexWrapper(vertex1);
            var vertexWrapper2 = getOrCreateVertexWrapper(vertex2);

            vertexWrapper1.addAdjacentVertex(vertexWrapper2);
        } finally {
            rwl.writeLock().unlock();
        }
    }

}
