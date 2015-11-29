package kz.greetgo.libase;

import kz.greetgo.libase.util.ConnectionHelper;

import java.sql.SQLException;

public interface SqlDialect {
  boolean isTableExist(String tableName, ConnectionHelper connection) throws SQLException;

  String varchar(int len);

  String forLong();

  String timestamp();

  String currentTimestamp();

  void lockTable(ConnectionHelper connection, String tableName) throws SQLException;
}
