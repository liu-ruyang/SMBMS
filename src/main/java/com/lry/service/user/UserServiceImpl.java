package com.lry.service.user;

import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.lry.dao.BaseDao;
import com.lry.dao.user.UserDao;
import com.lry.dao.user.UserDaoImpl;
import com.lry.pojo.User;

/**
 * @author 刘汝杨
 */
public class UserServiceImpl implements UserService {
    /**
     * 业务层都会调用Dao层，所以我们要引入Dao层
     */
    private UserDao userDao;

    public UserServiceImpl() {
        userDao = new UserDaoImpl();
    }

    /**
     * 用户登录，登录成功返回user信息，否则返回空指针
     *
     * @param userCode
     * @param password
     * @return
     */
    @Override
    public User login(String userCode, String password) {
        User user = null;
        Connection connection = null;

        connection = BaseDao.getConnection();
        //通过业务层调用对应的具体的数据库操作
        user = userDao.getLoginUser(connection, userCode);

        //密码错误
        if (!user.getUserPassword().equals(password)) {
            user = null;
        }

        //释放资源
        BaseDao.closeResource(connection, null, null);

        return user;
    }

    /**
     * 更新密码，更改成功返回true，否则返回false
     *
     * @param id
     * @param pwd
     * @return
     */
    @Override
    public boolean updatePwd(int id, String pwd) {
        boolean flag = false;
        int affectRows = 0;

        Connection connection = BaseDao.getConnection();
        affectRows = userDao.updatePwd(connection, id, pwd);

        if (affectRows > 0) {
            flag = true;
        }

        BaseDao.closeResource(connection, null, null);

        return flag;
    }

    /**
     * 返回查询用户数量
     *
     * @param username
     * @param userRole
     * @return
     */
    @Override
    public int getUserCount(String username, int userRole) {
        int count = 0;
        Connection connection = null;
        connection = BaseDao.getConnection();

        count = userDao.getUserCount(connection, username, userRole);

        //释放资源
        BaseDao.closeResource(connection, null, null);

        return count;
    }

    /**
     * 根据条件查询用户，返回用户List集合
     *
     * @param queryUserName
     * @param queryUserRole
     * @param currentPageNo
     * @param pageSize
     * @return
     */
    @Override
    public List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize) {
        List<User> userList = null;
        Connection connection = null;

        System.out.println("queryUserName ---- > " + queryUserName);
        System.out.println("queryUserRole ---- > " + queryUserRole);
        System.out.println("currentPageNo ---- > " + currentPageNo);
        System.out.println("pageSize ---- > " + pageSize);

        connection = BaseDao.getConnection();

        userList = userDao.getUserList(connection, queryUserName, queryUserRole, currentPageNo, pageSize);

        BaseDao.closeResource(connection, null, null);

        return userList;
    }

    /**
     * 判断userCode是否已经存在
     *
     * @param userCode
     * @return
     */
    @Override
    public boolean userCodeExist(String userCode) {
        boolean flag = false;

        Connection connection = BaseDao.getConnection();

        //调用查询函数
        flag = userDao.userCodeExist(connection, userCode);

        //释放资源
        BaseDao.closeResource(connection, null, null);

        return flag;
    }

    /**
     * 添加用户信息
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
    @Override
    public int addUser(String userCode, String userName, String userPassword, String gender, String birthday, String phone,
                       String address, String userRole, int createrId) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date birthdayDate = null;
        try {
            birthdayDate = simpleDateFormat.parse(birthday);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int succeeded = 0;

        User user = new User();
        user.setUserCode(userCode);
        user.setUserName(userName);
        user.setUserPassword(userPassword);
        user.setGender(Integer.parseInt(gender));
        user.setBirthday(birthdayDate);
        user.setPhone(phone);
        user.setAddress(address);
        user.setUserRole(Integer.parseInt(userRole));
        user.setCreatedBy(createrId);

        Connection connection = BaseDao.getConnection();

        succeeded = userDao.addUser(connection, user);

        BaseDao.closeResource(connection, null, null);

        return succeeded;
    }

    /**
     * 根据userId查询用户信息
     *
     * @param id
     * @return
     */
    @Override
    public User getUserById(String id) {
        User user = null;
        int userId = Integer.parseInt(id);

        Connection connection = BaseDao.getConnection();
        //此处的user不需要实例化
        user = userDao.getUserById(connection, userId);

        BaseDao.closeResource(connection, null, null);
        return user;
    }

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
     * @param operaterId
     * @return
     */
    @Override
    public int modifyUser(String uid, String userName, String gender, String birthday, String phone, String address, String userRole
            , int operaterId) {
        int affectRows = 0;
        Connection connection = BaseDao.getConnection();

        User user = new User();
        user.setId(Integer.parseInt(uid));
        user.setUserName(userName);
        user.setGender(Integer.parseInt(gender));

        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            date = format.parse(birthday);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.setBirthday(date);

        user.setPhone(phone);
        user.setAddress(address);
        user.setUserRole(Integer.parseInt(userRole));

        affectRows = userDao.modifyUser(connection, user, operaterId);

        BaseDao.closeResource(connection, null, null);
        return affectRows;
    }

    /**
     * 删除指定用户
     *
     * @param uid
     * @return
     */
    @Override
    public int delUser(int uid) {
        int affectRows = 0;

        Connection connection = BaseDao.getConnection();
        affectRows = userDao.delUser(connection, uid);

        return affectRows;
    }
}
