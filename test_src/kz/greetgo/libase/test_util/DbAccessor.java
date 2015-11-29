package kz.greetgo.libase.test_util;

import kz.greetgo.libase.DbType;
import kz.greetgo.libase.util.ConnectionHelper;

public interface DbAccessor {
  void cleanDb() throws Exception;

  ConnectionHelper getConnection() throws Exception;

  DbType dbType();
}
