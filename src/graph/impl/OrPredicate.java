package graph.impl;

import graph.api.Predicate;

/**
 * @author Huijuan Zou
 * OrPredicate takes two predicates as input,
 * and returns the logic or computation result of the two predicates.
 * @param <T> Type of input.
 */
public class OrPredicate<T> implements Predicate<T> {
  private Predicate<T> predicateA;
  private Predicate<T> predicateB;

  public OrPredicate(Predicate<T> predicateA, Predicate<T> predicateB) {
    if (predicateA == null || predicateB == null) {
      throw new IllegalArgumentException("Predicate cannot be null");
    }
    this.predicateA = predicateA;
    this.predicateB = predicateB;
  }

  @Override
  public boolean accept(T item) {
    if (item == null || item.equals("")) {
      return false;
    }
    return predicateA.accept(item) || predicateB.accept(item); 
  }
}