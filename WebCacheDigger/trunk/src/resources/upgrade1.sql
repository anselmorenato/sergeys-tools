-- upgrade version 1

alter table files
	add ignored boolean not null default false;

update properties set val='2' where property='version';