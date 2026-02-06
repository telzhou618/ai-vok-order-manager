package com.rbac.service

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.rbac.entity.SysUserRole
import com.rbac.mapper.SysUserRoleMapper
import org.springframework.stereotype.Service

@Service
class SysUserRoleService : ServiceImpl<SysUserRoleMapper, SysUserRole>() {

    fun getRoleIdsByUserId(userId: Long): List<Long> {
        return list(QueryWrapper<SysUserRole>().eq("user_id", userId))
            .map { it.roleId }
    }
}
