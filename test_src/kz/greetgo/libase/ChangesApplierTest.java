package kz.greetgo.libase;

import kz.greetgo.libase.dialects.AbstractSqlDialect;
import kz.greetgo.libase.test_util.DbAccessor;
import kz.greetgo.libase.test_util.DbAccessorFactory;
import kz.greetgo.libase.util.ConnectionHelper;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ChangesApplierTest {
  private final DbAccessorFactory dbAccessorFactory = new DbAccessorFactory();

  private final LibaseConfig config = new ConfigForTests();

  @DataProvider
  public Object[][] connectionDataProvider() {
    List<Object[]> ret = new ArrayList<>();

    for (DbType dbType : DbType.values()) {
      ret.add(new Object[]{dbAccessorFactory.createDbAccessor(dbType), AbstractSqlDialect.createBy(dbType)});
    }

    return ret.toArray(new Object[ret.size()][]);
  }

  @Test(dataProvider = "connectionDataProvider")
  public void apply(DbAccessor dbAccessor, SqlDialect sqlDialect) throws Exception {
    dbAccessor.cleanDb();

    ChangeCollector cc = new ChangeCollector();

    cc.toGroup("asd").sqls("me", "wow-1", "create table asd1(id int)");

    try (ConnectionHelper connection = dbAccessor.getConnection()) {
      new ChangesApplier(config, sqlDialect, connection).apply(cc);
    }

    cc.toGroup("asd").sqls("me", "wow-2", "create table asd2(id int)");

    try (ConnectionHelper connection = dbAccessor.getConnection()) {
      new ChangesApplier(config, sqlDialect, connection).apply(cc);
    }

    assertThat(1);
  }

}
