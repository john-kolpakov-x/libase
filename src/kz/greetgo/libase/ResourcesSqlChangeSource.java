package kz.greetgo.libase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

public class ResourcesSqlChangeSource implements ChangeIterable {

  private final ChangeList changeList = new ChangeList();

  @Override
  public Iterator<DbChange> iterator() {
    return changeList.iterator();
  }

  private String getGroup() {
    return getClass().getName();
  }

  private InputStream getResource(String resourceName) {
    Class<?> cl = getClass();
    return cl.getResourceAsStream(resourceName);
  }

  private static final String AUTHOR_PREFIX = "--author ";

  protected final void add(final String resourceName) {
    changeList.addAll(new ChangeIterable() {

      String sqls[] = null, author = null;

      void init() {
        if (sqls != null) return;

        try (InputStream inputStream = getResource(resourceName)) {
          if (inputStream == null) throw new RuntimeException("No resource " + resourceName + " in " + getGroup());
          final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

          StringBuilder sb = new StringBuilder();
          while (true) {
            String line = reader.readLine();
            if (line == null) break;

            if (author == null) {
              if (!line.toLowerCase().startsWith(AUTHOR_PREFIX)) throw new RuntimeException("Resource " + resourceName
                + " in " + getGroup() + " at the first line must contain " + AUTHOR_PREFIX + "<your name>");

              author = line.substring(AUTHOR_PREFIX.length()).trim();
              continue;
            }

            sb.append(line).append("\n");
          }

          sqls = sb.toString().split(";;");

        } catch (IOException e) {
          throw new RuntimeException(e);
        }

      }

      @Override
      public Iterator<DbChange> iterator() {
        init();
        return new SqlChangeIterable(getGroup(), author, resourceName, sqls).iterator();
      }
    });
  }

  protected final void include(final Class<? extends ResourcesSqlChangeSource> reference) {
    changeList.addAll(new ChangeIterable() {

      ResourcesSqlChangeSource cache = null;

      @Override
      public Iterator<DbChange> iterator() {

        if (cache == null) {
          try {
            cache = reference.newInstance();
          } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
          }
        }

        return cache.iterator();
      }
    });
  }

}
