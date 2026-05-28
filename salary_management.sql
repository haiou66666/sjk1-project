/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80045 (8.0.45)
 Source Host           : localhost:3306
 Source Schema         : salary_management

 Target Server Type    : MySQL
 Target Server Version : 80045 (8.0.45)
 File Encoding         : 65001

 Date: 29/05/2026 01:03:02
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for allowance
-- ----------------------------
DROP TABLE IF EXISTS `allowance`;
CREATE TABLE `allowance`  (
  `allow_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `emp_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `overtime_type` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `overtime_amount` decimal(5, 1) NOT NULL,
  `allowance_rate` decimal(8, 2) NOT NULL,
  `allowance_amount` decimal(10, 2) NOT NULL,
  `overtime_date` date NOT NULL,
  `att_month` varchar(7) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`allow_id`) USING BTREE,
  INDEX `emp_id`(`emp_id` ASC) USING BTREE,
  CONSTRAINT `allowance_ibfk_1` FOREIGN KEY (`emp_id`) REFERENCES `employee` (`emp_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of allowance
-- ----------------------------
INSERT INTO `allowance` VALUES ('ALLOW-001', 'EMP001', '工作日加班', 8.0, 25.00, 200.00, '2026-05-15', '2026-05');
INSERT INTO `allowance` VALUES ('ALLOW-002', 'EMP002', '工作日加班', 5.0, 25.00, 125.00, '2026-05-20', '2026-05');
INSERT INTO `allowance` VALUES ('ALLOW-003', 'EMP003', '休息日加班', 1.5, 300.00, 450.00, '2026-05-10', '2026-05');
INSERT INTO `allowance` VALUES ('ALLOW-004', 'EMP004', '休息日加班', 2.0, 300.00, 600.00, '2026-05-08', '2026-05');
INSERT INTO `allowance` VALUES ('ALLOW-005', 'EMP005', '工作日加班', 6.0, 25.00, 150.00, '2026-05-18', '2026-05');
INSERT INTO `allowance` VALUES ('ALLOW-006', 'EMP006', '法定节假日加班', 1.5, 450.00, 675.00, '2026-05-01', '2026-05');
INSERT INTO `allowance` VALUES ('ALLOW-007', 'EMP008', '工作日加班', 8.0, 25.00, 200.00, '2026-05-22', '2026-05');
INSERT INTO `allowance` VALUES ('ALLOW-008', 'EMP009', '工作日加班', 4.0, 25.00, 100.00, '2026-05-25', '2026-05');
INSERT INTO `allowance` VALUES ('ALLOW-009', 'EMP011', '工作日加班', 3.0, 25.00, 75.00, '2026-05-12', '2026-05');
INSERT INTO `allowance` VALUES ('ALLOW-010', 'EMP012', '工作日加班', 6.0, 25.00, 150.00, '2026-05-19', '2026-05');

-- ----------------------------
-- Table structure for attendance
-- ----------------------------
DROP TABLE IF EXISTS `attendance`;
CREATE TABLE `attendance`  (
  `att_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `emp_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `att_month` varchar(7) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `work_days` int NOT NULL,
  `leave_days` decimal(3, 1) NULL DEFAULT 0.0,
  `overtime_hours` decimal(5, 1) NULL DEFAULT 0.0,
  `overtime_days` decimal(3, 1) NULL DEFAULT 0.0,
  `att_remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`att_id`) USING BTREE,
  INDEX `emp_id`(`emp_id` ASC) USING BTREE,
  CONSTRAINT `attendance_ibfk_1` FOREIGN KEY (`emp_id`) REFERENCES `employee` (`emp_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of attendance
-- ----------------------------
INSERT INTO `attendance` VALUES ('ATT-2026-04-EMP001', 'EMP001', '2026-04', 22, 0.0, 6.0, 1.0, NULL);
INSERT INTO `attendance` VALUES ('ATT-2026-04-EMP002', 'EMP002', '2026-04', 21, 1.0, 3.0, 0.5, '事假1天');
INSERT INTO `attendance` VALUES ('ATT-2026-04-EMP003', 'EMP003', '2026-04', 22, 0.0, 8.0, 1.0, NULL);
INSERT INTO `attendance` VALUES ('ATT-2026-04-EMP004', 'EMP004', '2026-04', 21, 1.0, 10.0, 1.5, '事假1天');
INSERT INTO `attendance` VALUES ('ATT-2026-04-EMP005', 'EMP005', '2026-04', 22, 0.0, 4.0, 0.5, NULL);
INSERT INTO `attendance` VALUES ('ATT-2026-04-EMP006', 'EMP006', '2026-04', 22, 0.0, 8.0, 1.0, NULL);
INSERT INTO `attendance` VALUES ('ATT-2026-04-EMP007', 'EMP007', '2026-04', 22, 0.0, 2.0, 0.0, NULL);
INSERT INTO `attendance` VALUES ('ATT-2026-04-EMP008', 'EMP008', '2026-04', 21, 1.0, 5.0, 0.5, '事假1天');
INSERT INTO `attendance` VALUES ('ATT-2026-04-EMP009', 'EMP009', '2026-04', 22, 0.0, 6.0, 1.0, NULL);
INSERT INTO `attendance` VALUES ('ATT-2026-04-EMP010', 'EMP010', '2026-04', 22, 0.0, 0.0, 0.0, NULL);
INSERT INTO `attendance` VALUES ('ATT-2026-04-EMP011', 'EMP011', '2026-04', 22, 0.0, 0.0, 0.0, NULL);
INSERT INTO `attendance` VALUES ('ATT-2026-04-EMP012', 'EMP012', '2026-04', 22, 0.0, 4.0, 0.5, NULL);
INSERT INTO `attendance` VALUES ('ATT-2026-05-EMP001', 'EMP001', '2026-05', 21, 1.0, 8.0, 1.0, '事假1天');
INSERT INTO `attendance` VALUES ('ATT-2026-05-EMP002', 'EMP002', '2026-05', 22, 0.0, 5.0, 0.5, NULL);
INSERT INTO `attendance` VALUES ('ATT-2026-05-EMP003', 'EMP003', '2026-05', 20, 2.0, 10.0, 1.5, '病假2天');
INSERT INTO `attendance` VALUES ('ATT-2026-05-EMP004', 'EMP004', '2026-05', 22, 0.0, 15.0, 2.0, '加班');
INSERT INTO `attendance` VALUES ('ATT-2026-05-EMP005', 'EMP005', '2026-05', 21, 1.0, 6.0, 1.0, '事假1天');
INSERT INTO `attendance` VALUES ('ATT-2026-05-EMP006', 'EMP006', '2026-05', 22, 0.0, 12.0, 1.5, NULL);
INSERT INTO `attendance` VALUES ('ATT-2026-05-EMP007', 'EMP007', '2026-05', 20, 2.0, 0.0, 0.0, '病假2天');
INSERT INTO `attendance` VALUES ('ATT-2026-05-EMP008', 'EMP008', '2026-05', 22, 0.0, 8.0, 1.0, NULL);
INSERT INTO `attendance` VALUES ('ATT-2026-05-EMP009', 'EMP009', '2026-05', 21, 1.0, 4.0, 0.5, '事假1天');
INSERT INTO `attendance` VALUES ('ATT-2026-05-EMP010', 'EMP010', '2026-05', 22, 0.0, 0.0, 0.0, NULL);
INSERT INTO `attendance` VALUES ('ATT-2026-05-EMP011', 'EMP011', '2026-05', 22, 0.0, 3.0, 0.5, NULL);
INSERT INTO `attendance` VALUES ('ATT-2026-05-EMP012', 'EMP012', '2026-05', 21, 1.0, 6.0, 1.0, '事假1天');

-- ----------------------------
-- Table structure for employee
-- ----------------------------
DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee`  (
  `emp_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `emp_name` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `department` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `job_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `hire_date` date NOT NULL,
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '在职',
  PRIMARY KEY (`emp_id`) USING BTREE,
  UNIQUE INDEX `phone`(`phone` ASC) USING BTREE,
  INDEX `job_id`(`job_id` ASC) USING BTREE,
  CONSTRAINT `employee_ibfk_1` FOREIGN KEY (`job_id`) REFERENCES `job` (`job_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of employee
-- ----------------------------
INSERT INTO `employee` VALUES ('EMP001', '张伟', '行政部', 'J002', '2022-03-15', '13800138001', '在职');
INSERT INTO `employee` VALUES ('EMP002', '李娜', '行政部', 'J001', '2023-07-01', '13800138002', '在职');
INSERT INTO `employee` VALUES ('EMP003', '王强', '技术部', 'J005', '2020-01-10', '13800138003', '在职');
INSERT INTO `employee` VALUES ('EMP004', '赵敏', '技术部', 'J004', '2021-06-20', '13800138004', '在职');
INSERT INTO `employee` VALUES ('EMP005', '刘洋', '技术部', 'J003', '2024-02-15', '13800138005', '在职');
INSERT INTO `employee` VALUES ('EMP006', '陈刚', '生产部', 'J007', '2020-09-01', '13800138006', '在职');
INSERT INTO `employee` VALUES ('EMP007', '孙丽', '生产部', 'J006', '2023-04-10', '13800138007', '在职');
INSERT INTO `employee` VALUES ('EMP008', '周杰', '生产部', 'J006', '2024-05-20', '13800138008', '在职');
INSERT INTO `employee` VALUES ('EMP009', '吴芳', '财务部', 'J009', '2021-08-15', '13800138009', '在职');
INSERT INTO `employee` VALUES ('EMP010', '郑涛', '财务部', 'J008', '2023-11-01', '13800138010', '在职');
INSERT INTO `employee` VALUES ('EMP011', '巴沃祖拉程瓦', '人事部', 'J010', '2022-05-10', '13800138011', '在职');
INSERT INTO `employee` VALUES ('EMP012', '冯磊', '技术部', 'J003', '2024-08-01', '13800138012', '在职');

-- ----------------------------
-- Table structure for job
-- ----------------------------
DROP TABLE IF EXISTS `job`;
CREATE TABLE `job`  (
  `job_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `job_name` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `job_level` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `base_salary` decimal(10, 2) NOT NULL,
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`job_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of job
-- ----------------------------
INSERT INTO `job` VALUES ('J001', '行政专员', '初级', 5000.00, '负责日常行政事务');
INSERT INTO `job` VALUES ('J002', '行政专员', '中级', 6500.00, '负责行政管理工作');
INSERT INTO `job` VALUES ('J003', '技术工程师', '初级', 7000.00, '负责基础技术开发');
INSERT INTO `job` VALUES ('J004', '技术工程师', '中级', 9500.00, '负责核心模块开发');
INSERT INTO `job` VALUES ('J005', '技术工程师', '高级', 13000.00, '负责技术架构设计');
INSERT INTO `job` VALUES ('J006', '生产操作工', '初级', 4500.00, '负责生产线基础操作');
INSERT INTO `job` VALUES ('J007', '生产操作工', '中级', 5500.00, '负责生产线管理');
INSERT INTO `job` VALUES ('J008', '财务专员', '初级', 5200.00, '负责日常财务核算');
INSERT INTO `job` VALUES ('J009', '财务专员', '中级', 7000.00, '负责财务报表与分析');
INSERT INTO `job` VALUES ('J010', '人事专员', '中级', 6500.00, '负责员工招聘与培训');

-- ----------------------------
-- Table structure for monthly_salary
-- ----------------------------
DROP TABLE IF EXISTS `monthly_salary`;
CREATE TABLE `monthly_salary`  (
  `salary_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `emp_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `salary_month` varchar(7) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `base_salary` decimal(10, 2) NOT NULL,
  `allowance_total` decimal(10, 2) NULL DEFAULT 0.00,
  `deduction` decimal(10, 2) NULL DEFAULT 0.00,
  `net_salary` decimal(10, 2) NOT NULL,
  `calc_time` datetime NOT NULL,
  `operator` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`salary_id`) USING BTREE,
  INDEX `emp_id`(`emp_id` ASC) USING BTREE,
  CONSTRAINT `monthly_salary_ibfk_1` FOREIGN KEY (`emp_id`) REFERENCES `employee` (`emp_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of monthly_salary
-- ----------------------------
INSERT INTO `monthly_salary` VALUES ('SAL-2026-04-EMP001', 'EMP001', '2026-04', 6500.00, 150.00, 0.00, 6650.00, '2026-04-30 17:00:00', 'SYSTEM');
INSERT INTO `monthly_salary` VALUES ('SAL-2026-04-EMP002', 'EMP002', '2026-04', 5000.00, 75.00, 166.67, 4908.33, '2026-04-30 17:00:00', 'SYSTEM');
INSERT INTO `monthly_salary` VALUES ('SAL-2026-04-EMP003', 'EMP003', '2026-04', 13000.00, 300.00, 0.00, 13300.00, '2026-04-30 17:00:00', 'SYSTEM');
INSERT INTO `monthly_salary` VALUES ('SAL-2026-04-EMP004', 'EMP004', '2026-04', 9500.00, 450.00, 316.67, 9633.33, '2026-04-30 17:00:00', 'SYSTEM');
INSERT INTO `monthly_salary` VALUES ('SAL-2026-04-EMP005', 'EMP005', '2026-04', 7000.00, 100.00, 0.00, 7100.00, '2026-04-30 17:00:00', 'SYSTEM');
INSERT INTO `monthly_salary` VALUES ('SAL-2026-04-EMP006', 'EMP006', '2026-04', 5500.00, 240.00, 0.00, 5740.00, '2026-04-30 17:00:00', 'SYSTEM');
INSERT INTO `monthly_salary` VALUES ('SAL-2026-04-EMP007', 'EMP007', '2026-04', 4500.00, 50.00, 0.00, 4550.00, '2026-04-30 17:00:00', 'SYSTEM');
INSERT INTO `monthly_salary` VALUES ('SAL-2026-04-EMP008', 'EMP008', '2026-04', 4500.00, 125.00, 150.00, 4475.00, '2026-04-30 17:00:00', 'SYSTEM');
INSERT INTO `monthly_salary` VALUES ('SAL-2026-04-EMP009', 'EMP009', '2026-04', 7000.00, 150.00, 0.00, 7150.00, '2026-04-30 17:00:00', 'SYSTEM');
INSERT INTO `monthly_salary` VALUES ('SAL-2026-04-EMP010', 'EMP010', '2026-04', 5200.00, 0.00, 0.00, 5200.00, '2026-04-30 17:00:00', 'SYSTEM');
INSERT INTO `monthly_salary` VALUES ('SAL-2026-04-EMP011', 'EMP011', '2026-04', 6500.00, 0.00, 0.00, 6500.00, '2026-04-30 17:00:00', 'SYSTEM');
INSERT INTO `monthly_salary` VALUES ('SAL-2026-04-EMP012', 'EMP012', '2026-04', 7000.00, 100.00, 0.00, 7100.00, '2026-04-30 17:00:00', 'SYSTEM');

-- ----------------------------
-- Table structure for yearly_bonus
-- ----------------------------
DROP TABLE IF EXISTS `yearly_bonus`;
CREATE TABLE `yearly_bonus`  (
  `bonus_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `emp_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `year` int NOT NULL,
  `total_salary` decimal(12, 2) NOT NULL DEFAULT 0.00,
  `total_allowance` decimal(12, 2) NOT NULL DEFAULT 0.00,
  `bonus_amount` decimal(12, 2) NOT NULL DEFAULT 0.00,
  `calc_time` datetime NOT NULL,
  `operator` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`bonus_id`) USING BTREE,
  INDEX `emp_id`(`emp_id` ASC) USING BTREE,
  CONSTRAINT `yearly_bonus_ibfk_1` FOREIGN KEY (`emp_id`) REFERENCES `employee` (`emp_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of yearly_bonus
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
