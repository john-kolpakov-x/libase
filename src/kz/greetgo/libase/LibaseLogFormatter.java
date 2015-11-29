package kz.greetgo.libase;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LibaseLogFormatter extends Formatter {

  private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
  private final Date dat = new Date();

  @Override
  public synchronized String format(LogRecord record) {
    dat.setTime(record.getMillis());

    StringBuilder sb = new StringBuilder();
    sb.append(sdf.format(dat));

    sb.append(" [LIBASE] ").append(formatMessage(record));
    if (record.getThrown() != null) {
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      pw.println();
      record.getThrown().printStackTrace(pw);
      pw.close();
      sb.append(sw.toString());
    }
    sb.append("\n");

    return sb.toString();
  }
}
