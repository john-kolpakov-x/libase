package kz.greetgo.libase.resources.simple;

import kz.greetgo.libase.ResourcesSqlChangeSource;

public class SqlResourceSimple extends ResourcesSqlChangeSource {
  public SqlResourceSimple() {
    add("001.sql");
    add("002.sql");
  }
}
