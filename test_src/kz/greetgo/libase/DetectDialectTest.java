package kz.greetgo.libase;

import kz.greetgo.libase.DbType;
import kz.greetgo.libase.DetectDbType;
import kz.greetgo.libase.test_util.DbAccessor;
import kz.greetgo.libase.test_util.DbAccessorFactory;
import kz.greetgo.libase.util.ConnectionHelper;
import org.testng.annotations.Test;

import java.sql.Connection;

import static org.assertj.core.api.Assertions.assertThat;

public class DetectDialectTest {

  private final DbAccessorFactory connectionFactory = new DbAccessorFactory();

  @Test
  public void byConnection_postgres() throws Exception {
    DbAccessor dba = connectionFactory.createDbAccessor(DbType.POSTGRES);

    try (ConnectionHelper connection = dba.getConnection()) {

      DbType dbType = DetectDbType.byConnection(connection.connection);

      assertThat(dbType).isEqualTo(DbType.POSTGRES);

    }
  }

  @Test
  public void byConnection_h2() throws Exception {
    DbAccessor dba = connectionFactory.createDbAccessor(DbType.H2);

    try (ConnectionHelper connection = dba.getConnection()) {

      DbType dbType = DetectDbType.byConnection(connection.connection);

      assertThat(dbType).isEqualTo(DbType.H2);

    }
  }


  @Test
  public void byConnection_hsqldb() throws Exception {
    DbAccessor dba = connectionFactory.createDbAccessor(DbType.HSQLDB);

    try (ConnectionHelper connection = dba.getConnection()) {

      DbType dbType = DetectDbType.byConnection(connection.connection);

      assertThat(dbType).isEqualTo(DbType.HSQLDB);

    }
  }

}
