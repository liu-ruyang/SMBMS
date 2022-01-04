package com.lry.service.role;

import java.sql.Connection;
import java.util.List;

import com.lry.dao.BaseDao;
import com.lry.dao.role.RoleDao;
import com.lry.dao.role.RoleDaoImpl;
import com.lry.pojo.Role;

public class RoleServiceImpl implements RoleService {

    //引入Dao
    private RoleDao roleDao;

    /**
     * Constructs a new object.
     */
    public RoleServiceImpl() {
        roleDao = new RoleDaoImpl();
    }

    /**
     * 查询角色信息，返回角色列表
     *
     * @return
     */
    @Override
    public List<Role> getRoleList() {
        List<Role> roleList = null;

        Connection connection = null;

        connection = BaseDao.getConnection();

        roleList = roleDao.getRoleList(connection);

        BaseDao.closeResource(connection, null, null);

        return roleList;
    }
}
