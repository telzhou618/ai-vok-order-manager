package com.rbac.service

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.rbac.entity.SysUser
import com.rbac.mapper.SysUserMapper
import org.springframework.stereotype.Service

@Service
class SysUserService(
    private val userRoleService: SysUserRoleService,
    val roleService: SysRoleService
) : ServiceImpl<SysUserMapper, SysUser>() {

    fun getUserByUsername(username: String): SysUser? {
        return getOne(QueryWrapper<SysUser>().eq("username", username))
    }

    /**
     * 获取用户的所有角色编码
     */
    fun getUserRoles(userId: Long): List<String> {
        val roleIds = userRoleService.getRoleIdsByUserId(userId)
        if (roleIds.isEmpty()) return emptyList()

        return roleService.listByIds(roleIds).map { it.roleCode }
    }

    /**
     * 获取用户的所有权限编码
     */
    fun getUserPermissions(userId: Long): List<String> {
        val roleIds = userRoleService.getRoleIdsByUserId(userId)
        if (roleIds.isEmpty()) return emptyList()

        // 获取所有角色的权限
        val permissions = mutableSetOf<String>()
        roleIds.forEach { roleId ->
            val permIds = roleService.getPermissionIdsByRoleId(roleId)
            if (permIds.isNotEmpty()) {
                val perms = roleService.permissionService.listByIds(permIds)
                permissions.addAll(perms.map { it.permCode })
            }
        }

        return permissions.toList()
    }
}
