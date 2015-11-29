package kz.greetgo.libase;

public class TooManyTryLockLibaseCount extends RuntimeException {
  public final int tryCount;

  public TooManyTryLockLibaseCount(int tryCount) {
    super("tryCount = " + tryCount);
    this.tryCount = tryCount;
  }
}
