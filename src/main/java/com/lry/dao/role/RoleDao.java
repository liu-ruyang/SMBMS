package com.lry.dao.role;

import java.sql.Connection;
import java.util.List;

import com.lry.pojo.Role;

public interface RoleDao {
    /**
     * 获取角色列表
     *
     * @param connection
     * @return
     */
    List<Role> getRoleList(Connection connection);
}
