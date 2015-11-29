package kz.greetgo.libase;

import java.sql.Connection;
import java.sql.SQLException;

public class DetectDbType {
  private DetectDbType() {
  }

  public static DbType byConnection(Connection connection) throws SQLException {
    String databaseProductName = connection.getMetaData().getDatabaseProductName();

    if ("PostgreSQL".equals(databaseProductName)) return DbType.POSTGRES;
    if ("H2".equals(databaseProductName)) return DbType.H2;
    if ("HSQL Database Engine".equals(databaseProductName)) return DbType.HSQLDB;

    throw new IllegalArgumentException("Unknown databaseProductName = " + databaseProductName);
  }
}
