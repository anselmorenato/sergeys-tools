use svsdb;

drop table if exists testtbl;

create table testtbl(
    id int unsigned not null auto_increment
    , txt varchar(256)
    
    , primary key(id)
);

insert into testtbl (txt) values ('junk text');    
insert into testtbl (txt) values ('another text');
