package graph.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import graph.api.Predicate;
import graph.impl.Graph.Vertex;

public class NotPredicateTest {
  private Graph< String, Integer> directedGraph;
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

  @Before
  public void setPredicate() {
    directedGraph = new Graph<String, Integer>();
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
    labelNotA = new Predicate<Vertex>() {
      @Override
      public boolean accept(Vertex item) {
        return !item.getLabel().equals("A");
      }
    };
  }

  @Test
  public void testNotPredicate() {
    Predicate<Vertex> labelA = new NotPredicate<Vertex>(labelNotA); 
    for (Vertex vertex : directedGraph.getVertices()) {
      if (vertex.getLabel().equals("A")) {
        assertTrue(labelA.accept(vertex));
      } else {
        assertFalse(labelA.accept(vertex));
      }
    }
  }

  @Test (expected = IllegalArgumentException.class)
  public void testNullInput_NotPredicate() {
    Predicate<Vertex> notPredicate = new NotPredicate<Vertex>(null); 
  }

  @Test 
  public void testAccept_null_emptyInput() {
    Predicate notLabel = new NotPredicate(labelNotA);
    assertFalse(notLabel.accept(null));
    assertFalse(notLabel.accept(""));
  }
}