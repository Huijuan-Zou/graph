package graph.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

/**
 * The Graph class contains 3 methods, and it's intended for directed graph. 
 * One can create a new empty graph, add vertex to the graph, and remove 
 * vertex.
 * One can also iterate over the graph using BFS or DFS traverse method.
 * There are five fields in the class. 
 * The vertices contains all the vertex of the graph.
 * The edges contains all the edges of the graph, either directed or
 *  undirected.
 *  The idList contains all the unique ids of each vertex.
 * isDirected indicates if the graph is directed or undirected.
 * iteratorType is the traverse method a user wants to use.
 * @param <T> Type of id, iteratorType.
 * @param <E> Type of edge weight.
 */
public class Graph<T,E> implements Iterable {
  private List<Vertex> vertices;
  private List<Edge> edges;
  private List<T> idList;
  private boolean isDirected = true;
  private boolean BFSOn = false;

  public Graph(){
    this.vertices = new ArrayList<Vertex>();
    this.edges = new ArrayList<Edge>();
    idList = new ArrayList<T>();
  }

  /** 
   * {@inheritDoc}
   * The default return value is a DFSIterator.
   * If the iteratorType is set to "BFS", then the return value 
   * is a BFSIterator.
   */
  @Override
  public Iterator<Vertex> iterator() {
    if (BFSOn) {
      return new BFSIterator(); 
    } 
    return new DFSIterator();
  }

  public List<T> getIdList() {
    return idList;
  }

  public List<Edge> getEdges() {
    List<Edge> edgesCopy = new ArrayList<Edge>();
    for (Edge edge : edges) {
      edgesCopy.add(edge);
    }
    return edgesCopy;
  }

  public List<Vertex> getVertices() {
    List<Vertex> verticesCopy = new ArrayList<Vertex>();
    for (Vertex vertex : vertices) {
      verticesCopy.add(vertex);
    }
    return verticesCopy;
  }

  public boolean isDirected() {
    return isDirected;
  }

  public boolean getBFSOn() {
    return BFSOn;
  }

  public void setBFSOn(boolean BFSOn) {
    this.BFSOn = BFSOn;
  }

  /**
   * Method adding new vertex to the graph. 
   * The vertex, indegree vertex list and outdegree vertex list are added.
   * When adding inList and outList, should only add vertices that exist.
   * For example 1-->2, if you add 1 first, 1 has empty inList and outList,
   *  while 2 has 1 included in its inList.
   * @param label vertex label, can have duplicate label.
   * @param id vertex id, must be unique.
   * @param inList a list of id and edge length of indegree vertices.
   * @param outList a list of id and edge length of outdegree vertices.
   * @return a boolean value indicating if adding is successful.
   */
  public void addVertex(T id, T label, HashMap<T,E> inList, HashMap<T,E> outList) {
    //create new vertex 
    Vertex newVertex = new Vertex(id, label);
    newVertex.inDegree = inList.size();
    //add outdegree vertex to adjacent list
    List<Edge> adjacentList = new ArrayList<Edge>();
    HashMap<T,E> inListCopy = new HashMap<T,E>();
    for (Vertex vertex : vertices) {
      if (outList.isEmpty() && inList.isEmpty()) {
        break;
      }
      T adjacentId = vertex.getId();
      if (outList.containsKey(adjacentId)) {
        Edge edge = new Edge(newVertex,vertex, outList.get(adjacentId));
        adjacentList.add(edge);
        edges.add(edge);
        outList.remove(adjacentId);
      }
      // copy and remove items in inList to check if all ids in inList are valid
      if (inList.containsKey(adjacentId)) {
        inListCopy.put(adjacentId, inList.get(adjacentId));
        inList.remove(adjacentId);
      }
    }
    //check if all ids in outList are valid
    if (!outList.isEmpty()) {
      throw new IllegalArgumentException("invalid Id in outList");
    }
    //check if all ids in inList are valid
    if (!inList.isEmpty()) {
      throw new IllegalArgumentException("invalid Id in inList");
    }
    //assign value to adjacentList
    newVertex.setAdjacentList(adjacentList);
    //add new vertex to the graph
    vertices.add(newVertex);
    //add inList to the graph
    for (Vertex vertex : vertices) {
      T inId = vertex.getId();
      if (inListCopy.containsKey(inId)) {
        List<Edge> inAdjacentList = vertex.getAdjacentList();
        Edge newEdge = new Edge(vertex, newVertex, inListCopy.get(inId));
        inAdjacentList.add(newEdge);
        edges.add(newEdge);
        vertex.setAdjacentList(inAdjacentList);
      }
    }
  }

