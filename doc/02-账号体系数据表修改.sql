-- 企业表
CREATE TABLE `ryn_company` (
  `COMPANY_ID` VARCHAR (64) CHARACTER SET UTF8MB4 COLLATE UTF8MB4_BIN  NULL COMMENT '主键',
  `NAME` VARCHAR (127) COLLATE UTF8MB4_UNICODE_CI  NULL DEFAULT '' COMMENT '公司名称',
  `LOGO` VARCHAR (127) COLLATE UTF8MB4_UNICODE_CI  NULL DEFAULT '' COMMENT '公司LOGO',
  `INDUSTRY_TYPE` TINYINT (1)  NULL DEFAULT '0' COMMENT '行业类别ID',
  `PROVINCE_ID` INT (11)  NULL DEFAULT '0' COMMENT '省ID',
  `CITY_ID` INT (11)  NULL DEFAULT '0' COMMENT '城市ID',
  `DISTRICT_ID` INT (11)  NULL DEFAULT '0' COMMENT '区ID',
  `ADDRESS` VARCHAR (255) COLLATE UTF8MB4_UNICODE_CI  NULL DEFAULT '' COMMENT '详细地址',
  `POSTAL_CODE` VARCHAR (10) COLLATE UTF8MB4_UNICODE_CI  NULL DEFAULT '' COMMENT '邮编',
  `TEL` VARCHAR (11) COLLATE UTF8MB4_UNICODE_CI  NULL DEFAULT '' COMMENT '电话',
  `FAX` VARCHAR (50) COLLATE UTF8MB4_UNICODE_CI  NULL DEFAULT '' COMMENT '传真',
  `CONTACTOR` VARCHAR (64) COLLATE UTF8MB4_UNICODE_CI  NULL DEFAULT '' COMMENT '联系人',
  `EMPLOYEE_ID` VARCHAR (64) CHARACTER SET UTF8MB4 COLLATE UTF8MB4_BIN  NULL COMMENT '负责人员工ID',
  `POSITION` VARCHAR (64) COLLATE UTF8MB4_UNICODE_CI  NULL DEFAULT '' COMMENT '职位',
  `PHONE` VARCHAR (11) COLLATE UTF8MB4_UNICODE_CI  NULL DEFAULT '' COMMENT '手机号',
  `QQ` VARCHAR (15) COLLATE UTF8MB4_UNICODE_CI  NULL DEFAULT '' COMMENT 'QQ',
  `EMAIL` VARCHAR (127) COLLATE UTF8MB4_UNICODE_CI  NULL DEFAULT '' COMMENT '邮箱',
  `COMPANY_WEB` VARCHAR (255) COLLATE UTF8MB4_UNICODE_CI  NULL DEFAULT '' COMMENT '公司网址',
  `COMPANY_INTRODUCE` VARCHAR (255) COLLATE UTF8MB4_UNICODE_CI  NULL DEFAULT '' COMMENT '公司介绍',
  `TAXPAYER_IDENTIFY_NUM` VARCHAR (64) COLLATE UTF8MB4_UNICODE_CI  NULL DEFAULT '' COMMENT '纳税人识别号',
  `INVOICE_PAYABLE` VARCHAR (127) COLLATE UTF8MB4_UNICODE_CI  NULL DEFAULT '' COMMENT '发票抬头',
  `INVOICE_ADDRESS` VARCHAR (255) COLLATE UTF8MB4_UNICODE_CI  NULL DEFAULT '' COMMENT '（发票）地址',
  `INVOICE_TEL` VARCHAR (20) COLLATE UTF8MB4_UNICODE_CI  NULL DEFAULT '' COMMENT '（发票）电话',
  `INVOICE_OPEN_NAME` VARCHAR (127) COLLATE UTF8MB4_UNICODE_CI  NULL DEFAULT '' COMMENT '（发票）开户名称',
  `INVOICE_OPEN_BANK` VARCHAR (60) COLLATE UTF8MB4_UNICODE_CI  NULL DEFAULT '' COMMENT '（发票）开户银行',
  `INVOICE_BANK_ACCOUNT` VARCHAR (50) COLLATE UTF8MB4_UNICODE_CI  NULL DEFAULT '' COMMENT '（发票）银行账号',
  `HOTLINE_TEL` VARCHAR (11) COLLATE UTF8MB4_UNICODE_CI  NULL DEFAULT '' COMMENT '服务热线',
  `IS_DEL` TINYINT (1)  NULL DEFAULT '0' COMMENT '是否删除：0否 1是',
  `UPDATED_AT` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `CREATED_AT` TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  PRIMARY KEY (`COMPANY_ID`) USING BTREE
) ENGINE = INNODB DEFAULT CHARSET = UTF8 ROW_FORMAT = DYNAMIC COMMENT = '企业表';

