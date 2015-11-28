package kz.greetgo.libase.resources;

import kz.greetgo.libase.ResourcesSqlChangeSource;
import kz.greetgo.libase.resources.simple.SqlResourceSimple;

public class SqlResourceDeep extends ResourcesSqlChangeSource {
  public SqlResourceDeep() {
    add("top1.sql");
    include(SqlResourceSimple.class);
    add("top2.sql");
  }
}
