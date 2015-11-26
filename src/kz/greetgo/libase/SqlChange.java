package kz.greetgo.libase;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class SqlChange implements DbChange {
  private final String sql;
  private final String group;
  private final String author;
  private final String id;

  public SqlChange(String sql, String group, String author, String id) {
    this.sql = sql;
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
  public void apply(Connection connection) throws Exception {
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.executeUpdate();
    }
  }

  @Override
  public String identityStr() {
    return group + "-" + author + "-" + id;
  }
}