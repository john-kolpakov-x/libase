package kz.greetgo.libase.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectionHelper implements AutoCloseable {
  public final Connection connection;

  public ConnectionHelper(Connection connection) {
    this.connection = connection;
  }

  @Override
  public void close() throws Exception {
    connection.close();
  }


  public int oneIntOrFail(String sql, Object... sqlParams) throws SQLException {
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      setParams(ps, sqlParams);

      try (ResultSet rs = ps.executeQuery()) {
        if (!rs.next()) throw new NoData();
        return rs.getInt(1);
      }
    }
  }

  public String oneStrOrFail(String sql, Object... sqlParams) throws SQLException {
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      setParams(ps, sqlParams);

      try (ResultSet rs = ps.executeQuery()) {
        if (!rs.next()) throw new NoData();
        return rs.getString(1);
      }
    }
  }

  public int oneIntOrZero(String sql, Object... sqlParams) throws SQLException {
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      setParams(ps, sqlParams);

      try (ResultSet rs = ps.executeQuery()) {
        if (!rs.next()) return 0;
        return rs.getInt(1);
      }
    }
  }

  public int exec(String sql, Object... sqlParams) throws SQLException {
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      setParams(ps, sqlParams);
      return ps.executeUpdate();
    }
  }

  private static void setParams(PreparedStatement ps, Object[] sqlParams) throws SQLException {
    int index = 1;
    for (Object sqlParam : sqlParams) {
      ps.setObject(index++, sqlParam);
    }
  }

  public PreparedStatement prepareStatement(String sql) throws SQLException {
    return connection.prepareStatement(sql);
  }

  public int update(String sql, Object... sqlParams) throws SQLException {
    try (PreparedStatement ps = prepareStatement(sql)) {
      setParams(ps, sqlParams);
      return ps.executeUpdate();
    }
  }

  public boolean hasAnyRow(String sql, Object... sqlParams) throws SQLException {
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      setParams(ps, sqlParams);

      try (ResultSet rs = ps.executeQuery()) {
        return rs.next();
      }
    }
  }
}
