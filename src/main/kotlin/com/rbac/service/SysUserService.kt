package com.rbac.service

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.rbac.entity.SysRolePermission
import com.rbac.entity.SysUser
import com.rbac.entity.SysUserRole
import com.rbac.mapper.*
import org.springframework.stereotype.Service

@Service
class SysUserService(
    private val sysRoleMapper: SysRoleMapper,
    private val sysUserRoleMapper: SysUserRoleMapper,
    private val sysPermissionMapper: SysPermissionMapper,
    private val sysRolePermissionMapper: SysRolePermissionMapper
) : ServiceImpl<SysUserMapper, SysUser>() {

    fun getUserByUsername(username: String): SysUser? {
        return getOne(QueryWrapper<SysUser>().eq("username", username))
    }

    /**
     * 获取用户的所有角色编码
     */
    fun getUserRoles(userId: Long): List<String> {
        val roleIds = getRoleIdsByUserId(userId)
        if (roleIds.isEmpty()) {
            return emptyList()
        }
        return sysRoleMapper.selectBatchIds(roleIds).map { it.roleCode }
    }

    /**
     * 获取用户的所有权限编码
     */
    fun getUserPermissions(userId: Long): List<String> {
        val roleIds = getRoleIdsByUserId(userId)
        if (roleIds.isEmpty()) return emptyList()

        val permIds = sysRolePermissionMapper.selectList(
            KtQueryWrapper(SysRolePermission::class.java)
                .`in`(SysRolePermission::roleId, roleIds)
        ).map { it.permId }

        if (permIds.isEmpty()) {
            return emptyList()
        }
        return sysPermissionMapper.selectBatchIds(permIds)
            .map { it.permCode }
    }

    fun getRoleIdsByUserId(userId: Long): List<Long> {
        return sysUserRoleMapper.selectList(
            KtQueryWrapper(SysUserRole::class.java)
                .eq(SysUserRole::userId, userId)
        ).map { it.roleId }
    }
}
