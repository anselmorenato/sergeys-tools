drop table if exists properties;
drop table if exists files;

create table properties(
	property varchar(32) not null
	,val varchar(32) not null
);

create unique index uq_properties on properties(property);

create table files(
	id identity
	, absolutepath varchar(4096) not null
	, absolutedir varchar(4096) not null
	, lastmodified bigint null	-- File.lastModified is Long
	, filesize bigint not null		-- File.length is Long
	, mimetype varchar(64) not null	-- audio/mpeg
	, detectionmethod varchar(32) not null
	, hash varchar(32) -- md5 16 bytes
	, haspicture boolean not null default false
	, album varchar(2048)
	, artist varchar(1024)
);

create unique index uq_files1 on files(absolutepath);

insert into properties (property, val) values ('version', '1');
