package kz.greetgo.libase;

public class Config {

  public String changeTable = "gg_db_change";
  public String changeFieldGroup = "group";
  public String changeFieldAuthor = "author";
  public String changeFieldId = "id";

  public String changeFieldHash = "hash";
  public String changeFieldExecutionMillis = "execution_millis";
  public String changeFieldExecutedAt = "executed_at";

  public String lockTable = "gg_db_change_lock";
  public String lockName = "name";
  public String lockFlag = "flag";
  public String lockLastLockedAt = "last_locked_at";
}
