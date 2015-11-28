package kz.greetgo.libase;

public class ConfigForTests extends LibaseConfig {
  @Override
  public String changeTable() {
    return "gg_db_change";
  }

  @Override
  public String changeFieldGroup() {
    return "group";
  }

  @Override
  public String changeFieldAuthor() {
    return "author";
  }

  @Override
  public String changeFieldId() {
    return "id";
  }

  @Override
  public String changeFieldHash() {
    return "hash";
  }

  @Override
  public String changeFieldExecutionMillis() {
    return "execution_millis";
  }

  @Override
  public String changeFieldExecutedAt() {
    return "executed_at";
  }

  @Override
  public String lockTable() {
    return "gg_db_change_lock";
  }

  @Override
  public String lockName() {
    return "name";
  }

  @Override
  public String lockFlag() {
    return "flag";
  }

  @Override
  public String lockLastLockedAt() {
    return "last_locked_at";
  }
}

