insert into member (member_id, email, name, member_status, oauth_type) values (default, 'testA@email.com', 'testA', 'JOIN', 'GOOGLE');
insert into member (member_id, email, name, member_status, oauth_type) values (default, 'scrumble@email.com', 'scrumble', 'JOIN', 'GOOGLE');

insert into squad (squad_id, squad_name, deleted_flag, created_at) values ( default, '테스트 스쿼드', false, now());

insert into membership(membership_id, member_id, squad_id, membership_role, membership_status, created_at) values ( default, 1, 1, 'LEADER', 'JOIN', now());
insert into membership(membership_id, member_id, squad_id, membership_role, membership_status, created_at) values ( default, 2, 1, 'NORMAL', 'JOIN', now());