-- 部门表
CREATE TABLE `ryn_department` (
  `DEPARTMENT_ID` VARCHAR (64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '主键',
  `COMPANY_ID` VARCHAR(64) NOT NULL COMMENT '所属公司ID',
  `DEPARTMENT_NAME` VARCHAR(255) DEFAULT NULL COMMENT '部门名称',
  `LEVEL` VARCHAR(4) DEFAULT NULL COMMENT '部门级别',
  `PID` VARCHAR(64) DEFAULT NULL COMMENT '父级ID',
  `UPDATED_AT` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `CREATED_AT` TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`DEPARTMENT_ID`) USING BTREE
) ENGINE=INNODB DEFAULT CHARSET=UTF8 ROW_FORMAT=DYNAMIC COMMENT='部门表';

-- 用户表
CREATE TABLE `sys_user` (
  `SYS_USER_ID` VARCHAR (64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '主键',
  `USERNAME` VARCHAR(25) DEFAULT NULL COMMENT '用户名 (同手机号码)',
  `MOBILE` VARCHAR(25) DEFAULT NULL COMMENT '手机号码',
  `PASSWORD` VARCHAR(100) DEFAULT NULL COMMENT '登录密码',
  `STATUS` VARCHAR(4) DEFAULT NULL COMMENT '用户状态(0不可用1可用)',
  `AVATAR` VARCHAR(255) DEFAULT NULL DEFAULT '' COMMENT '用户头像',
  `UNION_ID` VARCHAR(64) DEFAULT NULL DEFAULT '' COMMENT '微信UNION_ID（如果绑定了微信）',
  `OPEN_ID` VARCHAR(64) DEFAULT NULL COMMENT '微信的OPENID',
  `UPDATED_AT` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `CREATED_AT` TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`SYS_USER_ID`) USING BTREE
) ENGINE=INNODB DEFAULT CHARSET=UTF8 ROW_FORMAT=DYNAMIC COMMENT='用户表';

INSERT INTO `sys_user` VALUES ('748475026721734656', '18518215883', '18518215883', '$2a$10$/o2V0gz9GYH8ndViguxhi.LlJuj8NuE9OGEitOHEG1Oh8SBXVENfG', '1', null, null, null, '2019-10-30 16:46:16', '2019-10-30 16:46:16');
INSERT INTO `sys_user` VALUES ('749875506521833471', '18518215882', '18518215882', '$2a$10$/o2V0gz9GYH8ndViguxhi.LlJuj8NuE9OGEitOHEG1Oh8SBXVENfG', '1', null, null, null, '2019-11-06 16:16:02', '2019-11-06 18:52:44',);
INSERT INTO `sys_user` VALUES ('749875506521833472', '18518215883', '18518215883', '$2a$10$/o2V0gz9GYH8ndViguxhi.LlJuj8NuE9OGEitOHEG1Oh8SBXVENfG', '1', null, null, null, '2019-11-01 15:08:46', '2019-11-01 15:08:46',);
INSERT INTO `sys_user` VALUES ('755017058655141888', '18618965582', '18618965582', '$2a$10$/o2V0gz9GYH8ndViguxhi.LlJuj8NuE9OGEitOHEG1Oh8SBXVENfG', '1', null, null, null, '2019-11-08 17:24:07', '2019-11-08 17:24:07',);

