package graph.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import graph.api.Predicate;
import graph.impl.Graph.Edge;
import graph.impl.Graph.Vertex;

public class OrPredicateTest {
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
  private Predicate<Edge> choice1;
  private Predicate<Edge> choice2;

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
    choice1 = new Predicate<Edge>() {
      public boolean accept(Edge item) {
        return ((int) item.getWeight() > 2 && (int) item.getWeight() < 5);
      }
    };
    choice2 = new Predicate<Edge>() {
      public boolean accept(Edge item) {
        return ((int) item.getWeight() < 4 && (int) item.getWeight() > 1);
      }
    };
  }

  @Test
  public void testAccept() {
    Predicate<Edge> appropriate = new OrPredicate<Edge>(choice1,choice2);
    for (Edge edge : directedGraph.getEdges()) {
      if ((int) edge.getWeight() > 1 && (int) edge.getWeight() < 5) {
        assertTrue(appropriate.accept(edge));
      } else {
        assertFalse(appropriate.accept(edge));
      }
    }
  }

  @Test (expected = IllegalArgumentException.class)
  public void testNullInput_OrPredicate() {
    Predicate<Edge> OrPredicate = new OrPredicate<Edge>(choice1, null);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testNullInput_OrPredicate_twoNull() {
    Predicate<Edge> OrPredicate = new OrPredicate<Edge>(null, null);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testNullInput_OrPredicate_oneNull() {
    Predicate<Edge> OrPredicate = new AndPredicate<Edge>(null, choice2);
  }

  @Test 
  public void testAccept_null_emptyInput() {
    Predicate orLabel = new OrPredicate(choice1, choice2);
    assertFalse(orLabel.accept(null));
    assertFalse(orLabel.accept(""));
  }
}