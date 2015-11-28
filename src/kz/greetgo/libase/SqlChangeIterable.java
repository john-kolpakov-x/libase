package kz.greetgo.libase;

import java.util.Iterator;

public class SqlChangeIterable implements ChangeIterable {
  private final String[] sqls;
  private final String group, author;
  private final String prefix;

  public SqlChangeIterable(String group, String author, String prefix, String sqls[]) {
    this.sqls = sqls;
    this.group = group;
    this.author = author;
    this.prefix = prefix;
  }

  @Override
  public Iterator<DbChange> iterator() {
    return new Iterator<DbChange>() {
      int index = 0;

      @Override
      public boolean hasNext() {
        return index < sqls.length;
      }

      @Override
      public DbChange next() {
        SqlChange ret = new SqlChange(sqls[index], group, author, prefix + '#' + (index + 1));
        index++;
        return ret;
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }
}
