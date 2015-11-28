package kz.greetgo.libase;

import kz.greetgo.libase.util.ConnectionHelper;

import java.sql.Connection;

public interface DbChange {
  String group();

  String author();

  String id();

  String hash();

  void apply(ConnectionHelper connection) throws Exception;

  String identityStr();
}
