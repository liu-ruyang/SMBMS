package com.lry.service.user;

import java.util.List;

import com.lry.pojo.User;

/**
 * @author 刘汝杨
 */
public interface UserService {

    /**
     * 用户登录
     *
     * @param userCode
     * @param password
     * @return
     */
    User login(String userCode, String password);

    /**
     * @param id
     * @param pwd
     * @return
     */
    boolean updatePwd(int id, String pwd);

    /**
     * @param username
     * @param userRole
     * @return
     */
    int getUserCount(String username, int userRole);

    /**
     * 根据条件查询用户，返回用户List集合
     *
     * @param queryUserName
     * @param queryUserRole
     * @param currentPageNo
     * @param pageSize
     * @return
     */
    List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize);

    /**
     * 判断userCode是否已经存在
     *
     * @param userCode
     * @return
     */
    boolean userCodeExist(String userCode);

    /**
     * 用于添加用户，返回数据修改行数
     *
     * @param userCode
     * @param userName
     * @param userPassword
     * @param gender
     * @param birthday
     * @param phone
     * @param address
     * @param userRole
     * @param createrId
     * @return
     */
    int addUser(String userCode, String userName, String userPassword, String gender, String birthday, String phone, String address,
                String userRole, int createrId);

    /**
     * 根据userId查询用户信息
     *
     * @param id
     * @return
     */
    User getUserById(String id);

    /**
     * 修改用户的信息
     *
     * @param uid
     * @param userName
     * @param gender
     * @param birthday
     * @param phone
     * @param address
     * @param userRole
     * @param OperaterId
     * @return
     */
    int modifyUser(String uid, String userName, String gender, String birthday, String phone, String address, String userRole,
                   int OperaterId);

    /**
     * 删除指定用户
     *
     * @param uid
     * @return
     */
    int delUser(int uid);
}
