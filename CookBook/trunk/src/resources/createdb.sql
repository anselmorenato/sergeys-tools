drop table if exists properties;
create table properties(
    property varchar(32) not null,
    val varchar(32) not null
);

create unique index uq_properties on properties(property);

drop table if exists recipes;
create table recipes(
    id identity,
    hash varchar(64) not null,	-- sha-256 digest
    title varchar(1024),
    packedfile blob,
    filesize bigint not null
);

create unique index uq_recipes on recipes(hash);

drop table if exists tags;
create table tags(
    id identity,
    parentid bigint,	-- == identity == Long
    val varchar(128) not null
);

create unique index uq_tags on tags(val);

drop table if exists recipetags;
create table recipetags(
    recipeid bigint not null,	-- == identity == Long
    tagid bigint not null
);

create unique index uq_recipetags on recipetags(recipeid, tagid);

insert into properties (property, val) values ('version', '1');

insert into tags (val) values ('Other');

-- test
insert into recipes (id, hash, title, filesize) values (5, '123', 'five', 0);
insert into recipes (id, hash, title, filesize) values (6, '1231', '6', 0);
insert into recipes (id, hash, title, filesize) values (7, '1232', '7', 0);
insert into recipes (id, hash, title, filesize) values (8, '1233', '8', 0);

insert into tags (id, parentid, val) values (5, null, 'meat');
insert into tags (id, parentid, val) values (6, null, 'vegetable');
insert into tags (id, parentid, val) values (7, 5, 'one');
insert into tags (id, parentid, val) values (8, 6, 'two');
insert into tags (id, parentid, val) values (9, 6, 'three');

insert into recipetags (recipeid, tagid) values (5, 7);
insert into recipetags (recipeid, tagid) values (5, 9);
insert into recipetags (recipeid, tagid) values (6, 7);
insert into recipetags (recipeid, tagid) values (7, 8);
insert into recipetags (recipeid, tagid) values (8, 9);
insert into recipetags (recipeid, tagid) values (8, 7);