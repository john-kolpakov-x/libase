package kz.greetgo.libase;

/**
 * Created by pompei on 29.11.15.
 */
public class TooManyTryLockLibaseCount extends RuntimeException {
  public final int tryCount;

  public TooManyTryLockLibaseCount(int tryCount) {
    super("tryCount = " + tryCount);
    this.tryCount = tryCount;
  }
}
