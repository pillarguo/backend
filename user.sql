show databases;
create database jwt;
use jwt;
 create table `user`(
 `id` int(11) not null auto_increment comment '主键',
 `name` varchar(50) comment '用户名',
 `password` varchar(255) comment '密码',
 `admin` int(11) not null comment '管理员',
 `nickname` varchar(50) comment '昵称',
 primary key(`id`)
 )ENGINE=InnoDB AUTO_INCREMENT=1 default charset=utf8;
 
 insert into user values(4,'zhangsan',md5('123456'),1,'Homer Simpson');
 insert into user values(5,'lisi',md5('654321'),0,'Bart');
 insert into user values(6,'wanger',md5('654321'),0,'巴尼');
 select * from user;
  create table `article`(
 `id` int(11) not null auto_increment comment '主键',
 `title` varchar(50) comment '标题',
 `content` text comment '内容',
 `author` varchar(50) not null comment '作者',
 
 primary key(`id`)
 )ENGINE=InnoDB AUTO_INCREMENT=1 default charset=utf8;
 