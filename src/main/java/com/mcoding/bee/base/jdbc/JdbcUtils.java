package com.mcoding.bee.base.jdbc;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * @author wzt on 2020/6/30.
 * @version 1.0
 */
public class JdbcUtils {


    private static String url = null;
    private static String username = null;
    private static String password = null;

    static {
        try {
            InputStream in = JdbcUtils.class.getClassLoader().getResourceAsStream("db.properties");
            Properties prop = new Properties();
            prop.load(in);

            String driver = prop.getProperty("driver");
            Class.forName(driver);

            url = prop.getProperty("url");
            username = prop.getProperty("username");
            password = prop.getProperty("password");

        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }


    public static Connection getConnection() {

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("获取连接异常");
        }

        return connection;
    }

    public static Statement createStatement() {
        try {
            return getConnection().createStatement();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("获取连接异常");
        }
    }

    public static PreparedStatement createPrepareStatement(String sql) {
        try {
            return getConnection().prepareStatement(sql);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("获取连接异常");
        }
    }

    public static void release(Connection conn, Statement st, ResultSet rs) {

        if (rs != null) {
            try {
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (st != null) {
            try {
                st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
