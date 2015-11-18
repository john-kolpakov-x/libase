package kz.greetgo.libase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChangeList implements Iterable<DbChange> {

  private final List<ChangeIterable> list = new ArrayList<>();

  @Override
  public Iterator<DbChange> iterator() {
    return new Iterator<DbChange>() {
      Iterator<ChangeIterable> parentIterator = list.iterator();

      Iterator<DbChange> currentIterator = null;

      @Override
      public boolean hasNext() {
        return parentIterator.hasNext();
      }

      @Override
      public DbChange next() {
        if (currentIterator == null || !currentIterator.hasNext()) {
          currentIterator = parentIterator.next().iterator();
        }
        return currentIterator.next();
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }

  public void addAll(ChangeIterable changeIterable) {
    list.add(changeIterable);
  }

  public void add(final DbChange dbChange) {
    list.add(new ChangeIterable() {
      @Override
      public Iterator<DbChange> iterator() {
        return new Iterator<DbChange>() {
          boolean returned = false;

          @Override
          public boolean hasNext() {
            return !returned;
          }

          @Override
          public DbChange next() {
            returned = true;
            return dbChange;
          }

          @Override
          public void remove() {
            throw new UnsupportedOperationException();
          }
        };
      }
    });
  }
}
