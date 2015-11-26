package kz.greetgo.libase.test_util;

public abstract class DbAccessorAbstract implements DbAccessor {
  @Override
  public String toString() {
    return getClass().getSimpleName();
  }
}