-- 员工表
CREATE TABLE `ryn_employee` (
  `EMPLOYEE_ID` VARCHAR (64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '主键',
  `CODE` VARCHAR(64) NOT NULL COMMENT '职工编号',
  `SYS_USER_ID` VARCHAR(64) DEFAULT NULL COMMENT '用户ID',
  `NAME` VARCHAR(255) DEFAULT NULL COMMENT '职工姓名',
  `COMPANY_ID` VARCHAR(64) DEFAULT NULL COMMENT '公司ID',
  `COMPANY_NAME` VARCHAR(255) DEFAULT NULL COMMENT '公司姓名',
  `DEPARTMENT_ID` VARCHAR(64) NOT NULL DEFAULT '' COMMENT '所属部门ID',
  `EMAIL` VARCHAR(100) NOT NULL DEFAULT '' COMMENT '邮箱',
  `STATUS` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '帐号状态 0-禁用 1-正常',
  `DATA_TYPE` TINYINT(2) DEFAULT NULL COMMENT '数据权限（1=本人2=本部门及下属部门3=全部4=指定部门）',
  `DATA_DEPARTMENT_ID` VARCHAR(255) DEFAULT NULL COMMENT '可操作部门ID（多个英文逗号隔开）',
  `UPDATED_AT` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `CREATED_AT` TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`EMPLOYEE_ID`) USING BTREE
) ENGINE=INNODB DEFAULT CHARSET=UTF8 ROW_FORMAT=DYNAMIC COMMENT='员工表';

-- 员工角色表
CREATE TABLE `sys_user_role` (
  `EMPLOYEE_ID` VARCHAR(64) NOT NULL,
  `ROLE_ID` VARCHAR(64) NOT NULL,
  PRIMARY KEY (`EMPLOYEE_ID`,`ROLE_ID`) USING BTREE
) ENGINE=INNODB DEFAULT CHARSET=UTF8 ROW_FORMAT=DYNAMIC COMMENT='员工角色表';

-- 角色表
CREATE TABLE `sys_role` (
  `RID` VARCHAR (64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '主键',
  `IS_ADMIN` TINYINT(2) DEFAULT NULL COMMENT '是否为超级管理员（0=否1=是）',
  `IS_PRESET` TINYINT(2) DEFAULT NULL COMMENT '是否预设角色（0=否1=是）',
  `RNAME` VARCHAR(50) DEFAULT NULL COMMENT '角色名，用于显示',
  `RDESC` VARCHAR(100) DEFAULT NULL COMMENT '角色描述',
  `COMPANY_ID` VARCHAR(64) DEFAULT NULL COMMENT '所属公司',
  `CREATED` TIMESTAMP NULL DEFAULT NULL COMMENT '创建时间',
  `UPDATED` TIMESTAMP NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`RID`) USING BTREE
) ENGINE=INNODB DEFAULT CHARSET=UTF8 ROW_FORMAT=DYNAMIC COMMENT='角色表';

 -- 角色资源表
CREATE TABLE `sys_role_perm` (
  `ROLE_ID` VARCHAR(64) NOT NULL,
  `PERM_ID` VARCHAR(64) NOT NULL,
  PRIMARY KEY (`ROLE_ID`,`PERM_VAL`) USING BTREE
) ENGINE=INNODB DEFAULT CHARSET=UTF8 ROW_FORMAT=DYNAMIC COMMENT='角色资源表';

