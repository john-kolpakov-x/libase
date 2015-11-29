package kz.greetgo.libase;

import kz.greetgo.libase.resources.SqlResourceDeep;
import kz.greetgo.libase.test_util.DbAccessor;
import kz.greetgo.libase.test_util.DbAccessorFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Formatter;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;

public class LibaseTest {

  private final DbAccessorFactory dbAccessorFactory = new DbAccessorFactory();

  private final LibaseConfig config = new ConfigForTests();

  @DataProvider
  public Object[][] connectionDataProvider() {
    List<Object[]> ret = new ArrayList<>();

    for (DbType dbType : DbType.values()) {
      ret.add(new Object[]{dbAccessorFactory.createDbAccessor(dbType)});
    }

    return ret.toArray(new Object[ret.size()][]);
  }

  @Test(dataProvider = "connectionDataProvider")
  public void execute(DbAccessor dbAccessor) throws Exception {

    StringBuilder sb = new StringBuilder();
    sb.append("# Настройки глобального логгера\n");
    sb.append("handlers = java.util.logging.ConsoleHandler\n");
    sb.append(".level = ALL\n");
    sb.append("# Конфигурация файлового хендлера\n");
    sb.append("java.util.logging.FileHandler.level = ALL\n");
    //sb.append("java.util.logging.FileHandler.formatter = java.util.logging.SimpleFormatter\n");
    sb.append("java.util.logging.FileHandler.formatter = kz.greetgo.libase.TestFormatter\n");
    sb.append("java.util.logging.FileHandler.limit = 1000000\n");
    sb.append("java.util.logging.FileHandler.pattern = log.txt\n");
    sb.append("# Конфигурация консольного хендлера\n");
    sb.append("java.util.logging.ConsoleHandler.level = ALL\n");
    sb.append("java.util.logging.ConsoleHandler.pattern = log.log\n");
    sb.append("java.util.logging.ConsoleHandler.formatter = kz.greetgo.libase.LibaseLogFormatter\n");

    LogManager.getLogManager().readConfiguration(new ByteArrayInputStream(sb.toString().getBytes("UTF-8")));

    dbAccessor.cleanDb();

    try (Connection connection = dbAccessor.getConnection().connection) {

      Libase.execute(connection, config, new SqlResourceDeep());

      Libase.execute(connection, config, new SqlResourceDeep());

    }
  }
}