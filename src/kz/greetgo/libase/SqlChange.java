package kz.greetgo.libase;

import kz.greetgo.libase.util.ConnectionHelper;
import kz.greetgo.libase.util.Md5Util;

import java.sql.PreparedStatement;

class SqlChange implements DbChange {
  private final String sql;
  private final String group;
  private final String author;
  private final String id;

  public SqlChange(String sql, String group, String author, String id) {
    this.sql = sql.trim();
    this.group = group;
    this.author = author;
    this.id = id;
  }

  @Override
  public String group() {
    return group;
  }

  @Override
  public String author() {
    return author;
  }

  @Override
  public String id() {
    return id;
  }

  @Override
  public String hash() {
    return Md5Util.strToMd5(sql);
  }

  @Override
  public void apply(ConnectionHelper connection) throws Exception {
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.executeUpdate();
    }
  }

  @Override
  public String identityStr() {
    return group + "-" + author + "-" + id;
  }

  @Override
  public String toString() {
    return "SqlChange(" + sql + ")";
  }
}
