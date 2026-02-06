package com.rbac.service

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.rbac.entity.SysPermission
import com.rbac.mapper.SysPermissionMapper
import org.springframework.stereotype.Service

@Service
class SysPermissionService : ServiceImpl<SysPermissionMapper, SysPermission>() {
    
}
