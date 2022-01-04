package com.lry.service.role;

import java.util.List;

import com.lry.pojo.Role;

/**
 * @author 刘汝杨
 */
public interface RoleService {

    /**
     * 查询角色信息，返回角色列表
     *
     * @return
     */
    List<Role> getRoleList();
}
