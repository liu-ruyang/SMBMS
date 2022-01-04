package com.lry.dao.user;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.lry.dao.BaseDao;
import com.lry.pojo.User;

/**
 * 对数据库进行操作
 *
 * @author 刘汝杨
 */
public class UserDaoImpl implements UserDao {
    /**
     * 查询有无此账户，若存在，再比较密码，若无或者密码错误，返回User空指针
     */
    @Override
    public User getLoginUser(Connection connection, String userCode) {
        User user = null;
        String sql = "select * from smbms_user where userCode = ?";
        Object[] params = {userCode};

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            resultSet = BaseDao.execute(connection, sql, params, preparedStatement, resultSet);

            //查询到此账户
            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUserCode(resultSet.getString("userCode"));
                user.setUserName(resultSet.getString("userName"));
                user.setUserPassword(resultSet.getString("userPassword"));
                user.setGender(resultSet.getInt("gender"));
                user.setBirthday(resultSet.getDate("birthday"));
                user.setPhone(resultSet.getString("phone"));
                user.setAddress(resultSet.getString("address"));
                user.setUserRole(resultSet.getInt("userRole"));
                user.setCreatedBy(resultSet.getInt("createdBy"));
                user.setCreationDate(resultSet.getTimestamp("creationDate"));
                user.setModifyBy(resultSet.getInt("modifyBy"));
                user.setModifyDate(resultSet.getTimestamp("modifyDate"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            //关闭资源
            BaseDao.closeResource(null, preparedStatement, resultSet);
        }
        return user;
    }

    /**
     * 执行数据库中用户密码的数据更新
     *
     * @param connection
     * @param id
     * @param password
     * @return
     */
    @Override
    public int updatePwd(Connection connection, int id, String password) {
        PreparedStatement preparedStatement = null;
        int execute = 0;

        if (connection != null) {
            String sql = "update smbms_user set userPassword = ? where id = ?";
            Object[] params = {password, id};

            execute = BaseDao.execute(connection, sql, params, preparedStatement, 0);
        }

        //释放资源
        BaseDao.closeResource(null, preparedStatement, null);

        //返回修改行数
        return execute;
    }

    /**
     * 查询用户总数
     *
     * @param connection
     * @param username
     * @param userRole
     * @return
     */
    @Override
    public int getUserCount(Connection connection, String username, int userRole) {
        int count = 0;

        if (connection != null) {
            //StringBuffer类型的字符串对象，内容是可扩充的和修改的
            StringBuffer sql = new StringBuffer();
            sql.append("select count(1) as count from smbms_user u ,smbms_role r where u.userRole = r .id");
            //存放我们的参数
            ArrayList<Object> list = new ArrayList<>();

            if (username != null && username.length() > 0) {
                sql.append(" and u.userName like ?");
                list.add("%" + username + "%");
            }
            if (userRole > 0) {
                sql.append(" and u.userRole = ?");
                list.add(userRole);
            }
            //到这里需要①把sql转换为String类型，②把list转换为Object[]类型
            Object[] params = list.toArray();

            //输出最后完整的sql语句
            System.out.println("UserDaoImpl->getUserCount:" + sql.toString());

            PreparedStatement preparedStatement = null;
            ResultSet rs = null;
            ResultSet resultSet = BaseDao.execute(connection, sql.toString(), params, null, rs);

            try {
                if (resultSet.next()) {
                    count = resultSet.getInt("count");
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            //释放资源
            BaseDao.closeResource(null, preparedStatement, resultSet);
        }
        return count;
    }

    /**
     * 依照username和userRole查询数据库中的用户，返回一个存储用户的List结合
     * 分页查询：数据库的查询使用功能分页查询技术
     *
     * @param connection
     * @param username
     * @param userRole
     * @param currentNo
     * @param pageSize
     * @return
     */
    @Override
    public List<User> getUserList(Connection connection, String username, int userRole, int currentNo, int pageSize) {
        List<User> users = null;

        if (connection != null) {
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;
            //StringBuffer类型的字符串对象，内容是可扩充的和修改的
            StringBuffer sql = new StringBuffer();
            sql.append("select top " + pageSize + " * from smbms_user u ,smbms_role r where u.userRole = r .id");
            //存放我们的参数
            ArrayList<Object> list = new ArrayList<>();

//            list.add(pageSize);   //在java中不支持top后面使用问号作为占位符

            if (username != null && username.length() > 0) {
                sql.append(" and u.userName like ?");
                list.add("%" + username + "%");
            }
            if (userRole > 0) {
                sql.append(" and u.userRole = ?");
                list.add(userRole);
            }
            //分页查询
            sql.append(" and u.id  not in (select top " + (currentNo - 1) * pageSize + " smbms_user.id from smbms_user order by u" +
                    ".id,r.id)order by u.id,r.id");
            //查找之前需要先排序
//            sql.append("order by u.id,r.id");
//            list.add((currentNo - 1) * pageSize);

            //到这里需要①把sql转换为String类型，②把list转换为Object[]类型
            Object[] params = list.toArray();

            //输出最后完整的sql语句
            System.out.println("UserDaoImpl->getUserCount:" + sql.toString());

            resultSet = BaseDao.execute(connection, sql.toString(), params, preparedStatement, resultSet);
            try {
                User user = null;
                users = new ArrayList<>();
                while (resultSet.next()) {
                    user = new User();

                    user.setId(resultSet.getInt(1));
                    user.setUserCode(resultSet.getString("userCode"));
                    user.setUserName(resultSet.getString("userName"));
                    user.setGender(resultSet.getInt("gender"));
                    user.setBirthday(resultSet.getDate("birthday"));
                    user.setPhone(resultSet.getString("phone"));
                    user.setUserRole(resultSet.getInt("userRole"));
                    user.setUserRoleName(resultSet.getString("roleName"));

                    users.add(user);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            //释放资源
            BaseDao.closeResource(null, preparedStatement, resultSet);
        }

        return users;
    }

    /**
     * 查询数据库中的userCode是否已经存在
     *
     * @param connection
     * @param userCode
     * @return
     */
    @Override
    public boolean userCodeExist(Connection connection, String userCode) {
        boolean flag = false;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        StringBuffer sql = new StringBuffer("select * from smbms_user ,smbms_role where smbms_user.userRole =smbms_role.id ");

        ArrayList<Object> list = new ArrayList<>();

        if (userCode != null && userCode.length() > 0) {
            sql.append("and userCode like ?");
            list.add(userCode);
            System.out.println(userCode);
        }

        Object[] params = list.toArray();

        System.out.println(sql.toString());

        resultSet = BaseDao.execute(connection, sql.toString(), params, preparedStatement, resultSet);

        //如果存在，就返回true
        try {
            if (resultSet.next()) {
                flag = true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //释放资源
        BaseDao.closeResource(null, preparedStatement, resultSet);
        return flag;
    }

    /**
     * 添加用户数据
     *
     * @param connection
     * @param user
     * @return
     */
    @Override
    public int addUser(Connection connection, User user) {
        int raws = 0;

        PreparedStatement preparedStatement = null;

        StringBuffer sql1 = new StringBuffer("insert into smbms_user (");
        StringBuffer valuesSql2 = new StringBuffer("values (");

        List<Object> list = new ArrayList<>();

        if (user.getUserCode() != null) {
            sql1.append(" userCode");
            valuesSql2.append("?");
            list.add(user.getUserCode());
            System.out.println("userCode：" + user.getUserCode());
        }
        if (user.getUserName() != null) {
            sql1.append(",userName");
            valuesSql2.append(",?");
            list.add(user.getUserName());
            System.out.println("userName：" + user.getUserName());

        }
        if (user.getUserPassword() != null) {
            sql1.append(",userPassword");
            valuesSql2.append(",?");
            list.add(user.getUserPassword());
            System.out.println("userPassword：" + user.getUserPassword());
        }
        if (user.getGender() != null) {
            sql1.append(",gender");
            valuesSql2.append(",?");
            list.add(user.getGender());
            System.out.println("gender：" + user.getGender());

        }
        if (user.getBirthday() != null) {
            sql1.append(",birthday");
            valuesSql2.append(",?");
            //util.Date与sql.Date的相互转换
            list.add(new Date(user.getBirthday().getTime()));
            System.out.println("birthday：" + user.getBirthday());

        }
        if (user.getPhone() != null) {
            sql1.append(",phone");
            valuesSql2.append(",?");
            list.add(user.getPhone());
            System.out.println("phone：" + user.getPhone());
        }
        if (user.getAddress() != null) {
            sql1.append(",address");
            valuesSql2.append(",?");
            list.add(user.getAddress());
            System.out.println("address：" + user.getAddress());
        }
        if (user.getUserRole() != null) {
            sql1.append(",userRole");
            valuesSql2.append(",?");
            list.add(user.getUserRole());
            System.out.println("userRole：" + user.getUserRole());
        }
        if (user.getCreatedBy() != null) {
            //创建者
            sql1.append(",createdBy");
            valuesSql2.append(",?");
            list.add(user.getCreatedBy());
            System.out.println("createdBy：" + user.getCreatedBy());
        }

        //创建时间
        sql1.append(",creationDate");
        valuesSql2.append(",?");
        list.add(new Date(System.currentTimeMillis()));
        System.out.println("creationDate：" + new Date(System.currentTimeMillis()));


        sql1.append(")");
        valuesSql2.append(")");

        sql1.append(valuesSql2);
        System.out.println(sql1.toString());

        Object[] params = list.toArray();

        raws = BaseDao.execute(connection, sql1.toString(), params, preparedStatement, raws);

        //释放资源
        BaseDao.closeResource(null, preparedStatement, null);

        return raws;
    }

    /**
     * 根据userId查询返回要被修改的用户的信息
     *
     * @param connection
     * @param userId
     * @return
     */
    @Override
    public User getUserById(Connection connection, int userId) {
        User user = null;

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        StringBuffer sql = new StringBuffer("select * from smbms_user u,smbms_role r where u.userRole = r.id and u.id = ?");

        ArrayList<Object> list = new ArrayList<>();
        list.add(userId);
        Object[] params = null;
        params = list.toArray();

        System.out.println("开始查询");
        System.out.println(sql.toString());
        System.out.println("条件为：" + params[0]);

        resultSet = BaseDao.execute(connection, sql.toString(), params, preparedStatement, resultSet);

        try {
            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUserCode(resultSet.getString("userCode"));
                user.setUserName(resultSet.getString("userName"));
                user.setUserPassword(resultSet.getString("userPassword"));
                user.setGender(resultSet.getInt("gender"));
                user.setBirthday(resultSet.getDate("birthday"));
                user.setPhone(resultSet.getString("phone"));
                user.setAddress(resultSet.getString("address"));
                user.setUserRole(resultSet.getInt("userRole"));
                user.setCreatedBy(resultSet.getInt("createdBy"));
                user.setCreationDate(resultSet.getDate("creationDate"));
                user.setModifyBy(resultSet.getInt("modifyBy"));
                user.setModifyDate(resultSet.getDate("modifyDate"));

                user.setUserRoleName(resultSet.getString("roleName"));
            } else {
                System.out.println("查询到的用户：" + user);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //释放资源
        BaseDao.closeResource(null, preparedStatement, resultSet);

        return user;
    }

    /**
     * 修改用户信息
     *
     * @param connection
     * @param user
     * @param operaterId
     * @return
     */
    @Override
    public int modifyUser(Connection connection, User user, int operaterId) {
        int affectRows = 0;

        PreparedStatement preparedStatement = null;

        StringBuffer sql1 = new StringBuffer("update smbms_user set");

        ArrayList<Object> list = new ArrayList<>();

        //被修改者的信息
        if (user.getUserName() != null) {
            sql1.append(" userName = ?,");
            list.add(user.getUserName());
            System.out.println("userName：" + user.getUserName());
        }
        if (user.getGender() != null) {
            sql1.append("gender = ?,");
            list.add(user.getGender());
            System.out.println("gender：" + user.getGender());

        }
        if (user.getBirthday() != null) {
            sql1.append("birthday = ?,");
            //util.Date与sql.Date的相互转换
            list.add(new Date(user.getBirthday().getTime()));
            System.out.println("birthday：" + user.getBirthday());

        }
        if (user.getPhone() != null) {
            sql1.append("phone = ? ,");
            list.add(user.getPhone());
            System.out.println("phone：" + user.getPhone());
        }
        if (user.getAddress() != null) {
            sql1.append("address = ? ,");
            list.add(user.getAddress());
            System.out.println("address：" + user.getAddress());
        }
        if (user.getUserRole() != null) {
            sql1.append("userRole = ?,");
            list.add(user.getUserRole());
            System.out.println("userRole：" + user.getUserRole());
        }

        //修改者的信息
        sql1.append(" modifyBy = ?,");
        list.add(operaterId);
        sql1.append("modifyDate = ? ");
        list.add(new Date(System.currentTimeMillis()));

        sql1.append(" where id = ?");
        list.add(user.getId());

        System.out.println(sql1);
        Object[] params = list.toArray();

        affectRows = BaseDao.execute(connection, sql1.toString(), params, preparedStatement, affectRows);

        //释放资源
        BaseDao.closeResource(null, preparedStatement, null);

        return affectRows;
    }

    /**
     * 删除指定用户
     *
     * @param connection
     * @param uid
     * @return
     */
    @Override
    public int delUser(Connection connection, int uid) {
        int affectRows = 0;
        PreparedStatement preparedStatement = null;
        String sql = "delete from smbms_user where id = ?";
        Object[] params = {uid};

        affectRows = BaseDao.execute(connection, sql, params, preparedStatement, affectRows);

        BaseDao.closeResource(null, preparedStatement, null);
        return affectRows;
    }
}