  /**
   * removeVertex method removes the unique vertex associated with 
   * the id provided.
   * @param id id of the vertex, must be unique, cannot be empty or null.
   */
  public void removeVertex(T id) {
    if (id == null || id.equals("")) {
      throw new IllegalArgumentException("Id can't be empty or null!");
    }
    for (Vertex vertex : vertices) {
      List<Edge> adjacentList = vertex.getAdjacentList();
      Iterator<Edge> edgeIt = adjacentList.iterator();
      while (edgeIt.hasNext()) {
        Edge edge = edgeIt.next();
        if (edge.getVertexV().getId().equals(id)) {
          edgeIt.remove();
          edges.remove(edge);
        }
      }
    }
    Iterator<Vertex> it = vertices.iterator();
    while (it.hasNext()) {
      Vertex vertex = it.next();
      if (vertex.getId().equals(id)) {
        it.remove();
        for (Edge edge : vertex.getAdjacentList()) {
          edges.remove(edge);
        }
      }
    }
  }

  /**
   * Edge class has three fields: the start vertex U, the end vertex V and 
   * the length of the edge.
   * No setter is included in the edge class to avoid confusion.
   * Edge can only be created through the constructor.
   * Each edge can only be created once. 
   * If new value needs to be assigned to an edge, one needs to
   *  delete the edge/edges first.
   */
  class Edge{
    private E weight;
    private Vertex vertexU;
    private Vertex vertexV;

    Edge(Vertex vertex1, Vertex vertex2, E weight){
      for (Edge edge : edges) {
        Vertex U = edge.getVertexU();
        Vertex V = edge.getVertexV();
        E edgeWeight = edge.getWeight();
        //Creating a new edge that already exists is not allowed
        if (U.equals(vertex1) && V.equals(vertex2)) {
          throw new IllegalArgumentException("Edge already exists!");
        }
        //Edges sharing same vertices should have same edge 
        //length for directed graph.
        if (U.equals(vertex2) && V.equals(vertex1) 
            && !edgeWeight.equals(weight)) {
          throw new IllegalArgumentException("Cannot assign edge length "
              + "with different values. Please use previous value!");
        } 
      }
      this.weight = weight;
      this.vertexU = vertex1;
      this.vertexV = vertex2;
    }

    public E getWeight() {
      return weight;
    }

    public Vertex getVertexU() {
      return vertexU;
    }

    public Vertex getVertexV() {
      return vertexV;
    }

    @Override
    public String toString() {
      return "Edge [weight=" + weight + ", vertexU=" + vertexU.getId()
      + ", vertexV=" + vertexV.getId() + "]";
    }
  }

  /**
   * class Vertex contains five fields.
   * id must be unique, cannot be empty or null.
   * label can have duplicate values.
   * inDegree is the number of vertices pointing to this vertex.
   * adjacentList contains all the vertices and  edge 
   * weights this vertex pointing to.
   * isVisited indicates whether the vertex is visited or not.
   */
  protected class Vertex{
    private final T id;
    private T label;
    private int inDegree;
    private List<Edge> adjacentList;
    private boolean isVisited = false;

    Vertex(T id, T label) {
      if (id == null || id.equals("")) {
        throw new IllegalArgumentException("Id can't be empty or null!");
      } else if (idList.contains(id)) {
        throw new IllegalArgumentException("Id is already taken!");
      } 
      this.id = id;
      idList.add(id);
      this.setLabel(label);
    }

