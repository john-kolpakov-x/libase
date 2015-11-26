package kz.greetgo.libase.test_util;

import org.postgresql.util.PSQLException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.regex.Pattern;

class DbAccessorPostgres implements DbAccessor {
  private static final Pattern DOES_NOT_EXISTS = Pattern.compile("(role|database).+does\\s+not\\s+exist");
  private static final Pattern LEFT_PASSWORD = Pattern.compile("password authentication failed");
  private final String dbName;

  public DbAccessorPostgres(String dbName) {
    this.dbName = dbName;
  }

  private Connection connectToAdmin() throws Exception {
    String host = System.getenv("PG_HOST");
    String port = System.getenv("PG_PORT");
    String adminDbName = System.getenv("PG_ADMIN");
    String admin = System.getenv("PG_ADMIN");
    String password = System.getenv("PG_ADMIN_PASSWORD");

    String url = String.format("jdbc:postgresql://%s:%s/%s", host, port, adminDbName);

    return DriverManager.getConnection(url, admin, password);
  }

  @Override
  public void cleanDb() throws Exception {
    try (Connection connection = connectToAdmin()) {

      try (PreparedStatement ps = connection.prepareStatement("drop database " + dbName)) {
        ps.executeUpdate();
      } catch (PSQLException e) {
        if (!DOES_NOT_EXISTS.matcher(e.getMessage()).find()) throw e;
        //ignore and go on
      }
      try (PreparedStatement ps = connection.prepareStatement("drop user " + dbName)) {
        ps.executeUpdate();
      } catch (PSQLException e) {
        if (!DOES_NOT_EXISTS.matcher(e.getMessage()).find()) throw e;
        //ignore and go on
      }

      try (PreparedStatement ps = connection.prepareStatement("create user " + dbName
        + " ENCRYPTED password '" + dbName + "'")) {
        ps.executeUpdate();
      }

      try (PreparedStatement ps = connection.prepareStatement("create database " + dbName + " with owner " + dbName)) {
        ps.executeUpdate();
      }
    }
  }

  @Override
  public Connection getConnection() throws Exception {

    Class.forName("org.postgresql.Driver");

    try {
      return connectTo();
    } catch (PSQLException e) {
      if (leftConnectionMessage(e.getMessage())) throw e;
    }

    cleanDb();

    return connectTo();
  }

  private boolean leftConnectionMessage(String message) {
    if (DOES_NOT_EXISTS.matcher(message).find()) return false;
    if (LEFT_PASSWORD.matcher(message).find()) return false;
    return true;
  }

  private Connection connectTo() throws Exception {
    String host = System.getenv("PG_HOST");
    String port = System.getenv("PG_PORT");

    String url = String.format("jdbc:postgresql://%s:%s/%s", host, port, dbName);

    return DriverManager.getConnection(url, dbName, dbName);
  }

  public static void main(String[] args) throws Exception {
    DbAccessorPostgres x = new DbAccessorPostgres("asd_111");
    try (Connection con = x.getConnection()) {
      System.out.println(con);
    }
  }

}
