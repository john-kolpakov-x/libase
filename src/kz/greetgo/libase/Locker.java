package kz.greetgo.libase;

import kz.greetgo.libase.util.ConnectionHelper;

import java.sql.SQLException;

public class Locker {

  private final Config config;
  private final ConnectionHelper connection;
  private final SqlDialect sqlDialect;
  private final String lockName = "CHANGES";

  public Locker(Config config, ConnectionHelper connection, SqlDialect sqlDialect) {
    this.config = config;
    this.connection = connection;
    this.sqlDialect = sqlDialect;
  }

  public boolean tryLock() throws SQLException {

    safelyCreateLockTable();

    connection.connection.setAutoCommit(false);
    try {

      sqlDialect.lockTable(connection, config.lockTable);

      int flag = connection.oneIntOrZero("select " + config.lockFlag + " from " + config.lockTable
        + " where " + config.lockName + " = ?", lockName);

      if (flag != 0) return false;

      int rows = connection.update("update " + config.lockTable + " set " + config.lockFlag + " = 1"
        + ", " + config.lockLastLockedAt + " = " + sqlDialect.currentTimestamp()
        + " where " + config.lockName + " = ?", lockName);

      if (rows == 0) {

        connection.exec("insert into " + config.lockTable
          + " (" + config.lockName + ", " + config.lockFlag + ", " + config.lockLastLockedAt
          + ") values (?, 1, " + sqlDialect.currentTimestamp() + ')', lockName);

      }

      return true;

    } finally {
      connection.connection.commit();
      connection.connection.setAutoCommit(true);
    }

  }

  private boolean safelyCreateLockTable() throws SQLException {

    try {
      StringBuilder sb = new StringBuilder("create table ");
      sb.append(config.lockTable).append(" (");
      sb.append(config.lockName).append(' ').append(sqlDialect.varchar(100)).append(" not null, ");
      sb.append(config.lockFlag).append(" int not null, ");
      sb.append(config.lockLastLockedAt).append(' ').append(sqlDialect.timestamp()).append(" not null,");
      sb.append("primary key(").append(config.lockName).append(')');
      sb.append(')');

      connection.exec(sb.toString());

      return true;
    } catch (SQLException e) {
      return false;
    }
  }

  public void unlock() throws SQLException {
    connection.update("update " + config.lockTable + " set " + config.lockFlag + " = 0"
      + " where " + config.lockName + " = ?", lockName);
  }
}
