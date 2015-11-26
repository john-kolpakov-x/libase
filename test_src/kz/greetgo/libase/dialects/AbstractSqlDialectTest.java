package kz.greetgo.libase.dialects;

import kz.greetgo.libase.DbType;
import kz.greetgo.libase.SqlDialect;
import kz.greetgo.libase.test_util.DbAccessor;
import kz.greetgo.libase.test_util.DbAccessorFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class AbstractSqlDialectTest {

  private final DbAccessorFactory dbAccessorFactory = new DbAccessorFactory();

  @Test
  public void createBy_postgres() throws Exception {

    SqlDialect dia = AbstractSqlDialect.createBy(DbType.POSTGRES);

    assertThat(dia).isInstanceOf(SqlDialectPostgres.class);

  }

  @Test
  public void createBy_h2() throws Exception {

    SqlDialect dia = AbstractSqlDialect.createBy(DbType.H2);

    assertThat(dia).isInstanceOf(SqlDialectH2.class);

  }

  @Test
  public void createBy_hsqldb() throws Exception {

    SqlDialect dia = AbstractSqlDialect.createBy(DbType.HSQLDB);

    assertThat(dia).isInstanceOf(SqlDialectHSQLDB.class);

  }

  @DataProvider
  private Object[][] connectionsDataProvider() throws Exception {
    Object[][] ret = new Object[DbType.values().length][];

    for (DbType dbType : DbType.values()) {

      ret[dbType.ordinal()] = new Object[]{dbAccessorFactory.createDbAccessor(dbType)};

    }

    return ret;
  }

  @Test(dataProvider = "connectionsDataProvider")
  public void isTableExist(DbAccessor dba) throws Exception {

    dba.cleanDb();

    SqlDialect dia = AbstractSqlDialect.createBy(dba.dbType());

    try (Connection connection = dba.getConnection()) {

      exec(connection, "create table \"asd\" (id int)");

      assertThat(dia.isTableExist("asd", connection)).isTrue();

    }

  }

  private void exec(Connection connection, String sql) throws SQLException {
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.executeUpdate();
    }
  }
}
