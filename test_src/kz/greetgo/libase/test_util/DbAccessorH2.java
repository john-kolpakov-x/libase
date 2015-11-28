package kz.greetgo.libase.test_util;

import kz.greetgo.libase.DbType;
import kz.greetgo.libase.util.ConnectionHelper;

import java.security.SecureRandom;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

class DbAccessorH2 extends DbAccessorAbstract {

  private static final Random rnd = new SecureRandom();
  private final long mainId = rnd.nextLong();
  private int id = 1;

  @Override
  public void cleanDb() throws Exception {
    id++;
  }

  @Override
  public ConnectionHelper getConnection() throws Exception {
    Class.forName("org.h2.Driver");
    return new ConnectionHelper(DriverManager.getConnection(
      "jdbc:h2:mem:asd" + mainId + '-' + id + ";DB_CLOSE_DELAY=-1;MVCC=TRUE", "sa", ""));
  }

  public static void main(String[] args) throws Exception {
    DbAccessorH2 da = new DbAccessorH2();
    try (ConnectionHelper connection = da.getConnection()) {
      try (PreparedStatement ps = connection.prepareStatement("create table asd(id int, name varchar(100))")) {
        ps.executeUpdate();
      }
      try (PreparedStatement ps = connection.prepareStatement("insert into asd values (1,'asd')")) {
        ps.executeUpdate();
      }
    }

    try (ConnectionHelper connection = da.getConnection()) {
      try (PreparedStatement ps = connection.prepareStatement("select * from asd")) {
        try (ResultSet rs = ps.executeQuery()) {
          while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            System.out.println("id = " + id + ", name = " + name);
          }
        }
      }
    }
  }

  @Override
  public DbType dbType() {
    return DbType.H2;
  }
}
