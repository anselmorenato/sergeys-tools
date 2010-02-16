-- create database
drop database if exists svsdb;

create database if not exists svsdb
    character set utf8; 

grant execute, select
    on svsdb.*
    to 'svsdbuser'@'127.0.0.1' identified by 'svsdbuser';