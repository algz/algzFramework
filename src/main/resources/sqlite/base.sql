-- 1.用户表
CREATE TABLE 'A_USERS' (
'USERID'  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
'USERNAME'  TEXT(50), --用户名
'PASSWORD'  TEXT(50), --密码
'ENABLED'  INTEGER,  --是否启用,有效1,无效0
'CREDENTIALSNONEXPIRED'  INTEGER,
'ACCOUNTNONEXPIRED'  INTEGER,  --是否过期, 有效1,无效0
'ACCOUNTNONLOCKED'  INTEGER  --是否被锁定, 有效1,无效0
);

-- 2.角色表
CREATE TABLE 'A_AUTOINCREMENT' (
'ROLEID'  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
'ROLENAME'  TEXT(50),
'ENABLED'  INTEGER
);
 
-- 3 用户_角色表
CREATE TABLE 'A_USERS_ROLES' (
'USER_ROLE_ID'  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
'USERID'  INTEGER,
'ROLEID'  INTEGER,
CONSTRAINT 'FK_UR_U' FOREIGN KEY ('USERID') REFERENCES 'A_USERS' ('USERID'),
CONSTRAINT 'FK_UR_R' FOREIGN KEY ('ROLEID') REFERENCES 'A_ROLES' ('ROLESID')
);
 
   4.资源表resources
   CREATE TABLE `resources` (
     `memo` varchar(255) default NULL,
     -- 权限所对应的url地址
     `url` varchar(255) default NULL,
     --优先权
     `priority` int(11) default NULL,
     --类型
     `type` int(11) default NULL,
     --权限所对应的编码，例201代表发表文章
     `name` varchar(255) default NULL,
     `id` int(11) NOT NULL auto_increment,
     PRIMARY KEY  (`id`)
   )
 
   5.角色_资源表roles_resources
    CREATE TABLE `roles_resources` (
      `rsid` int(11) default NULL,
      `rid` int(11) default NULL,
      `rrId` int(11) NOT NULL auto_increment,
      PRIMARY KEY  (`rrId`),
      KEY `rid` (`rid`),
      KEY `roles_resources_ibfk_2` (`rsid`),
      CONSTRAINT `roles_resources_ibfk_2` FOREIGN KEY (`rsid`) REFERENCES `resources` (`id`),
      CONSTRAINT `roles_resources_ibfk_1` FOREIGN KEY (`rid`) REFERENCES `roles` (`id`)
      )