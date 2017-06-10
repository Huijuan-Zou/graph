package graph.api;

/**
 * Predicate interface contains one method accept. 
 * Accept method test if the input passes the argument or not.
 * @param <T> Type of input.
 */
public interface Predicate<T> {
  /**
   * Accept method determines if the item passes the argument or not.
   * @param item input the user wants to test. item cannot be null or empty.
   * @return true if item passes the argument, otherwise return false.
   */
  boolean accept(T item);
}