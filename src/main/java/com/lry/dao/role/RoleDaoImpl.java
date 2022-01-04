package com.lry.dao.role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.lry.dao.BaseDao;
import com.lry.pojo.Role;

public class RoleDaoImpl implements RoleDao {
    /**
     * 获取角色列表
     *
     * @param connection
     * @return
     */
    @Override
    public List<Role> getRoleList(Connection connection) {
        List<Role> roleList = null;

        String sql = "select * from smbms_role";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Object[] params = {};

        ResultSet rs = BaseDao.execute(connection, sql, params, preparedStatement, resultSet);
        try {
            Role role = null;
            roleList = new ArrayList<>();
            while (rs.next()) {
                role = new Role();
                role.setId(rs.getInt("id"));
                role.setRoleCode(rs.getString("roleCode"));
                role.setRoleName(rs.getString("roleName"));
                roleList.add(role);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        BaseDao.closeResource(null, preparedStatement, resultSet);

        return roleList;
    }
}
