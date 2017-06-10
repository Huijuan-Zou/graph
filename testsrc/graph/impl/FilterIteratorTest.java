package graph.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

import graph.api.Predicate;
import graph.impl.Graph.Vertex;

public class FilterIteratorTest {
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
  private Predicate<Vertex> labelNotA;
  private Predicate<Vertex> labelNotC;
  private Predicate<Vertex> andPredicate;
  private Predicate<Vertex> orPredicate;
  private Predicate<Vertex> notPredicate;

  @Before
  public void setPredicate() {
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
    inList2.put("1", 1);
    inList3.put("1", 5);
    inList4.put("2", 3);
    inList5.put("3", 9);
    inList5.put("4", 4);
    inList5.put("2", 2);
    directedGraph.addVertex(id1, label1, inList1, outList1);
    directedGraph.addVertex(id2, label2, inList2, outList2);
    directedGraph.addVertex(id3, label3, inList3, outList3);
    directedGraph.addVertex(id4, label4, inList4, outList4);
    directedGraph.addVertex(id5, label5, inList5, outList5);
    labelNotA = new Predicate<Vertex>() {
      @Override
      public boolean accept(Vertex item) {
        return !item.getLabel().equals("A");
      }
    };
    labelNotC = new Predicate<Vertex>() {
      @Override
      public boolean accept(Vertex item) {
        return !((graph.impl.Graph.Vertex) item).getLabel().equals("C");
      }
    };
    andPredicate = new AndPredicate<Vertex>(labelNotA, labelNotC);
    orPredicate = new OrPredicate<Vertex>(labelNotA, labelNotC);
    notPredicate = new NotPredicate<Vertex>(labelNotA);
  }

  @Test
  public void testFilterIterator_BFS_andPredicate() {
    directedGraph.setBFSOn(true);
    Iterator<Graph<String, Integer>.Vertex> bfsIt = directedGraph.iterator();
    FilterIterator<Vertex> andBfsFIt= new FilterIterator(bfsIt, andPredicate);
    List<String> idCopy = new ArrayList<String>();

    while (andBfsFIt.hasNext()) {
      Graph<String, Integer>.Vertex vertex = 
          (Graph<String, Integer>.Vertex) andBfsFIt.next();
      assertTrue(!vertex.getLabel().equals("A") 
          && !vertex.getLabel().equals("C"));
      idCopy.add((String) vertex.getId());
    }
    assertEquals(Arrays.asList("2", "4", "5"),idCopy);
  }

  @Test
  public void testFilterIterator_BFS_orPredicate() {
    directedGraph.setBFSOn(true);
    Iterator<Graph<String, Integer>.Vertex> bfsIt = directedGraph.iterator();
    FilterIterator<Vertex> orBfsFIt= new FilterIterator(bfsIt, orPredicate);
    List<String> idCopy = new ArrayList<String>();

    while (orBfsFIt.hasNext()) {
      Graph<String, Integer>.Vertex vertex = 
          (Graph<String, Integer>.Vertex) orBfsFIt.next();
      assertTrue(!vertex.getLabel().equals("A")
          || !vertex.getLabel().equals("C"));
      idCopy.add((String) vertex.getId());
    }
    assertEquals(Arrays.asList("1","2","3", "4", "5"),idCopy);
  }

  @Test
  public void testFilterIterator_BFS_notPredicate() {
    directedGraph.setBFSOn(true);
    Iterator<Graph<String, Integer>.Vertex> bfsIt = directedGraph.iterator();
    FilterIterator<Vertex> notBfsFIt= new FilterIterator(bfsIt, notPredicate);
    List<String> idCopy = new ArrayList<String>();

    while (notBfsFIt.hasNext()) {
      Graph<String, Integer>.Vertex vertex = 
          (Graph<String, Integer>.Vertex) notBfsFIt.next();
      assertTrue(vertex.getLabel().equals("A"));
      idCopy.add((String) vertex.getId());
    }
    assertEquals(Arrays.asList("1"),idCopy);
  }

