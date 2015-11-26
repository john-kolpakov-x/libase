package kz.greetgo.libase.test_util;

public class DbAccessorFactory {

  public String dbName = "default_db";

  public DbAccessorFactory() {
  }

  public DbAccessor createDbAccessor(DbType dbType) {
    switch (dbType) {
      case POSTGRES:
        return new DbAccessorPostgres(dbName);

      case H2:
        return new DbAccessorH2();

      case HSQLDB:
        return new DbAccessorHSQLDB(dbName);

      default:
        throw new IllegalArgumentException("Unknown db type = " + dbType);
    }
  }
}
