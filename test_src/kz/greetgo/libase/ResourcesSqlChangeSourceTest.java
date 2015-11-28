package kz.greetgo.libase;

import kz.greetgo.libase.resources.SqlResource1;
import org.testng.annotations.Test;

public class ResourcesSqlChangeSourceTest {

  @Test
  public void simple() throws Exception {
    ResourcesSqlChangeSource r = new ResourcesSqlChangeSource();
    r.include(SqlResource1.class);

    for (DbChange dbChange : r) {
      System.out.println(dbChange);
    }
  }

}