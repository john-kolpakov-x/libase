package kz.greetgo.libase;

import kz.greetgo.libase.dialects.AbstractSqlDialect;
import kz.greetgo.libase.util.ConnectionHelper;

import java.sql.Connection;
import java.util.logging.Logger;

public class Libase {

  private final static Logger LOGGER = Logger.getLogger(Libase.class.getName());

  public static void execute(Connection connection, LibaseConfig config, ChangeIterable changeIterable) throws Exception {

    long startedAt = System.currentTimeMillis();

    DbType dbType = DetectDbType.byConnection(connection);
    SqlDialect sqlDialect = AbstractSqlDialect.createBy(dbType);

    ConnectionHelper connectionHelper = new ConnectionHelper(connection);

    ChangesApplier applier = new ChangesApplier(config, sqlDialect, connectionHelper);

    Locker locker = new Locker(config, connectionHelper, sqlDialect);

    LOGGER.info("Start Libase on " + dbType + " with " + changeIterable.getClass().getName());

    {
      int tryCount = 0;
      while (!locker.tryLock()) {

        if (++tryCount >= config.tryLockCount()) throw new TooManyTryLockLibaseCount(tryCount);

        LOGGER.info("Lock denied. Waiting for " + config.millisToWaitNextTryToLock() + " millis to lock again...");

        try {
          Thread.sleep(config.millisToWaitNextTryToLock());
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }

      }
    }

    LOGGER.info("Locked successfully. Applying db changes " + changeIterable.getClass().getName());

    try {

      applier.apply(changeIterable);

    } finally {
      locker.unlock();
      LOGGER.info("Unlocked successfully. Total execution time is "
        + (System.currentTimeMillis() - startedAt) + " millis");
    }

  }
}
