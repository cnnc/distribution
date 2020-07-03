package com.cnnc.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MysqlLock implements Lock {

    private IConnection connection = null;

    public MysqlLock() {
        connection = new ConnectionImpl();
    }

    @Override
    public void lock() {
        Connection conn = this.connection.getConnection();
        try {
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        while (true) {
            String sql = "select * from method_lock where method_name = 'lock' for update";
            PreparedStatement statement = null;
            try {
                statement = conn.prepareStatement(sql);
                boolean execute = statement.execute();
                if (execute) {
//                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public void unlock() {

    }
}
