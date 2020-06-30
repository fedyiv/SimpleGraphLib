package org.fedyiv.graph;

import java.util.*;

public abstract class AbstractGraph<T> implements Graph<T> {

    protected Set<VertexWrapper<T>> graph = new HashSet<>();

    protected static class VertexWrapper<T> {
        private final T value;
        private final Set<VertexWrapper<T>> adjecentVerteces;


        public VertexWrapper(T value) {
            this.value = value;
            adjecentVerteces = new HashSet<>();
        }

        public void addAdjecentVertex(VertexWrapper<T> adjecentVertexWrapper) {
            adjecentVerteces.add(adjecentVertexWrapper);
        }

        public Set<VertexWrapper<T>> getAdjecentVerteces() {
            return adjecentVerteces;
        }

        public T getValue() {
            return value;
        }

        public boolean isAdjecent(VertexWrapper<T> other) {
            return adjecentVerteces.contains(other);
        }
    }

    @Override
    public void addVertex(T vertex) {
        var existingVertexWrapper = getVertexWrapper(vertex);

        if (existingVertexWrapper == null) {
            graph.add(new VertexWrapper<>(vertex));
        }

    }

    @Override
    public List<T> getPath(T vertex1, T vertex2) {
        //TODO: Move this to separate class //BFS search

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

            for (VertexWrapper<T> adjecentVertex : currentVertex.getAdjecentVerteces()) {
                if (!visitedVertices.contains(adjecentVertex)) {
                    List<T> newPath = new ArrayList<>(currentPath);
                    newPath.add(adjecentVertex.getValue());
                    vertecesToVisitWithFullPathQueue.add(new AbstractMap.SimpleEntry<>(adjecentVertex, newPath));
                }
            }
        }

        return null;
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
        return getVertexWrapper(vertex) != null;
    }

    @Override
    public boolean containsEdge(T vertex1, T vertex2) {

        var vertexWrapper1 = getVertexWrapper(vertex1);
        var vertexWrapper2 = getVertexWrapper(vertex2);
        if (vertexWrapper1 == null || vertexWrapper2 == null)
            return false;
        return vertexWrapper1.isAdjecent(vertexWrapper2);

    }

    @Override
    public int numberOfVertices() {
        return graph.size();
    }

    @Override
    public int numberOfOutgoingEdgesWithFromVertex(T vertex) {

        var vertexWrapper = getVertexWrapper(vertex);
        if (vertexWrapper == null)
            throw new IllegalArgumentException("No edge " + vertex);

        return vertexWrapper.getAdjecentVerteces().size();

    }


}
