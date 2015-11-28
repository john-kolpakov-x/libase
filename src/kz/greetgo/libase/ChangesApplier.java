package kz.greetgo.libase;

import kz.greetgo.libase.util.ConnectionHelper;

import java.sql.*;
import java.util.Calendar;

public class ChangesApplier {

  private final LibaseConfig config;
  private final SqlDialect dialect;
  private final ConnectionHelper connection;

  public ChangesApplier(LibaseConfig config, SqlDialect dialect, ConnectionHelper connection) {
    this.config = config;
    this.dialect = dialect;
    this.connection = connection;
  }

  public void apply(ChangeCollector changeCollector) throws Exception {

    makeChangeTableExistence();

    for (DbChange dbChange : changeCollector.changeList) {

      if (wasDbChangeExecuted(dbChange)) {
        checkHash(dbChange);
      } else {
        applyDbChange(dbChange);
      }

    }
  }

  private void applyDbChange(DbChange dbChange) throws Exception {

    long startedAt = System.currentTimeMillis();

    dbChange.apply(connection);

    fixDbChangeExecuted(System.currentTimeMillis() - startedAt, dbChange);

  }

  private void makeChangeTableExistence() throws SQLException {
    if (!dialect.isTableExist(config.changeTable(), connection)) {
      createDbChangeTable();
    }
  }

  private void fixDbChangeExecuted(long executionTime, DbChange dbChange) throws SQLException {
    StringBuilder sb = new StringBuilder("insert into ");
    sb.append(quote(config.changeTable())).append(" (");

    sb.append(quote(config.changeFieldGroup())).append(", ");
    sb.append(quote(config.changeFieldAuthor())).append(", ");
    sb.append(quote(config.changeFieldId())).append(", ");

    sb.append(quote(config.changeFieldHash())).append(", ");
    sb.append(quote(config.changeFieldExecutedAt())).append(", ");
    sb.append(quote(config.changeFieldExecutionMillis()));

    sb.append(") values (?, ?, ?, ?, ?, ?)");

    try (PreparedStatement ps = connection.prepareStatement(sb.toString())) {
      ps.setString(1, dbChange.group());
      ps.setString(2, dbChange.author());
      ps.setString(3, dbChange.id());

      ps.setString(4, dbChange.hash());
      ps.setTimestamp(5, new Timestamp(Calendar.getInstance().getTimeInMillis()));
      ps.setLong(6, executionTime);

      ps.executeUpdate();
    }

  }

  private static String quote(String str) {
    return '"' + str + '"';
  }

  private void createDbChangeTable() throws SQLException {
    StringBuilder sb = new StringBuilder("create table");
    sb.append(quote(config.changeTable())).append('(');

    sb.append(quote(config.changeFieldGroup())).append(' ').append(dialect.varchar(255)).append(" not null");
    sb.append(',');
    sb.append(quote(config.changeFieldAuthor())).append(' ').append(dialect.varchar(255)).append(" not null");
    sb.append(',');
    sb.append(quote(config.changeFieldId())).append(' ').append(dialect.varchar(255)).append(" not null");
    sb.append(',');
    sb.append(quote(config.changeFieldHash())).append(' ').append(dialect.varchar(255));
    sb.append(',');
    sb.append(quote(config.changeFieldExecutionMillis())).append(' ').append(dialect.forLong()).append(" not null");
    sb.append(',');
    sb.append(quote(config.changeFieldExecutedAt())).append(' ').append(dialect.timestamp())
      .append(" default ").append(dialect.currentTimestamp()).append(" not null");

    sb.append(',');
    sb.append("primary key (").append(quote(config.changeFieldGroup())).append(',')
      .append(quote(config.changeFieldAuthor())).append(',').append(quote(config.changeFieldId()))
      .append(')');

    sb.append(')');

    try (PreparedStatement ps = connection.prepareStatement(sb.toString())) {
      ps.executeUpdate();
    }
  }

  private void checkHash(DbChange dbChange) throws SQLException {
    StringBuilder sb = new StringBuilder("select ").append(quote(config.changeFieldHash()));
    sb.append(" from ").append(quote(config.changeTable()));
    sb.append(" where ").append(quote(config.changeFieldGroup())).append(" = ?");
    sb.append(" and   ").append(quote(config.changeFieldAuthor())).append(" = ?");
    sb.append(" and   ").append(quote(config.changeFieldId())).append(" = ?");

    String dbHash = connection.oneStrOrFail(sb.toString(), dbChange.group(), dbChange.author(), dbChange.id());

    if (dbHash.equals(dbChange.hash())) return;
    throw new IllegalHash(dbChange, dbHash);

  }

  private boolean wasDbChangeExecuted(DbChange dbChange) throws SQLException {
    StringBuilder sb = new StringBuilder("select 1");
    sb.append(" from ").append(quote(config.changeTable()));
    sb.append(" where ").append(quote(config.changeFieldGroup())).append(" = ?");
    sb.append(" and   ").append(quote(config.changeFieldAuthor())).append(" = ?");
    sb.append(" and   ").append(quote(config.changeFieldId())).append(" = ?");

    return connection.hasAnyRow(sb.toString(), dbChange.group(), dbChange.author(), dbChange.id());
  }
}
