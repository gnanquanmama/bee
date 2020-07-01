package com.mcoding.bee.base.jdbc;

import com.google.common.collect.Lists;
import com.mcoding.bee.base.jdbc.JdbcUtils;
import org.apache.ibatis.type.SimpleTypeRegistry;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author wzt on 2020/6/30.
 * @version 1.0
 */
public class CommonDao {

    private static Properties prop = new Properties();

    static {
        InputStream in = JdbcUtils.class.getClassLoader().getResourceAsStream("myjdbc/user.properties");
        try {
            prop.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> List<T> selectList(String sqlId, Object param) {
        String preSql = prop.getProperty(sqlId);
        String resultTypeClassName = prop.getProperty(sqlId + ".resultclassname");

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = JdbcUtils.getConnection();
            statement = connection.prepareStatement(preSql);

            if (SimpleTypeRegistry.isSimpleType(param.getClass())) {
                statement.setObject(1, param);
            } else if (param instanceof Map) {
                Map<String, Object> paramMap = (Map<String, Object>) param;

                String columnNames = prop.getProperty(sqlId + ".columnnames");
                String[] columnNameArray = columnNames.split(",");

                for (int i = 1; i <= columnNameArray.length; i++) {
                    statement.setObject(i, paramMap.get(columnNameArray[i - 1]));
                }
            }

            resultSet = statement.executeQuery();

            List<T> resultList = Lists.newArrayList();

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
                resultList.add((T) record);
            }
            return resultList;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            JdbcUtils.release(connection, statement, resultSet);
        }
    }
}
