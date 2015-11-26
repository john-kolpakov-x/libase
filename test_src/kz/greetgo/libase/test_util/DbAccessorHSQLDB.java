package kz.greetgo.libase.test_util;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

class DbAccessorHSQLDB implements DbAccessor {

  private final Random rnd = new SecureRandom();

  private final long db = rnd.nextLong();
  private int id = 0;
  private final String dbName;

  public DbAccessorHSQLDB(String dbName) {
    this.dbName = dbName;
  }

  @Override
  public void cleanDb() throws Exception {
    id++;
  }

  @Override
  public Connection getConnection() throws Exception {
    Class.forName("org.hsqldb.jdbcDriver");
    return DriverManager.getConnection("jdbc:hsqldb:mem:xx" + db + "-" + id, "sa", "");
  }

  public static void main(String[] args) throws Exception {
    DbAccessorHSQLDB da = new DbAccessorHSQLDB("asd");
    try (Connection connection = da.getConnection()) {
      try (PreparedStatement ps = connection.prepareStatement("create table asd2(id1 int, name1 varchar(100))")) {
        ps.executeUpdate();
      }
      try (PreparedStatement ps = connection.prepareStatement("insert into asd2 values (1,'asd')")) {
        ps.executeUpdate();
      }
    }

    try (Connection connection = da.getConnection()) {
      try (PreparedStatement ps = connection.prepareStatement("select * from asd2")) {
        try (ResultSet rs = ps.executeQuery()) {
          while (rs.next()) {
            int id1 = rs.getInt("id1");
            String name1 = rs.getString("name1");
            System.out.println("id = " + id1 + ", name = " + name1);
          }
        }
      }
    }
  }
}
