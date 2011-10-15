drop table if exists properties;
drop table if exists savedfiles;

create table properties(
	property varchar(32) not null
	,val varchar(32) not null
);

create unique index uq_properties on properties(property);

create table savedfiles(
	id identity
	,filename varchar(256) not null	-- short source name, not absolute
	,filesize bigint not null		-- File.length is Long
	,md5hash varchar(32) not null	-- 16 bytes
		
);

-- create unique index uq_savedfiles on savedfiles(filesize, md5hash);
create unique index uq_savedfiles on savedfiles(md5hash);
create index idx_savedfiles1 on savedfiles(filesize);

insert into properties (property, val) values ('version', '1');

--insert into savedfiles (filename, filesize, md5hash) values ('file1', 500000, '8d133b11a34b26ab84d4e66d5ad01e52');
--insert into savedfiles (filename, filesize, md5hash) values ('file2', 500001, '8d133b11a34b26ab84d4e66d5ad01e52');
--
--select * from properties;
--select * from savedfiles;
