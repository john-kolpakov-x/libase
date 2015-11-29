package kz.greetgo.libase;

import kz.greetgo.libase.util.ConnectionHelper;

public interface DbChange {
  String group();

  String author();

  String id();

  String hash();

  void apply(ConnectionHelper connectionHelper) throws Exception;

  String identityStr();
}
