package kz.greetgo.libase;

public class IllegalHash extends RuntimeException {
  public final DbChange dbChange;
  public final String hash;

  public IllegalHash(DbChange dbChange, String hash) {
    super("Illegal hash for " + dbChange.identityStr() + " in db = " + hash);
    this.dbChange = dbChange;
    this.hash = hash;
  }
}
