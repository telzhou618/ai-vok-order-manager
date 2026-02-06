# ShardingSphere 分表配置说明

## 概述

本项目使用 ShardingSphere 实现订单表的分表功能，结合 MyBatis-Plus 动态数据源实现灵活的数据源切换。

## 配置说明

### 1. YAML 配置 (application.yml)

```yaml
sharding:
  show-sql: false              # 是否显示 SQL 日志
  sharding-size: 2             # 分表数量
  table-rule-configs:
    - logic-table-name: order  # 逻辑表名
      sharding-column: user_id # 分片键（按此字段取模）
```

### 2. 分表规则

- **逻辑表名**: `order`
- **物理表名**: `order_0`, `order_1`
- **分片键**: `user_id`
- **分片算法**: MOD（取模）
- **分片数量**: 2

**路由规则**:
- `user_id % 2 = 0` → `order_0`
- `user_id % 2 = 1` → `order_1`

### 3. 数据源配置

项目配置了两个数据源：
- **master**: 主数据源，用于系统表（用户、角色、权限等）
- **sharding**: 分表数据源，用于订单表

## 使用方式

### 1. Mapper 层使用 @DS 注解

```kotlin
@Mapper
@DS("sharding")  // 指定使用分表数据源
interface OrderMapper : BaseMapper<Order>
```

### 2. Service 层自动路由

```kotlin
@Service
class OrderService : ServiceImpl<OrderMapper, Order>() {
    
    // 带分片键查询，会自动路由到对应分表
    fun listByUserId(userId: Long): List<Order> {
        return lambdaQuery()
            .eq(Order::userId, userId)  // 使用分片键
            .list()
    }
    
    // 不带分片键查询，会扫描所有分表
    fun getByOrderNo(orderNo: String): Order? {
        return lambdaQuery()
            .eq(Order::orderNo, orderNo)
            .one()
    }
}
```

## 数据库初始化

执行 `sql/order_sharding.sql` 创建分表：

```sql
-- 创建 order_0 和 order_1 两张物理表
source sql/order_sharding.sql;
```

## 注意事项

### 1. 查询优化

- **推荐**: 查询时尽量带上分片键 `user_id`，可以精确路由到单表
  ```kotlin
  // ✅ 好的做法：带分片键
  lambdaQuery().eq(Order::userId, userId).list()
  ```

- **避免**: 不带分片键的查询会扫描所有分表，影响性能
  ```kotlin
  // ⚠️ 需要注意：全表扫描
  lambdaQuery().eq(Order::orderNo, orderNo).list()
  ```

### 2. 插入数据

插入时必须包含 `user_id`，ShardingSphere 会根据此值自动路由到对应分表：

```kotlin
val order = Order(
    orderNo = "ORD20260206001",
    userId = 1001,  // 必须设置分片键
    totalAmount = BigDecimal("99.99")
    // ... 其他字段
)
orderService.save(order)
```

### 3. 事务处理

分表查询在同一个数据源内，事务正常工作：

```kotlin
@Transactional
fun createOrder(order: Order) {
    orderService.save(order)
    // 其他业务逻辑
}
```

### 4. 扩展分表

如果需要增加分表数量，修改配置：

```yaml
sharding:
  sharding-size: 4  # 改为 4 张表
```

然后创建对应的物理表：`order_0`, `order_1`, `order_2`, `order_3`

## 性能建议

1. **合理设计分片键**: 选择查询频率高、分布均匀的字段作为分片键
2. **避免跨分片查询**: 尽量在单个分片内完成查询
3. **使用索引**: 在物理表上为常用查询字段建立索引
4. **监控 SQL**: 开发环境可开启 `show-sql: true` 查看实际执行的 SQL

## 故障排查

### 问题 1: 找不到表

**错误**: `Table 'rbac_db.order' doesn't exist`

**原因**: 没有创建物理分表

**解决**: 执行 `sql/order_sharding.sql` 创建 `order_0` 和 `order_1`

### 问题 2: 数据路由错误

**错误**: 查询不到数据

**原因**: 分片键值不一致

**解决**: 确保查询时使用的 `user_id` 与插入时一致

### 问题 3: 性能慢

**原因**: 没有使用分片键，导致全表扫描

**解决**: 优化查询条件，添加 `user_id` 条件
