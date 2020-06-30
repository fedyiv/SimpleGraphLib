package org.fedyiv.graph;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;

public abstract class AbstractGraph<T> implements Graph<T> {

    protected final ReadWriteLock rwl = new ReentrantReadWriteLock();

    protected final Set<VertexWrapper<T>> graph = new HashSet<>();

    protected static class VertexWrapper<T> {
        private T value;
        private final Set<VertexWrapper<T>> adjacentVertices;


        public VertexWrapper(T value) {
            this.value = value;
            adjacentVertices = new HashSet<>();
        }

        public void addAdjacentVertex(VertexWrapper<T> adjacentVertexWrapper) {
            adjacentVertices.add(adjacentVertexWrapper);
        }

        public Set<VertexWrapper<T>> getAdjacentVertices() {
            return adjacentVertices;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public boolean isAdjacent(VertexWrapper<T> other) {
            return adjacentVertices.contains(other);
        }
    }

    @Override
    public void addVertex(T vertex) {
        rwl.writeLock().lock();
        try {
            var existingVertexWrapper = getVertexWrapper(vertex);

            if (existingVertexWrapper == null) {
                graph.add(new VertexWrapper<>(vertex));
            }
        } finally {
            rwl.writeLock().unlock();
        }

    }

    /**
     * Uses Breadth-first search algorithm to find the shortest path between two vertexes
     */
    @Override
    public List<T> getPath(T vertex1, T vertex2) {

        rwl.readLock().lock();

        try {
            var vertexWrapper1 = getVertexWrapper(vertex1);
            var vertexWrapper2 = getVertexWrapper(vertex2);

            if (vertexWrapper1 == null || vertexWrapper2 == null)
                throw new IllegalArgumentException("One  or both of verteces do not exist (" + vertex1 + ", " + vertex2 + ")");

            Queue<AbstractMap.Entry<VertexWrapper<T>, List<T>>> vertecesToVisitWithFullPathQueue = new LinkedList<>();
            Set<VertexWrapper<T>> visitedVertices = new HashSet<>();

            List<T> initialPath = new ArrayList<>();
            initialPath.add(vertexWrapper1.getValue());
            vertecesToVisitWithFullPathQueue.add(new AbstractMap.SimpleEntry<>(vertexWrapper1, initialPath));


            while (vertecesToVisitWithFullPathQueue.peek() != null) {

                var currentVertexWithPath = vertecesToVisitWithFullPathQueue.remove();

                var currentVertex = currentVertexWithPath.getKey();
                var currentPath = currentVertexWithPath.getValue();

                visitedVertices.add(currentVertex);

                if (currentVertex.equals(vertexWrapper2))
                    return currentPath;

                if (currentVertex.equals(vertexWrapper1) && currentPath.size() != 1)
                    continue;

                for (VertexWrapper<T> adjacentVertex : currentVertex.getAdjacentVertices()) {
                    if (!visitedVertices.contains(adjacentVertex)) {
                        List<T> newPath = new ArrayList<>(currentPath);
                        newPath.add(adjacentVertex.getValue());
                        vertecesToVisitWithFullPathQueue.add(new AbstractMap.SimpleEntry<>(adjacentVertex, newPath));
                    }
                }
            }

            return null;
        } finally {
            rwl.readLock().unlock();
        }
    }

    protected VertexWrapper<T> getVertexWrapper(T vertex) {
        return graph.stream().filter(it -> it.getValue().equals(vertex)).reduce((a, b) -> {
            throw new IllegalStateException("Multiple elements: " + a + ", " + b);
        }).orElse(null);
    }

    protected VertexWrapper<T> getOrCreateVertexWrapper(T vertex) {
        var existingVertexWrapper = getVertexWrapper(vertex);

        if (existingVertexWrapper != null) {
            return existingVertexWrapper;
        } else {
            var newVertexWrapper = new VertexWrapper<>(vertex);
            graph.add(newVertexWrapper);
            return newVertexWrapper;
        }
    }

    @Override
    public boolean containsVertex(T vertex) {
        rwl.readLock().lock();
        try {
            return getVertexWrapper(vertex) != null;
        } finally {
            rwl.readLock().unlock();
        }
    }

    @Override
    public boolean containsEdge(T vertex1, T vertex2) {
        rwl.readLock().lock();
        try {

            var vertexWrapper1 = getVertexWrapper(vertex1);
            var vertexWrapper2 = getVertexWrapper(vertex2);
            if (vertexWrapper1 == null || vertexWrapper2 == null)
                return false;
            return vertexWrapper1.isAdjacent(vertexWrapper2);
        } finally {
            rwl.readLock().unlock();
        }
    }

    @Override
    public int numberOfVertices() {
        rwl.readLock().lock();
        try {
            return graph.size();
        } finally {
            rwl.readLock().unlock();
        }
    }

    @Override
    public int numberOfOutgoingEdgesFromVertex(T vertex) {
        rwl.readLock().lock();
        try {

            var vertexWrapper = getVertexWrapper(vertex);
            if (vertexWrapper == null)
                throw new IllegalArgumentException("No edge " + vertex);

            return vertexWrapper.getAdjacentVertices().size();
        } finally {
            rwl.readLock().unlock();
        }

    }

    public void traverse(Function<T, T> func) {
        rwl.readLock().lock();
        try {
            for (VertexWrapper<T> vertexWrapper : graph) {
                T initialValue = vertexWrapper.getValue();
                var changedValue = func.apply(initialValue);
                vertexWrapper.setValue(changedValue);
            }
        } finally {
            rwl.readLock().unlock();
        }

    }


}
