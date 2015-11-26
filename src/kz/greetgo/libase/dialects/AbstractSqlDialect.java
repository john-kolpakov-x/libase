package kz.greetgo.libase.dialects;

import kz.greetgo.libase.DbType;
import kz.greetgo.libase.SqlDialect;

public abstract class AbstractSqlDialect implements SqlDialect {
  public static SqlDialect createBy(DbType dbType) {

    switch (dbType) {

      case POSTGRES:
        return new SqlDialectPostgres();

      case H2:
        return new SqlDialectH2();

      case HSQLDB:
        return new SqlDialectHSQLDB();

      default:
        throw new IllegalArgumentException("Unknown dbType = " + dbType);

    }

  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + " sql dialect";
  }
}
