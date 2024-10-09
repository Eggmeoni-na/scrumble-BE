DROP TABLE IF EXISTS squad_member;

CREATE TABLE squad_member
(
    squad_member_id     BIGINT      NOT NULL AUTO_INCREMENT,
    member_id           BIGINT      NOT NULL,
    squad_id            BIGINT      NOT NULL,
    squad_member_status VARCHAR(50) NOT NULL DEFAULT 'INVITING',
    squad_member_role   VARCHAR(50) NOT NULL DEFAULT 'NORMAL',
    created_at          TIMESTAMP,
    updated_at          TIMESTAMP,
    PRIMARY KEY (squad_member_id)
);

DROP TABLE IF EXISTS squad_todo;

CREATE TABLE squad_todo
(
    squad_todo_id BIGINT    NOT NULL AUTO_INCREMENT,
    todo_id       BIGINT    NOT NULL,
    squad_id      BIGINT    NOT NULL,
    deleted_flag  TINYINT   NOT NULL,
    created_at    TIMESTAMP NULL,
    updated_at    TIMESTAMP NULL,
    PRIMARY KEY (squad_todo_id)
);

DROP TABLE IF EXISTS todo;

CREATE TABLE todo
(
    todo_id      BIGINT      NOT NULL AUTO_INCREMENT,
    todo_type    VARCHAR(50) NOT NULL,
    contents     CLOB        NOT NULL,
    todo_status  VARCHAR(50) NOT NULL,
    todo_at      DATE        NOT NULL,
    deleted_flag TINYINT     NOT NULL,
    member_id    BIGINT      NOT NULL,
    created_at   TIMESTAMP   NULL,
    updated_at   TIMESTAMP   NULL,
    PRIMARY KEY (todo_id)
);

DROP TABLE IF EXISTS squad;

CREATE TABLE squad
(
    squad_id     BIGINT       NOT NULL AUTO_INCREMENT,
    squad_name   VARCHAR(100) NOT NULL,
    deleted_flag TINYINT      NOT NULL,
    created_at   TIMESTAMP,
    updated_at   TIMESTAMP,
    PRIMARY KEY (squad_id)
);

DROP TABLE IF EXISTS member;

CREATE TABLE member
(
    member_id     BIGINT AUTO_INCREMENT NOT NULL,
    email         VARCHAR(100)          NOT NULL,
    member_status VARCHAR(50)           NOT NULL DEFAULT 'JOIN',
    name          VARCHAR(100),
    profile_image VARCHAR(100),
    joined_at     TIMESTAMP             NOT NULL DEFAULT CURRENT_TIMESTAMP,
    leaved_at     TIMESTAMP,
    created_at    TIMESTAMP,
    updated_at    TIMESTAMP,
    oauth_type    VARCHAR(50),
    oauth_id      VARCHAR(100),
    PRIMARY KEY (member_id)
);

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

CREATE UNIQUE INDEX idx_member_squad ON squad_member (member_id, squad_id);
