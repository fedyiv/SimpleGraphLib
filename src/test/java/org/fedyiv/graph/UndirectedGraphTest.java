package org.fedyiv.graph;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UndirectedGraphTest {

    @Test
    public void testSingleVertexCanBeAdded() {

        Graph<Integer> graph = new UndirectedGraph<>();

        final Integer vertex = 1;

        assertFalse(graph.containsVertex(vertex));
        assertEquals(0, graph.numberOfVertices());

        graph.addVertex(1);

        assertTrue(graph.containsVertex(vertex));
        assertEquals(1, graph.numberOfVertices());


    }

    @Test
    public void testDuplicateVertexIsNotAdded() {
        Graph<Integer> graph = new UndirectedGraph<>();

        final Integer vertex = 1;

        assertFalse(graph.containsVertex(vertex));

        graph.addVertex(vertex);
        graph.addVertex(vertex);

        assertTrue(graph.containsVertex(vertex));
        assertEquals(1, graph.numberOfVertices());
    }

    @Test
    public void testEdgeBetweenTwoExistingVertecesCreation() {
        Graph<Integer> graph = new UndirectedGraph<>();

        final Integer vertex1 = 1;
        final Integer vertex2 = 2;

        graph.addVertex(vertex1);
        graph.addVertex(vertex2);

        graph.addEdge(vertex1, vertex2);

        assertTrue(graph.containsEdge(vertex1, vertex2));

    }

    @Test
    public void testEdgeBetweenTwoNonExistingVertecesCreation() {
        Graph<Integer> graph = new UndirectedGraph<>();

        final Integer vertex1 = 1;
        final Integer vertex2 = 2;

        graph.addVertex(vertex1);

        graph.addEdge(vertex1, vertex2);

        assertTrue(graph.containsEdge(vertex1, vertex2));
    }

    @Test
    public void testEdgeBetweenOneExistingAndOneNotExistingVertecesCreation() {
        Graph<Integer> graph = new UndirectedGraph<>();

        final Integer vertex1 = 1;
        final Integer vertex2 = 2;


        graph.addEdge(vertex1, vertex2);

        assertTrue(graph.containsEdge(vertex1, vertex2));
    }

    @Test
    public void testDuplicateEdgeCreation() {
        Graph<Integer> graph = new UndirectedGraph<>();

        final Integer vertex1 = 1;
        final Integer vertex2 = 2;

        graph.addEdge(vertex1, vertex2);
        graph.addEdge(vertex2, vertex1);

        assertTrue(graph.containsEdge(vertex1, vertex2));
        assertTrue(graph.containsEdge(vertex2, vertex1));

        assertEquals(1, graph.numberOfVerticesConnectedWithVertex(vertex1));
        assertEquals(1, graph.numberOfVerticesConnectedWithVertex(vertex2));
    }

    @Test
    public void testGetPathForLinearGraph() {
        /*
         *   1 - 2 -  3
         * */
        Graph<Integer> graph = new UndirectedGraph<>();

        final Integer vertex1 = 1;
        final Integer vertex2 = 2;
        final Integer vertex3 = 3;

        final List<Integer> expectedPath1 = List.of(vertex1);
        final List<Integer> expectedPath2 = List.of(vertex1, vertex2);
        final List<Integer> expectedPath3 = List.of(vertex1, vertex2, vertex3);

        graph.addEdge(vertex1, vertex2);
        ;
        graph.addEdge(vertex2, vertex3);


        assertEquals(expectedPath1, graph.getPath(vertex1, vertex1));
        assertEquals(expectedPath2, graph.getPath(vertex1, vertex2));
        assertEquals(expectedPath3, graph.getPath(vertex1, vertex3));
    }

    @Test
    public void testGetPathForPartitionedLinearGraph() {
        /*
         *   1 - 2    3
         * */
        Graph<Integer> graph = new UndirectedGraph<>();

        final Integer vertex1 = 1;
        final Integer vertex2 = 2;
        final Integer vertex3 = 3;


        graph.addEdge(vertex1, vertex2);
        ;
        graph.addVertex(vertex3);

        assertNull(graph.getPath(vertex1, vertex3));
    }

    @Test
    public void testGetPathForCircularGraph() {
        /*
         *   1
         *  / \
         * 2 - 3
         * */

        Graph<Integer> graph = new UndirectedGraph<>();

        final Integer vertex1 = 1;
        final Integer vertex2 = 2;
        final Integer vertex3 = 3;

        final List<Integer> expectedPath1 = List.of(vertex1, vertex3);

        graph.addEdge(vertex1, vertex2);
        ;
        graph.addEdge(vertex2, vertex3);
        graph.addEdge(vertex3, vertex1);


        assertEquals(expectedPath1, graph.getPath(vertex1, vertex3));

    }

    @Test
    public void testGetPathForTreeGraph() {
        /*
         *   1
         *  /|\
         * 2 3 4
         * */

        Graph<Integer> graph = new UndirectedGraph<>();

        final Integer vertex1 = 1;
        final Integer vertex2 = 2;
        final Integer vertex3 = 3;
        final Integer vertex4 = 4;


        final List<Integer> expectedPath1 = List.of(vertex1, vertex2);
        final List<Integer> expectedPath2 = List.of(vertex2, vertex1, vertex4);

        graph.addEdge(vertex1, vertex2);
        ;
        graph.addEdge(vertex1, vertex3);
        graph.addEdge(vertex1, vertex4);


        assertEquals(expectedPath1, graph.getPath(vertex1, vertex2));
        assertEquals(expectedPath2, graph.getPath(vertex2, vertex4));

    }

    @Test
    public void testGetPathForMeshGraph() {
        /*
         * 1-2-3
         * | | |
         * 4-5-6
         * | | |
         * 7-8-9
         * */

        Graph<Integer> graph = new UndirectedGraph<>();

        final Integer vertex1 = 1;
        final Integer vertex2 = 2;
        final Integer vertex3 = 3;
        final Integer vertex4 = 4;
        final Integer vertex5 = 5;
        final Integer vertex6 = 6;
        final Integer vertex7 = 7;
        final Integer vertex8 = 8;
        final Integer vertex9 = 9;


        graph.addEdge(vertex1, vertex2);
        graph.addEdge(vertex1, vertex4);
        graph.addEdge(vertex2, vertex3);
        graph.addEdge(vertex2, vertex5);
        graph.addEdge(vertex3, vertex6);
        graph.addEdge(vertex4, vertex5);
        graph.addEdge(vertex4, vertex7);
        graph.addEdge(vertex5, vertex6);
        graph.addEdge(vertex5, vertex8);
        graph.addEdge(vertex6, vertex9);
        graph.addEdge(vertex7, vertex8);
        graph.addEdge(vertex8, vertex9);

        assertEquals(9, graph.numberOfVertices());

        assertEquals(5, graph.getPath(vertex1, vertex9).size());

    }

}