  @Test
  public void testFilterIterator_DFS_andPredicate() {
    directedGraph.setBFSOn(false);
    Iterator<Graph<String, Integer>.Vertex> dfsIt = directedGraph.iterator();
    FilterIterator<Vertex> andDfsFIt= new FilterIterator(dfsIt, andPredicate);
    List<String> idCopy = new ArrayList<String>();

    while (andDfsFIt.hasNext()) {
      Graph<String, Integer>.Vertex vertex = 
          (Graph<String, Integer>.Vertex) andDfsFIt.next();
      assertTrue(!vertex.getLabel().equals("A") 
          && !vertex.getLabel().equals("C"));
      idCopy.add((String) vertex.getId());
    }
    assertEquals(Arrays.asList("5", "4", "2"),idCopy);
  }

  @Test
  public void testFilterIterator_DFS_orPredicate() {
    directedGraph.setBFSOn(false);
    Iterator<Graph<String, Integer>.Vertex> dfsIt = directedGraph.iterator();
    FilterIterator<Vertex> orDfsFIt= new FilterIterator(dfsIt, orPredicate);
    List<String> idCopy = new ArrayList<String>();

    while (orDfsFIt.hasNext()) {
      Graph<String, Integer>.Vertex vertex = 
          (Graph<String, Integer>.Vertex) orDfsFIt.next();
      assertTrue(!vertex.getLabel().equals("A") 
          || !vertex.getLabel().equals("C"));
      idCopy.add((String) vertex.getId());
    }
    assertEquals(Arrays.asList("3","5", "4", "2","1"),idCopy);
  }

  @Test
  public void testFilterIterator_DFS_notPredicate() {
    directedGraph.setBFSOn(false);
    Iterator<Graph<String, Integer>.Vertex> dfsIt = directedGraph.iterator();
    FilterIterator<Vertex> notDfsFIt= new FilterIterator(dfsIt, notPredicate);
    List<String> idCopy = new ArrayList<String>();

    while (notDfsFIt.hasNext()) {
      Graph<String, Integer>.Vertex vertex = 
          (Graph<String, Integer>.Vertex) notDfsFIt.next();
      assertTrue(vertex.getLabel().equals("A"));
      idCopy.add((String) vertex.getId());
    }
    assertEquals(Arrays.asList("1"),idCopy);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testConstructor_twoNull() {
    FilterIterator filterIt = new FilterIterator(null,null);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testConstrutor_oneNull() {
    Iterator<Graph<String, Integer>.Vertex> dfsIt = directedGraph.iterator();
    FilterIterator<Vertex> notDfsFIt= new FilterIterator(dfsIt, null);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testConstrutor_null() {
    FilterIterator<Vertex> notDfsFIt= new FilterIterator(null, orPredicate);
  }

  @Test (expected = NoSuchElementException.class)
  public void testConstrutor_EmptyIterator() {
    directedGraph.setBFSOn(false);
    Iterator<Graph<String, Integer>.Vertex> dfsIt = 
        emptyDirectedGraph.iterator();
    FilterIterator<Vertex> notDfsFIt= new FilterIterator(dfsIt, notPredicate);
    notDfsFIt.next();
  }

  @Test (expected = NoSuchElementException.class)
  public void testNext() {
    directedGraph.setBFSOn(false);
    Iterator<Graph<String, Integer>.Vertex> dfsIt = directedGraph.iterator();
    FilterIterator<Vertex> andDfsFIt= new FilterIterator(dfsIt, andPredicate);
    assertEquals("5",andDfsFIt.next().getId());
    assertEquals("4",andDfsFIt.next().getId());
    andDfsFIt.hasNext();
    assertEquals("2",andDfsFIt.next().getId());
    andDfsFIt.next();
    andDfsFIt.next();
    andDfsFIt.next();   
  }

  @Test
  public void testHasNext_multiple() {
    directedGraph.setBFSOn(true);
    Iterator<Graph<String, Integer>.Vertex> bfsIt = directedGraph.iterator();
    FilterIterator<Vertex> orBfsFIt= new FilterIterator(bfsIt, orPredicate);
    orBfsFIt.hasNext();
    orBfsFIt.next();
    orBfsFIt.hasNext();
    orBfsFIt.hasNext();
    orBfsFIt.next();
    orBfsFIt.hasNext();
    orBfsFIt.hasNext();
    orBfsFIt.hasNext();
    orBfsFIt.next();
    orBfsFIt.hasNext();
    orBfsFIt.hasNext();
    orBfsFIt.hasNext();
  }
}