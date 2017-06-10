package graph.impl;

import java.util.Iterator;
import java.util.NoSuchElementException;

import graph.api.Predicate;

/**
 * FilterIterator class is an implementation of iterator interface.
 * It takes a customized iterator and a predicate in order to do 
 * customized filtering based on provided predicate during iteration.
 *  Only item fullfits the needs of predicate can be returned by next() method,
 *  and only when there exists items/item that fullfit the 
 * requirements by predicate should hasNext() return true.
 * @param <T> Type of nextItem.
 */
public class FilterIterator<T> implements Iterator<T> {
  private Iterator<T> iterator;
  private Predicate<T> predicate;
  private T nextItem;
  private boolean nextIsValid;

  public FilterIterator(Iterator<T> iterator, Predicate<T> predicate) {
    if (iterator == null || predicate == null) {
      throw new IllegalArgumentException("Predicate cannot be null");
    }
    this.iterator = iterator;
    this.predicate = predicate;
  }

  @Override
  public boolean hasNext() {
    if (nextIsValid) {
      return true;
    }
    while (iterator.hasNext() ) {
      nextItem = iterator.next();
      if (predicate.accept(nextItem)){
        nextIsValid = true;
        return true;
      }
    }
    return false;
  }

  @Override
  public T next() {
    if (nextIsValid) {
      nextIsValid = false;
      T val = nextItem;
      nextItem = null;
      return val;
    }
    if (hasNext()) {
      return next();
    }
    throw new NoSuchElementException();
  } 
}