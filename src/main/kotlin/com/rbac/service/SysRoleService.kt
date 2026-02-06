package com.rbac.service

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.rbac.entity.SysRole
import com.rbac.mapper.SysRoleMapper
import org.springframework.stereotype.Service

@Service
class SysRoleService(
    private val rolePermissionService: SysRolePermissionService,
    val permissionService: SysPermissionService
) : ServiceImpl<SysRoleMapper, SysRole>() {
    /**
     * 获取角色的所有权限ID列表
     */
    fun getPermissionIdsByRoleId(roleId: Long): List<Long> {
        return rolePermissionService.getPermIdsByRoleId(roleId)
    }
}
