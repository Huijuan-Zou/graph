package graph.impl;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

import graph.impl.Graph;
import graph.impl.Graph.Vertex;

public class GraphTest {
  private Graph< String, Integer> directedGraph;
  private Graph< String, Integer> emptyDirectedGraph;
  private String id1;
  private String id2;
  private String id3;
  private String id4;
  private String id5;
  private String label1;
  private String label2;
  private String label3;
  private String label4;
  private String label5;
  private HashMap<String, Integer> inList1;
  private HashMap<String, Integer> inList2;
  private HashMap<String, Integer> inList3;
  private HashMap<String, Integer> inList4;
  private HashMap<String, Integer> inList5;
  private HashMap<String, Integer> outList1;
  private HashMap<String, Integer> outList2;
  private HashMap<String, Integer> outList3;
  private HashMap<String, Integer> outList4;
  private HashMap<String, Integer> outList5;

  @Before
  public void setGraph() {
    directedGraph = new Graph<String, Integer>();
    emptyDirectedGraph = new Graph<String, Integer>();
    id1 = "1";
    label1 = "A";
    id2 = "2";
    label2 = "B";
    id3 = "3";
    label3 = "C";
    id4 = "4";
    label4 = "D";
    id5 = "5";
    label5 = "E";
    inList1 = new HashMap<String, Integer>();
    outList1 = new HashMap<String, Integer>();
    inList2 = new HashMap<String, Integer>();
    outList2 = new HashMap<String, Integer>();
    inList3 = new HashMap<String, Integer>();
    outList3 = new HashMap<String, Integer>();
    inList4 = new HashMap<String, Integer>();
    outList4 = new HashMap<String, Integer>();
    inList5 = new HashMap<String, Integer>();
    outList5 = new HashMap<String, Integer>();
  }

  private void addOneVertex(Graph<String, Integer> graph) {
    graph.addVertex(id1, label1, inList1, outList1);
    List<Graph<String, Integer>.Vertex> vertexList = graph.getVertices();
    for (Graph<String, Integer>.Vertex vertex : vertexList) {
      assertEquals("A",vertex.getLabel());
      assertEquals("1",vertex.getId());
      assertEquals(0,vertex.getAdjacentList().size());
    }
  }

  @Test
  public void testAddVertex_oneVertex() {
    addOneVertex(directedGraph);
  }

  @Test
  public void testAddVertex_vertices_DAG() {
    inList2.put("1", 2);
    outList2.put("1", 2);
    inList3.put("1", 5);
    inList4.put("2", 3);
    inList5.put("3", 4);
    directedGraph.addVertex(id1, label1, inList1, outList1);
    directedGraph.addVertex(id2, label2, inList2, outList2);
    directedGraph.addVertex(id3, label3, inList3, outList3);
    directedGraph.addVertex(id4, label4, inList4, outList4);
    directedGraph.addVertex(id5, label5, inList5, outList5);
    List<Graph<String, Integer>.Vertex> vertexList = 
        directedGraph.getVertices();
    for (Graph<String, Integer>.Vertex vertex : vertexList) {
      if (vertex.getId().equals("1")) {
        assertEquals("A",vertex.getLabel());
        assertEquals(2, vertex.getAdjacentList().size());
      } else if (vertex.getId().equals("2")) {
        assertEquals("B",vertex.getLabel());
        assertEquals(2, vertex.getAdjacentList().size());
      } else if (vertex.getId().equals("3")) {
        assertEquals("C",vertex.getLabel());
        assertEquals(1, vertex.getAdjacentList().size());
      } else if (vertex.getId().equals("4")) {
        assertEquals("D",vertex.getLabel());
        assertEquals(0, vertex.getAdjacentList().size());
      } else if (vertex.getId().equals("5")) {
        assertEquals("E",vertex.getLabel());
        assertEquals(0, vertex.getAdjacentList().size());
      }
    }
  }

  @Test (expected = IllegalArgumentException.class)
  public void testAddVertex_edgeWeightNotMatch() {
    inList2.put("1", 2);
    outList2.put("1", 1);
    directedGraph.addVertex(id1, label1, inList1, outList1);
    directedGraph.addVertex(id2, label2, inList2, outList2);

  }

