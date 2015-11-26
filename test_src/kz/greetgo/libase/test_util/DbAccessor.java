package kz.greetgo.libase.test_util;

import java.sql.Connection;

public interface DbAccessor {
  void cleanDb() throws Exception;

  Connection getConnection() throws Exception;
}
