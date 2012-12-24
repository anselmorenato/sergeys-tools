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
