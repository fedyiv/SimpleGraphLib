package org.fedyiv.graph;

import org.fedyiv.graph.impl.DirectedGraph;
import org.fedyiv.graph.impl.UndirectedGraph;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DirectedGraphTest {

    @Test
    public void testSingleVertexCanBeAdded() {

        Graph<Integer> graph = new DirectedGraph<>();

        final Integer vertex = 1;

        assertFalse(graph.containsVertex(vertex));
        assertEquals(0, graph.numberOfVertices());

        graph.addVertex(1);

        assertTrue(graph.containsVertex(vertex));
        assertEquals(1, graph.numberOfVertices());


    }

    @Test
    public void testDuplicateVertexIsNotAdded() {
        Graph<Integer> graph = new DirectedGraph<>();

        final Integer vertex = 1;

        assertFalse(graph.containsVertex(vertex));

        graph.addVertex(vertex);
        graph.addVertex(vertex);

        assertTrue(graph.containsVertex(vertex));
        assertEquals(1, graph.numberOfVertices());
    }

    @Test
    public void testEdgeBetweenTwoExistingVertecesCreation() {
        Graph<Integer> graph = new DirectedGraph<>();

        final Integer vertex1 = 1;
        final Integer vertex2 = 2;

        graph.addVertex(vertex1);
        graph.addVertex(vertex2);

        graph.addEdge(vertex1, vertex2);

        assertTrue(graph.containsEdge(vertex1, vertex2));
        assertFalse(graph.containsEdge(vertex2, vertex1));

    }

    @Test
    public void testEdgeBetweenTwoNonExistingVertecesCreation() {
        Graph<Integer> graph = new DirectedGraph<>();

        final Integer vertex1 = 1;
        final Integer vertex2 = 2;

        graph.addVertex(vertex1);

        graph.addEdge(vertex1, vertex2);

        assertTrue(graph.containsEdge(vertex1, vertex2));
    }

    @Test
    public void testEdgeBetweenOneExistingAndOneNotExistingVertecesCreation() {
        Graph<Integer> graph = new DirectedGraph<>();

        final Integer vertex1 = 1;
        final Integer vertex2 = 2;


        graph.addEdge(vertex1, vertex2);

        assertTrue(graph.containsEdge(vertex1, vertex2));
    }

    @Test
    public void testDuplicateEdgeCreation() {
        Graph<Integer> graph = new DirectedGraph<>();

        final Integer vertex1 = 1;
        final Integer vertex2 = 2;

        graph.addEdge(vertex1, vertex2);
        graph.addEdge(vertex1, vertex2);

        assertTrue(graph.containsEdge(vertex1, vertex2));
        assertFalse(graph.containsEdge(vertex2, vertex1));

        assertEquals(1, graph.numberOfOutgoingEdgesFromVertex(vertex1));
        assertEquals(0, graph.numberOfOutgoingEdgesFromVertex(vertex2));
    }

    @Test
    public void testEdgeInOppositeDirectionBetweenTwoNodesCreation() {
        Graph<Integer> graph = new DirectedGraph<>();

        final Integer vertex1 = 1;
        final Integer vertex2 = 2;

        graph.addEdge(vertex1, vertex2);
        graph.addEdge(vertex2, vertex1);

        assertTrue(graph.containsEdge(vertex1, vertex2));
        assertTrue(graph.containsEdge(vertex2, vertex1));

        assertEquals(1, graph.numberOfOutgoingEdgesFromVertex(vertex1));
        assertEquals(1, graph.numberOfOutgoingEdgesFromVertex(vertex2));
    }

    @Test
    public void testGetPathForLinearGraph() {
        /*
         *   1 -> 2 ->  3
         * */
        Graph<Integer> graph = new DirectedGraph<>();

        final Integer vertex1 = 1;
        final Integer vertex2 = 2;
        final Integer vertex3 = 3;

        final List<Integer> expectedPath1 = List.of(vertex1);
        final List<Integer> expectedPath2 = List.of(vertex1, vertex2);
        final List<Integer> expectedPath3 = List.of(vertex1, vertex2, vertex3);

        graph.addEdge(vertex1, vertex2);
        graph.addEdge(vertex2, vertex3);


        assertEquals(expectedPath1, graph.getPath(vertex1, vertex1));
        assertEquals(expectedPath2, graph.getPath(vertex1, vertex2));
        assertEquals(expectedPath3, graph.getPath(vertex1, vertex3));
        assertNull(graph.getPath(vertex3, vertex1));
        assertNull(graph.getPath(vertex2, vertex1));
    }

    @Test
    public void testGetPathForPartitionedLinearGraph() {
        /*
         *   1 -> 2    3
         * */
        Graph<Integer> graph = new DirectedGraph<>();

        final Integer vertex1 = 1;
        final Integer vertex2 = 2;
        final Integer vertex3 = 3;


        graph.addEdge(vertex1, vertex2);

        graph.addVertex(vertex3);

        assertNull(graph.getPath(vertex1, vertex3));
        assertNull(graph.getPath(vertex3, vertex1));
    }

    @Test
    public void testGetPathForCircularGraph() {
        /*
         *   1 -> 2 - > 3
         *   ^          |
         *   | _  _  _  |
         * */

        Graph<Integer> graph = new DirectedGraph<>();

        final Integer vertex1 = 1;
        final Integer vertex2 = 2;
        final Integer vertex3 = 3;

        final List<Integer> expectedPath1 = List.of(vertex1, vertex2, vertex3);
        final List<Integer> expectedPath2 = List.of(vertex3, vertex1);

        graph.addEdge(vertex1, vertex2);
        graph.addEdge(vertex2, vertex3);
        graph.addEdge(vertex3, vertex1);


        assertEquals(expectedPath1, graph.getPath(vertex1, vertex3));
        assertEquals(expectedPath2, graph.getPath(vertex3, vertex1));

    }

    @Test
    public void testGetPathForTreeGraph() {
        /*
         *    1
         *  / | \
         * v  v  v
         * 2  3  4
         * */

        Graph<Integer> graph = new DirectedGraph<>();

        final Integer vertex1 = 1;
        final Integer vertex2 = 2;
        final Integer vertex3 = 3;
        final Integer vertex4 = 4;


        final List<Integer> expectedPath1 = List.of(vertex1, vertex2);


        graph.addEdge(vertex1, vertex2);
        graph.addEdge(vertex1, vertex3);
        graph.addEdge(vertex1, vertex4);


        assertEquals(expectedPath1, graph.getPath(vertex1, vertex2));
        assertNull(graph.getPath(vertex2, vertex4));

    }

    @Test
    public void testGetPathForMeshGraph() {
        /*
         * 1->2->3
         * |  |  |
         * v  v  v
         * 4->5->6
         * |  |  |
         * v  v  v
         * 7->8->9
         * */

        Graph<Integer> graph = new DirectedGraph<>();

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
        assertNull(graph.getPath(vertex9, vertex1));

    }

    @Test
    public void testTraverse() {

        /*
         *   1 -> 2 ->  3
         * */
        Graph<Integer> graph = new UndirectedGraph<>();

        final Integer vertex1 = 1;
        final Integer vertex2 = 2;
        final Integer vertex3 = 3;

        final Integer vertex1transformed = 2;
        final Integer vertex2transformed = 4;
        final Integer vertex3transformed = 6;


        final List<Integer> expectedPath1 = List.of(vertex1transformed, vertex2transformed, vertex3transformed);

        graph.addEdge(vertex1, vertex2);
        graph.addEdge(vertex2, vertex3);

        graph.traverse(vertex -> vertex * 2);


        assertEquals(expectedPath1, graph.getPath(vertex1transformed, vertex3transformed));

    }

}