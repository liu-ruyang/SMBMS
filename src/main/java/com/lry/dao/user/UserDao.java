package com.lry.dao.user;

import java.sql.Connection;
import java.util.List;

import com.lry.pojo.User;

/**
 * @author 刘汝杨
 */
public interface UserDao {
    /**
     * 在数据库查询将要登录的用户的用户信息
     *
     * @param connection
     * @param username
     * @return
     */
    User getLoginUser(Connection connection, String username);

    /**
     * 执行更新数据库中的密码
     *
     * @param connection
     * @param id
     * @param password
     * @return
     */
    int updatePwd(Connection connection, int id, String password);

    /**
     * 执行查询数据库中用户的数量
     *
     * @param connection
     * @param username
     * @param userRole
     * @return
     */
    int getUserCount(Connection connection, String username, int userRole);

    /**
     * 依照username和userRole查询数据库中的用户，返回一个存储用户的List结合
     *
     * @param connection
     * @param username
     * @param userRole
     * @param currentNo
     * @param pageSize
     * @return
     */
    List<User> getUserList(Connection connection, String username, int userRole, int currentNo, int pageSize);

    /**
     * 根据传入的userCode，查看数据库中是否已经存在这个userCode了
     *
     * @param connection
     * @param userCode
     * @return
     */
    boolean userCodeExist(Connection connection, String userCode);

    /**
     * 添加用户数据
     *
     * @param connection
     * @param user
     * @return
     */
    int addUser(Connection connection, User user);

    /**
     * 根据userId查询返回要修改的用户的信息
     *
     * @param connection
     * @param userId
     * @return
     */
    User getUserById(Connection connection, int userId);

    /**
     * 修改用户信息
     *
     * @param connection
     * @param user
     * @param operaterId
     * @return
     */
    int modifyUser(Connection connection, User user, int operaterId);

    /**
     * 删除指定用户
     *
     * @param connection
     * @param uid
     * @return
     */
    int delUser(Connection connection, int uid);
}
