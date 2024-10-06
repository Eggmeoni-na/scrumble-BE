insert into member (member_id, email, name, member_status, oauth_type)
values (default, 'testA@email.com', 'testA', 'JOIN', 'GOOGLE');
insert into member (member_id, email, name, member_status, oauth_type)
values (default, 'scrumble@email.com', 'scrumble', 'JOIN', 'GOOGLE');

insert into squad (squad_id, squad_name, deleted_flag, created_at)
values (default, '테스트 스쿼드', false, now());

insert into squad_member(squad_member_id, member_id, squad_id, squad_member_role, squad_member_status, created_at)
values (default, 1, 1, 'LEADER', 'JOIN', now());
insert into squad_member(squad_member_id, member_id, squad_id, squad_member_role, squad_member_status, created_at)
values (default, 2, 1, 'NORMAL', 'JOIN', now());

insert into todo(todo_id, todo_type, contents, todo_status, todo_at, deleted_flag, member_id, created_at)
values (default, 'DAILY', '테스트 투두', 'PENDING', now(), false, 1, now());
insert into todo(todo_id, todo_type, contents, todo_status, todo_at, deleted_flag, member_id, created_at)
values (default, 'DAILY', '삭제된 투두', 'PENDING', now(), true, 1, now());
insert into todo(todo_id, todo_type, contents, todo_status, todo_at, deleted_flag, member_id, created_at)
values (default, 'DAILY', 'CS', 'COMPLETED', now(), false, 1, now());
insert into todo(todo_id, todo_type, contents, todo_status, todo_at, deleted_flag, member_id, created_at)
values (default, 'DAILY', '프로젝트', 'PENDING', now(), false, 1, now());
insert into todo(todo_id, todo_type, contents, todo_status, todo_at, deleted_flag, member_id, created_at)
values (default, 'DAILY', '알고리즘', 'PENDING', now(), false, 1, now());
insert into todo(todo_id, todo_type, contents, todo_status, todo_at, deleted_flag, member_id, created_at)
values (default, 'DAILY', '알고리즘', 'PENDING', DATEADD('DAY', -1, NOW()), false, 1, now());

insert into squad_todo(squad_todo_id, todo_id, squad_id, deleted_flag, created_at)
values (default, 1, 1, 0, now());
insert into squad_todo(squad_todo_id, todo_id, squad_id, deleted_flag, created_at)
values (default, 2, 1, 0, now());
insert into squad_todo(squad_todo_id, todo_id, squad_id, deleted_flag, created_at)
values (default, 3, 1, 0, now());
insert into squad_todo(squad_todo_id, todo_id, squad_id, deleted_flag, created_at)
values (default, 4, 1, 0, now());
insert into squad_todo(squad_todo_id, todo_id, squad_id, deleted_flag, created_at)
values (default, 5, 1, 0, now());
insert into squad_todo(squad_todo_id, todo_id, squad_id, deleted_flag, created_at)
values (default, 6, 1, 0, now());
