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

DROP TABLE IF EXISTS squad_member;

CREATE TABLE squad_member
(
    squad_member_id     BIGINT      NOT NULL AUTO_INCREMENT,
    member_id         BIGINT      NOT NULL,
    squad_id          BIGINT      NOT NULL,
    squad_member_status VARCHAR(50) NOT NULL DEFAULT 'INVITING',
    squad_member_role   VARCHAR(50) NOT NULL DEFAULT 'NORMAL',
    created_at        TIMESTAMP,
    updated_at        TIMESTAMP,
    PRIMARY KEY (squad_member_id)
);

DROP TABLE IF EXISTS squad;

CREATE TABLE squad
(
    squad_id     BIGINT       NOT NULL AUTO_INCREMENT,
    squad_name   VARCHAR(100) NOT NULL,
    deleted_flag TINYINT      NOT NULL DEFAULT 0,
    created_at   TIMESTAMP,
    updated_at   TIMESTAMP,
    PRIMARY KEY (squad_id)
);
