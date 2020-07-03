package com.cnnc.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionImpl implements IConnection {

    private static int initConnections = 5;
    private static int maxConnections = 10;
    private static int minConnections = 1;
    private static int maxActive = 10;
    private static int connTimeout = 1000;

    private static String driverName = "com.mysql.cj.jdbc.Driver";
    private static String url = "jdbc:mysql://localhost:3306/test?serverTimezone=UTC";
    private static String username = "root";
    private static String password = "root";

    private List<Connection> freeConnection = new CopyOnWriteArrayList<>();
    private List<Connection> activeConnection = new CopyOnWriteArrayList<>();

    private AtomicInteger count;

    public ConnectionImpl() {
        this.count = new AtomicInteger(0);
        init();
    }

    public void init() {
        for (int i = 0; i < initConnections; i++) {
            Connection connection = newConnection();
            if (connection != null) {
                freeConnection.add(connection);
            }
        }
    }

    public Connection newConnection() {
        try {
            Class.forName(driverName);
            Connection connection = DriverManager.getConnection(url, username, password);
            count.addAndGet(1);
            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            return null;
        }
    }

    public boolean isAvailable(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public synchronized Connection getConnection() {
        Connection connection = null;
        if (count.get() < maxActive) {
            if (freeConnection.size() > 0) {
                connection = freeConnection.remove(0);
            } else {
                connection = newConnection();
            }
            if (isAvailable(connection)) {
                activeConnection.add(connection);
            } else {
                count.decrementAndGet();
                connection = newConnection();
            }
        } else {
            try {
                wait(connTimeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    @Override
    public synchronized void releaseConnection(Connection connection) {
        if (isAvailable(connection)) {
            if (freeConnection.size() < maxConnections) {
                freeConnection.add(connection);
            } else {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            activeConnection.remove(connection);
            count.decrementAndGet();
            notifyAll();
        } else {
            throw new RuntimeException("连接回收异常");
        }
    }
}
