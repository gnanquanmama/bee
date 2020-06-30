package com.mcoding.bee.base.jdbc;

import com.mcoding.bee.biz.user.UserDao;
import org.junit.Test;

import java.util.List;

/**
 * @author wzt on 2020/6/30.
 * @version 1.0
 */
public class UserTest {

    @Test
    public void getUserList() {
        UserDao userDao = new UserDao();

        List<Object> userList = userDao.selectList("queryUserByName", "fish");
        System.out.println(userList);

    }


}