    public T getLabel() {
      return label;
    }

    public void setLabel(T value) {
      this.label = value;
    }

    public List<Edge> getAdjacentList() {
      List<Edge> adjacentListCopy = new ArrayList<Edge>();
      for (Edge edge : adjacentList) {
        adjacentListCopy.add(edge);
      }
      return adjacentListCopy;
    }

    public void setAdjacentList(List<Edge> adjacentList) {
      this.adjacentList = adjacentList;
    }

    public boolean isVisited() {
      return isVisited;
    }

    public void setVisited(boolean isVisited) {
      this.isVisited = isVisited;
    }

    public T getId() {
      return id;
    }

    public int getInDegree() {
      return inDegree;
    }

    @Override
    public String toString() {
      return "Vertex [id=" + id + ", label=" + label + ", inDegree=" + 
          inDegree + ", isVisited=" + isVisited + "]";
    }
  }

  /**
   * Iterator that does DFS traverse.
   * This method requires the input graph be a DAG.
   */
  public class DFSIterator implements Iterator<Vertex> {
    private Queue<Vertex> queue = new LinkedList<Vertex>();
    private Stack<Vertex> stack = new Stack<Vertex>();
    private Vertex currentVertex;

    DFSIterator() {
      for (Vertex vertex : vertices) {
        vertex.setVisited(false);
        queue.add(vertex);
      }
    }

    @Override
    public boolean hasNext() {
      if (!stack.isEmpty()) {
        return true;
      }
      while (!queue.isEmpty()) {
        currentVertex = queue.poll();
        if (!currentVertex.isVisited()) {
          stack.push(currentVertex);
          DFSVisit(currentVertex);
          return true;
        }
      }
      return false;
    }

    public void DFSVisit(Vertex vertex){
      for (Edge edge : vertex.adjacentList) {
        Vertex adjacentVertex = edge.getVertexV();
        if (!adjacentVertex.isVisited()) {
          stack.push(adjacentVertex);
          DFSVisit(adjacentVertex);
        }
      }
      vertex.setVisited(true);
    }

    @Override
    public Vertex next() {
      if (queue.isEmpty() && stack.isEmpty()) {
        throw new NoSuchElementException();
      }
      if (!stack.isEmpty()) {
        currentVertex = stack.pop();
        return currentVertex;
      }
      if (hasNext()) {
        currentVertex = stack.pop();
        return currentVertex;
      } 
      throw new NoSuchElementException();
    } 
  }

  /**
   * Iterator that does BFS traverse.
   * This method requires the input graph be a DAG.
   */
  public class BFSIterator implements Iterator<Vertex> {
    private Queue<Vertex> queue = new LinkedList<Vertex>();
    private Vertex source;
    private Vertex currentVertex;

    /*
     * The constructor chooses starting vertex for BFS iterator.
     * If the graph is DAG, choose the vertex with indegree value
     *  of 0 as source.
     * If the graph is Undirected, choose a random value. 
     */
    BFSIterator() {
      for (Vertex vertex : vertices) {
        vertex.setVisited(false);
        if (vertex.getInDegree() == 0) {
          source = vertex;
        }
      }
      if (!vertices.isEmpty()) {
        queue.add(source);
        source.setVisited(true);
      } 
    }

    @Override
    public boolean hasNext() {
      if (queue.isEmpty()) {
        return false;
      }   
      return true;
    }

    @Override
    public Vertex next() {
      if (queue.isEmpty()) {
        throw new NoSuchElementException();
      }
      currentVertex = queue.poll();
      List<Edge> adjacentList = currentVertex.getAdjacentList();
      for (Edge edge : adjacentList) {
        Vertex adjacentVertex = edge.getVertexV();
        if (!adjacentVertex.isVisited()) {
          queue.add(adjacentVertex);
          adjacentVertex.setVisited(true);
        }
      }
      return currentVertex;
    }
  }
}