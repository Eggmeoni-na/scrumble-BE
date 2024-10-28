insert into member (member_id, email, name, member_status, oauth_type) values (default, 'testA@email.com', 'testA', 'JOIN', 'GOOGLE');
insert into member (member_id, email, name, member_status, oauth_type) values (default, 'scrumble@email.com', 'scrumble', 'JOIN', 'GOOGLE');
insert into member (member_id, email, name, member_status, oauth_type) values (default, 'testB@email.com', '테스트유저', 'JOIN', 'GOOGLE');


insert into squad (squad_id, squad_name, deleted_flag, created_at) values ( default, '테스트 스쿼드', false, now());

insert into squad_member(squad_member_id, member_id, squad_id, squad_member_role, squad_member_status, created_at) values ( default, 1, 1, 'LEADER', 'JOIN', now());
insert into squad_member(squad_member_id, member_id, squad_id, squad_member_role, squad_member_status, created_at) values ( default, 2, 1, 'NORMAL', 'JOIN', now());
insert into squad_member(squad_member_id, member_id, squad_id, squad_member_role, squad_member_status, created_at) values (default, 3, 1, 'NORMAL', 'INVITING', now());

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

insert into notification(notification_id, recipient_id, notification_type, read_flag, notification_data, created_at, updated_at)
values (default, 2, 'INVITE_REQUEST', true, '{"userName": "testA", "squadName": "테스트 스쿼드", "squadId": "1"}', now(), null);
insert into notification(notification_id, recipient_id, notification_type, read_flag, notification_data, created_at, updated_at)
values (default, 1, 'INVITE_ACCEPT', true, '{"userName": "scrumble", "squadName": "테스트 스쿼드"}', now(), null);
