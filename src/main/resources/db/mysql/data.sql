insert into member (member_id, email, name, member_status, oauth_type) values (default, 'testA@email.com', 'testA', 'JOIN', 'GOOGLE');
insert into member (member_id, email, name, member_status, oauth_type) values (default, 'scrumble@email.com', 'scrumble', 'JOIN', 'GOOGLE');

insert into squad (squad_id, squad_name, deleted_flag, created_at) values ( default, '테스트 스쿼드', false, now());

insert into squad_member(squad_member_id, member_id, squad_id, squad_member_role, squad_member_status, created_at) values ( default, 1, 1, 'LEADER', 'JOIN', now());
insert into squad_member(squad_member_id, member_id, squad_id, squad_member_role, squad_member_status, created_at) values ( default, 2, 1, 'NORMAL', 'JOIN', now());

insert into todo(todo_id, todo_type, contents, todo_status, todo_at, deleted_flag, member_id, created_at)
values (default, 'DAILY', '테스트 투두', 'PENDING', now(), false, 1, now());
insert into todo(todo_id, todo_type, contents, todo_status, todo_at, deleted_flag, member_id, created_at)
values (default, 'DAILY', '삭제된 투두', 'PENDING', now(), true, 1, now());
insert into todo(todo_id, todo_type, contents, todo_status, todo_at, deleted_flag, member_id, created_at)
values (default, 'DAILY', '완료된 투두', 'COMPLETED', now(), false, 1, now());

insert into squad_todo(squad_todo_id, todo_id, squad_id, deleted_flag, created_at)
values (default, 1, 1, 0, now());
insert into squad_todo(squad_todo_id, todo_id, squad_id, deleted_flag, created_at)
values (default, 2, 1, 0, now());
insert into squad_todo(squad_todo_id, todo_id, squad_id, deleted_flag, created_at)
values (default, 3, 1, 0, now());
