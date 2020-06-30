package com.mcoding.bee.biz.user;

import com.google.common.collect.Lists;
import com.mcoding.bee.base.jdbc.JdbcUtils;
import org.apache.ibatis.type.SimpleTypeRegistry;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.List;
import java.util.Properties;

/**
 * @author wzt on 2020/6/30.
 * @version 1.0
 */
public class UserDao {

    private static Properties prop = new Properties();

    static {
        InputStream in = JdbcUtils.class.getClassLoader().getResourceAsStream("myjdbc/user.properties");
        try {
            prop.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Object> selectList(String sqlId, String name) {
        String preSql = prop.getProperty(sqlId);
        String resultTypeClassName = prop.getProperty(sqlId + ".resultclassname");

        ResultSet resultSet = null;

        try (Connection connection = JdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(preSql)) {

            if (SimpleTypeRegistry.isSimpleType(name.getClass())) {
                statement.setObject(1, name);
            }

            List<Object> resultList = Lists.newArrayList();

            resultSet = statement.executeQuery();
            while (resultSet.next()) {

                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                Class<?> resultTypeClass = Class.forName(resultTypeClassName);

                Object record = resultTypeClass.newInstance();

                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object columnValue = resultSet.getObject(i);

                    Field field = resultTypeClass.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(record, columnValue);
                }

                resultList.add(record);
            }
            return resultList;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
