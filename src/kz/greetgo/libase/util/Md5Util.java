package kz.greetgo.libase.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Md5Util {
  public static String strToMd5(String str) {
    try {
      return strToMd5Inner(str);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static final MessageDigest MD5 = createMd5();

  private static MessageDigest createMd5() {
    try {
      return MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

  private static String strToMd5Inner(String str) throws Exception {
    byte[] bytes = MD5.digest(str.getBytes("UTF-8"));
    return Base64.getEncoder().encodeToString(bytes);
  }
}
