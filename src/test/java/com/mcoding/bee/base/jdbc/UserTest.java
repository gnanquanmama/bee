package com.mcoding.bee.base.jdbc;

import com.google.common.collect.Maps;
import com.mcoding.bee.biz.user.User;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * @author wzt on 2020/6/30.
 * @version 1.0
 */
public class UserTest {

    @Test
    public void getUserList() {
        CommonDao commonDao = new CommonDao();

        List<User> userList = commonDao.selectList("queryUserByName", "fish");
        System.out.println(userList);

    }

    @Test
    public void getUserListByNameAndAge() {
        CommonDao commonDao = new CommonDao();

        Map<String, Object> param = Maps.newHashMap();
        param.put("name", "fish");
        param.put("age", 68);
        List<User> userList = commonDao.selectList("queryUserByNameAndAge", param);
        System.out.println(userList);

    }


}
