package com.rbac.service

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.rbac.entity.SysRolePermission
import com.rbac.mapper.SysRolePermissionMapper
import org.springframework.stereotype.Service

@Service
class SysRolePermissionService : ServiceImpl<SysRolePermissionMapper, SysRolePermission>() {

    fun getPermIdsByRoleId(roleId: Long): List<Long> {
        return list(QueryWrapper<SysRolePermission>().eq("role_id", roleId))
            .map { it.permId }
    }
}
