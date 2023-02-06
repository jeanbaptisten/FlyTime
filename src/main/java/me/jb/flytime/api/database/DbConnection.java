package me.jb.flytime.api.database;

import java.sql.Connection;
import java.sql.SQLException;
import javax.inject.Singleton;

public interface DbConnection {
  void open() throws SQLException, ClassNotFoundException;

  void close() throws SQLException;

  boolean isOpened() throws SQLException;

  Connection getConnection();
}
