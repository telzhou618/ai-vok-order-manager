-- 订单测试数据
-- 注意：根据 user_id 取模分表
-- user_id % 2 = 0 的数据会插入到 order_0
-- user_id % 2 = 1 的数据会插入到 order_1

-- 用户ID为 1000 的订单（会路由到 order_0）
INSERT INTO `order` (order_no, user_id, order_status, pay_status, total_amount, discount_amount, actual_amount, freight_amount, pay_type, pay_time, consignee, phone, address, memo, source_type, created_time)
VALUES 
('ORD202602060001', 1000, 4, 1, 299.00, 20.00, 289.00, 10.00, 1, '2026-02-05 10:30:00', '张三', '13800138000', '北京市朝阳区某某街道123号', '请尽快发货', 1, '2026-02-05 10:00:00'),
('ORD202602060002', 1000, 2, 1, 599.00, 50.00, 559.00, 10.00, 2, '2026-02-05 14:20:00', '张三', '13800138000', '北京市朝阳区某某街道123号', '', 2, '2026-02-05 14:00:00'),
('ORD202602060003', 1000, 1, 0, 199.00, 0.00, 199.00, 0.00, 0, NULL, '张三', '13800138000', '北京市朝阳区某某街道123号', '', 3, '2026-02-06 09:00:00');

-- 用户ID为 1001 的订单（会路由到 order_1）
INSERT INTO `order` (order_no, user_id, order_status, pay_status, total_amount, discount_amount, actual_amount, freight_amount, pay_type, pay_time, consignee, phone, address, memo, source_type, created_time)
VALUES 
('ORD202602060004', 1001, 3, 1, 899.00, 100.00, 809.00, 10.00, 1, '2026-02-04 16:30:00', '李四', '13900139000', '上海市浦东新区某某路456号', '送货前请电话联系', 1, '2026-02-04 16:00:00'),
('ORD202602060005', 1001, 5, 0, 399.00, 0.00, 399.00, 0.00, 0, NULL, '李四', '13900139000', '上海市浦东新区某某路456号', '', 4, '2026-02-05 11:00:00'),
('ORD202602060006', 1001, 4, 1, 1299.00, 200.00, 1109.00, 10.00, 3, '2026-02-03 09:15:00', '李四', '13900139000', '上海市浦东新区某某路456号', '', 2, '2026-02-03 09:00:00');

-- 用户ID为 1002 的订单（会路由到 order_0）
INSERT INTO `order` (order_no, user_id, order_status, pay_status, total_amount, discount_amount, actual_amount, freight_amount, pay_type, pay_time, consignee, phone, address, memo, source_type, created_time)
VALUES 
('ORD202602060007', 1002, 2, 1, 499.00, 30.00, 479.00, 10.00, 2, '2026-02-06 08:45:00', '王五', '13700137000', '广州市天河区某某大道789号', '', 1, '2026-02-06 08:30:00'),
('ORD202602060008', 1002, 1, 0, 799.00, 0.00, 799.00, 0.00, 0, NULL, '王五', '13700137000', '广州市天河区某某大道789号', '请开发票', 3, '2026-02-06 10:00:00');

-- 用户ID为 1003 的订单（会路由到 order_1）
INSERT INTO `order` (order_no, user_id, order_status, pay_status, total_amount, discount_amount, actual_amount, freight_amount, pay_type, pay_time, consignee, phone, address, memo, source_type, created_time)
VALUES 
('ORD202602060009', 1003, 4, 1, 1599.00, 300.00, 1309.00, 10.00, 1, '2026-02-02 15:20:00', '赵六', '13600136000', '深圳市南山区某某科技园101号', '', 2, '2026-02-02 15:00:00'),
('ORD202602060010', 1003, 3, 1, 699.00, 50.00, 659.00, 10.00, 4, '2026-02-05 13:30:00', '赵六', '13600136000', '深圳市南山区某某科技园101号', '工作日配送', 1, '2026-02-05 13:00:00');

-- 更新部分订单的物流信息
UPDATE `order` SET delivery_company = '顺丰速运', delivery_no = 'SF1234567890', delivery_time = '2026-02-05 16:00:00' WHERE order_no = 'ORD202602060001';
UPDATE `order` SET delivery_company = '圆通快递', delivery_no = 'YT9876543210', delivery_time = '2026-02-04 18:00:00', receive_time = '2026-02-06 10:00:00' WHERE order_no = 'ORD202602060004';
UPDATE `order` SET delivery_company = '中通快递', delivery_no = 'ZT1122334455', delivery_time = '2026-02-03 14:00:00', receive_time = '2026-02-05 09:00:00' WHERE order_no = 'ORD202602060006';
UPDATE `order` SET delivery_company = '韵达快递', delivery_no = 'YD5566778899', delivery_time = '2026-02-05 15:00:00' WHERE order_no = 'ORD202602060010';

-- 更新取消订单的信息
UPDATE `order` SET cancel_time = '2026-02-05 12:00:00', cancel_reason = '不想要了' WHERE order_no = 'ORD202602060005';
