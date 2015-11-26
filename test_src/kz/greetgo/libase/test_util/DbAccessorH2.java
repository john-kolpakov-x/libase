package kz.greetgo.libase.test_util;

import kz.greetgo.libase.DbType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

class DbAccessorH2 extends DbAccessorAbstract {

  @Override
  public void cleanDb() throws Exception {
  }

  @Override
  public Connection getConnection() throws Exception {
    Class.forName("org.h2.Driver");
    return DriverManager.getConnection("jdbc:h2:mem:", "sa", "");
  }

  public static void main(String[] args) throws Exception {
    DbAccessorH2 da = new DbAccessorH2();
    try (Connection connection = da.getConnection()) {
      try (PreparedStatement ps = connection.prepareStatement("create table asd(id int, name varchar(100))")) {
        ps.executeUpdate();
      }
      try (PreparedStatement ps = connection.prepareStatement("insert into asd values (1,'asd')")) {
        ps.executeUpdate();
      }
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
