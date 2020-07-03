package com.cnnc.mysql;

import java.sql.Connection;

public interface IConnection {

    Connection getConnection();

    void releaseConnection(Connection connection);

}
