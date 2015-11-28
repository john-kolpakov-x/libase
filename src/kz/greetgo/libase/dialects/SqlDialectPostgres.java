package kz.greetgo.libase.dialects;

import kz.greetgo.libase.util.ConnectionHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlDialectPostgres extends AbstractSqlDialect {
  @Override
  public boolean isTableExist(String tableName, ConnectionHelper connection) throws SQLException {
    try (PreparedStatement ps = connection.connection.prepareStatement("select 1" +
      " from information_schema.tables where table_schema = 'public' and table_name = ?")) {

      ps.setString(1, tableName);

      try (ResultSet rs = ps.executeQuery()) {
        return rs.next();
      }

    }
  }

  @Override
  public String varchar(int len) {
    return "varchar(" + len + ')';
  }

  @Override
  public String forLong() {
    return "bigint";
  }

  @Override
  public String timestamp() {
    return "timestamp";
  }

  @Override
  public String currentTimestamp() {
    return "current_timestamp";
  }

  @Override
  public void lockTable(ConnectionHelper connection, String tableName) throws SQLException {
    connection.exec("lock table " + tableName + " in exclusive mode");
  }

}
