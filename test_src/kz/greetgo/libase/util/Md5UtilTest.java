package kz.greetgo.libase.util;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Md5UtilTest {

  @Test
  public void strToMd5() throws Exception {
    String md5 = Md5Util.strToMd5("asd_wow");
    assertThat(md5).isEqualTo("c4rgBdPSOU7e2RnvNMnIxA==");
  }

}
