package kz.greetgo.libase;

public class ChangeCollector {
  final ChangeList changeList = new ChangeList();

  public class Appender {
    private String group;

    private Appender(String group) {
      this.group = group;
    }

    public void sqls(String author, String prefix, String sqlsDividedByTwoSemicolons) {
      String[] sqls = sqlsDividedByTwoSemicolons.split(";;");
      changeList.addAll(new SqlChangeIterable(group, author, prefix, sqls));
    }
  }

  public Appender toGroup(String group) {
    return new Appender(group);
  }

}
