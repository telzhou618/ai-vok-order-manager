package com.rbac.service

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.rbac.entity.SysOperationLog
import com.rbac.mapper.SysOperationLogMapper
import org.springframework.stereotype.Service

@Service
class SysOperationLogService : ServiceImpl<SysOperationLogMapper, SysOperationLog>() {

}
