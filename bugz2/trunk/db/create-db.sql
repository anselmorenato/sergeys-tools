-- create database
drop database if exists svsdb;

create database if not exists svsdb
    character set utf8; 

grant execute, select, insert
    on svsdb.*
    to 'svsdbuser'@'127.0.0.1' identified by 'svsdbuser';
grant execute, select, insert
    on svsdb.*
    to 'svsdbuser'@'localhost' identified by 'svsdbuser';    