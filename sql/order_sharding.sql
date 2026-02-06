-- 订单分表脚本
-- 根据 user_id 取模分为 2 张表：order_0 和 order_1

-- 创建 order_0 表
CREATE TABLE `order_0`
(
    `id`               bigint(20)     NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `order_no`         varchar(32)    NOT NULL COMMENT '订单号（业务唯一标识）',
    `user_id`          bigint(20)     NOT NULL DEFAULT '0' COMMENT '用户ID',
    `order_status`     tinyint(4)     NOT NULL DEFAULT '1' COMMENT '订单状态：1-待付款 2-已付款 3-已发货 4-已完成 5-已取消 6-已关闭',
    `pay_status`       tinyint(4)     NOT NULL DEFAULT '0' COMMENT '支付状态：0-未支付 1-支付成功 2-支付失败 3-已退款 4-退款中',
    `total_amount`     decimal(10, 2) NOT NULL DEFAULT '0.00' COMMENT '订单总金额',
    `discount_amount`  decimal(10, 2) NOT NULL DEFAULT '0.00' COMMENT '优惠金额',
    `actual_amount`    decimal(10, 2) NOT NULL DEFAULT '0.00' COMMENT '实付金额',
    `freight_amount`   decimal(10, 2) NOT NULL DEFAULT '0.00' COMMENT '运费',
    `pay_type`         tinyint(4)     NOT NULL DEFAULT '0' COMMENT '支付方式：0-未选择 1-微信 2-支付宝 3-银行卡 4-余额',
    `pay_time`         datetime       NULL COMMENT '支付时间',
    `consignee`        varchar(50)    NOT NULL DEFAULT '' COMMENT '收货人',
    `phone`            varchar(20)    NOT NULL DEFAULT '' COMMENT '联系电话',
    `address`          varchar(200)   NOT NULL DEFAULT '' COMMENT '收货地址',
    `memo`             varchar(500)   NOT NULL DEFAULT '' COMMENT '订单备注',
    `delivery_time`    datetime       NULL COMMENT '发货时间',
    `receive_time`     datetime       NULL COMMENT '收货时间',
    `cancel_time`      datetime       NULL COMMENT '取消时间',
    `cancel_reason`    varchar(200)   NOT NULL DEFAULT '' COMMENT '取消原因',
    `delivery_company` varchar(50)    NOT NULL DEFAULT '' COMMENT '物流公司',
    `delivery_no`      varchar(50)    NOT NULL DEFAULT '' COMMENT '物流单号',
    `invoice_type`     tinyint(4)     NOT NULL DEFAULT '0' COMMENT '发票类型：0-不需要 1-个人 2-公司',
    `invoice_title`    varchar(100)   NOT NULL DEFAULT '' COMMENT '发票抬头',
    `invoice_tax_no`   varchar(50)    NOT NULL DEFAULT '' COMMENT '税号',
    `source_type`      tinyint(4)     NOT NULL DEFAULT '1' COMMENT '订单来源：1-APP 2-PC 3-小程序 4-H5',
    `is_deleted`       tinyint(1)     NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除 1-已删除',
    `version`          int(11)        NOT NULL DEFAULT '0' COMMENT '版本号，用于乐观锁',
    `created_time`     datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time`     datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_created_time` (`created_time`),
    KEY `idx_order_status` (`order_status`),
    KEY `idx_pay_status` (`pay_status`),
    KEY `idx_delivery_company` (`delivery_company`(20))
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='订单主表_分片0';

-- 创建 order_1 表
CREATE TABLE `order_1`
(
    `id`               bigint(20)     NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `order_no`         varchar(32)    NOT NULL COMMENT '订单号（业务唯一标识）',
    `user_id`          bigint(20)     NOT NULL DEFAULT '0' COMMENT '用户ID',
    `order_status`     tinyint(4)     NOT NULL DEFAULT '1' COMMENT '订单状态：1-待付款 2-已付款 3-已发货 4-已完成 5-已取消 6-已关闭',
    `pay_status`       tinyint(4)     NOT NULL DEFAULT '0' COMMENT '支付状态：0-未支付 1-支付成功 2-支付失败 3-已退款 4-退款中',
    `total_amount`     decimal(10, 2) NOT NULL DEFAULT '0.00' COMMENT '订单总金额',
    `discount_amount`  decimal(10, 2) NOT NULL DEFAULT '0.00' COMMENT '优惠金额',
    `actual_amount`    decimal(10, 2) NOT NULL DEFAULT '0.00' COMMENT '实付金额',
    `freight_amount`   decimal(10, 2) NOT NULL DEFAULT '0.00' COMMENT '运费',
    `pay_type`         tinyint(4)     NOT NULL DEFAULT '0' COMMENT '支付方式：0-未选择 1-微信 2-支付宝 3-银行卡 4-余额',
    `pay_time`         datetime       NULL COMMENT '支付时间',
    `consignee`        varchar(50)    NOT NULL DEFAULT '' COMMENT '收货人',
    `phone`            varchar(20)    NOT NULL DEFAULT '' COMMENT '联系电话',
    `address`          varchar(200)   NOT NULL DEFAULT '' COMMENT '收货地址',
    `memo`             varchar(500)   NOT NULL DEFAULT '' COMMENT '订单备注',
    `delivery_time`    datetime       NULL COMMENT '发货时间',
    `receive_time`     datetime       NULL COMMENT '收货时间',
    `cancel_time`      datetime       NULL COMMENT '取消时间',
    `cancel_reason`    varchar(200)   NOT NULL DEFAULT '' COMMENT '取消原因',
    `delivery_company` varchar(50)    NOT NULL DEFAULT '' COMMENT '物流公司',
    `delivery_no`      varchar(50)    NOT NULL DEFAULT '' COMMENT '物流单号',
    `invoice_type`     tinyint(4)     NOT NULL DEFAULT '0' COMMENT '发票类型：0-不需要 1-个人 2-公司',
    `invoice_title`    varchar(100)   NOT NULL DEFAULT '' COMMENT '发票抬头',
    `invoice_tax_no`   varchar(50)    NOT NULL DEFAULT '' COMMENT '税号',
    `source_type`      tinyint(4)     NOT NULL DEFAULT '1' COMMENT '订单来源：1-APP 2-PC 3-小程序 4-H5',
    `is_deleted`       tinyint(1)     NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除 1-已删除',
    `version`          int(11)        NOT NULL DEFAULT '0' COMMENT '版本号，用于乐观锁',
    `created_time`     datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time`     datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_created_time` (`created_time`),
    KEY `idx_order_status` (`order_status`),
    KEY `idx_pay_status` (`pay_status`),
    KEY `idx_delivery_company` (`delivery_company`(20))
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='订单主表_分片1';