  @Test (expected = IllegalArgumentException.class)
  public void testAddVertex_invalidOutList() {
    inList2.put("1", 2);
    outList2.put("1", 1);
    outList2.put("9", 1);
    directedGraph.addVertex(id1, label1, inList1, outList1);
    directedGraph.addVertex(id2, label2, inList2, outList2);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testAddVertex_invalidInList() {
    inList2.put("1", 2);
    outList2.put("1", 1);
    inList2.put("9", 1);
    directedGraph.addVertex(id1, label1, inList1, outList1);
    directedGraph.addVertex(id2, label2, inList2, outList2);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testAddVertex_duplicateId() {
    inList2.put("1", 2);
    outList2.put("1", 1);
    directedGraph.addVertex(id1, label1, inList1, outList1);
    directedGraph.addVertex(id1, label2, inList2, outList2);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testAddVertex_emptyId() {
    directedGraph.addVertex("", label1, inList1, outList1);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testAddVertex_nullId() {
    directedGraph.addVertex(null, label1, inList1, outList1);
  }

  @Test
  public void testRemoveVertex_oneVertex() {
    directedGraph.addVertex(id1, label1, inList1, outList1);
    assertEquals(1, directedGraph.getVertices().size());
    directedGraph.removeVertex("1");
    assertEquals(0,directedGraph.getVertices().size());
  }

  @Test
  public void testRemoveVertex_vertices() {
    inList2.put("1", 2);
    outList2.put("1", 2);
    inList3.put("1", 5);
    inList4.put("2", 3);
    inList5.put("3", 4);
    directedGraph.addVertex(id1, label1, inList1, outList1);
    directedGraph.addVertex(id2, label2, inList2, outList2);
    directedGraph.addVertex(id3, label3, inList3, outList3);
    directedGraph.addVertex(id4, label4, inList4, outList4);
    directedGraph.addVertex(id5, label5, inList5, outList5);
    assertEquals(5, directedGraph.getVertices().size());
    directedGraph.removeVertex("1");
    assertEquals(4,directedGraph.getVertices().size());
  }

  @Test (expected = IllegalArgumentException.class)
  public void testRemoveVertex_nullId() {
    directedGraph.removeVertex(null);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testRemoveVertex_emptyId() {
    directedGraph.removeVertex("");
  }

  @Test 
  public void testIterator_DAG() {
    inList2.put("1", 2);
    inList3.put("1", 5);
    inList4.put("2", 3);
    inList4.put("3", 3);
    inList5.put("3", 4);
    inList5.put("4", 3);
    directedGraph.addVertex(id1, label1, inList1, outList1);
    directedGraph.addVertex(id2, label2, inList2, outList2);
    directedGraph.addVertex(id3, label3, inList3, outList3);
    directedGraph.addVertex(id4, label4, inList4, outList4);
    directedGraph.addVertex(id5, label5, inList5, outList5);
    //DFS
    Iterator<Graph<String, Integer>.Vertex> dfsIt = 
        directedGraph.iterator();
    List<String> idCopy = new ArrayList<String>();
    while(dfsIt.hasNext()) {
      idCopy.add(dfsIt.next().getId());
    }
    assertEquals(idCopy,Arrays.asList("3", "5", "4","2","1"));
    List<Graph<String, Integer>.Vertex> vertexList = 
        directedGraph.getVertices();
    for (Graph<String, Integer>.Vertex vertex : vertexList) {
      assertTrue(vertex.isVisited());
    }   
    //BFS
    directedGraph.setBFSOn(true);
    Iterator<Graph<String, Integer>.Vertex> bfsIt = 
        directedGraph.iterator();
    List<String> idCopyBfs = new ArrayList<String>();
    while(bfsIt.hasNext()) {
      idCopyBfs.add(bfsIt.next().getId());
    }
    assertEquals(idCopyBfs,Arrays.asList("1", "2", "3","4","5"));
    for (Graph<String, Integer>.Vertex vertex : vertexList) {
      assertTrue(vertex.isVisited());
    }
  }

  @Test
  public void testIterator_hasNext_EmptyGraph() {
    Iterator<Graph<String, Integer>.Vertex> dfsIt = 
        emptyDirectedGraph.iterator();
    assertFalse(dfsIt.hasNext());
    directedGraph.setBFSOn(true);
    Iterator<Graph<String, Integer>.Vertex> bfsIt = 
        emptyDirectedGraph.iterator();
    assertFalse(bfsIt.hasNext());
  }

  @Test (expected = NoSuchElementException.class)
  public void testDFSIterator_next_emptyDirectedGraph() {
    Iterator<Graph<String, Integer>.Vertex> dfsIt = 
        emptyDirectedGraph.iterator();
    dfsIt.next();
  }

  @Test (expected = NoSuchElementException.class)
  public void testBFSIterator_next_emptyDirectedGraph() {
    emptyDirectedGraph.setBFSOn(true);
    Iterator<Graph<String, Integer>.Vertex> bfsIt = 
        emptyDirectedGraph.iterator();
    bfsIt.next();
  }

  @Test (expected = NoSuchElementException.class)
  public void testDFSIterator_next_multiple() {
    inList2.put("1", 2);
    inList3.put("1", 5);
    inList4.put("2", 3);
    inList4.put("3", 3);
    inList5.put("3", 4);
    inList5.put("4", 3);
    directedGraph.addVertex(id1, label1, inList1, outList1);
    directedGraph.addVertex(id2, label2, inList2, outList2);
    directedGraph.addVertex(id3, label3, inList3, outList3);
    directedGraph.addVertex(id4, label4, inList4, outList4);
    directedGraph.addVertex(id5, label5, inList5, outList5);
    directedGraph.setBFSOn(false);
    Iterator<Graph<String, Integer>.Vertex> dfsIt = 
        directedGraph.iterator();
    dfsIt.next();
    dfsIt.next();
    dfsIt.hasNext();
    dfsIt.next();
    dfsIt.next();
    dfsIt.hasNext();
    dfsIt.next();
    dfsIt.next();
    dfsIt.hasNext();
  }
  
  @Test (expected = NoSuchElementException.class)
  public void testBFSIterator_next_multiple() {
    inList2.put("1", 2);
    inList3.put("1", 5);
    inList4.put("2", 3);
    inList4.put("3", 3);
    inList5.put("3", 4);
    inList5.put("4", 3);
    directedGraph.addVertex(id1, label1, inList1, outList1);
    directedGraph.addVertex(id2, label2, inList2, outList2);
    directedGraph.addVertex(id3, label3, inList3, outList3);
    directedGraph.addVertex(id4, label4, inList4, outList4);
    directedGraph.addVertex(id5, label5, inList5, outList5);
    directedGraph.setBFSOn(true);
    Iterator<Graph<String, Integer>.Vertex> bfsIt = 
        directedGraph.iterator();
    bfsIt.next();
    bfsIt.next();
    bfsIt.hasNext();
    bfsIt.next();
    bfsIt.next();
    bfsIt.hasNext();
    bfsIt.next();
    bfsIt.next();
    bfsIt.hasNext();
  }

  @Test 
  public void testToString_vertex() {
    inList2.put("1", 2);
    outList2.put("1", 2);
    directedGraph.addVertex(id1, label1, inList1, outList1);
    directedGraph.addVertex(id2, label2, inList2, outList2);
    List<Graph<String, Integer>.Vertex> vertexList = 
        directedGraph.getVertices();
    for (Graph<String, Integer>.Vertex vertex : vertexList) {
      assertEquals("Vertex [id=" + vertex.getId() + ", label=" 
          + vertex.getLabel() + ", inDegree=" + vertex.getInDegree()
          + ", isVisited=" + vertex.isVisited() + "]",vertex.toString());
    }   
  }

  @Test 
  public void testToString_edge() {
    inList2.put("1", 2);
    outList2.put("1", 2);
    directedGraph.addVertex(id1, label1, inList1, outList1);
    directedGraph.addVertex(id2, label2, inList2, outList2);
    List<Graph<String, Integer>.Edge> edgeList = 
        directedGraph.getEdges();
    for (Graph<String, Integer>.Edge edge : edgeList) {
      assertEquals("Edge [weight=" + edge.getWeight() 
      + ", vertexU=" + edge.getVertexU().getId() + ", vertexV=" 
      + edge.getVertexV().getId() + "]",edge.toString());
    }
  }

  @Test
  public void testGetter_getBFSOn() {
    directedGraph.setBFSOn(true);
    assertTrue(directedGraph.getBFSOn());
  }

  @Test
  public void testGetter_isDirected() {
    assertTrue(directedGraph.isDirected());
  }

  @Test
  public void testGetter_getIdList() {
    directedGraph.addVertex(id1, label1, inList1, outList1);
    directedGraph.addVertex(id2, label2, inList2, outList2);
    directedGraph.addVertex(id3, label3, inList3, outList3);
    directedGraph.addVertex(id4, label4, inList4, outList4);
    assertEquals(Arrays.asList("1", "2", "3","4"), directedGraph.getIdList());
  }
}