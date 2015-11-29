package kz.greetgo.libase;

import kz.greetgo.libase.util.ConnectionHelper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ChangeListTest {

  private static class Change implements DbChange {
    @Override
    public String group() {
      return null;
    }

    @Override
    public String author() {
      return null;
    }

    @Override
    public String id() {
      return null;
    }

    @Override
    public String hash() {
      return null;
    }

    @Override
    public void apply(ConnectionHelper connectionHelper) throws Exception {

    }

    @Override
    public String identityStr() {
      return name;
    }

    public final String name;

    @Override
    public String toString() {
      return name;
    }

    public Change(String name) {
      this.name = name;
    }
  }

  @Test
  public void simple() throws Exception {
    ChangeList cl = new ChangeList();

    cl.add(new Change("one"));
    cl.add(new Change("two"));

    List<String> list = new ArrayList<>();

    for (DbChange dbChange : cl) {
      list.add(dbChange.toString());
    }

    assertThat(list).containsExactly("one", "two");
  }

  @Test
  public void deeper() throws Exception {
    ChangeList changeList1 = new ChangeList();

    changeList1.add(new Change("one"));
    changeList1.add(new Change("two"));

    ChangeList changeList2 = new ChangeList();

    changeList2.add(new Change("three"));
    changeList2.add(new Change("four"));

    changeList1.addAll(changeList2);

    List<String> list = new ArrayList<>();

    for (DbChange dbChange : changeList1) {
      list.add(dbChange.toString());
    }

    System.out.println(list);

    assertThat(list).containsExactly("one", "two", "three", "four");
  }

}