package kz.greetgo.libase;

import kz.greetgo.libase.dialects.AbstractSqlDialect;
import kz.greetgo.libase.test_util.DbAccessor;
import kz.greetgo.libase.test_util.DbAccessorFactory;
import kz.greetgo.libase.util.ConnectionHelper;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.assertj.core.api.Assertions.assertThat;

public class LockerTest {

  private final DbAccessorFactory dbAccessorFactory = new DbAccessorFactory();

  private final Config config = new Config();

  @DataProvider
  public Object[][] connectionDataProvider() {
    List<Object[]> ret = new ArrayList<>();

    for (DbType dbType : DbType.values()) {
      ret.add(new Object[]{dbAccessorFactory.createDbAccessor(dbType), dbType});
    }

    return ret.toArray(new Object[ret.size()][]);
  }

  @Test(dataProvider = "connectionDataProvider")
  public void tryLockToChange(DbAccessor dbAccessor, DbType dbType) throws Exception {

    if (dbType != DbType.POSTGRES) throw new SkipException("Cannot lock for dbType = " + dbType);

    dbAccessor.cleanDb();

    final SqlDialect sqlDialect = AbstractSqlDialect.createBy(dbType);

    try (ConnectionHelper connection = dbAccessor.getConnection()) {
      connection.exec("create table tables (name varchar(100))");
    }

    final ConcurrentLinkedQueue<String> log = new ConcurrentLinkedQueue<>();

    Thread[] threads = new Thread[50];
    boolean okArray[] = new boolean[threads.length];

    for (int i = 0, C = threads.length; i < C; i++) {
      final int I = i;
      threads[i] = new Thread(() -> {

        try {
          try (ConnectionHelper connection = dbAccessor.getConnection()) {

            Locker locker = new Locker(config, connection, sqlDialect);

            while (!locker.tryLock()) {
//              System.err.println("Waiting for lock from thread " + I + " ...");
              Thread.sleep(300);
            }

            log.add("Started changes in thread " + I);

            String tableName = "asd" + (I % 5);

            int count = connection.oneIntOrFail("select count(1) from tables where name = ?", tableName);

            if (count == 0) {
              connection.exec("create table " + tableName + " (id int)");

              connection.exec("insert into tables (name) values (?)", tableName);
            }

            log.add("Completed changes in thread " + I);

            locker.unlock();

            okArray[I] = true;
          }
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      });
    }

    for (Thread thread : threads) {
      thread.start();
    }
    for (Thread thread : threads) {
      thread.join();
    }

    {
      int index = 0;
      for (boolean ok : okArray) {
        assertThat(ok).describedAs("Crash in thread number %s", index++).isTrue();
      }
    }

    for (String s : log) {
      System.out.println(s);
    }

  }


}