-- 权限表
CREATE TABLE `sys_perm` (
  `PERM_ID` VARCHAR (64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '主键',
  `RES_GROUP_SORT` TINYINT(2) DEFAULT NULL COMMENT '资源组排序',
  `RES_GROUP_NAME` VARCHAR(50) DEFAULT NULL COMMENT '资源组名称',
  `SORT` TINYINT(2) DEFAULT NULL COMMENT '同资源组下资源排序',
  `RES_NAME` VARCHAR(50) DEFAULT NULL COMMENT '资源名称',
  `RES_KEY` VARCHAR(50) DEFAULT NULL COMMENT '资源标识',
  PRIMARY KEY (`PERM_ID`) USING BTREE
)  ENGINE=INNODB DEFAULT CHARSET=UTF8 ROW_FORMAT=DYNAMIC COMMENT='权限表';

TRUNCATE TABLE `sys_perm`;
INSERT INTO `sys_perm` ( `PERM_ID`,  `RES_GROUP_SORT`, `RES_GROUP_NAME`, `SORT`,  `RES_NAME`,  `RES_KEY` )
VALUES
( '762194993925300001', '1', '应收合同', '1', '合同查看', 'contract_detail'),
( '762194993925300002', '1', '应收合同', '2', '合同导出', 'contract_export'),
( '762194993925300003', '1', '应收合同', '3', '新增合同', 'contract_create'),
( '762194993925300004', '1', '应收合同', '4', '合同删除', 'contract_delete'),
( '762194993925300005', '1', '应收合同', '5', '合同编辑', 'contract_edit'),

( '762194993925300006', '2', '跟进反馈', '1', '跟进反馈管理', 'follow_manage'),
( '762194993925300007', '2', '跟进反馈', '1', '新增跟进', 'follow_create'),

( '762194993925300008', '3', '回款管理', '1', '新增回款', 'repay_create'),
( '762194993925300009', '3', '回款管理', '1', '作废回款', 'repay_invalid'),
( '762194993925300010', '3', '回款管理', '1', '删除回款', 'repay_delete'),
( '762194993925300011', '3', '回款管理', '1', '查看回款', 'repay_detail'),

( '762194993925300012', '4', '客户管理', '1', '客户查看', 'customer_detail'),
( '762194993925300013', '4', '客户管理', '1', '客户新增', 'customer_create'),
( '762194993925300014', '4', '客户管理', '1', '客户修改', 'customer_edit'),
( '762194993925300015', '4', '客户管理', '1', '客户删除', 'customer_delete'),

( '762194993925300016', '5', '风险预警', '1', '风险监控查看', 'monitor_detail'),
( '762194993925300017', '5', '风险预警', '1', '监控名单配置', 'monitor_list_config'),
( '762194993925300018', '5', '风险预警', '1', '新增监控名单', 'monitor_create'),
( '762194993925300019', '5', '风险预警', '1', '设置监控消息通知', 'monitor_ad'),

( '762194993925300020', '6', '报表', '1', '应收统计报表', 'form_unpay'),
( '762194993925300021', '6', '报表', '1', '回款统计报表', 'form_repay'),
( '762194993925300022', '6', '报表', '1', '逾期统计报表', 'form_overdue'),

( '762194993925300023', '7', '设置', '1', '消息设置', 'config_ad'),
( '762194993925300024', '7', '设置', '1', '财务设置', 'config_finance'),
( '762194993925300025', '7', '设置', '1', '员工设置', 'config_employee'),
( '762194993925300026', '7', '设置', '1', '公司信息设置', 'config_company_info');


-- 资源URL表
CREATE TABLE `perm_url` (
  `PERM_ID` VARCHAR(64) NOT NULL,
  `URL` VARCHAR(50) NOT NULL,
 `RES_TYPE` TINYINT(2) DEFAULT NULL COMMENT '菜单类型（1.菜单 2.按钮 3.接口 4.特殊）',
  PRIMARY KEY (`PERM_ID`, `URL`) USING BTREE
) ENGINE=INNODB DEFAULT CHARSET=UTF8 ROW_FORMAT=DYNAMIC COMMENT='资源URL表';

TRUNCATE TABLE `perm_url`;
INSERT INTO `perm_url` ( `PERM_ID`,  `URL`, `RES_TYPE`)
VALUES
('762194993925300025', '/employee/list', '2'),
('762194993925300025', '/employee/updateStatus', '2'),
('762194993925300025', '/employee/edit', '2'),
('762194993925300025', '/employee/detail', '2'),

('762194993925300026', '/department/tree', '2'),
('762194993925300026', '/department/edit', '2'),
('762194993925300026', '/department/delete', '2'),
('762194993925300026', '/department/migrate', '2'),
('762194993925300026', '/role/list', '2'),
('762194993925300026', '/role/allResources', '2'),
('762194993925300026', '/role/currentResources', '2'),
('762194993925300026', '/role/edit', '2'),
('762194993925300026', '/role/delete', '2'),
('762194993925300026', '/role/nameList', '2');


