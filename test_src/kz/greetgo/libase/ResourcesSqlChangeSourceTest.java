package kz.greetgo.libase;

import kz.greetgo.libase.resources.SqlResourceDeep;
import kz.greetgo.libase.resources.simple.SqlResourceSimple;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ResourcesSqlChangeSourceTest {

  @Test
  public void checkSqlResourceSimple() throws Exception {
    ResourcesSqlChangeSource r = new ResourcesSqlChangeSource();
    r.include(SqlResourceSimple.class);

    List<String> list = new ArrayList<>();

    for (DbChange dbChange : r) {
      list.add(dbChange.toString());
    }

    assertThat(list).hasSize(4);
    assertThat(list.get(0)).isEqualTo("SqlChange(create table asd1 (id int))");
    assertThat(list.get(1)).isEqualTo("SqlChange(create table asd2 (id int))");
    assertThat(list.get(2)).isEqualTo("SqlChange(create table asd3 (id int))");
    assertThat(list.get(3)).isEqualTo("SqlChange(create table asd4 (id int))");
  }

  @Test
  public void checkSqlResourceDeep() throws Exception {
    ResourcesSqlChangeSource r = new ResourcesSqlChangeSource();
    r.include(SqlResourceDeep.class);

    List<String> list = new ArrayList<>();

    for (DbChange dbChange : r) {
      list.add(dbChange.toString());
    }

//    list.forEach(System.out::println);

    assertThat(list).hasSize(8);
    assertThat(list.get(0)).isEqualTo("SqlChange(create table top_asd11 (id int))");
    assertThat(list.get(1)).isEqualTo("SqlChange(create table top_asd12 (id int))");
    assertThat(list.get(2)).isEqualTo("SqlChange(create table asd1 (id int))");
    assertThat(list.get(3)).isEqualTo("SqlChange(create table asd2 (id int))");
    assertThat(list.get(4)).isEqualTo("SqlChange(create table asd3 (id int))");
    assertThat(list.get(5)).isEqualTo("SqlChange(create table asd4 (id int))");
    assertThat(list.get(6)).isEqualTo("SqlChange(create table top_asd13 (id int))");
    assertThat(list.get(7)).isEqualTo("SqlChange(create table top_asd14 (id int))");
  }

}
