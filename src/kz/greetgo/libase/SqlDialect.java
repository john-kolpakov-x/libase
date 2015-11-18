package kz.greetgo.libase;

import java.sql.Connection;
import java.sql.SQLException;

public interface SqlDialect {
  boolean isTableExist(String tableName, Connection connection) throws SQLException;

  String varchar(int len);

  String forLong();

  String timestamp();

  String currentTimestamp();
}
