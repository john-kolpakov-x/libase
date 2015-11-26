package kz.greetgo.libase;

import java.sql.Connection;
import java.sql.DriverManager;

public class ProbePostgresConnect {
  public static void main(String[] args) throws Exception {
    Class.forName("org.postgresql.Driver");
    try (Connection connection = DriverManager.getConnection(
      "jdbc:postgresql://localhost:5432/postgres", "postgres", "")) {

      System.out.println(connection);

    }

    String url = String.format("jdbc:postgresql://%s:%s/%s", "asd", "5432", "wow");

    System.out.println(url);

  }
}
