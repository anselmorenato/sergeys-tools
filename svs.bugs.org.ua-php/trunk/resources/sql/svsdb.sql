use svsdb;

drop table if exists visit;
create table visit(
    visittime timestamp
    , http_referer varchar(128)
    , http_user_agent varchar(256)
    , http_via varchar(128)
    , http_x_forwarded_for varchar(64)
    , remote_addr varchar(15)
    , remote_host varchar(64)
    , request_uri varchar(128)
);