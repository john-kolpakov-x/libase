package kz.greetgo.libase;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class TestFormatter extends Formatter {

  private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
  private final Date dat = new Date();

  @Override
  public synchronized String format(LogRecord record) {
    dat.setTime(record.getMillis());

    StringBuilder sb=new StringBuilder();
    sb.append(sdf.format(dat));

    sb.append(" [");
    if (record.getSourceClassName() != null) {
      sb.append(record.getSourceClassName());
      if (record.getSourceMethodName() != null) {
        sb.append(' ').append(record.getSourceMethodName());
      }
    } else {
      sb.append(record.getLoggerName());
    }
    sb.append("] ").append(formatMessage(record));
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
