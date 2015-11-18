package kz.greetgo.libase;

import java.sql.Connection;

public interface DbChange {
  String group();

  String author();

  String id();

  String hash();

  void apply(Connection connection) throws Exception;

  String identityStr();
}
