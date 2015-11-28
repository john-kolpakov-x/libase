package kz.greetgo.libase.resources;

import kz.greetgo.libase.ResourcesSqlChangeSource;

import java.io.InputStream;

public class SqlResource1 extends ResourcesSqlChangeSource {
  public SqlResource1() {
    add("001.sql");
    add("002.sql");
  }

  public static void main(String[] args) {
    InputStream inputStream = SqlResource1.class.getResourceAsStream("001.sql");
    System.out.println(inputStream);
  }
}
