DROP TABLE IF EXISTS notification;

CREATE TABLE notification
(
    notification_id     BIGINT      NOT NULL AUTO_INCREMENT,
    recipient_id        BIGINT      NOT NULL,
    notification_type   VARCHAR(50) NOT NULL,
    read_flag           TINYINT     NOT NULL,
    notification_data   CLOB        NULL,
    notification_status VARCHAR(50) NOT NULL,
    created_at          TIMESTAMP   NULL,
    updated_at          TIMESTAMP   NULL,
    PRIMARY KEY (notification_id)
);

DROP TABLE IF EXISTS squad_todo;

CREATE TABLE squad_todo
(
    squad_todo_id bigint   NOT NULL AUTO_INCREMENT PRIMARY KEY,
    todo_id       bigint   NOT NULL,
    squad_id      bigint   NOT NULL,
    deleted_flag  tinyint  NOT NULL,
    created_at    datetime NULL,
    updated_at    datetime NULL
);


DROP TABLE IF EXISTS squad_member;

CREATE TABLE squad_member
(
    squad_member_id     bigint       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id           bigint       NOT NULL,
    squad_id            bigint       NOT NULL,
    squad_member_status nvarchar(50) NOT NULL DEFAULT 'INVITING',
    squad_member_role   nvarchar(50) NOT NULL DEFAULT 'NORMAL',
    created_at          datetime     NULL,
    updated_at          datetime     NULL
);

DROP TABLE IF EXISTS todo;

CREATE TABLE todo
(
    todo_id      bigint      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    todo_type    varchar(50) NOT NULL,
    contents     text        NOT NULL,
    todo_status  varchar(50) NOT NULL,
    todo_at      date        NOT NULL COMMENT '투두 실행일',
    deleted_flag tinyint     NOT NULL DEFAULT 0,
    member_id    bigint      NOT NULL COMMENT '작성자',
    created_at   datetime    NULL,
    updated_at   datetime    NULL
);

DROP TABLE IF EXISTS member;

CREATE TABLE member
(
    member_id     bigint       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email         varchar(100) NOT NULL,
    member_status varchar(50)  NOT NULL DEFAULT 'JOIN',
    name          varchar(100) NULL,
    profile_image varchar(100) NULL,
    joined_at     datetime     NOT NULL DEFAULT now(),
    leaved_at     datetime     NULL,
    created_at    datetime     NULL,
    updated_at    datetime     NULL,
    oauth_type    varchar(50)  NULL,
    oauth_id      varchar(100) NULL
);

DROP TABLE IF EXISTS squad;

CREATE TABLE squad
(
    squad_id     bigint       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    squad_name   varchar(100) NOT NULL,
    deleted_flag tinyint      NOT NULL DEFAULT 0,
    created_at   datetime     NULL,
    updated_at   datetime     NULL
);


ALTER TABLE notification
    ADD CONSTRAINT FK_member_TO_notification_1 FOREIGN KEY (recipient_id)
        REFERENCES member (member_id);

ALTER TABLE squad_member
    ADD CONSTRAINT FK_member_TO_squad_member_1 FOREIGN KEY (member_id)
        REFERENCES member (member_id);

ALTER TABLE squad_member
    ADD CONSTRAINT FK_squad_TO_squad_member_1 FOREIGN KEY (squad_id)
        REFERENCES squad (squad_id);

ALTER TABLE todo
    ADD CONSTRAINT FK_member_TO_todo_1 FOREIGN KEY (member_id)
        REFERENCES member (member_id);

ALTER TABLE squad_todo
    ADD CONSTRAINT FK_todo_TO_squad_todo_1 FOREIGN KEY (todo_id)
        REFERENCES todo (todo_id);

ALTER TABLE squad_todo
    ADD CONSTRAINT FK_squad_TO_squad_todo_1 FOREIGN KEY (squad_id)
        REFERENCES squad (squad_id);
