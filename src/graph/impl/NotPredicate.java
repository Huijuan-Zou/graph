package graph.impl;

import graph.api.Predicate;

/**
 * @author Huijuan Zou
 * AndPredicate takes one predicate as input,
 * and returns the logic not computation result of the predicate.
 * @param <T> Type of input.
 */
public class NotPredicate<T> implements Predicate<T> {
  private Predicate<T> predicate;

  public NotPredicate(Predicate<T> predicate) {
    if (predicate == null) {
      throw new IllegalArgumentException("Predicate cannot be null");
    }
    this.predicate = predicate;
  }

  @Override
  public boolean accept(T item) {
    if (item == null || item.equals("")) {
      return false;
    }
    return !predicate.accept(item);
  }
}