package kz.greetgo.libase;

public class IllegalHash extends RuntimeException {
  public final DbChange dbChange;
  public final String hash;

  public IllegalHash(DbChange dbChange, String dbHash) {
    super("Illegal dbHash for " + dbChange.identityStr() + ": nowHash = " + dbChange.hash()
      + ", dbHash = " + dbHash + ", change: " + dbChange.toString());
    this.dbChange = dbChange;
    this.hash = dbHash;
  }
}
